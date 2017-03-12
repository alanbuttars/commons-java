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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

import com.alanbuttars.commons.compress.util.FilesFunction;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

/**
 * Utility class for {@link Archives} integration tests.
 * 
 * @author Alan Buttars
 *
 */
public class ArchivesIntegrationAbstractTest {

	protected void testExtract(String archiveType, //
			FilesFunction decompressFunction) throws IOException {
		try (Reader reader = new FileReader(getConfig(archiveType))) {
			List<Archive> archives = new Gson().fromJson(reader, new TypeToken<List<Archive>>() {
			}.getType());
			for (Archive archive : archives) {
				File source = getArchive(archiveType, archive.getFileName());
				File destination = decompressFunction.act(source);
				assertTrue(destination.exists());
				assertFiles(archive, destination);
			}
		}
	}

	protected void testArchive(String archiveType, //
			FilesFunction compressFunction, //
			FilesFunction decompressFunction) throws IOException {
		try (Reader reader = new FileReader(getConfig(archiveType))) {
			List<Archive> archives = new Gson().fromJson(reader, new TypeToken<List<Archive>>() {
			}.getType());
			for (Archive archive : archives) {
				File decompressSource = getArchive(archiveType, archive.getFileName());
				File decompressDestination = decompressFunction.act(decompressSource);

				File compressDestination = compressFunction.act(decompressDestination);
				assertTrue(compressDestination.exists());
				assertTrue(compressDestination.isFile());
				assertTrue(compressDestination.length() > 0);

				File decompressSecondDestination = decompressFunction.act(compressDestination);
				assertTrue(decompressSecondDestination.exists());
				assertFiles(archive, decompressSecondDestination);
			}
		}
	}

	private File getArchiveDirectory(String archiveType) {
		return new File(getClass().getResource(archiveType).getFile());
	}

	private File getConfig(String archiveType) {
		return new File(getArchiveDirectory(archiveType), archiveType.toLowerCase() + ".json");
	}

	private File getArchive(String archiveType, String fileName) {
		return new File(getArchiveDirectory(archiveType), fileName);
	}

	private void assertFiles(Archive archive, File destination) throws IOException {
		List<File> actualFiles = listActualFiles(destination);
		assertEquals(actualFiles.size(), archive.getFiles().size());
		assertFiles(destination, actualFiles, archive.getFiles());
	}

	private void assertFiles(File destination, List<File> actualFiles, List<ArchiveFile> expectedFiles) throws IOException {
		for (ArchiveFile expectedFile : expectedFiles) {
			File actualFile = getActualFile(destination, actualFiles, expectedFile);
			assertNotNull("Could not find " + expectedFile.getFileName(), actualFile);
			assertTrue(actualFile.length() > 0);

			if (expectedFile.getContents() != null) {
				byte[] actualFileBytes = Files.readAllBytes(actualFile.toPath());
				String actualFileContents = new String(actualFileBytes).trim();
				assertEquals(expectedFile.getContents(), actualFileContents);
			}
		}
	}

	private File getActualFile(File destination, List<File> actualFiles, ArchiveFile expectedFile) throws IOException {
		for (File actualFile : actualFiles) {
			String actualFilePrefix = getActualFilePrefix(destination);
			String actualFilePath = actualFile.getAbsolutePath().replaceFirst(actualFilePrefix, "");
			if (expectedFile.getFileName().equals(actualFilePath)) {
				return actualFile;
			}
		}
		return null;
	}

	private String getActualFilePrefix(File destination) {
		return destination.getAbsolutePath() + File.separator;
	}

	private List<File> listActualFiles(File file) {
		return listActualFiles(file, new ArrayList<File>());
	}

	private List<File> listActualFiles(File file, List<File> filesSoFar) {
		if (file.isFile()) {
			filesSoFar.add(file);
		}
		else {
			for (File child : file.listFiles()) {
				listActualFiles(child, filesSoFar);
			}
		}
		return filesSoFar;
	}
}
