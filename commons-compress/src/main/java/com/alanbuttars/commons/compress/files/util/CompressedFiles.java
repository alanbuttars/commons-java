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

import static com.alanbuttars.commons.compress.files.config.CompressedFileConfigs.INPUT_CONFIG_FUNCTIONS;
import static com.alanbuttars.commons.compress.files.config.CompressedFileConfigs.INPUT_STREAM_FUNCTIONS;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import com.alanbuttars.commons.compress.archives.util.Archives;
import com.alanbuttars.commons.compress.files.config.CompressedFileConfigs;
import com.alanbuttars.commons.compress.files.config.input.CompressedFileInputStreamConfig;
import com.alanbuttars.commons.compress.files.input.CompressedFileInputStream;
import com.alanbuttars.commons.compress.files.output.CompressedFileOutputStream;
import com.alanbuttars.commons.util.functions.Function;
import com.alanbuttars.commons.util.validators.Arguments;

/**
 * Utility functions class for compressed files.
 * 
 * @author Alan Buttars
 *
 */
public class CompressedFiles {

	public static String BZIP2 = "bzip2";
	public static String DEFLATE = "deflate";
	public static String GZIP = "gzip";
	public static String LZMA = "lzma";
	public static String PACK200 = "pack200";
	public static String SNAPPY = "snappy";
	public static String XZ = "xz";
	public static String Z = "z";

	public static Set<String> COMPRESS_TYPES = new HashSet<>(Arrays.asList(//
			BZIP2, //
			DEFLATE, //
			GZIP, //
			LZMA, //
			PACK200, //
			SNAPPY, //
			XZ, //
			Z));

	/**
	 * Decompresses a file to a file destination.
	 * 
	 * @param fileType
	 *            non-null file type
	 * @param sourceFilePath
	 *            non-null file path pointing to a file which is to be decompressed
	 * @param destinationFilePath
	 *            non-null file path
	 * @throws IOException
	 *             on any IO exception
	 */
	public static void decompress(String fileType, String sourceFilePath, String destinationFilePath) throws IOException {
		decompress(fileType, new File(sourceFilePath), new File(destinationFilePath));
	}

	/**
	 * Decompresses a file to a file destination.
	 * 
	 * @param fileType
	 *            non-null file type
	 * @param source
	 *            non-null file which is to be decompressed
	 * @param destination
	 *            non-null file
	 * @throws IOException
	 *             on any IO exception
	 */
	public static void decompress(String fileType, File source, File destination) throws IOException {
		decompress(fileType, source, destination, null, null);
	}

	/**
	 * Decompresses a file to a file destination.
	 * 
	 * @param fileType
	 *            non-null file type
	 * @param source
	 *            non-null file which is to be decompressed
	 * @param destination
	 *            non-null file
	 * @param streamConfigFunction
	 *            nullable function which maps the <code>source</code>'s input stream to a stream config. If null, the
	 *            default function associated with <code>fileType</code> in
	 *            {@link CompressedFileConfigs#INPUT_CONFIG_FUNCTIONS} is used
	 * @param streamFunction
	 *            nullable function which maps the <code>source</code>'s stream config to a compressor stream. If null,
	 *            the default function associated with <code>fileType</code> in
	 *            {@link CompressedFileConfigs#INPUT_STREAM_FUNCTIONS} is used
	 * @throws IOException
	 *             on any IO exception
	 */
	public static void decompress(//
			String fileType, //
			File source, //
			File destination, //
			Function<InputStream, CompressedFileInputStreamConfig> streamConfigFunction, //
			Function<CompressedFileInputStreamConfig, CompressedFileInputStream> streamFunction) throws IOException {
		try (InputStream inputStream = new FileInputStream(source);
				BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream);
				FileOutputStream outputStream = new FileOutputStream(destination);
				CompressedFileInputStream compressorInputStream = createCompressorInputStream(fileType, inputStream, streamConfigFunction, streamFunction)) {
			byte[] content = new byte[1024];
			int length = 0;
			while ((length = compressorInputStream.read(content)) > 0) {
				outputStream.write(content, 0, length);
			}
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
	 *            nullable function which maps the <code>source</code>'s output stream to a compressed output stream
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
	}

	private static CompressedFileInputStream createCompressorInputStream(//
			String fileType, //
			InputStream inputStream, //
			Function<InputStream, CompressedFileInputStreamConfig> configFunction, //
			Function<CompressedFileInputStreamConfig, CompressedFileInputStream> streamFunction) {
		if (configFunction == null) {
			configFunction = INPUT_CONFIG_FUNCTIONS.get(fileType);
			Arguments.verify(configFunction != null, "File type " + fileType + " is not recognized");
		}
		if (streamFunction == null) {
			streamFunction = INPUT_STREAM_FUNCTIONS.get(fileType);
			Arguments.verify(streamFunction != null, "File	 type " + fileType + " is not recognized");
		}
		CompressedFileInputStreamConfig config = configFunction.apply(inputStream);
		CompressedFileInputStream stream = streamFunction.apply(config);
		return stream;
	}

}
