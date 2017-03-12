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
package com.alanbuttars.commons.compress.stub.decompress;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.io.File;
import java.io.IOException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.alanbuttars.commons.compress.archives.input.ArchiveInputStream;
import com.alanbuttars.commons.util.functions.Function;

/**
 * Test class for {@link DecompressArchiveStub}.
 * 
 * @author Alan Buttars
 *
 */
public class DecompressArchiveStubTest {

	private File source;
	private Function<File, ArchiveInputStream> decompressionFunction;

	@Before
	public void setup() throws IOException {
		source = File.createTempFile(getClass().getName(), ".tmp");
		decompressionFunction = new Function<File, ArchiveInputStream>() {

			@Override
			public ArchiveInputStream apply(File input) {
				return null;
			}

		};
	}
	
	@After
	public void teardown() {
		source.deleteOnExit();
	}

	@Test
	public void testConstructor() {
		DecompressArchiveStub stub = new DecompressArchiveStub(source);
		assertEquals(source, stub.source);
	}

	@Test
	public void testWithNullFileType() {
		try {
			new DecompressArchiveStub(source).with(null, decompressionFunction);
			fail();
		}
		catch (IllegalArgumentException e) {
			assertEquals("File type must be non-null", e.getMessage());
		}
	}

	@Test
	public void testWithEmptyFileType() {
		try {
			new DecompressArchiveStub(source).with("", decompressionFunction);
			fail();
		}
		catch (IllegalArgumentException e) {
			assertEquals("File type must be non-empty", e.getMessage());
		}
	}

	@Test
	public void testWithBlankFileType() {
		try {
			new DecompressArchiveStub(source).with(" ", decompressionFunction);
			fail();
		}
		catch (IllegalArgumentException e) {
			assertEquals("File type must be non-empty", e.getMessage());
		}
	}

	@Test
	public void testWithNullDecompressionFunction() {
		try {
			new DecompressArchiveStub(source).with("blah", null);
			fail();
		}
		catch (IllegalArgumentException e) {
			assertEquals("Decompression function must be non-null", e.getMessage());
		}
	}

}
