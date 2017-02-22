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

import static com.alanbuttars.commons.compress.archives.util.Archives.SEVENZ;

import java.io.File;
import java.io.IOException;

import org.apache.commons.compress.archivers.sevenz.SevenZMethodConfiguration;
import org.apache.commons.compress.archivers.sevenz.SevenZOutputFile;

import com.alanbuttars.commons.compress.archives.config.output.ArchiveOutputStreamConfig;
import com.alanbuttars.commons.compress.archives.config.output.ArchiveOutputStreamConfigSevenZImpl;
import com.alanbuttars.commons.compress.archives.output.ArchiveOutputStream;
import com.alanbuttars.commons.compress.archives.output.ArchiveOutputStreamSevenZImpl;
import com.alanbuttars.commons.compress.archives.util.Archives;
import com.alanbuttars.commons.util.annotations.VisibleForTesting;
import com.alanbuttars.commons.util.functions.Function;

/**
 * Extension of {@link ArchiveFileAsStub} for {@link Archives#SEVENZ} archives.
 * 
 * @author Alan Buttars
 *
 */
class ArchiveFileAsStubSevenZImpl extends ArchiveFileAsStub {

	private Iterable<? extends SevenZMethodConfiguration> contentMethods;

	ArchiveFileAsStubSevenZImpl(File directory) {
		super(directory, SEVENZ);
	}

	public ArchiveFileAsStubSevenZImpl withContentMethods(Iterable<? extends SevenZMethodConfiguration> contentMethods) {
		this.contentMethods = contentMethods;
		return this;
	}

	@Override
	protected Function<ArchiveOutputStreamConfig, ArchiveOutputStream> streamFunction() {
		return new Function<ArchiveOutputStreamConfig, ArchiveOutputStream>() {

			@Override
			public ArchiveOutputStream apply(ArchiveOutputStreamConfig config) {
				try {
					ArchiveOutputStreamConfigSevenZImpl sevenZConfig = (ArchiveOutputStreamConfigSevenZImpl) config;
					return createArchiveOutputStream(sevenZConfig.getFile(), or(contentMethods, sevenZConfig.getContentMethods()));
				}
				catch (IOException e) {
					throw new RuntimeException(e);
				}
			}

		};
	}

	@VisibleForTesting
	protected ArchiveOutputStream createArchiveOutputStream(File file, Iterable<? extends SevenZMethodConfiguration> contentMethods) throws IOException {
		SevenZOutputFile sevenZFile = new SevenZOutputFile(file);
		sevenZFile.setContentMethods(contentMethods);
		return new ArchiveOutputStreamSevenZImpl(sevenZFile);
	}

}
