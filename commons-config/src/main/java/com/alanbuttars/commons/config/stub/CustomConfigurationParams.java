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

import java.io.File;

import com.alanbuttars.commons.config.Configuration;
import com.alanbuttars.commons.config.eventbus.EventBus;

/**
 * A simple POJO containing params used to create a custom {@link Configuration}.
 * 
 * @author Alan Buttars
 *
 */
public class CustomConfigurationParams {

	private final String sourceId;
	private final File file;
	private final EventBus eventBus;

	CustomConfigurationParams(String sourceId, File file, EventBus eventBus) {
		this.sourceId = sourceId;
		this.file = file;
		this.eventBus = eventBus;
	}

	public String getSourceId() {
		return sourceId;
	}

	public File getFile() {
		return file;
	}

	public EventBus getEventBus() {
		return eventBus;
	}

}
