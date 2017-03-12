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
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.alanbuttars.commons.compress.archives.output.ArchiveOutputStream;
import com.alanbuttars.commons.util.functions.BiFunction;
import com.alanbuttars.commons.util.functions.Function;

/**
 * Test class for {@link CompressDirectoryWithStub}.
 * 
 * @author Alan Buttars
 *
 */
public class CompressDirectoryWithStubTest {

	private File source;
	private File destination;
	private CompressDirectoryWithStub stub;

	@Before
	public void setup() throws IOException {
		source = Files.createTempDirectory(getClass().getName()).toFile();
		destination = File.createTempFile(getClass().getName(), ".tmp");
		stub = new CompressDirectoryWithStub(source, "blah") {

			@Override
			protected BiFunction<String, Long, ArchiveEntry> entryFunction() {
				return null;
			}

			@Override
			protected Function<File, ArchiveOutputStream> compressionFunction() {
				return null;
			}
		};
	}
	
	@After
	public void teardown() {
		source.deleteOnExit();
		destination.deleteOnExit();
	}

	@Test
	public void testToDestinationIsNull() throws IOException {
		try {
			stub.to(null);
			fail();
		}
		catch (IllegalArgumentException e) {
			assertEquals("Destination must be non-null", e.getMessage());
		}
	}

	@Test
	public void testToDestinationIsDirectory() throws IOException {
		File directory = Files.createTempDirectory(getClass().getName()).toFile();
		try {
			stub.to(directory);
			fail();
		}
		catch (IllegalArgumentException e) {
			assertEquals("Destination " + directory.getAbsolutePath() + " must not be a directory", e.getMessage());
		}
	}

	@Test
	public void testToDestinationIsNotWriteable() throws IOException {
		destination.setWritable(false);
		try {
			stub.to(destination);
			fail();
		}
		catch (IllegalArgumentException e) {
			assertEquals("Destination " + destination.getAbsolutePath() + " is not writeable", e.getMessage());
		}
	}

}
