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

import static com.alanbuttars.commons.compress.archives.util.Archives.JAR;

import java.io.File;
import java.io.OutputStream;

import org.apache.commons.compress.archivers.jar.JarArchiveOutputStream;
import org.apache.commons.compress.archivers.zip.Zip64Mode;
import org.apache.commons.compress.archivers.zip.ZipArchiveOutputStream.UnicodeExtraFieldPolicy;

import com.alanbuttars.commons.compress.archives.config.output.ArchiveOutputStreamConfig;
import com.alanbuttars.commons.compress.archives.config.output.ArchiveOutputStreamConfigJarImpl;
import com.alanbuttars.commons.compress.archives.output.ArchiveOutputStream;
import com.alanbuttars.commons.compress.archives.output.ArchiveOutputStreamImpl;
import com.alanbuttars.commons.compress.archives.util.Archives;
import com.alanbuttars.commons.util.annotations.VisibleForTesting;
import com.alanbuttars.commons.util.functions.Function;

/**
 * Extension of {@link ArchiveFileAsStub} for {@link Archives#JAR} archives.
 * 
 * @author Alan Buttars
 *
 */
class ArchiveFileAsStubJarImpl extends ArchiveFileAsStub {

	private String encoding;
	private String comment;
	private UnicodeExtraFieldPolicy unicodeExtraFieldPolicy;
	private Boolean fallbackToUTF8;
	private int level;
	private int method;
	private Boolean useLanguageEncoding;
	private Zip64Mode zip64Mode;

	ArchiveFileAsStubJarImpl(File directory) {
		super(directory, JAR);
	}

	public ArchiveFileAsStubJarImpl withEncoding(String encoding) {
		this.encoding = encoding;
		return this;
	}

	public ArchiveFileAsStubJarImpl withComment(String comment) {
		this.comment = comment;
		return this;
	}

	public ArchiveFileAsStubJarImpl withUnicodeExtraFieldPolicy(UnicodeExtraFieldPolicy unicodeExtraFieldPolicy) {
		this.unicodeExtraFieldPolicy = unicodeExtraFieldPolicy;
		return this;
	}

	public ArchiveFileAsStubJarImpl fallbackToUTF8(boolean fallbackToUTF8) {
		this.fallbackToUTF8 = fallbackToUTF8;
		return this;
	}

	public ArchiveFileAsStubJarImpl withLevel(int level) {
		this.level = level;
		return this;
	}

	public ArchiveFileAsStubJarImpl withMethod(int method) {
		this.method = method;
		return this;
	}

	public ArchiveFileAsStubJarImpl useLanguageEncoding(boolean useLanguageEncoding) {
		this.useLanguageEncoding = useLanguageEncoding;
		return this;
	}

	public ArchiveFileAsStubJarImpl withZip64Mode(Zip64Mode zip64Mode) {
		this.zip64Mode = zip64Mode;
		return this;
	}

	@Override
	protected Function<ArchiveOutputStreamConfig, ArchiveOutputStream> streamFunction() {
		return new Function<ArchiveOutputStreamConfig, ArchiveOutputStream>() {

			@Override
			public ArchiveOutputStream apply(ArchiveOutputStreamConfig config) {
				ArchiveOutputStreamConfigJarImpl jarConfig = (ArchiveOutputStreamConfigJarImpl) config;
				return createArchiveOutputStream(jarConfig.getOutputStream(), //
						or(encoding, jarConfig.getEncoding()), //
						or(comment, jarConfig.getComment()), //
						or(unicodeExtraFieldPolicy, jarConfig.getUnicodeExtraFieldPolicy()), //
						or(fallbackToUTF8, jarConfig.fallbackToUTF8()), //
						or(level, jarConfig.getLevel()), // z
						or(method, jarConfig.getMethod()), //
						or(useLanguageEncoding, jarConfig.useLanguageEncoding()), //
						or(zip64Mode, jarConfig.getZip64Mode()));
			}

		};
	}

	@VisibleForTesting
	protected ArchiveOutputStream createArchiveOutputStream(OutputStream outputStream, //
			String encoding, //
			String comment, //
			UnicodeExtraFieldPolicy unicodeExtraFieldPolicy, //
			boolean fallbackToUTF8, //
			int level, //
			int method, //
			boolean useLanguageEncoding, //
			Zip64Mode zip64Mode) {
		JarArchiveOutputStream stream = new JarArchiveOutputStream(outputStream, encoding);
		stream.setComment(comment);
		stream.setCreateUnicodeExtraFields(unicodeExtraFieldPolicy);
		stream.setFallbackToUTF8(fallbackToUTF8);
		stream.setLevel(level);
		stream.setMethod(method);
		stream.setUseLanguageEncodingFlag(useLanguageEncoding);
		stream.setUseZip64(zip64Mode);
		return new ArchiveOutputStreamImpl(stream);
	}

}
