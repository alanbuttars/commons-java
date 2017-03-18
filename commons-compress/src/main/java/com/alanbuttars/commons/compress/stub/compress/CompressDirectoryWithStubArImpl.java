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
 * Extension of {@link CompressDirectoryWithStub} for {@link Archives#AR} files. For details on the file format, see
 * <a href="https://en.wikipedia.org/wiki/Ar_(Unix)">https://en.wikipedia.org/wiki/Ar_(Unix)</a>.
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
		this.lastModified = Long.MAX_VALUE;
		this.longFileMode = ArArchiveOutputStream.LONGFILE_ERROR;
		this.mode = 33188;
		this.userId = 0;
	}

	/**
	 * Sets the numeric group ID for each file entry in the archive. By default, it is set to 0.
	 */
	public CompressDirectoryWithStubArImpl andGroupId(int groupId) {
		this.groupId = groupId;
		return this;
	}

	/**
	 * Sets the timestamp, in millis, of each file entry's last modified property. By default, this timestamp is
	 * calculated at the time the file entry is created.
	 */
	public CompressDirectoryWithStubArImpl andLastModified(long lastModified) {
		this.lastModified = lastModified;
		return this;
	}

	/**
	 * Sets the long file mode of the archive. See {@link ArArchiveOutputStream#setLongFileMode(int)}.
	 */
	public CompressDirectoryWithStubArImpl andLongFileMode(int longFileMode) {
		this.longFileMode = longFileMode;
		return this;
	}

	/**
	 * Sets the mode for each file entry in the archive. By default, it is set to
	 * {@link ArArchiveOutputStream#LONGFILE_ERROR}.
	 */
	public CompressDirectoryWithStubArImpl andMode(int mode) {
		this.mode = mode;
		return this;
	}

	/**
	 * Sets the numeric user ID for each file entry in the archive. By default, it is set to 0.
	 */
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
				if (lastModified == Long.MAX_VALUE) {
					return new ArArchiveEntry(entryName, fileSize, userId, groupId, mode, System.currentTimeMillis() / 1000);
				}
				return new ArArchiveEntry(entryName, fileSize, userId, groupId, mode, lastModified);
			}

		};
	}

}
