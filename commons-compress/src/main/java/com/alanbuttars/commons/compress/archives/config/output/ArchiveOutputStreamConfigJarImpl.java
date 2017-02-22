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
import java.util.zip.ZipEntry;

import org.apache.commons.compress.archivers.zip.Zip64Mode;
import org.apache.commons.compress.archivers.zip.ZipArchiveOutputStream;
import org.apache.commons.compress.archivers.zip.ZipArchiveOutputStream.UnicodeExtraFieldPolicy;

import com.alanbuttars.commons.compress.archives.util.Archives;

/**
 * Extension of {@link ArchiveOutputStreamConfig} used for {@link Archives#ZIP} archives.
 * 
 * @author Alan Buttars
 *
 */
public class ArchiveOutputStreamConfigJarImpl extends ArchiveOutputStreamConfig {

	private String encoding;
	private boolean useLanguageEncoding;
	private boolean fallbackToUTF8;
	private int level;
	private int method;
	private String comment;
	private Zip64Mode zip64Mode;
	private UnicodeExtraFieldPolicy unicodeExtraFieldPolicy;

	public ArchiveOutputStreamConfigJarImpl(File file) {
		super(file);
		this.comment = "";
		this.encoding = "UTF8";
		this.fallbackToUTF8 = false;
		this.level = ZipArchiveOutputStream.DEFAULT_COMPRESSION;
		this.method = ZipEntry.DEFLATED;
		this.unicodeExtraFieldPolicy = UnicodeExtraFieldPolicy.NEVER;
		this.useLanguageEncoding = true;
		this.zip64Mode = Zip64Mode.AsNeeded;
	}

	public String getEncoding() {
		return encoding;
	}

	public void setEncoding(String encoding) {
		this.encoding = encoding;
	}

	public boolean useLanguageEncoding() {
		return useLanguageEncoding;
	}

	public void setUseLanguageEncoding(boolean useLanguageEncoding) {
		this.useLanguageEncoding = useLanguageEncoding;
	}

	public boolean fallbackToUTF8() {
		return fallbackToUTF8;
	}

	public void setFallbackToUTF8(boolean fallbackToUTF8) {
		this.fallbackToUTF8 = fallbackToUTF8;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public int getMethod() {
		return method;
	}

	public void setMethod(int method) {
		this.method = method;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public Zip64Mode getZip64Mode() {
		return zip64Mode;
	}

	public void setZip64Mode(Zip64Mode zip64Mode) {
		this.zip64Mode = zip64Mode;
	}

	public UnicodeExtraFieldPolicy getUnicodeExtraFieldPolicy() {
		return unicodeExtraFieldPolicy;
	}

	public void setUnicodeExtraFieldPolicy(UnicodeExtraFieldPolicy unicodeExtraFieldPolicy) {
		this.unicodeExtraFieldPolicy = unicodeExtraFieldPolicy;
	}

}
