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
import java.io.IOException;

import com.alanbuttars.commons.config.Configuration;
import com.alanbuttars.commons.config.ConfigurationPropertiesImpl;
import com.alanbuttars.commons.config.eventbus.EventBus;

/**
 * Terminal stub used to create a {@link ConfigurationPropertiesImpl} from a properties file.
 * 
 * @author Alan Buttars
 *
 * @param <T>
 *            Type belonging to {@link ConfigurationPropertiesImpl}
 */
public class WatchFilePropertiesImplStub {

	protected final File file;

	WatchFilePropertiesImplStub(File file) {
		this.file = file;
	}

	/**
	 * Instructs the created {@link Configuration} to subscribe to events published by the given event bus.
	 * 
	 * @param eventBus
	 *            Non-null event bus
	 * @return The configuration object
	 * @throws IOException
	 *             On I/O parsing the properties file
	 */
	public ConfigurationPropertiesImpl withEventBus(EventBus eventBus) throws IOException {
		verifyNonNull(eventBus, "Event bus must be non-null");
		return new ConfigurationPropertiesImpl(file, eventBus);
	}

}
