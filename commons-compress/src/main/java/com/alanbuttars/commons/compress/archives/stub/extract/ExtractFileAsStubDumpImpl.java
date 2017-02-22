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

import org.apache.commons.compress.archivers.ArchiveException;
import org.apache.commons.compress.archivers.dump.DumpArchiveInputStream;

import com.alanbuttars.commons.compress.archives.config.input.ArchiveInputStreamConfig;
import com.alanbuttars.commons.compress.archives.config.input.ArchiveInputStreamConfigDumpImpl;
import com.alanbuttars.commons.compress.archives.input.ArchiveInputStream;
import com.alanbuttars.commons.compress.archives.input.ArchiveInputStreamImpl;
import com.alanbuttars.commons.compress.archives.util.Archives;
import com.alanbuttars.commons.util.annotations.VisibleForTesting;
import com.alanbuttars.commons.util.functions.Function;

/**
 * Extension of {@link ExtractFileAsStub} for {@link Archives#DUMP} archives.
 * 
 * @author Alan Buttars
 *
 */
class ExtractFileAsStubDumpImpl extends ExtractFileAsStub {

	private String encoding;

	ExtractFileAsStubDumpImpl(File archive) {
		super(archive, Archives.DUMP);
	}

	public ExtractFileAsStubDumpImpl withEncoding(String encoding) {
		this.encoding = encoding;
		return this;
	}

	@Override
	protected Function<ArchiveInputStreamConfig, ArchiveInputStream> streamFunction() {
		return new Function<ArchiveInputStreamConfig, ArchiveInputStream>() {

			@Override
			public ArchiveInputStream apply(ArchiveInputStreamConfig config) {
				try {
					ArchiveInputStreamConfigDumpImpl dumpConfig = (ArchiveInputStreamConfigDumpImpl) config;
					return createArchiveInputStream(dumpConfig.getInputStream(), or(encoding, dumpConfig.getEncoding()));
				}
				catch (ArchiveException e) {
					throw new RuntimeException(e);
				}
			}

		};
	}

	@VisibleForTesting
	protected ArchiveInputStream createArchiveInputStream(InputStream inputStream, String encoding) throws ArchiveException {
		return new ArchiveInputStreamImpl(new DumpArchiveInputStream(inputStream, encoding));
	}

}