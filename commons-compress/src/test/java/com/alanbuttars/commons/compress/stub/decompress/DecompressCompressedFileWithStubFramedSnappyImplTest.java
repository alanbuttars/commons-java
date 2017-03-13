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

import static com.alanbuttars.commons.compress.files.util.CompressedFiles.SNAPPY;
import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.compress.compressors.snappy.FramedSnappyDialect;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.modules.junit4.PowerMockRunner;

/**
 * Test class for {@link DecompressCompressedFileWithStubFramedSnappyImpl}.
 * 
 * @author Alan Buttars
 *
 */
@RunWith(PowerMockRunner.class)
public class DecompressCompressedFileWithStubFramedSnappyImplTest {

	private File source;
	private File destination;
	private DecompressCompressedFileWithStubFramedSnappyImpl stub;

	@Before
	public void setup() throws IOException {
		source = File.createTempFile(getClass().getName(), ".tmp");
		destination = File.createTempFile(getClass().getName(), ".tmp");
		stub = spy(new DecompressCompressedFileWithStubFramedSnappyImpl(source));
	}

	@After
	public void teardown() {
		source.deleteOnExit();
		destination.deleteOnExit();
	}

	@Test
	public void testConstructor() {
		assertEquals(source, stub.source);
		assertEquals(SNAPPY, stub.fileType);
	}

	@Test(expected = RuntimeException.class)
	public void testCompressionFunction() throws IOException {
		stub.to(destination);
		verify(stub, times(1)).createCompressedFileInputStream(any(InputStream.class), eq(FramedSnappyDialect.STANDARD));
	}

	@Test
	public void testCustomCompressionFunction() throws IOException {
		stub.andDialect(FramedSnappyDialect.IWORK_ARCHIVE).to(destination);
		verify(stub, times(1)).createCompressedFileInputStream(any(InputStream.class), eq(FramedSnappyDialect.IWORK_ARCHIVE));
	}
}
