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
import static com.alanbuttars.commons.compress.files.util.CompressedFiles.SNAPPY;
import static com.alanbuttars.commons.compress.files.util.CompressedFiles.XZ;
import static com.alanbuttars.commons.compress.files.util.CompressedFiles.Z;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.compress.compressors.bzip2.BZip2CompressorInputStream;
import org.apache.commons.compress.compressors.deflate.DeflateCompressorInputStream;
import org.apache.commons.compress.compressors.gzip.GzipCompressorInputStream;
import org.apache.commons.compress.compressors.lzma.LZMACompressorInputStream;
import org.apache.commons.compress.compressors.pack200.Pack200CompressorInputStream;
import org.apache.commons.compress.compressors.snappy.FramedSnappyCompressorInputStream;
import org.apache.commons.compress.compressors.snappy.SnappyCompressorInputStream;
import org.apache.commons.compress.compressors.xz.XZCompressorInputStream;
import org.apache.commons.compress.compressors.z.ZCompressorInputStream;

import com.alanbuttars.commons.compress.files.config.input.CompressedFileInputStreamConfig;
import com.alanbuttars.commons.compress.files.config.input.CompressedFileInputStreamConfigBzip2Impl;
import com.alanbuttars.commons.compress.files.config.input.CompressedFileInputStreamConfigDeflateImpl;
import com.alanbuttars.commons.compress.files.config.input.CompressedFileInputStreamConfigGzipImpl;
import com.alanbuttars.commons.compress.files.config.input.CompressedFileInputStreamConfigPack200Impl;
import com.alanbuttars.commons.compress.files.config.input.CompressedFileInputStreamConfigSnappyImpl;
import com.alanbuttars.commons.compress.files.config.input.CompressedFileInputStreamConfigXzImpl;
import com.alanbuttars.commons.compress.files.input.CompressedFileInputStream;
import com.alanbuttars.commons.compress.files.input.CompressedFileInputStreamImpl;
import com.alanbuttars.commons.util.functions.Function;

/**
 * The source of default functions mapping file input stream to an {@link CompressedFileInputStreamConfig}.
 * 
 * @author Alan Buttars
 *
 */
public class CompressedFileInputStreamFunctions {

	public static Map<String, Function<InputStream, CompressedFileInputStreamConfig>> defaultConfigFunctions() {
		Map<String, Function<InputStream, CompressedFileInputStreamConfig>> functions = new HashMap<>();
		functions.put(BZIP2, defaultBzip2ConfigFunction());
		functions.put(DEFLATE, defaultDeflateConfigFunction());
		functions.put(GZIP, defaultGzipConfigFunction());
		functions.put(LZMA, defaultLzmaConfigFunction());
		functions.put(PACK200, defaultPack200ConfigFunction());
		functions.put(SNAPPY, defaultSnappyConfigFunction());
		functions.put(XZ, defaultXzConfigFunction());
		functions.put(Z, defaultZConfigFunction());
		return functions;
	}

	private static Function<InputStream, CompressedFileInputStreamConfig> defaultBzip2ConfigFunction() {
		return new Function<InputStream, CompressedFileInputStreamConfig>() {

			@Override
			public CompressedFileInputStreamConfig apply(InputStream input) {
				return new CompressedFileInputStreamConfigBzip2Impl(input);
			}

		};
	}

	private static Function<InputStream, CompressedFileInputStreamConfig> defaultDeflateConfigFunction() {
		return new Function<InputStream, CompressedFileInputStreamConfig>() {

			@Override
			public CompressedFileInputStreamConfig apply(InputStream input) {
				return new CompressedFileInputStreamConfigDeflateImpl(input);
			}

		};
	}

	private static Function<InputStream, CompressedFileInputStreamConfig> defaultGzipConfigFunction() {
		return new Function<InputStream, CompressedFileInputStreamConfig>() {

			@Override
			public CompressedFileInputStreamConfig apply(InputStream input) {
				return new CompressedFileInputStreamConfigGzipImpl(input);
			}

		};
	}

