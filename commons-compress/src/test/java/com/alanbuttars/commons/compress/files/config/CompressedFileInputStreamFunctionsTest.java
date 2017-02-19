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
package com.alanbuttars.commons.compress.files.config;

import static com.alanbuttars.commons.compress.files.util.CompressedFiles.BZIP2;
import static com.alanbuttars.commons.compress.files.util.CompressedFiles.DEFLATE;
import static com.alanbuttars.commons.compress.files.util.CompressedFiles.GZIP;
import static com.alanbuttars.commons.compress.files.util.CompressedFiles.LZMA;
import static com.alanbuttars.commons.compress.files.util.CompressedFiles.PACK200;
import static com.alanbuttars.commons.compress.files.util.CompressedFiles.SNAPPY;
import static com.alanbuttars.commons.compress.files.util.CompressedFiles.XZ;
import static com.alanbuttars.commons.compress.files.util.CompressedFiles.Z;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.ByteArrayInputStream;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.compress.compressors.deflate.DeflateCompressorInputStream;
import org.apache.commons.compress.compressors.pack200.Pack200Strategy;
import org.apache.commons.compress.compressors.snappy.FramedSnappyDialect;
import org.apache.commons.compress.compressors.snappy.SnappyCompressorInputStream;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.modules.junit4.PowerMockRunner;

import com.alanbuttars.commons.compress.files.config.input.CompressedFileInputStreamConfig;
import com.alanbuttars.commons.compress.files.config.input.CompressedFileInputStreamConfigBzip2Impl;
import com.alanbuttars.commons.compress.files.config.input.CompressedFileInputStreamConfigDeflateImpl;
import com.alanbuttars.commons.compress.files.config.input.CompressedFileInputStreamConfigGzipImpl;
import com.alanbuttars.commons.compress.files.config.input.CompressedFileInputStreamConfigPack200Impl;
import com.alanbuttars.commons.compress.files.config.input.CompressedFileInputStreamConfigSnappyImpl;
import com.alanbuttars.commons.compress.files.config.input.CompressedFileInputStreamConfigSnappyImpl.Format;
import com.alanbuttars.commons.compress.files.config.input.CompressedFileInputStreamConfigXzImpl;
import com.alanbuttars.commons.compress.files.input.CompressedFileInputStream;
import com.alanbuttars.commons.compress.files.input.CompressedFileInputStreamImpl;
import com.alanbuttars.commons.util.functions.Function;

/**
 * Test class for {@link CompressedFileInputStreamFunctions}.
 * 
 * @author Alan Buttars
 *
 */
@RunWith(PowerMockRunner.class)
public class CompressedFileInputStreamFunctionsTest {

	private InputStream inputStream;
	private Function<InputStream, CompressedFileInputStreamConfig> configFunction;
	private Function<CompressedFileInputStreamConfig, CompressedFileInputStream> streamFunction;

	@Before
	public void setup() {
		inputStream = new ByteArrayInputStream("a\nb\nc".getBytes());
	}

	private void prepare(String archiveType) {
		configFunction = CompressedFileInputStreamFunctions.defaultConfigFunctions().get(archiveType);
		streamFunction = CompressedFileInputStreamFunctions.defaultStreamFunctions().get(archiveType);
	}

	@Test
	public void testBzip2() throws Exception {
		prepare(BZIP2);

		CompressedFileInputStreamConfig config = configFunction.apply(inputStream);
		assertEquals(CompressedFileInputStreamConfigBzip2Impl.class, config.getClass());
		CompressedFileInputStreamConfigBzip2Impl bzip2Config = (CompressedFileInputStreamConfigBzip2Impl) config;
		assertNotNull(bzip2Config.getInputStream());
		assertFalse(bzip2Config.decompressConcatenated());

		try {
			streamFunction.apply(config);
			fail();
		}
		catch (RuntimeException e) {
			assertTrue(e.getCause() instanceof IOException);
		}
	}

	@Test
	public void testDeflate() throws Exception {
		prepare(DEFLATE);

		CompressedFileInputStreamConfig config = configFunction.apply(inputStream);
		assertEquals(CompressedFileInputStreamConfigDeflateImpl.class, config.getClass());
		CompressedFileInputStreamConfigDeflateImpl deflateConfig = (CompressedFileInputStreamConfigDeflateImpl) config;
		assertNotNull(deflateConfig.getInputStream());
		assertNotNull(deflateConfig.getParameters());

		CompressedFileInputStream stream = streamFunction.apply(config);
		assertEquals(CompressedFileInputStreamImpl.class, stream.getClass());
		assertEquals(DeflateCompressorInputStream.class, stream.getStream().getClass());
	}

