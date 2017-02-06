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
package com.alanbuttars.commons.compress.archives.config.entry;

import com.alanbuttars.commons.compress.archives.util.Archives;

/**
 * Extension of {@link ArchiveEntryConfig} used for {@link Archives#DUMP} archives.
 * 
 * @author Alan Buttars
 *
 */
public class ArchiveEntryConfigDumpImpl extends ArchiveEntryConfig {

	private String simpleName;

	public ArchiveEntryConfigDumpImpl(String entryName) {
		super(entryName);
	}

	public String getSimpleName() {
		return simpleName;
	}

	public void setSimpleName(String simpleName) {
		this.simpleName = simpleName;
	}

}
