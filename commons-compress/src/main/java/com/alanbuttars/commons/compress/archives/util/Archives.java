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
package com.alanbuttars.commons.compress.archives.util;

import static com.alanbuttars.commons.compress.archives.config.ArchiveConfigs.ENTRY_CONFIG_FUNCTIONS;
import static com.alanbuttars.commons.compress.archives.config.ArchiveConfigs.ENTRY_FUNCTIONS;
import static com.alanbuttars.commons.compress.archives.config.ArchiveConfigs.INPUT_CONFIG_FUNCTIONS;
import static com.alanbuttars.commons.compress.archives.config.ArchiveConfigs.INPUT_STREAM_FUNCTIONS;
import static com.alanbuttars.commons.compress.archives.config.ArchiveConfigs.OUTPUT_CONFIG_FUNCTIONS;
import static com.alanbuttars.commons.compress.archives.config.ArchiveConfigs.OUTPUT_STREAM_FUNCTIONS;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.compress.archivers.ArchiveEntry;

import com.alanbuttars.commons.compress.archives.config.ArchiveConfigs;
import com.alanbuttars.commons.compress.archives.config.entry.ArchiveEntryConfig;
import com.alanbuttars.commons.compress.archives.config.input.ArchiveInputStreamConfig;
import com.alanbuttars.commons.compress.archives.config.output.ArchiveOutputStreamConfig;
import com.alanbuttars.commons.compress.archives.input.ArchiveInputStream;
import com.alanbuttars.commons.compress.archives.output.ArchiveOutputStream;
import com.alanbuttars.commons.compress.files.util.CompressedFiles;
import com.alanbuttars.commons.util.functions.BiFunction;
import com.alanbuttars.commons.util.functions.Function;
import com.alanbuttars.commons.util.validators.Arguments;

/**
 * Utility functions class for file archives.
 * 
 * @author Alan Buttars
 *
 */
public class Archives {

	public static String SEVENZ = "7z";
	public static String AR = "ar";
	public static String ARJ = "arj";
	public static String CPIO = "cpio";
	public static String DUMP = "dump";
	public static String JAR = "jar";
	public static String TAR = "tar";
	public static String ZIP = "zip";

	public static Set<String> ARCHIVE_TYPES = new HashSet<>(Arrays.asList(//
			SEVENZ, //
			AR, //
			ARJ, //
			CPIO, //
			DUMP, //
			JAR, //
			TAR, //
			ZIP));

	/**
	 * Extracts an archive to a file destination.
	 * 
	 * @param archiveType
	 *            non-null archive type
	 * @param sourceFilePath
	 *            non-null file path pointing to a file archive which is to be extracted
	 * @param destinationFilePath
	 *            non-null directory path
	 * @throws IOException
	 *             on any IO exception
	 */
	public static void extract(String archiveType, String sourceFilePath, String destinationFilePath) throws IOException {
		validateExtractArchiveType(archiveType);
		validateSourceFilePath(sourceFilePath);
		validateDestinationFilePath(destinationFilePath);
		extract(archiveType, new File(sourceFilePath), new File(destinationFilePath));
	}

	/**
	 * Extracts an archive to a file destination.
	 * 
	 * @param archiveType
	 *            non-null archive type
	 * @param source
	 *            non-null file archive which is to be extracted
	 * @param destination
	 *            non-null directory
	 * @throws IOException
	 *             on any IO exception
	 */
	public static void extract(String archiveType, File source, File destination) throws IOException {
		extract(archiveType, source, destination, null, null);
	}

	/**
	 * Extracts an archive to a file destination using inline stream mapping functions.
	 * 
	 * @param archiveType
	 *            non-null archive type
	 * @param source
	 *            non-null file archive which is to be extracted
	 * @param destination
	 *            non-null directory
	 * @param streamConfigFunction
	 *            nullable function which maps the <code>source</code>'s input stream to a stream config. If null, the
	 *            default function associated with <code>archiveType</code> in
	 *            {@link ArchiveConfigs#INPUT_CONFIG_FUNCTIONS} is used
	 * @param streamFunction
	 *            nullable function which maps the <code>source</code>'s stream config to an archive stream. If null,
	 *            the default function associated with <code>archiveType</code> in
	 *            {@link ArchiveConfigs#INPUT_STREAM_FUNCTIONS} is used
	 * @throws IOException
	 *             on any IO exception
	 */
	public static void extract(//
			String archiveType, //
			File source, //
			File destination, //
			Function<File, ArchiveInputStreamConfig> streamConfigFunction, //
			Function<ArchiveInputStreamConfig, ArchiveInputStream> streamFunction) throws IOException {
		validateExtractArchiveType(archiveType);

		Arguments.verify(source != null, "Source must be non-null");
		Arguments.verify(source.exists(), "Source " + source.getAbsolutePath() + " does not exist");
		Arguments.verify(source.isFile(), "Source " + source.getAbsolutePath() + " must not be a directory; to extract a directory use Archives.extract()");
		Arguments.verify(source.canRead(), "Source " + source.getAbsolutePath() + " is not readable");

		Arguments.verify(destination != null, "Destination must be non-null");
		Arguments.verify(destination.isDirectory(), "Destination " + destination.getAbsolutePath() + " must be a directory");
		Arguments.verify(destination.canWrite(), "Destination " + destination.getAbsolutePath() + " is not writable");

		try (ArchiveInputStream archiveInputStream = createArchiveInputStream(archiveType, source, streamConfigFunction, streamFunction)) {
			readFromArchive(archiveInputStream, destination);
		}
	}

