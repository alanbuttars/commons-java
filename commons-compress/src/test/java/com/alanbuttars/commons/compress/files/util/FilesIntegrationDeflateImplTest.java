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

import static com.alanbuttars.commons.compress.files.util.Files.DEFLATE;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.junit.Test;

import com.alanbuttars.commons.compress.files.config.input.FileInputStreamConfig;
import com.alanbuttars.commons.compress.files.config.input.FileInputStreamConfigDeflateImpl;
import com.alanbuttars.commons.compress.files.config.output.FileOutputStreamConfig;
import com.alanbuttars.commons.compress.files.config.output.FileOutputStreamConfigDeflateImpl;
import com.alanbuttars.commons.util.functions.Function;

/**
 * Integration test class for {@link Files} for {@link Files#DEFLATE} files.
 * 
 * @author Alan Buttars
 *
 */
public class FilesIntegrationDeflateImplTest extends FilesIntegrationAbstractTest {

	@Test
	public void testDecompress() throws IOException {
		testDecompress(DEFLATE, inputStreamConfigFunction());
	}

	@Test
	public void testCompress() throws IOException {
		testCompress(DEFLATE, inputStreamConfigFunction(), outputStreamConfigFunction());
	}

	private Function<InputStream, FileInputStreamConfig> inputStreamConfigFunction() {
		return new Function<InputStream, FileInputStreamConfig>() {

			@Override
			public FileInputStreamConfig apply(InputStream input) {
				FileInputStreamConfigDeflateImpl config = new FileInputStreamConfigDeflateImpl(input);
				config.getParameters().setWithZlibHeader(false);
				return config;
			}

		};
	}

	private Function<OutputStream, FileOutputStreamConfig> outputStreamConfigFunction() {
		return new Function<OutputStream, FileOutputStreamConfig>() {

			@Override
			public FileOutputStreamConfig apply(OutputStream output) {
				FileOutputStreamConfigDeflateImpl config = new FileOutputStreamConfigDeflateImpl(output);
				config.getParameters().setWithZlibHeader(false);
				return config;
			}

		};
	}
}