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

import org.apache.commons.compress.compressors.snappy.FramedSnappyCompressorInputStream;
import org.apache.commons.compress.compressors.snappy.FramedSnappyDialect;
import org.apache.commons.compress.compressors.snappy.SnappyCompressorInputStream;

import com.alanbuttars.commons.compress.files.input.CompressedFileInputStream;
import com.alanbuttars.commons.compress.files.input.CompressedFileInputStreamImpl;
import com.alanbuttars.commons.compress.files.util.CompressedFiles;
import com.alanbuttars.commons.util.annotations.VisibleForTesting;
import com.alanbuttars.commons.util.functions.Function;

/**
 * Extension of {@link DecompressFileWithStub} for {@link CompressedFiles#SNAPPY} files.
 * 
 * @author Alan Buttars
 *
 */
public class DecompressFileWithStubSnappyImpl extends DecompressFileWithStub {

	private FramedSnappyDialect framedDialect;
	private int blockSize;

	DecompressFileWithStubSnappyImpl(File source) {
		super(source, SNAPPY);
		this.framedDialect = null;
		this.blockSize = SnappyCompressorInputStream.DEFAULT_BLOCK_SIZE;
	}

	public DecompressFileWithStubSnappyImpl andFramedDialect(FramedSnappyDialect framedDialect) {
		this.framedDialect = framedDialect;
		return this;
	}

	public DecompressFileWithStubSnappyImpl andBlockSize(int blockSize) {
		this.blockSize = blockSize;
		return this;
	}

	@Override
	protected Function<InputStream, CompressedFileInputStream> decompressionFunction() {
		return new Function<InputStream, CompressedFileInputStream>() {

			@Override
			public CompressedFileInputStream apply(InputStream inputStream) {
				try {
					return createCompressedFileInputStream(inputStream, framedDialect, blockSize);
				}
				catch (IOException e) {
					throw new RuntimeException(e);
				}
			}

		};
	}

	@VisibleForTesting
	protected CompressedFileInputStream createCompressedFileInputStream(InputStream inputStream, FramedSnappyDialect framedDialect, int blockSize) throws IOException {
		if (framedDialect != null) {
			return new CompressedFileInputStreamImpl(new FramedSnappyCompressorInputStream(inputStream, framedDialect));
		}
		return new CompressedFileInputStreamImpl(new SnappyCompressorInputStream(inputStream, blockSize));
	}

}
