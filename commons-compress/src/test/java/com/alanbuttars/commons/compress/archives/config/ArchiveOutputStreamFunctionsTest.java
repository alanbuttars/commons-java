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
package com.alanbuttars.commons.compress.archives.config;

import static com.alanbuttars.commons.compress.archives.util.Archives.AR;
import static com.alanbuttars.commons.compress.archives.util.Archives.ARJ;
import static com.alanbuttars.commons.compress.archives.util.Archives.CPIO;
import static com.alanbuttars.commons.compress.archives.util.Archives.DUMP;
import static com.alanbuttars.commons.compress.archives.util.Archives.JAR;
import static com.alanbuttars.commons.compress.archives.util.Archives.SEVENZ;
import static com.alanbuttars.commons.compress.archives.util.Archives.TAR;
import static com.alanbuttars.commons.compress.archives.util.Archives.ZIP;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.util.zip.ZipEntry;

import org.apache.commons.compress.archivers.ar.ArArchiveOutputStream;
import org.apache.commons.compress.archivers.cpio.CpioArchiveOutputStream;
import org.apache.commons.compress.archivers.cpio.CpioConstants;
import org.apache.commons.compress.archivers.jar.JarArchiveOutputStream;
import org.apache.commons.compress.archivers.sevenz.SevenZMethod;
import org.apache.commons.compress.archivers.sevenz.SevenZOutputFile;
import org.apache.commons.compress.archivers.tar.TarArchiveOutputStream;
import org.apache.commons.compress.archivers.tar.TarConstants;
import org.apache.commons.compress.archivers.zip.Zip64Mode;
import org.apache.commons.compress.archivers.zip.ZipArchiveOutputStream;
import org.apache.commons.compress.archivers.zip.ZipArchiveOutputStream.UnicodeExtraFieldPolicy;
import org.apache.commons.compress.utils.CharsetNames;
import org.apache.commons.compress.utils.Lists;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import com.alanbuttars.commons.compress.archives.config.output.ArchiveOutputStreamConfig;
import com.alanbuttars.commons.compress.archives.config.output.ArchiveOutputStreamConfigArImpl;
import com.alanbuttars.commons.compress.archives.config.output.ArchiveOutputStreamConfigCpioImpl;
import com.alanbuttars.commons.compress.archives.config.output.ArchiveOutputStreamConfigJarImpl;
import com.alanbuttars.commons.compress.archives.config.output.ArchiveOutputStreamConfigSevenZImpl;
import com.alanbuttars.commons.compress.archives.config.output.ArchiveOutputStreamConfigTarImpl;
import com.alanbuttars.commons.compress.archives.config.output.ArchiveOutputStreamConfigZipImpl;
import com.alanbuttars.commons.compress.archives.output.ArchiveOutputStream;
import com.alanbuttars.commons.compress.archives.output.ArchiveOutputStreamImpl;
import com.alanbuttars.commons.compress.archives.output.ArchiveOutputStreamSevenZImpl;
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

	private File file;
	private Function<File, ArchiveOutputStreamConfig> configFunction;
	private Function<ArchiveOutputStreamConfig, ArchiveOutputStream> streamFunction;

	@Before
	public void setup() throws IOException {
		file = File.createTempFile(getClass().getName(), ".tmp");
	}

	private void prepare(String archiveType) {
		configFunction = ArchiveOutputStreamFunctions.defaultConfigFunctions().get(archiveType);
		streamFunction = ArchiveOutputStreamFunctions.defaultStreamFunctions().get(archiveType);
	}

	@Test
	public void testAr() throws Exception {
		prepare(AR);

		ArchiveOutputStreamConfig config = configFunction.apply(file);
		assertEquals(ArchiveOutputStreamConfigArImpl.class, config.getClass());
		ArchiveOutputStreamConfigArImpl arConfig = (ArchiveOutputStreamConfigArImpl) config;
		assertEquals(TarArchiveOutputStream.LONGFILE_ERROR, arConfig.getLongFileMode());

		ArchiveOutputStream stream = streamFunction.apply(arConfig);
		assertEquals(ArchiveOutputStreamImpl.class, stream.getClass());
		assertEquals(ArArchiveOutputStream.class, stream.getStream().getClass());
	}

	@Test
	public void testArj() throws Exception {
		prepare(ARJ);

		assertNull(configFunction);
		assertNull(streamFunction);
	}

	@Test
	public void testCpio() throws Exception {
		prepare(CPIO);

		ArchiveOutputStreamConfig config = configFunction.apply(file);
		assertEquals(ArchiveOutputStreamConfigCpioImpl.class, config.getClass());
		ArchiveOutputStreamConfigCpioImpl cpioConfig = (ArchiveOutputStreamConfigCpioImpl) config;
		assertEquals(CharsetNames.US_ASCII, cpioConfig.getEncoding());
		assertEquals(CpioConstants.BLOCK_SIZE, cpioConfig.getBlockSize());
		assertEquals(CpioConstants.FORMAT_NEW, cpioConfig.getFormat());

		ArchiveOutputStream stream = streamFunction.apply(cpioConfig);
		assertEquals(ArchiveOutputStreamImpl.class, stream.getClass());
		assertEquals(CpioArchiveOutputStream.class, stream.getStream().getClass());
	}

	@Test
	public void testDump() throws Exception {
		prepare(DUMP);

		assertNull(configFunction);
		assertNull(streamFunction);
	}

	@Test
	public void testJar() throws Exception {
		prepare(JAR);

		ArchiveOutputStreamConfig config = configFunction.apply(file);
		assertEquals(ArchiveOutputStreamConfigJarImpl.class, config.getClass());
		ArchiveOutputStreamConfigJarImpl jarConfig = (ArchiveOutputStreamConfigJarImpl) config;
		assertEquals("", jarConfig.getComment());
		assertFalse(jarConfig.fallbackToUTF8());
		assertEquals(ZipArchiveOutputStream.DEFAULT_COMPRESSION, jarConfig.getLevel());
		assertEquals(ZipEntry.DEFLATED, jarConfig.getMethod());
		assertNotNull(jarConfig.getOutputStream());
		assertEquals(UnicodeExtraFieldPolicy.NEVER, jarConfig.getUnicodeExtraFieldPolicy());
		assertTrue(jarConfig.useLanguageEncoding());
		assertEquals(Zip64Mode.AsNeeded, jarConfig.getZip64Mode());

		ArchiveOutputStream stream = streamFunction.apply(jarConfig);
		assertEquals(ArchiveOutputStreamImpl.class, stream.getClass());
		assertEquals(JarArchiveOutputStream.class, stream.getStream().getClass());
	}

	@Test
	public void testSevenZ() throws Exception {
		prepare(SEVENZ);

		ArchiveOutputStreamConfig config = configFunction.apply(file);
		assertEquals(ArchiveOutputStreamConfigSevenZImpl.class, config.getClass());
		ArchiveOutputStreamConfigSevenZImpl sevenZConfig = (ArchiveOutputStreamConfigSevenZImpl) config;
		assertNotNull(sevenZConfig.getFile());
		assertEquals(1, Lists.newArrayList(sevenZConfig.getContentMethods().iterator()).size());
		assertEquals(SevenZMethod.LZMA2, sevenZConfig.getContentMethods().iterator().next().getMethod());
		assertNotNull(sevenZConfig.getOutputStream());

		ArchiveOutputStream stream = streamFunction.apply(sevenZConfig);
		assertEquals(ArchiveOutputStreamSevenZImpl.class, stream.getClass());
		assertEquals(SevenZOutputFile.class, stream.getStream().getClass());
	}

	@Test
	public void testTar() throws Exception {
		prepare(TAR);

		ArchiveOutputStreamConfig config = configFunction.apply(file);
		assertEquals(ArchiveOutputStreamConfigTarImpl.class, config.getClass());
		ArchiveOutputStreamConfigTarImpl tarConfig = (ArchiveOutputStreamConfigTarImpl) config;
		assertNull(tarConfig.getEncoding());
		assertFalse(tarConfig.addPaxHeadersForNonAsciiNames());
		assertEquals(TarArchiveOutputStream.BIGNUMBER_ERROR, tarConfig.getBigNumberMode());
		assertEquals(TarConstants.DEFAULT_BLKSIZE, tarConfig.getBlockSize());
		assertEquals(TarArchiveOutputStream.LONGFILE_ERROR, tarConfig.getLongFileMode());
		assertEquals(TarConstants.DEFAULT_RCDSIZE, tarConfig.getRecordSize());

		ArchiveOutputStream stream = streamFunction.apply(tarConfig);
		assertEquals(ArchiveOutputStreamImpl.class, stream.getClass());
		assertEquals(TarArchiveOutputStream.class, stream.getStream().getClass());
	}

	@Test
	public void testZip() throws Exception {
		prepare(ZIP);

		ArchiveOutputStreamConfig config = configFunction.apply(file);
		assertEquals(ArchiveOutputStreamConfigZipImpl.class, config.getClass());
		ArchiveOutputStreamConfigZipImpl zipConfig = (ArchiveOutputStreamConfigZipImpl) config;
		assertEquals("", zipConfig.getComment());
		assertFalse(zipConfig.fallbackToUTF8());
		assertEquals(ZipArchiveOutputStream.DEFAULT_COMPRESSION, zipConfig.getLevel());
		assertEquals(ZipEntry.DEFLATED, zipConfig.getMethod());
		assertNotNull(zipConfig.getOutputStream());
		assertEquals(UnicodeExtraFieldPolicy.NEVER, zipConfig.getUnicodeExtraFieldPolicy());
		assertTrue(zipConfig.useLanguageEncoding());
		assertEquals(Zip64Mode.AsNeeded, zipConfig.getZip64Mode());

		ArchiveOutputStream stream = streamFunction.apply(zipConfig);
		assertEquals(ArchiveOutputStreamImpl.class, stream.getClass());
		assertEquals(ZipArchiveOutputStream.class, stream.getStream().getClass());
	}

}
