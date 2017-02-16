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
package com.alanbuttars.commons.compress.files.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.util.List;

import com.alanbuttars.commons.compress.archives.util.ArchiveFile;
import com.alanbuttars.commons.compress.files.config.input.FileInputStreamConfig;
import com.alanbuttars.commons.compress.files.config.output.FileOutputStreamConfig;
import com.alanbuttars.commons.util.functions.Function;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

/**
 * Utility class for {@link Files} integration tests.
 * 
 * @author Alan Buttars
 *
 */
public class FilesIntegrationAbstractTest {

	protected void testDecompress(String fileType) throws IOException {
		testDecompress(fileType, null);
	}

	protected void testDecompress(String fileType, //
			Function<InputStream, FileInputStreamConfig> streamConfigFunction) throws IOException {
		try (Reader reader = new FileReader(getConfig(fileType))) {
			List<ArchiveFile> testFiles = new Gson().fromJson(reader, new TypeToken<List<ArchiveFile>>() {
			}.getType());
			for (ArchiveFile testFile : testFiles) {
				File source = getFile(fileType, testFile.getFileName());
				File destination = File.createTempFile(testFile.getFileName(), ".tmp");
				Files.decompress(fileType, source, destination, streamConfigFunction, null);

				assertTrue(destination.exists());
				assertFile(testFile, destination);
			}
		}
	}

	protected void testCompress(String fileType) throws IOException {
		testCompress(fileType, null, null);
	}

	protected void testCompress(String fileType, //
			Function<InputStream, FileInputStreamConfig> inputStreamConfigFunction, //
			Function<OutputStream, FileOutputStreamConfig> outputStreamConfigFunction) throws IOException {
		try (Reader reader = new FileReader(getConfig(fileType))) {
			List<ArchiveFile> testFiles = new Gson().fromJson(reader, new TypeToken<List<ArchiveFile>>() {
			}.getType());
			for (ArchiveFile testFile : testFiles) {
				File decompressSource = getFile(fileType, testFile.getFileName());
				File decompressDestination = File.createTempFile(testFile.getFileName(), ".tmp");
				Files.decompress(fileType, decompressSource, decompressDestination, inputStreamConfigFunction, null);

				File compressDestination = File.createTempFile(fileType + "__", "." + fileType.toLowerCase());
				Files.compress(fileType, decompressDestination, compressDestination, outputStreamConfigFunction, null);

				assertTrue(compressDestination.exists());
				assertTrue(compressDestination.isFile());
				assertTrue(compressDestination.length() > 0);

				File decompressSecondDestination = File.createTempFile(fileType + "__", "." + fileType.toLowerCase());
				Files.decompress(fileType, compressDestination, decompressSecondDestination, inputStreamConfigFunction, null);

				assertTrue(decompressSecondDestination.exists());
				assertFile(testFile, decompressSecondDestination);
			}
		}
	}

	private File getFileDirectory(String fileType) {
		return new File(getClass().getResource(fileType).getFile());
	}

	private File getConfig(String fileType) {
		return new File(getFileDirectory(fileType), fileType.toLowerCase() + ".json");
	}

	private File getFile(String fileType, String fileName) {
		return new File(getFileDirectory(fileType), fileName);
	}

	private void assertFile(ArchiveFile expectedFile, File actualFile) throws IOException {
		if (expectedFile.getContents() != null) {
			byte[] actualFileBytes = java.nio.file.Files.readAllBytes(actualFile.toPath());
			String actualFileContents = new String(actualFileBytes).trim();
			assertEquals(expectedFile.getContents(), actualFileContents);
		}
	}
}
