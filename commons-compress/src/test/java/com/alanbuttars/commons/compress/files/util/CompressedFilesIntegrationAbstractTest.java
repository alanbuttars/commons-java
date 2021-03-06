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
import java.io.Reader;
import java.util.List;

import com.alanbuttars.commons.compress.archives.util.ArchiveFile;
import com.alanbuttars.commons.compress.util.FilesFunction;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

/**
 * Utility class for {@link CompressedFiles} integration tests.
 * 
 * @author Alan Buttars
 *
 */
public class CompressedFilesIntegrationAbstractTest {

	protected void testDecompress(String fileType, //
			FilesFunction decompressionFunction) throws IOException {
		try (Reader reader = new FileReader(getConfig(fileType))) {
			List<ArchiveFile> testFiles = new Gson().fromJson(reader, new TypeToken<List<ArchiveFile>>() {
			}.getType());
			for (ArchiveFile testFile : testFiles) {
				File source = getFile(fileType, testFile.getFileName());
				source.deleteOnExit();
				
				File destination = decompressionFunction.act(source);
				destination.deleteOnExit();
				assertTrue(destination.exists());
				assertFile(testFile, destination);
			}
		}
	}

	protected void testCompress(String fileType, //
			FilesFunction compressionFunction, //
			FilesFunction decompressionFunction) throws IOException {
		try (Reader reader = new FileReader(getConfig(fileType))) {
			List<ArchiveFile> testFiles = new Gson().fromJson(reader, new TypeToken<List<ArchiveFile>>() {
			}.getType());
			for (ArchiveFile testFile : testFiles) {
				File decompressSource = getFile(fileType, testFile.getFileName());
				decompressSource.deleteOnExit();
				
				File decompressDestination = decompressionFunction.act(decompressSource);
				decompressDestination.deleteOnExit();
				
				File compressDestination = compressionFunction.act(decompressDestination);
				compressDestination.deleteOnExit();
				assertTrue(compressDestination.exists());
				assertTrue(compressDestination.isFile());
				assertTrue(compressDestination.length() > 0);

				File decompressSecondDestination = decompressionFunction.act(compressDestination);
				decompressSecondDestination.deleteOnExit();
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
