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

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.ar.ArArchiveEntry;
import org.apache.commons.compress.archivers.arj.ArjArchiveEntry;
import org.apache.commons.compress.archivers.cpio.CpioArchiveEntry;
import org.apache.commons.compress.archivers.dump.DumpArchiveEntry;
import org.apache.commons.compress.archivers.jar.JarArchiveEntry;
import org.apache.commons.compress.archivers.sevenz.SevenZArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;

import com.alanbuttars.commons.compress.archives.config.entry.ArchiveEntryConfig;
import com.alanbuttars.commons.compress.archives.config.entry.ArchiveEntryConfigArImpl;
import com.alanbuttars.commons.compress.archives.config.entry.ArchiveEntryConfigCpioImpl;
import com.alanbuttars.commons.compress.archives.config.entry.ArchiveEntryConfigDumpImpl;
import com.alanbuttars.commons.compress.archives.config.entry.ArchiveEntryConfigTarImpl;
import com.alanbuttars.commons.util.functions.BiFunction;
import com.alanbuttars.commons.util.functions.Function;

/**
 * The source of default functions mapping file details to an {@link ArchiveEntryConfig}.
 * 
 * @author Alan Buttars
 *
 */
class ArchiveEntryFunctions {

	public static Map<String, BiFunction<String, Long, ArchiveEntryConfig>> defaultConfigFunctions() {
		Map<String, BiFunction<String, Long, ArchiveEntryConfig>> functions = new HashMap<>();
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

	private static BiFunction<String, Long, ArchiveEntryConfig> defaultArConfigFunction() {
		return new BiFunction<String, Long, ArchiveEntryConfig>() {

			@Override
			public ArchiveEntryConfig apply(String entryName, Long fileLength) {
				return new ArchiveEntryConfigArImpl(entryName, fileLength);
			}

		};
	}

	private static BiFunction<String, Long, ArchiveEntryConfig> defaultArjConfigFunction() {
		return new BiFunction<String, Long, ArchiveEntryConfig>() {

			@Override
			public ArchiveEntryConfig apply(String entryName, Long fileLength) {
				return new ArchiveEntryConfig(entryName);
			}

		};
	}

	private static BiFunction<String, Long, ArchiveEntryConfig> defaultCpioConfigFunction() {
		return new BiFunction<String, Long, ArchiveEntryConfig>() {

			@Override
			public ArchiveEntryConfig apply(String entryName, Long fileLength) {
				return new ArchiveEntryConfigCpioImpl(entryName, fileLength);
			}

		};
	}

	private static BiFunction<String, Long, ArchiveEntryConfig> defaultDumpConfigFunction() {
		return new BiFunction<String, Long, ArchiveEntryConfig>() {

			@Override
			public ArchiveEntryConfig apply(String entryName, Long fileLength) {
				return new ArchiveEntryConfigDumpImpl(entryName);
			}

		};
	}

	private static BiFunction<String, Long, ArchiveEntryConfig> defaultJarConfigFunction() {
		return new BiFunction<String, Long, ArchiveEntryConfig>() {

			@Override
			public ArchiveEntryConfig apply(String entryName, Long fileLength) {
				return new ArchiveEntryConfig(entryName);
			}

		};
	}

	private static BiFunction<String, Long, ArchiveEntryConfig> defaultSevenZConfigFunction() {
		return new BiFunction<String, Long, ArchiveEntryConfig>() {

			@Override
			public ArchiveEntryConfig apply(String entryName, Long fileLength) {
				return new ArchiveEntryConfig(entryName);
			}

		};
	}

	private static BiFunction<String, Long, ArchiveEntryConfig> defaultTarConfigFunction() {
		return new BiFunction<String, Long, ArchiveEntryConfig>() {

			@Override
			public ArchiveEntryConfig apply(String entryName, Long fileLength) {
				return new ArchiveEntryConfigTarImpl(entryName, fileLength);
			}

		};
	}

	private static BiFunction<String, Long, ArchiveEntryConfig> defaultZipConfigFunction() {
		return new BiFunction<String, Long, ArchiveEntryConfig>() {

			@Override
			public ArchiveEntryConfig apply(String entryName, Long fileLength) {
				return new ArchiveEntryConfig(entryName);
			}

		};
	}

	public static Map<String, Function<ArchiveEntryConfig, ArchiveEntry>> defaultEntryFunctions() {
		Map<String, Function<ArchiveEntryConfig, ArchiveEntry>> functions = new HashMap<>();
		functions.put(AR, defaultArEntryFunction());
		functions.put(ARJ, defaultArjEntryFunction());
		functions.put(CPIO, defaultCpioEntryFunction());
		functions.put(DUMP, defaultDumpEntryFunction());
		functions.put(JAR, defaultJarEntryFunction());
		functions.put(SEVENZ, defaultSevenZEntryFunction());
		functions.put(TAR, defaultTarEntryFunction());
		functions.put(ZIP, defaultZipEntryFunction());
		return functions;
	}

	private static Function<ArchiveEntryConfig, ArchiveEntry> defaultArEntryFunction() {
		return new Function<ArchiveEntryConfig, ArchiveEntry>() {

			@Override
			public ArchiveEntry apply(ArchiveEntryConfig config) {
				ArchiveEntryConfigArImpl arConfig = (ArchiveEntryConfigArImpl) config;
				return new ArArchiveEntry(arConfig.getEntryName(), arConfig.getLength(), arConfig.getUserId(), arConfig.getGroupId(), arConfig.getMode(), arConfig.getLastModified());
			}

		};
	}

	private static Function<ArchiveEntryConfig, ArchiveEntry> defaultArjEntryFunction() {
		return new Function<ArchiveEntryConfig, ArchiveEntry>() {

			@Override
			public ArchiveEntry apply(ArchiveEntryConfig config) {
				return new ArjArchiveEntry();
			}

		};
	}

	private static Function<ArchiveEntryConfig, ArchiveEntry> defaultCpioEntryFunction() {
		return new Function<ArchiveEntryConfig, ArchiveEntry>() {

			@Override
			public ArchiveEntry apply(ArchiveEntryConfig config) {
				ArchiveEntryConfigCpioImpl cpioConfig = (ArchiveEntryConfigCpioImpl) config;
				return new CpioArchiveEntry(cpioConfig.getFormat(), cpioConfig.getEntryName(), cpioConfig.getLength());
			}

		};
	}

	private static Function<ArchiveEntryConfig, ArchiveEntry> defaultDumpEntryFunction() {
		return new Function<ArchiveEntryConfig, ArchiveEntry>() {

			@Override
			public ArchiveEntry apply(ArchiveEntryConfig config) {
				ArchiveEntryConfigDumpImpl dumpConfig = (ArchiveEntryConfigDumpImpl) config;
				return new DumpArchiveEntry(dumpConfig.getEntryName(), dumpConfig.getSimpleName());
			}

		};
	}

	private static Function<ArchiveEntryConfig, ArchiveEntry> defaultJarEntryFunction() {
		return new Function<ArchiveEntryConfig, ArchiveEntry>() {

			@Override
			public ArchiveEntry apply(ArchiveEntryConfig config) {
				return new JarArchiveEntry(config.getEntryName());
			}

		};
	}

	private static Function<ArchiveEntryConfig, ArchiveEntry> defaultSevenZEntryFunction() {
		return new Function<ArchiveEntryConfig, ArchiveEntry>() {

			@Override
			public ArchiveEntry apply(ArchiveEntryConfig config) {
				SevenZArchiveEntry entry = new SevenZArchiveEntry();
				entry.setName(config.getEntryName());
				return entry;
			}

		};
	}

	private static Function<ArchiveEntryConfig, ArchiveEntry> defaultTarEntryFunction() {
		return new Function<ArchiveEntryConfig, ArchiveEntry>() {

			@Override
			public ArchiveEntry apply(ArchiveEntryConfig config) {
				ArchiveEntryConfigTarImpl tarConfig = (ArchiveEntryConfigTarImpl) config;
				TarArchiveEntry entry = new TarArchiveEntry(tarConfig.getEntryName(), tarConfig.preserveLeadingSlas());
				entry.setSize(tarConfig.getLength());
				return entry;
			}

		};
	}

	private static Function<ArchiveEntryConfig, ArchiveEntry> defaultZipEntryFunction() {
		return new Function<ArchiveEntryConfig, ArchiveEntry>() {

			@Override
			public ArchiveEntry apply(ArchiveEntryConfig config) {
				return new ZipArchiveEntry(config.getEntryName());
			}

		};
	}

}
