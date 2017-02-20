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
package com.alanbuttars.commons.compress.archives.stub.extract;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.io.File;
import java.io.IOException;

import org.junit.Before;
import org.junit.Test;

import com.alanbuttars.commons.compress.archives.config.input.ArchiveInputStreamConfig;
import com.alanbuttars.commons.compress.archives.input.ArchiveInputStream;
import com.alanbuttars.commons.util.functions.Function;

/**
 * Test class for {@link ExtractFileStub}.
 * 
 * @author Alan Buttars
 *
 */
public class ExtractFileStubTest {

	private File archive;

	@Before
	public void setup() throws IOException {
		archive = File.createTempFile(getClass().getName(), ".tmp");
	}

	@Test
	public void testConstructor() {
		ExtractFileStub stub = new ExtractFileStub(archive);
		assertEquals(archive, stub.archive);
	}

	@Test
	public void testAs() {
		ExtractFileAsStub stub = new ExtractFileStub(archive).as("blah");

		assertNull(stub.streamConfigFunction());
		assertNull(stub.streamFunction());
	}

	@Test
	public void testAsInline() {
		Function<File, ArchiveInputStreamConfig> streamConfigFunction = new Function<File, ArchiveInputStreamConfig>() {

			@Override
			public ArchiveInputStreamConfig apply(File input) {
				return null;
			}
		};
		Function<ArchiveInputStreamConfig, ArchiveInputStream> streamFunction = new Function<ArchiveInputStreamConfig, ArchiveInputStream>() {

			@Override
			public ArchiveInputStream apply(ArchiveInputStreamConfig input) {
				return null;
			}

		};

		ExtractFileAsStub stub = new ExtractFileStub(archive).as("blah", streamConfigFunction, streamFunction);

		assertEquals(streamConfigFunction, stub.streamConfigFunction());
		assertEquals(streamFunction, stub.streamFunction());
	}

}