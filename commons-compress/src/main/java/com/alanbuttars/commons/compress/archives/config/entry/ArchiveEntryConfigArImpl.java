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
 * Extension of {@link ArchiveEntryConfig} used for {@link Archives#AR} archives.
 * 
 * @author Alan Buttars
 *
 */
public class ArchiveEntryConfigArImpl extends ArchiveEntryConfig {

	private int userId;
	private int groupId;
	private int mode;
	private long length;
	private long lastModified;

	public ArchiveEntryConfigArImpl(String entryName, long length) {
		super(entryName);
		this.userId = 0;
		this.groupId = 0;
		this.mode = 33188;
		this.length = length;
		this.lastModified = System.currentTimeMillis() / 1000;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public int getGroupId() {
		return groupId;
	}

	public void setGroupId(int groupId) {
		this.groupId = groupId;
	}

	public int getMode() {
		return mode;
	}

	public void setMode(int mode) {
		this.mode = mode;
	}

	public long getLength() {
		return length;
	}

	public long getLastModified() {
		return lastModified;
	}

	public void setLastModified(long lastModified) {
		this.lastModified = lastModified;
	}

}
