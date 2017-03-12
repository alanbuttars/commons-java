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

import org.apache.commons.compress.archivers.ArchiveEntry;
import org.junit.Before;
import org.junit.Test;

import com.alanbuttars.commons.compress.archives.output.ArchiveOutputStream;
import com.alanbuttars.commons.util.functions.BiFunction;
import com.alanbuttars.commons.util.functions.Function;

/**
 * Test class for {@link CompressDirectoryStub}.
 * 
 * @author Alan Buttars
 *
 */
public class CompressDirectoryStubTest {

	private File source;
	private Function<File, ArchiveOutputStream> compressionFunction;
	private BiFunction<String, Long, ArchiveEntry> entryFunction;

	@Before
	public void setup() throws IOException {
		source = Files.createTempDirectory(getClass().getName()).toFile();
		compressionFunction = new Function<File, ArchiveOutputStream>() {

			@Override
			public ArchiveOutputStream apply(File input) {
				return null;
			}

		};
		entryFunction = new BiFunction<String, Long, ArchiveEntry>() {

			@Override
			public ArchiveEntry apply(String inputA, Long inputB) {
				return null;
			}

		};
	}

	@Test
	public void testConstructor() {
		CompressDirectoryStub stub = new CompressDirectoryStub(source);
		assertEquals(source, stub.source);
	}

	@Test
	public void testWithNullFileType() {
		try {
			new CompressDirectoryStub(source).with(null, compressionFunction, entryFunction);
			fail();
		}
		catch (IllegalArgumentException e) {
			assertEquals("File type must be non-null", e.getMessage());
		}
	}

	@Test
	public void testWithEmptyFileType() {
		try {
			new CompressDirectoryStub(source).with("", compressionFunction, entryFunction);
			fail();
		}
		catch (IllegalArgumentException e) {
			assertEquals("File type must be non-empty", e.getMessage());
		}
	}

	@Test
	public void testWithBlankFileType() {
		try {
			new CompressDirectoryStub(source).with(" ", compressionFunction, entryFunction);
			fail();
		}
		catch (IllegalArgumentException e) {
			assertEquals("File type must be non-empty", e.getMessage());
		}
	}

	@Test
	public void testWithNullCompressionFunction() {
		try {
			new CompressDirectoryStub(source).with("blah", null, entryFunction);
			fail();
		}
		catch (IllegalArgumentException e) {
			assertEquals("Compression function must be non-null", e.getMessage());
		}
	}

	@Test
	public void testWithNullEntryFunction() {
		try {
			new CompressDirectoryStub(source).with("blah", compressionFunction, null);
			fail();
		}
		catch (IllegalArgumentException e) {
			assertEquals("Entry function must be non-null", e.getMessage());
		}
	}
}
