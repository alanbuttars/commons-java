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
import static org.junit.Assert.fail;

import java.io.File;
import java.io.IOException;

import org.junit.Test;

import com.alanbuttars.commons.compress.archives.util.Archives;

/**
 * Test class for {@link CompressedFiles}.
 * 
 * @author Alan Buttars
 *
 */
public class CompressedFilesTest {

	private File file() throws IOException {
		return File.createTempFile("archive", ".tmp");
	}

	private File directory() throws IOException {
		return java.nio.file.Files.createTempDirectory("archive").toFile();
	}

	@Test
	public void testCompressFilePathsFileTypeNull() throws IOException {
		try {
			CompressedFiles.compress(null, file().getAbsolutePath(), file().getAbsolutePath());
			fail();
		}
		catch (IllegalArgumentException e) {
			assertEquals("File type must be non-null", e.getMessage());
		}
	}

	@Test
	public void testCompressFilePathsFileTypeEmpty() throws IOException {
		try {
			CompressedFiles.compress("", file().getAbsolutePath(), file().getAbsolutePath());
			fail();
		}
		catch (IllegalArgumentException e) {
			assertEquals("File type must be non-empty", e.getMessage());
		}
	}

	@Test
	public void testCompressFilePathsFileTypeBlank() throws IOException {
		try {
			CompressedFiles.compress(" ", file().getAbsolutePath(), file().getAbsolutePath());
			fail();
		}
		catch (IllegalArgumentException e) {
			assertEquals("File type must be non-empty", e.getMessage());
		}
	}

	@Test
	public void testCompressFilePathsFileTypeInvalid() throws IOException {
		try {
			CompressedFiles.compress(Archives.AR, file().getAbsolutePath(), file().getAbsolutePath());
			fail();
		}
		catch (IllegalArgumentException e) {
			assertEquals("Archive type " + Archives.AR + " cannot be compressed; use Archives.archive()", e.getMessage());
		}
	}

