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

import org.apache.commons.compress.archivers.ArchiveInputStream;
import org.apache.commons.compress.archivers.ar.ArArchiveInputStream;
import org.apache.commons.compress.archivers.arj.ArjArchiveInputStream;
import org.apache.commons.compress.archivers.cpio.CpioArchiveInputStream;
import org.apache.commons.compress.archivers.dump.DumpArchiveInputStream;
import org.apache.commons.compress.archivers.jar.JarArchiveInputStream;
import org.apache.commons.compress.archivers.tar.TarArchiveInputStream;
import org.apache.commons.compress.archivers.zip.ZipArchiveInputStream;
import org.junit.Test;

/**
 * Test class for {@link ArchiveInputStreamImpl}.
 * 
 * @author Alan Buttars
 *
 */
public class ArchiveInputStreamImplTest {

	private <T extends org.apache.commons.compress.archivers.ArchiveInputStream> T mockInput(Class<T> clazz) {
		return mock(clazz);
	}

	private ArchiveInputStreamImpl stream(ArchiveInputStream mockInput) {
		return new ArchiveInputStreamImpl(mockInput);
	}

	@Test
	public void testClose() throws IOException {
		ArchiveInputStream mockInput = mockInput(org.apache.commons.compress.archivers.ArchiveInputStream.class);
		stream(mockInput).close();

		verify(mockInput, times(1)).close();
	}

	@Test
	public void testRead() throws IOException {
		ArchiveInputStream mockInput = mockInput(org.apache.commons.compress.archivers.ArchiveInputStream.class);
		stream(mockInput).read(new byte[4]);

		verify(mockInput, times(1)).read(new byte[4]);
	}

	@Test
	public void testGetNextEntry() throws IOException {
		ArchiveInputStream mockInput = mockInput(ArchiveInputStream.class);
		stream(mockInput).getNextEntry();

		verify(mockInput, times(1)).getNextEntry();
	}

	@Test
	public void testGetNextEntryAr() throws IOException {
		ArArchiveInputStream mockInput = mockInput(ArArchiveInputStream.class);
		stream(mockInput).getNextEntry();

		verify(mockInput, times(1)).getNextArEntry();
	}

	@Test
	public void testGetNextEntryArj() throws IOException {
		ArjArchiveInputStream mockInput = mockInput(ArjArchiveInputStream.class);
		stream(mockInput).getNextEntry();

		verify(mockInput, times(1)).getNextEntry();
	}

	@Test
	public void testGetNextEntryCpio() throws IOException {
		CpioArchiveInputStream mockInput = mockInput(CpioArchiveInputStream.class);
		stream(mockInput).getNextEntry();

		verify(mockInput, times(1)).getNextCPIOEntry();
	}

	@Test
	public void testGetNextEntryDump() throws IOException {
		DumpArchiveInputStream mockInput = mockInput(DumpArchiveInputStream.class);
		stream(mockInput).getNextEntry();

		verify(mockInput, times(1)).getNextDumpEntry();
	}

	@Test
	public void testGetNextEntryJar() throws IOException {
		JarArchiveInputStream mockInput = mockInput(JarArchiveInputStream.class);
		stream(mockInput).getNextEntry();

		verify(mockInput, times(1)).getNextJarEntry();
	}

	@Test
	public void testGetNextEntryTar() throws IOException {
		TarArchiveInputStream mockInput = mockInput(TarArchiveInputStream.class);
		stream(mockInput).getNextEntry();

		verify(mockInput, times(1)).getNextTarEntry();
	}

	@Test
	public void testGetNextEntryZip() throws IOException {
		ZipArchiveInputStream mockInput = mockInput(ZipArchiveInputStream.class);
		stream(mockInput).getNextEntry();

		verify(mockInput, times(1)).getNextZipEntry();
	}

	@Test
	public void testGetStream() {
		ArchiveInputStream mockInput = mockInput(org.apache.commons.compress.archivers.ArchiveInputStream.class);
		assertEquals(mockInput, stream(mockInput).getStream());
	}
}
