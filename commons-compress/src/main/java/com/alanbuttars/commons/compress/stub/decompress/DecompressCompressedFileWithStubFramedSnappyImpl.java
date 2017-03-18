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

import static com.alanbuttars.commons.compress.files.util.CompressedFiles.FRAMEDSNAPPY;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.compress.compressors.snappy.FramedSnappyCompressorInputStream;
import org.apache.commons.compress.compressors.snappy.FramedSnappyDialect;

import com.alanbuttars.commons.compress.files.input.CompressedFileInputStream;
import com.alanbuttars.commons.compress.files.input.CompressedFileInputStreamImpl;
import com.alanbuttars.commons.compress.files.util.CompressedFiles;
import com.alanbuttars.commons.util.annotations.VisibleForTesting;
import com.alanbuttars.commons.util.functions.Function;

/**
 * Extension of {@link DecompressCompressedFileWithStub} for {@link CompressedFiles#FRAMEDSNAPPY} files. For details on
 * the file format, see
 * <a href="https://en.wikipedia.org/wiki/Snappy_(compression)">https://en.wikipedia.org/wiki/Snappy_(compression)</a>.
 * 
 * @author Alan Buttars
 *
 */
public class DecompressCompressedFileWithStubFramedSnappyImpl extends DecompressCompressedFileWithStub {

	private FramedSnappyDialect dialect;

	DecompressCompressedFileWithStubFramedSnappyImpl(File source) {
		super(source, FRAMEDSNAPPY);
		this.dialect = FramedSnappyDialect.STANDARD;
	}

	/**
	 * Sets the dialect on the compressed file. By default, it is set to {@link FramedSnappyDialect#STANDARD}.
	 */
	public DecompressCompressedFileWithStubFramedSnappyImpl andDialect(FramedSnappyDialect dialect) {
		this.dialect = dialect;
		return this;
	}

	@Override
	protected Function<InputStream, CompressedFileInputStream> decompressionFunction() {
		return new Function<InputStream, CompressedFileInputStream>() {

			@Override
			public CompressedFileInputStream apply(InputStream inputStream) {
				try {
					return createCompressedFileInputStream(inputStream, dialect);
				}
				catch (IOException e) {
					throw new RuntimeException(e);
				}
			}

		};
	}

	@VisibleForTesting
	protected CompressedFileInputStream createCompressedFileInputStream(InputStream inputStream, FramedSnappyDialect dialect) throws IOException {
		return new CompressedFileInputStreamImpl(new FramedSnappyCompressorInputStream(inputStream, dialect));
	}

}
