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
package com.alanbuttars.commons.compress.archives.stub.archive;

import static com.alanbuttars.commons.compress.archives.util.Archives.ZIP;
import static com.alanbuttars.commons.compress.util.Optionals.or;

import java.io.File;
import java.io.OutputStream;

import org.apache.commons.compress.archivers.zip.Zip64Mode;
import org.apache.commons.compress.archivers.zip.ZipArchiveOutputStream;
import org.apache.commons.compress.archivers.zip.ZipArchiveOutputStream.UnicodeExtraFieldPolicy;

import com.alanbuttars.commons.compress.archives.config.output.ArchiveOutputStreamConfig;
import com.alanbuttars.commons.compress.archives.config.output.ArchiveOutputStreamConfigZipImpl;
import com.alanbuttars.commons.compress.archives.output.ArchiveOutputStream;
import com.alanbuttars.commons.compress.archives.output.ArchiveOutputStreamImpl;
import com.alanbuttars.commons.compress.archives.util.Archives;
import com.alanbuttars.commons.util.annotations.VisibleForTesting;
import com.alanbuttars.commons.util.functions.Function;

/**
 * Extension of {@link ArchiveFileWithStub} for {@link Archives#JAR} archives.
 * 
 * @author Alan Buttars
 *
 */
class ArchiveFileWithStubZipImpl extends ArchiveFileWithStub {

	private String comment;
	private UnicodeExtraFieldPolicy unicodeExtraFieldPolicy;
	private String encoding;
	private Boolean fallbackToUTF8;
	private int level;
	private int method;
	private Boolean useLanguageEncoding;
	private Zip64Mode zip64Mode;

	ArchiveFileWithStubZipImpl(File source) {
		super(source, ZIP);
	}

	public ArchiveFileWithStubZipImpl withComment(String comment) {
		this.comment = comment;
		return this;
	}

	public ArchiveFileWithStubZipImpl withUnicodeExtraFieldPolicy(UnicodeExtraFieldPolicy unicodeExtraFieldPolicy) {
		this.unicodeExtraFieldPolicy = unicodeExtraFieldPolicy;
		return this;
	}

	public ArchiveFileWithStubZipImpl withEncoding(String encoding) {
		this.encoding = encoding;
		return this;
	}

	public ArchiveFileWithStubZipImpl fallbackToUTF8(boolean fallbackToUTF8) {
		this.fallbackToUTF8 = fallbackToUTF8;
		return this;
	}

	public ArchiveFileWithStubZipImpl withLevel(int level) {
		this.level = level;
		return this;
	}

	public ArchiveFileWithStubZipImpl withMethod(int method) {
		this.method = method;
		return this;
	}

	public ArchiveFileWithStubZipImpl useLanguageEncoding(boolean useLanguageEncoding) {
		this.useLanguageEncoding = useLanguageEncoding;
		return this;
	}

	public ArchiveFileWithStubZipImpl withZip64Mode(Zip64Mode zip64Mode) {
		this.zip64Mode = zip64Mode;
		return this;
	}

	@Override
	protected Function<ArchiveOutputStreamConfig, ArchiveOutputStream> streamFunction() {
		return new Function<ArchiveOutputStreamConfig, ArchiveOutputStream>() {

			@Override
			public ArchiveOutputStream apply(ArchiveOutputStreamConfig config) {
				ArchiveOutputStreamConfigZipImpl zipConfig = (ArchiveOutputStreamConfigZipImpl) config;
				return createArchiveOutputStream(zipConfig.getOutputStream(), //
						or(comment, zipConfig.getComment()), //
						or(unicodeExtraFieldPolicy, zipConfig.getUnicodeExtraFieldPolicy()), //
						or(encoding, zipConfig.getEncoding()), //
						or(fallbackToUTF8, zipConfig.fallbackToUTF8()), //
						or(level, zipConfig.getLevel()), //
						or(method, zipConfig.getMethod()), //
						or(useLanguageEncoding, zipConfig.useLanguageEncoding()), //
						or(zip64Mode, zipConfig.getZip64Mode()));
			}

		};
	}

	@VisibleForTesting
	protected ArchiveOutputStream createArchiveOutputStream(OutputStream outputStream, //
			String comment, //
			UnicodeExtraFieldPolicy unicodeExtraFieldPolicy, //
			String encoding, //
			boolean fallbackToUTF8, //
			int level, //
			int method, //
			boolean useLanguageEncoding, //
			Zip64Mode zip64Mode) {
		ZipArchiveOutputStream stream = new ZipArchiveOutputStream(outputStream);
		stream.setComment(comment);
		stream.setCreateUnicodeExtraFields(unicodeExtraFieldPolicy);
		stream.setEncoding(encoding);
		stream.setFallbackToUTF8(fallbackToUTF8);
		stream.setLevel(level);
		stream.setMethod(method);
		stream.setUseLanguageEncodingFlag(useLanguageEncoding);
		stream.setUseZip64(zip64Mode);
		return new ArchiveOutputStreamImpl(stream);
	}

}
