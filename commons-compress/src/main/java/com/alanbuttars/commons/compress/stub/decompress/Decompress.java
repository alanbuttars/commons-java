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

/**
 * The stubbing class for all decompression operations. Files may be decompressed using pre-configured or custom
 * decompression algorithms:
 * <p>
 * Compressed files may be decompressed using {@link Decompress#compressedFile(File)}:
 * </p>
 * 
 * <pre>
 * File destination = new File("test.txt");
 * Decompress.compressedFile(new File("test.txt.bz")).withBzip2().to(destination);
 * Decompress.compressedFile(new File("test.txt.deflate")).withDeflate().to(destination);
 * Decompress.compressedFile(new File("test.txt.gz")).withGzip().to(destination);
 * Decompress.compressedFile(new File("test.txt.lzma")).withLzma().to(destination);
 * Decompress.compressedFile(new File("test.txt.pack")).withPack200().to(destination);
 * Decompress.compressedFile(new File("test.txt.sz")).withSnappy().to(destination);
 * Decompress.compressedFile(new File("test.txt.sz")).withFramedSnappy().to(destination);
 * Decompress.compressedFile(new File("test.txt.xz")).withXz().to(destination);
 * Decompress.compressedFile(new File("test.txt.z")).withZ().to(destination);
 * Decompress.compressedFile(new File("test.txt.out")).with("my-custom-algorithm", //
 * 		new Function&lt;InputStream, CompressedFileInputStream&gt;() {
 * 			&#64;Override
 * 			public CompressedFileInputStream apply(InputStream inputStream) {
 * 				// Define some logic
 * 			}
 * 		}).to(destination);
 * </pre>
 * 
 * <p>
 * Archives may be decompressed using {@link Decompress#archive(File)}:
 * </p>
 * 
 * <pre>
 * File destination = new File("test");
 * Decompress.archive(new File("test.7z")).with7z().to(destination);
 * Decompress.archive(new File("test.a")).withAr().to(destination);
 * Decompress.archive(new File("test.arj")).withArj().to(destination);
 * Decompress.archive(new File("test.cpio")).withCpio().to(destination);
 * Decompress.archive(new File("test.dump")).withDump().to(destination);
 * Decompress.archive(new File("test.jar")).withJar().to(destination);
 * Decompress.archive(new File("test.tar")).withTar().to(destination);
 * Decompress.archive(new File("test.zip")).withZip().to(destination);
 * Decompress.archive(new File("test.out")).with("my-custom-algorithm", //
 * 		new Function&lt;File, ArchiveInputStream&gt;() {
 * 			&#64;Override
 * 			public ArchiveInputStream apply(File file) {
 * 				// Define some logic
 * 			}
 * 		}).to(destination);
 * </pre>
 * 
 * @author Alan Buttars
 *
 */
public class Decompress {

	private Decompress() {
	}

	public static DecompressCompressedFileStub compressedFile(File source) {
		verifySource(source);
		return new DecompressCompressedFileStub(source);
	}

	public static DecompressArchiveStub archive(File source) {
		verifySource(source);
		return new DecompressArchiveStub(source);
	}

	private static void verifySource(File source) {
		verifyNonNull(source, "Source must be non-null");
		verify(source.exists(), "Source " + source.getAbsolutePath() + " does not exist");
		verify(source.isFile(), "Source " + source.getAbsolutePath() + " must not be a directory");
		verify(source.canRead(), "Source " + source.getAbsolutePath() + " is not readable");
	}

}