	private static void readFromArchive(ArchiveInputStream archiveInputStream, File destination) throws IOException {
		ArchiveEntry archiveEntry = null;
		while ((archiveEntry = archiveInputStream.getNextEntry()) != null) {
			File outputFile = new File(destination, archiveEntry.getName());
			if (archiveEntry.isDirectory()) {
				outputFile.mkdirs();
			}
			else {
				outputFile.getParentFile().mkdirs();
				try (OutputStream outputStream = new FileOutputStream(outputFile);
						BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(outputStream)) {
					byte[] content = new byte[1024];
					int length = 0;
					while ((length = archiveInputStream.read(content)) > 0) {
						bufferedOutputStream.write(content, 0, length);
						bufferedOutputStream.flush();
					}
				}
			}
		}
	}

	/**
	 * Archives a directory to a file destination.
	 * 
	 * @param archiveType
	 *            non-null archive type
	 * @param sourceFilePath
	 *            non-null directory to be archived
	 * @param destinationFilePath
	 *            non-null file archive destination
	 * @throws IOException
	 *             on any IO exception
	 */
	public static void archive(String archiveType, String sourceFilePath, String destinationFilePath) throws IOException {
		validateArchiveArchiveType(archiveType);
		validateSourceFilePath(sourceFilePath);
		validateDestinationFilePath(destinationFilePath);
		archive(archiveType, new File(sourceFilePath), new File(destinationFilePath));
	}

	/**
	 * Archives a directory to a file destination.
	 * 
	 * @param archiveType
	 *            non-null archive type
	 * @param source
	 *            non-null directory to be archived
	 * @param destination
	 *            non-null file archive destination
	 * @throws IOException
	 *             on any IO exception
	 */
	public static void archive(String archiveType, File source, File destination) throws IOException {
		archive(archiveType, source, destination, null, null, null, null);
	}

	/**
	 * Archives a directory to a file destination using inline stream mapping functions.
	 * 
	 * @param archiveType
	 *            non-null archive type
	 * @param source
	 *            non-null directory to be archived
	 * @param destination
	 *            non-null file archive destination
	 * @param streamConfigFunction
	 *            nullable function which maps the <code>source</code>'s output stream to a stream config. If null, the
	 *            default function associated with <code>archiveType</code> in
	 *            {@link ArchiveConfigs#OUTPUT_CONFIG_FUNCTIONS} is used
	 * @param streamFunction
	 *            nullable function which maps the <code>source</code>'s stream config to an archive stream. If null,
	 *            the default function associated with <code>archiveType</code> in
	 *            {@link ArchiveConfigs#OUTPUT_STREAM_FUNCTIONS} is used
	 * @throws IOException
	 *             on any IO exception
	 */
	public static void archive(//
			String archiveType, //
			File source, //
			File destination, //
			Function<File, ArchiveOutputStreamConfig> streamConfigFunction, //
			Function<ArchiveOutputStreamConfig, ArchiveOutputStream> streamFunction, //
			BiFunction<String, Long, ArchiveEntryConfig> entryConfigFunction, //
			Function<ArchiveEntryConfig, ArchiveEntry> entryFunction) throws IOException {
		validateArchiveArchiveType(archiveType);

		Arguments.verify(source != null, "Source must be non-null");
		Arguments.verify(source.exists(), "Source " + source.getAbsolutePath() + " does not exist");
		Arguments.verify(source.isDirectory(), "Source " + source.getAbsolutePath() + " must be a directory; to compress a file use File.compress()");
		Arguments.verify(source.canRead(), "Source " + source.getAbsolutePath() + " is not readable");

		Arguments.verify(destination != null, "Destination must be non-null");
		Arguments.verify(destination.isFile(), "Destination " + destination.getAbsolutePath() + " must not be a directory");
		Arguments.verify(destination.canWrite(), "Destination " + destination.getAbsolutePath() + " is not writable");

		try (ArchiveOutputStream archiveOutputStream = createArchiveOutputStream(archiveType, destination, streamConfigFunction, streamFunction)) {
			writeToArchive(archiveType, source, source, archiveOutputStream, entryConfigFunction, entryFunction);
		}
	}

