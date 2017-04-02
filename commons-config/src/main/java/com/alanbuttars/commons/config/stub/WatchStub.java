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
import com.alanbuttars.commons.config.eventbus.EventBus;

public class WatchStub {

	private final File yamlFile;

	WatchStub(File yamlFile) {
		this.yamlFile = yamlFile;
	}

	/**
	 * Creates the singleton instance used to create {@link Configuration} objects.
	 * 
	 * @param eventBus
	 *            Non-null event bus used to publish and subscribe to events
	 * @throws IOException
	 *             On I/O error reading the YAML
	 */
	public Watch withEventBus(EventBus eventBus) throws IOException {
		verifyNonNull(eventBus, "Event bus must be non-null");
		return new Watch(yamlFile, eventBus);
	}
}
