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
import static org.junit.Assert.fail;

import java.io.EOFException;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import org.apache.commons.compress.archivers.ArchiveException;
import org.apache.commons.compress.archivers.ar.ArArchiveInputStream;
import org.apache.commons.compress.archivers.cpio.CpioArchiveInputStream;
import org.apache.commons.compress.archivers.cpio.CpioConstants;
import org.apache.commons.compress.archivers.jar.JarArchiveInputStream;
import org.apache.commons.compress.archivers.tar.TarArchiveInputStream;
import org.apache.commons.compress.archivers.tar.TarConstants;
import org.apache.commons.compress.archivers.zip.ZipArchiveInputStream;
import org.apache.commons.compress.utils.CharsetNames;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import com.alanbuttars.commons.compress.archives.config.input.ArchiveInputStreamConfig;
import com.alanbuttars.commons.compress.archives.config.input.ArchiveInputStreamConfigArjImpl;
import com.alanbuttars.commons.compress.archives.config.input.ArchiveInputStreamConfigCpioImpl;
import com.alanbuttars.commons.compress.archives.config.input.ArchiveInputStreamConfigDumpImpl;
import com.alanbuttars.commons.compress.archives.config.input.ArchiveInputStreamConfigJarImpl;
import com.alanbuttars.commons.compress.archives.config.input.ArchiveInputStreamConfigSevenZImpl;
import com.alanbuttars.commons.compress.archives.config.input.ArchiveInputStreamConfigTarImpl;
import com.alanbuttars.commons.compress.archives.config.input.ArchiveInputStreamConfigZipImpl;
import com.alanbuttars.commons.compress.archives.input.ArchiveInputStream;
import com.alanbuttars.commons.compress.archives.input.ArchiveInputStreamImpl;
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

	private File file;
	private Function<File, ArchiveInputStreamConfig> configFunction;
	private Function<ArchiveInputStreamConfig, ArchiveInputStream> streamFunction;

	@Before
	public void setup() throws IOException {
		file = File.createTempFile(getClass().getName(), ".tmp");
		Files.write(file.toPath(), "a\nb\nc".getBytes());
	}

	private void prepare(String archiveType) {
		configFunction = ArchiveInputStreamFunctions.defaultConfigFunctions().get(archiveType);
		streamFunction = ArchiveInputStreamFunctions.defaultStreamFunctions().get(archiveType);
	}

	@Test
	public void testAr() throws Exception {
		prepare(AR);

		ArchiveInputStreamConfig config = configFunction.apply(file);
		assertEquals(ArchiveInputStreamConfig.class, config.getClass());

		ArchiveInputStream stream = streamFunction.apply(config);
		assertEquals(ArchiveInputStreamImpl.class, stream.getClass());
		assertEquals(ArArchiveInputStream.class, stream.getStream().getClass());
	}

	@Test
	public void testArj() throws Exception {
		prepare(ARJ);

		ArchiveInputStreamConfig config = configFunction.apply(file);
		assertEquals(ArchiveInputStreamConfigArjImpl.class, config.getClass());
		ArchiveInputStreamConfigArjImpl arjConfig = (ArchiveInputStreamConfigArjImpl) config;
		assertEquals("CP437", arjConfig.getEncoding());

		try {
			streamFunction.apply(config);
			fail();
		}
		catch (RuntimeException e) {
			assertTrue(e.getCause() instanceof ArchiveException);
		}
	}

	@Test
	public void testCpio() throws Exception {
		prepare(CPIO);

		ArchiveInputStreamConfig config = configFunction.apply(file);
		assertEquals(ArchiveInputStreamConfigCpioImpl.class, config.getClass());
		ArchiveInputStreamConfigCpioImpl cpioConfig = (ArchiveInputStreamConfigCpioImpl) config;
		assertEquals(CharsetNames.US_ASCII, cpioConfig.getEncoding());
		assertEquals(CpioConstants.BLOCK_SIZE, cpioConfig.getBlockSize());

		ArchiveInputStream stream = streamFunction.apply(config);
		assertEquals(ArchiveInputStreamImpl.class, stream.getClass());
		assertEquals(CpioArchiveInputStream.class, stream.getStream().getClass());
	}

	@Test
	public void testDump() throws Exception {
		prepare(DUMP);

		ArchiveInputStreamConfig config = configFunction.apply(file);
		assertEquals(ArchiveInputStreamConfigDumpImpl.class, config.getClass());
		ArchiveInputStreamConfigDumpImpl dumpConfig = (ArchiveInputStreamConfigDumpImpl) config;
		assertEquals("UTF8", dumpConfig.getEncoding());

		try {
			streamFunction.apply(config);
			fail();
		}
		catch (RuntimeException e) {
			assertTrue(e.getCause() instanceof ArchiveException);
		}
	}

	@Test
	public void testJar() throws Exception {
		prepare(JAR);

		ArchiveInputStreamConfig config = configFunction.apply(file);
		assertEquals(ArchiveInputStreamConfigJarImpl.class, config.getClass());
		ArchiveInputStreamConfigJarImpl jarConfig = (ArchiveInputStreamConfigJarImpl) config;
		assertFalse(jarConfig.allowStoredEntriesWithDataDescriptor());
		assertEquals("UTF8", jarConfig.getEncoding());
		assertTrue(jarConfig.useUnicodeExtraFields());
		assertNotNull(jarConfig.getInputStream());

		ArchiveInputStream stream = streamFunction.apply(jarConfig);
		assertEquals(ArchiveInputStreamImpl.class, stream.getClass());
		assertEquals(JarArchiveInputStream.class, stream.getStream().getClass());
	}

	@Test
	public void testSevenZ() throws Exception {
		prepare(SEVENZ);

		ArchiveInputStreamConfig config = configFunction.apply(file);
		assertEquals(ArchiveInputStreamConfigSevenZImpl.class, config.getClass());
		ArchiveInputStreamConfigSevenZImpl sevenZConfig = (ArchiveInputStreamConfigSevenZImpl) config;
		assertNotNull(sevenZConfig.getFile());
		assertNull(sevenZConfig.getPassword());
		assertNotNull(sevenZConfig.getInputStream());

		try {
			streamFunction.apply(config);
			fail();
		}
		catch (RuntimeException e) {
			assertTrue(e.getCause() instanceof EOFException);
		}
	}

	@Test
	public void testTar() throws Exception {
		prepare(TAR);

		ArchiveInputStreamConfig config = configFunction.apply(file);
		assertEquals(ArchiveInputStreamConfigTarImpl.class, config.getClass());
		ArchiveInputStreamConfigTarImpl tarConfig = (ArchiveInputStreamConfigTarImpl) config;
		assertNull(tarConfig.getEncoding());
		assertEquals(TarConstants.DEFAULT_BLKSIZE, tarConfig.getBlockSize());
		assertEquals(TarConstants.DEFAULT_RCDSIZE, tarConfig.getRecordSize());

		ArchiveInputStream stream = streamFunction.apply(tarConfig);
		assertEquals(ArchiveInputStreamImpl.class, stream.getClass());
		assertEquals(TarArchiveInputStream.class, stream.getStream().getClass());
	}

	@Test
	public void testZip() throws Exception {
		prepare(ZIP);

		ArchiveInputStreamConfig config = configFunction.apply(file);
		assertEquals(ArchiveInputStreamConfigZipImpl.class, config.getClass());
		ArchiveInputStreamConfigZipImpl zipConfig = (ArchiveInputStreamConfigZipImpl) config;
		assertFalse(zipConfig.allowStoredEntriesWithDataDescriptor());
		assertEquals("UTF8", zipConfig.getEncoding());
		assertTrue(zipConfig.useUnicodeExtraFields());
		assertNotNull(zipConfig.getInputStream());

		ArchiveInputStream stream = streamFunction.apply(zipConfig);
		assertEquals(ArchiveInputStreamImpl.class, stream.getClass());
		assertEquals(ZipArchiveInputStream.class, stream.getStream().getClass());
	}

}
