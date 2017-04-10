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

import com.alanbuttars.commons.config.event.FileEvent;
import com.alanbuttars.commons.config.event.FileEventType;
import com.alanbuttars.commons.config.eventbus.EventBus;
import com.alanbuttars.commons.util.annotations.VisibleForTesting;
import com.google.common.eventbus.Subscribe;

/**
 * Abstract implementation of {@link Configuration} which defines the logic by which event subscription works.
 * 
 * 
 * Extending classes must call {@link #init(File)} in the last call of the constructor. For example:
 * 
 * <pre>
 * class ConfigurationCustomImpl implements Configuration&lt;User&gt; {
 * 
 * 	private final UserDao userDao;
 * 
 * 	public ConfigurationCustomImpl(String sourceId, File configFile, EventBus eventBus, UserDao userDao) {
 * 		super(sourceId, eventBus);
 * 		this.userDao = userDao;
 * 		init(configFile);
 * 	}
 * 
 * 	&#64;Override
 * 	public User load(File configFile) {
 * 		String username = readAdminUsernameFromFile(configFile);
 * 		return new UserDao().getUser(username);
 * 	}
 * }
 * </pre>
 * 
 * @author Alan Buttars
 *
 * @param <T>
 *            Configuration object type
 */
public abstract class ConfigurationAbstractImpl<T> implements Configuration<T> {

	private final String sourceId;
	private final EventBus eventBus;
	private T value;

	ConfigurationAbstractImpl(String sourceId, EventBus eventBus) {
		this.sourceId = sourceId;
		this.eventBus = eventBus;
	}

	/**
	 * Initializes the configuration {@link #value} and subscribes this object to file changes.
	 * 
	 * @param configFile
	 *            Non-null configuration file
	 * @throws IOException
	 *             On I/O error parsing the config file
	 */
	protected void init(File configFile) throws IOException {
		this.value = load(configFile);
		eventBus.subscribe(this);
	}

	@VisibleForTesting
	public String getSourceId() {
		return sourceId;
	}

	@VisibleForTesting
	protected EventBus getEventBus() {
		return eventBus;
	}

	/**
	 * Returns the configuration object.
	 */
	public T getValue() {
		return value;
	}

	/**
	 * Called when a file event published from the {@link #eventBus} is heard.
	 * 
	 * @param fileEvent
	 *            Non-null file event which may or may not correspond to this configuration object
	 * @throws IOException
	 */
	@Subscribe
	public void onFileEvent(FileEvent fileEvent) throws IOException {
		if (getSourceId().equals(fileEvent.getSourceId())) {
			onFileEventMatch(fileEvent.getFile(), fileEvent.getFileEventType());
		}
	}

	/**
	 * Defines the reloading logic to be performed when the file event matches this configuration object.
	 * 
	 * @param configFile
	 *            Non-null configuration file which was changed
	 * @param fileEventType
	 *            Non-null file event type
	 * @throws IOException
	 */
	@VisibleForTesting
	protected synchronized void onFileEventMatch(File configFile, FileEventType fileEventType) throws IOException {
		switch (fileEventType) {
		case CREATED:
			whenConfigFileCreated(configFile);
			break;
		case UPDATED:
			whenConfigFileUpdated(configFile);
			break;
		case DELETED:
			whenConfigFileDeleted(configFile);
			break;
		}
	}

	/**
	 * Called when the file associated with this object is {@link FileEventType#CREATED}. By default, this method
	 * reloads the {@link #config} object.
	 * 
	 * @throws IOException
	 */
	protected void whenConfigFileCreated(File configFile) throws IOException {
		reload(configFile);
	}

	/**
	 * Called when the file associated with this object is {@link FileEventType#UPDATED}. By default, this method
	 * reloads the {@link #config} object.
	 * 
	 * @throws IOException
	 */
	protected void whenConfigFileUpdated(File configFile) throws IOException {
		reload(configFile);
	}

	/**
	 * Called when the file associated with this object is {@link FileEventType#DELETED}. By default, this method does
	 * nothing.
	 * 
	 * @throws IOException
	 */
	protected void whenConfigFileDeleted(File configFile) throws IOException {

	}

	protected void reload(File configFile) throws IOException {
		value = load(configFile);
	}

}