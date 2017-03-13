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

import static com.alanbuttars.commons.compress.files.util.CompressedFiles.BZIP2;

import java.io.File;
import java.io.IOException;

import org.junit.Test;

import com.alanbuttars.commons.compress.stub.compress.Compress;
import com.alanbuttars.commons.compress.stub.decompress.Decompress;
import com.alanbuttars.commons.compress.util.FilesFunction;

/**
 * Integration test class for {@link CompressedFiles} for {@link CompressedFiles#BZIP2} files.
 * 
 * @author Alan Buttars
 *
 */
public class CompressedFilesIntegrationBzip2ImplTest extends CompressedFilesIntegrationAbstractTest {

	@Test
	public void testDecompress() throws IOException {
		testDecompress(BZIP2, decompressFunction());
	}

	@Test
	public void testCompress() throws IOException {
		testCompress(BZIP2, compressFunction(), decompressFunction());
	}

	private FilesFunction decompressFunction() {
		return new FilesFunction() {

			@Override
			public File act(File original) throws IOException {
				return Decompress.compressedFile(original).withBzip2().toTempFile();
			}
		};
	}

	private FilesFunction compressFunction() {
		return new FilesFunction() {

			@Override
			public File act(File original) throws IOException {
				return Compress.file(original).withBzip2().toTempFile();
			}
		};
	}
}