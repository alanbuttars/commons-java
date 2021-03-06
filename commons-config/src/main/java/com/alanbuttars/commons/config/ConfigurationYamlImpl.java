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
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;

/**
 * {@link Configuration} implementation used for YAML files. Instances of this class accept a single YAML file which is
 * mapped to a given class type.
 * 
 * @author Alan Buttars
 *
 * @param <T>
 *            Class type to which the given YAML file will be mapped
 */
public class ConfigurationYamlImpl<T> extends ConfigurationAbstractImpl<T> {

	private final Class<T> clazz;

	public ConfigurationYamlImpl(String sourceId, File configFile, EventBus eventBus, Class<T> clazz) throws IOException {
		super(sourceId, eventBus);
		this.clazz = clazz;
		init(configFile);
	}

	@Override
	public T load(File configFile) throws IOException {
		try {
			YAMLFactory yamlFactory = new YAMLFactory();
			ObjectMapper objectMapper = new ObjectMapper(yamlFactory);
			return objectMapper.readValue(configFile, clazz);
		}
		catch (JsonMappingException | JsonParseException e) {
			throw new IOException(e);
		}
	}

}
