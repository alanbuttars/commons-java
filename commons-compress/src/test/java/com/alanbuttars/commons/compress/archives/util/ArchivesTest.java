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
import static org.junit.Assert.fail;

import java.io.File;
import java.io.IOException;

import org.junit.Test;

import com.alanbuttars.commons.compress.archives.util.Archives;
import com.alanbuttars.commons.compress.files.util.Files;

/**
 * Test class for {@link Archives}.
 * 
 * @author Alan Buttars
 *
 */
public class ArchivesTest {

	private File file() throws IOException {
		return File.createTempFile("archive", ".tmp");
	}

	private File directory() throws IOException {
		return java.nio.file.Files.createTempDirectory("archive").toFile();
	}

	@Test
	public void testArchiveFilePathsArchiveTypeNull() throws IOException {
		try {
			Archives.archive(null, directory().getAbsolutePath(), file().getAbsolutePath());
			fail();
		}
		catch (IllegalArgumentException e) {
			assertEquals("Archive type must be non-null", e.getMessage());
		}
	}

	@Test
	public void testArchiveFilePathsArchiveTypeEmpty() throws IOException {
		try {
			Archives.archive("", directory().getAbsolutePath(), file().getAbsolutePath());
			fail();
		}
		catch (IllegalArgumentException e) {
			assertEquals("Archive type must be non-empty", e.getMessage());
		}
	}

	@Test
	public void testArchiveFilePathsArchiveTypeBlank() throws IOException {
		try {
			Archives.archive(" ", directory().getAbsolutePath(), file().getAbsolutePath());
			fail();
		}
		catch (IllegalArgumentException e) {
			assertEquals("Archive type must be non-empty", e.getMessage());
		}
	}

	@Test
	public void testArchiveFilePathsArchiveTypeInvalid() throws IOException {
		try {
			Archives.archive(Files.BZIP2, directory().getAbsolutePath(), file().getAbsolutePath());
			fail();
		}
		catch (IllegalArgumentException e) {
			assertEquals("File type " + Files.BZIP2 + " cannot be archived; use Files.compress()", e.getMessage());
		}
	}

	@Test
	public void testArchiveFilePathsArchiveTypeUnrecognized() throws IOException {
		try {
			Archives.archive("blah", directory().getAbsolutePath(), file().getAbsolutePath());
			fail();
		}
		catch (IllegalArgumentException e) {
			assertEquals("Archive type blah is not recognized", e.getMessage());
		}
	}

	@Test
	public void testArchiveFilePathsSourceNull() throws IOException {
		try {
			Archives.archive(Archives.AR, null, file().getAbsolutePath());
			fail();
		}
		catch (IllegalArgumentException e) {
			assertEquals("Source file path must be non-null", e.getMessage());
		}
	}

	@Test
	public void testArchiveFilePathsSourceEmpty() throws IOException {
		try {
			Archives.archive(Archives.AR, "", file().getAbsolutePath());
			fail();
		}
		catch (IllegalArgumentException e) {
			assertEquals("Source file path must be non-empty", e.getMessage());
		}
	}

	@Test
	public void testArchiveFilePathsSourceBlank() throws IOException {
		try {
			Archives.archive(Archives.AR, " ", file().getAbsolutePath());
			fail();
		}
		catch (IllegalArgumentException e) {
			assertEquals("Source file path must be non-empty", e.getMessage());
		}
	}

	@Test
	public void testArchiveFilePathsSourceDoesntExist() throws IOException {
		File source = new File("i-dont-exist.txt");
		try {
			Archives.archive(Archives.AR, source.getAbsolutePath(), file().getAbsolutePath());
			fail();
		}
		catch (IllegalArgumentException e) {
			assertEquals("Source " + source.getAbsolutePath() + " does not exist", e.getMessage());
		}
	}

