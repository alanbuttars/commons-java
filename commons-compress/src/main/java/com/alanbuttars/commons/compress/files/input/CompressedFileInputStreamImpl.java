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
package com.alanbuttars.commons.compress.files.input;

import java.io.Closeable;
import java.io.IOException;

import org.apache.commons.compress.compressors.CompressorInputStream;

/**
 * Extension of {@link CompressedFileInputStream} which wraps {@link CompressorInputStream}s.
 * 
 * @author Alan Buttars
 *
 */
public class CompressedFileInputStreamImpl implements CompressedFileInputStream {

	private final CompressorInputStream compressorInputStream;

	/**
	 * @param compressorInputStream
	 *            Non-null Apache compressor input stream
	 */
	public CompressedFileInputStreamImpl(CompressorInputStream compressorInputStream) {
		this.compressorInputStream = compressorInputStream;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void close() throws IOException {
		compressorInputStream.close();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int read(byte[] content) throws IOException {
		return compressorInputStream.read(content);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Closeable getStream() {
		return compressorInputStream;
	}

}
