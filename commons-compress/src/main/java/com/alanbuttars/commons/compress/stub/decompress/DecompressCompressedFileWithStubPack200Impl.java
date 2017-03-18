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

import static com.alanbuttars.commons.compress.files.util.CompressedFiles.PACK200;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

import org.apache.commons.compress.compressors.pack200.Pack200CompressorInputStream;
import org.apache.commons.compress.compressors.pack200.Pack200Strategy;

import com.alanbuttars.commons.compress.files.input.CompressedFileInputStream;
import com.alanbuttars.commons.compress.files.input.CompressedFileInputStreamImpl;
import com.alanbuttars.commons.compress.files.util.CompressedFiles;
import com.alanbuttars.commons.util.annotations.VisibleForTesting;
import com.alanbuttars.commons.util.functions.Function;

/**
 * Extension of {@link DecompressCompressedFileWithStub} for {@link CompressedFiles#PACK200} files. For details on the
 * file format, see <a href="https://en.wikipedia.org/wiki/Pack200">https://en.wikipedia.org/wiki/Pack200</a>.
 * 
 * @author Alan Buttars
 *
 */
public class DecompressCompressedFileWithStubPack200Impl extends DecompressCompressedFileWithStub {

	private Pack200Strategy mode;
	private Map<String, String> properties;

	DecompressCompressedFileWithStubPack200Impl(File source) {
		super(source, PACK200);
		this.mode = Pack200Strategy.IN_MEMORY;
		this.properties = null;
	}

	/**
	 * Sets the mode on the compressed file. By default, it is set to {@link Pack200Strategy#IN_MEMORY}.
	 */
	public DecompressCompressedFileWithStubPack200Impl andMode(Pack200Strategy mode) {
		this.mode = mode;
		return this;
	}

	/**
	 * Sets the string properties on the compressed file. By default, it is set to an empty <code>Map</code>.
	 */
	public DecompressCompressedFileWithStubPack200Impl andProperties(Map<String, String> properties) {
		this.properties = properties;
		return this;
	}

	@Override
	protected Function<InputStream, CompressedFileInputStream> decompressionFunction() {
		return new Function<InputStream, CompressedFileInputStream>() {

			@Override
			public CompressedFileInputStream apply(InputStream inputStream) {
				try {
					return createCompressedFileInputStream(inputStream, mode, properties);
				}
				catch (IOException e) {
					throw new RuntimeException(e);
				}
			}

		};
	}

	@VisibleForTesting
	protected CompressedFileInputStream createCompressedFileInputStream(InputStream inputStream, Pack200Strategy mode, Map<String, String> properties) throws IOException {
		return new CompressedFileInputStreamImpl(new Pack200CompressorInputStream(inputStream, mode, properties));
	}

}
