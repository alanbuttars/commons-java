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

import static com.alanbuttars.commons.compress.archives.util.Archives.SEVENZ;

import java.io.File;
import java.io.IOException;

import org.apache.commons.compress.archivers.sevenz.SevenZFile;

import com.alanbuttars.commons.compress.archives.input.ArchiveInputStream;
import com.alanbuttars.commons.compress.archives.input.ArchiveInputStream7zImpl;
import com.alanbuttars.commons.compress.archives.util.Archives;
import com.alanbuttars.commons.util.annotations.VisibleForTesting;
import com.alanbuttars.commons.util.functions.Function;

/**
 * Extension of {@link DecompressArchiveWithStub} for {@link Archives#SEVENZ}. For details on the file format, see
 * <a href="https://en.wikipedia.org/wiki/7z">https://en.wikipedia.org/wiki/7z</a>.
 * 
 * @author Alan Buttars
 *
 */
public class DecompressArchiveWithStub7zImpl extends DecompressArchiveWithStub {

	private byte[] password;

	DecompressArchiveWithStub7zImpl(File source) {
		super(source, SEVENZ);
		this.password = null;
	}

	public DecompressArchiveWithStub7zImpl andDecryptWithPassword(byte[] password) {
		this.password = password;
		return this;
	}

	@Override
	protected Function<File, ArchiveInputStream> decompressionFunction() {
		return new Function<File, ArchiveInputStream>() {

			@Override
			public ArchiveInputStream apply(File file) {
				try {
					return createArchiveInputStream(file, password);
				}
				catch (IOException e) {
					throw new RuntimeException(e);
				}
			}

		};
	}

	@VisibleForTesting
	protected ArchiveInputStream createArchiveInputStream(File file, byte[] password) throws IOException {
		SevenZFile sevenZFile = new SevenZFile(file, password);
		return new ArchiveInputStream7zImpl(sevenZFile);
	}

}
