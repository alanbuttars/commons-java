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

import static com.alanbuttars.commons.compress.archives.util.Archives.AR;
import static com.alanbuttars.commons.compress.util.Optionals.or;

import java.io.File;
import java.io.OutputStream;

import org.apache.commons.compress.archivers.ar.ArArchiveOutputStream;

import com.alanbuttars.commons.compress.archives.config.output.ArchiveOutputStreamConfig;
import com.alanbuttars.commons.compress.archives.config.output.ArchiveOutputStreamConfigArImpl;
import com.alanbuttars.commons.compress.archives.output.ArchiveOutputStream;
import com.alanbuttars.commons.compress.archives.output.ArchiveOutputStreamImpl;
import com.alanbuttars.commons.compress.archives.util.Archives;
import com.alanbuttars.commons.util.annotations.VisibleForTesting;
import com.alanbuttars.commons.util.functions.Function;

/**
 * Extension of {@link ArchiveFileWithStub} for {@link Archives#AR} archives.
 * 
 * @author Alan Buttars
 *
 */
class ArchiveFileWithStubArImpl extends ArchiveFileWithStub {

	private int longFileMode;

	ArchiveFileWithStubArImpl(File source) {
		super(source, AR);
	}

	public ArchiveFileWithStubArImpl withLongFileMode(int longFileMode) {
		this.longFileMode = longFileMode;
		return this;
	}

	@Override
	protected Function<ArchiveOutputStreamConfig, ArchiveOutputStream> streamFunction() {
		return new Function<ArchiveOutputStreamConfig, ArchiveOutputStream>() {

			@Override
			public ArchiveOutputStream apply(ArchiveOutputStreamConfig config) {
				ArchiveOutputStreamConfigArImpl arConfig = (ArchiveOutputStreamConfigArImpl) config;
				return createArchiveOutputStream(arConfig.getOutputStream(), or(longFileMode, arConfig.getLongFileMode()));
			}

		};
	}

	@VisibleForTesting
	protected ArchiveOutputStream createArchiveOutputStream(OutputStream outputStream, int longFileMode) {
		ArArchiveOutputStream stream = new ArArchiveOutputStream(outputStream);
		stream.setLongFileMode(longFileMode);
		return new ArchiveOutputStreamImpl(stream);
	}

}
