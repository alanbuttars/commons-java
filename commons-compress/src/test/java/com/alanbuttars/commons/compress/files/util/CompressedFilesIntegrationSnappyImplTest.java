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

import static com.alanbuttars.commons.compress.files.util.CompressedFiles.SNAPPY;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.io.IOException;
import java.io.InputStream;

import org.junit.Test;

import com.alanbuttars.commons.compress.files.config.input.CompressedFileInputStreamConfig;
import com.alanbuttars.commons.compress.files.config.input.CompressedFileInputStreamConfigSnappyImpl;
import com.alanbuttars.commons.compress.files.config.input.CompressedFileInputStreamConfigSnappyImpl.Format;
import com.alanbuttars.commons.util.functions.Function;

/**
 * Integration test class for {@link CompressedFiles} for {@link CompressedFiles#SNAPPY} files.
 * 
 * @author Alan Buttars
 *
 */
public class CompressedFilesIntegrationSnappyImplTest extends CompressedFilesIntegrationAbstractTest {

	@Test
	public void testDecompress() throws IOException {
		testDecompress(SNAPPY, inputStreamConfigFunction());
	}

	@Test
	public void testCompress() throws IOException {
		try {
			testCompress(SNAPPY, inputStreamConfigFunction(), null);
			fail();
		}
		catch (Exception e) {
			assertEquals("File type snappy is not recognized", e.getMessage());
		}
	}

	private Function<InputStream, CompressedFileInputStreamConfig> inputStreamConfigFunction() {
		return new Function<InputStream, CompressedFileInputStreamConfig>() {

			@Override
			public CompressedFileInputStreamConfig apply(InputStream input) {
				CompressedFileInputStreamConfigSnappyImpl config = new CompressedFileInputStreamConfigSnappyImpl(input);
				config.setFormat(Format.FRAMED);
				return config;
			}

		};
	}

}