	private static ArchiveInputStream createArchiveInputStream(//
			String archiveType, //
			File source, //
			Function<File, ArchiveInputStreamConfig> configFunction, //
			Function<ArchiveInputStreamConfig, ArchiveInputStream> streamFunction) {
		if (configFunction == null) {
			configFunction = INPUT_CONFIG_FUNCTIONS.get(archiveType);
			Arguments.verify(configFunction != null, "Archive type " + archiveType + " is not recognized");
		}
		if (streamFunction == null) {
			streamFunction = INPUT_STREAM_FUNCTIONS.get(archiveType);
			Arguments.verify(streamFunction != null, "Archive type " + archiveType + " is not recognized");
		}
		ArchiveInputStreamConfig config = configFunction.apply(source);
		ArchiveInputStream stream = streamFunction.apply(config);
		return stream;
	}

	private static ArchiveOutputStream createArchiveOutputStream(//
			String archiveType, //
			File file, //
			Function<File, ArchiveOutputStreamConfig> configFunction, //
			Function<ArchiveOutputStreamConfig, ArchiveOutputStream> streamFunction) {
		if (configFunction == null) {
			configFunction = OUTPUT_CONFIG_FUNCTIONS.get(archiveType);
			Arguments.verify(configFunction != null, "Archive type " + archiveType + " is not recognized");
		}
		if (streamFunction == null) {
			streamFunction = OUTPUT_STREAM_FUNCTIONS.get(archiveType);
			Arguments.verify(streamFunction != null, "Archive type " + archiveType + " is not recognized");
		}
		ArchiveOutputStreamConfig config = configFunction.apply(file);
		ArchiveOutputStream stream = streamFunction.apply(config);
		return stream;
	}

	private static ArchiveEntry createArchiveEntry(//
			String archiveType, //
			String entryName, //
			long fileLength, //
			BiFunction<String, Long, ArchiveEntryConfig> configFunction, //
			Function<ArchiveEntryConfig, ArchiveEntry> entryFunction) {
		if (configFunction == null) {
			configFunction = ENTRY_CONFIG_FUNCTIONS.get(archiveType);
		}
		if (entryFunction == null) {
			entryFunction = ENTRY_FUNCTIONS.get(archiveType);
		}
		ArchiveEntryConfig config = configFunction.apply(entryName, fileLength);
		ArchiveEntry entry = entryFunction.apply(config);
		return entry;
	}

	private static void writeToArchive(//
			String archiveType, //
			File source, //
			File currentFile, //
			ArchiveOutputStream archiveOutputStream, //
			BiFunction<String, Long, ArchiveEntryConfig> entryConfigFunction, //
			Function<ArchiveEntryConfig, ArchiveEntry> entryFunction) throws IOException {
		if (currentFile.isFile()) {
			int index = source.getAbsolutePath().length() + 1;
			String currentFilePath = currentFile.getCanonicalPath();
			String entryName = currentFilePath.substring(index);

			ArchiveEntry entry = createArchiveEntry(archiveType, entryName, currentFile.length(), entryConfigFunction, entryFunction);

			archiveOutputStream.putArchiveEntry(entry);
			try (InputStream inputStream = new FileInputStream(currentFile);
					BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream)) {
				byte[] content = new byte[1024];
				int length = 0;
				while ((length = bufferedInputStream.read(content)) > 0) {
					archiveOutputStream.write(content, 0, length);
					archiveOutputStream.flush();
				}
			}
			archiveOutputStream.closeArchiveEntry();
		}
		else {
			for (File child : currentFile.listFiles()) {
				writeToArchive(archiveType, source, child, archiveOutputStream, entryConfigFunction, entryFunction);
			}
		}
	}

	private static void validateExtractArchiveType(String archiveType) {
		validateArchiveType(archiveType);
		Arguments.verify(!CompressedFiles.COMPRESS_TYPES.contains(archiveType), "File type " + archiveType + " cannot be extracted; use Files.decompress()");
	}

	private static void validateArchiveArchiveType(String archiveType) {
		validateArchiveType(archiveType);
		Arguments.verify(!CompressedFiles.COMPRESS_TYPES.contains(archiveType), "File type " + archiveType + " cannot be archived; use Files.compress()");
	}

	private static void validateArchiveType(String archiveType) {
		Arguments.verify(archiveType != null, "Archive type must be non-null");
		Arguments.verify(!archiveType.trim().isEmpty(), "Archive type must be non-empty");
	}

	private static void validateSourceFilePath(String sourceFilePath) {
		Arguments.verify(sourceFilePath != null, "Source file path must be non-null");
		Arguments.verify(!sourceFilePath.trim().isEmpty(), "Source file path must be non-empty");
	}

	private static void validateDestinationFilePath(String destinationFilePath) {
		Arguments.verify(destinationFilePath != null, "Destination file path must be non-null");
		Arguments.verify(!destinationFilePath.trim().isEmpty(), "Destination file path must be non-empty");
	}
}
