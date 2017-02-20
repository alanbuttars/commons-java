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
package com.alanbuttars.commons.compress.archives.stub.extract;

import java.io.File;
import java.io.InputStream;

import org.apache.commons.compress.archivers.tar.TarArchiveInputStream;

import com.alanbuttars.commons.compress.archives.config.input.ArchiveInputStreamConfig;
import com.alanbuttars.commons.compress.archives.config.input.ArchiveInputStreamConfigTarImpl;
import com.alanbuttars.commons.compress.archives.input.ArchiveInputStream;
import com.alanbuttars.commons.compress.archives.input.ArchiveInputStreamImpl;
import com.alanbuttars.commons.compress.archives.util.Archives;
import com.alanbuttars.commons.util.annotations.VisibleForTesting;
import com.alanbuttars.commons.util.functions.Function;

/**
 * Extension of {@link ExtractFileAsStub} for {@link Archives#TAR} archives.
 * 
 * @author Alan Buttars
 *
 */
class ExtractFileAsStubTarImpl extends ExtractFileAsStub {

	private int blockSize;
	private int recordSize;
	private String encoding;

	ExtractFileAsStubTarImpl(File archive) {
		super(archive, Archives.TAR);
	}

	public ExtractFileAsStubTarImpl withBlockSize(int blockSize) {
		this.blockSize = blockSize;
		return this;
	}

	public ExtractFileAsStubTarImpl withRecordSize(int recordSize) {
		this.recordSize = recordSize;
		return this;
	}

	public ExtractFileAsStubTarImpl withEncoding(String encoding) {
		this.encoding = encoding;
		return this;
	}

	@Override
	protected Function<ArchiveInputStreamConfig, ArchiveInputStream> streamFunction() {
		return new Function<ArchiveInputStreamConfig, ArchiveInputStream>() {

			@Override
			public ArchiveInputStream apply(ArchiveInputStreamConfig config) {
				ArchiveInputStreamConfigTarImpl tarConfig = (ArchiveInputStreamConfigTarImpl) config;
				return createArchiveInputStream(//
						tarConfig.getInputStream(), //
						or(blockSize, tarConfig.getBlockSize()), //
						or(recordSize, tarConfig.getRecordSize()), //
						or(encoding, tarConfig.getEncoding()));
			}

		};
	}

	@VisibleForTesting
	protected ArchiveInputStream createArchiveInputStream(InputStream inputStream, int blockSize, int recordSize, String encoding) {
		return new ArchiveInputStreamImpl(new TarArchiveInputStream(inputStream, blockSize, recordSize, encoding));
	}

}
