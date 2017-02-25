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
package com.alanbuttars.commons.compress.files.config;

import java.io.InputStream;
import java.util.Map;

import com.alanbuttars.commons.compress.files.config.input.CompressedFileInputStreamConfig;
import com.alanbuttars.commons.compress.files.input.CompressedFileInputStream;
import com.alanbuttars.commons.util.functions.Function;

/**
 * Entrypoint for file config functions.
 * 
 * @author Alan Buttars
 *
 */
public class CompressedFileConfigs {

	public static Map<String, Function<InputStream, CompressedFileInputStreamConfig>> INPUT_CONFIG_FUNCTIONS = CompressedFileInputStreamFunctions.defaultConfigFunctions();
	public static Map<String, Function<CompressedFileInputStreamConfig, CompressedFileInputStream>> INPUT_STREAM_FUNCTIONS = CompressedFileInputStreamFunctions.defaultStreamFunctions();

}
