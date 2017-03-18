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

import static com.alanbuttars.commons.compress.archives.util.Archives.TAR;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import org.apache.commons.compress.archivers.tar.TarArchiveInputStream;
import org.apache.commons.compress.archivers.tar.TarConstants;

import com.alanbuttars.commons.compress.archives.input.ArchiveInputStream;
import com.alanbuttars.commons.compress.archives.input.ArchiveInputStreamImpl;
import com.alanbuttars.commons.compress.archives.util.Archives;
import com.alanbuttars.commons.util.annotations.VisibleForTesting;
import com.alanbuttars.commons.util.functions.Function;

/**
 * Extension of {@link DecompressArchiveWithStub} for {@link Archives#TAR}. For details on the file format, see
 * <a href="https://en.wikipedia.org/wiki/Tar_(computing)">https://en.wikipedia.org/wiki/Tar_(computing)</a>.
 * 
 * @author Alan Buttars
 *
 */
public class DecompressArchiveWithStubTarImpl extends DecompressArchiveWithStub {

	private int blockSize;
	private String encoding;
	private int recordSize;

	DecompressArchiveWithStubTarImpl(File source) {
		super(source, TAR);
		this.blockSize = TarConstants.DEFAULT_BLKSIZE;
		this.encoding = null;
		this.recordSize = TarConstants.DEFAULT_RCDSIZE;
	}

	/**
	 * Sets the block size for the archive. By default, it is set to {@link TarConstants#DEFAULT_BLKSIZE}.
	 */
	public DecompressArchiveWithStubTarImpl andBlockSize(int blockSize) {
		this.blockSize = blockSize;
		return this;
	}

	/**
	 * Sets the file encoding for the archive. By default, it is set to <code>null</code>.
	 */
	public DecompressArchiveWithStubTarImpl andEncoding(String encoding) {
		this.encoding = encoding;
		return this;
	}

	/**
	 * Sets the record size for the archive. By default, it is set to {@link TarConstants#DEFAULT_RCDSIZE}.
	 */
	public DecompressArchiveWithStubTarImpl andRecordSize(int recordSize) {
		this.recordSize = recordSize;
		return this;
	}

	@Override
	protected Function<File, ArchiveInputStream> decompressionFunction() {
		return new Function<File, ArchiveInputStream>() {

			@Override
			public ArchiveInputStream apply(File file) {
				try {
					return createArchiveInputStream(file, blockSize, encoding, recordSize);
				}
				catch (IOException e) {
					throw new RuntimeException(e);
				}
			}

		};
	}

	@VisibleForTesting
	protected ArchiveInputStream createArchiveInputStream(File file, int blockSize, String encoding, int recordSize) throws IOException {
		FileInputStream fileInputStream = new FileInputStream(file);
		TarArchiveInputStream archiveInputStream = new TarArchiveInputStream(fileInputStream, blockSize, recordSize, encoding);
		return new ArchiveInputStreamImpl(archiveInputStream);
	}

}
