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
import java.io.IOException;

import com.alanbuttars.commons.config.ConfigurationYamlImpl;
import com.alanbuttars.commons.config.eventbus.EventBusSyncImpl;
import com.alanbuttars.commons.config.master.YamlConfig;
import com.alanbuttars.commons.config.master.YamlFileConfig;

/**
 * Helper class for {@link WatchTest}.
 * 
 * @author Alan Buttars
 *
 */
public class WatchTestHelper {

	/**
	 * Retrieves the file path to the YAML.
	 */
	public static String getYaml() {
		return WatchTest.class.getResource("valid.yml").getFile();
	}

	public static File getSourceFile(String sourceId) throws IOException {
		File yamlFile = new File(getYaml());
		ConfigurationYamlImpl<YamlConfig> config = new ConfigurationYamlImpl<>(sourceId, yamlFile, new EventBusSyncImpl(), YamlConfig.class);
		YamlConfig yamlConfig = config.getValue();
		YamlFileConfig yamlConfigFile = yamlConfig.getFileConfigs().get(sourceId);
		return new File(yamlConfigFile.getFile());
	}

}
