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

import static com.alanbuttars.commons.compress.archives.util.Archives.SEVENZ;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.powermock.api.mockito.PowerMockito.doReturn;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Collections;
import java.util.List;

import org.apache.commons.compress.archivers.sevenz.SevenZMethod;
import org.apache.commons.compress.archivers.sevenz.SevenZMethodConfiguration;
import org.junit.Before;
import org.junit.Test;

import com.alanbuttars.commons.compress.archives.config.ArchiveConfigs;
import com.alanbuttars.commons.compress.archives.config.output.ArchiveOutputStreamConfig;
import com.alanbuttars.commons.compress.archives.config.output.ArchiveOutputStreamConfigSevenZImpl;

/**
 * Test class for {@link ArchiveFileAsStubSevenZImpl}.
 * 
 * @author Alan Buttars
 *
 */
public class ArchiveFileAsStubSevenZImplTest {

	private File directory;
	private File destination;
	private ArchiveFileAsStubSevenZImpl stub;

	@Before
	public void setup() throws IOException {
		directory = Files.createTempDirectory(getClass().getName()).toFile();
		destination = File.createTempFile(getClass().getName(), ".tmp");
		stub = spy(new ArchiveFileAsStubSevenZImpl(directory));
	}

	@Test
	public void testConstructor() {
		assertEquals(directory, stub.directory);
		assertEquals(SEVENZ, stub.archiveType);
		assertEquals(ArchiveConfigs.OUTPUT_CONFIG_FUNCTIONS.get(SEVENZ), stub.streamConfigFunction());
		assertNotEquals(ArchiveConfigs.OUTPUT_STREAM_FUNCTIONS.get(SEVENZ), stub.streamFunction());
	}

	@Test
	public void testStreamFunction() throws IOException {
		List<SevenZMethodConfiguration> contentMethods = Collections.singletonList(new SevenZMethodConfiguration(SevenZMethod.LZMA2));
		doReturn(null).when(stub).createArchiveOutputStream(any(File.class), any(Iterable.class));
		ArchiveOutputStreamConfig config = stub.streamConfigFunction().apply(destination);
		ArchiveOutputStreamConfigSevenZImpl sevenZConfig = (ArchiveOutputStreamConfigSevenZImpl) config;
		sevenZConfig.setContentMethods(contentMethods);
		stub.streamFunction().apply(config);

		verify(stub).createArchiveOutputStream(any(File.class), eq(contentMethods));
	}

	@Test
	public void testCustomizedStreamFunction() throws IOException {
		List<SevenZMethodConfiguration> customizedContentMethods = Collections.singletonList(new SevenZMethodConfiguration(SevenZMethod.AES256SHA256));
		stub.withContentMethods(customizedContentMethods);

		List<SevenZMethodConfiguration> contentMethods = Collections.singletonList(new SevenZMethodConfiguration(SevenZMethod.LZMA2));
		doReturn(null).when(stub).createArchiveOutputStream(any(File.class), any(Iterable.class));
		ArchiveOutputStreamConfig config = stub.streamConfigFunction().apply(destination);
		ArchiveOutputStreamConfigSevenZImpl sevenZConfig = (ArchiveOutputStreamConfigSevenZImpl) config;
		sevenZConfig.setContentMethods(contentMethods);
		stub.streamFunction().apply(config);

		verify(stub).createArchiveOutputStream(any(File.class), eq(customizedContentMethods));
	}

}
