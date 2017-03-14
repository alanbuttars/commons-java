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

/**
 * The stubbing class for all compression operations. Files may be compressed using pre-configured or custom compression
 * algorithms:
 * <p>
 * Individual files may be compressed using {@link Compress#file(File)}:
 * </p>
 * 
 * <pre>
 * File source = new File("test.txt");
 * Compress.file(source).withBzip2().to(new File("test.txt.bz"));
 * Compress.file(source).withDeflate().to(new File("test.txt.deflate"));
 * Compress.file(source).withGzip().to(new File("test.txt.gz"));
 * Compress.file(source).withLzma().to(new File("test.txt.lzma"));
 * Compress.file(source).withPack200().to(new File("test.txt.pack"));
 * Compress.file(source).withXz().to(new File("test.txt.xz"));
 * Compress.file(source).with("my-custom-algorithm", //
 * 		new Function&lt;OutputStream, CompressedFileOutputStream&gt;() {
 * 			&#64;Override
 * 			public CompressedFileOutputStream apply(OutputStream outputStream) {
 * 				// Define some logic
 * 			}
 * 		}).to(new File("test.txt.out"));
 * </pre>
 * 
 * <p>
 * Directories may be compressed using {@link Compress#directory(File)}:
 * </p>
 * 
 * <pre>
 * File source = new File("test");
 * Compress.directory(source).with7z().to(new File("test.7z"));
 * Compress.directory(source).withAr().to(new File("test.a"));
 * Compress.directory(source).withCpio().to(new File("test.cpio"));
 * Compress.directory(source).withJar().to(new File("test.jar"));
 * Compress.directory(source).withTar().to(new File("test.tar"));
 * Compress.directory(source).withZip().to(new File("test.zip"));
 * Compress.directory(source).with("my-custom-algorithm", //
 * 		new Function&lt;File, ArchiveOutputStream&gt;() {
 * 			&#64;Override
 * 			public ArchiveOutputStream apply(File file) {
 * 				// Define some logic
 * 			}
 * 		}, new BiFunction&lt;String, Long, ArchiveEntry&gt;() {
 * 			&#64;Override
 * 			public ArchiveEntry apply(String entryName, Long fileSize) {
 * 				// Define some logic
 * 			}
 * 		}).to(new File("test.out");
 * </pre>
 * 
 * @author Alan Buttars
 *
 */
public class Compress {

	private Compress() {
	}

	public static CompressFileStub file(File source) {
		verifyNonNull(source, "Source must be non-null");
		verify(source.exists(), "Source " + source.getAbsolutePath() + " does not exist");
		verify(source.isFile(), "Source " + source.getAbsolutePath() + " must not be a directory; to compress a directory use Compress.directory()");
		verify(source.canRead(), "Source " + source.getAbsolutePath() + " is not readable");

		return new CompressFileStub(source);
	}

	public static CompressDirectoryStub directory(File source) {
		verifyNonNull(source, "Source must be non-null");
		verify(source.exists(), "Source " + source.getAbsolutePath() + " does not exist");
		verify(source.isDirectory(), "Source " + source.getAbsolutePath() + " must be a directory; to compress a file use Compress.file()");
		verify(source.canRead(), "Source " + source.getAbsolutePath() + " is not readable");

		return new CompressDirectoryStub(source);
	}

}
