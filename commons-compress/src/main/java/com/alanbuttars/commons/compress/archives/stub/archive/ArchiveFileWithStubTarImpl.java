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
package com.alanbuttars.commons.compress.archives.stub.archive;

import static com.alanbuttars.commons.compress.archives.util.Archives.TAR;
import static com.alanbuttars.commons.compress.util.Optionals.or;

import java.io.File;
import java.io.OutputStream;

import org.apache.commons.compress.archivers.tar.TarArchiveOutputStream;

import com.alanbuttars.commons.compress.archives.config.output.ArchiveOutputStreamConfig;
import com.alanbuttars.commons.compress.archives.config.output.ArchiveOutputStreamConfigTarImpl;
import com.alanbuttars.commons.compress.archives.output.ArchiveOutputStream;
import com.alanbuttars.commons.compress.archives.output.ArchiveOutputStreamImpl;
import com.alanbuttars.commons.compress.archives.util.Archives;
import com.alanbuttars.commons.util.annotations.VisibleForTesting;
import com.alanbuttars.commons.util.functions.Function;

/**
 * Extension of {@link ArchiveFileWithStub} for {@link Archives#TAR} archives.
 * 
 * @author Alan Buttars
 *
 */
class ArchiveFileWithStubTarImpl extends ArchiveFileWithStub {

	private int blockSize;
	private int recordSize;
	private String encoding;
	private Boolean addPaxHeadersForNonAsciiNames;
	private int bigNumberMode;
	private int longFileMode;

	ArchiveFileWithStubTarImpl(File source) {
		super(source, TAR);
	}

	public ArchiveFileWithStubTarImpl withBlockSize(int blockSize) {
		this.blockSize = blockSize;
		return this;
	}

	public ArchiveFileWithStubTarImpl withRecordSize(int recordSize) {
		this.recordSize = recordSize;
		return this;
	}

	public ArchiveFileWithStubTarImpl withEncoding(String encoding) {
		this.encoding = encoding;
		return this;
	}

	public ArchiveFileWithStubTarImpl addPaxHeadersForNonAsciiNames(boolean addPaxHeadersForNonAsciiNames) {
		this.addPaxHeadersForNonAsciiNames = addPaxHeadersForNonAsciiNames;
		return this;
	}

	public ArchiveFileWithStubTarImpl withBigNumberMode(int bigNumberMode) {
		this.bigNumberMode = bigNumberMode;
		return this;
	}

	public ArchiveFileWithStubTarImpl withLongFileMode(int longFileMode) {
		this.longFileMode = longFileMode;
		return this;
	}

	@Override
	protected Function<ArchiveOutputStreamConfig, ArchiveOutputStream> streamFunction() {
		return new Function<ArchiveOutputStreamConfig, ArchiveOutputStream>() {

			@Override
			public ArchiveOutputStream apply(ArchiveOutputStreamConfig config) {
				ArchiveOutputStreamConfigTarImpl tarConfig = (ArchiveOutputStreamConfigTarImpl) config;
				return createArchiveOutputStream(tarConfig.getOutputStream(), //
						or(blockSize, tarConfig.getBlockSize()), //
						or(recordSize, tarConfig.getRecordSize()), //
						or(encoding, tarConfig.getEncoding()), //
						or(addPaxHeadersForNonAsciiNames, tarConfig.addPaxHeadersForNonAsciiNames()), //
						or(bigNumberMode, tarConfig.getBigNumberMode()), //
						or(longFileMode, tarConfig.getLongFileMode()));
			}

		};
	}

	@VisibleForTesting
	protected ArchiveOutputStream createArchiveOutputStream(OutputStream outputStream, //
			int blockSize, //
			int recordSize, //
			String encoding, //
			boolean addPaxHeadersForNonAsciiNames, //
			int bigNumberMode, //
			int longFileMode) {
		TarArchiveOutputStream stream = new TarArchiveOutputStream(outputStream, blockSize, recordSize, encoding);
		stream.setAddPaxHeadersForNonAsciiNames(addPaxHeadersForNonAsciiNames);
		stream.setBigNumberMode(bigNumberMode);
		stream.setLongFileMode(longFileMode);
		return new ArchiveOutputStreamImpl(stream);
	}

}
