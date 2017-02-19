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
package com.alanbuttars.commons.compress.files.input;

import java.io.Closeable;
import java.io.IOException;

import com.alanbuttars.commons.util.annotations.VisibleForTesting;

/**
 * An interface encompassing a readable compressed file input stream.
 * 
 * @author Alan Buttars
 *
 */
public interface CompressedFileInputStream extends Closeable {

	/**
	 * Reads bytes from an open file stream.
	 * 
	 * @throws IOException
	 */
	public int read(byte[] content) throws IOException;

	/**
	 * Returns the input stream.
	 */
	@VisibleForTesting
	public Closeable getStream();
}
