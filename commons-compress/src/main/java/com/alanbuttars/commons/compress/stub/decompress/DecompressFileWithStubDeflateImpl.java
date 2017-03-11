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

import static com.alanbuttars.commons.compress.files.util.CompressedFiles.DEFLATE;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.compress.compressors.deflate.DeflateCompressorInputStream;
import org.apache.commons.compress.compressors.deflate.DeflateParameters;

import com.alanbuttars.commons.compress.files.input.CompressedFileInputStream;
import com.alanbuttars.commons.compress.files.input.CompressedFileInputStreamImpl;
import com.alanbuttars.commons.compress.files.util.CompressedFiles;
import com.alanbuttars.commons.util.annotations.VisibleForTesting;
import com.alanbuttars.commons.util.functions.Function;

/**
 * Extension of {@link DecompressFileWithStub} for {@link CompressedFiles#DEFLATE} files.
 * 
 * @author Alan Buttars
 *
 */
public class DecompressFileWithStubDeflateImpl extends DecompressFileWithStub {

	private DeflateParameters parameters;;

	DecompressFileWithStubDeflateImpl(File source) {
		super(source, DEFLATE);
		this.parameters = new DeflateParameters();
	}

	public DecompressFileWithStubDeflateImpl andParameters(DeflateParameters parameters) {
		this.parameters = parameters;
		return this;
	}

	@Override
	protected Function<InputStream, CompressedFileInputStream> decompressionFunction() {
		return new Function<InputStream, CompressedFileInputStream>() {

			@Override
			public CompressedFileInputStream apply(InputStream inputStream) {
				try {
					return createCompressedFileInputStream(inputStream, parameters);
				}
				catch (IOException e) {
					throw new RuntimeException(e);
				}
			}

		};
	}

	@VisibleForTesting
	protected CompressedFileInputStream createCompressedFileInputStream(InputStream inputStream, DeflateParameters parameters) throws IOException {
		return new CompressedFileInputStreamImpl(new DeflateCompressorInputStream(inputStream, parameters));
	}

}
