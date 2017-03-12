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

import static com.alanbuttars.commons.compress.archives.util.Archives.SEVENZ;
import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.eq;
import static org.mockito.Matchers.isNotNull;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Collections;

import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.sevenz.SevenZArchiveEntry;
import org.apache.commons.compress.archivers.sevenz.SevenZMethod;
import org.apache.commons.compress.archivers.sevenz.SevenZMethodConfiguration;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * Test class for {@link CompressDirectoryWithStub7zImpl}.
 * 
 * @author Alan Buttars
 *
 */
public class CompressDirectoryWithStub7zImplTest {

	private File source;
	private File destination;
	private CompressDirectoryWithStub7zImpl stub;

	@Before
	public void setup() throws IOException {
		source = Files.createTempDirectory(getClass().getName()).toFile();
		destination = File.createTempFile(getClass().getName(), ".tmp");
		stub = spy(new CompressDirectoryWithStub7zImpl(source));
	}
	
	@After
	public void teardown() {
		source.deleteOnExit();
		destination.deleteOnExit();
	}

	@Test
	public void testConstructor() {
		assertEquals(source, stub.source);
		assertEquals(SEVENZ, stub.fileType);
	}

	@Test
	public void testCompressionFunction() throws IOException {
		stub.to(destination);
		verify(stub, times(1)).createArchiveOutputStream(eq(destination), isNotNull(Iterable.class));
	}

	@Test
	public void testCustomCompressionFunction() throws IOException {
		Iterable<? extends SevenZMethodConfiguration> contentMethods = Collections.singletonList(new SevenZMethodConfiguration(SevenZMethod.LZMA2));
		stub.andContentMethods(contentMethods).to(destination);
		verify(stub, times(1)).createArchiveOutputStream(eq(destination), eq(contentMethods));
	}

	@Test
	public void testEntryFunction() {
		ArchiveEntry entry = stub.entryFunction().apply("name", 1L);
		assertEquals(SevenZArchiveEntry.class, entry.getClass());
		SevenZArchiveEntry sevenZEntry = (SevenZArchiveEntry) entry;
		assertEquals("name", sevenZEntry.getName());
		assertEquals(0, sevenZEntry.getSize());
	}
}
