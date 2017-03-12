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

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import com.alanbuttars.commons.compress.archives.util.Archives;

/**
 * Test class for {@link Decompress#archive(File)}.
 * 
 * @author Alan Buttars
 *
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({ Archives.class })
public class DecompressArchiveTest {

	private File source;
	private File destination;

	@Before
	public void setup() throws IOException {
		this.source = File.createTempFile(getClass().getName(), ".tmp");
		this.destination = Files.createTempDirectory(getClass().getName()).toFile();
	}
	
	@After
	public void teardown() {
		source.deleteOnExit();
		destination.deleteOnExit();
	}

	@Test
	public void testFileIsFile() {
		try {
			Decompress.archive(null);
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
			Decompress.archive(file);
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
			Decompress.archive(file);
			fail();
		}
		catch (IllegalArgumentException e) {
			assertEquals("Source " + file.getAbsolutePath() + " must not be a directory", e.getMessage());
		}
	}

	@Test
	public void testFileIsNotReadable() {
		source.setReadable(false);
		try {
			Decompress.archive(source);
			fail();
		}
		catch (IllegalArgumentException e) {
			assertEquals("Source " + source.getAbsolutePath() + " is not readable", e.getMessage());
		}
	}

	@Test
	public void testFile() {
		DecompressArchiveStub stub = Decompress.archive(source);
		assertEquals(source, stub.source);
	}

	@Test
	public void test7z() throws IOException {
		PowerMockito.mockStatic(Archives.class);
		Decompress.archive(source).with7z().andEncryptWithPassword("password".getBytes()).to(destination);
		PowerMockito.verifyStatic();
	}

	@Test
	public void testAr() throws IOException {
		PowerMockito.mockStatic(Archives.class);
		Decompress.archive(source).withAr().to(destination);
		PowerMockito.verifyStatic();
	}

	@Test
	public void testArj() throws IOException {
		PowerMockito.mockStatic(Archives.class);
		Decompress.archive(source).withArj().andEncoding("UTF16").to(destination);
		PowerMockito.verifyStatic();
	}

	@Test
	public void testCpio() throws IOException {
		PowerMockito.mockStatic(Archives.class);
		Decompress.archive(source).withCpio().andBlockSize(1).andEncoding("UTF16").to(destination);
		PowerMockito.verifyStatic();
	}

	@Test
	public void testDump() throws IOException {
		PowerMockito.mockStatic(Archives.class);
		Decompress.archive(source).withDump().andEncoding("UTF16").to(destination);
		PowerMockito.verifyStatic();
	}

	@Test
	public void testJar() throws IOException {
		PowerMockito.mockStatic(Archives.class);
		Decompress.archive(source).withJar().andEncoding("UTF16").to(destination);
		PowerMockito.verifyStatic();
	}

	@Test
	public void testTar() throws IOException {
		PowerMockito.mockStatic(Archives.class);
		Decompress.archive(source).withTar().andBlockSize(1).andEncoding("UTF16").andRecordSize(2).to(destination);
		PowerMockito.verifyStatic();
	}

	@Test
	public void testZip() throws IOException {
		PowerMockito.mockStatic(Archives.class);
		Decompress.archive(source).withZip().andAllowStoredEntriesWithDataDescriptor(false).andEncoding("UTF16").andUseUnicodeExtraFields(false).to(destination);
		PowerMockito.verifyStatic();
	}
}
