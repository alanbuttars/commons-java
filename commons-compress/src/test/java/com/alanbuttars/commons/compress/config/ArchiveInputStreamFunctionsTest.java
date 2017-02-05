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
import static com.alanbuttars.commons.compress.util.Archives.JAR;
import static com.alanbuttars.commons.compress.util.Archives.TAR;
import static com.alanbuttars.commons.compress.util.Archives.ZIP;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import org.apache.commons.compress.archivers.ArchiveException;
import org.apache.commons.compress.archivers.ArchiveInputStream;
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

import com.alanbuttars.commons.compress.config.input.ArchiveInputStreamConfig;
import com.alanbuttars.commons.compress.config.input.ArchiveInputStreamConfigCpioImpl;
import com.alanbuttars.commons.compress.config.input.ArchiveInputStreamConfigDumpImpl;
import com.alanbuttars.commons.compress.config.input.ArchiveInputStreamConfigJarImpl;
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
	public void testAr() throws Exception {
		prepare(AR);

		ArchiveInputStreamConfig config = configFunction.apply(inputStream);
		assertEquals(ArchiveInputStreamConfig.class, config.getClass());

		ArchiveInputStream stream = streamFunction.apply(config);
		assertEquals(ArArchiveInputStream.class, stream.getClass());
	}

	@Test
	public void testCpio() throws Exception {
		prepare(CPIO);

		ArchiveInputStreamConfig config = configFunction.apply(inputStream);
		assertEquals(ArchiveInputStreamConfigCpioImpl.class, config.getClass());
		ArchiveInputStreamConfigCpioImpl cpioConfig = (ArchiveInputStreamConfigCpioImpl) config;
		assertEquals(CharsetNames.US_ASCII, cpioConfig.getEncoding());
		assertEquals(CpioConstants.BLOCK_SIZE, cpioConfig.getBlockSize());

		ArchiveInputStream stream = streamFunction.apply(config);
		assertEquals(CpioArchiveInputStream.class, stream.getClass());
	}

	@Test
	public void testDump() throws Exception {
		prepare(DUMP);

		ArchiveInputStreamConfig config = configFunction.apply(inputStream);
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

		ArchiveInputStreamConfig config = configFunction.apply(inputStream);
		assertEquals(ArchiveInputStreamConfigJarImpl.class, config.getClass());
		ArchiveInputStreamConfigJarImpl jarConfig = (ArchiveInputStreamConfigJarImpl) config;
		assertFalse(jarConfig.allowStoredEntriesWithDataDescriptor());
		assertEquals("UTF8", jarConfig.getEncoding());
		assertTrue(jarConfig.useUnicodeExtraFields());
		assertEquals(inputStream, jarConfig.getInputStream());

		ArchiveInputStream stream = streamFunction.apply(jarConfig);
		assertEquals(JarArchiveInputStream.class, stream.getClass());
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
