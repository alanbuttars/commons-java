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
package com.alanbuttars.commons.compress.config.input;

import java.io.InputStream;

import org.apache.commons.compress.archivers.tar.TarConstants;

import com.alanbuttars.commons.compress.util.Archives;

/**
 * Extension of {@link ArchiveInputStreamConfig} used for {@link Archives#TAR} archives.
 * 
 * @author Alan Buttars
 *
 */
public class ArchiveInputStreamConfigTarImpl extends ArchiveInputStreamConfig {

	private String encoding;
	private int blockSize;
	private int recordSize;

	public ArchiveInputStreamConfigTarImpl(InputStream inputStream) {
		super(inputStream);
		this.encoding = null;
		this.blockSize = TarConstants.DEFAULT_BLKSIZE;
		this.recordSize = TarConstants.DEFAULT_RCDSIZE;
	}

	public String getEncoding() {
		return encoding;
	}

	public void setEncoding(String encoding) {
		this.encoding = encoding;
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

}
