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

import static com.alanbuttars.commons.compress.files.util.Files.BZIP2;
import static com.alanbuttars.commons.compress.files.util.Files.DEFLATE;
import static com.alanbuttars.commons.compress.files.util.Files.GZIP;
import static com.alanbuttars.commons.compress.files.util.Files.LZMA;
import static com.alanbuttars.commons.compress.files.util.Files.PACK200;
import static com.alanbuttars.commons.compress.files.util.Files.SNAPPY;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.compress.compressors.CompressorInputStream;
import org.apache.commons.compress.compressors.deflate.DeflateCompressorInputStream;
import org.apache.commons.compress.compressors.pack200.Pack200Strategy;
import org.apache.commons.compress.compressors.snappy.FramedSnappyDialect;
import org.apache.commons.compress.compressors.snappy.SnappyCompressorInputStream;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.modules.junit4.PowerMockRunner;

import com.alanbuttars.commons.compress.files.config.input.FileInputStreamConfig;
import com.alanbuttars.commons.compress.files.config.input.FileInputStreamConfigBzip2Impl;
import com.alanbuttars.commons.compress.files.config.input.FileInputStreamConfigDeflateImpl;
import com.alanbuttars.commons.compress.files.config.input.FileInputStreamConfigGzipImpl;
import com.alanbuttars.commons.compress.files.config.input.FileInputStreamConfigPack200Impl;
import com.alanbuttars.commons.compress.files.config.input.FileInputStreamConfigSnappyImpl;
import com.alanbuttars.commons.compress.files.config.input.FileInputStreamConfigSnappyImpl.Format;
import com.alanbuttars.commons.util.functions.Function;

/**
 * Test class for {@link FileInputStreamFunctions}.
 * 
 * @author Alan Buttars
 *
 */
@RunWith(PowerMockRunner.class)
public class FileInputStreamFunctionsTest {

	private InputStream inputStream;
	private Function<InputStream, FileInputStreamConfig> configFunction;
	private Function<FileInputStreamConfig, CompressorInputStream> streamFunction;

	@Before
	public void setup() {
		inputStream = new ByteArrayInputStream("a\nb\nc".getBytes());
	}

	private void prepare(String archiveType) {
		configFunction = FileInputStreamFunctions.defaultConfigFunctions().get(archiveType);
		streamFunction = FileInputStreamFunctions.defaultStreamFunctions().get(archiveType);
	}

	@Test
	public void testBzip2() throws Exception {
		prepare(BZIP2);

		FileInputStreamConfig config = configFunction.apply(inputStream);
		assertEquals(FileInputStreamConfigBzip2Impl.class, config.getClass());
		FileInputStreamConfigBzip2Impl bzip2Config = (FileInputStreamConfigBzip2Impl) config;
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

		FileInputStreamConfig config = configFunction.apply(inputStream);
		assertEquals(FileInputStreamConfigDeflateImpl.class, config.getClass());
		FileInputStreamConfigDeflateImpl deflateConfig = (FileInputStreamConfigDeflateImpl) config;
		assertNotNull(deflateConfig.getInputStream());
		assertNotNull(deflateConfig.getParameters());

		CompressorInputStream stream = streamFunction.apply(config);
		assertEquals(DeflateCompressorInputStream.class, stream.getClass());
	}

	@Test
	public void testGzip() throws Exception {
		prepare(GZIP);

		FileInputStreamConfig config = configFunction.apply(inputStream);
		assertEquals(FileInputStreamConfigGzipImpl.class, config.getClass());
		FileInputStreamConfigGzipImpl gzipConfig = (FileInputStreamConfigGzipImpl) config;
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

		FileInputStreamConfig config = configFunction.apply(inputStream);
		assertEquals(FileInputStreamConfig.class, config.getClass());
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

		FileInputStreamConfig config = configFunction.apply(inputStream);
		assertEquals(FileInputStreamConfigPack200Impl.class, config.getClass());
		FileInputStreamConfigPack200Impl packConfig = (FileInputStreamConfigPack200Impl) config;
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

		FileInputStreamConfig config = configFunction.apply(inputStream);
		assertEquals(FileInputStreamConfigSnappyImpl.class, config.getClass());
		FileInputStreamConfigSnappyImpl snappyConfig = (FileInputStreamConfigSnappyImpl) config;
		assertNotNull(snappyConfig.getInputStream());
		assertEquals(Format.STANDARD, snappyConfig.getFormat());
		assertEquals(FramedSnappyDialect.STANDARD, snappyConfig.getDialect());
		assertEquals(SnappyCompressorInputStream.DEFAULT_BLOCK_SIZE, snappyConfig.getBlockSize());

		CompressorInputStream stream = streamFunction.apply(config);
		assertEquals(SnappyCompressorInputStream.class, stream.getClass());
	}

	@Test
	public void testSnappyFramed() throws Exception {
		prepare(SNAPPY);

		FileInputStreamConfig config = configFunction.apply(inputStream);
		assertEquals(FileInputStreamConfigSnappyImpl.class, config.getClass());
		FileInputStreamConfigSnappyImpl snappyConfig = (FileInputStreamConfigSnappyImpl) config;
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

}
