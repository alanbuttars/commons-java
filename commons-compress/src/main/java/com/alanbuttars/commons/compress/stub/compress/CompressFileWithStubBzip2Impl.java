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
package com.alanbuttars.commons.compress.stub.compress;

import static com.alanbuttars.commons.compress.files.util.CompressedFiles.BZIP2;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;

import org.apache.commons.compress.compressors.bzip2.BZip2CompressorOutputStream;

import com.alanbuttars.commons.compress.files.output.CompressedFileOutputStream;
import com.alanbuttars.commons.compress.files.output.CompressedFileOutputStreamImpl;
import com.alanbuttars.commons.compress.files.util.CompressedFiles;
import com.alanbuttars.commons.util.annotations.VisibleForTesting;
import com.alanbuttars.commons.util.functions.Function;

/**
 * Extension of {@link CompressFileWithStub} for {@link CompressedFiles#BZIP2} files. For details on the file format,
 * see <a href="https://en.wikipedia.org/wiki/Bzip2">https://en.wikipedia.org/wiki/Bzip2</a>.
 * 
 * @author Alan Buttars
 *
 */
public class CompressFileWithStubBzip2Impl extends CompressFileWithStub {

	private int blockSize;

	CompressFileWithStubBzip2Impl(File source) {
		super(source, BZIP2);
		this.blockSize = BZip2CompressorOutputStream.MAX_BLOCKSIZE;
	}

	public CompressFileWithStubBzip2Impl andBlockSize(int blockSize) {
		this.blockSize = blockSize;
		return this;
	}

	@Override
	protected Function<OutputStream, CompressedFileOutputStream> compressionFunction() {
		return new Function<OutputStream, CompressedFileOutputStream>() {

			@Override
			public CompressedFileOutputStream apply(OutputStream outputStream) {
				try {
					return createCompressedFileOutputStream(outputStream, blockSize);
				}
				catch (IOException e) {
					throw new RuntimeException(e);
				}
			}

		};
	}

	@VisibleForTesting
	protected CompressedFileOutputStream createCompressedFileOutputStream(OutputStream outputStream, int blockSize) throws IOException {
		return new CompressedFileOutputStreamImpl(new BZip2CompressorOutputStream(outputStream, blockSize));
	}

}