	@Test
	public void testArchiveFilePathsSourceNotDirectory() throws IOException {
		File source = file();
		try {
			Archives.archive(Archives.AR, source.getAbsolutePath(), file().getAbsolutePath());
			fail();
		}
		catch (IllegalArgumentException e) {
			assertEquals("Source " + source.getAbsolutePath() + " must be a directory; to compress a file use File.compress()", e.getMessage());
		}
	}

	@Test
	public void testArchiveFilePathsSourceUnreadable() throws IOException {
		File source = directory();
		source.setReadable(false);
		try {
			Archives.archive(Archives.AR, source.getAbsolutePath(), file().getAbsolutePath());
			fail();
		}
		catch (IllegalArgumentException e) {
			assertEquals("Source " + source.getAbsolutePath() + " is not readable", e.getMessage());
		}
	}

	@Test
	public void testArchiveFilePathsDestinationNull() throws IOException {
		try {
			Archives.archive(Archives.AR, directory().getAbsolutePath(), null);
			fail();
		}
		catch (IllegalArgumentException e) {
			assertEquals("Destination file path must be non-null", e.getMessage());
		}
	}

	@Test
	public void testArchiveFilePathsDestinationEmpty() throws IOException {
		try {
			Archives.archive(Archives.AR, directory().getAbsolutePath(), "");
			fail();
		}
		catch (IllegalArgumentException e) {
			assertEquals("Destination file path must be non-empty", e.getMessage());
		}
	}

	@Test
	public void testArchiveFilePathsDestinationBlank() throws IOException {
		try {
			Archives.archive(Archives.AR, directory().getAbsolutePath(), " ");
			fail();
		}
		catch (IllegalArgumentException e) {
			assertEquals("Destination file path must be non-empty", e.getMessage());
		}
	}

	@Test
	public void testArchiveFilePathsDestinationIsDirectory() throws IOException {
		File destination = directory();
		try {
			Archives.archive(Archives.AR, directory().getAbsolutePath(), destination.getAbsolutePath());
			fail();
		}
		catch (IllegalArgumentException e) {
			assertEquals("Destination " + destination.getAbsolutePath() + " must not be a directory", e.getMessage());
		}
	}

	@Test
	public void testArchiveFilePathsDestinationUnwriteable() throws IOException {
		File destination = file();
		destination.setWritable(false);
		try {
			Archives.archive(Archives.AR, directory().getAbsolutePath(), destination.getAbsolutePath());
			fail();
		}
		catch (IllegalArgumentException e) {
			assertEquals("Destination " + destination.getAbsolutePath() + " is not writable", e.getMessage());
		}
	}

	@Test
	public void testExtractFilePathsArchiveTypeNull() throws IOException {
		try {
			Archives.extract(null, file().getAbsolutePath(), directory().getAbsolutePath());
			fail();
		}
		catch (IllegalArgumentException e) {
			assertEquals("Archive type must be non-null", e.getMessage());
		}
	}

	@Test
	public void testExtractFilePathsArchiveTypeEmpty() throws IOException {
		try {
			Archives.extract("", file().getAbsolutePath(), directory().getAbsolutePath());
			fail();
		}
		catch (IllegalArgumentException e) {
			assertEquals("Archive type must be non-empty", e.getMessage());
		}
	}

	@Test
	public void testExtractFilePathsArchiveTypeBlank() throws IOException {
		try {
			Archives.extract(" ", file().getAbsolutePath(), directory().getAbsolutePath());
			fail();
		}
		catch (IllegalArgumentException e) {
			assertEquals("Archive type must be non-empty", e.getMessage());
		}
	}

	@Test
	public void testExtractFilePathsArchiveTypeInvalid() throws IOException {
		try {
			Archives.extract(Files.BZIP2, file().getAbsolutePath(), directory().getAbsolutePath());
			fail();
		}
		catch (IllegalArgumentException e) {
			assertEquals("File type " + Files.BZIP2 + " cannot be extracted; use Files.decompress()", e.getMessage());
		}
	}

