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

import static com.alanbuttars.commons.compress.archives.util.Archives.SEVENZ;

import java.io.File;
import java.io.IOException;
import java.util.Collections;

import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.sevenz.SevenZArchiveEntry;
import org.apache.commons.compress.archivers.sevenz.SevenZMethod;
import org.apache.commons.compress.archivers.sevenz.SevenZMethodConfiguration;
import org.apache.commons.compress.archivers.sevenz.SevenZOutputFile;

import com.alanbuttars.commons.compress.archives.output.ArchiveOutputStream;
import com.alanbuttars.commons.compress.archives.output.ArchiveOutputStreamSevenZImpl;
import com.alanbuttars.commons.compress.archives.util.Archives;
import com.alanbuttars.commons.util.annotations.VisibleForTesting;
import com.alanbuttars.commons.util.functions.BiFunction;
import com.alanbuttars.commons.util.functions.Function;

/**
 * Extension of {@link CompressDirectoryWithStub} for {@link Archives#SEVENZ} files.
 * 
 * @author Alan Buttars
 *
 */
public class CompressDirectoryWithStub7zImpl extends CompressDirectoryWithStub {

	private Iterable<? extends SevenZMethodConfiguration> contentMethods;

	CompressDirectoryWithStub7zImpl(File source) {
		super(source, SEVENZ);
		this.contentMethods = Collections.singletonList(new SevenZMethodConfiguration(SevenZMethod.LZMA2));
	}

	public CompressDirectoryWithStub7zImpl andContentMethods(Iterable<? extends SevenZMethodConfiguration> contentMethods) {
		this.contentMethods = contentMethods;
		return this;
	}

	@Override
	protected Function<File, ArchiveOutputStream> compressionFunction() {
		return new Function<File, ArchiveOutputStream>() {

			@Override
			public ArchiveOutputStream apply(File file) {
				try {
					return createArchiveOutputStream(file, contentMethods);
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

	@Override
	protected BiFunction<String, Long, ArchiveEntry> entryFunction() {
		return new BiFunction<String, Long, ArchiveEntry>() {

			@Override
			public ArchiveEntry apply(String entryName, Long fileSize) {
				SevenZArchiveEntry entry = new SevenZArchiveEntry();
				entry.setName(entryName);
				return entry;
			}

		};
	}

}
