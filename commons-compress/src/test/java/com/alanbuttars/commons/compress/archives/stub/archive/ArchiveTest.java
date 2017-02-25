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
package com.alanbuttars.commons.compress.archives.stub.archive;

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
 * Test class for {@link Archive}.
 * 
 * @author Alan Buttars
 *
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({ Archives.class })
public class ArchiveTest {

	private File file;
	private File directory;

	@Before
	public void setup() throws IOException {
		this.file = File.createTempFile(getClass().getName(), ".tmp");
		this.directory = Files.createTempDirectory(getClass().getName()).toFile();
	}

	@Test
	public void testAr() throws Exception {
		PowerMockito.mockStatic(Archives.class);
		Archive.directory(directory).withAr().withLongFileMode(2).to(file);
		PowerMockito.verifyStatic();
	}

	@Test
	public void testCpio() throws Exception {
		PowerMockito.mockStatic(Archives.class);
		Archive.directory(directory).withCpio().withBlockSize((short) 2).withBlockSize(4).withEncoding("blah").to(file);
		PowerMockito.verifyStatic();
	}

	@Test
	public void testJar() throws Exception {
		PowerMockito.mockStatic(Archives.class);
		Archive.directory(directory).withJar()//
				.withComment("comment")//
				.withEncoding("blah")//
				.withLevel(2)//
				.withMethod(4)//
				.withUnicodeExtraFieldPolicy(UnicodeExtraFieldPolicy.ALWAYS)//
				.withZip64Mode(Zip64Mode.Always)//
				.fallbackToUTF8(false)//
				.useLanguageEncoding(true)//
				.to(file);
		PowerMockito.verifyStatic();
	}

	@Test
	public void testSevenZ() throws Exception {
		PowerMockito.mockStatic(Archives.class);
		Archive.directory(directory).withSevenZ().withContentMethods(null).to(file);
		PowerMockito.verifyStatic();
	}

	@Test
	public void testTar() throws Exception {
		PowerMockito.mockStatic(Archives.class);
		Archive.directory(directory).withTar()//
				.withBigNumberMode(2)//
				.withBlockSize(4)//
				.withEncoding("blah")//
				.withLongFileMode(6)//
				.withRecordSize(8)//
				.addPaxHeadersForNonAsciiNames(true)//
				.to(file);
		PowerMockito.verifyStatic();
	}

	@Test
	public void testZip() throws Exception {
		PowerMockito.mockStatic(Archives.class);
		Archive.directory(directory).withZip()//
				.withComment("comment")//
				.withEncoding("blah")//
				.withLevel(2)//
				.withMethod(4)//
				.withUnicodeExtraFieldPolicy(UnicodeExtraFieldPolicy.ALWAYS)//
				.withZip64Mode(Zip64Mode.Always)//
				.fallbackToUTF8(false)//
				.useLanguageEncoding(true)//
				.to(file);
		PowerMockito.verifyStatic();
	}

}
