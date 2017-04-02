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

import com.alanbuttars.commons.config.eventbus.EventBus;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

public class ConfigurationJsonImpl<T> implements Configuration {

	private T object;

	public ConfigurationJsonImpl(File configFile, Class<T> clazz, EventBus eventBus) throws IOException {
		this.object = getObjectMapper().readValue(configFile, clazz);
		eventBus.subscribe(this);
	}

	public <C> ConfigurationJsonImpl(File configFile, TypeReference<C> typeReference, EventBus eventBus) throws IOException {
		this.object = getObjectMapper().readValue(configFile, typeReference);
		eventBus.subscribe(this);
	}

	public T getValue() {
		return object;
	}

	private static ObjectMapper getObjectMapper() {
		JsonFactory yamlFactory = new JsonFactory();
		ObjectMapper objectMapper = new ObjectMapper(yamlFactory);
		return objectMapper;
	}

}
