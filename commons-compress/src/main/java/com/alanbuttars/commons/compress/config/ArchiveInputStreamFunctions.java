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

import static com.alanbuttars.commons.compress.util.Archives.AR;
import static com.alanbuttars.commons.compress.util.Archives.ARJ;
import static com.alanbuttars.commons.compress.util.Archives.CPIO;
import static com.alanbuttars.commons.compress.util.Archives.DUMP;
import static com.alanbuttars.commons.compress.util.Archives.JAR;
import static com.alanbuttars.commons.compress.util.Archives.TAR;
import static com.alanbuttars.commons.compress.util.Archives.ZIP;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.compress.archivers.ArchiveException;
import org.apache.commons.compress.archivers.ArchiveInputStream;
import org.apache.commons.compress.archivers.ar.ArArchiveInputStream;
import org.apache.commons.compress.archivers.arj.ArjArchiveInputStream;
import org.apache.commons.compress.archivers.cpio.CpioArchiveInputStream;
import org.apache.commons.compress.archivers.dump.DumpArchiveInputStream;
import org.apache.commons.compress.archivers.jar.JarArchiveInputStream;
import org.apache.commons.compress.archivers.tar.TarArchiveInputStream;
import org.apache.commons.compress.archivers.zip.ZipArchiveInputStream;

import com.alanbuttars.commons.compress.config.input.ArchiveInputStreamConfig;
import com.alanbuttars.commons.compress.config.input.ArchiveInputStreamConfigArjImpl;
import com.alanbuttars.commons.compress.config.input.ArchiveInputStreamConfigCpioImpl;
import com.alanbuttars.commons.compress.config.input.ArchiveInputStreamConfigDumpImpl;
import com.alanbuttars.commons.compress.config.input.ArchiveInputStreamConfigJarImpl;
import com.alanbuttars.commons.compress.config.input.ArchiveInputStreamConfigTarImpl;
import com.alanbuttars.commons.compress.config.input.ArchiveInputStreamConfigZipImpl;
import com.alanbuttars.commons.util.functions.Function;

/**
 * The source of default functions mapping file input stream to an {@link ArchiveInputStreamConfig}.
 * 
 * @author Alan Buttars
 *
 */
class ArchiveInputStreamFunctions {

	public static Map<String, Function<InputStream, ArchiveInputStreamConfig>> defaultConfigFunctions() {
		Map<String, Function<InputStream, ArchiveInputStreamConfig>> functions = new HashMap<>();
		functions.put(AR, defaultArConfigFunction());
		functions.put(ARJ, defaultArjConfigFunction());
		functions.put(CPIO, defaultCpioConfigFunction());
		functions.put(DUMP, defaultDumpConfigFunction());
		functions.put(JAR, defaultJarConfigFunction());
		functions.put(TAR, defaultTarConfigFunction());
		functions.put(ZIP, defaultZipConfigFunction());
		return functions;
	}

	private static Function<InputStream, ArchiveInputStreamConfig> defaultArConfigFunction() {
		return new Function<InputStream, ArchiveInputStreamConfig>() {

			@Override
			public ArchiveInputStreamConfig apply(InputStream input) {
				return new ArchiveInputStreamConfig(input);
			}
		};
	}

	private static Function<InputStream, ArchiveInputStreamConfig> defaultArjConfigFunction() {
		return new Function<InputStream, ArchiveInputStreamConfig>() {

			@Override
			public ArchiveInputStreamConfig apply(InputStream input) {
				return new ArchiveInputStreamConfigArjImpl(input);
			}
		};
	}

	private static Function<InputStream, ArchiveInputStreamConfig> defaultCpioConfigFunction() {
		return new Function<InputStream, ArchiveInputStreamConfig>() {

			@Override
			public ArchiveInputStreamConfig apply(InputStream input) {
				return new ArchiveInputStreamConfigCpioImpl(input);
			}
		};
	}

	private static Function<InputStream, ArchiveInputStreamConfig> defaultDumpConfigFunction() {
		return new Function<InputStream, ArchiveInputStreamConfig>() {

			@Override
			public ArchiveInputStreamConfig apply(InputStream input) {
				return new ArchiveInputStreamConfig(input);
			}
		};
	}

	private static Function<InputStream, ArchiveInputStreamConfig> defaultJarConfigFunction() {
		return new Function<InputStream, ArchiveInputStreamConfig>() {

			@Override
			public ArchiveInputStreamConfig apply(InputStream input) {
				return new ArchiveInputStreamConfigZipImpl(input);
			}
		};
	}

	private static Function<InputStream, ArchiveInputStreamConfig> defaultTarConfigFunction() {
		return new Function<InputStream, ArchiveInputStreamConfig>() {

			@Override
			public ArchiveInputStreamConfig apply(InputStream input) {
				return new ArchiveInputStreamConfigTarImpl(input);
			}
		};
	}

