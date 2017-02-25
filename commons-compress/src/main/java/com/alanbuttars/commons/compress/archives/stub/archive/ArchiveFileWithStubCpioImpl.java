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

import static com.alanbuttars.commons.compress.archives.util.Archives.CPIO;
import static com.alanbuttars.commons.compress.util.Optionals.or;

import java.io.File;
import java.io.OutputStream;

import org.apache.commons.compress.archivers.cpio.CpioArchiveOutputStream;

import com.alanbuttars.commons.compress.archives.config.output.ArchiveOutputStreamConfig;
import com.alanbuttars.commons.compress.archives.config.output.ArchiveOutputStreamConfigCpioImpl;
import com.alanbuttars.commons.compress.archives.output.ArchiveOutputStream;
import com.alanbuttars.commons.compress.archives.output.ArchiveOutputStreamImpl;
import com.alanbuttars.commons.compress.archives.util.Archives;
import com.alanbuttars.commons.util.annotations.VisibleForTesting;
import com.alanbuttars.commons.util.functions.Function;

/**
 * Extension of {@link ArchiveFileWithStub} for {@link Archives#CPIO} archives.
 * 
 * @author Alan Buttars
 *
 */
class ArchiveFileWithStubCpioImpl extends ArchiveFileWithStub {

	private short format;
	private int blockSize;
	private String encoding;

	ArchiveFileWithStubCpioImpl(File source) {
		super(source, CPIO);
	}

	public ArchiveFileWithStubCpioImpl withFormat(short format) {
		this.format = format;
		return this;
	}

	public ArchiveFileWithStubCpioImpl withBlockSize(int blockSize) {
		this.blockSize = blockSize;
		return this;
	}

	public ArchiveFileWithStubCpioImpl withEncoding(String encoding) {
		this.encoding = encoding;
		return this;
	}

	@Override
	protected Function<ArchiveOutputStreamConfig, ArchiveOutputStream> streamFunction() {
		return new Function<ArchiveOutputStreamConfig, ArchiveOutputStream>() {

			@Override
			public ArchiveOutputStream apply(ArchiveOutputStreamConfig config) {
				ArchiveOutputStreamConfigCpioImpl cpioConfig = (ArchiveOutputStreamConfigCpioImpl) config;
				return createArchiveOutputStream(//
						cpioConfig.getOutputStream(), //
						or(format, cpioConfig.getFormat()), //
						or(blockSize, cpioConfig.getBlockSize()), //
						or(encoding, cpioConfig.getEncoding()));
			}

		};
	}

	@VisibleForTesting
	protected ArchiveOutputStream createArchiveOutputStream(OutputStream outputStream, short format, int blockSize, String encoding) {
		return new ArchiveOutputStreamImpl(new CpioArchiveOutputStream(outputStream, format, blockSize, encoding));
	}

}
