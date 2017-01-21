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

import static com.alanbuttars.commons.compress.util.Archives.ZIP;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.util.zip.ZipEntry;

import org.apache.commons.compress.archivers.ArchiveOutputStream;
import org.apache.commons.compress.archivers.zip.Zip64Mode;
import org.apache.commons.compress.archivers.zip.ZipArchiveOutputStream;
import org.apache.commons.compress.archivers.zip.ZipArchiveOutputStream.UnicodeExtraFieldPolicy;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import com.alanbuttars.commons.compress.config.output.ArchiveOutputStreamConfig;
import com.alanbuttars.commons.compress.config.output.ArchiveOutputStreamConfigZipImpl;
import com.alanbuttars.commons.util.functions.Function;

/**
 * Test class for {@link ArchiveOutputStreamFunctions}.
 * 
 * @author Alan Buttars
 *
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({ ZipArchiveOutputStream.class })
public class ArchiveOutputStreamFunctionsTest {

	private OutputStream outputStream;
	private Function<OutputStream, ArchiveOutputStreamConfig> configFunction;
	private Function<ArchiveOutputStreamConfig, ArchiveOutputStream> streamFunction;

	@Before
	public void setup() {
		outputStream = new ByteArrayOutputStream();
	}

	private void prepare(String archiveType) {
		configFunction = ArchiveOutputStreamFunctions.defaultConfigFunctions().get(archiveType);
		streamFunction = ArchiveOutputStreamFunctions.defaultStreamFunctions().get(archiveType);
	}

	@Test
	public void testZip() throws Exception {
		prepare(ZIP);

		ArchiveOutputStreamConfig config = configFunction.apply(outputStream);
		assertEquals(ArchiveOutputStreamConfigZipImpl.class, config.getClass());
		ArchiveOutputStreamConfigZipImpl zipConfig = (ArchiveOutputStreamConfigZipImpl) config;
		assertEquals("", zipConfig.getComment());
		assertFalse(zipConfig.fallbackToUTF8());
		assertEquals(ZipArchiveOutputStream.DEFAULT_COMPRESSION, zipConfig.getLevel());
		assertEquals(ZipEntry.DEFLATED, zipConfig.getMethod());
		assertEquals(outputStream, zipConfig.getOutputStream());
		assertEquals(UnicodeExtraFieldPolicy.NEVER, zipConfig.getUnicodeExtraFieldPolicy());
		assertTrue(zipConfig.useLanguageEncoding());
		assertEquals(Zip64Mode.AsNeeded, zipConfig.getZip64Mode());

		ArchiveOutputStream stream = streamFunction.apply(zipConfig);
		assertEquals(ZipArchiveOutputStream.class, stream.getClass());
	}

}