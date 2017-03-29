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
package com.alanbuttars.commons.config.source;

import static com.alanbuttars.commons.util.validators.Arguments.verifyNonEmpty;
import static com.alanbuttars.commons.util.validators.Arguments.verifyNonNull;

import java.io.File;
import java.io.IOException;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;

/**
 * The entrypoint to this module. Configurations may be loaded in two different ways:
 * 
 * @author Alan Buttars
 *
 */
public class ConfigLoader {

	private ConfigLoader() {
	}

	public static Config load() throws IOException {
		return load(getConfigurationSourceFile());
	}

	public static Config load(File configurationSourceFile) throws IOException {
		YAMLFactory yamlFactory = new YAMLFactory();
		ObjectMapper objectMapper = new ObjectMapper(yamlFactory);
		Config config = objectMapper.readValue(configurationSourceFile, Config.class);
		return config;
	}

	private static File getConfigurationSourceFile() {
		String environmentValue = System.getProperty("commons.config");
		verifyNonNull(environmentValue, "Environment variable commons.config must be non-null");
		verifyNonEmpty(environmentValue, "Environment variable commons.config must be non-empty");
		return new File(environmentValue);
	}

}
