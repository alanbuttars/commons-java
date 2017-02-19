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
package com.alanbuttars.commons.compress.files.output;

import java.io.Closeable;
import java.io.IOException;

import org.apache.commons.compress.compressors.CompressorOutputStream;

/**
 * Extension of {@link CompressedFileOutputStream} which wraps {@link CompressorOutputStream}s.
 * 
 * @author Alan Buttars
 *
 */
public class CompressedFileOutputStreamImpl implements CompressedFileOutputStream {

	private final CompressorOutputStream compressorOutputStream;

	public CompressedFileOutputStreamImpl(CompressorOutputStream compressorOutputStream) {
		this.compressorOutputStream = compressorOutputStream;
	}

	@Override
	public void close() throws IOException {
		compressorOutputStream.close();
	}

	@Override
	public void write(byte[] content, int offset, int length) throws IOException {
		compressorOutputStream.write(content, offset, length);
	}

	@Override
	public Closeable getStream() {
		return compressorOutputStream;
	}
}
