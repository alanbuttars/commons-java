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
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * {@link Configuration} implementation used for JSON files. Instances of this class accept a single JSON file which is
 * mapped to a given class type.
 * 
 * @author Alan Buttars
 *
 * @param <C>
 *            Collection class type to which the given JSON file will be mapped
 */
public class ConfigurationJsonCollectionImpl<C> extends ConfigurationAbstractImpl<C> {

	private final TypeReference<C> typeReference;

	public ConfigurationJsonCollectionImpl(File configFile, EventBus eventBus, TypeReference<C> typeReference) throws IOException {
		super(configFile);
		this.typeReference = typeReference;
		initEventBus(eventBus);
	}

	@Override
	public C load(File configFile) throws IOException {
		try {
			JsonFactory jsonFactory = new JsonFactory();
			ObjectMapper objectMapper = new ObjectMapper(jsonFactory);
			return objectMapper.readValue(configFile, typeReference);
		}
		catch (JsonMappingException | JsonParseException e) {
			throw new IOException(e);
		}
	}

}
