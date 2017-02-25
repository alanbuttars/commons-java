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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import org.apache.commons.compress.archivers.ArchiveEntry;
import org.junit.Before;
import org.junit.Test;

import com.alanbuttars.commons.compress.archives.config.entry.ArchiveEntryConfig;
import com.alanbuttars.commons.compress.archives.config.output.ArchiveOutputStreamConfig;
import com.alanbuttars.commons.compress.archives.output.ArchiveOutputStream;
import com.alanbuttars.commons.compress.archives.util.Archives;
import com.alanbuttars.commons.util.functions.BiFunction;
import com.alanbuttars.commons.util.functions.Function;

/**
 * Test class for {@link ArchiveFileStub}.
 * 
 * @author Alan Buttars
 *
 */
public class ArchiveFileStubTest {

	private File directory;

	@Before
	public void setup() throws IOException {
		directory = Files.createTempDirectory(getClass().getName()).toFile();
	}

	@Test
	public void testConstructor() {
		ArchiveFileStub stub = new ArchiveFileStub(directory);
		assertEquals(directory, stub.source);
	}

	@Test
	public void testWith() {
		ArchiveFileWithStub stub = new ArchiveFileStub(directory).with(Archives.AR);

		assertNotNull(stub.streamConfigFunction());
		assertNotNull(stub.streamFunction());
		assertNotNull(stub.entryConfigFunction());
		assertNotNull(stub.entryFunction());
	}

	@Test
	public void testWithNullArchiveType() {
		try {
			new ArchiveFileStub(directory).with(null);
			fail();
		}
		catch (IllegalArgumentException e) {
			assertEquals("Archive type must be non-null", e.getMessage());
		}
	}

	@Test
	public void testWithEmptyArchiveType() {
		try {
			new ArchiveFileStub(directory).with("");
			fail();
		}
		catch (IllegalArgumentException e) {
			assertEquals("Archive type must be non-empty", e.getMessage());
		}
	}

	@Test
	public void testWithBlankArchiveType() {
		try {
			new ArchiveFileStub(directory).with(" ");
			fail();
		}
		catch (IllegalArgumentException e) {
			assertEquals("Archive type must be non-empty", e.getMessage());
		}
	}

	@Test
	public void testWithUnconfiguredArchiveType() {
		try {
			new ArchiveFileStub(directory).with("blah");
			fail();
		}
		catch (IllegalArgumentException e) {
			assertEquals("Stream config function is not configured for archive type blah", e.getMessage());
		}
	}

	@Test
	public void testWithInline() {
		Function<File, ArchiveOutputStreamConfig> streamConfigFunction = new Function<File, ArchiveOutputStreamConfig>() {

			@Override
			public ArchiveOutputStreamConfig apply(File input) {
				return null;
			}

		};
		Function<ArchiveOutputStreamConfig, ArchiveOutputStream> streamFunction = new Function<ArchiveOutputStreamConfig, ArchiveOutputStream>() {

			@Override
			public ArchiveOutputStream apply(ArchiveOutputStreamConfig input) {
				return null;
			}

		};
		BiFunction<String, Long, ArchiveEntryConfig> entryConfigFunction = new BiFunction<String, Long, ArchiveEntryConfig>() {

			@Override
			public ArchiveEntryConfig apply(String inputA, Long inputB) {
				return null;
			}

		};
		Function<ArchiveEntryConfig, ArchiveEntry> entryFunction = new Function<ArchiveEntryConfig, ArchiveEntry>() {

			@Override
			public ArchiveEntry apply(ArchiveEntryConfig input) {
				return null;
			}

		};

		ArchiveFileWithStub stub = new ArchiveFileStub(directory).with("blah", streamConfigFunction, streamFunction, entryConfigFunction, entryFunction);

		assertEquals(streamConfigFunction, stub.streamConfigFunction());
		assertEquals(streamFunction, stub.streamFunction());
		assertEquals(entryConfigFunction, stub.entryConfigFunction());
		assertEquals(entryFunction, stub.entryFunction());
	}
	
	@Test
	public void testWithInlineNullArchiveType() {
		try {
			new ArchiveFileStub(directory).with(null, null, null, null, null);
			fail();
		}
		catch (IllegalArgumentException e) {
			assertEquals("Archive type must be non-null", e.getMessage());
		}
	}

	@Test
	public void testWithInlineEmptyArchiveType() {
		try {
			new ArchiveFileStub(directory).with("", null, null, null, null);
			fail();
		}
		catch (IllegalArgumentException e) {
			assertEquals("Archive type must be non-empty", e.getMessage());
		}
	}

	@Test
	public void testWithInlineBlankArchiveType() {
		try {
			new ArchiveFileStub(directory).with(" ", null, null, null, null);
			fail();
		}
		catch (IllegalArgumentException e) {
			assertEquals("Archive type must be non-empty", e.getMessage());
		}
	}

	@Test
	public void testWithInlineNullStreamConfigFunction() {
		try {
			new ArchiveFileStub(directory).with(Archives.AR, null, null, null, null);
			fail();
		}
		catch (IllegalArgumentException e) {
			assertEquals("Stream config function must be non-null", e.getMessage());
		}
	}
}
