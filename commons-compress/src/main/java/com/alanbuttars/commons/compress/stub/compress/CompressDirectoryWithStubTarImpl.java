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

import static com.alanbuttars.commons.compress.archives.util.Archives.TAR;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveOutputStream;
import org.apache.commons.compress.archivers.tar.TarConstants;

import com.alanbuttars.commons.compress.archives.output.ArchiveOutputStream;
import com.alanbuttars.commons.compress.archives.output.ArchiveOutputStreamImpl;
import com.alanbuttars.commons.compress.archives.util.Archives;
import com.alanbuttars.commons.util.annotations.VisibleForTesting;
import com.alanbuttars.commons.util.functions.BiFunction;
import com.alanbuttars.commons.util.functions.Function;

/**
 * Extension of {@link CompressDirectoryWithStub} for {@link Archives#TAR} files. For details on the file format, see
 * <a href="https://en.wikipedia.org/wiki/Tar_(computing)">https://en.wikipedia.org/wiki/Tar_(computing)</a>.
 * 
 * @author Alan Buttars
 *
 */
public class CompressDirectoryWithStubTarImpl extends CompressDirectoryWithStub {

	private boolean addPaxHeadersForNonAsciiNames;
	private int bigNumberMode;
	private int blockSize;
	private String encoding;
	private int longFileMode;
	private boolean preserveLeadingSlashes;
	private int recordSize;

	CompressDirectoryWithStubTarImpl(File source) {
		super(source, TAR);
		this.addPaxHeadersForNonAsciiNames = false;
		this.bigNumberMode = TarArchiveOutputStream.BIGNUMBER_ERROR;
		this.blockSize = TarConstants.DEFAULT_BLKSIZE;
		this.encoding = null;
		this.longFileMode = TarArchiveOutputStream.LONGFILE_ERROR;
		this.preserveLeadingSlashes = false;
		this.recordSize = TarConstants.DEFAULT_RCDSIZE;
	}

	public CompressDirectoryWithStubTarImpl andAddPaxHeadersForNonAsciiNames(boolean addPaxHeadersForNonAsciiNames) {
		this.addPaxHeadersForNonAsciiNames = addPaxHeadersForNonAsciiNames;
		return this;
	}

	public CompressDirectoryWithStubTarImpl andBigNumberMode(int bigNumberMode) {
		this.bigNumberMode = bigNumberMode;
		return this;
	}

	public CompressDirectoryWithStubTarImpl andBlockSize(int blockSize) {
		this.blockSize = blockSize;
		return this;
	}

	public CompressDirectoryWithStubTarImpl andEncoding(String encoding) {
		this.encoding = encoding;
		return this;
	}

	public CompressDirectoryWithStubTarImpl andLongFileMode(int longFileMode) {
		this.longFileMode = longFileMode;
		return this;
	}

	public CompressDirectoryWithStubTarImpl andPreserveLeadingSlashes(boolean preserveLeadingSlashes) {
		this.preserveLeadingSlashes = preserveLeadingSlashes;
		return this;
	}

	public CompressDirectoryWithStubTarImpl andRecordSize(int recordSize) {
		this.recordSize = recordSize;
		return this;
	}

	@Override
	protected Function<File, ArchiveOutputStream> compressionFunction() {
		return new Function<File, ArchiveOutputStream>() {

			@Override
			public ArchiveOutputStream apply(File file) {
				try {
					return createArchiveOutputStream(file, addPaxHeadersForNonAsciiNames, bigNumberMode, blockSize, encoding, longFileMode, recordSize);
				}
				catch (IOException e) {
					throw new RuntimeException(e);
				}
			}

		};
	}

	@VisibleForTesting
	protected ArchiveOutputStream createArchiveOutputStream(File file, //
			boolean addPaxHeadersForNonAsciiNames, //
			int bigNumberMode, //
			int blockSize, //
			String encoding, //
			int longFileMode, //
			int recordSize) throws FileNotFoundException {
		FileOutputStream fileOutputStream = new FileOutputStream(file);
		TarArchiveOutputStream archiveOutputStream = new TarArchiveOutputStream(fileOutputStream, blockSize, recordSize, encoding);
		archiveOutputStream.setAddPaxHeadersForNonAsciiNames(addPaxHeadersForNonAsciiNames);
		archiveOutputStream.setBigNumberMode(bigNumberMode);
		archiveOutputStream.setLongFileMode(longFileMode);
		return new ArchiveOutputStreamImpl(archiveOutputStream);
	}

	@Override
	protected BiFunction<String, Long, ArchiveEntry> entryFunction() {
		return new BiFunction<String, Long, ArchiveEntry>() {

			@Override
			public ArchiveEntry apply(String entryName, Long fileSize) {
				TarArchiveEntry entry = new TarArchiveEntry(entryName, preserveLeadingSlashes);
				entry.setSize(fileSize);
				return entry;
			}

		};
	}

}
