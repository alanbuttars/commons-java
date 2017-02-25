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
import java.io.IOException;

import com.alanbuttars.commons.compress.archives.config.ArchiveConfigs;
import com.alanbuttars.commons.compress.archives.config.input.ArchiveInputStreamConfig;
import com.alanbuttars.commons.compress.archives.input.ArchiveInputStream;
import com.alanbuttars.commons.compress.archives.util.Archives;
import com.alanbuttars.commons.util.functions.Function;

/**
 * Abstraction of the final archive extraction stub. Extensions of this class should offer archive-type-specific
 * configuration functions with the builder pattern. For example, in the extraction stub:
 * 
 * <pre>
 * Extract.archive(file).asZip().withEncoding("UTF8").to(directory);
 * </pre>
 * 
 * <p>
 * <code>withEncoding()</code> is a builder function supported by {@link ExtractFileWithStubZipImpl}.
 * 
 * @author Alan Buttars
 *
 */
abstract class ExtractFileWithStub {

	protected final File source;
	protected final String archiveType;

	ExtractFileWithStub(File source, String archiveType) {
		this.source = source;
		this.archiveType = archiveType;
	}

	/**
	 * Concludes this stub by invoking the extraction logic configured by the complete stub.
	 * 
	 * @param directory
	 *            Non-null file extraction destination
	 * @throws IOException
	 */
	public void to(File directory) throws IOException {
		Archives.extract(archiveType, source, directory, streamConfigFunction(), streamFunction());
	}

	/**
	 * Returns the universally-configured stream config function by default.
	 */
	protected Function<File, ArchiveInputStreamConfig> streamConfigFunction() {
		return ArchiveConfigs.INPUT_CONFIG_FUNCTIONS.get(archiveType);
	}

	/**
	 * By default, should return the universally-configured stream function. Stubs may tweak this behavior by adding
	 * further configuration functions.
	 */
	protected abstract Function<ArchiveInputStreamConfig, ArchiveInputStream> streamFunction();

}
