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
package com.alanbuttars.commons.config.master;

import java.util.Map;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Object which encompasses the YAML configuration file for this module.
 * 
 * @author Alan Buttars
 *
 */
public class YamlConfig {

	@JsonProperty("master")
	private YamlMasterConfig master;
	@JsonProperty("files")
	private Map<String, YamlFileConfig> configFiles;

	public YamlMasterConfig getMaster() {
		return master;
	}

	void setMaster(YamlMasterConfig master) {
		this.master = master;
	}

	public Map<String, YamlFileConfig> getConfigFiles() {
		return configFiles;
	}

	void setConfigFiles(Map<String, YamlFileConfig> configFiles) {
		this.configFiles = configFiles;
	}

}
