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

import static com.alanbuttars.commons.compress.files.util.CompressedFiles.BZIP2;
import static com.alanbuttars.commons.compress.files.util.CompressedFiles.DEFLATE;
import static com.alanbuttars.commons.compress.files.util.CompressedFiles.GZIP;
import static com.alanbuttars.commons.compress.files.util.CompressedFiles.LZMA;
import static com.alanbuttars.commons.compress.files.util.CompressedFiles.PACK200;
import static com.alanbuttars.commons.compress.files.util.CompressedFiles.XZ;

import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.compress.compressors.bzip2.BZip2CompressorOutputStream;
import org.apache.commons.compress.compressors.deflate.DeflateCompressorOutputStream;
import org.apache.commons.compress.compressors.gzip.GzipCompressorOutputStream;
import org.apache.commons.compress.compressors.lzma.LZMACompressorOutputStream;
import org.apache.commons.compress.compressors.pack200.Pack200CompressorOutputStream;
import org.apache.commons.compress.compressors.xz.XZCompressorOutputStream;

import com.alanbuttars.commons.compress.files.config.output.CompressedFileOutputStreamConfig;
import com.alanbuttars.commons.compress.files.config.output.CompressedFileOutputStreamConfigBzip2Impl;
import com.alanbuttars.commons.compress.files.config.output.CompressedFileOutputStreamConfigDeflateImpl;
import com.alanbuttars.commons.compress.files.config.output.CompressedFileOutputStreamConfigGzipImpl;
import com.alanbuttars.commons.compress.files.config.output.CompressedFileOutputStreamConfigPack200Impl;
import com.alanbuttars.commons.compress.files.config.output.CompressedFileOutputStreamConfigXzImpl;
import com.alanbuttars.commons.compress.files.output.CompressedFileOutputStream;
import com.alanbuttars.commons.compress.files.output.CompressedFileOutputStreamImpl;
import com.alanbuttars.commons.util.functions.Function;

/**
 * The source of default functions mapping file output stream to an {@link CompressedFileOutputStreamConfig}.
 * 
 * @author Alan Buttars
 *
 */
public class CompressedFileOutputStreamFunctions {

	public static Map<String, Function<OutputStream, CompressedFileOutputStreamConfig>> defaultConfigFunctions() {
		Map<String, Function<OutputStream, CompressedFileOutputStreamConfig>> functions = new HashMap<>();
		functions.put(BZIP2, defaultBzip2ConfigFunction());
		functions.put(DEFLATE, defaultDeflateConfigFunction());
		functions.put(GZIP, defaultGzipConfigFunction());
		functions.put(LZMA, defaultLzmaConfigFunction());
		functions.put(PACK200, defaultPack200ConfigFunction());
		functions.put(XZ, defaultXzConfigFunction());
		return functions;
	}

	private static Function<OutputStream, CompressedFileOutputStreamConfig> defaultBzip2ConfigFunction() {
		return new Function<OutputStream, CompressedFileOutputStreamConfig>() {

			@Override
			public CompressedFileOutputStreamConfig apply(OutputStream output) {
				return new CompressedFileOutputStreamConfigBzip2Impl(output);
			}

		};
	}

	private static Function<OutputStream, CompressedFileOutputStreamConfig> defaultDeflateConfigFunction() {
		return new Function<OutputStream, CompressedFileOutputStreamConfig>() {

			@Override
			public CompressedFileOutputStreamConfig apply(OutputStream output) {
				return new CompressedFileOutputStreamConfigDeflateImpl(output);
			}

		};
	}

	private static Function<OutputStream, CompressedFileOutputStreamConfig> defaultGzipConfigFunction() {
		return new Function<OutputStream, CompressedFileOutputStreamConfig>() {

			@Override
			public CompressedFileOutputStreamConfig apply(OutputStream output) {
				return new CompressedFileOutputStreamConfigGzipImpl(output);
			}

		};
	}

	private static Function<OutputStream, CompressedFileOutputStreamConfig> defaultLzmaConfigFunction() {
		return new Function<OutputStream, CompressedFileOutputStreamConfig>() {

			@Override
			public CompressedFileOutputStreamConfig apply(OutputStream output) {
				return new CompressedFileOutputStreamConfig(output);
			}

		};
	}

	private static Function<OutputStream, CompressedFileOutputStreamConfig> defaultPack200ConfigFunction() {
		return new Function<OutputStream, CompressedFileOutputStreamConfig>() {

			@Override
			public CompressedFileOutputStreamConfig apply(OutputStream output) {
				return new CompressedFileOutputStreamConfigPack200Impl(output);
			}

		};
	}

	private static Function<OutputStream, CompressedFileOutputStreamConfig> defaultXzConfigFunction() {
		return new Function<OutputStream, CompressedFileOutputStreamConfig>() {

			@Override
			public CompressedFileOutputStreamConfig apply(OutputStream output) {
				return new CompressedFileOutputStreamConfigXzImpl(output);
			}

		};
	}

