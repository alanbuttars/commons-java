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
package com.alanbuttars.commons.compress.stub.compress;

import static com.alanbuttars.commons.util.validators.Arguments.verify;
import static com.alanbuttars.commons.util.validators.Arguments.verifyNonNull;

import java.io.File;

import org.apache.commons.compress.archivers.ArchiveEntry;

import com.alanbuttars.commons.compress.archives.output.ArchiveOutputStream;
import com.alanbuttars.commons.compress.archives.util.Archives;
import com.alanbuttars.commons.util.functions.BiFunction;
import com.alanbuttars.commons.util.functions.Function;

public class CompressDirectoryStub {

	protected final File source;

	CompressDirectoryStub(File source) {
		this.source = source;
	}

	/**
	 * Indicates that the {@link #source} will be compressed with {@link Archives#SEVENZ}.
	 */
	public CompressDirectoryWithStub7zImpl with7z() {
		return new CompressDirectoryWithStub7zImpl(source);
	}

	/**
	 * Indicates that the {@link #source} will be compressed with {@link Archives#AR}.
	 */
	public CompressDirectoryWithStubArImpl withAr() {
		return new CompressDirectoryWithStubArImpl(source);
	}

	/**
	 * Indicates that the {@link #source} will be compressed with {@link Archives#CPIO}.
	 */
	public CompressDirectoryWithStubCpioImpl withCpio() {
		return new CompressDirectoryWithStubCpioImpl(source);
	}

	/**
	 * Indicates that the {@link #source} will be compressed with {@link Archives#JAR}.
	 */
	public CompressDirectoryWithStubJarImpl withJar() {
		return new CompressDirectoryWithStubJarImpl(source);
	}

	/**
	 * Indicates that the {@link #source} will be compressed with {@link Archives#TAR}.
	 */
	public CompressDirectoryWithStubTarImpl withTar() {
		return new CompressDirectoryWithStubTarImpl(source);
	}

	/**
	 * Indicates that the {@link #source} will be compressed with {@link Archives#ZIP}.
	 */
	public CompressDirectoryWithStubZipImpl withZip() {
		return new CompressDirectoryWithStubZipImpl(source);
	}

	/**
	 * Indicates that the {@link #source} will be compressed with the given functions.
	 * 
	 * @param fileType
	 *            Non-null file type
	 * @param compressionFunction
	 *            Function used to transform the {@link #source} to an archive output stream
	 * @param entryFunction
	 *            Function used to transform a file entry's name and size to an archive entry
	 */
	public CompressDirectoryWithStub with(String fileType, //
			final Function<File, ArchiveOutputStream> compressionFunction, //
			final BiFunction<String, Long, ArchiveEntry> entryFunction) {
		verifyNonNull(fileType, "File type must be non-null");
		verify(!fileType.trim().isEmpty(), "File type must be non-empty");
		verifyNonNull(compressionFunction, "Compression function must be non-null");
		verifyNonNull(entryFunction, "Entry function must be non-null");

		return new CompressDirectoryWithStub(source, fileType) {

			@Override
			protected Function<File, ArchiveOutputStream> compressionFunction() {
				return compressionFunction;
			}

			@Override
			protected BiFunction<String, Long, ArchiveEntry> entryFunction() {
				return entryFunction;
			}
		};
	}

}
