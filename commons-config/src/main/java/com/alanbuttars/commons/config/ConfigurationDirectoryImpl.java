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
package com.alanbuttars.commons.config;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.alanbuttars.commons.config.event.DirectoryFileEvent;
import com.alanbuttars.commons.config.event.FileEvent;
import com.alanbuttars.commons.config.eventbus.EventBus;

/**
 * {@link Configuration} implementation used for directories. Instances of this class maintain a mapping of the relative
 * file path to the directory, like so:
 * 
 * <pre>
 * EventBus eventBus = new EventBusSyncImpl();
 * Watch watch = Watch.config().withEventBus(eventBus);
 * 
 * // watches the directory specified in the master YAML under the 'clients' file entry
 * ConfigurationDirectoryImpl config = watch.directory("clients");
 * 
 * // grabs watchedDirectory/apple.properties
 * File appleFile = config.getValue().get("apple.properties");
 * 
 * // grabs watchedDirectory/google.properties
 * File googleFile = config.getValue().get("google.properties");
 * 
 * // grabs watchedDirectory/paypal/venmo.properties
 * File venmoFile = config.getValue().get("paypal/venmo.properties");
 * 
 * // grabs watchedDirectory/paypal/braintree.properties
 * File braintreeFile = config.getValue().get("paypal/braintree.properties");
 * </pre>
 * 
 * @author Alan Buttars
 * 
 */
public class ConfigurationDirectoryImpl extends ConfigurationAbstractImpl<Map<String, File>> {

	public ConfigurationDirectoryImpl(String sourceId, File configFile, EventBus eventBus) throws IOException {
		super(sourceId, eventBus);
		init(configFile);
	}

	@Override
	public void onFileEvent(FileEvent fileEvent) throws IOException {
		if (fileEvent instanceof DirectoryFileEvent) {
			DirectoryFileEvent directoryFileEvent = (DirectoryFileEvent) fileEvent;
			if (getSourceId().equals(directoryFileEvent.getSourceId())) {
				onFileEventMatch(directoryFileEvent.getDirectory(), directoryFileEvent.getFileEventType());
			}
		}
	}

	@Override
	protected void whenConfigFileDeleted(File configFile) throws IOException {
		reload(configFile);
	}

	@Override
	public Map<String, File> load(File configFile) throws IOException {
		return load(configFile, configFile, new HashMap<String, File>());
	}

	/**
	 * Recursively loads files under the configured directory mapped by their relative path names.
	 */
	private Map<String, File> load(File root, File currentFile, Map<String, File> files) {
		if (currentFile.exists()) {
			if (currentFile.isFile()) {
				String rootFilePath = root.getAbsolutePath();
				String currentFilePath = currentFile.getAbsolutePath();
				String alias = currentFilePath.replaceFirst(rootFilePath + "/", "");
				files.put(alias, currentFile);
			}
			else {
				for (File file : currentFile.listFiles()) {
					load(root, file, files);
				}
			}
		}
		return files;
	}

}
