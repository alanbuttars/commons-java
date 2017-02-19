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
package com.alanbuttars.commons.compress.archives.config;

import static com.alanbuttars.commons.compress.archives.util.Archives.AR;
import static com.alanbuttars.commons.compress.archives.util.Archives.ARJ;
import static com.alanbuttars.commons.compress.archives.util.Archives.CPIO;
import static com.alanbuttars.commons.compress.archives.util.Archives.DUMP;
import static com.alanbuttars.commons.compress.archives.util.Archives.JAR;
import static com.alanbuttars.commons.compress.archives.util.Archives.SEVENZ;
import static com.alanbuttars.commons.compress.archives.util.Archives.TAR;
import static com.alanbuttars.commons.compress.archives.util.Archives.ZIP;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.compress.archivers.ArchiveException;
import org.apache.commons.compress.archivers.ar.ArArchiveInputStream;
import org.apache.commons.compress.archivers.arj.ArjArchiveInputStream;
import org.apache.commons.compress.archivers.cpio.CpioArchiveInputStream;
import org.apache.commons.compress.archivers.dump.DumpArchiveInputStream;
import org.apache.commons.compress.archivers.jar.JarArchiveInputStream;
import org.apache.commons.compress.archivers.sevenz.SevenZFile;
import org.apache.commons.compress.archivers.tar.TarArchiveInputStream;
import org.apache.commons.compress.archivers.zip.ZipArchiveInputStream;

import com.alanbuttars.commons.compress.archives.config.input.ArchiveInputStreamConfig;
import com.alanbuttars.commons.compress.archives.config.input.ArchiveInputStreamConfigArjImpl;
import com.alanbuttars.commons.compress.archives.config.input.ArchiveInputStreamConfigCpioImpl;
import com.alanbuttars.commons.compress.archives.config.input.ArchiveInputStreamConfigDumpImpl;
import com.alanbuttars.commons.compress.archives.config.input.ArchiveInputStreamConfigJarImpl;
import com.alanbuttars.commons.compress.archives.config.input.ArchiveInputStreamConfigSevenZImpl;
import com.alanbuttars.commons.compress.archives.config.input.ArchiveInputStreamConfigTarImpl;
import com.alanbuttars.commons.compress.archives.config.input.ArchiveInputStreamConfigZipImpl;
import com.alanbuttars.commons.compress.archives.input.ArchiveInputStream;
import com.alanbuttars.commons.compress.archives.input.ArchiveInputStreamImpl;
import com.alanbuttars.commons.compress.archives.input.ArchiveInputStreamSevenZImpl;
import com.alanbuttars.commons.util.functions.Function;

/**
 * The source of default functions mapping file input stream to an {@link ArchiveInputStreamConfig}.
 * 
 * @author Alan Buttars
 *
 */
class ArchiveInputStreamFunctions {

	public static Map<String, Function<File, ArchiveInputStreamConfig>> defaultConfigFunctions() {
		Map<String, Function<File, ArchiveInputStreamConfig>> functions = new HashMap<>();
		functions.put(AR, defaultArConfigFunction());
		functions.put(ARJ, defaultArjConfigFunction());
		functions.put(CPIO, defaultCpioConfigFunction());
		functions.put(DUMP, defaultDumpConfigFunction());
		functions.put(JAR, defaultJarConfigFunction());
		functions.put(SEVENZ, defaultSevenZConfigFunction());
		functions.put(TAR, defaultTarConfigFunction());
		functions.put(ZIP, defaultZipConfigFunction());
		return functions;
	}

	private static Function<File, ArchiveInputStreamConfig> defaultArConfigFunction() {
		return new Function<File, ArchiveInputStreamConfig>() {

			@Override
			public ArchiveInputStreamConfig apply(File input) {
				return new ArchiveInputStreamConfig(input);
			}
		};
	}

	private static Function<File, ArchiveInputStreamConfig> defaultArjConfigFunction() {
		return new Function<File, ArchiveInputStreamConfig>() {

			@Override
			public ArchiveInputStreamConfig apply(File input) {
				return new ArchiveInputStreamConfigArjImpl(input);
			}
		};
	}

	private static Function<File, ArchiveInputStreamConfig> defaultCpioConfigFunction() {
		return new Function<File, ArchiveInputStreamConfig>() {

			@Override
			public ArchiveInputStreamConfig apply(File input) {
				return new ArchiveInputStreamConfigCpioImpl(input);
			}
		};
	}

	private static Function<File, ArchiveInputStreamConfig> defaultDumpConfigFunction() {
		return new Function<File, ArchiveInputStreamConfig>() {

			@Override
			public ArchiveInputStreamConfig apply(File input) {
				return new ArchiveInputStreamConfigDumpImpl(input);
			}
		};
	}

	private static Function<File, ArchiveInputStreamConfig> defaultJarConfigFunction() {
		return new Function<File, ArchiveInputStreamConfig>() {

			@Override
			public ArchiveInputStreamConfig apply(File input) {
				return new ArchiveInputStreamConfigJarImpl(input);
			}
		};
	}

