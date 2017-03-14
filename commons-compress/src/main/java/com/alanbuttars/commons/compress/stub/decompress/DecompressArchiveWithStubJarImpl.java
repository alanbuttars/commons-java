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

import static com.alanbuttars.commons.compress.archives.util.Archives.JAR;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import org.apache.commons.compress.archivers.jar.JarArchiveInputStream;

import com.alanbuttars.commons.compress.archives.input.ArchiveInputStream;
import com.alanbuttars.commons.compress.archives.input.ArchiveInputStreamImpl;
import com.alanbuttars.commons.compress.archives.util.Archives;
import com.alanbuttars.commons.util.annotations.VisibleForTesting;
import com.alanbuttars.commons.util.functions.Function;

/**
 * Extension of {@link DecompressArchiveWithStub} for {@link Archives#JAR}. For details on the file format, see
 * <a href="https://en.wikipedia.org/wiki/JAR_(file_format)">https://en.wikipedia.org/wiki/JAR_(file_format)</a>.
 * 
 * @author Alan Buttars
 *
 */
public class DecompressArchiveWithStubJarImpl extends DecompressArchiveWithStub {

	private String encoding;

	DecompressArchiveWithStubJarImpl(File source) {
		super(source, JAR);
		this.encoding = "UTF8";
	}

	public DecompressArchiveWithStubJarImpl andEncoding(String encoding) {
		this.encoding = encoding;
		return this;
	}

	@Override
	protected Function<File, ArchiveInputStream> decompressionFunction() {
		return new Function<File, ArchiveInputStream>() {

			@Override
			public ArchiveInputStream apply(File file) {
				try {
					return createArchiveInputStream(file, encoding);
				}
				catch (IOException e) {
					throw new RuntimeException(e);
				}
			}

		};
	}

	@VisibleForTesting
	protected ArchiveInputStream createArchiveInputStream(File file, String encoding) throws IOException {
		FileInputStream fileInputStream = new FileInputStream(file);
		JarArchiveInputStream archiveInputStream = new JarArchiveInputStream(fileInputStream, encoding);
		return new ArchiveInputStreamImpl(archiveInputStream);
	}

}
