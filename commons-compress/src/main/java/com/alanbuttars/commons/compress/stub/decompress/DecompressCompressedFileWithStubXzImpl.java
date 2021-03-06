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

import static com.alanbuttars.commons.compress.files.util.CompressedFiles.XZ;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.compress.compressors.xz.XZCompressorInputStream;

import com.alanbuttars.commons.compress.files.input.CompressedFileInputStream;
import com.alanbuttars.commons.compress.files.input.CompressedFileInputStreamImpl;
import com.alanbuttars.commons.compress.files.util.CompressedFiles;
import com.alanbuttars.commons.util.annotations.VisibleForTesting;
import com.alanbuttars.commons.util.functions.Function;

/**
 * Extension of {@link DecompressCompressedFileWithStub} for {@link CompressedFiles#XZ} files. For details on the file
 * format, see <a href="https://en.wikipedia.org/wiki/Xz">https://en.wikipedia.org/wiki/Xz</a>.
 * 
 * @author Alan Buttars
 *
 */
public class DecompressCompressedFileWithStubXzImpl extends DecompressCompressedFileWithStub {

	private boolean decompressConcatenated;

	DecompressCompressedFileWithStubXzImpl(File source) {
		super(source, XZ);
		this.decompressConcatenated = false;
	}

	/**
	 * If set to <code>true</code>, decompresses the stream until the end of the input. By default, it is set to
	 * <code>false</code>. See {@link XZCompressorInputStream#XZCompressorInputStream(InputStream, boolean)}.
	 */
	public DecompressCompressedFileWithStubXzImpl andDecompressConcatenated(boolean decompressConcatenated) {
		this.decompressConcatenated = decompressConcatenated;
		return this;
	}

	@Override
	protected Function<InputStream, CompressedFileInputStream> decompressionFunction() {
		return new Function<InputStream, CompressedFileInputStream>() {

			@Override
			public CompressedFileInputStream apply(InputStream inputStream) {
				try {
					return createCompressedFileInputStream(inputStream, decompressConcatenated);
				}
				catch (IOException e) {
					throw new RuntimeException(e);
				}
			}

		};
	}

	@VisibleForTesting
	protected CompressedFileInputStream createCompressedFileInputStream(InputStream inputStream, boolean decompressConcatenated) throws IOException {
		return new CompressedFileInputStreamImpl(new XZCompressorInputStream(inputStream, decompressConcatenated));
	}

}
