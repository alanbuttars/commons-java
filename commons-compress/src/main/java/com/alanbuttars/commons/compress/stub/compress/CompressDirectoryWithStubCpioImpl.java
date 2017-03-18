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

import static com.alanbuttars.commons.compress.archives.util.Archives.CPIO;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.cpio.CpioArchiveEntry;
import org.apache.commons.compress.archivers.cpio.CpioArchiveOutputStream;
import org.apache.commons.compress.archivers.cpio.CpioConstants;
import org.apache.commons.compress.utils.CharsetNames;

import com.alanbuttars.commons.compress.archives.output.ArchiveOutputStream;
import com.alanbuttars.commons.compress.archives.output.ArchiveOutputStreamImpl;
import com.alanbuttars.commons.compress.archives.util.Archives;
import com.alanbuttars.commons.util.annotations.VisibleForTesting;
import com.alanbuttars.commons.util.functions.BiFunction;
import com.alanbuttars.commons.util.functions.Function;

/**
 * Extension of {@link CompressDirectoryWithStub} for {@link Archives#CPIO} files. For details on the file format, see
 * <a href="https://en.wikipedia.org/wiki/Cpio">https://en.wikipedia.org/wiki/Cpio</a>.
 * 
 * @author Alan Buttars
 *
 */
public class CompressDirectoryWithStubCpioImpl extends CompressDirectoryWithStub {

	private int blockSize;
	private String encoding;
	private short format;

	CompressDirectoryWithStubCpioImpl(File source) {
		super(source, CPIO);
		this.blockSize = CpioConstants.BLOCK_SIZE;
		this.encoding = CharsetNames.US_ASCII;
		this.format = CpioConstants.FORMAT_NEW;
	}

	/**
	 * Sets the block size of the archive. By default, it is set to {@link CpioConstants#BLOCK_SIZE}.
	 */
	public CompressDirectoryWithStubCpioImpl andBlockSize(int blockSize) {
		this.blockSize = blockSize;
		return this;
	}

	/**
	 * Sets the encoding of file names within the archive. By default, it is set to {@link CharsetNames#US_ASCII}. Pass
	 * <code>null</code> to use the platform's default.
	 */
	public CompressDirectoryWithStubCpioImpl andEncoding(String encoding) {
		this.encoding = encoding;
		return this;
	}

	/**
	 * Sets the format of the archive stream. By default, it is set to {@link CpioConstants#FORMAT_NEW}.
	 */
	public CompressDirectoryWithStubCpioImpl andFormat(short format) {
		this.format = format;
		return this;
	}

	@Override
	protected Function<File, ArchiveOutputStream> compressionFunction() {
		return new Function<File, ArchiveOutputStream>() {

			@Override
			public ArchiveOutputStream apply(File file) {
				try {
					return createArchiveOutputStream(file, blockSize, encoding, format);
				}
				catch (IOException e) {
					throw new RuntimeException(e);
				}
			}

		};
	}

	@VisibleForTesting
	protected ArchiveOutputStream createArchiveOutputStream(File file, int blockSize, String encoding, short format) throws IOException {
		FileOutputStream fileOutputStream = new FileOutputStream(file);
		CpioArchiveOutputStream archiveOutputStream = new CpioArchiveOutputStream(fileOutputStream, format, blockSize, encoding);
		return new ArchiveOutputStreamImpl(archiveOutputStream);
	}

	@Override
	protected BiFunction<String, Long, ArchiveEntry> entryFunction() {
		return new BiFunction<String, Long, ArchiveEntry>() {

			@Override
			public ArchiveEntry apply(String entryName, Long fileSize) {
				return new CpioArchiveEntry(format, entryName, fileSize);
			}

		};
	}

}
