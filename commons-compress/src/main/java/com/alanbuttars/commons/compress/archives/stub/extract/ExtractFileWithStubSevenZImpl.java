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

import static com.alanbuttars.commons.compress.archives.util.Archives.SEVENZ;
import static com.alanbuttars.commons.compress.util.Optionals.or;

import java.io.File;
import java.io.IOException;

import org.apache.commons.compress.archivers.sevenz.SevenZFile;

import com.alanbuttars.commons.compress.archives.config.input.ArchiveInputStreamConfig;
import com.alanbuttars.commons.compress.archives.config.input.ArchiveInputStreamConfigSevenZImpl;
import com.alanbuttars.commons.compress.archives.input.ArchiveInputStream;
import com.alanbuttars.commons.compress.archives.input.ArchiveInputStreamSevenZImpl;
import com.alanbuttars.commons.compress.archives.util.Archives;
import com.alanbuttars.commons.util.annotations.VisibleForTesting;
import com.alanbuttars.commons.util.functions.Function;

/**
 * Extension of {@link ExtractFileWithStub} for {@link Archives#SEVENZ} archives.
 * 
 * @author Alan Buttars
 *
 */
class ExtractFileWithStubSevenZImpl extends ExtractFileWithStub {

	private byte[] password;

	ExtractFileWithStubSevenZImpl(File source) {
		super(source, SEVENZ);
	}

	public ExtractFileWithStubSevenZImpl withPassword(byte[] password) {
		this.password = password;
		return this;
	}

	@Override
	protected Function<ArchiveInputStreamConfig, ArchiveInputStream> streamFunction() {
		return new Function<ArchiveInputStreamConfig, ArchiveInputStream>() {

			@Override
			public ArchiveInputStream apply(ArchiveInputStreamConfig config) {
				try {
					ArchiveInputStreamConfigSevenZImpl sevenZConfig = (ArchiveInputStreamConfigSevenZImpl) config;
					return createArchiveInputStream(sevenZConfig.getFile(), or(password, sevenZConfig.getPassword()));
				}
				catch (IOException e) {
					throw new RuntimeException(e);
				}
			}

		};
	}

	@VisibleForTesting
	protected ArchiveInputStream createArchiveInputStream(File file, byte[] password) throws IOException {
		return new ArchiveInputStreamSevenZImpl(new SevenZFile(file, password));
	}

}