	private static Function<InputStream, CompressedFileInputStreamConfig> defaultLzmaConfigFunction() {
		return new Function<InputStream, CompressedFileInputStreamConfig>() {

			@Override
			public CompressedFileInputStreamConfig apply(InputStream input) {
				return new CompressedFileInputStreamConfig(input);
			}

		};
	}

	private static Function<InputStream, CompressedFileInputStreamConfig> defaultPack200ConfigFunction() {
		return new Function<InputStream, CompressedFileInputStreamConfig>() {

			@Override
			public CompressedFileInputStreamConfig apply(InputStream input) {
				return new CompressedFileInputStreamConfigPack200Impl(input);
			}

		};
	}

	private static Function<InputStream, CompressedFileInputStreamConfig> defaultSnappyConfigFunction() {
		return new Function<InputStream, CompressedFileInputStreamConfig>() {

			@Override
			public CompressedFileInputStreamConfig apply(InputStream input) {
				return new CompressedFileInputStreamConfigSnappyImpl(input);
			}

		};
	}

	private static Function<InputStream, CompressedFileInputStreamConfig> defaultXzConfigFunction() {
		return new Function<InputStream, CompressedFileInputStreamConfig>() {

			@Override
			public CompressedFileInputStreamConfig apply(InputStream input) {
				return new CompressedFileInputStreamConfigXzImpl(input);
			}

		};
	}

	private static Function<InputStream, CompressedFileInputStreamConfig> defaultZConfigFunction() {
		return new Function<InputStream, CompressedFileInputStreamConfig>() {

			@Override
			public CompressedFileInputStreamConfig apply(InputStream input) {
				return new CompressedFileInputStreamConfig(input);
			}

		};
	}

	public static Map<String, Function<CompressedFileInputStreamConfig, CompressedFileInputStream>> defaultStreamFunctions() {
		Map<String, Function<CompressedFileInputStreamConfig, CompressedFileInputStream>> functions = new HashMap<>();
		functions.put(BZIP2, defaultBzip2StreamFunction());
		functions.put(DEFLATE, defaultDeflateStreamFunction());
		functions.put(GZIP, defaultGzipStreamFunction());
		functions.put(LZMA, defaultLzmaStreamFunction());
		functions.put(PACK200, defaultPack200StreamFunction());
		functions.put(SNAPPY, defaultSnappyStreamFunction());
		functions.put(XZ, defaultXzStreamFunction());
		functions.put(Z, defaultZStreamFunction());
		return functions;
	}

	private static Function<CompressedFileInputStreamConfig, CompressedFileInputStream> defaultBzip2StreamFunction() {
		return new Function<CompressedFileInputStreamConfig, CompressedFileInputStream>() {

			@Override
			public CompressedFileInputStream apply(CompressedFileInputStreamConfig config) {
				try {
					CompressedFileInputStreamConfigBzip2Impl bzip2Config = (CompressedFileInputStreamConfigBzip2Impl) config;
					return new CompressedFileInputStreamImpl(new BZip2CompressorInputStream(bzip2Config.getInputStream(), bzip2Config.decompressConcatenated()));
				}
				catch (IOException e) {
					throw new RuntimeException(e);
				}
			}

		};
	}

	private static Function<CompressedFileInputStreamConfig, CompressedFileInputStream> defaultDeflateStreamFunction() {
		return new Function<CompressedFileInputStreamConfig, CompressedFileInputStream>() {

			@Override
			public CompressedFileInputStream apply(CompressedFileInputStreamConfig config) {
				CompressedFileInputStreamConfigDeflateImpl deflateConfig = (CompressedFileInputStreamConfigDeflateImpl) config;
				return new CompressedFileInputStreamImpl(new DeflateCompressorInputStream(deflateConfig.getInputStream(), deflateConfig.getParameters()));
			}

		};
	}

