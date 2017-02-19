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
import static com.alanbuttars.commons.compress.files.util.CompressedFiles.XZ;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;

import org.apache.commons.compress.compressors.bzip2.BZip2CompressorOutputStream;
import org.apache.commons.compress.compressors.deflate.DeflateCompressorOutputStream;
import org.apache.commons.compress.compressors.gzip.GzipCompressorOutputStream;
import org.apache.commons.compress.compressors.lzma.LZMACompressorOutputStream;
import org.apache.commons.compress.compressors.pack200.Pack200CompressorOutputStream;
import org.apache.commons.compress.compressors.pack200.Pack200Strategy;
import org.apache.commons.compress.compressors.xz.XZCompressorOutputStream;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.modules.junit4.PowerMockRunner;
import org.tukaani.xz.LZMA2Options;

import com.alanbuttars.commons.compress.files.config.output.CompressedFileOutputStreamConfig;
import com.alanbuttars.commons.compress.files.config.output.CompressedFileOutputStreamConfigBzip2Impl;
import com.alanbuttars.commons.compress.files.config.output.CompressedFileOutputStreamConfigDeflateImpl;
import com.alanbuttars.commons.compress.files.config.output.CompressedFileOutputStreamConfigGzipImpl;
import com.alanbuttars.commons.compress.files.config.output.CompressedFileOutputStreamConfigPack200Impl;
import com.alanbuttars.commons.compress.files.config.output.CompressedFileOutputStreamConfigXzImpl;
import com.alanbuttars.commons.compress.files.output.CompressedFileOutputStream;
import com.alanbuttars.commons.compress.files.output.CompressedFileOutputStreamImpl;
import com.alanbuttars.commons.util.functions.Function;

/**
 * Test class for {@link CompressedFileOutputStreamFunctions}.
 * 
 * @author Alan Buttars
 *
 */
@RunWith(PowerMockRunner.class)
public class CompressedFileOutputStreamFunctionsTest {

	private OutputStream outputStream;
	private Function<OutputStream, CompressedFileOutputStreamConfig> configFunction;
	private Function<CompressedFileOutputStreamConfig, CompressedFileOutputStream> streamFunction;

	@Before
	public void setup() {
		outputStream = new ByteArrayOutputStream();
	}

	private void prepare(String archiveType) {
		configFunction = CompressedFileOutputStreamFunctions.defaultConfigFunctions().get(archiveType);
		streamFunction = CompressedFileOutputStreamFunctions.defaultStreamFunctions().get(archiveType);
	}

	@Test
	public void testBzip2() throws Exception {
		prepare(BZIP2);

		CompressedFileOutputStreamConfig config = configFunction.apply(outputStream);
		assertEquals(CompressedFileOutputStreamConfigBzip2Impl.class, config.getClass());
		CompressedFileOutputStreamConfigBzip2Impl bzip2Config = (CompressedFileOutputStreamConfigBzip2Impl) config;
		assertNotNull(bzip2Config.getOutputStream());
		assertEquals(BZip2CompressorOutputStream.MAX_BLOCKSIZE, bzip2Config.getBlockSize());

		CompressedFileOutputStream stream = streamFunction.apply(bzip2Config);
		assertEquals(CompressedFileOutputStreamImpl.class, stream.getClass());
		assertEquals(BZip2CompressorOutputStream.class, stream.getStream().getClass());
	}

	@Test
	public void testDeflate() throws Exception {
		prepare(DEFLATE);

		CompressedFileOutputStreamConfig config = configFunction.apply(outputStream);
		assertEquals(CompressedFileOutputStreamConfigDeflateImpl.class, config.getClass());
		CompressedFileOutputStreamConfigDeflateImpl deflateConfig = (CompressedFileOutputStreamConfigDeflateImpl) config;
		assertNotNull(deflateConfig.getOutputStream());
		assertNotNull(deflateConfig.getParameters());

		CompressedFileOutputStream stream = streamFunction.apply(deflateConfig);
		assertEquals(CompressedFileOutputStreamImpl.class, stream.getClass());
		assertEquals(DeflateCompressorOutputStream.class, stream.getStream().getClass());
	}

	@Test
	public void testGzip() throws Exception {
		prepare(GZIP);

		CompressedFileOutputStreamConfig config = configFunction.apply(outputStream);
		assertEquals(CompressedFileOutputStreamConfigGzipImpl.class, config.getClass());
		CompressedFileOutputStreamConfigGzipImpl gzipConfig = (CompressedFileOutputStreamConfigGzipImpl) config;
		assertNotNull(gzipConfig.getOutputStream());
		assertNotNull(gzipConfig.getParameters());

		CompressedFileOutputStream stream = streamFunction.apply(gzipConfig);
		assertEquals(CompressedFileOutputStreamImpl.class, stream.getClass());
		assertEquals(GzipCompressorOutputStream.class, stream.getStream().getClass());
	}

	@Test
	public void testLzma() throws Exception {
		prepare(LZMA);

		CompressedFileOutputStreamConfig config = configFunction.apply(outputStream);
		assertEquals(CompressedFileOutputStreamConfig.class, config.getClass());
		assertNotNull(config.getOutputStream());

		CompressedFileOutputStream stream = streamFunction.apply(config);
		assertEquals(CompressedFileOutputStreamImpl.class, stream.getClass());
		assertEquals(LZMACompressorOutputStream.class, stream.getStream().getClass());
	}

	@Test
	public void testPack200() throws Exception {
		prepare(PACK200);

		CompressedFileOutputStreamConfig config = configFunction.apply(outputStream);
		assertEquals(CompressedFileOutputStreamConfigPack200Impl.class, config.getClass());
		CompressedFileOutputStreamConfigPack200Impl packConfig = (CompressedFileOutputStreamConfigPack200Impl) config;
		assertNotNull(packConfig.getOutputStream());
		assertEquals(Pack200Strategy.IN_MEMORY, packConfig.getMode());
		assertNull(packConfig.getProperties());

		CompressedFileOutputStream stream = streamFunction.apply(packConfig);
		assertEquals(CompressedFileOutputStreamImpl.class, stream.getClass());
		assertEquals(Pack200CompressorOutputStream.class, stream.getStream().getClass());
	}

	@Test
	public void testXz() throws Exception {
		prepare(XZ);

		CompressedFileOutputStreamConfig config = configFunction.apply(outputStream);
		assertEquals(CompressedFileOutputStreamConfigXzImpl.class, config.getClass());
		CompressedFileOutputStreamConfigXzImpl xzConfig = (CompressedFileOutputStreamConfigXzImpl) config;
		assertNotNull(xzConfig.getOutputStream());
		assertEquals(LZMA2Options.PRESET_DEFAULT, xzConfig.getPreset());

		CompressedFileOutputStream stream = streamFunction.apply(xzConfig);
		assertEquals(CompressedFileOutputStreamImpl.class, stream.getClass());
		assertEquals(XZCompressorOutputStream.class, stream.getStream().getClass());
	}
}
