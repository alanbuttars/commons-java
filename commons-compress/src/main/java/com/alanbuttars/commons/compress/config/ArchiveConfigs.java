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
package com.alanbuttars.commons.compress.config;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.Map;

import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.ArchiveInputStream;
import org.apache.commons.compress.archivers.ArchiveOutputStream;

import com.alanbuttars.commons.compress.config.entry.ArchiveEntryConfig;
import com.alanbuttars.commons.compress.config.input.ArchiveInputStreamConfig;
import com.alanbuttars.commons.compress.config.output.ArchiveOutputStreamConfig;
import com.alanbuttars.commons.util.functions.DoubleInputFunction;
import com.alanbuttars.commons.util.functions.Function;

/**
 * Entrypoint for archive config functions.
 * 
 * @author Alan Buttars
 *
 */
public class ArchiveConfigs {

	public static Map<String, Function<InputStream, ArchiveInputStreamConfig>> INPUT_CONFIG_FUNCTIONS = ArchiveInputStreamFunctions.defaultConfigFunctions();
	public static Map<String, Function<ArchiveInputStreamConfig, ArchiveInputStream>> INPUT_STREAM_FUNCTIONS = ArchiveInputStreamFunctions.defaultStreamFunctions();

	public static Map<String, Function<OutputStream, ArchiveOutputStreamConfig>> OUTPUT_CONFIG_FUNCTIONS = ArchiveOutputStreamFunctions.defaultConfigFunctions();
	public static Map<String, Function<ArchiveOutputStreamConfig, ArchiveOutputStream>> OUTPUT_STREAM_FUNCTIONS = ArchiveOutputStreamFunctions.defaultStreamFunctions();

	public static Map<String, DoubleInputFunction<String, Long, ArchiveEntryConfig>> ENTRY_CONFIG_FUNCTIONS = ArchiveEntryFunctions.defaultConfigFunctions();
	public static Map<String, Function<ArchiveEntryConfig, ArchiveEntry>> ENTRY_FUNCTIONS = ArchiveEntryFunctions.defaultEntryFunctions();

}
