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
 * Extract.archive(file).asZip().withEncoding("UTF8").toDirectory(directory);
 * </pre>
 * 
 * <p>
 * <code>withEncoding()</code> is a builder function supported by {@link ExtractFileAsStubZipImpl}.
 * 
 * @author Alan Buttars
 *
 */
abstract class ExtractFileAsStub {

	protected final File archive;
	protected final String archiveType;

	ExtractFileAsStub(File archive, String archiveType) {
		this.archive = archive;
		this.archiveType = archiveType;
	}

	/**
	 * Concludes this stub by invoking the extraction logic configured by the complete stub.
	 * 
	 * @param directory
	 *            Non-null file extraction destination
	 * @throws IOException
	 */
	public void toDirectory(File directory) throws IOException {
		Archives.extract(archiveType, archive, directory, streamConfigFunction(), streamFunction());
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

	/**
	 * Returns the <code>primary</code> if is non-null; the <code>secondary</code> otherwise.
	 */
	protected <T> T or(T primary, T secondary) {
		if (primary != null) {
			return primary;
		}
		return secondary;
	}

	/**
	 * Returns the <code>primary</code> if it is positive; the <code>secondary</code> otherwise.
	 */
	protected int or(int primary, int secondary) {
		if (primary > 0) {
			return primary;
		}
		return secondary;
	}

}
