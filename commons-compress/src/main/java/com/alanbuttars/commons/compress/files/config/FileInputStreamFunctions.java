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
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.compress.compressors.CompressorInputStream;

import com.alanbuttars.commons.compress.files.config.input.FileInputStreamConfig;
import com.alanbuttars.commons.util.functions.Function;

/**
 * The source of default functions mapping file input stream to an {@link FileInputStreamConfig}.
 * 
 * @author Alan Buttars
 *
 */
public class FileInputStreamFunctions {

	public static Map<String, Function<InputStream, FileInputStreamConfig>> defaultConfigFunctions() {
		Map<String, Function<InputStream, FileInputStreamConfig>> functions = new HashMap<>();
		return functions;
	}

	public static Map<String, Function<FileInputStreamConfig, CompressorInputStream>> defaultStreamFunctions() {
		Map<String, Function<FileInputStreamConfig, CompressorInputStream>> functions = new HashMap<>();
		return functions;
	}

}
