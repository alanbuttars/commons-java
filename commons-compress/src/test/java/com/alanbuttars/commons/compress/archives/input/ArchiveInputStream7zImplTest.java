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
package com.alanbuttars.commons.compress.archives.input;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.powermock.api.mockito.PowerMockito.mock;

import java.io.IOException;

import org.apache.commons.compress.archivers.sevenz.SevenZFile;
import org.junit.Before;
import org.junit.Test;

/**
 * Test class for {@link ArchiveInputStream7zImpl}.
 * 
 * @author Alan Buttars
 *
 */
public class ArchiveInputStream7zImplTest {

	private ArchiveInputStream7zImpl stream;
	private SevenZFile mockInput;

	@Before
	public void setup() {
		this.mockInput = mock(SevenZFile.class);
		this.stream = new ArchiveInputStream7zImpl(mockInput);
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
	public void testGetNextEntry() throws IOException {
		stream.getNextEntry();

		verify(mockInput, times(1)).getNextEntry();
	}

	@Test
	public void testGetStream() {
		assertEquals(mockInput, stream.getStream());
	}
}
