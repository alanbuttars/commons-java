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

import javax.xml.bind.JAXBException;

import com.alanbuttars.commons.config.Configuration;
import com.alanbuttars.commons.config.ConfigurationDirectoryImpl;
import com.alanbuttars.commons.config.ConfigurationJsonImpl;
import com.alanbuttars.commons.config.ConfigurationPropertiesImpl;
import com.alanbuttars.commons.config.ConfigurationXmlImpl;
import com.alanbuttars.commons.config.ConfigurationYamlImpl;
import com.alanbuttars.commons.util.annotations.VisibleForTesting;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;

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
 * 		Watch watch = Watch.config();
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
 * 		Watch watch = Watch.config("/path/to/commons.config.yml");
 * 	}
 * }
 * </pre>
 * 
 * </li>
 * </ol>
 * 
 * <p>
 * Once you have created your singleton {@link Watch}, you can use valueTypeit to create {@link Configuration} objects.
 * See <a href=
 * "https://github.com/alanbuttars/commons-java/wiki/commons-config#configuration">https://github.com/alanbuttars/commons-java/wiki/commons-config#configuration</a>.
 * 
 * @author Alan Buttars
 *
 */
public class Watch {

	private final YamlConfig config;

	private Watch(File yamlFile) throws IOException {
		YAMLFactory yamlFactory = new YAMLFactory();
		ObjectMapper objectMapper = new ObjectMapper(yamlFactory);
		this.config = objectMapper.readValue(yamlFile, YamlConfig.class);
	}

	/**
	 * Creates an instance of this class, using the system property <code>commons.config</code>.
	 * 
	 * @throws IOException
	 *             On I/O error while reading the configuration YAML
	 * @throws IllegalArgumentException
	 *             If the system property points to an invalid file
	 */
	public static Watch config() throws IOException {
		String yamlFilePath = System.getProperty("commons.config");
		return config(yamlFilePath, "System property for commons.config");
	}

	/**
	 * Creates an instance of this class, using the given file path.
	 * 
	 * @param yamlFilePath
	 *            Non-null file path of the configuration YAML
	 * @throws IOException
	 *             On I/O error while reading the configuration YAML
	 * @throws IllegalArgumentException
	 *             If the file path points to an invalid file
	 */
	public static Watch config(String yamlFilePath) throws IOException {
		return config(yamlFilePath, "YAML file path");
	}

	private static Watch config(String yamlFilePath, String paramName) throws IOException {
		verifyNonNull(yamlFilePath, paramName + " must be non-null");
		verifyNonEmpty(yamlFilePath, paramName + " must be non-empty");
		File yamlFile = new File(yamlFilePath);
		verify(yamlFile.exists(), paramName + " " + yamlFile.getAbsolutePath() + " does not exist");
		verify(!yamlFile.isDirectory(), paramName + " " + yamlFile.getAbsolutePath() + " is a directory; it must be a file");
		verify(yamlFile.canRead(), paramName + " " + yamlFile.getAbsolutePath() + " is not readable");

		return new Watch(yamlFile);
	}

	public ConfigurationPropertiesImpl properties(String sourceId) throws IOException {
		return new ConfigurationPropertiesImpl(getFile(sourceId));
	}

	public <T> ConfigurationJsonImpl<T> json(String sourceId, Class<T> clazz) throws IOException {
		return new ConfigurationJsonImpl<T>(getFile(sourceId), clazz);
	}

	public <C> ConfigurationJsonImpl<C> json(String sourceId, TypeReference<C> typeReference) throws IOException {
		return new ConfigurationJsonImpl<>(getFile(sourceId), typeReference);
	}

	public <T> ConfigurationXmlImpl<T> xml(String sourceId, Class<T> clazz) throws IOException, JAXBException {
		return new ConfigurationXmlImpl<>(getFile(sourceId), clazz);
	}

	public <T> ConfigurationYamlImpl<T> yaml(String sourceId, Class<T> clazz) throws IOException {
		return new ConfigurationYamlImpl<>(getFile(sourceId), clazz);
	}

	public <C> ConfigurationYamlImpl<C> yaml(String sourceId, TypeReference<C> typeReference) throws IOException {
		return new ConfigurationYamlImpl<>(getFile(sourceId), typeReference);
	}

	public ConfigurationDirectoryImpl directory(String sourceId) throws IOException {
		return new ConfigurationDirectoryImpl(getFile(sourceId));
	}

	@VisibleForTesting
	protected YamlConfig getConfig() {
		return config;
	}

	private File getFile(String sourceId) {
		verifyNonNull(sourceId, "Source ID must be non-null");
		verifyNonEmpty(sourceId, "Source ID must be non-empty");

		YamlConfigFile configFile = config.getConfigFiles().get(sourceId);
		verifyNonNull(configFile, "Configuration does not exist for source ID '" + sourceId + "'");

		String filePath = configFile.getFile();
		verifyNonEmpty(filePath, "Configuration for source ID '" + sourceId + "' is missing the file attribute");

		File file = new File(filePath);
		verify(file.exists(), "Configuration for source ID '" + sourceId + "' has file attribute '" + filePath + "', which does not exist");
		verify(file.canRead(), "Configuration for source ID '" + sourceId + "' has file attribute '" + filePath + "', which is unreadable");

		return file;
	}

}