	@Test
	public void testCompressFilePathsFileTypeUnrecognized() throws IOException {
		try {
			CompressedFiles.compress("blah", file().getAbsolutePath(), file().getAbsolutePath());
			fail();
		}
		catch (IllegalArgumentException e) {
			assertEquals("File type blah is not recognized", e.getMessage());
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Test
	public void testCompressFilePathsSourceNull() throws IOException {
		try {
			CompressedFiles.compress(CompressedFiles.BZIP2, null, file().getAbsolutePath());
			fail();
		}
		catch (IllegalArgumentException e) {
			assertEquals("Source file path must be non-null", e.getMessage());
		}
	}

	@Test
	public void testCompressFilePathsSourceEmpty() throws IOException {
		try {
			CompressedFiles.compress(CompressedFiles.BZIP2, "", file().getAbsolutePath());
			fail();
		}
		catch (IllegalArgumentException e) {
			assertEquals("Source file path must be non-empty", e.getMessage());
		}
	}

	@Test
	public void testCompressFilePathsSourceBlank() throws IOException {
		try {
			CompressedFiles.compress(CompressedFiles.BZIP2, " ", file().getAbsolutePath());
			fail();
		}
		catch (IllegalArgumentException e) {
			assertEquals("Source file path must be non-empty", e.getMessage());
		}
	}

	@Test
	public void testCompressFilePathsSourceDoesntExist() throws IOException {
		File source = new File("i-dont-exist.txt");
		try {
			CompressedFiles.compress(CompressedFiles.BZIP2, source.getAbsolutePath(), file().getAbsolutePath());
			fail();
		}
		catch (IllegalArgumentException e) {
			assertEquals("Source " + source.getAbsolutePath() + " does not exist", e.getMessage());
		}
	}

	@Test
	public void testCompressFilePathsSourceIsDirectory() throws IOException {
		File source = directory();
		try {
			CompressedFiles.compress(CompressedFiles.BZIP2, source.getAbsolutePath(), file().getAbsolutePath());
			fail();
		}
		catch (IllegalArgumentException e) {
			assertEquals("Source " + source.getAbsolutePath() + " must not be a directory; to archive a directory use Archives.archive()", e.getMessage());
		}
	}

	@Test
	public void testCompressFilePathsSourceUnreadable() throws IOException {
		File source = file();
		source.setReadable(false);
		try {
			CompressedFiles.compress(CompressedFiles.BZIP2, source.getAbsolutePath(), file().getAbsolutePath());
			fail();
		}
		catch (IllegalArgumentException e) {
			assertEquals("Source " + source.getAbsolutePath() + " is not readable", e.getMessage());
		}
	}

	@Test
	public void testCompressFilePathsDestinationNull() throws IOException {
		try {
			CompressedFiles.compress(CompressedFiles.BZIP2, file().getAbsolutePath(), null);
			fail();
		}
		catch (IllegalArgumentException e) {
			assertEquals("Destination file path must be non-null", e.getMessage());
		}
	}

	@Test
	public void testCompressFilePathsDestinationEmpty() throws IOException {
		try {
			CompressedFiles.compress(CompressedFiles.BZIP2, file().getAbsolutePath(), "");
			fail();
		}
		catch (IllegalArgumentException e) {
			assertEquals("Destination file path must be non-empty", e.getMessage());
		}
	}

	@Test
	public void testCompressFilePathsDestinationBlank() throws IOException {
		try {
			CompressedFiles.compress(CompressedFiles.BZIP2, file().getAbsolutePath(), " ");
			fail();
		}
		catch (IllegalArgumentException e) {
			assertEquals("Destination file path must be non-empty", e.getMessage());
		}
	}

	@Test
	public void testCompressFilePathsDestinationIsDirectory() throws IOException {
		File destination = directory();
		try {
			CompressedFiles.compress(CompressedFiles.BZIP2, file().getAbsolutePath(), destination.getAbsolutePath());
			fail();
		}
		catch (IllegalArgumentException e) {
			assertEquals("Destination " + destination.getAbsolutePath() + " must not be a directory", e.getMessage());
		}
	}

	@Test
	public void testCompressFilePathsDestinationUnwriteable() throws IOException {
		File destination = file();
		destination.setWritable(false);
		try {
			CompressedFiles.compress(CompressedFiles.BZIP2, file().getAbsolutePath(), destination.getAbsolutePath());
			fail();
		}
		catch (IllegalArgumentException e) {
			assertEquals("Destination " + destination.getAbsolutePath() + " is not writable", e.getMessage());
		}
	}

	@Test
	public void testDecompressFilePathsFileTypeNull() throws IOException {
		try {
			CompressedFiles.decompress(null, file().getAbsolutePath(), file().getAbsolutePath());
			fail();
		}
		catch (IllegalArgumentException e) {
			assertEquals("File type must be non-null", e.getMessage());
		}
	}

	@Test
	public void testDecompressFilePathsFileTypeEmpty() throws IOException {
		try {
			CompressedFiles.decompress("", file().getAbsolutePath(), file().getAbsolutePath());
			fail();
		}
		catch (IllegalArgumentException e) {
			assertEquals("File type must be non-empty", e.getMessage());
		}
	}

	@Test
	public void testDecompressFilePathsFileTypeBlank() throws IOException {
		try {
			CompressedFiles.decompress(" ", file().getAbsolutePath(), file().getAbsolutePath());
			fail();
		}
		catch (IllegalArgumentException e) {
			assertEquals("File type must be non-empty", e.getMessage());
		}
	}

	@Test
	public void testDecompressFilePathsFileTypeInvalid() throws IOException {
		try {
			CompressedFiles.decompress(Archives.AR, file().getAbsolutePath(), file().getAbsolutePath());
			fail();
		}
		catch (IllegalArgumentException e) {
			assertEquals("Archive type " + Archives.AR + " cannot be decompressed; use Archives.extract()", e.getMessage());
		}
	}

	@Test
	public void testDecompressFilePathsFileTypeUnrecognized() throws IOException {
		try {
			CompressedFiles.decompress("blah", file().getAbsolutePath(), file().getAbsolutePath());
			fail();
		}
		catch (IllegalArgumentException e) {
			assertEquals("File type blah is not recognized", e.getMessage());
		}
	}

	@Test
	public void testDecompressFilePathsSourceNull() throws IOException {
		try {
			CompressedFiles.decompress(CompressedFiles.BZIP2, null, file().getAbsolutePath());
			fail();
		}
		catch (IllegalArgumentException e) {
			assertEquals("Source file path must be non-null", e.getMessage());
		}
	}

	@Test
	public void testDecompressFilePathsSourceEmpty() throws IOException {
		try {
			CompressedFiles.decompress(CompressedFiles.BZIP2, "", file().getAbsolutePath());
			fail();
		}
		catch (IllegalArgumentException e) {
			assertEquals("Source file path must be non-empty", e.getMessage());
		}
	}

	@Test
	public void testDecompressFilePathsSourceBlank() throws IOException {
		try {
			CompressedFiles.decompress(CompressedFiles.BZIP2, " ", file().getAbsolutePath());
			fail();
		}
		catch (IllegalArgumentException e) {
			assertEquals("Source file path must be non-empty", e.getMessage());
		}
	}

	@Test
	public void testDecompressFilePathsSourceDoesntExist() throws IOException {
		File source = new File("i-dont-exist.txt");
		try {
			CompressedFiles.decompress(CompressedFiles.BZIP2, source.getAbsolutePath(), file().getAbsolutePath());
			fail();
		}
		catch (IllegalArgumentException e) {
			assertEquals("Source " + source.getAbsolutePath() + " does not exist", e.getMessage());
		}
	}

	@Test
	public void testDecompressFilePathsSourceIsDirectory() throws IOException {
		File source = directory();
		try {
			CompressedFiles.decompress(CompressedFiles.BZIP2, source.getAbsolutePath(), file().getAbsolutePath());
			fail();
		}
		catch (IllegalArgumentException e) {
			assertEquals("Source " + source.getAbsolutePath() + " must not be a directory; to extract a directory use Archives.extract()", e.getMessage());
		}
	}

	@Test
	public void testDecompressFilePathsSourceUnreadable() throws IOException {
		File source = file();
		source.setReadable(false);
		try {
			CompressedFiles.decompress(CompressedFiles.BZIP2, source.getAbsolutePath(), file().getAbsolutePath());
			fail();
		}
		catch (IllegalArgumentException e) {
			assertEquals("Source " + source.getAbsolutePath() + " is not readable", e.getMessage());
		}
	}

	@Test
	public void testDecompressFilePathsDestinationNull() throws IOException {
		try {
			CompressedFiles.decompress(CompressedFiles.BZIP2, file().getAbsolutePath(), null);
			fail();
		}
		catch (IllegalArgumentException e) {
			assertEquals("Destination file path must be non-null", e.getMessage());
		}
	}

	@Test
	public void testDecompressFilePathsDestinationEmpty() throws IOException {
		try {
			CompressedFiles.decompress(CompressedFiles.BZIP2, file().getAbsolutePath(), "");
			fail();
		}
		catch (IllegalArgumentException e) {
			assertEquals("Destination file path must be non-empty", e.getMessage());
		}
	}

	@Test
	public void testDecompressFilePathsDestinationBlank() throws IOException {
		try {
			CompressedFiles.decompress(CompressedFiles.BZIP2, file().getAbsolutePath(), " ");
			fail();
		}
		catch (IllegalArgumentException e) {
			assertEquals("Destination file path must be non-empty", e.getMessage());
		}
	}

	@Test
	public void testDecompressFilePathsDestinationIsDirectory() throws IOException {
		File destination = directory();
		try {
			CompressedFiles.decompress(CompressedFiles.BZIP2, file().getAbsolutePath(), destination.getAbsolutePath());
			fail();
		}
		catch (IllegalArgumentException e) {
			assertEquals("Destination " + destination.getAbsolutePath() + " must not be a directory", e.getMessage());
		}
	}

	@Test
	public void testDecompressFilePathsDestinationUnwriteable() throws IOException {
		File destination = file();
		destination.setWritable(false);
		try {
			CompressedFiles.decompress(CompressedFiles.BZIP2, file().getAbsolutePath(), destination.getAbsolutePath());
			fail();
		}
		catch (IllegalArgumentException e) {
			assertEquals("Destination " + destination.getAbsolutePath() + " is not writable", e.getMessage());
		}
	}

	@Test
	public void testCompressFilesFileTypeNull() throws IOException {
		try {
			CompressedFiles.compress(null, file(), file());
			fail();
		}
		catch (IllegalArgumentException e) {
			assertEquals("File type must be non-null", e.getMessage());
		}
	}

	@Test
	public void testCompressFilesFileTypeEmpty() throws IOException {
		try {
			CompressedFiles.compress("", file(), file());
			fail();
		}
		catch (IllegalArgumentException e) {
			assertEquals("File type must be non-empty", e.getMessage());
		}
	}

	@Test
	public void testCompressFilesFileTypeBlank() throws IOException {
		try {
			CompressedFiles.compress(" ", file(), file());
			fail();
		}
		catch (IllegalArgumentException e) {
			assertEquals("File type must be non-empty", e.getMessage());
		}
	}

	@Test
	public void testCompressFilesFileTypeInvalid() throws IOException {
		try {
			CompressedFiles.compress(Archives.AR, file(), file());
			fail();
		}
		catch (IllegalArgumentException e) {
			assertEquals("Archive type " + Archives.AR + " cannot be compressed; use Archives.archive()", e.getMessage());
		}
	}

	@Test
	public void testCompressFilesFileTypeUnrecognized() throws IOException {
		try {
			CompressedFiles.compress("blah", file(), file());
			fail();
		}
		catch (IllegalArgumentException e) {
			assertEquals("File type blah is not recognized", e.getMessage());
		}
	}

	@Test
	public void testCompressFilesSourceNull() throws IOException {
		try {
			CompressedFiles.compress(CompressedFiles.BZIP2, null, file());
			fail();
		}
		catch (IllegalArgumentException e) {
			assertEquals("Source must be non-null", e.getMessage());
		}
	}

	@Test
	public void testCompressFilesSourceDoesntExist() throws IOException {
		File source = new File("i-dont-exist.txt");
		try {
			CompressedFiles.compress(CompressedFiles.BZIP2, source, file());
			fail();
		}
		catch (IllegalArgumentException e) {
			assertEquals("Source " + source.getAbsolutePath() + " does not exist", e.getMessage());
		}
	}

	@Test
	public void testCompressFilesSourceIsDirectory() throws IOException {
		File source = directory();
		try {
			CompressedFiles.compress(CompressedFiles.BZIP2, source, file());
			fail();
		}
		catch (IllegalArgumentException e) {
			assertEquals("Source " + source.getAbsolutePath() + " must not be a directory; to archive a directory use Archives.archive()", e.getMessage());
		}
	}

	@Test
	public void testCompressFilesSourceUnreadable() throws IOException {
		File source = file();
		source.setReadable(false);
		try {
			CompressedFiles.compress(CompressedFiles.BZIP2, source, file());
			fail();
		}
		catch (IllegalArgumentException e) {
			assertEquals("Source " + source.getAbsolutePath() + " is not readable", e.getMessage());
		}
	}

	@Test
	public void testCompressFilesDestinationNull() throws IOException {
		try {
			CompressedFiles.compress(CompressedFiles.BZIP2, file(), null);
			fail();
		}
		catch (IllegalArgumentException e) {
			assertEquals("Destination must be non-null", e.getMessage());
		}
	}

	@Test
	public void testCompressFilesDestinationIsDirectory() throws IOException {
		File destination = directory();
		try {
			CompressedFiles.compress(CompressedFiles.BZIP2, file(), destination);
			fail();
		}
		catch (IllegalArgumentException e) {
			assertEquals("Destination " + destination.getAbsolutePath() + " must not be a directory", e.getMessage());
		}
	}

	@Test
	public void testCompressFilesDestinationUnwriteable() throws IOException {
		File destination = file();
		destination.setWritable(false);
		try {
			CompressedFiles.compress(CompressedFiles.BZIP2, file(), destination);
			fail();
		}
		catch (IllegalArgumentException e) {
			assertEquals("Destination " + destination.getAbsolutePath() + " is not writable", e.getMessage());
		}
	}

	@Test
	public void testDecompressFilesFileTypeNull() throws IOException {
		try {
			CompressedFiles.decompress(null, file(), file());
			fail();
		}
		catch (IllegalArgumentException e) {
			assertEquals("File type must be non-null", e.getMessage());
		}
	}

	@Test
	public void testDecompressFilesFileTypeEmpty() throws IOException {
		try {
			CompressedFiles.decompress("", file(), file());
			fail();
		}
		catch (IllegalArgumentException e) {
			assertEquals("File type must be non-empty", e.getMessage());
		}
	}

	@Test
	public void testDecompressFilesFileTypeBlank() throws IOException {
		try {
			CompressedFiles.decompress(" ", file(), file());
			fail();
		}
		catch (IllegalArgumentException e) {
			assertEquals("File type must be non-empty", e.getMessage());
		}
	}

	@Test
	public void testDecompressFilesFileTypeInvalid() throws IOException {
		try {
			CompressedFiles.decompress(Archives.AR, file(), file());
			fail();
		}
		catch (IllegalArgumentException e) {
			assertEquals("Archive type " + Archives.AR + " cannot be decompressed; use Archives.extract()", e.getMessage());
		}
	}

	@Test
	public void testDecompressFilesFileTypeUnrecognized() throws IOException {
		try {
			CompressedFiles.decompress("blah", file(), file());
			fail();
		}
		catch (IllegalArgumentException e) {
			assertEquals("File type blah is not recognized", e.getMessage());
		}
	}

	@Test
	public void testDecompressFilesSourceNull() throws IOException {
		try {
			CompressedFiles.decompress(CompressedFiles.BZIP2, null, file());
			fail();
		}
		catch (IllegalArgumentException e) {
			assertEquals("Source must be non-null", e.getMessage());
		}
	}

	@Test
	public void testDecompressFilesSourceDoesntExist() throws IOException {
		File source = new File("i-dont-exist.txt");
		try {
			CompressedFiles.decompress(CompressedFiles.BZIP2, source, file());
			fail();
		}
		catch (IllegalArgumentException e) {
			assertEquals("Source " + source.getAbsolutePath() + " does not exist", e.getMessage());
		}
	}

	@Test
	public void testDecompressFilesSourceIsDirectory() throws IOException {
		File source = directory();
		try {
			CompressedFiles.decompress(CompressedFiles.BZIP2, source, file());
			fail();
		}
		catch (IllegalArgumentException e) {
			assertEquals("Source " + source.getAbsolutePath() + " must not be a directory; to extract a directory use Archives.extract()", e.getMessage());
		}
	}

	@Test
	public void testDecompressFilesSourceUnreadable() throws IOException {
		File source = file();
		source.setReadable(false);
		try {
			CompressedFiles.decompress(CompressedFiles.BZIP2, source, file());
			fail();
		}
		catch (IllegalArgumentException e) {
			assertEquals("Source " + source.getAbsolutePath() + " is not readable", e.getMessage());
		}
	}

	@Test
	public void testDecompressFilesDestinationNull() throws IOException {
		try {
			CompressedFiles.decompress(CompressedFiles.BZIP2, file(), null);
			fail();
		}
		catch (IllegalArgumentException e) {
			assertEquals("Destination must be non-null", e.getMessage());
		}
	}

	@Test
	public void testDecompressFilesDestinationIsDirectory() throws IOException {
		File destination = directory();
		try {
			CompressedFiles.decompress(CompressedFiles.BZIP2, file(), destination);
			fail();
		}
		catch (IllegalArgumentException e) {
			assertEquals("Destination " + destination.getAbsolutePath() + " must not be a directory", e.getMessage());
		}
	}

	@Test
	public void testDecompressFilesDestinationUnwriteable() throws IOException {
		File destination = file();
		destination.setWritable(false);
		try {
			CompressedFiles.decompress(CompressedFiles.BZIP2, file(), destination);
			fail();
		}
		catch (IllegalArgumentException e) {
			assertEquals("Destination " + destination.getAbsolutePath() + " is not writable", e.getMessage());
		}
	}
}
