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

import static com.alanbuttars.commons.compress.files.util.CompressedFiles.PACK200;
import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Matchers.isNull;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.compress.compressors.pack200.Pack200Strategy;
import org.junit.Before;
import org.junit.Test;

/**
 * Test class for {@link CompressFileWithStubPack200Impl}.
 * 
 * @author Alan Buttars
 *
 */
public class CompressFileWithStubPack200ImplTest {

	private File source;
	private File destination;
	private CompressFileWithStubPack200Impl stub;

	@Before
	public void setup() throws IOException {
		source = File.createTempFile(getClass().getName(), ".tmp");
		destination = File.createTempFile(getClass().getName(), ".tmp");
		stub = spy(new CompressFileWithStubPack200Impl(source));
	}

	@Test
	public void testConstructor() {
		assertEquals(source, stub.source);
		assertEquals(PACK200, stub.fileType);
	}

	@Test
	public void testCompressionFunction() throws IOException {
		stub.to(destination);
		verify(stub, times(1)).createCompressedFileOutputStream(any(OutputStream.class), eq(Pack200Strategy.IN_MEMORY), isNull(Map.class));
	}

	@Test
	public void testCustomCompressionFunction() throws IOException {
		Map<String, String> properties = new HashMap<>();
		stub.andMode(Pack200Strategy.TEMP_FILE).andProperties(properties).to(destination);
		verify(stub, times(1)).createCompressedFileOutputStream(any(OutputStream.class), eq(Pack200Strategy.TEMP_FILE), eq(properties));
	}
}