	public static Map<String, Function<CompressedFileOutputStreamConfig, CompressedFileOutputStream>> defaultStreamFunctions() {
		Map<String, Function<CompressedFileOutputStreamConfig, CompressedFileOutputStream>> functions = new HashMap<>();
		functions.put(BZIP2, defaultBzip2StreamFunction());
		functions.put(DEFLATE, defaultDeflateStreamFunction());
		functions.put(GZIP, defaultGzipStreamFunction());
		functions.put(LZMA, defaultLzmaStreamFunction());
		functions.put(PACK200, defaultPack200StreamFunction());
		functions.put(XZ, defaultXzStreamFunction());
		return functions;
	}

	private static Function<CompressedFileOutputStreamConfig, CompressedFileOutputStream> defaultBzip2StreamFunction() {
		return new Function<CompressedFileOutputStreamConfig, CompressedFileOutputStream>() {

			@Override
			public CompressedFileOutputStream apply(CompressedFileOutputStreamConfig config) {
				try {
					CompressedFileOutputStreamConfigBzip2Impl bzip2Config = (CompressedFileOutputStreamConfigBzip2Impl) config;
					return new CompressedFileOutputStreamImpl(new BZip2CompressorOutputStream(bzip2Config.getOutputStream(), bzip2Config.getBlockSize()));
				}
				catch (IOException e) {
					throw new RuntimeException(e);
				}
			}

		};
	}

	private static Function<CompressedFileOutputStreamConfig, CompressedFileOutputStream> defaultDeflateStreamFunction() {
		return new Function<CompressedFileOutputStreamConfig, CompressedFileOutputStream>() {

			@Override
			public CompressedFileOutputStream apply(CompressedFileOutputStreamConfig config) {
				try {
					CompressedFileOutputStreamConfigDeflateImpl deflateConfig = (CompressedFileOutputStreamConfigDeflateImpl) config;
					return new CompressedFileOutputStreamImpl(new DeflateCompressorOutputStream(deflateConfig.getOutputStream(), deflateConfig.getParameters()));
				}
				catch (IOException e) {
					throw new RuntimeException(e);
				}
			}

		};
	}

	private static Function<CompressedFileOutputStreamConfig, CompressedFileOutputStream> defaultGzipStreamFunction() {
		return new Function<CompressedFileOutputStreamConfig, CompressedFileOutputStream>() {

			@Override
			public CompressedFileOutputStream apply(CompressedFileOutputStreamConfig config) {
				try {
					CompressedFileOutputStreamConfigGzipImpl gzipConfig = (CompressedFileOutputStreamConfigGzipImpl) config;
					return new CompressedFileOutputStreamImpl(new GzipCompressorOutputStream(gzipConfig.getOutputStream(), gzipConfig.getParameters()));
				}
				catch (IOException e) {
					throw new RuntimeException(e);
				}
			}

		};
	}

	private static Function<CompressedFileOutputStreamConfig, CompressedFileOutputStream> defaultLzmaStreamFunction() {
		return new Function<CompressedFileOutputStreamConfig, CompressedFileOutputStream>() {

			@Override
			public CompressedFileOutputStream apply(CompressedFileOutputStreamConfig config) {
				try {
					return new CompressedFileOutputStreamImpl(new LZMACompressorOutputStream(config.getOutputStream()));
				}
				catch (IOException e) {
					throw new RuntimeException(e);
				}
			}

		};
	}

	private static Function<CompressedFileOutputStreamConfig, CompressedFileOutputStream> defaultPack200StreamFunction() {
		return new Function<CompressedFileOutputStreamConfig, CompressedFileOutputStream>() {

			@Override
			public CompressedFileOutputStream apply(CompressedFileOutputStreamConfig config) {
				try {
					CompressedFileOutputStreamConfigPack200Impl packConfig = (CompressedFileOutputStreamConfigPack200Impl) config;
					return new CompressedFileOutputStreamImpl(new Pack200CompressorOutputStream(packConfig.getOutputStream(), packConfig.getMode(), packConfig.getProperties()));
				}
				catch (IOException e) {
					throw new RuntimeException(e);
				}
			}

		};
	}

	private static Function<CompressedFileOutputStreamConfig, CompressedFileOutputStream> defaultXzStreamFunction() {
		return new Function<CompressedFileOutputStreamConfig, CompressedFileOutputStream>() {

			@Override
			public CompressedFileOutputStream apply(CompressedFileOutputStreamConfig config) {
				try {
					CompressedFileOutputStreamConfigXzImpl xzConfig = (CompressedFileOutputStreamConfigXzImpl) config;
					return new CompressedFileOutputStreamImpl(new XZCompressorOutputStream(xzConfig.getOutputStream(), xzConfig.getPreset()));
				}
				catch (IOException e) {
					throw new RuntimeException(e);
				}
			}

		};
	}

}
