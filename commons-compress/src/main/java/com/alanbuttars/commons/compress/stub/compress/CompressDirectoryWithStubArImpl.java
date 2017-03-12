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

import static com.alanbuttars.commons.compress.archives.util.Archives.AR;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.ar.ArArchiveEntry;
import org.apache.commons.compress.archivers.ar.ArArchiveOutputStream;

import com.alanbuttars.commons.compress.archives.output.ArchiveOutputStream;
import com.alanbuttars.commons.compress.archives.output.ArchiveOutputStreamImpl;
import com.alanbuttars.commons.compress.archives.util.Archives;
import com.alanbuttars.commons.util.annotations.VisibleForTesting;
import com.alanbuttars.commons.util.functions.BiFunction;
import com.alanbuttars.commons.util.functions.Function;

/**
 * Extension of {@link CompressDirectoryWithStub} for {@link Archives#AR} files.
 * 
 * @author Alan Buttars
 *
 */
public class CompressDirectoryWithStubArImpl extends CompressDirectoryWithStub {

	private int groupId;
	private long lastModified;
	private int longFileMode;
	private int mode;
	private int userId;

	CompressDirectoryWithStubArImpl(File source) {
		super(source, AR);
		this.groupId = 0;
		this.lastModified = System.currentTimeMillis() / 1000;
		this.longFileMode = ArArchiveOutputStream.LONGFILE_ERROR;
		this.mode = 33188;
		this.userId = 0;
	}

	public CompressDirectoryWithStubArImpl andGroupId(int groupId) {
		this.groupId = groupId;
		return this;
	}

	public CompressDirectoryWithStubArImpl andLastModified(long lastModified) {
		this.lastModified = lastModified;
		return this;
	}

	public CompressDirectoryWithStubArImpl andLongFileMode(int longFileMode) {
		this.longFileMode = longFileMode;
		return this;
	}

	public CompressDirectoryWithStubArImpl andMode(int mode) {
		this.mode = mode;
		return this;
	}

	public CompressDirectoryWithStubArImpl andUserId(int userId) {
		this.userId = userId;
		return this;
	}

	@Override
	protected Function<File, ArchiveOutputStream> compressionFunction() {
		return new Function<File, ArchiveOutputStream>() {

			@Override
			public ArchiveOutputStream apply(File file) {
				try {
					return createArchiveOutputStream(file, longFileMode);
				}
				catch (IOException e) {
					throw new RuntimeException(e);
				}
			}

		};
	}

	@VisibleForTesting
	protected ArchiveOutputStream createArchiveOutputStream(File file, int longFileMode) throws IOException {
		FileOutputStream fileOutputStream = new FileOutputStream(file);
		ArArchiveOutputStream archiveOutputStream = new ArArchiveOutputStream(fileOutputStream);
		archiveOutputStream.setLongFileMode(longFileMode);
		return new ArchiveOutputStreamImpl(archiveOutputStream);
	}

	@Override
	protected BiFunction<String, Long, ArchiveEntry> entryFunction() {
		return new BiFunction<String, Long, ArchiveEntry>() {

			@Override
			public ArchiveEntry apply(String entryName, Long fileSize) {
				return new ArArchiveEntry(entryName, fileSize, userId, groupId, mode, lastModified);
			}

		};
	}

}
