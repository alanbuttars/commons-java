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
import static com.alanbuttars.commons.compress.files.util.Files.SNAPPY;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.compress.compressors.CompressorInputStream;
import org.apache.commons.compress.compressors.bzip2.BZip2CompressorInputStream;
import org.apache.commons.compress.compressors.deflate.DeflateCompressorInputStream;
import org.apache.commons.compress.compressors.gzip.GzipCompressorInputStream;
import org.apache.commons.compress.compressors.lzma.LZMACompressorInputStream;
import org.apache.commons.compress.compressors.pack200.Pack200CompressorInputStream;
import org.apache.commons.compress.compressors.snappy.FramedSnappyCompressorInputStream;
import org.apache.commons.compress.compressors.snappy.SnappyCompressorInputStream;

import com.alanbuttars.commons.compress.files.config.input.FileInputStreamConfig;
import com.alanbuttars.commons.compress.files.config.input.FileInputStreamConfigBzip2Impl;
import com.alanbuttars.commons.compress.files.config.input.FileInputStreamConfigDeflateImpl;
import com.alanbuttars.commons.compress.files.config.input.FileInputStreamConfigGzipImpl;
import com.alanbuttars.commons.compress.files.config.input.FileInputStreamConfigPack200Impl;
import com.alanbuttars.commons.compress.files.config.input.FileInputStreamConfigSnappyImpl;
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
		functions.put(BZIP2, defaultBzip2ConfigFunction());
		functions.put(DEFLATE, defaultDeflateConfigFunction());
		functions.put(GZIP, defaultGzipConfigFunction());
		functions.put(LZMA, defaultLzmaConfigFunction());
		functions.put(PACK200, defaultPack200ConfigFunction());
		functions.put(SNAPPY, defaultSnappyConfigFunction());
		return functions;
	}

	private static Function<InputStream, FileInputStreamConfig> defaultBzip2ConfigFunction() {
		return new Function<InputStream, FileInputStreamConfig>() {

			@Override
			public FileInputStreamConfig apply(InputStream input) {
				return new FileInputStreamConfigBzip2Impl(input);
			}

		};
	}

	private static Function<InputStream, FileInputStreamConfig> defaultDeflateConfigFunction() {
		return new Function<InputStream, FileInputStreamConfig>() {

			@Override
			public FileInputStreamConfig apply(InputStream input) {
				return new FileInputStreamConfigDeflateImpl(input);
			}

		};
	}

	private static Function<InputStream, FileInputStreamConfig> defaultGzipConfigFunction() {
		return new Function<InputStream, FileInputStreamConfig>() {

			@Override
			public FileInputStreamConfig apply(InputStream input) {
				return new FileInputStreamConfigGzipImpl(input);
			}

		};
	}

	private static Function<InputStream, FileInputStreamConfig> defaultLzmaConfigFunction() {
		return new Function<InputStream, FileInputStreamConfig>() {

			@Override
			public FileInputStreamConfig apply(InputStream input) {
				return new FileInputStreamConfig(input);
			}

		};
	}

	private static Function<InputStream, FileInputStreamConfig> defaultPack200ConfigFunction() {
		return new Function<InputStream, FileInputStreamConfig>() {

			@Override
			public FileInputStreamConfig apply(InputStream input) {
				return new FileInputStreamConfigPack200Impl(input);
			}

		};
	}

	private static Function<InputStream, FileInputStreamConfig> defaultSnappyConfigFunction() {
		return new Function<InputStream, FileInputStreamConfig>() {

			@Override
			public FileInputStreamConfig apply(InputStream input) {
				return new FileInputStreamConfigSnappyImpl(input);
			}

		};
	}

	public static Map<String, Function<FileInputStreamConfig, CompressorInputStream>> defaultStreamFunctions() {
		Map<String, Function<FileInputStreamConfig, CompressorInputStream>> functions = new HashMap<>();
		functions.put(BZIP2, defaultBzip2StreamFunction());
		functions.put(DEFLATE, defaultDeflateStreamFunction());
		functions.put(GZIP, defaultGzipStreamFunction());
		functions.put(LZMA, defaultLzmaStreamFunction());
		functions.put(PACK200, defaultPack200StreamFunction());
		functions.put(SNAPPY, defaultSnappyStreamFunction());
		return functions;
	}

	private static Function<FileInputStreamConfig, CompressorInputStream> defaultBzip2StreamFunction() {
		return new Function<FileInputStreamConfig, CompressorInputStream>() {

			@Override
			public CompressorInputStream apply(FileInputStreamConfig config) {
				try {
					FileInputStreamConfigBzip2Impl bzip2Config = (FileInputStreamConfigBzip2Impl) config;
					return new BZip2CompressorInputStream(bzip2Config.getInputStream(), bzip2Config.decompressConcatenated());
				}
				catch (IOException e) {
					throw new RuntimeException(e);
				}
			}

		};
	}

	private static Function<FileInputStreamConfig, CompressorInputStream> defaultDeflateStreamFunction() {
		return new Function<FileInputStreamConfig, CompressorInputStream>() {

			@Override
			public CompressorInputStream apply(FileInputStreamConfig config) {
				FileInputStreamConfigDeflateImpl deflateConfig = (FileInputStreamConfigDeflateImpl) config;
				return new DeflateCompressorInputStream(deflateConfig.getInputStream(), deflateConfig.getParameters());
			}

		};
	}

	private static Function<FileInputStreamConfig, CompressorInputStream> defaultGzipStreamFunction() {
		return new Function<FileInputStreamConfig, CompressorInputStream>() {

			@Override
			public CompressorInputStream apply(FileInputStreamConfig config) {
				try {
					FileInputStreamConfigGzipImpl gzipConfig = (FileInputStreamConfigGzipImpl) config;
					return new GzipCompressorInputStream(gzipConfig.getInputStream(), gzipConfig.decompressConcatenated());
				}
				catch (IOException e) {
					throw new RuntimeException(e);
				}
			}

		};
	}

	private static Function<FileInputStreamConfig, CompressorInputStream> defaultLzmaStreamFunction() {
		return new Function<FileInputStreamConfig, CompressorInputStream>() {

			@Override
			public CompressorInputStream apply(FileInputStreamConfig config) {
				try {
					return new LZMACompressorInputStream(config.getInputStream());
				}
				catch (IOException e) {
					throw new RuntimeException(e);
				}
			}

		};
	}

	private static Function<FileInputStreamConfig, CompressorInputStream> defaultPack200StreamFunction() {
		return new Function<FileInputStreamConfig, CompressorInputStream>() {

			@Override
			public CompressorInputStream apply(FileInputStreamConfig config) {
				try {
					FileInputStreamConfigPack200Impl packConfig = (FileInputStreamConfigPack200Impl) config;
					return new Pack200CompressorInputStream(packConfig.getInputStream(), packConfig.getMode(), packConfig.getProperties());
				}
				catch (IOException e) {
					throw new RuntimeException(e);
				}
			}

		};
	}

	private static Function<FileInputStreamConfig, CompressorInputStream> defaultSnappyStreamFunction() {
		return new Function<FileInputStreamConfig, CompressorInputStream>() {

			@Override
			public CompressorInputStream apply(FileInputStreamConfig config) {
				try {
					FileInputStreamConfigSnappyImpl snappyConfig = (FileInputStreamConfigSnappyImpl) config;
					if (snappyConfig.getFormat() == FileInputStreamConfigSnappyImpl.Format.FRAMED) {
						return new FramedSnappyCompressorInputStream(snappyConfig.getInputStream(), snappyConfig.getDialect());
					}
					return new SnappyCompressorInputStream(snappyConfig.getInputStream(), snappyConfig.getBlockSize());
				}
				catch (IOException e) {
					throw new RuntimeException(e);
				}
			}

		};
	}

}
