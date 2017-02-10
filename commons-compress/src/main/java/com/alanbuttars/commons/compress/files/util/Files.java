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

import static com.alanbuttars.commons.compress.files.config.FileConfigs.INPUT_CONFIG_FUNCTIONS;
import static com.alanbuttars.commons.compress.files.config.FileConfigs.INPUT_STREAM_FUNCTIONS;
import static com.alanbuttars.commons.compress.files.config.FileConfigs.OUTPUT_CONFIG_FUNCTIONS;
import static com.alanbuttars.commons.compress.files.config.FileConfigs.OUTPUT_STREAM_FUNCTIONS;

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

import org.apache.commons.compress.compressors.CompressorInputStream;
import org.apache.commons.compress.compressors.CompressorOutputStream;

import com.alanbuttars.commons.compress.archives.util.Archives;
import com.alanbuttars.commons.compress.files.config.FileConfigs;
import com.alanbuttars.commons.compress.files.config.input.FileInputStreamConfig;
import com.alanbuttars.commons.compress.files.config.output.FileOutputStreamConfig;
import com.alanbuttars.commons.util.functions.Function;
import com.alanbuttars.commons.util.validators.Arguments;

/**
 * Utility functions class for files.
 * 
 * @author Alan Buttars
 *
 */
public class Files {

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
		validateDecompressFileType(fileType);
		validateSourceFilePath(sourceFilePath);
		validateDestinationFilePath(destinationFilePath);
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
	 *            default function associated with <code>fileType</code> in {@link FileConfigs#INPUT_CONFIG_FUNCTIONS}
	 *            is used
	 * @param streamFunction
	 *            nullable function which maps the <code>source</code>'s stream config to a compressor stream. If null,
	 *            the default function associated with <code>fileType</code> in
	 *            {@link FileConfigs#INPUT_STREAM_FUNCTIONS} is used
	 * @throws IOException
	 *             on any IO exception
	 */
	public static void decompress(//
			String fileType, //
			File source, //
			File destination, //
			Function<InputStream, FileInputStreamConfig> streamConfigFunction, //
			Function<FileInputStreamConfig, CompressorInputStream> streamFunction) throws IOException {
		validateDecompressFileType(fileType);

		Arguments.verify(source != null, "Source must be non-null");
		Arguments.verify(source.exists(), "Source " + source.getAbsolutePath() + " does not exist");
		Arguments.verify(source.isFile(), "Source " + source.getAbsolutePath() + " must not be a directory; to extract a directory use Archives.extract()");
		Arguments.verify(source.canRead(), "Source " + source.getAbsolutePath() + " is not readable");

		Arguments.verify(destination != null, "Destination must be non-null");
		Arguments.verify(destination.isFile(), "Destination " + destination.getAbsolutePath() + " must not be a directory");
		Arguments.verify(destination.canWrite(), "Destination " + destination.getAbsolutePath() + " is not writable");

		try (InputStream inputStream = new FileInputStream(source);
				BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream);
				FileOutputStream outputStream = new FileOutputStream(destination);
				CompressorInputStream compressorInputStream = createCompressorInputStream(fileType, inputStream, streamConfigFunction, streamFunction)) {
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
	 * @param sourceFilePath
	 *            non-null file path pointing to a file which is to be compressed
	 * @param destinationFilePath
	 *            non-null file path
	 * @throws IOException
	 *             on any IO exception
	 */
	public static void compress(String fileType, String sourceFilePath, String destinationFilePath) throws IOException {
		validateCompressFileType(fileType);
		validateSourceFilePath(sourceFilePath);
		validateDestinationFilePath(destinationFilePath);
		compress(fileType, new File(sourceFilePath), new File(destinationFilePath));
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
	 * @throws IOException
	 *             on any IO exception
	 */
	public static void compress(String fileType, File source, File destination) throws IOException {
		compress(fileType, source, destination, null, null);
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
	 * @param streamConfigFunction
	 *            nullable function which maps the <code>source</code>'s input stream to a stream config. If null, the
	 *            default function associated with <code>fileType</code> in {@link FileConfigs#OUTPUT_CONFIG_FUNCTIONS}
	 *            is used
	 * @param streamFunction
	 *            nullable function which maps the <code>source</code>'s stream config to a compressor stream. If null,
	 *            the default function associated with <code>fileType</code> in
	 *            {@link FileConfigs#OUTPUT_STREAM_FUNCTIONS} is used
	 * @throws IOException
	 *             on any IO exception
	 */
	public static void compress(String fileType, //
			File source, //
			File destination, //
			Function<OutputStream, FileOutputStreamConfig> streamConfigFunction, //
			Function<FileOutputStreamConfig, CompressorOutputStream> streamFunction) throws IOException {
		validateCompressFileType(fileType);

		Arguments.verify(source != null, "Source must be non-null");
		Arguments.verify(source.exists(), "Source " + source.getAbsolutePath() + " does not exist");
		Arguments.verify(source.isFile(), "Source " + source.getAbsolutePath() + " must not be a directory; to archive a directory use Archives.archive()");
		Arguments.verify(source.canRead(), "Source " + source.getAbsolutePath() + " is not readable");

		Arguments.verify(destination != null, "Destination must be non-null");
		Arguments.verify(destination.isFile(), "Destination " + destination.getAbsolutePath() + " must not be a directory");
		Arguments.verify(destination.canWrite(), "Destination " + destination.getAbsolutePath() + " is not writable");

		try (InputStream inputStream = new FileInputStream(source);
				OutputStream outputStream = new FileOutputStream(destination);
				BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(outputStream);
				CompressorOutputStream compressorOutputStream = createCompressorOutputStream(fileType, bufferedOutputStream, streamConfigFunction, streamFunction)) {
			byte[] content = new byte[1024];
			int length = 0;
			while ((length = inputStream.read(content)) > 0) {
				compressorOutputStream.write(content, 0, length);
			}
		}
	}

	private static CompressorInputStream createCompressorInputStream(//
			String fileType, //
			InputStream inputStream, //
			Function<InputStream, FileInputStreamConfig> configFunction, //
			Function<FileInputStreamConfig, CompressorInputStream> streamFunction) {
		if (configFunction == null) {
			configFunction = INPUT_CONFIG_FUNCTIONS.get(fileType);
			Arguments.verify(configFunction != null, "File type " + fileType + " is not recognized");
		}
		if (streamFunction == null) {
			streamFunction = INPUT_STREAM_FUNCTIONS.get(fileType);
			Arguments.verify(streamFunction != null, "File	 type " + fileType + " is not recognized");
		}
		FileInputStreamConfig config = configFunction.apply(inputStream);
		CompressorInputStream stream = streamFunction.apply(config);
		return stream;
	}

	private static CompressorOutputStream createCompressorOutputStream(String fileType, //
			OutputStream outputStream, //
			Function<OutputStream, FileOutputStreamConfig> configFunction, //
			Function<FileOutputStreamConfig, CompressorOutputStream> streamFunction) {
		if (configFunction == null) {
			configFunction = OUTPUT_CONFIG_FUNCTIONS.get(fileType);
			Arguments.verify(configFunction != null, "File type " + fileType + " is not recognized");
		}
		if (streamFunction == null) {
			streamFunction = OUTPUT_STREAM_FUNCTIONS.get(fileType);
			Arguments.verify(streamFunction != null, "File	 type " + fileType + " is not recognized");
		}
		FileOutputStreamConfig config = configFunction.apply(outputStream);
		CompressorOutputStream stream = streamFunction.apply(config);
		return stream;
	}

	private static void validateDecompressFileType(String fileType) {
		validateFileType(fileType);
		Arguments.verify(!Archives.ARCHIVE_TYPES.contains(fileType), "Archive type " + fileType + " cannot be decompressed; use Archives.extract()");
	}

	private static void validateCompressFileType(String fileType) {
		validateFileType(fileType);
		Arguments.verify(!Archives.ARCHIVE_TYPES.contains(fileType), "Archive type " + fileType + " cannot be compressed; use Archives.archive()");
	}

	private static void validateFileType(String fileType) {
		Arguments.verify(fileType != null, "File type must be non-null");
		Arguments.verify(!fileType.trim().isEmpty(), "File type must be non-empty");
	}

	private static void validateSourceFilePath(String sourceFilePath) {
		Arguments.verify(sourceFilePath != null, "Source file path must be non-null");
		Arguments.verify(!sourceFilePath.trim().isEmpty(), "Source file path must be non-empty");
	}

	private static void validateDestinationFilePath(String destinationFilePath) {
		Arguments.verify(destinationFilePath != null, "Destination file path must be non-null");
		Arguments.verify(!destinationFilePath.trim().isEmpty(), "Destination file path must be non-empty");
	}
}
