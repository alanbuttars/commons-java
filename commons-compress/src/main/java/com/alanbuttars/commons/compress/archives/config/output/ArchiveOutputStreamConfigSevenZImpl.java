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
import java.util.Collections;

import org.apache.commons.compress.archivers.sevenz.SevenZMethod;
import org.apache.commons.compress.archivers.sevenz.SevenZMethodConfiguration;

import com.alanbuttars.commons.compress.archives.util.Archives;

/**
 * Extension of {@link ArchiveOutputStreamConfig} used for {@link Archives#SEVENZ} archives.
 * 
 * @author Alan Buttars
 *
 */
public class ArchiveOutputStreamConfigSevenZImpl extends ArchiveOutputStreamConfig {

	private Iterable<? extends SevenZMethodConfiguration> contentMethods;

	public ArchiveOutputStreamConfigSevenZImpl(File file) {
		super(file);
		this.contentMethods = Collections.singletonList(new SevenZMethodConfiguration(SevenZMethod.LZMA2));
	}

	public File getFile() {
		return file;
	}

	public Iterable<? extends SevenZMethodConfiguration> getContentMethods() {
		return contentMethods;
	}

	public void setContentMethods(Iterable<? extends SevenZMethodConfiguration> contentMethods) {
		this.contentMethods = contentMethods;
	}

}