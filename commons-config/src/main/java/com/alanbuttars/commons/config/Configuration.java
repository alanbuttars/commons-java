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

/**
 * Models a configuration object with the flexibility to return multiple configuration value types.
 * 
 * @author Alan Buttars
 * 
 * @param <T>
 *            Configuration object type
 *
 */
public interface Configuration<T> {

	/**
	 * Loads the configuration object from the configuration file.
	 * 
	 * @param configFile
	 *            Non-null configuration file
	 * @return Non-null configuration object
	 * @throws IOException
	 *             On any I/O error while reading the configuration file
	 */
	public T load(File configFile) throws IOException;
}