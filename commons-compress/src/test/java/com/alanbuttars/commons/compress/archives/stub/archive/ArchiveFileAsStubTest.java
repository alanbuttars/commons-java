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
package com.alanbuttars.commons.compress.archives.stub.archive;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import org.junit.Before;
import org.junit.Test;

import com.alanbuttars.commons.compress.archives.config.output.ArchiveOutputStreamConfig;
import com.alanbuttars.commons.compress.archives.output.ArchiveOutputStream;
import com.alanbuttars.commons.util.functions.Function;

/**
 * Test class for {@link ArchiveFileAsStub}.
 * 
 * @author Alan Buttars
 *
 */
public class ArchiveFileAsStubTest {

	private File directory;
	private ArchiveFileAsStub stub;

	@Before
	public void setup() throws IOException {
		directory = Files.createTempDirectory(getClass().getName()).toFile();
		stub = new ArchiveFileAsStub(directory, "blah") {

			@Override
			protected Function<ArchiveOutputStreamConfig, ArchiveOutputStream> streamFunction() {
				return null;
			}
		};
	}

	@Test
	public void testConstructor() {
		assertEquals(directory, stub.directory);
		assertEquals("blah", stub.archiveType);
		assertNull(stub.streamConfigFunction());
		assertNull(stub.streamFunction());
		assertNull(stub.entryConfigFunction());
		assertNull(stub.entryFunction());
	}

	@Test
	public void testOr() {
		assertEquals("a", stub.or("a", "b"));
		assertEquals("b", stub.or(null, "b"));
		assertNull(stub.or(null, null));

		assertEquals(1, stub.or(1, 2));
		assertEquals(2, stub.or(0, 2));
		assertEquals(0, stub.or(0, 0));
	}

}
