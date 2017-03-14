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

import static com.alanbuttars.commons.util.validators.Arguments.verify;
import static com.alanbuttars.commons.util.validators.Arguments.verifyNonNull;

import java.io.File;

import com.alanbuttars.commons.compress.archives.input.ArchiveInputStream;
import com.alanbuttars.commons.compress.archives.util.Archives;
import com.alanbuttars.commons.util.functions.Function;

/**
 * Directory decompression stub which simply contains the source archive as a context.
 * 
 * @author Alan Buttars
 *
 */
public class DecompressArchiveStub {

	protected final File source;

	DecompressArchiveStub(File source) {
		this.source = source;
	}

	/**
	 * Indicates that the {@link #source} will be compressed with {@link Archives#SEVENZ}.
	 */
	public DecompressArchiveWithStub7zImpl with7z() {
		return new DecompressArchiveWithStub7zImpl(source);
	}

	/**
	 * Indicates that the {@link #source} will be compressed with {@link Archives#AR}.
	 */
	public DecompressArchiveWithStubArImpl withAr() {
		return new DecompressArchiveWithStubArImpl(source);
	}

	/**
	 * Indicates that the {@link #source} will be compressed with {@link Archives#ARJ}.
	 */
	public DecompressArchiveWithStubArjImpl withArj() {
		return new DecompressArchiveWithStubArjImpl(source);
	}

	/**
	 * Indicates that the {@link #source} will be compressed with {@link Archives#CPIO}.
	 */
	public DecompressArchiveWithStubCpioImpl withCpio() {
		return new DecompressArchiveWithStubCpioImpl(source);
	}

	/**
	 * Indicates that the {@link #source} will be compressed with {@link Archives#DUMP}.
	 */
	public DecompressArchiveWithStubDumpImpl withDump() {
		return new DecompressArchiveWithStubDumpImpl(source);
	}

	/**
	 * Indicates that the {@link #source} will be compressed with {@link Archives#JAR}.
	 */
	public DecompressArchiveWithStubJarImpl withJar() {
		return new DecompressArchiveWithStubJarImpl(source);
	}

	/**
	 * Indicates that the {@link #source} will be compressed with {@link Archives#TAR}.
	 */
	public DecompressArchiveWithStubTarImpl withTar() {
		return new DecompressArchiveWithStubTarImpl(source);
	}

	/**
	 * Indicates that the {@link #source} will be compressed with {@link Archives#ZIP}.
	 */
	public DecompressArchiveWithStubZipImpl withZip() {
		return new DecompressArchiveWithStubZipImpl(source);
	}

	/**
	 * Indicates that the {@link #source} will be compressed with the given functions.
	 * 
	 * @param fileType
	 *            Non-null file type
	 * @param decompressionFunction
	 *            Function used to transform the {@link #source} to an archive input stream
	 */
	public DecompressArchiveWithStub with(String fileType, //
			final Function<File, ArchiveInputStream> decompressionFunction) {
		verifyNonNull(fileType, "File type must be non-null");
		verify(!fileType.trim().isEmpty(), "File type must be non-empty");
		verifyNonNull(decompressionFunction, "Decompression function must be non-null");

		return new DecompressArchiveWithStub(source, fileType) {

			@Override
			protected Function<File, ArchiveInputStream> decompressionFunction() {
				return decompressionFunction;
			}

		};
	}

}
