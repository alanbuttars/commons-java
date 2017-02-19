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

import com.alanbuttars.commons.util.annotations.VisibleForTesting;

/**
 * An interface encompassing a writeable archive output stream.
 * 
 * @author Alan Buttars
 *
 */
public interface ArchiveOutputStream extends Closeable {

	/**
	 * Inserts a new archive entry into the stream.
	 * 
	 * @param entry
	 *            Non-null entry
	 * @throws IOException
	 */
	public void putArchiveEntry(ArchiveEntry entry) throws IOException;

	/**
	 * Closes the archive entry which is currently open.
	 * 
	 * @throws IOException
	 */
	public void closeArchiveEntry() throws IOException;

	/**
	 * Writes bytes to an open archive entry.
	 * 
	 * @throws IOException
	 */
	public void write(byte[] content, int offset, int length) throws IOException;

	/**
	 * Flushes the current file buffer.
	 * 
	 * @throws IOException
	 */
	public void flush() throws IOException;
	
	/**
	 * Returns the output stream.
	 */
	@VisibleForTesting
	public Closeable getStream();

}
