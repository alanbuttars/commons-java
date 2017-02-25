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
package com.alanbuttars.commons.compress.archives.stub.extract;

import static com.alanbuttars.commons.compress.util.Optionals.or;

import java.io.File;
import java.io.InputStream;

import org.apache.commons.compress.archivers.zip.ZipArchiveInputStream;

import com.alanbuttars.commons.compress.archives.config.input.ArchiveInputStreamConfig;
import com.alanbuttars.commons.compress.archives.config.input.ArchiveInputStreamConfigZipImpl;
import com.alanbuttars.commons.compress.archives.input.ArchiveInputStream;
import com.alanbuttars.commons.compress.archives.input.ArchiveInputStreamImpl;
import com.alanbuttars.commons.compress.archives.util.Archives;
import com.alanbuttars.commons.util.annotations.VisibleForTesting;
import com.alanbuttars.commons.util.functions.Function;

/**
 * Extension of {@link ExtractFileWithStub} for {@link Archives#ZIP} archives.
 * 
 * @author Alan Buttars
 *
 */
class ExtractFileWithStubZipImpl extends ExtractFileWithStub {

	private String encoding;
	private Boolean useUnicodeExtraFields;
	private Boolean allowStoredEntriesWithDataDescriptor;

	ExtractFileWithStubZipImpl(File source) {
		super(source, Archives.ZIP);
	}

	public ExtractFileWithStubZipImpl withEncoding(String encoding) {
		this.encoding = encoding;
		return this;
	}

	public ExtractFileWithStubZipImpl withUnicodeExtraFields(boolean useUnicodeExtraFields) {
		this.useUnicodeExtraFields = useUnicodeExtraFields;
		return this;
	}

	public ExtractFileWithStubZipImpl withStoredEntriesWithDataDescriptor(boolean allowStoredEntriesWithDataDescriptor) {
		this.allowStoredEntriesWithDataDescriptor = allowStoredEntriesWithDataDescriptor;
		return this;
	}

	@Override
	protected Function<ArchiveInputStreamConfig, ArchiveInputStream> streamFunction() {
		return new Function<ArchiveInputStreamConfig, ArchiveInputStream>() {

			@Override
			public ArchiveInputStream apply(ArchiveInputStreamConfig config) {
				ArchiveInputStreamConfigZipImpl zipConfig = (ArchiveInputStreamConfigZipImpl) config;
				return createArchiveInputStream(//
						zipConfig.getInputStream(), //
						or(encoding, zipConfig.getEncoding()), //
						or(useUnicodeExtraFields, zipConfig.useUnicodeExtraFields()), //
						or(allowStoredEntriesWithDataDescriptor, zipConfig.allowStoredEntriesWithDataDescriptor()));
			}

		};
	}

	@VisibleForTesting
	protected ArchiveInputStream createArchiveInputStream(InputStream inputStream, String encoding, boolean useUnicodeExtraFields, boolean allowStoredEntriesWithDescriptor) {
		return new ArchiveInputStreamImpl(new ZipArchiveInputStream(inputStream, encoding, useUnicodeExtraFields, allowStoredEntriesWithDescriptor));
	}

}