	private static Function<File, ArchiveInputStreamConfig> defaultSevenZConfigFunction() {
		return new Function<File, ArchiveInputStreamConfig>() {

			@Override
			public ArchiveInputStreamConfig apply(File input) {
				return new ArchiveInputStreamConfigSevenZImpl(input);
			}
		};
	}

	private static Function<File, ArchiveInputStreamConfig> defaultTarConfigFunction() {
		return new Function<File, ArchiveInputStreamConfig>() {

			@Override
			public ArchiveInputStreamConfig apply(File input) {
				return new ArchiveInputStreamConfigTarImpl(input);
			}
		};
	}

	private static Function<File, ArchiveInputStreamConfig> defaultZipConfigFunction() {
		return new Function<File, ArchiveInputStreamConfig>() {

			@Override
			public ArchiveInputStreamConfig apply(File input) {
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
		functions.put(SEVENZ, defaultSevenZStreamFunction());
		functions.put(TAR, defaultTarStreamFunction());
		functions.put(ZIP, defaultZipStreamFunction());
		return functions;
	}

	private static Function<ArchiveInputStreamConfig, ArchiveInputStream> defaultArStreamFunction() {
		return new Function<ArchiveInputStreamConfig, ArchiveInputStream>() {

			@Override
			public ArchiveInputStream apply(ArchiveInputStreamConfig config) {
				return new ArchiveInputStreamImpl(new ArArchiveInputStream(config.getInputStream()));
			}

		};
	}

	private static Function<ArchiveInputStreamConfig, ArchiveInputStream> defaultArjStreamFunction() {
		return new Function<ArchiveInputStreamConfig, ArchiveInputStream>() {

			@Override
			public ArchiveInputStream apply(ArchiveInputStreamConfig config) {
				try {
					ArchiveInputStreamConfigArjImpl arjConfig = (ArchiveInputStreamConfigArjImpl) config;
					return new ArchiveInputStreamImpl(new ArjArchiveInputStream(arjConfig.getInputStream(), arjConfig.getEncoding()));
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
				return new ArchiveInputStreamImpl(new CpioArchiveInputStream(cpioConfig.getInputStream(), cpioConfig.getBlockSize(), cpioConfig.getEncoding()));
			}

		};
	}

	private static Function<ArchiveInputStreamConfig, ArchiveInputStream> defaultDumpStreamFunction() {
		return new Function<ArchiveInputStreamConfig, ArchiveInputStream>() {

			@Override
			public ArchiveInputStream apply(ArchiveInputStreamConfig config) {
				try {
					ArchiveInputStreamConfigDumpImpl dumpConfig = (ArchiveInputStreamConfigDumpImpl) config;
					return new ArchiveInputStreamImpl(new DumpArchiveInputStream(dumpConfig.getInputStream(), dumpConfig.getEncoding()));
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
				return new ArchiveInputStreamImpl(new JarArchiveInputStream(jarConfig.getInputStream(), jarConfig.getEncoding()));
			}

		};
	}

	private static Function<ArchiveInputStreamConfig, ArchiveInputStream> defaultSevenZStreamFunction() {
		return new Function<ArchiveInputStreamConfig, ArchiveInputStream>() {

			@Override
			public ArchiveInputStream apply(ArchiveInputStreamConfig config) {
				try {
					ArchiveInputStreamConfigSevenZImpl sevenZConfig = (ArchiveInputStreamConfigSevenZImpl) config;
					return new ArchiveInputStreamSevenZImpl(new SevenZFile(sevenZConfig.getFile(), sevenZConfig.getPassword()));
				}
				catch (IOException e) {
					throw new RuntimeException(e);
				}
			}

		};
	}

	private static Function<ArchiveInputStreamConfig, ArchiveInputStream> defaultTarStreamFunction() {
		return new Function<ArchiveInputStreamConfig, ArchiveInputStream>() {

			@Override
			public ArchiveInputStream apply(ArchiveInputStreamConfig config) {
				ArchiveInputStreamConfigTarImpl tarConfig = (ArchiveInputStreamConfigTarImpl) config;
				return new ArchiveInputStreamImpl(new TarArchiveInputStream(tarConfig.getInputStream(), tarConfig.getBlockSize(), tarConfig.getRecordSize(), tarConfig.getEncoding()));
			}

		};
	}

	private static Function<ArchiveInputStreamConfig, ArchiveInputStream> defaultZipStreamFunction() {
		return new Function<ArchiveInputStreamConfig, ArchiveInputStream>() {

			@Override
			public ArchiveInputStream apply(ArchiveInputStreamConfig config) {
				ArchiveInputStreamConfigZipImpl zipConfig = (ArchiveInputStreamConfigZipImpl) config;
				return new ArchiveInputStreamImpl(
						new ZipArchiveInputStream(zipConfig.getInputStream(), zipConfig.getEncoding(), zipConfig.useUnicodeExtraFields(), zipConfig.allowStoredEntriesWithDataDescriptor()));
			}

		};
	}

}
