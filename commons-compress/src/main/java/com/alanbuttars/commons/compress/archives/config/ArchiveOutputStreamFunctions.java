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
import static com.alanbuttars.commons.compress.archives.util.Archives.CPIO;
import static com.alanbuttars.commons.compress.archives.util.Archives.JAR;
import static com.alanbuttars.commons.compress.archives.util.Archives.SEVENZ;
import static com.alanbuttars.commons.compress.archives.util.Archives.TAR;
import static com.alanbuttars.commons.compress.archives.util.Archives.ZIP;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.compress.archivers.ar.ArArchiveOutputStream;
import org.apache.commons.compress.archivers.cpio.CpioArchiveOutputStream;
import org.apache.commons.compress.archivers.jar.JarArchiveOutputStream;
import org.apache.commons.compress.archivers.sevenz.SevenZOutputFile;
import org.apache.commons.compress.archivers.tar.TarArchiveOutputStream;
import org.apache.commons.compress.archivers.zip.ZipArchiveOutputStream;

import com.alanbuttars.commons.compress.archives.config.output.ArchiveOutputStreamConfig;
import com.alanbuttars.commons.compress.archives.config.output.ArchiveOutputStreamConfigArImpl;
import com.alanbuttars.commons.compress.archives.config.output.ArchiveOutputStreamConfigCpioImpl;
import com.alanbuttars.commons.compress.archives.config.output.ArchiveOutputStreamConfigJarImpl;
import com.alanbuttars.commons.compress.archives.config.output.ArchiveOutputStreamConfigSevenZImpl;
import com.alanbuttars.commons.compress.archives.config.output.ArchiveOutputStreamConfigTarImpl;
import com.alanbuttars.commons.compress.archives.config.output.ArchiveOutputStreamConfigZipImpl;
import com.alanbuttars.commons.compress.archives.output.ArchiveOutputStream;
import com.alanbuttars.commons.compress.archives.output.ArchiveOutputStreamImpl;
import com.alanbuttars.commons.compress.archives.output.ArchiveOutputStreamSevenZImpl;
import com.alanbuttars.commons.util.functions.Function;

/**
 * The source of default functions mapping file output stream to an {@link ArchiveOutputStreamConfig}.
 * 
 * @author Alan Buttars
 *
 */
class ArchiveOutputStreamFunctions {

	public static Map<String, Function<File, ArchiveOutputStreamConfig>> defaultConfigFunctions() {
		Map<String, Function<File, ArchiveOutputStreamConfig>> functions = new HashMap<>();
		functions.put(AR, defaultArConfigFunction());
		functions.put(CPIO, defaultCpioConfigFunction());
		functions.put(JAR, defaultJarConfigFunction());
		functions.put(SEVENZ, defaultSevenZConfigFunction());
		functions.put(TAR, defaultTarConfigFunction());
		functions.put(ZIP, defaultZipConfigFunction());
		return functions;
	}

	private static Function<File, ArchiveOutputStreamConfig> defaultArConfigFunction() {
		return new Function<File, ArchiveOutputStreamConfig>() {

			@Override
			public ArchiveOutputStreamConfig apply(File output) {
				return new ArchiveOutputStreamConfigArImpl(output);
			}

		};
	}

	private static Function<File, ArchiveOutputStreamConfig> defaultCpioConfigFunction() {
		return new Function<File, ArchiveOutputStreamConfig>() {

			@Override
			public ArchiveOutputStreamConfig apply(File output) {
				return new ArchiveOutputStreamConfigCpioImpl(output);
			}

		};
	}

	private static Function<File, ArchiveOutputStreamConfig> defaultJarConfigFunction() {
		return new Function<File, ArchiveOutputStreamConfig>() {

			@Override
			public ArchiveOutputStreamConfig apply(File output) {
				return new ArchiveOutputStreamConfigJarImpl(output);
			}

		};
	}

	private static Function<File, ArchiveOutputStreamConfig> defaultSevenZConfigFunction() {
		return new Function<File, ArchiveOutputStreamConfig>() {

			@Override
			public ArchiveOutputStreamConfig apply(File output) {
				return new ArchiveOutputStreamConfigSevenZImpl(output);
			}

		};
	}

	private static Function<File, ArchiveOutputStreamConfig> defaultTarConfigFunction() {
		return new Function<File, ArchiveOutputStreamConfig>() {

			@Override
			public ArchiveOutputStreamConfig apply(File output) {
				return new ArchiveOutputStreamConfigTarImpl(output);
			}

		};
	}

	private static Function<File, ArchiveOutputStreamConfig> defaultZipConfigFunction() {
		return new Function<File, ArchiveOutputStreamConfig>() {

			@Override
			public ArchiveOutputStreamConfig apply(File output) {
				return new ArchiveOutputStreamConfigZipImpl(output);
			}

		};
	}

	public static Map<String, Function<ArchiveOutputStreamConfig, ArchiveOutputStream>> defaultStreamFunctions() {
		Map<String, Function<ArchiveOutputStreamConfig, ArchiveOutputStream>> functions = new HashMap<>();
		functions.put(AR, defaultArStreamFunction());
		functions.put(CPIO, defaultCpioStreamFunction());
		functions.put(JAR, defaultJarStreamFunction());
		functions.put(SEVENZ, defaultSevenZStreamFunction());
		functions.put(TAR, defaultTarStreamFunction());
		functions.put(ZIP, defaultZipStreamFunction());
		return functions;
	}

