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
package com.alanbuttars.commons.compress.files.util;

import static com.alanbuttars.commons.compress.files.util.CompressedFiles.DEFLATE;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.junit.Test;

import com.alanbuttars.commons.compress.files.config.input.CompressedFileInputStreamConfig;
import com.alanbuttars.commons.compress.files.config.input.CompressedFileInputStreamConfigDeflateImpl;
import com.alanbuttars.commons.compress.files.config.output.CompressedFileOutputStreamConfig;
import com.alanbuttars.commons.compress.files.config.output.CompressedFileOutputStreamConfigDeflateImpl;
import com.alanbuttars.commons.util.functions.Function;

/**
 * Integration test class for {@link CompressedFiles} for {@link CompressedFiles#DEFLATE} files.
 * 
 * @author Alan Buttars
 *
 */
public class CompressedFilesIntegrationDeflateImplTest extends CompressedFilesIntegrationAbstractTest {

	@Test
	public void testDecompress() throws IOException {
		testDecompress(DEFLATE, inputStreamConfigFunction());
	}

	@Test
	public void testCompress() throws IOException {
		testCompress(DEFLATE, inputStreamConfigFunction(), outputStreamConfigFunction());
	}

	private Function<InputStream, CompressedFileInputStreamConfig> inputStreamConfigFunction() {
		return new Function<InputStream, CompressedFileInputStreamConfig>() {

			@Override
			public CompressedFileInputStreamConfig apply(InputStream input) {
				CompressedFileInputStreamConfigDeflateImpl config = new CompressedFileInputStreamConfigDeflateImpl(input);
				config.getParameters().setWithZlibHeader(false);
				return config;
			}

		};
	}

	private Function<OutputStream, CompressedFileOutputStreamConfig> outputStreamConfigFunction() {
		return new Function<OutputStream, CompressedFileOutputStreamConfig>() {

			@Override
			public CompressedFileOutputStreamConfig apply(OutputStream output) {
				CompressedFileOutputStreamConfigDeflateImpl config = new CompressedFileOutputStreamConfigDeflateImpl(output);
				config.getParameters().setWithZlibHeader(false);
				return config;
			}

		};
	}
}