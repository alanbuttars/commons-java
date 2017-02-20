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

import java.io.File;
import java.io.InputStream;

import org.apache.commons.compress.archivers.jar.JarArchiveInputStream;

import com.alanbuttars.commons.compress.archives.config.input.ArchiveInputStreamConfig;
import com.alanbuttars.commons.compress.archives.config.input.ArchiveInputStreamConfigJarImpl;
import com.alanbuttars.commons.compress.archives.input.ArchiveInputStream;
import com.alanbuttars.commons.compress.archives.input.ArchiveInputStreamImpl;
import com.alanbuttars.commons.compress.archives.util.Archives;
import com.alanbuttars.commons.util.annotations.VisibleForTesting;
import com.alanbuttars.commons.util.functions.Function;

/**
 * Extension of {@link ExtractFileAsStub} for {@link Archives#JAR} archives.
 * 
 * @author Alan Buttars
 *
 */
class ExtractFileAsStubJarImpl extends ExtractFileAsStub {

	private String encoding;

	ExtractFileAsStubJarImpl(File archive) {
		super(archive, Archives.JAR);
	}

	public ExtractFileAsStubJarImpl withEncoding(String encoding) {
		this.encoding = encoding;
		return this;
	}

	@Override
	protected Function<ArchiveInputStreamConfig, ArchiveInputStream> streamFunction() {
		return new Function<ArchiveInputStreamConfig, ArchiveInputStream>() {

			@Override
			public ArchiveInputStream apply(ArchiveInputStreamConfig config) {
				ArchiveInputStreamConfigJarImpl jarConfig = (ArchiveInputStreamConfigJarImpl) config;
				return createArchiveInputStream(jarConfig.getInputStream(), or(encoding, jarConfig.getEncoding()));
			}

		};
	}

	@VisibleForTesting
	protected ArchiveInputStream createArchiveInputStream(InputStream inputStream, String encoding) {
		return new ArchiveInputStreamImpl(new JarArchiveInputStream(inputStream, encoding));
	}

}
