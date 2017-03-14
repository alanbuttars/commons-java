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
package com.alanbuttars.commons.compress.archives.util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.commons.compress.archivers.ArchiveEntry;

import com.alanbuttars.commons.compress.archives.input.ArchiveInputStream;
import com.alanbuttars.commons.compress.archives.output.ArchiveOutputStream;
import com.alanbuttars.commons.compress.stub.compress.Compress;
import com.alanbuttars.commons.compress.stub.decompress.Decompress;
import com.alanbuttars.commons.util.functions.BiFunction;
import com.alanbuttars.commons.util.functions.Function;

/**
 * Utility functions class for file archives. While users of this API may directly use this class for compression and
 * decompression, it is advised to use the stubbing classes: {@link Compress#directory(File)} and
 * {@link Decompress#archive(File)}.
 * 
 * @author Alan Buttars
 *
 */
public class Archives {

	public static String SEVENZ = "7z";
	public static String AR = "ar";
	public static String ARJ = "arj";
	public static String CPIO = "cpio";
	public static String DUMP = "dump";
	public static String JAR = "jar";
	public static String TAR = "tar";
	public static String ZIP = "zip";

	/**
	 * Decompresses an archive to a directory destination.
	 * 
	 * @param fileType
	 *            non-null file type
	 * @param source
	 *            non-null file archive which is to be decompressed
	 * @param destination
	 *            non-null directory destination
	 * @param decompressionFunction
	 *            non-null function which maps the <code>source</code>'s input stream to an archive input stream
	 * @throws IOException
	 *             on any IO exception
	 */
	public static void decompress(//
			String fileType, //
			File source, //
			File destination, //
			Function<File, ArchiveInputStream> decompressionFunction) throws IOException {
		try (ArchiveInputStream archiveInputStream = decompressionFunction.apply(source)) {
			readFromArchive(archiveInputStream, destination);
		}
	}

	private static void readFromArchive(ArchiveInputStream archiveInputStream, File destination) throws IOException {
		ArchiveEntry archiveEntry = null;
		while ((archiveEntry = archiveInputStream.getNextEntry()) != null) {
			File outputFile = new File(destination, archiveEntry.getName());
			if (archiveEntry.isDirectory()) {
				outputFile.mkdirs();
			}
			else {
				outputFile.getParentFile().mkdirs();
				try (OutputStream outputStream = new FileOutputStream(outputFile);
						BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(outputStream)) {
					byte[] content = new byte[1024];
					int length = 0;
					while ((length = archiveInputStream.read(content)) > 0) {
						bufferedOutputStream.write(content, 0, length);
						bufferedOutputStream.flush();
					}
				}
			}
		}
	}

	/**
	 * Compresses a directory to a file destination.
	 * 
	 * @param fileType
	 *            non-null file type
	 * @param source
	 *            non-null directory to be compressed
	 * @param destination
	 *            non-null file destination
	 * @param compressionFunction
	 *            non-null function which maps the <code>destination</code>'s file to an archive output stream
	 * @param entryFunction
	 *            non-null function which maps the files within the <code>source</code> to archive entries
	 * @throws IOException
	 *             on any IO exception
	 */
	public static void compress(//
			String fileType, //
			File source, //
			File destination, //
			Function<File, ArchiveOutputStream> compressionFunction, //
			BiFunction<String, Long, ArchiveEntry> entryFunction) throws IOException {
		try (ArchiveOutputStream archiveOutputStream = compressionFunction.apply(destination)) {
			writeToArchive(fileType, source, source, archiveOutputStream, entryFunction);
		}
	}

	private static void writeToArchive(//
			String archiveType, //
			File source, //
			File currentFile, //
			ArchiveOutputStream archiveOutputStream, //
			BiFunction<String, Long, ArchiveEntry> entryFunction) throws IOException {
		if (currentFile.isFile()) {
			int index = source.getAbsolutePath().length() + 1;
			String currentFilePath = currentFile.getCanonicalPath();
			String entryName = currentFilePath.substring(index);

			ArchiveEntry entry = entryFunction.apply(entryName, currentFile.length());

			archiveOutputStream.putArchiveEntry(entry);
			try (InputStream inputStream = new FileInputStream(currentFile);
					BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream)) {
				byte[] content = new byte[1024];
				int length = 0;
				while ((length = bufferedInputStream.read(content)) > 0) {
					archiveOutputStream.write(content, 0, length);
					archiveOutputStream.flush();
				}
			}
			archiveOutputStream.closeArchiveEntry();
		}
		else {
			for (File child : currentFile.listFiles()) {
				writeToArchive(archiveType, source, child, archiveOutputStream, entryFunction);
			}
		}
	}

}
