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
import static com.alanbuttars.commons.compress.files.util.Files.GZIP;
import static com.alanbuttars.commons.compress.files.util.Files.LZMA;
import static com.alanbuttars.commons.compress.files.util.Files.PACK200;

import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.compress.compressors.CompressorOutputStream;
import org.apache.commons.compress.compressors.bzip2.BZip2CompressorOutputStream;
import org.apache.commons.compress.compressors.deflate.DeflateCompressorOutputStream;
import org.apache.commons.compress.compressors.gzip.GzipCompressorOutputStream;
import org.apache.commons.compress.compressors.lzma.LZMACompressorOutputStream;
import org.apache.commons.compress.compressors.pack200.Pack200CompressorOutputStream;

import com.alanbuttars.commons.compress.files.config.output.FileOutputStreamConfig;
import com.alanbuttars.commons.compress.files.config.output.FileOutputStreamConfigBzip2Impl;
import com.alanbuttars.commons.compress.files.config.output.FileOutputStreamConfigDeflateImpl;
import com.alanbuttars.commons.compress.files.config.output.FileOutputStreamConfigGzipImpl;
import com.alanbuttars.commons.compress.files.config.output.FileOutputStreamConfigPack200Impl;
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
		functions.put(GZIP, defaultGzipConfigFunction());
		functions.put(LZMA, defaultLzmaConfigFunction());
		functions.put(PACK200, defaultPack200ConfigFunction());
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

	private static Function<OutputStream, FileOutputStreamConfig> defaultGzipConfigFunction() {
		return new Function<OutputStream, FileOutputStreamConfig>() {

			@Override
			public FileOutputStreamConfig apply(OutputStream output) {
				return new FileOutputStreamConfigGzipImpl(output);
			}

		};
	}

	private static Function<OutputStream, FileOutputStreamConfig> defaultLzmaConfigFunction() {
		return new Function<OutputStream, FileOutputStreamConfig>() {

			@Override
			public FileOutputStreamConfig apply(OutputStream output) {
				return new FileOutputStreamConfig(output);
			}

		};
	}

	private static Function<OutputStream, FileOutputStreamConfig> defaultPack200ConfigFunction() {
		return new Function<OutputStream, FileOutputStreamConfig>() {

			@Override
			public FileOutputStreamConfig apply(OutputStream output) {
				return new FileOutputStreamConfigPack200Impl(output);
			}

		};
	}

	public static Map<String, Function<FileOutputStreamConfig, CompressorOutputStream>> defaultStreamFunctions() {
		Map<String, Function<FileOutputStreamConfig, CompressorOutputStream>> functions = new HashMap<>();
		functions.put(BZIP2, defaultBzip2StreamFunction());
		functions.put(DEFLATE, defaultDeflateStreamFunction());
		functions.put(GZIP, defaultGzipStreamFunction());
		functions.put(LZMA, defaultLzmaStreamFunction());
		functions.put(PACK200, defaultPack200StreamFunction());
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

	private static Function<FileOutputStreamConfig, CompressorOutputStream> defaultGzipStreamFunction() {
		return new Function<FileOutputStreamConfig, CompressorOutputStream>() {

			@Override
			public CompressorOutputStream apply(FileOutputStreamConfig config) {
				try {
					FileOutputStreamConfigGzipImpl gzipConfig = (FileOutputStreamConfigGzipImpl) config;
					return new GzipCompressorOutputStream(gzipConfig.getOutputStream(), gzipConfig.getParameters());
				}
				catch (IOException e) {
					throw new RuntimeException(e);
				}
			}

		};
	}

	private static Function<FileOutputStreamConfig, CompressorOutputStream> defaultLzmaStreamFunction() {
		return new Function<FileOutputStreamConfig, CompressorOutputStream>() {

			@Override
			public CompressorOutputStream apply(FileOutputStreamConfig config) {
				try {
					return new LZMACompressorOutputStream(config.getOutputStream());
				}
				catch (IOException e) {
					throw new RuntimeException(e);
				}
			}

		};
	}

	private static Function<FileOutputStreamConfig, CompressorOutputStream> defaultPack200StreamFunction() {
		return new Function<FileOutputStreamConfig, CompressorOutputStream>() {

			@Override
			public CompressorOutputStream apply(FileOutputStreamConfig config) {
				try {
					FileOutputStreamConfigPack200Impl packConfig = (FileOutputStreamConfigPack200Impl) config;
					return new Pack200CompressorOutputStream(packConfig.getOutputStream(), packConfig.getMode(), packConfig.getProperties());
				}
				catch (IOException e) {
					throw new RuntimeException(e);
				}
			}

		};
	}

}
