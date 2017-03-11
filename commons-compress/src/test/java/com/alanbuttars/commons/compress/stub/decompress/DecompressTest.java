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
package com.alanbuttars.commons.compress.stub.decompress;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.HashMap;

import org.apache.commons.compress.compressors.deflate.DeflateParameters;
import org.apache.commons.compress.compressors.pack200.Pack200Strategy;
import org.apache.commons.compress.compressors.snappy.FramedSnappyDialect;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import com.alanbuttars.commons.compress.files.util.CompressedFiles;

/**
 * Test class for {@link Decompress}.
 * 
 * @author Alan Buttars
 *
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({ CompressedFiles.class })
public class DecompressTest {

	private File source;
	private File destination;

	@Before
	public void setup() throws IOException {
		this.source = File.createTempFile(getClass().getName(), ".tmp");
		this.destination = File.createTempFile(getClass().getName(), ".tmp");
	}

	@Test
	public void testFileIsFile() {
		try {
			Decompress.file(null);
			fail();
		}
		catch (IllegalArgumentException e) {
			assertEquals("Source must be non-null", e.getMessage());
		}
	}

	@Test
	public void testFileDoesNotExist() {
		File file = new File("i-dont-exist");
		try {
			Decompress.file(file);
			fail();
		}
		catch (IllegalArgumentException e) {
			assertEquals("Source " + file.getAbsolutePath() + " does not exist", e.getMessage());
		}
	}

	@Test
	public void testFileIsDirectory() throws IOException {
		File file = Files.createTempDirectory(getClass().getName()).toFile();
		try {
			Decompress.file(file);
			fail();
		}
		catch (IllegalArgumentException e) {
			assertEquals("Source " + file.getAbsolutePath() + " must not be a directory; to decompress a directory use Decompress.directory()", e.getMessage());
		}
	}

	@Test
	public void testFileIsNotReadable() {
		source.setReadable(false);
		try {
			Decompress.file(source);
			fail();
		}
		catch (IllegalArgumentException e) {
			assertEquals("Source " + source.getAbsolutePath() + " is not readable", e.getMessage());
		}
	}

	@Test
	public void testFile() {
		DecompressFileStub stub = Decompress.file(source);
		assertEquals(source, stub.source);
	}

	@Test
	public void testBzip() throws IOException {
		PowerMockito.mockStatic(CompressedFiles.class);
		Decompress.file(source).withBzip().andDecompressConcatenated(true).to(destination);
		PowerMockito.verifyStatic();
	}

	@Test
	public void testDeflate() throws IOException {
		PowerMockito.mockStatic(CompressedFiles.class);
		Decompress.file(source).withDeflate().andParameters(new DeflateParameters()).to(destination);
		PowerMockito.verifyStatic();
	}

	@Test
	public void testGzip() throws IOException {
		PowerMockito.mockStatic(CompressedFiles.class);
		Decompress.file(source).withGzip().andDecompressConcatenated(true).to(destination);
		PowerMockito.verifyStatic();
	}

	@Test
	public void testLzma() throws IOException {
		PowerMockito.mockStatic(CompressedFiles.class);
		Decompress.file(source).withLzma().to(destination);
		PowerMockito.verifyStatic();
	}

	@Test
	public void testPack200() throws IOException {
		PowerMockito.mockStatic(CompressedFiles.class);
		Decompress.file(source).withPack200().andMode(Pack200Strategy.IN_MEMORY).andProperties(new HashMap<String, String>()).to(destination);
		PowerMockito.verifyStatic();
	}

	@Test
	public void testSnappy() throws IOException {
		PowerMockito.mockStatic(CompressedFiles.class);
		Decompress.file(source).withSnappy().andBlockSize(2).andFramedDialect(FramedSnappyDialect.IWORK_ARCHIVE).to(destination);
		PowerMockito.verifyStatic();
	}

	@Test
	public void testXz() throws IOException {
		PowerMockito.mockStatic(CompressedFiles.class);
		Decompress.file(source).withXz().andDecompressConcatenated(true).to(destination);
		PowerMockito.verifyStatic();
	}

	@Test
	public void testZ() throws IOException {
		PowerMockito.mockStatic(CompressedFiles.class);
		Decompress.file(source).withZ().to(destination);
		PowerMockito.verifyStatic();
	}
}
