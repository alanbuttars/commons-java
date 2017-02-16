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
package com.alanbuttars.commons.compress.files.config.input;

import java.io.InputStream;

import org.apache.commons.compress.compressors.snappy.FramedSnappyDialect;
import org.apache.commons.compress.compressors.snappy.SnappyCompressorInputStream;

import com.alanbuttars.commons.compress.files.util.Files;

/**
 * Extension of {@link FileInputStreamConfig} used for {@link Files#SNAPPY} files.
 * 
 * @author Alan Buttars
 *
 */
public class FileInputStreamConfigSnappyImpl extends FileInputStreamConfig {

	public static enum Format {
		STANDARD, //
		FRAMED;
	}

	private Format format;
	private FramedSnappyDialect dialect;
	private int blockSize;

	public FileInputStreamConfigSnappyImpl(InputStream inputStream) {
		super(inputStream);
		this.format = Format.STANDARD;
		this.dialect = FramedSnappyDialect.STANDARD;
		this.blockSize = SnappyCompressorInputStream.DEFAULT_BLOCK_SIZE;
	}

	public Format getFormat() {
		return format;
	}

	public void setFormat(Format format) {
		this.format = format;
	}

	public FramedSnappyDialect getDialect() {
		return dialect;
	}

	public void setDialect(FramedSnappyDialect dialect) {
		this.dialect = dialect;
	}

	public int getBlockSize() {
		return blockSize;
	}

	public void setBlockSize(int blockSize) {
		this.blockSize = blockSize;
	}

}