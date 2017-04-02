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

/**
 * Intermediate stub ultimately used to create a {@link Configuration}.
 * 
 * @author Alan Buttars
 *
 */
public class WatchFileStub {

	protected final File file;

	WatchFileStub(File file) {
		this.file = file;
	}

	/**
	 * Treats the file as JSON.
	 */
	public WatchFileJsonImplStub asJson() {
		return new WatchFileJsonImplStub(file);
	}

	/**
	 * Treats the file as a Java .properties file.
	 */
	public WatchFilePropertiesImplStub asProperties() {
		return new WatchFilePropertiesImplStub(file);
	}

	/**
	 * Treats the file as XML.
	 */
	public WatchFileXmlImplStub asXml() {
		return new WatchFileXmlImplStub(file);
	}

	/**
	 * Treats the file as YAML.
	 */
	public WatchFileYamlImplStub asYaml() {
		return new WatchFileYamlImplStub(file);
	}
}
