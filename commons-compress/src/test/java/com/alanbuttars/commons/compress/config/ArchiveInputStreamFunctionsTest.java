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
package com.alanbuttars.commons.compress.config;

import static com.alanbuttars.commons.compress.util.Archives.TAR;
import static com.alanbuttars.commons.compress.util.Archives.ZIP;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import org.apache.commons.compress.archivers.ArchiveInputStream;
import org.apache.commons.compress.archivers.tar.TarArchiveInputStream;
import org.apache.commons.compress.archivers.tar.TarConstants;
import org.apache.commons.compress.archivers.zip.ZipArchiveInputStream;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import com.alanbuttars.commons.compress.config.input.ArchiveInputStreamConfig;
import com.alanbuttars.commons.compress.config.input.ArchiveInputStreamConfigTarImpl;
import com.alanbuttars.commons.compress.config.input.ArchiveInputStreamConfigZipImpl;
import com.alanbuttars.commons.util.functions.Function;

/**
 * Test class for {@link ArchiveInputStreamFunctions}.
 * 
 * @author Alan Buttars
 *
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({ ZipArchiveInputStream.class })
public class ArchiveInputStreamFunctionsTest {

	private InputStream inputStream;
	private Function<InputStream, ArchiveInputStreamConfig> configFunction;
	private Function<ArchiveInputStreamConfig, ArchiveInputStream> streamFunction;

	@Before
	public void setup() {
		inputStream = new ByteArrayInputStream("a\nb\nc".getBytes());
	}

	private void prepare(String archiveType) {
		configFunction = ArchiveInputStreamFunctions.defaultConfigFunctions().get(archiveType);
		streamFunction = ArchiveInputStreamFunctions.defaultStreamFunctions().get(archiveType);
	}

	@Test
	public void testTar() throws Exception {
		prepare(TAR);

		ArchiveInputStreamConfig config = configFunction.apply(inputStream);
		assertEquals(ArchiveInputStreamConfigTarImpl.class, config.getClass());
		ArchiveInputStreamConfigTarImpl tarConfig = (ArchiveInputStreamConfigTarImpl) config;
		assertNull(tarConfig.getEncoding());
		assertEquals(TarConstants.DEFAULT_BLKSIZE, tarConfig.getBlockSize());
		assertEquals(TarConstants.DEFAULT_RCDSIZE, tarConfig.getRecordSize());

		ArchiveInputStream stream = streamFunction.apply(tarConfig);
		assertEquals(TarArchiveInputStream.class, stream.getClass());
	}

	@Test
	public void testZip() throws Exception {
		prepare(ZIP);

		ArchiveInputStreamConfig config = configFunction.apply(inputStream);
		assertEquals(ArchiveInputStreamConfigZipImpl.class, config.getClass());
		ArchiveInputStreamConfigZipImpl zipConfig = (ArchiveInputStreamConfigZipImpl) config;
		assertFalse(zipConfig.allowStoredEntriesWithDataDescriptor());
		assertEquals("UTF8", zipConfig.getEncoding());
		assertTrue(zipConfig.useUnicodeExtraFields());
		assertEquals(inputStream, zipConfig.getInputStream());

		ArchiveInputStream stream = streamFunction.apply(zipConfig);
		assertEquals(ZipArchiveInputStream.class, stream.getClass());
	}

}
