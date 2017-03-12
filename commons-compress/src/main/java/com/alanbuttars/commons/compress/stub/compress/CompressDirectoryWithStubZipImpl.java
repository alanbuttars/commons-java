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
package com.alanbuttars.commons.compress.stub.compress;

import static com.alanbuttars.commons.compress.archives.util.Archives.ZIP;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;

import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.zip.Zip64Mode;
import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipArchiveOutputStream;
import org.apache.commons.compress.archivers.zip.ZipArchiveOutputStream.UnicodeExtraFieldPolicy;

import com.alanbuttars.commons.compress.archives.output.ArchiveOutputStream;
import com.alanbuttars.commons.compress.archives.output.ArchiveOutputStreamImpl;
import com.alanbuttars.commons.compress.archives.util.Archives;
import com.alanbuttars.commons.util.annotations.VisibleForTesting;
import com.alanbuttars.commons.util.functions.BiFunction;
import com.alanbuttars.commons.util.functions.Function;

/**
 * Extension of {@link CompressDirectoryWithStub} for {@link Archives#ZIP} files.
 * 
 * @author Alan Buttars
 *
 */
public class CompressDirectoryWithStubZipImpl extends CompressDirectoryWithStub {

	private String comment;
	private String encoding;
	private boolean fallbackToUTF8;
	private int level;
	private int method;
	private UnicodeExtraFieldPolicy unicodeExtraFieldPolicy;
	private boolean useLanguageEncoding;
	private Zip64Mode zip64Mode;

	CompressDirectoryWithStubZipImpl(File source) {
		super(source, ZIP);
		this.comment = "";
		this.encoding = "UTF8";
		this.fallbackToUTF8 = false;
		this.level = ZipArchiveOutputStream.DEFAULT_COMPRESSION;
		this.method = ZipEntry.DEFLATED;
		this.unicodeExtraFieldPolicy = UnicodeExtraFieldPolicy.NEVER;
		this.useLanguageEncoding = true;
		this.zip64Mode = Zip64Mode.AsNeeded;
	}

	public CompressDirectoryWithStubZipImpl andComment(String comment) {
		this.comment = comment;
		return this;
	}

	public CompressDirectoryWithStubZipImpl andEncoding(String encoding) {
		this.encoding = encoding;
		return this;
	}

	public CompressDirectoryWithStubZipImpl andFallbackToUTF8(boolean fallbackToUTF8) {
		this.fallbackToUTF8 = fallbackToUTF8;
		return this;
	}

	public CompressDirectoryWithStubZipImpl andLevel(int level) {
		this.level = level;
		return this;
	}

	public CompressDirectoryWithStubZipImpl andMethod(int method) {
		this.method = method;
		return this;
	}

	public CompressDirectoryWithStubZipImpl andUnicodeExtraFieldPolicy(UnicodeExtraFieldPolicy unicodeExtraFieldPolicy) {
		this.unicodeExtraFieldPolicy = unicodeExtraFieldPolicy;
		return this;
	}

	public CompressDirectoryWithStubZipImpl andUseLanguageEncoding(boolean useLanguageEncoding) {
		this.useLanguageEncoding = useLanguageEncoding;
		return this;
	}

	public CompressDirectoryWithStubZipImpl andZip64Mode(Zip64Mode zip64Mode) {
		this.zip64Mode = zip64Mode;
		return this;
	}

	@Override
	protected Function<File, ArchiveOutputStream> compressionFunction() {
		return new Function<File, ArchiveOutputStream>() {

			@Override
			public ArchiveOutputStream apply(File file) {
				try {
					return createArchiveOutputStream(file, comment, encoding, fallbackToUTF8, level, method, unicodeExtraFieldPolicy, useLanguageEncoding, zip64Mode);
				}
				catch (IOException e) {
					throw new RuntimeException(e);
				}
			}

		};
	}

	@VisibleForTesting
	protected ArchiveOutputStream createArchiveOutputStream(File file, //
			String comment, //
			String encoding, //
			boolean fallbackToUTF8, //
			int level, //
			int method, //
			UnicodeExtraFieldPolicy unicodeExtraFieldPolicy, //
			boolean useLanguageEncoding, //
			Zip64Mode zip64Mode) throws IOException {
		FileOutputStream fileOutputStream = new FileOutputStream(file);
		ZipArchiveOutputStream archiveOutputStream = new ZipArchiveOutputStream(fileOutputStream);
		archiveOutputStream.setComment(comment);
		archiveOutputStream.setCreateUnicodeExtraFields(unicodeExtraFieldPolicy);
		archiveOutputStream.setEncoding(encoding);
		archiveOutputStream.setFallbackToUTF8(fallbackToUTF8);
		archiveOutputStream.setLevel(level);
		archiveOutputStream.setMethod(method);
		archiveOutputStream.setUseLanguageEncodingFlag(useLanguageEncoding);
		archiveOutputStream.setUseZip64(zip64Mode);
		return new ArchiveOutputStreamImpl(archiveOutputStream);
	}

	@Override
	protected BiFunction<String, Long, ArchiveEntry> entryFunction() {
		return new BiFunction<String, Long, ArchiveEntry>() {

			@Override
			public ArchiveEntry apply(String entryName, Long fileSize) {
				return new ZipArchiveEntry(entryName);
			}

		};
	}

}
