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
