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
package com.alanbuttars.commons.compress.config.entry;

import org.apache.commons.compress.archivers.cpio.CpioConstants;

import com.alanbuttars.commons.compress.util.Archives;

/**
 * Extension of {@link ArchiveEntryConfig} used for {@link Archives#CPIO} archives.
 * 
 * @author Alan Buttars
 *
 */
public class ArchiveEntryConfigCpioImpl extends ArchiveEntryConfig {

	private short format;

	public ArchiveEntryConfigCpioImpl(String entryName) {
		super(entryName);
		this.format = CpioConstants.FORMAT_NEW;
	}

	public short getFormat() {
		return format;
	}

	public void setFormat(short format) {
		this.format = format;
	}

}
