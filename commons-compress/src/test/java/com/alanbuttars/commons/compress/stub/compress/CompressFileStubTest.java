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
import java.io.OutputStream;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.alanbuttars.commons.compress.files.output.CompressedFileOutputStream;
import com.alanbuttars.commons.util.functions.Function;

/**
 * Test class for {@link CompressFileStub}.
 * 
 * @author Alan Buttars
 *
 */
public class CompressFileStubTest {

	private File source;
	private Function<OutputStream, CompressedFileOutputStream> compressionFunction;

	@Before
	public void setup() throws IOException {
		source = File.createTempFile(getClass().getName(), ".tmp");
		compressionFunction = new Function<OutputStream, CompressedFileOutputStream>() {

			@Override
			public CompressedFileOutputStream apply(OutputStream input) {
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
		CompressFileStub stub = new CompressFileStub(source);
		assertEquals(source, stub.source);
	}

	@Test
	public void testWithNullFileType() {
		try {
			new CompressFileStub(source).with(null, compressionFunction);
			fail();
		}
		catch (IllegalArgumentException e) {
			assertEquals("File type must be non-null", e.getMessage());
		}
	}

	@Test
	public void testWithEmptyFileType() {
		try {
			new CompressFileStub(source).with("", compressionFunction);
			fail();
		}
		catch (IllegalArgumentException e) {
			assertEquals("File type must be non-empty", e.getMessage());
		}
	}

	@Test
	public void testWithBlankFileType() {
		try {
			new CompressFileStub(source).with(" ", compressionFunction);
			fail();
		}
		catch (IllegalArgumentException e) {
			assertEquals("File type must be non-empty", e.getMessage());
		}
	}

	@Test
	public void testWithNullCompressionFunction() {
		try {
			new CompressFileStub(source).with("blah", null);
			fail();
		}
		catch (IllegalArgumentException e) {
			assertEquals("Compression function must be non-null", e.getMessage());
		}
	}

}
