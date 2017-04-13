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
package com.alanbuttars.commons.compress.files.util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import com.alanbuttars.commons.compress.files.input.CompressedFileInputStream;
import com.alanbuttars.commons.compress.files.output.CompressedFileOutputStream;
import com.alanbuttars.commons.compress.stub.compress.Compress;
import com.alanbuttars.commons.compress.stub.decompress.Decompress;
import com.alanbuttars.commons.util.functions.Function;

/**
 * Utility functions class for compressed files. While users of this API may directly use this class for compression and
 * decompression, it is advised to use the stubbing classes: {@link Compress#file(File)} and
 * {@link Decompress#compressedFile(File)}.
 * 
 * @author Alan Buttars
 *
 */
public class CompressedFiles {

	public static String BZIP2 = "bzip2";
	public static String DEFLATE = "deflate";
	public static String FRAMEDSNAPPY = "framedsnappy";
	public static String GZIP = "gzip";
	public static String LZMA = "lzma";
	public static String PACK200 = "pack200";
	public static String SNAPPY = "snappy";
	public static String XZ = "xz";
	public static String Z = "z";

	/**
	 * Decompresses a file to a file destination.
	 * 
	 * @param fileType
	 *            non-null file type
	 * @param source
	 *            non-null file which is to be decompressed
	 * @param destination
	 *            non-null file
	 * @param decompressionFunction
	 *            non-null function which maps the <code>source</code>'s input stream to a compressed input stream
	 * @throws IOException
	 *             on any IO exception
	 */
	public static void decompress(//
			String fileType, //
			File source, //
			File destination, //
			Function<InputStream, CompressedFileInputStream> decompressionFunction) throws IOException {
		try (InputStream inputStream = new FileInputStream(source);
				BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream);
				FileOutputStream outputStream = new FileOutputStream(destination);
				CompressedFileInputStream compressorInputStream = decompressionFunction.apply(bufferedInputStream)) {
			byte[] content = new byte[1024];
			int length = 0;
			while ((length = compressorInputStream.read(content)) > 0) {
				outputStream.write(content, 0, length);
			}
		}
		catch (RuntimeException e) {
			if (e.getCause() != null && e.getCause() instanceof IOException) {
				throw (IOException) e.getCause();
			}
			throw e;
		}
	}

	/**
	 * Compresses a file to a file destination.
	 * 
	 * @param fileType
	 *            non-null file type
	 * @param source
	 *            non-null file which is to be compressed
	 * @param destination
	 *            non-null file
	 * @param compressionFunction
	 *            non-null function which maps the <code>source</code>'s output stream to a compressed output stream
	 * @throws IOException
	 *             on any IO exception
	 */
	public static void compress(String fileType, //
			File source, //
			File destination, //
			Function<OutputStream, CompressedFileOutputStream> compressionFunction) throws IOException {
		try (InputStream inputStream = new FileInputStream(source);
				OutputStream outputStream = new FileOutputStream(destination);
				BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(outputStream);
				CompressedFileOutputStream compressorOutputStream = compressionFunction.apply(bufferedOutputStream)) {
			byte[] content = new byte[1024];
			int length = 0;
			while ((length = inputStream.read(content)) > 0) {
				compressorOutputStream.write(content, 0, length);
			}
		}
		catch (RuntimeException e) {
			if (e.getCause() != null && e.getCause() instanceof IOException) {
				throw (IOException) e.getCause();
			}
			throw e;
		}
	}
}
