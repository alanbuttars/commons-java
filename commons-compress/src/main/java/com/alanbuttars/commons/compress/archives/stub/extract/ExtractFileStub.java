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

import com.alanbuttars.commons.compress.archives.config.ArchiveConfigs;
import com.alanbuttars.commons.compress.archives.config.input.ArchiveInputStreamConfig;
import com.alanbuttars.commons.compress.archives.input.ArchiveInputStream;
import com.alanbuttars.commons.compress.archives.util.Archives;
import com.alanbuttars.commons.util.functions.Function;
import com.alanbuttars.commons.util.validators.Arguments;

/**
 * Intermediate archive extraction stub object which:
 * <ol>
 * <li>maintains the archive to be extracted</li>
 * <li>offers the user to specify the archive type</li>
 * </ol>
 * 
 * @author Alan Buttars
 *
 */
class ExtractFileStub {

	protected final File source;

	ExtractFileStub(File archive) {
		this.source = archive;
	}

	/**
	 * Indicates that the {@link #source} will be treated as a {@link Archives#AR}.
	 */
	public ExtractFileWithStubArImpl withAr() {
		return new ExtractFileWithStubArImpl(source);
	}

	/**
	 * Indicates that the {@link #source} will be treated as a {@link Archives#ARJ}.
	 */
	public ExtractFileWithStubArjImpl withArj() {
		return new ExtractFileWithStubArjImpl(source);
	}

	/**
	 * Indicates that the {@link #source} will be treated as a {@link Archives#CPIO}.
	 */
	public ExtractFileWithStubCpioImpl withCpio() {
		return new ExtractFileWithStubCpioImpl(source);
	}

	/**
	 * Indicates that the {@link #source} will be treated as a {@link Archives#DUMP}.
	 */
	public ExtractFileWithStubDumpImpl withDump() {
		return new ExtractFileWithStubDumpImpl(source);
	}

	/**
	 * Indicates that the {@link #source} will be treated as a {@link Archives#JAR}.
	 */
	public ExtractFileWithStubJarImpl withJar() {
		return new ExtractFileWithStubJarImpl(source);
	}

	/**
	 * Indicates that the {@link #source} will be treated as a {@link Archives#SEVENZ}.
	 */
	public ExtractFileWithStubSevenZImpl withSevenZ() {
		return new ExtractFileWithStubSevenZImpl(source);
	}

	/**
	 * Indicates that the {@link #source} will be treated as a {@link Archives#TAR}.
	 */
	public ExtractFileWithStubTarImpl withTar() {
		return new ExtractFileWithStubTarImpl(source);
	}

	/**
	 * Indicates that the {@link #source} will be treated as a {@link Archives#ZIP}.
	 */
	public ExtractFileWithStubZipImpl withZip() {
		return new ExtractFileWithStubZipImpl(source);
	}

	/**
	 * Indicates that the {@link #source} will be treated as a new archive type already configured by the API user.
	 */
	public ExtractFileWithStub with(String archiveType) {
		Arguments.verify(!Archives.ARCHIVE_TYPES.contains(archiveType));
		return with(archiveType, //
				ArchiveConfigs.INPUT_CONFIG_FUNCTIONS.get(archiveType), //
				ArchiveConfigs.INPUT_STREAM_FUNCTIONS.get(archiveType));
	}

	/**
	 * Indicates that the {@link #source} will be treated as a new archive type with inline stream functions.
	 */
	public ExtractFileWithStub with(String archiveType, //
			final Function<File, ArchiveInputStreamConfig> streamConfigFunction, //
			final Function<ArchiveInputStreamConfig, ArchiveInputStream> streamFunction) {
		Arguments.verify(!Archives.ARCHIVE_TYPES.contains(archiveType));
		return new ExtractFileWithStub(source, archiveType) {

			@Override
			protected Function<File, ArchiveInputStreamConfig> streamConfigFunction() {
				return streamConfigFunction;
			}

			@Override
			protected Function<ArchiveInputStreamConfig, ArchiveInputStream> streamFunction() {
				return streamFunction;
			}
		};
	}

}
