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

import static com.alanbuttars.commons.compress.archives.util.Archives.DUMP;

import java.io.File;
import java.io.IOException;

import org.junit.Test;

import com.alanbuttars.commons.compress.stub.decompress.Decompress;
import com.alanbuttars.commons.compress.util.FilesFunction;

/**
 * Integration test class for {@link Archives} for {@link Archives#DUMP} archives.
 * 
 * @author Alan Buttars
 *
 */
public class ArchivesIntegrationDumpImplTest extends ArchivesIntegrationAbstractTest {

	@Test
	public void testExtract() throws IOException {
		testExtract(DUMP, decompressFunction());
	}

	private FilesFunction decompressFunction() {
		return new FilesFunction() {

			@Override
			public File act(File original) throws IOException {
				return Decompress.archive(original).withDump().toTempDirectory();
			}
		};
	}

}
