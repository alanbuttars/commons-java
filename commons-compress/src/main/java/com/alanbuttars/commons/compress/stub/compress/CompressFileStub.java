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

import static com.alanbuttars.commons.util.validators.Arguments.verify;
import static com.alanbuttars.commons.util.validators.Arguments.verifyNonNull;

import java.io.File;
import java.io.OutputStream;

import com.alanbuttars.commons.compress.files.output.CompressedFileOutputStream;
import com.alanbuttars.commons.compress.files.util.CompressedFiles;
import com.alanbuttars.commons.util.functions.Function;

/**
 * Directory compression stub which simply contains the source file as a context.
 * 
 * @author Alan Buttars
 *
 */
public class CompressFileStub {

	protected final File source;

	CompressFileStub(File source) {
		this.source = source;
	}

	/**
	 * Indicates that the {@link #source} will be compressed with {@link CompressedFiles#BZIP2}.
	 */
	public CompressFileWithStubBzip2Impl withBzip2() {
		return new CompressFileWithStubBzip2Impl(source);
	}

	/**
	 * Indicates that the {@link #source} will be compressed with {@link CompressedFiles#DEFLATE}.
	 */
	public CompressFileWithStubDeflateImpl withDeflate() {
		return new CompressFileWithStubDeflateImpl(source);
	}

	/**
	 * Indicates that the {@link #source} will be compressed with {@link CompressedFiles#GZIP}.
	 */
	public CompressFileWithStubGzipImpl withGzip() {
		return new CompressFileWithStubGzipImpl(source);
	}

	/**
	 * Indicates that the {@link #source} will be compressed with {@link CompressedFiles#LZMA}.
	 */
	public CompressFileWithStubLzmaImpl withLzma() {
		return new CompressFileWithStubLzmaImpl(source);
	}

	/**
	 * Indicates that the {@link #source} will be compressed with {@link CompressedFiles#PACK200}.
	 */
	public CompressFileWithStubPack200Impl withPack200() {
		return new CompressFileWithStubPack200Impl(source);
	}

	/**
	 * Indicates that the {@link #source} will be compressed with {@link CompressedFiles#XZ}.
	 */
	public CompressFileWithStubXzImpl withXz() {
		return new CompressFileWithStubXzImpl(source);
	}

	/**
	 * Indicates that the {@link #source} will be compressed with the given functions.
	 * 
	 * @param fileType
	 *            Non-null file type
	 * @param compressionFunction
	 *            Function used to transform the {@link #source} file stream to a compressed output stream
	 */
	public CompressFileWithStub with(String fileType, //
			final Function<OutputStream, CompressedFileOutputStream> compressionFunction) {
		verifyNonNull(fileType, "File type must be non-null");
		verify(!fileType.trim().isEmpty(), "File type must be non-empty");
		verifyNonNull(compressionFunction, "Compression function must be non-null");

		return new CompressFileWithStub(source, fileType) {

			@Override
			protected Function<OutputStream, CompressedFileOutputStream> compressionFunction() {
				return compressionFunction;
			}

		};
	}
}
