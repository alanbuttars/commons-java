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
package com.alanbuttars.commons.compress.files.output;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.powermock.api.mockito.PowerMockito.mock;

import java.io.IOException;

import org.apache.commons.compress.compressors.CompressorOutputStream;
import org.junit.Before;
import org.junit.Test;

/**
 * Test class for {@link CompressedFileOutputStreamImpl}.
 * 
 * @author Alan Buttars
 *
 */
public class CompressedFileOutputStreamImplTest {

	private CompressedFileOutputStreamImpl stream;
	private CompressorOutputStream mockOutput;

	@Before
	public void setup() {
		this.mockOutput = mock(CompressorOutputStream.class);
		this.stream = new CompressedFileOutputStreamImpl(mockOutput);
	}

	@Test
	public void testClose() throws IOException {
		stream.close();

		verify(mockOutput, times(1)).close();
	}

	@Test
	public void testWrite() throws IOException {
		stream.write(new byte[] { 1, 2 }, 0, 2);

		verify(mockOutput, times(1)).write(new byte[] { 1, 2 }, 0, 2);
	}

	@Test
	public void testGetStream() {
		assertEquals(mockOutput, stream.getStream());
	}
}
