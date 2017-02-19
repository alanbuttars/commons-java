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

import java.io.File;

import org.apache.commons.compress.archivers.cpio.CpioConstants;
import org.apache.commons.compress.utils.CharsetNames;

import com.alanbuttars.commons.compress.archives.util.Archives;

/**
 * Extension of {@link ArchiveOutputStreamConfig} used for {@link Archives#CPIO} archives.
 * 
 * @author Alan Buttars
 *
 */
public class ArchiveOutputStreamConfigCpioImpl extends ArchiveOutputStreamConfig {

	private String encoding;
	private short format;
	private int blockSize;

	public ArchiveOutputStreamConfigCpioImpl(File file) {
		super(file);
		this.encoding = CharsetNames.US_ASCII;
		this.blockSize = CpioConstants.BLOCK_SIZE;
		this.format = CpioConstants.FORMAT_NEW;
	}

	public String getEncoding() {
		return encoding;
	}

	public void setEncoding(String encoding) {
		this.encoding = encoding;
	}

	public short getFormat() {
		return format;
	}

	public void setFormat(short format) {
		this.format = format;
	}

	public int getBlockSize() {
		return blockSize;
	}

	public void setBlockSize(int blockSize) {
		this.blockSize = blockSize;
	}

}