	private static Function<ArchiveOutputStreamConfig, ArchiveOutputStream> defaultArStreamFunction() {
		return new Function<ArchiveOutputStreamConfig, ArchiveOutputStream>() {

			@Override
			public ArchiveOutputStream apply(ArchiveOutputStreamConfig config) {
				ArchiveOutputStreamConfigArImpl arConfig = (ArchiveOutputStreamConfigArImpl) config;
				ArArchiveOutputStream stream = new ArArchiveOutputStream(arConfig.getOutputStream());
				stream.setLongFileMode(arConfig.getLongFileMode());
				return new ArchiveOutputStreamImpl(stream);
			}

		};
	}

	private static Function<ArchiveOutputStreamConfig, ArchiveOutputStream> defaultCpioStreamFunction() {
		return new Function<ArchiveOutputStreamConfig, ArchiveOutputStream>() {

			@Override
			public ArchiveOutputStream apply(ArchiveOutputStreamConfig config) {
				ArchiveOutputStreamConfigCpioImpl cpioConfig = (ArchiveOutputStreamConfigCpioImpl) config;
				return new ArchiveOutputStreamImpl(new CpioArchiveOutputStream(cpioConfig.getOutputStream(), cpioConfig.getFormat(), cpioConfig.getBlockSize(), cpioConfig.getEncoding()));
			}

		};
	}

	private static Function<ArchiveOutputStreamConfig, ArchiveOutputStream> defaultJarStreamFunction() {
		return new Function<ArchiveOutputStreamConfig, ArchiveOutputStream>() {

			@Override
			public ArchiveOutputStream apply(ArchiveOutputStreamConfig config) {
				ArchiveOutputStreamConfigJarImpl jarConfig = (ArchiveOutputStreamConfigJarImpl) config;
				JarArchiveOutputStream stream = new JarArchiveOutputStream(jarConfig.getOutputStream(), jarConfig.getEncoding());
				stream.setComment(jarConfig.getComment());
				stream.setCreateUnicodeExtraFields(jarConfig.getUnicodeExtraFieldPolicy());
				stream.setFallbackToUTF8(jarConfig.fallbackToUTF8());
				stream.setLevel(jarConfig.getLevel());
				stream.setMethod(jarConfig.getMethod());
				stream.setUseLanguageEncodingFlag(jarConfig.useLanguageEncoding());
				stream.setUseZip64(jarConfig.getZip64Mode());
				return new ArchiveOutputStreamImpl(stream);
			}

		};
	}

	private static Function<ArchiveOutputStreamConfig, ArchiveOutputStream> defaultSevenZStreamFunction() {
		return new Function<ArchiveOutputStreamConfig, ArchiveOutputStream>() {

			@Override
			public ArchiveOutputStream apply(ArchiveOutputStreamConfig config) {
				try {
					ArchiveOutputStreamConfigSevenZImpl sevenZConfig = (ArchiveOutputStreamConfigSevenZImpl) config;
					SevenZOutputFile sevenZFile = new SevenZOutputFile(sevenZConfig.getFile());
					sevenZFile.setContentMethods(sevenZConfig.getContentMethods());
					return new ArchiveOutputStreamSevenZImpl(sevenZFile);
				}
				catch (IOException e) {
					throw new RuntimeException(e);
				}
			}

		};
	}

	private static Function<ArchiveOutputStreamConfig, ArchiveOutputStream> defaultTarStreamFunction() {
		return new Function<ArchiveOutputStreamConfig, ArchiveOutputStream>() {

			@Override
			public ArchiveOutputStream apply(ArchiveOutputStreamConfig config) {
				ArchiveOutputStreamConfigTarImpl tarConfig = (ArchiveOutputStreamConfigTarImpl) config;
				TarArchiveOutputStream stream = new TarArchiveOutputStream(tarConfig.getOutputStream(), tarConfig.getBlockSize(), tarConfig.getRecordSize(), tarConfig.getEncoding());
				stream.setAddPaxHeadersForNonAsciiNames(tarConfig.addPaxHeadersForNonAsciiNames());
				stream.setBigNumberMode(tarConfig.getBigNumberMode());
				stream.setLongFileMode(tarConfig.getLongFileMode());
				return new ArchiveOutputStreamImpl(stream);
			}

		};
	}

	private static Function<ArchiveOutputStreamConfig, ArchiveOutputStream> defaultZipStreamFunction() {
		return new Function<ArchiveOutputStreamConfig, ArchiveOutputStream>() {

			@Override
			public ArchiveOutputStream apply(ArchiveOutputStreamConfig config) {
				ArchiveOutputStreamConfigZipImpl zipConfig = (ArchiveOutputStreamConfigZipImpl) config;
				ZipArchiveOutputStream stream = new ZipArchiveOutputStream(zipConfig.getOutputStream());
				stream.setComment(zipConfig.getComment());
				stream.setCreateUnicodeExtraFields(zipConfig.getUnicodeExtraFieldPolicy());
				stream.setEncoding(zipConfig.getEncoding());
				stream.setFallbackToUTF8(zipConfig.fallbackToUTF8());
				stream.setLevel(zipConfig.getLevel());
				stream.setMethod(zipConfig.getMethod());
				stream.setUseLanguageEncodingFlag(zipConfig.useLanguageEncoding());
				stream.setUseZip64(zipConfig.getZip64Mode());
				return new ArchiveOutputStreamImpl(stream);
			}

		};
	}

}
