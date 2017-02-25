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
import com.alanbuttars.commons.util.functions.BiFunction;
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
class ArchiveFileStub {

	protected final File source;

	ArchiveFileStub(File source) {
		this.source = source;
	}

	/**
	 * Indicates that the {@link #source} will be treated as a {@link Archives#AR}.
	 */
	public ArchiveFileWithStubArImpl withAr() {
		return new ArchiveFileWithStubArImpl(source);
	}

	/**
	 * Indicates that the {@link #source} will be treated as a {@link Archives#CPIO}.
	 */
	public ArchiveFileWithStubCpioImpl withCpio() {
		return new ArchiveFileWithStubCpioImpl(source);
	}

	/**
	 * Indicates that the {@link #source} will be treated as a {@link Archives#JAR}.
	 */
	public ArchiveFileWithStubJarImpl withJar() {
		return new ArchiveFileWithStubJarImpl(source);
	}

	/**
	 * Indicates that the {@link #source} will be treated as a {@link Archives#SEVENZ}.
	 */
	public ArchiveFileWithStubSevenZImpl withSevenZ() {
		return new ArchiveFileWithStubSevenZImpl(source);
	}

	/**
	 * Indicates that the {@link #source} will be treated as a {@link Archives#TAR}.
	 */
	public ArchiveFileWithStubTarImpl withTar() {
		return new ArchiveFileWithStubTarImpl(source);
	}

	/**
	 * Indicates that the {@link #source} will be treated as a {@link Archives#ZIP}.
	 */
	public ArchiveFileWithStubZipImpl withZip() {
		return new ArchiveFileWithStubZipImpl(source);
	}

	/**
	 * Indicates that the {@link #source} will be treated as a new archive type already configured by the API user.
	 */
	public ArchiveFileWithStub with(String archiveType) {
		Arguments.verify(archiveType != null, "Archive type must be non-null");
		Arguments.verify(!archiveType.trim().isEmpty(), "Archive type must be non-empty");
		
		Function<File, ArchiveOutputStreamConfig> streamConfigFunction = ArchiveConfigs.OUTPUT_CONFIG_FUNCTIONS.get(archiveType);
		Function<ArchiveOutputStreamConfig, ArchiveOutputStream> streamFunction = ArchiveConfigs.OUTPUT_STREAM_FUNCTIONS.get(archiveType);
		BiFunction<String, Long, ArchiveEntryConfig> entryConfigFunction = ArchiveConfigs.ENTRY_CONFIG_FUNCTIONS.get(archiveType);
		Function<ArchiveEntryConfig, ArchiveEntry> entryFunction = ArchiveConfigs.ENTRY_FUNCTIONS.get(archiveType);
		
		Arguments.verify(streamConfigFunction != null, "Stream config function is not configured for archive type " + archiveType);
		Arguments.verify(streamFunction != null, "Stream function is not configured for archive type " + archiveType);
		Arguments.verify(streamConfigFunction != null, "Stream config function is not configured for archive type " + archiveType);
		Arguments.verify(streamFunction != null, "Stream function is not configured for archive type " + archiveType);

		return with(archiveType, //
				streamConfigFunction, //
				streamFunction, //
				entryConfigFunction, //
				entryFunction);
	}

	/**
	 * Indicates that the {@link #source} will be treated as a new archive type with inline stream functions.
	 */
	public ArchiveFileWithStub with(String archiveType, //
			final Function<File, ArchiveOutputStreamConfig> streamConfigFunction, //
			final Function<ArchiveOutputStreamConfig, ArchiveOutputStream> streamFunction, //
			final BiFunction<String, Long, ArchiveEntryConfig> entryConfigFunction, //
			final Function<ArchiveEntryConfig, ArchiveEntry> entryFunction) {
		Arguments.verify(archiveType != null, "Archive type must be non-null");
		Arguments.verify(!archiveType.trim().isEmpty(), "Archive type must be non-empty");
		Arguments.verify(streamConfigFunction != null, "Stream config function must be non-null");
		Arguments.verify(streamFunction != null, "Stream function must be non-null");
		Arguments.verify(entryConfigFunction != null, "Entry config function must be non-null");
		Arguments.verify(entryFunction != null, "Entry function must be non-null");
		
		return new ArchiveFileWithStub(source, archiveType) {

			@Override
			protected Function<File, ArchiveOutputStreamConfig> streamConfigFunction() {
				return streamConfigFunction;
			}

			@Override
			protected Function<ArchiveOutputStreamConfig, ArchiveOutputStream> streamFunction() {
				return streamFunction;
			}

			@Override
			protected BiFunction<String, Long, ArchiveEntryConfig> entryConfigFunction() {
				return entryConfigFunction;
			}

			@Override
			protected Function<ArchiveEntryConfig, ArchiveEntry> entryFunction() {
				return entryFunction;
			}
		};
	}
}
