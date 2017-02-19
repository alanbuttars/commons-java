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
package com.alanbuttars.commons.compress.files.input;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.powermock.api.mockito.PowerMockito.mock;

import java.io.IOException;

import org.apache.commons.compress.compressors.CompressorInputStream;
import org.junit.Before;
import org.junit.Test;

/**
 * Test class for {@link CompressedFileInputStreamImpl}.
 * 
 * @author Alan Buttars
 *
 */
public class CompressedFileInputStreamImplTest {

	private CompressedFileInputStreamImpl stream;
	private CompressorInputStream mockInput;

	@Before
	public void setup() {
		this.mockInput = mock(CompressorInputStream.class);
		this.stream = new CompressedFileInputStreamImpl(mockInput);
	}

	@Test
	public void testClose() throws IOException {
		stream.close();

		verify(mockInput, times(1)).close();
	}

	@Test
	public void testRead() throws IOException {
		stream.read(new byte[4]);

		verify(mockInput, times(1)).read(new byte[4]);
	}

	@Test
	public void testGetStream() {
		assertEquals(mockInput, stream.getStream());
	}
}
