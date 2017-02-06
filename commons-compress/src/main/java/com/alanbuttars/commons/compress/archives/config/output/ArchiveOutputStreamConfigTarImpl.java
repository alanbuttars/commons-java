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
package com.alanbuttars.commons.compress.archives.config.output;

import java.io.OutputStream;

import org.apache.commons.compress.archivers.tar.TarArchiveOutputStream;
import org.apache.commons.compress.archivers.tar.TarConstants;

import com.alanbuttars.commons.compress.archives.util.Archives;

/**
 * Extension of {@link ArchiveOutputStreamConfig} used for {@link Archives#TAR} archives.
 * 
 * @author Alan Buttars
 *
 */
public class ArchiveOutputStreamConfigTarImpl extends ArchiveOutputStreamConfig {

	private String encoding;
	private int longFileMode;
	private int bigNumberMode;
	private int blockSize;
	private int recordSize;
	private boolean addPaxHeadersForNonAsciiNames;

	public ArchiveOutputStreamConfigTarImpl(OutputStream outputStream) {
		super(outputStream);
		this.encoding = null;
		this.addPaxHeadersForNonAsciiNames = false;
		this.bigNumberMode = TarArchiveOutputStream.BIGNUMBER_ERROR;
		this.blockSize = TarConstants.DEFAULT_BLKSIZE;
		this.longFileMode = TarArchiveOutputStream.LONGFILE_ERROR;
		this.recordSize = TarConstants.DEFAULT_RCDSIZE;
	}

	public String getEncoding() {
		return encoding;
	}

	public void setEncoding(String encoding) {
		this.encoding = encoding;
	}

	public int getLongFileMode() {
		return longFileMode;
	}

	public void setLongFileMode(int longFileMode) {
		this.longFileMode = longFileMode;
	}

	public int getBigNumberMode() {
		return bigNumberMode;
	}

	public void setBigNumberMode(int bigNumberMode) {
		this.bigNumberMode = bigNumberMode;
	}

	public int getBlockSize() {
		return blockSize;
	}

	public void setBlockSize(int blockSize) {
		this.blockSize = blockSize;
	}

	public int getRecordSize() {
		return recordSize;
	}

	public void setRecordSize(int recordSize) {
		this.recordSize = recordSize;
	}

	public boolean addPaxHeadersForNonAsciiNames() {
		return addPaxHeadersForNonAsciiNames;
	}

	public void setAddPaxHeadersForNonAsciiNames(boolean addPaxHeadersForNonAsciiNames) {
		this.addPaxHeadersForNonAsciiNames = addPaxHeadersForNonAsciiNames;
	}

}
