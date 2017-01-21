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

import com.alanbuttars.commons.compress.util.Archives;

/**
 * Extension of {@link ArchiveInputStreamConfig} used for {@link Archives#ZIP} archives.
 * 
 * @author Alan Buttars
 *
 */
public class ArchiveInputStreamConfigZipImpl extends ArchiveInputStreamConfig {

	private String encoding;
	private boolean useUnicodeExtraFields;
	private boolean allowStoredEntriesWithDataDescriptor;

	public ArchiveInputStreamConfigZipImpl(InputStream inputStream) {
		super(inputStream);
		this.encoding = "UTF8";
		this.allowStoredEntriesWithDataDescriptor = false;
		this.useUnicodeExtraFields = true;
	}

	public String getEncoding() {
		return encoding;
	}

	public void setEncoding(String encoding) {
		this.encoding = encoding;
	}

	public boolean useUnicodeExtraFields() {
		return useUnicodeExtraFields;
	}

	public void setUseUnicodeExtraFields(boolean useUnicodeExtraFields) {
		this.useUnicodeExtraFields = useUnicodeExtraFields;
	}

	public boolean allowStoredEntriesWithDataDescriptor() {
		return allowStoredEntriesWithDataDescriptor;
	}

	public void setAllowStoredEntriesWithDataDescriptor(boolean allowStoredEntriesWithDataDescriptor) {
		this.allowStoredEntriesWithDataDescriptor = allowStoredEntriesWithDataDescriptor;
	}
}