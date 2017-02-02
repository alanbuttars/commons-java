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

import static com.alanbuttars.commons.compress.util.Archives.AR;
import static com.alanbuttars.commons.compress.util.Archives.CPIO;
import static com.alanbuttars.commons.compress.util.Archives.DUMP;
import static com.alanbuttars.commons.compress.util.Archives.TAR;
import static com.alanbuttars.commons.compress.util.Archives.ZIP;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.util.zip.ZipEntry;

import org.apache.commons.compress.archivers.ArchiveOutputStream;
import org.apache.commons.compress.archivers.ar.ArArchiveOutputStream;
import org.apache.commons.compress.archivers.cpio.CpioArchiveOutputStream;
import org.apache.commons.compress.archivers.cpio.CpioConstants;
import org.apache.commons.compress.archivers.tar.TarArchiveOutputStream;
import org.apache.commons.compress.archivers.tar.TarConstants;
import org.apache.commons.compress.archivers.zip.Zip64Mode;
import org.apache.commons.compress.archivers.zip.ZipArchiveOutputStream;
import org.apache.commons.compress.archivers.zip.ZipArchiveOutputStream.UnicodeExtraFieldPolicy;
import org.apache.commons.compress.utils.CharsetNames;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import com.alanbuttars.commons.compress.config.output.ArchiveOutputStreamConfig;
import com.alanbuttars.commons.compress.config.output.ArchiveOutputStreamConfigArImpl;
import com.alanbuttars.commons.compress.config.output.ArchiveOutputStreamConfigCpioImpl;
import com.alanbuttars.commons.compress.config.output.ArchiveOutputStreamConfigTarImpl;
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
	public void testAr() throws Exception {
		prepare(AR);

		ArchiveOutputStreamConfig config = configFunction.apply(outputStream);
		assertEquals(ArchiveOutputStreamConfigArImpl.class, config.getClass());
		ArchiveOutputStreamConfigArImpl arConfig = (ArchiveOutputStreamConfigArImpl) config;
		assertEquals(TarArchiveOutputStream.LONGFILE_ERROR, arConfig.getLongFileMode());

		ArchiveOutputStream stream = streamFunction.apply(arConfig);
		assertEquals(ArArchiveOutputStream.class, stream.getClass());
	}

	@Test
	public void testCpio() throws Exception {
		prepare(CPIO);

		ArchiveOutputStreamConfig config = configFunction.apply(outputStream);
		assertEquals(ArchiveOutputStreamConfigCpioImpl.class, config.getClass());
		ArchiveOutputStreamConfigCpioImpl cpioConfig = (ArchiveOutputStreamConfigCpioImpl) config;
		assertEquals(CharsetNames.US_ASCII, cpioConfig.getEncoding());
		assertEquals(CpioConstants.BLOCK_SIZE, cpioConfig.getBlockSize());
		assertEquals(CpioConstants.FORMAT_NEW, cpioConfig.getFormat());

		ArchiveOutputStream stream = streamFunction.apply(cpioConfig);
		assertEquals(CpioArchiveOutputStream.class, stream.getClass());
	}

	@Test
	public void testDump() throws Exception {
		prepare(DUMP);

		assertNull(configFunction);
		assertNull(streamFunction);
	}

	@Test
	public void testTar() throws Exception {
		prepare(TAR);

		ArchiveOutputStreamConfig config = configFunction.apply(outputStream);
		assertEquals(ArchiveOutputStreamConfigTarImpl.class, config.getClass());
		ArchiveOutputStreamConfigTarImpl tarConfig = (ArchiveOutputStreamConfigTarImpl) config;
		assertNull(tarConfig.getEncoding());
		assertFalse(tarConfig.addPaxHeadersForNonAsciiNames());
		assertEquals(TarArchiveOutputStream.BIGNUMBER_ERROR, tarConfig.getBigNumberMode());
		assertEquals(TarConstants.DEFAULT_BLKSIZE, tarConfig.getBlockSize());
		assertEquals(TarArchiveOutputStream.LONGFILE_ERROR, tarConfig.getLongFileMode());
		assertEquals(TarConstants.DEFAULT_RCDSIZE, tarConfig.getRecordSize());

		ArchiveOutputStream stream = streamFunction.apply(tarConfig);
		assertEquals(TarArchiveOutputStream.class, stream.getClass());
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
