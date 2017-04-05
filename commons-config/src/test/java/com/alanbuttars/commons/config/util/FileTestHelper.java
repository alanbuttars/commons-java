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
package com.alanbuttars.commons.config.util;

import java.io.File;
import java.io.IOException;
import java.nio.file.StandardOpenOption;

import com.google.common.io.Files;

/**
 * Utility test class for creating and writing to files.
 * 
 * @author Alan Buttars
 *
 */
public class FileTestHelper {

	/**
	 * Creates a new temporary file.
	 * 
	 * @throws IOException
	 */
	public static File file() throws IOException {
		File file = File.createTempFile(FileTestHelper.class.getName(), ".tmp");
		file.deleteOnExit();
		return file;
	}

	/**
	 * Creates a new temporary file under the given parent.
	 * 
	 * @param parent
	 *            Existing directory
	 * @throws IOException
	 */
	public static File file(File parent) throws IOException {
		File file = File.createTempFile(FileTestHelper.class.getName(), ".tmp", parent);
		file.deleteOnExit();
		return file;
	}

	/**
	 * Creates a new temporary directory.
	 * 
	 * @throws IOException
	 */
	public static File directory() throws IOException {
		File directory = Files.createTempDir();
		directory.deleteOnExit();
		return directory;
	}

	/**
	 * Appends given content to a file.
	 * 
	 * @param content
	 *            Non-null content
	 * @param file
	 *            Non-null existing file
	 * @throws IOException
	 */
	public static void append(String content, File file) throws IOException {
		java.nio.file.Files.write(file.toPath(), content.getBytes(), StandardOpenOption.APPEND);
	}

	/**
	 * Writes given content to a file.
	 * 
	 * @param content
	 *            Non-null content
	 * @param file
	 *            Non-null existing file
	 * @throws IOException
	 */
	public static void write(String content, File file) throws IOException {
		java.nio.file.Files.write(file.toPath(), content.getBytes());
	}

	/**
	 * Deletes the file or directory.
	 * 
	 * @param file
	 *            Non-null file which may or may not exist
	 * @throws IOException
	 */
	public static void delete(File file) throws IOException {
		if (file.isFile()) {
			file.delete();
		}
		else if (file.isDirectory()) {
			for (File child : file.listFiles()) {
				delete(child);
			}
		}
	}
}
