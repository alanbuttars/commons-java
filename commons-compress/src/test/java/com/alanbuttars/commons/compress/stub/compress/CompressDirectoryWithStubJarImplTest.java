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

import static com.alanbuttars.commons.compress.archives.util.Archives.JAR;
import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.zip.ZipEntry;

import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.jar.JarArchiveEntry;
import org.apache.commons.compress.archivers.zip.Zip64Mode;
import org.apache.commons.compress.archivers.zip.ZipArchiveOutputStream;
import org.apache.commons.compress.archivers.zip.ZipArchiveOutputStream.UnicodeExtraFieldPolicy;
import org.junit.Before;
import org.junit.Test;

/**
 * Test class for {@link CompressDirectoryWithStubJarImpl}.
 * 
 * @author Alan Buttars
 *
 */
public class CompressDirectoryWithStubJarImplTest {

	private File source;
	private File destination;
	private CompressDirectoryWithStubJarImpl stub;

	@Before
	public void setup() throws IOException {
		source = Files.createTempDirectory(getClass().getName()).toFile();
		destination = File.createTempFile(getClass().getName(), ".tmp");
		stub = spy(new CompressDirectoryWithStubJarImpl(source));
	}

	@Test
	public void testConstructor() {
		assertEquals(source, stub.source);
		assertEquals(JAR, stub.fileType);
	}

	@Test
	public void testCompressionFunction() throws IOException {
		stub.to(destination);
		verify(stub, times(1)).createArchiveOutputStream(//
				eq(destination), //
				eq(""), //
				eq("UTF8"), //
				eq(false), //
				eq(ZipArchiveOutputStream.DEFAULT_COMPRESSION), //
				eq(ZipEntry.DEFLATED), //
				eq(UnicodeExtraFieldPolicy.NEVER), //
				eq(true), //
				eq(Zip64Mode.AsNeeded));
	}

	@Test
	public void testCustomCompressionFunction() throws IOException {
		stub.andComment("comment")//
				.andEncoding("UTF16")//
				.andFallbackToUTF8(true)//
				.andLevel(1)//
				.andMethod(2)//
				.andUnicodeExtraFieldPolicy(UnicodeExtraFieldPolicy.ALWAYS)//
				.andUseLanguageEncoding(false) //
				.andZip64Mode(Zip64Mode.Always)//
				.to(destination);
		verify(stub, times(1)).createArchiveOutputStream(//
				eq(destination), //
				eq("comment"), //
				eq("UTF16"), //
				eq(true), //
				eq(1), //
				eq(2), //
				eq(UnicodeExtraFieldPolicy.ALWAYS), //
				eq(false), //
				eq(Zip64Mode.Always));
	}

	@Test
	public void testEntryFunction() {
		ArchiveEntry entry = stub.entryFunction().apply("name", 1L);
		assertEquals(JarArchiveEntry.class, entry.getClass());
		JarArchiveEntry jarEntry = (JarArchiveEntry) entry;
		assertEquals("name", jarEntry.getName());
		assertEquals(-1, jarEntry.getSize());
	}
}
