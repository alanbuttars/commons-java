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

import static com.alanbuttars.commons.util.validators.Arguments.verify;
import static com.alanbuttars.commons.util.validators.Arguments.verifyNonNull;

import java.io.File;
import java.io.InputStream;

import com.alanbuttars.commons.compress.files.input.CompressedFileInputStream;
import com.alanbuttars.commons.compress.files.util.CompressedFiles;
import com.alanbuttars.commons.util.functions.Function;

public class DecompressCompressedFileStub {

	protected final File source;

	DecompressCompressedFileStub(File source) {
		this.source = source;
	}

	/**
	 * Indicates that the {@link #source} will be decompressed with {@link CompressedFiles#BZIP2}.
	 */
	public DecompressCompressedFileWithStubBzip2Impl withBzip2() {
		return new DecompressCompressedFileWithStubBzip2Impl(source);
	}

	/**
	 * Indicates that the {@link #source} will be decompressed with {@link CompressedFiles#DEFLATE}.
	 */
	public DecompressCompressedFileWithStubDeflateImpl withDeflate() {
		return new DecompressCompressedFileWithStubDeflateImpl(source);
	}

	/**
	 * Indicates that the {@link #source} will be decompressed with framed {@link CompressedFiles#SNAPPY}.
	 */
	public DecompressCompressedFileWithStubFramedSnappyImpl withFramedSnappy() {
		return new DecompressCompressedFileWithStubFramedSnappyImpl(source);
	}

	/**
	 * Indicates that the {@link #source} will be decompressed with {@link CompressedFiles#GZIP}.
	 */
	public DecompressCompressedFileWithStubGzipImpl withGzip() {
		return new DecompressCompressedFileWithStubGzipImpl(source);
	}

	/**
	 * Indicates that the {@link #source} will be decompressed with {@link CompressedFiles#LZMA}.
	 */
	public DecompressCompressedFileWithStubLzmaImpl withLzma() {
		return new DecompressCompressedFileWithStubLzmaImpl(source);
	}

	/**
	 * Indicates that the {@link #source} will be decompressed with {@link CompressedFiles#PACK200}.
	 */
	public DecompressCompressedFileWithStubPack200Impl withPack200() {
		return new DecompressCompressedFileWithStubPack200Impl(source);
	}

	/**
	 * Indicates that the {@link #source} will be decompressed with {@link CompressedFiles#SNAPPY}.
	 */
	public DecompressCompressedFileWithStubSnappyImpl withSnappy() {
		return new DecompressCompressedFileWithStubSnappyImpl(source);
	}

	/**
	 * Indicates that the {@link #source} will be decompressed with {@link CompressedFiles#XZ}.
	 */
	public DecompressCompressedFileWithStubXzImpl withXz() {
		return new DecompressCompressedFileWithStubXzImpl(source);
	}

	/**
	 * Indicates that the {@link #source} will be decompressed with {@link CompressedFiles#Z}.
	 */
	public DecompressCompressedFileWithStubZImpl withZ() {
		return new DecompressCompressedFileWithStubZImpl(source);
	}

	/**
	 * Indicates that the {@link #source} will be decompressed with the given stream functions.
	 * 
	 * @param fileType
	 *            Non-null file type
	 * @param decompressionFunction
	 *            Function used to transform the {@link #source} file stream to a decompressed output stream
	 */
	public DecompressCompressedFileWithStub with(String fileType, //
			final Function<InputStream, CompressedFileInputStream> decompressionFunction) {
		verifyNonNull(fileType, "File type must be non-null");
		verify(!fileType.trim().isEmpty(), "File type must be non-empty");
		verifyNonNull(decompressionFunction, "Decompression function must be non-null");

		return new DecompressCompressedFileWithStub(source, fileType) {

			@Override
			protected Function<InputStream, CompressedFileInputStream> decompressionFunction() {
				return decompressionFunction;
			}

		};
	}

}
