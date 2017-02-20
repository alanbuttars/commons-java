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

import org.apache.commons.compress.archivers.cpio.CpioArchiveInputStream;

import com.alanbuttars.commons.compress.archives.config.input.ArchiveInputStreamConfig;
import com.alanbuttars.commons.compress.archives.config.input.ArchiveInputStreamConfigCpioImpl;
import com.alanbuttars.commons.compress.archives.input.ArchiveInputStream;
import com.alanbuttars.commons.compress.archives.input.ArchiveInputStreamImpl;
import com.alanbuttars.commons.compress.archives.util.Archives;
import com.alanbuttars.commons.util.annotations.VisibleForTesting;
import com.alanbuttars.commons.util.functions.Function;

/**
 * Extension of {@link ExtractFileAsStub} for {@link Archives#CPIO} archives.
 * 
 * @author Alan Buttars
 *
 */
class ExtractFileAsStubCpioImpl extends ExtractFileAsStub {

	private int blockSize;
	private String encoding;

	ExtractFileAsStubCpioImpl(File archive) {
		super(archive, Archives.CPIO);
	}

	public ExtractFileAsStubCpioImpl withBlockSize(int blockSize) {
		this.blockSize = blockSize;
		return this;
	}

	public ExtractFileAsStubCpioImpl withEncoding(String encoding) {
		this.encoding = encoding;
		return this;
	}

	@Override
	protected Function<ArchiveInputStreamConfig, ArchiveInputStream> streamFunction() {
		return new Function<ArchiveInputStreamConfig, ArchiveInputStream>() {

			@Override
			public ArchiveInputStream apply(ArchiveInputStreamConfig config) {
				ArchiveInputStreamConfigCpioImpl cpioConfig = (ArchiveInputStreamConfigCpioImpl) config;
				return createArchiveInputStream(cpioConfig.getInputStream(), or(blockSize, cpioConfig.getBlockSize()), or(encoding, cpioConfig.getEncoding()));
			}

		};
	}

	@VisibleForTesting
	protected ArchiveInputStream createArchiveInputStream(InputStream inputStream, int blockSize, String encoding) {
		return new ArchiveInputStreamImpl(new CpioArchiveInputStream(inputStream, blockSize, encoding));
	}

}
