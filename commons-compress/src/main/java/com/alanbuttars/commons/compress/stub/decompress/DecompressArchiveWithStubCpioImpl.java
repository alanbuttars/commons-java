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

import static com.alanbuttars.commons.compress.archives.util.Archives.CPIO;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import org.apache.commons.compress.archivers.cpio.CpioArchiveInputStream;
import org.apache.commons.compress.archivers.cpio.CpioConstants;
import org.apache.commons.compress.utils.CharsetNames;

import com.alanbuttars.commons.compress.archives.input.ArchiveInputStream;
import com.alanbuttars.commons.compress.archives.input.ArchiveInputStreamImpl;
import com.alanbuttars.commons.compress.archives.util.Archives;
import com.alanbuttars.commons.util.annotations.VisibleForTesting;
import com.alanbuttars.commons.util.functions.Function;

/**
 * Extension of {@link DecompressArchiveWithStub} for {@link Archives#CPIO}. For details on the file format, see
 * <a href="https://en.wikipedia.org/wiki/Cpio">https://en.wikipedia.org/wiki/Cpio</a>.
 * 
 * @author Alan Buttars
 *
 */
public class DecompressArchiveWithStubCpioImpl extends DecompressArchiveWithStub {

	private int blockSize;
	private String encoding;

	DecompressArchiveWithStubCpioImpl(File source) {
		super(source, CPIO);
		this.blockSize = CpioConstants.BLOCK_SIZE;
		this.encoding = CharsetNames.US_ASCII;
	}

	/**
	 * Sets the block size used to read the archive. By default, it is set to {@link CpioConstants#BLOCK_SIZE}.
	 */
	public DecompressArchiveWithStubCpioImpl andBlockSize(int blockSize) {
		this.blockSize = blockSize;
		return this;
	}

	/**
	 * Sets the file encoding for the archive. Pass <code>null</code> to use the platform default. By default, it is set
	 * to {@link CharsetNames#US_ASCII}.
	 */
	public DecompressArchiveWithStubCpioImpl andEncoding(String encoding) {
		this.encoding = encoding;
		return this;
	}

	@Override
	protected Function<File, ArchiveInputStream> decompressionFunction() {
		return new Function<File, ArchiveInputStream>() {

			@Override
			public ArchiveInputStream apply(File file) {
				try {
					return createArchiveInputStream(file, blockSize, encoding);
				}
				catch (IOException e) {
					throw new RuntimeException(e);
				}
			}

		};
	}

	@VisibleForTesting
	protected ArchiveInputStream createArchiveInputStream(File file, int blockSize, String encoding) throws IOException {
		FileInputStream fileInputStream = new FileInputStream(file);
		CpioArchiveInputStream archiveInputStream = new CpioArchiveInputStream(fileInputStream, blockSize, encoding);
		return new ArchiveInputStreamImpl(archiveInputStream);
	}

}