	private static Function<CompressedFileInputStreamConfig, CompressedFileInputStream> defaultGzipStreamFunction() {
		return new Function<CompressedFileInputStreamConfig, CompressedFileInputStream>() {

			@Override
			public CompressedFileInputStream apply(CompressedFileInputStreamConfig config) {
				try {
					CompressedFileInputStreamConfigGzipImpl gzipConfig = (CompressedFileInputStreamConfigGzipImpl) config;
					return new CompressedFileInputStreamImpl(new GzipCompressorInputStream(gzipConfig.getInputStream(), gzipConfig.decompressConcatenated()));
				}
				catch (IOException e) {
					throw new RuntimeException(e);
				}
			}

		};
	}

	private static Function<CompressedFileInputStreamConfig, CompressedFileInputStream> defaultLzmaStreamFunction() {
		return new Function<CompressedFileInputStreamConfig, CompressedFileInputStream>() {

			@Override
			public CompressedFileInputStream apply(CompressedFileInputStreamConfig config) {
				try {
					return new CompressedFileInputStreamImpl(new LZMACompressorInputStream(config.getInputStream()));
				}
				catch (IOException e) {
					throw new RuntimeException(e);
				}
			}

		};
	}

	private static Function<CompressedFileInputStreamConfig, CompressedFileInputStream> defaultPack200StreamFunction() {
		return new Function<CompressedFileInputStreamConfig, CompressedFileInputStream>() {

			@Override
			public CompressedFileInputStream apply(CompressedFileInputStreamConfig config) {
				try {
					CompressedFileInputStreamConfigPack200Impl packConfig = (CompressedFileInputStreamConfigPack200Impl) config;
					return new CompressedFileInputStreamImpl(new Pack200CompressorInputStream(packConfig.getInputStream(), packConfig.getMode(), packConfig.getProperties()));
				}
				catch (IOException e) {
					throw new RuntimeException(e);
				}
			}

		};
	}

	private static Function<CompressedFileInputStreamConfig, CompressedFileInputStream> defaultSnappyStreamFunction() {
		return new Function<CompressedFileInputStreamConfig, CompressedFileInputStream>() {

			@Override
			public CompressedFileInputStream apply(CompressedFileInputStreamConfig config) {
				try {
					CompressedFileInputStreamConfigSnappyImpl snappyConfig = (CompressedFileInputStreamConfigSnappyImpl) config;
					if (snappyConfig.getFormat() == CompressedFileInputStreamConfigSnappyImpl.Format.FRAMED) {
						return new CompressedFileInputStreamImpl(new FramedSnappyCompressorInputStream(snappyConfig.getInputStream(), snappyConfig.getDialect()));
					}
					return new CompressedFileInputStreamImpl(new SnappyCompressorInputStream(snappyConfig.getInputStream(), snappyConfig.getBlockSize()));
				}
				catch (IOException e) {
					throw new RuntimeException(e);
				}
			}

		};
	}

	private static Function<CompressedFileInputStreamConfig, CompressedFileInputStream> defaultXzStreamFunction() {
		return new Function<CompressedFileInputStreamConfig, CompressedFileInputStream>() {

			@Override
			public CompressedFileInputStream apply(CompressedFileInputStreamConfig config) {
				try {
					CompressedFileInputStreamConfigXzImpl xzConfig = (CompressedFileInputStreamConfigXzImpl) config;
					return new CompressedFileInputStreamImpl(new XZCompressorInputStream(xzConfig.getInputStream(), xzConfig.decompressConcatenated()));
				}
				catch (IOException e) {
					throw new RuntimeException(e);
				}
			}

		};
	}

	private static Function<CompressedFileInputStreamConfig, CompressedFileInputStream> defaultZStreamFunction() {
		return new Function<CompressedFileInputStreamConfig, CompressedFileInputStream>() {

			@Override
			public CompressedFileInputStream apply(CompressedFileInputStreamConfig config) {
				try {
					return new CompressedFileInputStreamImpl(new ZCompressorInputStream(config.getInputStream()));
				}
				catch (IOException e) {
					throw new RuntimeException(e);
				}
			}

		};
	}

}
