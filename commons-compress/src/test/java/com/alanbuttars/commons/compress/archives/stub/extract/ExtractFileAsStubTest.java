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
 * Test class for {@link ExtractFileAsStub}.
 * 
 * @author Alan Buttars
 *
 */
public class ExtractFileAsStubTest {

	private File archive;
	private ExtractFileAsStub stub;

	@Before
	public void setup() throws IOException {
		archive = File.createTempFile(getClass().getName(), ".tmp");
		stub = new ExtractFileAsStub(archive, "blah") {

			@Override
			protected Function<ArchiveInputStreamConfig, ArchiveInputStream> streamFunction() {
				return null;
			}

		};
	}

	@Test
	public void testConstructor() {
		assertEquals(archive, stub.archive);
		assertEquals("blah", stub.archiveType);
		assertNull(stub.streamConfigFunction());
		assertNull(stub.streamFunction());
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