	@Test
	public void testExtractFilePathsArchiveTypeUnrecognized() throws IOException {
		try {
			Archives.extract("blah", file().getAbsolutePath(), directory().getAbsolutePath());
			fail();
		}
		catch (IllegalArgumentException e) {
			assertEquals("Archive type blah is not recognized", e.getMessage());
		}
	}

	@Test
	public void testExtractFilePathsSourceNull() throws IOException {
		try {
			Archives.extract(Archives.AR, null, directory().getAbsolutePath());
			fail();
		}
		catch (IllegalArgumentException e) {
			assertEquals("Source file path must be non-null", e.getMessage());
		}
	}

	@Test
	public void testExtractFilePathsSourceEmpty() throws IOException {
		try {
			Archives.extract(Archives.AR, "", directory().getAbsolutePath());
			fail();
		}
		catch (IllegalArgumentException e) {
			assertEquals("Source file path must be non-empty", e.getMessage());
		}
	}

	@Test
	public void testExtractFilePathsSourceBlank() throws IOException {
		try {
			Archives.extract(Archives.AR, " ", directory().getAbsolutePath());
			fail();
		}
		catch (IllegalArgumentException e) {
			assertEquals("Source file path must be non-empty", e.getMessage());
		}
	}

	@Test
	public void testExtractFilePathsSourceDoesntExist() throws IOException {
		File source = new File("i-dont-exist.txt");
		try {
			Archives.extract(Archives.AR, source.getAbsolutePath(), directory().getAbsolutePath());
			fail();
		}
		catch (IllegalArgumentException e) {
			assertEquals("Source " + source.getAbsolutePath() + " does not exist", e.getMessage());
		}
	}

	@Test
	public void testExtractFilePathsSourceNotFile() throws IOException {
		File source = directory();
		try {
			Archives.extract(Archives.AR, source.getAbsolutePath(), directory().getAbsolutePath());
			fail();
		}
		catch (IllegalArgumentException e) {
			assertEquals("Source " + source.getAbsolutePath() + " must not be a directory", e.getMessage());
		}
	}

	@Test
	public void testExtractFilePathsSourceUnreadable() throws IOException {
		File source = file();
		source.setReadable(false);
		try {
			Archives.extract(Archives.AR, source.getAbsolutePath(), directory().getAbsolutePath());
			fail();
		}
		catch (IllegalArgumentException e) {
			assertEquals("Source " + source.getAbsolutePath() + " is not readable", e.getMessage());
		}
	}

	@Test
	public void testExtractFilePathsDestinationNull() throws IOException {
		try {
			Archives.extract(Archives.AR, file().getAbsolutePath(), null);
			fail();
		}
		catch (IllegalArgumentException e) {
			assertEquals("Destination file path must be non-null", e.getMessage());
		}
	}

	@Test
	public void testExtractFilePathsDestinationEmpty() throws IOException {
		try {
			Archives.extract(Archives.AR, file().getAbsolutePath(), "");
			fail();
		}
		catch (IllegalArgumentException e) {
			assertEquals("Destination file path must be non-empty", e.getMessage());
		}
	}

	@Test
	public void testExtractFilePathsDestinationBlank() throws IOException {
		try {
			Archives.extract(Archives.AR, file().getAbsolutePath(), " ");
			fail();
		}
		catch (IllegalArgumentException e) {
			assertEquals("Destination file path must be non-empty", e.getMessage());
		}
	}

	@Test
	public void testExtractFilePathsDestinationIsFile() throws IOException {
		File destination = file();
		try {
			Archives.extract(Archives.AR, file().getAbsolutePath(), destination.getAbsolutePath());
			fail();
		}
		catch (IllegalArgumentException e) {
			assertEquals("Destination " + destination.getAbsolutePath() + " must be a directory", e.getMessage());
		}
	}

