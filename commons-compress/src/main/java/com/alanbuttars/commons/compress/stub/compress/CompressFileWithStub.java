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
import java.io.IOException;
import java.io.OutputStream;

import com.alanbuttars.commons.compress.files.output.CompressedFileOutputStream;
import com.alanbuttars.commons.compress.files.util.CompressedFiles;
import com.alanbuttars.commons.util.functions.Function;

/**
 * Abstraction of the final compressed file creation stub. Extensions of this class should offer file-type-specific
 * configuration functions with a builder pattern. For example:
 * 
 * <pre>
 * Compress.file(source).withBzip2().andBlockSize(BZip2CompressorOutputStream.MIN_BLOCKSIZE).to(destination);
 * </pre>
 * 
 * <p>
 * <code>andBlockSize()</code> is a builder function supported by {@link CompressFileWithStubBzip2Impl}.
 * 
 * @author Alan Buttars
 *
 */
abstract class CompressFileWithStub {

	protected final File source;
	protected final String fileType;

	CompressFileWithStub(File source, String fileType) {
		this.source = source;
		this.fileType = fileType;
	}

	/**
	 * Concludes this stub by invoking the creation logic configured by the complete stub.
	 * 
	 * @param destination
	 *            Non-null compressed file destination
	 */
	public void to(File destination) throws IOException {
		verifyNonNull(destination, "Destination must be non-null");
		verify(!destination.isDirectory(), "Destination " + destination.getAbsolutePath() + " must not be a directory");
		verify(destination.canWrite(), "Destination " + destination.getAbsolutePath() + " is not writeable");

		CompressedFiles.compress(fileType, source, destination, compressionFunction());
	}

	/**
	 * Concludes this stub by invoking the creation logic configured by the complete stub and storing the resulting
	 * compressed file in a temporary file.
	 * 
	 * @return The compressed file
	 */
	public File toTempFile() throws IOException {
		File tempFile = File.createTempFile(source.getName(), "." + fileType);
		to(tempFile);
		return tempFile;
	}

	/**
	 * Function used to transform the file output stream to a compressed file output stream.
	 */
	protected abstract Function<OutputStream, CompressedFileOutputStream> compressionFunction();

}
