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

/**
 * Extension of {@link ArchiveOutputStreamImpl} which wraps {@link ArchiveOutputStream}s.
 * 
 * @author Alan Buttars
 *
 */
public class ArchiveOutputStreamImpl implements ArchiveOutputStream {

	private final org.apache.commons.compress.archivers.ArchiveOutputStream archiveOutputStream;

	/**
	 * @param archiveOutputStream
	 *            Non-null Apache archive output stream
	 */
	public ArchiveOutputStreamImpl(org.apache.commons.compress.archivers.ArchiveOutputStream archiveOutputStream) {
		this.archiveOutputStream = archiveOutputStream;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void close() throws IOException {
		archiveOutputStream.close();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void putArchiveEntry(ArchiveEntry entry) throws IOException {
		archiveOutputStream.putArchiveEntry(entry);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void closeArchiveEntry() throws IOException {
		archiveOutputStream.closeArchiveEntry();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void write(byte[] content, int offset, int length) throws IOException {
		archiveOutputStream.write(content, offset, length);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void flush() throws IOException {
		archiveOutputStream.flush();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Closeable getStream() {
		return archiveOutputStream;
	}
}
