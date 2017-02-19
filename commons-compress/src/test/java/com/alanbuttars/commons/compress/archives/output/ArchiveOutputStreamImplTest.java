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
package com.alanbuttars.commons.compress.archives.output;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.powermock.api.mockito.PowerMockito.mock;

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;

/**
 * Test class for {@link ArchiveOutputStreamImpl}.
 * 
 * @author Alan Buttars
 *
 */
public class ArchiveOutputStreamImplTest {

	private ArchiveOutputStreamImpl stream;
	private org.apache.commons.compress.archivers.ArchiveOutputStream mockOutput;

	@Before
	public void setup() {
		this.mockOutput = mock(org.apache.commons.compress.archivers.ArchiveOutputStream.class);
		this.stream = new ArchiveOutputStreamImpl(mockOutput);
	}

	@Test
	public void testClose() throws IOException {
		stream.close();

		verify(mockOutput, times(1)).close();
	}

	@Test
	public void testPutArchiveEntry() throws IOException {
		stream.putArchiveEntry(null);

		verify(mockOutput, times(1)).putArchiveEntry(null);
	}

	@Test
	public void testCloseArchiveEntry() throws IOException {
		stream.closeArchiveEntry();

		verify(mockOutput, times(1)).closeArchiveEntry();
	}

	@Test
	public void testWrite() throws IOException {
		stream.write(new byte[] { 1, 2 }, 0, 2);

		verify(mockOutput, times(1)).write(new byte[] { 1, 2 }, 0, 2);
	}

	@Test
	public void testFlush() throws IOException {
		stream.flush();

		verify(mockOutput, times(1)).flush();
	}

	@Test
	public void testGetStream() {
		assertEquals(mockOutput, stream.getStream());
	}
}
