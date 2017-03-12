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
