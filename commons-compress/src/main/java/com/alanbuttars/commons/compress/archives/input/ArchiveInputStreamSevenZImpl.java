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
package com.alanbuttars.commons.compress.archives.input;

import java.io.Closeable;
import java.io.IOException;

import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.sevenz.SevenZFile;

/**
 * Extension of {@link ArchiveInputStream} which wraps {@link SevenZFile}s.
 * 
 * @author Alan Buttars
 *
 */
public class ArchiveInputStreamSevenZImpl implements ArchiveInputStream {

	private final SevenZFile sevenZFile;

	public ArchiveInputStreamSevenZImpl(SevenZFile sevenZFile) {
		this.sevenZFile = sevenZFile;
	}

	@Override
	public int read(byte[] content) throws IOException {
		return sevenZFile.read(content);
	}

	@Override
	public ArchiveEntry getNextEntry() throws IOException {
		return sevenZFile.getNextEntry();
	}

	@Override
	public void close() throws IOException {
		sevenZFile.close();
	}

	@Override
	public Closeable getStream() {
		return sevenZFile;
	}

}
