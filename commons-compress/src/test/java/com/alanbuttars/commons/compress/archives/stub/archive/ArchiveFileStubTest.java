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
import static org.junit.Assert.assertNull;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import org.apache.commons.compress.archivers.ArchiveEntry;
import org.junit.Before;
import org.junit.Test;

import com.alanbuttars.commons.compress.archives.config.entry.ArchiveEntryConfig;
import com.alanbuttars.commons.compress.archives.config.output.ArchiveOutputStreamConfig;
import com.alanbuttars.commons.compress.archives.output.ArchiveOutputStream;
import com.alanbuttars.commons.util.functions.DoubleInputFunction;
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
		assertEquals(directory, stub.directory);
	}

	@Test
	public void testAs() {
		ArchiveFileAsStub stub = new ArchiveFileStub(directory).as("blah");

		assertNull(stub.streamConfigFunction());
		assertNull(stub.streamFunction());
		assertNull(stub.entryConfigFunction());
		assertNull(stub.entryFunction());
	}

	@Test
	public void testAsInline() {
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
		DoubleInputFunction<String, Long, ArchiveEntryConfig> entryConfigFunction = new DoubleInputFunction<String, Long, ArchiveEntryConfig>() {

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

		ArchiveFileAsStub stub = new ArchiveFileStub(directory).as("blah", streamConfigFunction, streamFunction, entryConfigFunction, entryFunction);

		assertEquals(streamConfigFunction, stub.streamConfigFunction());
		assertEquals(streamFunction, stub.streamFunction());
		assertEquals(entryConfigFunction, stub.entryConfigFunction());
		assertEquals(entryFunction, stub.entryFunction());
	}

}
