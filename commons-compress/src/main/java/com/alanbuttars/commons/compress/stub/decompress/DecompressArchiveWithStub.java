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
import java.io.IOException;
import java.nio.file.Files;

import com.alanbuttars.commons.compress.archives.input.ArchiveInputStream;
import com.alanbuttars.commons.compress.archives.util.Archives;
import com.alanbuttars.commons.util.functions.Function;

abstract class DecompressArchiveWithStub {

	protected final File source;
	protected final String fileType;

	DecompressArchiveWithStub(File source, String fileType) {
		this.source = source;
		this.fileType = fileType;
	}

	/**
	 * Concludes this stub by invoking the creation logic configured by the complete stub.
	 * 
	 * @param destination
	 *            Non-null decompressed directory destination
	 */
	public void to(File destination) throws IOException {
		verifyNonNull(destination, "Destination must be non-null");
		verify(!destination.isFile(), "Destination " + destination.getAbsolutePath() + " must not be an existing file");
		verify(destination.canWrite(), "Destination " + destination.getAbsolutePath() + " is not writeable");

		Archives.decompress(fileType, source, destination, decompressionFunction());
	}

	/**
	 * Concludes this stub by invoking the creation logic configured by the complete stub and storing the resulting
	 * compressed file in a temporary directory.
	 * 
	 * @return The compressed directory
	 */
	public File toTempDirectory() throws IOException {
		File tempFile = Files.createTempDirectory(source.getName()).toFile();
		to(tempFile);
		return tempFile;
	}

	/**
	 * Function used to transform the {@link #source} to an archive input stream.
	 */
	protected abstract Function<File, ArchiveInputStream> decompressionFunction();

}
