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

import org.apache.commons.compress.archivers.ArchiveEntry;

import com.alanbuttars.commons.compress.archives.config.ArchiveConfigs;
import com.alanbuttars.commons.compress.archives.config.entry.ArchiveEntryConfig;
import com.alanbuttars.commons.compress.archives.config.output.ArchiveOutputStreamConfig;
import com.alanbuttars.commons.compress.archives.output.ArchiveOutputStream;
import com.alanbuttars.commons.compress.archives.util.Archives;
import com.alanbuttars.commons.util.functions.DoubleInputFunction;
import com.alanbuttars.commons.util.functions.Function;
import com.alanbuttars.commons.util.validators.Arguments;

/**
 * Intermediate archive creation stub object which:
 * <ol>
 * <li>maintains the directory to be archived</li>
 * <li>offers the user to specify the archive type</li>
 * </ol>
 * 
 * @author Alan Buttars
 *
 */
public class ArchiveFileStub {

	protected final File directory;

	ArchiveFileStub(File directory) {
		this.directory = directory;
	}

	/**
	 * Indicates that the {@link #directory} will be treated as a {@link Archives#AR}.
	 */
	public ArchiveFileAsStubArImpl asAr() {
		return new ArchiveFileAsStubArImpl(directory);
	}

	/**
	 * Indicates that the {@link #directory} will be treated as a {@link Archives#CPIO}.
	 */
	public ArchiveFileAsStubCpioImpl asCpio() {
		return new ArchiveFileAsStubCpioImpl(directory);
	}

	/**
	 * Indicates that the {@link #directory} will be treated as a {@link Archives#JAR}.
	 */
	public ArchiveFileAsStubJarImpl asJar() {
		return new ArchiveFileAsStubJarImpl(directory);
	}

	/**
	 * Indicates that the {@link #directory} will be treated as a {@link Archives#SEVENZ}.
	 */
	public ArchiveFileAsStubSevenZImpl asSevenZ() {
		return new ArchiveFileAsStubSevenZImpl(directory);
	}

	/**
	 * Indicates that the {@link #directory} will be treated as a {@link Archives#TAR}.
	 */
	public ArchiveFileAsStubTarImpl asTar() {
		return new ArchiveFileAsStubTarImpl(directory);
	}

	/**
	 * Indicates that the {@link #directory} will be treated as a {@link Archives#ZIP}.
	 */
	public ArchiveFileAsStubZipImpl asZip() {
		return new ArchiveFileAsStubZipImpl(directory);
	}

	/**
	 * Indicates that the {@link #directory} will be treated as a new archive type already configured by the API user.
	 */
	public ArchiveFileAsStub as(String archiveType) {
		Arguments.verify(!Archives.ARCHIVE_TYPES.contains(archiveType));
		return as(archiveType, //
				ArchiveConfigs.OUTPUT_CONFIG_FUNCTIONS.get(archiveType), //
				ArchiveConfigs.OUTPUT_STREAM_FUNCTIONS.get(archiveType), //
				ArchiveConfigs.ENTRY_CONFIG_FUNCTIONS.get(archiveType), //
				ArchiveConfigs.ENTRY_FUNCTIONS.get(archiveType));
	}

	/**
	 * Indicates that the {@link #directory} will be treated as a new archive type with inline stream functions.
	 */
	public ArchiveFileAsStub as(String archiveType, //
			final Function<File, ArchiveOutputStreamConfig> streamConfigFunction, //
			final Function<ArchiveOutputStreamConfig, ArchiveOutputStream> streamFunction, //
			final DoubleInputFunction<String, Long, ArchiveEntryConfig> entryConfigFunction, //
			final Function<ArchiveEntryConfig, ArchiveEntry> entryFunction) {
		Arguments.verify(!Archives.ARCHIVE_TYPES.contains(archiveType));
		return new ArchiveFileAsStub(directory, archiveType) {

			@Override
			protected Function<File, ArchiveOutputStreamConfig> streamConfigFunction() {
				return streamConfigFunction;
			}

			@Override
			protected Function<ArchiveOutputStreamConfig, ArchiveOutputStream> streamFunction() {
				return streamFunction;
			}

			@Override
			protected DoubleInputFunction<String, Long, ArchiveEntryConfig> entryConfigFunction() {
				return entryConfigFunction;
			}

			@Override
			protected Function<ArchiveEntryConfig, ArchiveEntry> entryFunction() {
				return entryFunction;
			}
		};
	}
}