	@Test
	public void testExtractFilePathsDestinationUnwriteable() throws IOException {
		File destination = directory();
		destination.setWritable(false);
		try {
			Archives.extract(Archives.AR, file().getAbsolutePath(), destination.getAbsolutePath());
			fail();
		}
		catch (IllegalArgumentException e) {
			assertEquals("Destination " + destination.getAbsolutePath() + " is not writable", e.getMessage());
		}
	}

	@Test
	public void testArchiveFilesArchiveTypeNull() throws IOException {
		try {
			Archives.archive(null, directory(), file());
			fail();
		}
		catch (IllegalArgumentException e) {
			assertEquals("Archive type must be non-null", e.getMessage());
		}
	}

	@Test
	public void testArchiveFilesArchiveTypeEmpty() throws IOException {
		try {
			Archives.archive("", directory(), file());
			fail();
		}
		catch (IllegalArgumentException e) {
			assertEquals("Archive type must be non-empty", e.getMessage());
		}
	}

	@Test
	public void testArchiveFilesArchiveTypeBlank() throws IOException {
		try {
			Archives.archive(" ", directory(), file());
			fail();
		}
		catch (IllegalArgumentException e) {
			assertEquals("Archive type must be non-empty", e.getMessage());
		}
	}

	@Test
	public void testArchiveFilesArchiveTypeInvalid() throws IOException {
		try {
			Archives.archive(Files.BZIP2, directory(), file());
			fail();
		}
		catch (IllegalArgumentException e) {
			assertEquals("File type " + Files.BZIP2 + " cannot be archived; use Files.compress()", e.getMessage());
		}
	}

	@Test
	public void testArchiveFilesArchiveTypeUnrecognized() throws IOException {
		try {
			Archives.archive("blah", directory(), file());
			fail();
		}
		catch (IllegalArgumentException e) {
			assertEquals("Archive type blah is not recognized", e.getMessage());
		}
	}

	@Test
	public void testArchiveFilesSourceNull() throws IOException {
		try {
			Archives.archive(Archives.AR, null, file());
			fail();
		}
		catch (IllegalArgumentException e) {
			assertEquals("Source must be non-null", e.getMessage());
		}
	}

	@Test
	public void testArchiveFilesSourceDoesntExist() throws IOException {
		File source = new File("i-dont-exist.txt");
		try {
			Archives.archive(Archives.AR, source, file());
			fail();
		}
		catch (IllegalArgumentException e) {
			assertEquals("Source " + source.getAbsolutePath() + " does not exist", e.getMessage());
		}
	}

	@Test
	public void testArchiveFilesSourceNotDirectory() throws IOException {
		File source = file();
		try {
			Archives.archive(Archives.AR, source, file());
			fail();
		}
		catch (IllegalArgumentException e) {
			assertEquals("Source " + source.getAbsolutePath() + " must be a directory; to compress a file use File.compress()", e.getMessage());
		}
	}

	@Test
	public void testArchiveFilesSourceUnreadable() throws IOException {
		File source = directory();
		source.setReadable(false);
		try {
			Archives.archive(Archives.AR, source, file());
			fail();
		}
		catch (IllegalArgumentException e) {
			assertEquals("Source " + source.getAbsolutePath() + " is not readable", e.getMessage());
		}
	}

	@Test
	public void testArchiveFilesDestinationNull() throws IOException {
		try {
			Archives.archive(Archives.AR, directory(), null);
			fail();
		}
		catch (IllegalArgumentException e) {
			assertEquals("Destination must be non-null", e.getMessage());
		}
	}

	@Test
	public void testArchiveFilesDestinationIsDirectory() throws IOException {
		File destination = directory();
		try {
			Archives.archive(Archives.AR, directory(), destination);
			fail();
		}
		catch (IllegalArgumentException e) {
			assertEquals("Destination " + destination.getAbsolutePath() + " must not be a directory", e.getMessage());
		}
	}

	@Test
	public void testArchiveFilesDestinationUnwriteable() throws IOException {
		File destination = file();
		destination.setWritable(false);
		try {
			Archives.archive(Archives.AR, directory(), destination);
			fail();
		}
		catch (IllegalArgumentException e) {
			assertEquals("Destination " + destination.getAbsolutePath() + " is not writable", e.getMessage());
		}
	}

