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

import static com.alanbuttars.commons.compress.files.util.CompressedFiles.DEFLATE;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;

import org.apache.commons.compress.compressors.deflate.DeflateCompressorOutputStream;
import org.apache.commons.compress.compressors.deflate.DeflateParameters;

import com.alanbuttars.commons.compress.files.output.CompressedFileOutputStream;
import com.alanbuttars.commons.compress.files.output.CompressedFileOutputStreamImpl;
import com.alanbuttars.commons.compress.files.util.CompressedFiles;
import com.alanbuttars.commons.util.annotations.VisibleForTesting;
import com.alanbuttars.commons.util.functions.Function;

/**
 * Extension of {@link CompressFileWithStub} for {@link CompressedFiles#DEFLATE} files. For details on the file format, see
 * <a href="https://en.wikipedia.org/wiki/DEFLATE">https://en.wikipedia.org/wiki/DEFLATE</a>.
 * 
 * @author Alan Buttars
 *
 */
public class CompressFileWithStubDeflateImpl extends CompressFileWithStub {

	private DeflateParameters parameters;

	CompressFileWithStubDeflateImpl(File source) {
		super(source, DEFLATE);
		this.parameters = new DeflateParameters();
	}

	public CompressFileWithStubDeflateImpl andParameters(DeflateParameters parameters) {
		this.parameters = parameters;
		return this;
	}

	@Override
	protected Function<OutputStream, CompressedFileOutputStream> compressionFunction() {
		return new Function<OutputStream, CompressedFileOutputStream>() {

			@Override
			public CompressedFileOutputStream apply(OutputStream outputStream) {
				try {
					return createCompressedFileOutputStream(outputStream, parameters);
				}
				catch (IOException e) {
					throw new RuntimeException(e);
				}
			}

		};
	}

	@VisibleForTesting
	protected CompressedFileOutputStream createCompressedFileOutputStream(OutputStream outputStream, DeflateParameters parameters) throws IOException {
		return new CompressedFileOutputStreamImpl(new DeflateCompressorOutputStream(outputStream, parameters));
	}

}
