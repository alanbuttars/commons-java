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
package com.alanbuttars.commons.compress.archives.output;

import java.io.Closeable;
import java.io.IOException;

import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.sevenz.SevenZOutputFile;

/**
 * Extension of {@link ArchiveOutputStreamImpl} which wraps {@link SevenZOutputFile}s.
 * 
 * @author Alan Buttars
 *
 */
public class ArchiveOutputStream7zImpl implements ArchiveOutputStream {

	private final SevenZOutputFile sevenZFile;

	/**
	 * @param sevenZFile
	 *            Non-null Apache 7z object
	 */
	public ArchiveOutputStream7zImpl(SevenZOutputFile sevenZFile) {
		this.sevenZFile = sevenZFile;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void close() throws IOException {
		sevenZFile.close();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void putArchiveEntry(ArchiveEntry entry) throws IOException {
		sevenZFile.putArchiveEntry(entry);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void closeArchiveEntry() throws IOException {
		sevenZFile.closeArchiveEntry();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void write(byte[] content, int offset, int length) throws IOException {
		sevenZFile.write(content, offset, length);
	}

	/**
	 * {@inheritDoc} This implementation does nothing.
	 */
	@Override
	public void flush() throws IOException {
	}

	@Override
	public Closeable getStream() {
		return sevenZFile;
	}
}
