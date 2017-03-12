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

import static com.alanbuttars.commons.compress.archives.util.Archives.CPIO;
import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.cpio.CpioArchiveEntry;
import org.apache.commons.compress.archivers.cpio.CpioConstants;
import org.apache.commons.compress.utils.CharsetNames;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * Test class for {@link CompressDirectoryWithStubCpioImpl}.
 * 
 * @author Alan Buttars
 *
 */
public class CompressDirectoryWithStubCpioImplTest {

	private File source;
	private File destination;
	private CompressDirectoryWithStubCpioImpl stub;

	@Before
	public void setup() throws IOException {
		source = Files.createTempDirectory(getClass().getName()).toFile();
		destination = File.createTempFile(getClass().getName(), ".tmp");
		stub = spy(new CompressDirectoryWithStubCpioImpl(source));
	}
	
	@After
	public void teardown() {
		source.deleteOnExit();
		destination.deleteOnExit();
	}

	@Test
	public void testConstructor() {
		assertEquals(source, stub.source);
		assertEquals(CPIO, stub.fileType);
	}

	@Test
	public void testCompressionFunction() throws IOException {
		stub.to(destination);
		verify(stub, times(1)).createArchiveOutputStream(eq(destination), eq(CpioConstants.BLOCK_SIZE), eq(CharsetNames.US_ASCII), eq(CpioConstants.FORMAT_NEW));
	}

	@Test
	public void testCustomCompressionFunction() throws IOException {
		stub.andBlockSize(1).andEncoding("UTF16").andFormat((short) 2).to(destination);
		verify(stub, times(1)).createArchiveOutputStream(eq(destination), eq(1), eq("UTF16"), eq((short) 2));
	}

	@Test
	public void testEntryFunction() {
		ArchiveEntry entry = stub.entryFunction().apply("name", 1L);
		assertEquals(CpioArchiveEntry.class, entry.getClass());
		CpioArchiveEntry cpioEntry = (CpioArchiveEntry) entry;
		assertEquals("name", cpioEntry.getName());
		assertEquals(1, cpioEntry.getSize());
		assertEquals(CpioConstants.FORMAT_NEW, cpioEntry.getFormat());
	}

	@Test
	public void testCustomEntryFunction() {
		ArchiveEntry entry = stub.andFormat((short) 2).entryFunction().apply("name", 1L);
		assertEquals(CpioArchiveEntry.class, entry.getClass());
		CpioArchiveEntry cpioEntry = (CpioArchiveEntry) entry;
		assertEquals("name", cpioEntry.getName());
		assertEquals(1, cpioEntry.getSize());
		assertEquals(2, cpioEntry.getFormat());
	}
}
