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
package com.alanbuttars.commons.compress.stub.decompress;

import static com.alanbuttars.commons.compress.archives.util.Archives.ZIP;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import org.apache.commons.compress.archivers.zip.ZipArchiveInputStream;

import com.alanbuttars.commons.compress.archives.input.ArchiveInputStream;
import com.alanbuttars.commons.compress.archives.input.ArchiveInputStreamImpl;
import com.alanbuttars.commons.compress.archives.util.Archives;
import com.alanbuttars.commons.util.annotations.VisibleForTesting;
import com.alanbuttars.commons.util.functions.Function;

/**
 * Extension of {@link DecompressArchiveWithStub} for {@link Archives#ZIP}. For details on the file format, see
 * <a href="https://en.wikipedia.org/wiki/Zip_(file_format)">https://en.wikipedia.org/wiki/Zip_(file_format)</a>.
 * 
 * @author Alan Buttars
 *
 */
public class DecompressArchiveWithStubZipImpl extends DecompressArchiveWithStub {

	private boolean allowStoredEntriesWithDataDescriptor;
	private String encoding;
	private boolean useUnicodeExtraFields;

	DecompressArchiveWithStubZipImpl(File source) {
		super(source, ZIP);
		this.allowStoredEntriesWithDataDescriptor = false;
		this.encoding = "UTF8";
		this.useUnicodeExtraFields = true;
	}

	public DecompressArchiveWithStubZipImpl andAllowStoredEntriesWithDataDescriptor(boolean allowStoredEntriesWithDataDescriptor) {
		this.allowStoredEntriesWithDataDescriptor = allowStoredEntriesWithDataDescriptor;
		return this;
	}

	public DecompressArchiveWithStubZipImpl andEncoding(String encoding) {
		this.encoding = encoding;
		return this;
	}

	public DecompressArchiveWithStubZipImpl andUseUnicodeExtraFields(boolean useUnicodeExtraFields) {
		this.useUnicodeExtraFields = useUnicodeExtraFields;
		return this;
	}

	@Override
	protected Function<File, ArchiveInputStream> decompressionFunction() {
		return new Function<File, ArchiveInputStream>() {

			@Override
			public ArchiveInputStream apply(File file) {
				try {
					return createArchiveInputStream(file, allowStoredEntriesWithDataDescriptor, encoding, useUnicodeExtraFields);
				}
				catch (IOException e) {
					throw new RuntimeException(e);
				}
			}

		};
	}

	@VisibleForTesting
	protected ArchiveInputStream createArchiveInputStream(File file, boolean allowStoredEntriesWithDataDescriptor, String encoding, boolean useUnicodeExtraFields) throws IOException {
		FileInputStream fileInputStream = new FileInputStream(file);
		ZipArchiveInputStream archiveInputStream = new ZipArchiveInputStream(fileInputStream, encoding, useUnicodeExtraFields, allowStoredEntriesWithDataDescriptor);
		return new ArchiveInputStreamImpl(archiveInputStream);
	}

}
