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

import static com.alanbuttars.commons.compress.files.util.Files.BZIP2;
import static com.alanbuttars.commons.compress.files.util.Files.DEFLATE;

import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.compress.compressors.CompressorOutputStream;
import org.apache.commons.compress.compressors.bzip2.BZip2CompressorOutputStream;
import org.apache.commons.compress.compressors.deflate.DeflateCompressorOutputStream;

import com.alanbuttars.commons.compress.files.config.output.FileOutputStreamConfig;
import com.alanbuttars.commons.compress.files.config.output.FileOutputStreamConfigBzip2Impl;
import com.alanbuttars.commons.compress.files.config.output.FileOutputStreamConfigDeflateImpl;
import com.alanbuttars.commons.util.functions.Function;

/**
 * The source of default functions mapping file output stream to an {@link FileOutputStreamConfig}.
 * 
 * @author Alan Buttars
 *
 */
public class FileOutputStreamFunctions {

	public static Map<String, Function<OutputStream, FileOutputStreamConfig>> defaultConfigFunctions() {
		Map<String, Function<OutputStream, FileOutputStreamConfig>> functions = new HashMap<>();
		functions.put(BZIP2, defaultBzip2ConfigFunction());
		functions.put(DEFLATE, defaultDeflateConfigFunction());
		return functions;
	}

	private static Function<OutputStream, FileOutputStreamConfig> defaultBzip2ConfigFunction() {
		return new Function<OutputStream, FileOutputStreamConfig>() {

			@Override
			public FileOutputStreamConfig apply(OutputStream output) {
				return new FileOutputStreamConfigBzip2Impl(output);
			}

		};
	}

	private static Function<OutputStream, FileOutputStreamConfig> defaultDeflateConfigFunction() {
		return new Function<OutputStream, FileOutputStreamConfig>() {

			@Override
			public FileOutputStreamConfig apply(OutputStream output) {
				return new FileOutputStreamConfigDeflateImpl(output);
			}

		};
	}

	public static Map<String, Function<FileOutputStreamConfig, CompressorOutputStream>> defaultStreamFunctions() {
		Map<String, Function<FileOutputStreamConfig, CompressorOutputStream>> functions = new HashMap<>();
		functions.put(BZIP2, defaultBzip2StreamFunction());
		functions.put(DEFLATE, defaultDeflateStreamFunction());
		return functions;
	}

	private static Function<FileOutputStreamConfig, CompressorOutputStream> defaultBzip2StreamFunction() {
		return new Function<FileOutputStreamConfig, CompressorOutputStream>() {

			@Override
			public CompressorOutputStream apply(FileOutputStreamConfig config) {
				try {
					FileOutputStreamConfigBzip2Impl bzip2Config = (FileOutputStreamConfigBzip2Impl) config;
					return new BZip2CompressorOutputStream(bzip2Config.getOutputStream(), bzip2Config.getBlockSize());
				}
				catch (IOException e) {
					throw new RuntimeException(e);
				}
			}

		};
	}

	private static Function<FileOutputStreamConfig, CompressorOutputStream> defaultDeflateStreamFunction() {
		return new Function<FileOutputStreamConfig, CompressorOutputStream>() {

			@Override
			public CompressorOutputStream apply(FileOutputStreamConfig config) {
				try {
					FileOutputStreamConfigDeflateImpl deflateConfig = (FileOutputStreamConfigDeflateImpl) config;
					return new DeflateCompressorOutputStream(deflateConfig.getOutputStream(), deflateConfig.getParameters());
				}
				catch (IOException e) {
					throw new RuntimeException(e);
				}
			}

		};
	}

}