	@Test
	public void testExtractFilesArchiveTypeNull() throws IOException {
		try {
			Archives.extract(null, file(), directory());
			fail();
		}
		catch (IllegalArgumentException e) {
			assertEquals("Archive type must be non-null", e.getMessage());
		}
	}

	@Test
	public void testExtractFilesArchiveTypeEmpty() throws IOException {
		try {
			Archives.extract("", file(), directory());
			fail();
		}
		catch (IllegalArgumentException e) {
			assertEquals("Archive type must be non-empty", e.getMessage());
		}
	}

	@Test
	public void testExtractFilesArchiveTypeBlank() throws IOException {
		try {
			Archives.extract(" ", file(), directory());
			fail();
		}
		catch (IllegalArgumentException e) {
			assertEquals("Archive type must be non-empty", e.getMessage());
		}
	}

	@Test
	public void testExtractFilesArchiveTypeInvalid() throws IOException {
		try {
			Archives.extract(Files.BZIP2, file(), directory());
			fail();
		}
		catch (IllegalArgumentException e) {
			assertEquals("File type " + Files.BZIP2 + " cannot be extracted; use Files.decompress()", e.getMessage());
		}
	}

	@Test
	public void testExtractFilesArchiveTypeUnrecognized() throws IOException {
		try {
			Archives.extract("blah", file(), directory());
			fail();
		}
		catch (IllegalArgumentException e) {
			assertEquals("Archive type blah is not recognized", e.getMessage());
		}
	}

	@Test
	public void testExtractFilesSourceNull() throws IOException {
		try {
			Archives.extract(Archives.AR, null, directory());
			fail();
		}
		catch (IllegalArgumentException e) {
			assertEquals("Source must be non-null", e.getMessage());
		}
	}

	@Test
	public void testExtractFilesSourceDoesntExist() throws IOException {
		File source = new File("i-dont-exist.txt");
		try {
			Archives.extract(Archives.AR, source, directory());
			fail();
		}
		catch (IllegalArgumentException e) {
			assertEquals("Source " + source.getAbsolutePath() + " does not exist", e.getMessage());
		}
	}

	@Test
	public void testExtractFilesSourceNotFile() throws IOException {
		File source = directory();
		try {
			Archives.extract(Archives.AR, source, directory());
			fail();
		}
		catch (IllegalArgumentException e) {
			assertEquals("Source " + source.getAbsolutePath() + " must not be a directory", e.getMessage());
		}
	}

	@Test
	public void testExtractFilesSourceUnreadable() throws IOException {
		File source = file();
		source.setReadable(false);
		try {
			Archives.extract(Archives.AR, source, directory());
			fail();
		}
		catch (IllegalArgumentException e) {
			assertEquals("Source " + source.getAbsolutePath() + " is not readable", e.getMessage());
		}
	}

	@Test
	public void testExtractFilesDestinationNull() throws IOException {
		try {
			Archives.extract(Archives.AR, file(), null);
			fail();
		}
		catch (IllegalArgumentException e) {
			assertEquals("Destination must be non-null", e.getMessage());
		}
	}

	@Test
	public void testExtractFilesDestinationIsFile() throws IOException {
		File destination = file();
		try {
			Archives.extract(Archives.AR, file(), destination);
			fail();
		}
		catch (IllegalArgumentException e) {
			assertEquals("Destination " + destination.getAbsolutePath() + " must be a directory", e.getMessage());
		}
	}

	@Test
	public void testExtractFilesDestinationUnwriteable() throws IOException {
		File destination = directory();
		destination.setWritable(false);
		try {
			Archives.extract(Archives.AR, file(), destination);
			fail();
		}
		catch (IllegalArgumentException e) {
			assertEquals("Destination " + destination.getAbsolutePath() + " is not writable", e.getMessage());
		}
	}

}
