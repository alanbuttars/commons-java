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
 * Extending classes must call {@link #initEventBus(EventBus)} in the last call of the constructor. For example:
 * 
 * <pre>
 * class ConfigurationCustomImpl implements Configuration&lt;User&gt; {
 * 
 * 	private final UserDao userDao;
 * 
 * 	public ConfigurationCustomImpl(File configFile, EventBus eventBus, UserDao userDao) {
 * 		super(configFile);
 * 		this.userDao = userDao;
 * 		initEventBus();
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

	private final File configFile;
	protected T value;

	ConfigurationAbstractImpl(File configFile) throws IOException {
		this.configFile = configFile;
	}

	protected void initEventBus(EventBus eventBus) throws IOException {
		this.value = load(configFile);
		eventBus.subscribe(this);
	}

	/**
	 * Returns the configuration object.
	 */
	public T getValue() {
		return value;
	}

	@Subscribe
	public void onFileEvent(FileEvent fileEvent) throws IOException {
		if (configFile.equals(fileEvent.getFile())) {
			onFileEventMatch(fileEvent.getFileEventType());
		}
	}

	@VisibleForTesting
	protected synchronized void onFileEventMatch(FileEventType fileEventType) throws IOException {
		switch (fileEventType) {
		case CREATED:
			whenConfigFileCreated();
			break;
		case UPDATED:
			whenConfigFileUpdated();
			break;
		case DELETED:
			whenConfigFileDeleted();
			break;
		}
	}

	/**
	 * Called when the {@link #configFile} associated with this object is {@link FileEventType#CREATED}. By default,
	 * this method reloads the {@link #config} object.
	 * 
	 * @throws IOException
	 */
	protected void whenConfigFileCreated() throws IOException {
		reload();
	}

	/**
	 * Called when the {@link #configFile} associated with this object is {@link FileEventType#UPDATED}. By default,
	 * this method reloads the {@link #config} object.
	 * 
	 * @throws IOException
	 */
	protected void whenConfigFileUpdated() throws IOException {
		reload();
	}

	/**
	 * Called when the {@link #configFile} associated with this object is {@link FileEventType#DELETED}. By default,
	 * this method does nothing.
	 */
	protected void whenConfigFileDeleted() {

	}

	private void reload() throws IOException {
		value = load(configFile);
	}

}