	private static Function<InputStream, ArchiveInputStreamConfig> defaultZipConfigFunction() {
		return new Function<InputStream, ArchiveInputStreamConfig>() {

			@Override
			public ArchiveInputStreamConfig apply(InputStream input) {
				return new ArchiveInputStreamConfigZipImpl(input);
			}

		};
	}

	public static Map<String, Function<ArchiveInputStreamConfig, ArchiveInputStream>> defaultStreamFunctions() {
		Map<String, Function<ArchiveInputStreamConfig, ArchiveInputStream>> functions = new HashMap<>();
		functions.put(AR, defaultArStreamFunction());
		functions.put(ARJ, defaultArjStreamFunction());
		functions.put(CPIO, defaultCpioStreamFunction());
		functions.put(DUMP, defaultDumpStreamFunction());
		functions.put(JAR, defaultJarStreamFunction());
		functions.put(TAR, defaultTarStreamFunction());
		functions.put(ZIP, defaultZipStreamFunction());
		return functions;
	}

	private static Function<ArchiveInputStreamConfig, ArchiveInputStream> defaultArStreamFunction() {
		return new Function<ArchiveInputStreamConfig, ArchiveInputStream>() {

			@Override
			public ArchiveInputStream apply(ArchiveInputStreamConfig config) {
				return new ArArchiveInputStream(config.getInputStream());
			}

		};
	}

	private static Function<ArchiveInputStreamConfig, ArchiveInputStream> defaultArjStreamFunction() {
		return new Function<ArchiveInputStreamConfig, ArchiveInputStream>() {

			@Override
			public ArchiveInputStream apply(ArchiveInputStreamConfig config) {
				try {
					ArchiveInputStreamConfigArjImpl arjConfig = (ArchiveInputStreamConfigArjImpl) config;
					return new ArjArchiveInputStream(arjConfig.getInputStream(), arjConfig.getEncoding());
				}
				catch (ArchiveException e) {
					throw new RuntimeException(e);
				}
			}

		};
	}

	private static Function<ArchiveInputStreamConfig, ArchiveInputStream> defaultCpioStreamFunction() {
		return new Function<ArchiveInputStreamConfig, ArchiveInputStream>() {

			@Override
			public ArchiveInputStream apply(ArchiveInputStreamConfig config) {
				ArchiveInputStreamConfigCpioImpl cpioConfig = (ArchiveInputStreamConfigCpioImpl) config;
				return new CpioArchiveInputStream(cpioConfig.getInputStream(), cpioConfig.getBlockSize(), cpioConfig.getEncoding());
			}

		};
	}

	private static Function<ArchiveInputStreamConfig, ArchiveInputStream> defaultDumpStreamFunction() {
		return new Function<ArchiveInputStreamConfig, ArchiveInputStream>() {

			@Override
			public ArchiveInputStream apply(ArchiveInputStreamConfig config) {
				ArchiveInputStreamConfigDumpImpl dumpConfig = (ArchiveInputStreamConfigDumpImpl) config;
				try {
					return new DumpArchiveInputStream(dumpConfig.getInputStream(), dumpConfig.getEncoding());
				}
				catch (ArchiveException e) {
					throw new RuntimeException(e);
				}
			}

		};
	}

	private static Function<ArchiveInputStreamConfig, ArchiveInputStream> defaultJarStreamFunction() {
		return new Function<ArchiveInputStreamConfig, ArchiveInputStream>() {

			@Override
			public ArchiveInputStream apply(ArchiveInputStreamConfig config) {
				ArchiveInputStreamConfigJarImpl jarConfig = (ArchiveInputStreamConfigJarImpl) config;
				return new JarArchiveInputStream(jarConfig.getInputStream(), jarConfig.getEncoding());
			}

		};
	}

	private static Function<ArchiveInputStreamConfig, ArchiveInputStream> defaultTarStreamFunction() {
		return new Function<ArchiveInputStreamConfig, ArchiveInputStream>() {

			@Override
			public ArchiveInputStream apply(ArchiveInputStreamConfig config) {
				ArchiveInputStreamConfigTarImpl tarConfig = (ArchiveInputStreamConfigTarImpl) config;
				return new TarArchiveInputStream(tarConfig.getInputStream(), tarConfig.getBlockSize(), tarConfig.getRecordSize(), tarConfig.getEncoding());
			}

		};
	}

	private static Function<ArchiveInputStreamConfig, ArchiveInputStream> defaultZipStreamFunction() {
		return new Function<ArchiveInputStreamConfig, ArchiveInputStream>() {

			@Override
			public ArchiveInputStream apply(ArchiveInputStreamConfig config) {
				ArchiveInputStreamConfigZipImpl zipConfig = (ArchiveInputStreamConfigZipImpl) config;
				return new ZipArchiveInputStream(zipConfig.getInputStream(), zipConfig.getEncoding(), zipConfig.useUnicodeExtraFields(), zipConfig.allowStoredEntriesWithDataDescriptor());
			}

		};
	}

}