	@Test
	public void testGzip() throws Exception {
		prepare(GZIP);

		CompressedFileInputStreamConfig config = configFunction.apply(inputStream);
		assertEquals(CompressedFileInputStreamConfigGzipImpl.class, config.getClass());
		CompressedFileInputStreamConfigGzipImpl gzipConfig = (CompressedFileInputStreamConfigGzipImpl) config;
		assertNotNull(gzipConfig.getInputStream());
		assertFalse(gzipConfig.decompressConcatenated());

		try {
			streamFunction.apply(config);
			fail();
		}
		catch (RuntimeException e) {
			assertTrue(e.getCause() instanceof IOException);
		}
	}

	@Test
	public void testLzma() throws Exception {
		prepare(LZMA);

		CompressedFileInputStreamConfig config = configFunction.apply(inputStream);
		assertEquals(CompressedFileInputStreamConfig.class, config.getClass());
		assertNotNull(config.getInputStream());

		try {
			streamFunction.apply(config);
			fail();
		}
		catch (RuntimeException e) {
			assertTrue(e.getCause() instanceof IOException);
		}
	}

	@Test
	public void testPack200() throws Exception {
		prepare(PACK200);

		CompressedFileInputStreamConfig config = configFunction.apply(inputStream);
		assertEquals(CompressedFileInputStreamConfigPack200Impl.class, config.getClass());
		CompressedFileInputStreamConfigPack200Impl packConfig = (CompressedFileInputStreamConfigPack200Impl) config;
		assertNotNull(packConfig.getInputStream());
		assertEquals(Pack200Strategy.IN_MEMORY, packConfig.getMode());
		assertNull(packConfig.getProperties());

		try {
			streamFunction.apply(config);
			fail();
		}
		catch (RuntimeException e) {
			assertTrue(e.getCause() instanceof IOException);
		}
	}

	@Test
	public void testSnappyStandard() throws Exception {
		prepare(SNAPPY);

		CompressedFileInputStreamConfig config = configFunction.apply(inputStream);
		assertEquals(CompressedFileInputStreamConfigSnappyImpl.class, config.getClass());
		CompressedFileInputStreamConfigSnappyImpl snappyConfig = (CompressedFileInputStreamConfigSnappyImpl) config;
		assertNotNull(snappyConfig.getInputStream());
		assertEquals(Format.STANDARD, snappyConfig.getFormat());
		assertEquals(FramedSnappyDialect.STANDARD, snappyConfig.getDialect());
		assertEquals(SnappyCompressorInputStream.DEFAULT_BLOCK_SIZE, snappyConfig.getBlockSize());

		CompressedFileInputStream stream = streamFunction.apply(config);
		assertEquals(CompressedFileInputStreamImpl.class, stream.getClass());
		assertEquals(SnappyCompressorInputStream.class, stream.getStream().getClass());
	}

	@Test
	public void testSnappyFramed() throws Exception {
		prepare(SNAPPY);

		CompressedFileInputStreamConfig config = configFunction.apply(inputStream);
		assertEquals(CompressedFileInputStreamConfigSnappyImpl.class, config.getClass());
		CompressedFileInputStreamConfigSnappyImpl snappyConfig = (CompressedFileInputStreamConfigSnappyImpl) config;
		snappyConfig.setFormat(Format.FRAMED);
		assertNotNull(snappyConfig.getInputStream());
		assertEquals(Format.FRAMED, snappyConfig.getFormat());
		assertEquals(FramedSnappyDialect.STANDARD, snappyConfig.getDialect());
		assertEquals(SnappyCompressorInputStream.DEFAULT_BLOCK_SIZE, snappyConfig.getBlockSize());

		try {
			streamFunction.apply(config);
			fail();
		}
		catch (RuntimeException e) {
			assertTrue(e.getCause() instanceof IOException);
		}
	}

	@Test
	public void testXz() throws Exception {
		prepare(XZ);

		CompressedFileInputStreamConfig config = configFunction.apply(inputStream);
		assertEquals(CompressedFileInputStreamConfigXzImpl.class, config.getClass());
		CompressedFileInputStreamConfigXzImpl xzConfig = (CompressedFileInputStreamConfigXzImpl) config;
		assertNotNull(xzConfig.getInputStream());
		assertFalse(xzConfig.decompressConcatenated());

		try {
			streamFunction.apply(config);
			fail();
		}
		catch (RuntimeException e) {
			assertTrue(e.getCause() instanceof EOFException);
		}
	}

	@Test
	public void testZ() throws Exception {
		prepare(Z);

		CompressedFileInputStreamConfig config = configFunction.apply(inputStream);
		assertEquals(CompressedFileInputStreamConfig.class, config.getClass());
		assertNotNull(config.getInputStream());

		try {
			streamFunction.apply(config);
			fail();
		}
		catch (RuntimeException e) {
			assertTrue(e.getCause() instanceof IOException);
		}
	}

}
