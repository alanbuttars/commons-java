/*
 * Copyright (C) Alan Buttars
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.alanbuttars.commons.config.stub;

import static com.alanbuttars.commons.util.validators.Arguments.verify;
import static com.alanbuttars.commons.util.validators.Arguments.verifyNonEmpty;
import static com.alanbuttars.commons.util.validators.Arguments.verifyNonNull;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;

import com.alanbuttars.commons.config.Configuration;
import com.alanbuttars.commons.config.ConfigurationDirectoryImpl;
import com.alanbuttars.commons.config.ConfigurationPropertiesImpl;
import com.alanbuttars.commons.config.ConfigurationYamlImpl;
import com.alanbuttars.commons.config.event.DirectoryFileEvent;
import com.alanbuttars.commons.config.event.FileEvent;
import com.alanbuttars.commons.config.event.FileEventType;
import com.alanbuttars.commons.config.eventbus.EventBus;
import com.alanbuttars.commons.config.master.YamlConfig;
import com.alanbuttars.commons.config.master.YamlConfigValidator;
import com.alanbuttars.commons.config.master.YamlFileConfig;
import com.alanbuttars.commons.config.poll.DirectoryPoll;
import com.alanbuttars.commons.config.poll.FilePoll;
import com.alanbuttars.commons.util.annotations.VisibleForTesting;

/**
 * <p>
 * Models the entrypoint to this module. At application startup, a single instance of {@link Watch} should be created
 * and subsequently used to (1) register configuration files to be watched, and (2) create configuration objects which
 * will encompass those files.
 * </p>
 * <p>
 * The singleton instance of {@link Watch} can be constructed in two ways, both of which require a configuration YAML
 * (see <a href=
 * "https://github.com/alanbuttars/commons-java/wiki/commons-config#commonsconfigyml">https://github.com/alanbuttars/commons-java/wiki/commons-config#commonsconfigyml</a>.
 * <ol>
 * <li>Deploy your application with a system property pointing to the YAML</li>
 * 
 * <pre>
 * java -Dcommons.config=/path/to/commons.config.yml -jar MyApplication.jar
 * </pre>
 * 
 * Then in your main class:
 * 
 * <pre>
 * public class MyApplication {
 * 	public static void main(String[] args) {
 * 		EventBus eventBus = new EventBusSyncImpl();
 * 		Watch watch = Watch.config().withEventBus(eventBus);
 * 	}
 * }
 * </pre>
 * 
 * </li>
 * 
 * <li>Pass the YAML file path directly in your main class:
 * 
 * <pre>
 * public class MyApplication {
 * 	public static void main(String[] args) {
 * 		EventBus eventBus = new EventBusAsyncImpl();
 * 		Watch watch = Watch.config("/path/to/commons.config.yml").withEventBus(eventBus);
 * 	}
 * }
 * </pre>
 * 
 * </li>
 * </ol>
 * 
 * <p>
 * Once you have created your singleton {@link Watch}, you can use it to create {@link Configuration} objects. See
 * <a href=
 * "https://github.com/alanbuttars/commons-java/wiki/commons-config#configuration">https://github.com/alanbuttars/commons-java/wiki/commons-config#configuration</a>.
 * 
 * <pre>
 * public class MyApplication {
 * 
 * 	private static ConfigurationPropertiesImpl database;
 * 	private static ConfigurationJsonImpl<User> admin;
 * 
 * 	public static void main(String[] args) {
 * 		EventBus eventBus = new EventBusSyncImpl();
 * 		Watch watch = Watch.config().withEventBus(eventBus);
 * 
 * 		database = watch.properties("database");
 * 		admin = watch.json("admin").mappedTo(User.class);
 * 	}
 * }
 * </pre>
 * 
 * @author Alan Buttars
 *
 */
public class Watch extends ConfigurationYamlImpl<YamlConfig> {

	protected static final String SOURCE_ID = "master-yaml";
	private ScheduledThreadPoolExecutor executor;
	private List<ScheduledFuture<?>> futures;

	Watch(File configFile, EventBus eventBus, Class<YamlConfig> clazz) throws IOException {
		super(SOURCE_ID, configFile, eventBus, clazz);
		YamlConfigValidator.validate(getValue());
		this.executor = new ScheduledThreadPoolExecutor(getValue().getMaster().getPoolSize());
		this.futures = new ArrayList<>();
		scheduleExecutor(configFile);
	}

	/**
	 * Prevents repeated tasks from being submitted to the {@link #executor}. If the YAML's
	 * {@link YamlConfig#getPoolSize()} has been changed, this method will also entirely reinitialize the
	 * {@link #executor}.
	 */
	private void flushExecutor() {
		getExecutor().shutdownNow();
		executor = new ScheduledThreadPoolExecutor(getValue().getMaster().getPoolSize());
	}

	/**
	 * Using the master YAML configuration, submits a repeating {@link Runnable} file poll for each configuration file
	 * and the master YAML.
	 */
	private void scheduleExecutor(File configFile) {
		Runnable masterRunnable = new FilePoll(getSourceId(), configFile, getEventBus());
		ScheduledFuture<?> masterFuture = getExecutor().scheduleAtFixedRate(//
				masterRunnable, //
				getValue().getMaster().getPollEvery(), //
				getValue().getMaster().getPollEvery(), //
				getValue().getMaster().getPollEveryUnit());
		futures.add(masterFuture);

		for (Entry<String, YamlFileConfig> fileConfigEntry : getValue().getFileConfigs().entrySet()) {
			String sourceId = fileConfigEntry.getKey();
			YamlFileConfig fileConfig = fileConfigEntry.getValue();
			Runnable runnable = createPoll(sourceId, fileConfig);
			ScheduledFuture<?> future = getExecutor().scheduleAtFixedRate(//
					runnable, //
					fileConfig.getPollEvery(), //
					fileConfig.getPollEvery(), //
					fileConfig.getPollEveryUnit());
			futures.add(future);
		}
	}

