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
package com.alanbuttars.commons.compress.util;

import static com.alanbuttars.commons.compress.util.Archives.ZIP;
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

import org.junit.Test;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

/**
 * Test class for {@link Archives}.
 * 
 * @author Alan Buttars
 *
 */
public class ArchivesTest {

	@Test
	public void testExtractZip() throws IOException {
		testExtract(ZIP);
	}

	@Test
	public void testArchiveZip() throws IOException {
		testArchive(ZIP);
	}

	private void testExtract(String archiveType) throws IOException {
		try (Reader reader = new FileReader(getConfig(archiveType))) {
			List<Archive> archives = new Gson().fromJson(reader, new TypeToken<List<Archive>>() {
			}.getType());
			for (Archive archive : archives) {
				File source = getArchive(archiveType, archive.getFileName());
				File destination = Files.createTempDirectory(archiveType).toFile();
				Archives.extract(archiveType, source, destination);

				assertTrue(destination.exists());
				assertFiles(archive, destination);
			}
		}
	}

	private void testArchive(String archiveType) throws IOException {
		try (Reader reader = new FileReader(getConfig(archiveType))) {
			List<Archive> archives = new Gson().fromJson(reader, new TypeToken<List<Archive>>() {
			}.getType());
			for (Archive archive : archives) {
				File extractSource = getArchive(archiveType, archive.getFileName());
				File extractDestination = Files.createTempDirectory(archiveType).toFile();
				Archives.extract(archiveType, extractSource, extractDestination);

				File archiveDestination = Files.createTempFile(archiveType, "." + archiveType.toLowerCase()).toFile();
				Archives.archive(archiveType, extractDestination, archiveDestination);

				assertTrue(archiveDestination.exists());
				assertTrue(archiveDestination.isFile());
				assertTrue(archiveDestination.length() > 0);

				File archiveSecondDestination = Files.createTempDirectory(archiveType).toFile();
				Archives.extract(archiveType, archiveDestination, archiveSecondDestination);

				assertTrue(archiveSecondDestination.exists());
				assertFiles(archive, archiveSecondDestination);
			}
		}
	}

	private File getArchiveDirectory(String archiveType) {
		return new File(getClass().getResource(archiveType).getFile());
	}

	private File getConfig(String archiveType) {
		return new File(getArchiveDirectory(archiveType), "config.json");
	}

	private File getArchive(String archiveType, String fileName) {
		return new File(getArchiveDirectory(archiveType), fileName);
	}

	private void assertFiles(Archive archive, File destination) throws IOException {
		List<File> actualFiles = listActualFiles(destination);
		assertEquals(actualFiles.size(), archive.getFiles().size());
		assertFiles(archive, destination, actualFiles, archive.getFiles());
	}

	private void assertFiles(Archive archive, File destination, List<File> actualFiles, List<ArchiveFile> expectedFiles) throws IOException {
		for (ArchiveFile expectedFile : expectedFiles) {
			File actualFile = getActualFile(archive, destination, actualFiles, expectedFile);
			assertNotNull("Could not find " + expectedFile.getFileName(), actualFile);

			byte[] actualFileBytes = Files.readAllBytes(actualFile.toPath());
			String actualFileContents = new String(actualFileBytes).trim();
			assertEquals(expectedFile.getContents(), actualFileContents);
		}
	}

	private File getActualFile(Archive archive, File destination, List<File> actualFiles, ArchiveFile expectedFile) throws IOException {
		for (File actualFile : actualFiles) {
			String actualFilePrefix = getActualFilePrefix(archive, destination);
			String actualFilePath = actualFile.getAbsolutePath().replaceFirst(actualFilePrefix, "");
			if (expectedFile.getFileName().equals(actualFilePath)) {
				return actualFile;
			}
		}
		return null;
	}

	private String getActualFilePrefix(Archive archive, File destination) {
		String archiveFilename = archive.getFileName();
		return destination.getAbsolutePath() + //
				File.separator + //
				archiveFilename.substring(0, archiveFilename.lastIndexOf(".")) + //
				File.separator;
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
