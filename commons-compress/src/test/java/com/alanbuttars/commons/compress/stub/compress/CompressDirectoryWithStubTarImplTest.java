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

import static com.alanbuttars.commons.compress.archives.util.Archives.TAR;
import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.eq;
import static org.mockito.Matchers.isNull;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveOutputStream;
import org.apache.commons.compress.archivers.tar.TarConstants;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * Test class for {@link CompressDirectoryWithStubTarImpl}.
 * 
 * @author Alan Buttars
 *
 */
public class CompressDirectoryWithStubTarImplTest {

	private File source;
	private File destination;
	private CompressDirectoryWithStubTarImpl stub;

	@Before
	public void setup() throws IOException {
		source = Files.createTempDirectory(getClass().getName()).toFile();
		destination = File.createTempFile(getClass().getName(), ".tmp");
		stub = spy(new CompressDirectoryWithStubTarImpl(source));
	}
	
	@After
	public void teardown() {
		source.deleteOnExit();
		destination.deleteOnExit();
	}

	@Test
	public void testConstructor() {
		assertEquals(source, stub.source);
		assertEquals(TAR, stub.fileType);
	}

	@Test
	public void testCompressionFunction() throws IOException {
		stub.to(destination);
		verify(stub, times(1)).createArchiveOutputStream(eq(destination), //
				eq(false), //
				eq(TarArchiveOutputStream.BIGNUMBER_ERROR), //
				eq(TarConstants.DEFAULT_BLKSIZE), //
				isNull(String.class), //
				eq(TarArchiveOutputStream.LONGFILE_ERROR), //
				eq(TarConstants.DEFAULT_RCDSIZE));
	}

	@Test
	public void testCustomCompressionFunction() throws IOException {
		stub.andAddPaxHeadersForNonAsciiNames(true)//
				.andBigNumberMode(1)//
				.andBlockSize(4)//
				.andEncoding("UTF16")//
				.andLongFileMode(3)//
				.andRecordSize(4)//
				.to(destination);
		verify(stub, times(1)).createArchiveOutputStream(eq(destination), //
				eq(true), //
				eq(1), //
				eq(4), //
				eq("UTF16"), //
				eq(3), //
				eq(4));
	}

	@Test
	public void testEntryFunction() {
		ArchiveEntry entry = stub.entryFunction().apply("/name", 1L);
		assertEquals(TarArchiveEntry.class, entry.getClass());
		TarArchiveEntry tarEntry = (TarArchiveEntry) entry;
		assertEquals("name", tarEntry.getName());
		assertEquals(1, tarEntry.getSize());
	}

	@Test
	public void testCustomEntryFunction() {
		ArchiveEntry entry = stub.andPreserveLeadingSlashes(true).entryFunction().apply("/name", 1L);
		assertEquals(TarArchiveEntry.class, entry.getClass());
		TarArchiveEntry tarEntry = (TarArchiveEntry) entry;
		assertEquals("/name", tarEntry.getName());
		assertEquals(1, tarEntry.getSize());
	}
}
