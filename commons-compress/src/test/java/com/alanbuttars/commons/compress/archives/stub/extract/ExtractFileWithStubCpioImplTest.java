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
package com.alanbuttars.commons.compress.archives.stub.extract;

import static com.alanbuttars.commons.compress.archives.util.Archives.CPIO;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.powermock.api.mockito.PowerMockito.doReturn;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.compress.archivers.ArchiveException;
import org.apache.commons.compress.archivers.cpio.CpioConstants;
import org.apache.commons.compress.utils.CharsetNames;
import org.junit.Before;
import org.junit.Test;

import com.alanbuttars.commons.compress.archives.config.ArchiveConfigs;
import com.alanbuttars.commons.compress.archives.config.input.ArchiveInputStreamConfig;

/**
 * Test class for {@link ExtractFileWithStubCpioImpl}
 * 
 * @author Alan Buttars
 *
 */
public class ExtractFileWithStubCpioImplTest {

	private File archive;
	private ExtractFileWithStubCpioImpl stub;

	@Before
	public void setup() throws IOException {
		archive = File.createTempFile(getClass().getName(), ".tmp");
		stub = spy(new ExtractFileWithStubCpioImpl(archive));
	}

	@Test
	public void testConstructor() {
		assertEquals(archive, stub.source);
		assertEquals(CPIO, stub.archiveType);
		assertEquals(ArchiveConfigs.INPUT_CONFIG_FUNCTIONS.get(CPIO), stub.streamConfigFunction());
		assertNotEquals(ArchiveConfigs.INPUT_STREAM_FUNCTIONS.get(CPIO), stub.streamFunction());
	}

	@Test
	public void testStreamFunction() throws ArchiveException {
		doReturn(null).when(stub).createArchiveInputStream(any(InputStream.class), anyInt(), anyString());
		ArchiveInputStreamConfig config = stub.streamConfigFunction().apply(archive);
		stub.streamFunction().apply(config);

		verify(stub).createArchiveInputStream(any(InputStream.class), eq(CpioConstants.BLOCK_SIZE), eq(CharsetNames.US_ASCII));
	}

	@Test
	public void testCustomizedStreamFunction() throws ArchiveException {
		stub.withBlockSize(2).withEncoding("blah");
		doReturn(null).when(stub).createArchiveInputStream(any(InputStream.class), anyInt(), anyString());
		ArchiveInputStreamConfig config = stub.streamConfigFunction().apply(archive);
		stub.streamFunction().apply(config);

		verify(stub).createArchiveInputStream(any(InputStream.class), eq(2), eq("blah"));
	}

}