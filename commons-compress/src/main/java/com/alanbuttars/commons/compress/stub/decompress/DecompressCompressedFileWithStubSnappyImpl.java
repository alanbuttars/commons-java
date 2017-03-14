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
package com.alanbuttars.commons.compress.stub.decompress;

import static com.alanbuttars.commons.compress.files.util.CompressedFiles.SNAPPY;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.compress.compressors.snappy.SnappyCompressorInputStream;

import com.alanbuttars.commons.compress.files.input.CompressedFileInputStream;
import com.alanbuttars.commons.compress.files.input.CompressedFileInputStreamImpl;
import com.alanbuttars.commons.compress.files.util.CompressedFiles;
import com.alanbuttars.commons.util.annotations.VisibleForTesting;
import com.alanbuttars.commons.util.functions.Function;

/**
 * Extension of {@link DecompressCompressedFileWithStub} for {@link CompressedFiles#SNAPPY} files. For details on the file format, see
 * <a href="https://en.wikipedia.org/wiki/Snappy_(compression)">https://en.wikipedia.org/wiki/Snappy_(compression)</a>.
 * 
 * @author Alan Buttars
 *
 */
public class DecompressCompressedFileWithStubSnappyImpl extends DecompressCompressedFileWithStub {

	private int blockSize;

	DecompressCompressedFileWithStubSnappyImpl(File source) {
		super(source, SNAPPY);
		this.blockSize = SnappyCompressorInputStream.DEFAULT_BLOCK_SIZE;
	}

	public DecompressCompressedFileWithStubSnappyImpl andBlockSize(int blockSize) {
		this.blockSize = blockSize;
		return this;
	}

	@Override
	protected Function<InputStream, CompressedFileInputStream> decompressionFunction() {
		return new Function<InputStream, CompressedFileInputStream>() {

			@Override
			public CompressedFileInputStream apply(InputStream inputStream) {
				try {
					return createCompressedFileInputStream(inputStream, blockSize);
				}
				catch (IOException e) {
					throw new RuntimeException(e);
				}
			}

		};
	}

	@VisibleForTesting
	protected CompressedFileInputStream createCompressedFileInputStream(InputStream inputStream, int blockSize) throws IOException {
		return new CompressedFileInputStreamImpl(new SnappyCompressorInputStream(inputStream, blockSize));
	}

}
