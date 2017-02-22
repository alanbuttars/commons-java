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

import java.io.File;
import java.io.IOException;

import org.apache.commons.compress.archivers.ArchiveEntry;

import com.alanbuttars.commons.compress.archives.config.ArchiveConfigs;
import com.alanbuttars.commons.compress.archives.config.entry.ArchiveEntryConfig;
import com.alanbuttars.commons.compress.archives.config.output.ArchiveOutputStreamConfig;
import com.alanbuttars.commons.compress.archives.output.ArchiveOutputStream;
import com.alanbuttars.commons.compress.archives.util.Archives;
import com.alanbuttars.commons.util.functions.DoubleInputFunction;
import com.alanbuttars.commons.util.functions.Function;

/**
 * Abstraction of the final archive creation stub. Extensions of this class should offer archive-type-specific
 * configuration functions with the builder pattern. For example, in the archive creation stub:
 * 
 * <pre>
 * Archive.directory(directory).asZip().withEncoding("UTF8").toFile(file);
 * </pre>
 * 
 * <p>
 * <code>withEncoding()</code> is a builder function supported by {@link ArchiveFileAsStubZipImpl}.
 * 
 * @author Alan Buttars
 *
 */
abstract class ArchiveFileAsStub {

	protected final File directory;
	protected final String archiveType;

	public ArchiveFileAsStub(File directory, String archiveType) {
		this.directory = directory;
		this.archiveType = archiveType;
	}

	/**
	 * Concludes this stub by invoking the creation logic configured by the complete stub.
	 * 
	 * @param directory
	 *            Non-null file archive destination
	 * @throws IOException
	 */
	public void toFile(File destination) throws IOException {
		Archives.archive(archiveType, directory, destination, streamConfigFunction(), streamFunction(), entryConfigFunction(), entryFunction());
	}

	/**
	 * Returns the universally-configured stream config function by default.
	 */
	protected Function<File, ArchiveOutputStreamConfig> streamConfigFunction() {
		return ArchiveConfigs.OUTPUT_CONFIG_FUNCTIONS.get(archiveType);
	}

	/**
	 * By default, should return the universally-configured stream function. Stubs may tweak this behavior by adding
	 * further configuration functions.
	 */
	protected abstract Function<ArchiveOutputStreamConfig, ArchiveOutputStream> streamFunction();

	/**
	 * Returns the universally-configured entry config function by default.
	 */
	protected DoubleInputFunction<String, Long, ArchiveEntryConfig> entryConfigFunction() {
		return ArchiveConfigs.ENTRY_CONFIG_FUNCTIONS.get(archiveType);
	}

	/**
	 * Returns the universally-configured entry function by default.
	 */
	protected Function<ArchiveEntryConfig, ArchiveEntry> entryFunction() {
		return ArchiveConfigs.ENTRY_FUNCTIONS.get(archiveType);
	}

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
	protected short or(short primary, short secondary) {
		if (primary > 0) {
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
