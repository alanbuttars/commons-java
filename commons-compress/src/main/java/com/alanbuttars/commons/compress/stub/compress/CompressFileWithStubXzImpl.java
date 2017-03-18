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

import static com.alanbuttars.commons.compress.files.util.CompressedFiles.XZ;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;

import org.apache.commons.compress.compressors.xz.XZCompressorOutputStream;
import org.tukaani.xz.LZMA2Options;

import com.alanbuttars.commons.compress.files.output.CompressedFileOutputStream;
import com.alanbuttars.commons.compress.files.output.CompressedFileOutputStreamImpl;
import com.alanbuttars.commons.compress.files.util.CompressedFiles;
import com.alanbuttars.commons.util.annotations.VisibleForTesting;
import com.alanbuttars.commons.util.functions.Function;

/**
 * Extension of {@link CompressFileWithStub} for {@link CompressedFiles#XZ} files. For details on the file format, see
 * <a href="https://en.wikipedia.org/wiki/Xz">https://en.wikipedia.org/wiki/Xz</a>.
 * 
 * @author Alan Buttars
 *
 */
public class CompressFileWithStubXzImpl extends CompressFileWithStub {

	private int preset;

	CompressFileWithStubXzImpl(File source) {
		super(source, XZ);
		this.preset = LZMA2Options.PRESET_DEFAULT;
	}

	/**
	 * Sets the specified LZMA2 preset level. By default it is set to {@link LZMA2Options#PRESET_DEFAULT}. See
	 * {@link XZCompressorOutputStream#XZCompressorOutputStream(OutputStream, int)}.
	 */
	public CompressFileWithStubXzImpl andPreset(int preset) {
		this.preset = preset;
		return this;
	}

	@Override
	protected Function<OutputStream, CompressedFileOutputStream> compressionFunction() {
		return new Function<OutputStream, CompressedFileOutputStream>() {

			@Override
			public CompressedFileOutputStream apply(OutputStream outputStream) {
				try {
					return createCompressedFileOutputStream(outputStream, preset);
				}
				catch (IOException e) {
					throw new RuntimeException(e);
				}
			}

		};
	}

	@VisibleForTesting
	protected CompressedFileOutputStream createCompressedFileOutputStream(OutputStream outputStream, int preset) throws IOException {
		return new CompressedFileOutputStreamImpl(new XZCompressorOutputStream(outputStream, preset));
	}

}
