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
package com.alanbuttars.commons.compress.stub.compress;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.HashMap;

import org.apache.commons.compress.compressors.deflate.DeflateParameters;
import org.apache.commons.compress.compressors.gzip.GzipParameters;
import org.apache.commons.compress.compressors.pack200.Pack200Strategy;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import com.alanbuttars.commons.compress.files.util.CompressedFiles;

/**
 * Test class for {@link Compress#file(File)}.
 * 
 * @author Alan Buttars
 *
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({ CompressedFiles.class })
public class CompressFileTest {

	private File source;
	private File destination;

	@Before
	public void setup() throws IOException {
		this.source = File.createTempFile(getClass().getName(), ".tmp");
		this.destination = File.createTempFile(getClass().getName(), ".tmp");
	}
	
	@After
	public void teardown() {
		source.deleteOnExit();
		destination.deleteOnExit();
	}

	@Test
	public void testFileIsNull() {
		try {
			Compress.file(null);
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
			Compress.file(file);
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
			Compress.file(file);
			fail();
		}
		catch (IllegalArgumentException e) {
			assertEquals("Source " + file.getAbsolutePath() + " must not be a directory; to compress a directory use Compress.directory()", e.getMessage());
		}
	}

	@Test
	public void testFileIsNotReadable() {
		source.setReadable(false);
		try {
			Compress.file(source);
			fail();
		}
		catch (IllegalArgumentException e) {
			assertEquals("Source " + source.getAbsolutePath() + " is not readable", e.getMessage());
		}
	}

	@Test
	public void testFile() {
		CompressFileStub stub = Compress.file(source);
		assertEquals(source, stub.source);
	}

	@Test
	public void testBzip2() throws IOException {
		PowerMockito.mockStatic(CompressedFiles.class);
		Compress.file(source).withBzip2().andBlockSize(2).to(destination);
		PowerMockito.verifyStatic();
	}

	@Test
	public void testDeflate() throws IOException {
		PowerMockito.mockStatic(CompressedFiles.class);
		Compress.file(source).withDeflate().andParameters(new DeflateParameters()).to(destination);
		PowerMockito.verifyStatic();
	}

	@Test
	public void testGzip() throws IOException {
		PowerMockito.mockStatic(CompressedFiles.class);
		Compress.file(source).withGzip().andParameters(new GzipParameters()).to(destination);
		PowerMockito.verifyStatic();
	}

	@Test
	public void testLzma() throws IOException {
		PowerMockito.mockStatic(CompressedFiles.class);
		Compress.file(source).withLzma().to(destination);
		PowerMockito.verifyStatic();
	}

	@Test
	public void testPack200() throws IOException {
		PowerMockito.mockStatic(CompressedFiles.class);
		Compress.file(source).withPack200().andMode(Pack200Strategy.IN_MEMORY).andProperties(new HashMap<String, String>()).to(destination);
		PowerMockito.verifyStatic();
	}

	@Test
	public void testXz() throws IOException {
		PowerMockito.mockStatic(CompressedFiles.class);
		Compress.file(source).withXz().andPreset(2).to(destination);
		PowerMockito.verifyStatic();
	}

}
