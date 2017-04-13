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

import static com.alanbuttars.commons.util.validators.Arguments.verifyNonNull;

import java.io.File;

import com.alanbuttars.commons.config.Configuration;
import com.alanbuttars.commons.config.ConfigurationAbstractImpl;
import com.alanbuttars.commons.config.eventbus.EventBus;
import com.alanbuttars.commons.util.functions.Function;

/**
 * Intermediate stub ultimately used to create a custom {@link ConfigurationAbstractImpl}.
 * 
 * @author Alan Buttars
 *
 */
public class WatchFileCustomImplStub {

	protected final String sourceId;
	protected final File file;
	protected final EventBus eventBus;

	public WatchFileCustomImplStub(String sourceId, File file, EventBus eventBus) {
		this.sourceId = sourceId;
		this.file = file;
		this.eventBus = eventBus;
	}

	/**
	 * Uses the given function to transform configuration params into a defined {@link Configuration}.
	 * 
	 * @param configFunction
	 *            Non-null function
	 */
	public <C extends ConfigurationAbstractImpl<T>, T> C mappedWith(Function<CustomConfigurationParams, C> configFunction) {
		verifyNonNull(configFunction, "Config function must be non-null");
		return configFunction.apply(new CustomConfigurationParams(sourceId, file, eventBus));
	}
}
