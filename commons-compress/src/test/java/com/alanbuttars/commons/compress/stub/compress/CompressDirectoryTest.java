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

import org.apache.commons.compress.archivers.zip.Zip64Mode;
import org.apache.commons.compress.archivers.zip.ZipArchiveOutputStream.UnicodeExtraFieldPolicy;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import com.alanbuttars.commons.compress.archives.util.Archives;

/**
 * Test class for {@link Compress#directory(File)}.
 * 
 * @author Alan Buttars
 *
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({ Archives.class })
public class CompressDirectoryTest {

	private File source;
	private File destination;

	@Before
	public void setup() throws IOException {
		this.source = Files.createTempDirectory(getClass().getName()).toFile();
		this.destination = File.createTempFile(getClass().getName(), ".tmp");
	}

	@Test
	public void testDirectoryIsNull() {
		try {
			Compress.directory(null);
			fail();
		}
		catch (IllegalArgumentException e) {
			assertEquals("Source must be non-null", e.getMessage());
		}
	}

	@Test
	public void testDirectoryDoesNotExist() {
		File file = new File("i-dont-exist");
		try {
			Compress.directory(file);
			fail();
		}
		catch (IllegalArgumentException e) {
			assertEquals("Source " + file.getAbsolutePath() + " does not exist", e.getMessage());
		}
	}

	@Test
	public void testDirectoryIsFile() throws IOException {
		File file = File.createTempFile(getClass().getName(), ".tmp");
		try {
			Compress.directory(file);
			fail();
		}
		catch (IllegalArgumentException e) {
			assertEquals("Source " + file.getAbsolutePath() + " must be a directory; to compress a file use Compress.file()", e.getMessage());
		}
	}

	@Test
	public void testDirectoryIsNotReadable() {
		source.setReadable(false);
		try {
			Compress.directory(source);
			fail();
		}
		catch (IllegalArgumentException e) {
			assertEquals("Source " + source.getAbsolutePath() + " is not readable", e.getMessage());
		}
	}

	@Test
	public void testDirectory() {
		CompressDirectoryStub stub = Compress.directory(source);
		assertEquals(source, stub.source);
	}

	@Test
	public void test7z() throws IOException {
		PowerMockito.mockStatic(Archives.class);
		Compress.directory(source).with7z().andContentMethods(null).to(destination);
		PowerMockito.verifyStatic();
	}

	@Test
	public void testAr() throws IOException {
		PowerMockito.mockStatic(Archives.class);
		Compress.directory(source).withAr()//
				.andGroupId(1)//
				.andLastModified(2)//
				.andLongFileMode(3)//
				.andMode(4)//
				.andUserId(5)//
				.to(destination);
		PowerMockito.verifyStatic();
	}

	@Test
	public void testCpio() throws IOException {
		PowerMockito.mockStatic(Archives.class);
		Compress.directory(source).withCpio()//
				.andBlockSize(1)//
				.andEncoding("UTF16")//
				.andFormat((short) 2)//
				.to(destination);
		PowerMockito.verifyStatic();
	}

	@Test
	public void testJar() throws IOException {
		PowerMockito.mockStatic(Archives.class);
		Compress.directory(source).withJar()//
				.andComment("comment")//
				.andEncoding("UTF16")//
				.andFallbackToUTF8(true)//
				.andLevel(1)//
				.andMethod(2)//
				.andUnicodeExtraFieldPolicy(UnicodeExtraFieldPolicy.ALWAYS)//
				.andUseLanguageEncoding(false)//
				.andZip64Mode(Zip64Mode.Always)//
				.to(destination);
		PowerMockito.verifyStatic();
	}

	@Test
	public void testTar() throws IOException {
		PowerMockito.mockStatic(Archives.class);
		Compress.directory(source)//
				.withTar()//
				.andAddPaxHeadersForNonAsciiNames(true)//
				.andBigNumberMode(1)//
				.andBlockSize(2)//
				.andEncoding("UTF16")//
				.andLongFileMode(3)//
				.andPreserveLeadingSlashes(true)//
				.andRecordSize(4)//
				.to(destination);
		PowerMockito.verifyStatic();
	}

	@Test
	public void testZip() throws IOException {
		PowerMockito.mockStatic(Archives.class);
		Compress.directory(source).withZip()//
				.andComment("comment")//
				.andEncoding("UTF16")//
				.andFallbackToUTF8(true)//
				.andLevel(1)//
				.andMethod(2)//
				.andUnicodeExtraFieldPolicy(UnicodeExtraFieldPolicy.ALWAYS)//
				.andUseLanguageEncoding(false)//
				.andZip64Mode(Zip64Mode.Always)//
				.to(destination);
		PowerMockito.verifyStatic();
	}

}