	/**
	 * Creates the runnable appropriate for the given file config.
	 */
	private Runnable createPoll(String sourceId, YamlFileConfig fileConfig) {
		File file = new File(fileConfig.getFile());
		if (file.isFile()) {
			return new FilePoll(sourceId, file, getEventBus());
		}
		else {
			return new DirectoryPoll(sourceId, file, getEventBus());
		}
	}

	/**
	 * Creates an instance of this class, using the system property <code>commons.config</code>.
	 * 
	 * @throws IllegalArgumentException
	 *             If the system property points to an invalid file
	 */
	public static WatchStub config() {
		String yamlFilePath = System.getProperty("commons.config");
		return config(yamlFilePath, "System property for commons.config");
	}

	/**
	 * Creates an instance of this class, using the given file path.
	 * 
	 * @param yamlFilePath
	 *            Non-null file path of the configuration YAML
	 * @throws IllegalArgumentException
	 *             If the file path points to an invalid file
	 */
	public static WatchStub config(String yamlFilePath) {
		return config(yamlFilePath, "YAML file path");
	}

	private static WatchStub config(String yamlFilePath, String paramName) {
		verifyNonNull(yamlFilePath, paramName + " must be non-null");
		verifyNonEmpty(yamlFilePath, paramName + " must be non-empty");
		File yamlFile = new File(yamlFilePath);
		verify(yamlFile.exists(), paramName + " " + yamlFile.getAbsolutePath() + " does not exist");
		verify(!yamlFile.isDirectory(), paramName + " " + yamlFile.getAbsolutePath() + " is a directory; it must be a file");
		verify(yamlFile.canRead(), paramName + " " + yamlFile.getAbsolutePath() + " is not readable");

		return new WatchStub(yamlFile);
	}

	/**
	 * Creates a configuration object from a JSON file.
	 * 
	 * @param sourceId
	 *            Non-null source ID from a JSON file entry in the master configuration YAML
	 * @return The configuration stubbing object
	 */
	public WatchFileJsonImplStub json(String sourceId) {
		return new WatchFileJsonImplStub(sourceId, getFile(sourceId, false), getEventBus());
	}

	/**
	 * Creates a configuration object from a properties file.
	 * 
	 * @param sourceId
	 *            Non-null source ID from a properties file entry in the master configuration YAML
	 * @return The configuration object
	 * @throws IOException
	 *             On I/O parsing the properties file
	 */
	public ConfigurationPropertiesImpl properties(String sourceId) throws IOException {
		return new ConfigurationPropertiesImpl(sourceId, getFile(sourceId, false), getEventBus());
	}

	/**
	 * Creates a configuration object from a XML file.
	 * 
	 * @param sourceId
	 *            Non-null source ID from a XML file entry in the master configuration YAML
	 * @return The configuration stubbing object
	 */
	public WatchFileXmlImplStub xml(String sourceId) {
		return new WatchFileXmlImplStub(sourceId, getFile(sourceId, false), getEventBus());
	}

	/**
	 * Creates a configuration object from a YAML file.
	 * 
	 * @param sourceId
	 *            Non-null source ID from a YAML file entry in the master configuration YAML
	 * @return The configuration stubbing object
	 */
	public WatchFileYamlImplStub yaml(String sourceId) {
		return new WatchFileYamlImplStub(sourceId, getFile(sourceId, false), getEventBus());
	}

	/**
	 * Creates a configuration object from a directory.
	 * 
	 * @param sourceId
	 *            Non-null source ID from a directory file entry in the master configuration YAML
	 * @return The configuration object
	 * @throws IOException
	 *             On I/O parsing the directory
	 */
	public ConfigurationDirectoryImpl directory(String sourceId) throws IOException {
		return new ConfigurationDirectoryImpl(sourceId, getFile(sourceId, true), getEventBus());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void reload(File configFile) throws IOException {
		super.reload(configFile);
		for (Entry<String, YamlFileConfig> fileConfigEntry : getValue().getFileConfigs().entrySet()) {
			String sourceId = fileConfigEntry.getKey();
			YamlFileConfig fileConfig = fileConfigEntry.getValue();
			if (new File(fileConfig.getFile()).isDirectory()) {
				getEventBus().publish(new DirectoryFileEvent(sourceId, new File(fileConfig.getFile()), new File(fileConfig.getFile()), FileEventType.UPDATED));
			}
			else {
				getEventBus().publish(new FileEvent(sourceId, new File(fileConfig.getFile()), FileEventType.UPDATED));
			}
		}
		flushExecutor();
		scheduleExecutor(configFile);
	}

	@VisibleForTesting
	protected ScheduledThreadPoolExecutor getExecutor() {
		return executor;
	}

	private File getFile(String sourceId, boolean isDirectory) {
		verifyNonNull(sourceId, "Source ID must be non-null");
		verifyNonEmpty(sourceId, "Source ID must be non-empty");

		YamlFileConfig configFile = getValue().getFileConfigs().get(sourceId);
		verifyNonNull(configFile, "Configuration does not exist for source ID '" + sourceId + "'");

		String filePath = configFile.getFile();
		File file = new File(filePath);
		if (isDirectory) {
			verify(file.isDirectory(), "Configuration with source ID '" + sourceId + "' is associated with the file '" + file.getAbsolutePath() + "'; it must point to a directory");
		}
		else {
			verify(file.isFile(), "Configuration with source ID '" + sourceId + "' is associated with the directory '" + file.getAbsolutePath() + "'; it must point to a file");
		}
		return file;
	}

}
