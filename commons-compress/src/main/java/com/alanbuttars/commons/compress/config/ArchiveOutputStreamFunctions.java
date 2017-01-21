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
import static com.alanbuttars.commons.compress.util.Archives.CPIO;
import static com.alanbuttars.commons.compress.util.Archives.JAR;
import static com.alanbuttars.commons.compress.util.Archives.TAR;
import static com.alanbuttars.commons.compress.util.Archives.ZIP;

import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.compress.archivers.ArchiveOutputStream;
import org.apache.commons.compress.archivers.ar.ArArchiveOutputStream;
import org.apache.commons.compress.archivers.cpio.CpioArchiveOutputStream;
import org.apache.commons.compress.archivers.jar.JarArchiveOutputStream;
import org.apache.commons.compress.archivers.tar.TarArchiveOutputStream;
import org.apache.commons.compress.archivers.zip.ZipArchiveOutputStream;

import com.alanbuttars.commons.compress.config.output.ArchiveOutputStreamConfig;
import com.alanbuttars.commons.compress.config.output.ArchiveOutputStreamConfigArImpl;
import com.alanbuttars.commons.compress.config.output.ArchiveOutputStreamConfigCpioImpl;
import com.alanbuttars.commons.compress.config.output.ArchiveOutputStreamConfigTarImpl;
import com.alanbuttars.commons.compress.config.output.ArchiveOutputStreamConfigZipImpl;
import com.alanbuttars.commons.util.functions.Function;

/**
 * The source of default functions mapping file output stream to an {@link ArchiveOutputStreamConfig}.
 * 
 * @author Alan Buttars
 *
 */
class ArchiveOutputStreamFunctions {

	public static Map<String, Function<OutputStream, ArchiveOutputStreamConfig>> defaultConfigFunctions() {
		Map<String, Function<OutputStream, ArchiveOutputStreamConfig>> functions = new HashMap<>();
		functions.put(AR, defaultArConfigFunction());
		functions.put(CPIO, defaultCpioConfigFunction());
		functions.put(JAR, defaultJarConfigFunction());
		functions.put(TAR, defaultTarConfigFunction());
		functions.put(ZIP, defaultZipConfigFunction());
		return functions;
	}

	private static Function<OutputStream, ArchiveOutputStreamConfig> defaultArConfigFunction() {
		return new Function<OutputStream, ArchiveOutputStreamConfig>() {

			@Override
			public ArchiveOutputStreamConfig apply(OutputStream output) {
				return new ArchiveOutputStreamConfigArImpl(output);
			}

		};
	}

	private static Function<OutputStream, ArchiveOutputStreamConfig> defaultCpioConfigFunction() {
		return new Function<OutputStream, ArchiveOutputStreamConfig>() {

			@Override
			public ArchiveOutputStreamConfig apply(OutputStream output) {
				return new ArchiveOutputStreamConfigCpioImpl(output);
			}

		};
	}

	private static Function<OutputStream, ArchiveOutputStreamConfig> defaultJarConfigFunction() {
		return defaultZipConfigFunction();
	}

	private static Function<OutputStream, ArchiveOutputStreamConfig> defaultTarConfigFunction() {
		return new Function<OutputStream, ArchiveOutputStreamConfig>() {

			@Override
			public ArchiveOutputStreamConfig apply(OutputStream output) {
				return new ArchiveOutputStreamConfigTarImpl(output);
			}

		};
	}

	private static Function<OutputStream, ArchiveOutputStreamConfig> defaultZipConfigFunction() {
		return new Function<OutputStream, ArchiveOutputStreamConfig>() {

			@Override
			public ArchiveOutputStreamConfig apply(OutputStream output) {
				return new ArchiveOutputStreamConfigZipImpl(output);
			}

		};
	}

	public static Map<String, Function<ArchiveOutputStreamConfig, ArchiveOutputStream>> defaultStreamFunctions() {
		Map<String, Function<ArchiveOutputStreamConfig, ArchiveOutputStream>> functions = new HashMap<>();
		functions.put(AR, defaultArStreamFunction());
		functions.put(CPIO, defaultCpioStreamFunction());
		functions.put(JAR, defaultJarStreamFunction());
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
				return stream;
			}

		};
	}

	private static Function<ArchiveOutputStreamConfig, ArchiveOutputStream> defaultCpioStreamFunction() {
		return new Function<ArchiveOutputStreamConfig, ArchiveOutputStream>() {

			@Override
			public ArchiveOutputStream apply(ArchiveOutputStreamConfig config) {
				ArchiveOutputStreamConfigCpioImpl cpioConfig = (ArchiveOutputStreamConfigCpioImpl) config;
				return new CpioArchiveOutputStream(cpioConfig.getOutputStream(), cpioConfig.getFormat(), cpioConfig.getBlockSize(), cpioConfig.getEncoding());
			}

		};
	}

	private static Function<ArchiveOutputStreamConfig, ArchiveOutputStream> defaultJarStreamFunction() {
		return new Function<ArchiveOutputStreamConfig, ArchiveOutputStream>() {

			@Override
			public ArchiveOutputStream apply(ArchiveOutputStreamConfig config) {
				ArchiveOutputStreamConfigZipImpl zipConfig = (ArchiveOutputStreamConfigZipImpl) config;
				JarArchiveOutputStream stream = new JarArchiveOutputStream(zipConfig.getOutputStream(), zipConfig.getEncoding());
				stream.setComment(zipConfig.getComment());
				stream.setCreateUnicodeExtraFields(zipConfig.getUnicodeExtraFieldPolicy());
				stream.setFallbackToUTF8(zipConfig.fallbackToUTF8());
				stream.setLevel(zipConfig.getLevel());
				stream.setMethod(zipConfig.getMethod());
				stream.setUseLanguageEncodingFlag(zipConfig.useLanguageEncoding());
				stream.setUseZip64(zipConfig.getZip64Mode());
				return stream;
			}

		};
	}

	private static Function<ArchiveOutputStreamConfig, ArchiveOutputStream> defaultTarStreamFunction() {
		return new Function<ArchiveOutputStreamConfig, ArchiveOutputStream>() {

			@Override
			public ArchiveOutputStream apply(ArchiveOutputStreamConfig config) {
				ArchiveOutputStreamConfigTarImpl tarConfig = (ArchiveOutputStreamConfigTarImpl) config;
				TarArchiveOutputStream stream = new TarArchiveOutputStream(tarConfig.getOutputStream(), tarConfig.getBlockSize(), tarConfig.getRecordSize(), tarConfig.getEncoding());
				stream.setAddPaxHeadersForNonAsciiNames(tarConfig.isAddPaxHeadersForNonAsciiNames());
				stream.setBigNumberMode(tarConfig.getBigNumberMode());
				stream.setLongFileMode(tarConfig.getLongFileMode());
				return stream;
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
				return stream;
			}

		};
	}

}
