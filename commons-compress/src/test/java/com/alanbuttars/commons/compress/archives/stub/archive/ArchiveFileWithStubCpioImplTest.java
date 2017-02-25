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
package com.alanbuttars.commons.compress.archives.stub.archive;

import static com.alanbuttars.commons.compress.archives.util.Archives.CPIO;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyShort;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.powermock.api.mockito.PowerMockito.doReturn;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;

import org.apache.commons.compress.archivers.ArchiveException;
import org.apache.commons.compress.archivers.cpio.CpioConstants;
import org.apache.commons.compress.utils.CharsetNames;
import org.junit.Before;
import org.junit.Test;

import com.alanbuttars.commons.compress.archives.config.ArchiveConfigs;
import com.alanbuttars.commons.compress.archives.config.output.ArchiveOutputStreamConfig;

/**
 * Test class for {@link ArchiveFileWithStubCpioImpl}.
 * 
 * @author Alan Buttars
 *
 */
public class ArchiveFileWithStubCpioImplTest {

	private File directory;
	private File destination;
	private ArchiveFileWithStubCpioImpl stub;

	@Before
	public void setup() throws IOException {
		directory = Files.createTempDirectory(getClass().getName()).toFile();
		destination = File.createTempFile(getClass().getName(), ".tmp");
		stub = spy(new ArchiveFileWithStubCpioImpl(directory));
	}

	@Test
	public void testConstructor() {
		assertEquals(directory, stub.source);
		assertEquals(CPIO, stub.archiveType);
		assertEquals(ArchiveConfigs.OUTPUT_CONFIG_FUNCTIONS.get(CPIO), stub.streamConfigFunction());
		assertNotEquals(ArchiveConfigs.OUTPUT_STREAM_FUNCTIONS.get(CPIO), stub.streamFunction());
	}

	@Test
	public void testStreamFunction() throws ArchiveException {
		doReturn(null).when(stub).createArchiveOutputStream(any(OutputStream.class), anyShort(), anyInt(), anyString());
		ArchiveOutputStreamConfig config = stub.streamConfigFunction().apply(destination);
		stub.streamFunction().apply(config);

		verify(stub).createArchiveOutputStream(any(OutputStream.class), eq(CpioConstants.FORMAT_NEW), eq(CpioConstants.BLOCK_SIZE), eq(CharsetNames.US_ASCII));
	}

	@Test
	public void testCustomizedStreamFunction() throws ArchiveException {
		stub.withFormat((short) 2).withBlockSize(4).withEncoding("blah");
		doReturn(null).when(stub).createArchiveOutputStream(any(OutputStream.class), anyShort(), anyInt(), anyString());
		ArchiveOutputStreamConfig config = stub.streamConfigFunction().apply(destination);
		stub.streamFunction().apply(config);

		verify(stub).createArchiveOutputStream(any(OutputStream.class), eq((short) 2), eq(4), eq("blah"));
	}

}