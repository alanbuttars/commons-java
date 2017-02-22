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
package com.alanbuttars.commons.compress.archives.config.input;

import java.io.File;

import com.alanbuttars.commons.compress.archives.util.Archives;

/**
 * Extension of {@link ArchiveInputStreamConfig} used for {@link Archives#SEVENZ} archives.
 * 
 * @author Alan Buttars
 *
 */
public class ArchiveInputStreamConfigSevenZImpl extends ArchiveInputStreamConfig {

	private byte[] password;

	public ArchiveInputStreamConfigSevenZImpl(File file) {
		super(file);
		this.password = null;
	}
	
	public File getFile() {
		return file;
	}

	public byte[] getPassword() {
		return password;
	}

	public void setPassword(byte[] password) {
		this.password = password;
	}

}