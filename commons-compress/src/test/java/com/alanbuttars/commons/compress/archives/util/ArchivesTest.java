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
package com.alanbuttars.commons.compress.archives.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static org.powermock.api.mockito.PowerMockito.doThrow;
import static org.powermock.api.mockito.PowerMockito.mock;

import java.io.File;
import java.io.IOException;

import org.junit.Before;
import org.junit.Test;

import com.alanbuttars.commons.compress.archives.input.ArchiveInputStream;
import com.alanbuttars.commons.compress.archives.output.ArchiveOutputStream;
import com.alanbuttars.commons.util.functions.Function;

/**
 * Test class for {@link Archives}.
 * 
 * @author Alan Buttars
 *
 */
public class ArchivesTest {

	private File source;
	private File destination;

	@Before
	public void setup() throws IOException {
		source = File.createTempFile(getClass().getName(), ".tmp");
		source.deleteOnExit();
		destination = File.createTempFile(getClass().getName(), ".tmp");
		destination.deleteOnExit();
	}

	@SuppressWarnings("unchecked")
	@Test
	public void testDecompressThrowsRuntimeException() {
		Function<File, ArchiveInputStream> decompressionFunction = mock(Function.class);
		doThrow(new RuntimeException("runtime")).when(decompressionFunction).apply(source);
		try {
			Archives.decompress(Archives.AR, source, destination, decompressionFunction);
			fail();
		}
		catch (IOException e) {
			fail();
		}
		catch (RuntimeException e) {
			assertEquals("runtime", e.getMessage());
		}
	}

	@SuppressWarnings("unchecked")
	@Test
	public void testDecompressThrowsIOException() {
		Function<File, ArchiveInputStream> decompressionFunction = mock(Function.class);
		doThrow(new RuntimeException(new IOException("io"))).when(decompressionFunction).apply(source);
		try {
			Archives.decompress(Archives.AR, source, destination, decompressionFunction);
			fail();
		}
		catch (IOException e) {
			assertEquals("io", e.getMessage());
		}
		catch (RuntimeException e) {
			fail();
		}
	}

	@SuppressWarnings("unchecked")
	@Test
	public void testCompressThrowsRuntimeException() {
		Function<File, ArchiveOutputStream> compressionFunction = mock(Function.class);
		doThrow(new RuntimeException("runtime")).when(compressionFunction).apply(destination);
		try {
			Archives.compress(Archives.AR, source, destination, compressionFunction, null);
			fail();
		}
		catch (IOException e) {
			fail();
		}
		catch (RuntimeException e) {
			assertEquals("runtime", e.getMessage());
		}
	}

	@SuppressWarnings("unchecked")
	@Test
	public void testCompressThrowsIOException() {
		Function<File, ArchiveOutputStream> compressionFunction = mock(Function.class);
		doThrow(new RuntimeException(new IOException("io"))).when(compressionFunction).apply(destination);
		try {
			Archives.compress(Archives.AR, source, destination, compressionFunction, null);
			fail();
		}
		catch (IOException e) {
			assertEquals("io", e.getMessage());
		}
		catch (RuntimeException e) {
			fail();
		}
	}
}
