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

import static com.alanbuttars.commons.compress.archives.util.Archives.AR;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.ar.ArArchiveEntry;
import org.apache.commons.compress.archivers.ar.ArArchiveOutputStream;
import org.junit.Before;
import org.junit.Test;

/**
 * Test class for {@link CompressDirectoryWithStub7zImpl}.
 * 
 * @author Alan Buttars
 *
 */
public class CompressDirectoryWithStubArImplTest {

	private File source;
	private File destination;
	private CompressDirectoryWithStubArImpl stub;

	@Before
	public void setup() throws IOException {
		source = Files.createTempDirectory(getClass().getName()).toFile();
		destination = File.createTempFile(getClass().getName(), ".tmp");
		stub = spy(new CompressDirectoryWithStubArImpl(source));
	}

	@Test
	public void testConstructor() {
		assertEquals(source, stub.source);
		assertEquals(AR, stub.fileType);
	}

	@Test
	public void testCompressionFunction() throws IOException {
		stub.to(destination);
		verify(stub, times(1)).createArchiveOutputStream(eq(destination), eq(ArArchiveOutputStream.LONGFILE_ERROR));
	}

	@Test
	public void testCustomCompressionFunction() throws IOException {
		stub.andLongFileMode(1).to(destination);
		verify(stub, times(1)).createArchiveOutputStream(eq(destination), eq(1));
	}

	@Test
	public void testEntryFunction() {
		ArchiveEntry entry = stub.entryFunction().apply("name", 1L);
		assertEquals(ArArchiveEntry.class, entry.getClass());
		ArArchiveEntry arEntry = (ArArchiveEntry) entry;
		assertEquals("name", arEntry.getName());
		assertEquals(1L, arEntry.getSize());
		assertEquals(0, arEntry.getGroupId());
		assertTrue(arEntry.getLastModified() < System.currentTimeMillis() + 5);
		assertEquals(33188, arEntry.getMode());
		assertEquals(0, arEntry.getUserId());
	}

	@Test
	public void testCustomEntryFunction() {
		ArchiveEntry entry = stub//
				.andGroupId(2)//
				.andLastModified(System.currentTimeMillis())//
				.andLongFileMode(3)//
				.andMode(4)//
				.andUserId(5)//
				.entryFunction()//
				.apply("name", 1L);
		assertEquals(ArArchiveEntry.class, entry.getClass());
		ArArchiveEntry arEntry = (ArArchiveEntry) entry;
		assertEquals("name", arEntry.getName());
		assertEquals(1L, arEntry.getSize());
		assertEquals(2, arEntry.getGroupId());
		assertTrue(arEntry.getLastModified() < System.currentTimeMillis() + 5);
		assertEquals(4, arEntry.getMode());
		assertEquals(5, arEntry.getUserId());
	}
}
