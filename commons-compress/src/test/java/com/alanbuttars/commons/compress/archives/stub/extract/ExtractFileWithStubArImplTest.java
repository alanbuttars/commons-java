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

import static com.alanbuttars.commons.compress.archives.util.Archives.AR;
import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.IOException;

import org.junit.Before;
import org.junit.Test;

import com.alanbuttars.commons.compress.archives.config.ArchiveConfigs;

/**
 * Test class for {@link ExtractFileWithStubArImpl}
 * 
 * @author Alan Buttars
 *
 */
public class ExtractFileWithStubArImplTest {

	private File archive;
	private ExtractFileWithStubArImpl stub;

	@Before
	public void setup() throws IOException {
		archive = File.createTempFile(getClass().getName(), ".tmp");
		stub = new ExtractFileWithStubArImpl(archive);
	}

	@Test
	public void testConstructor() {
		assertEquals(archive, stub.source);
		assertEquals(AR, stub.archiveType);
		assertEquals(ArchiveConfigs.INPUT_CONFIG_FUNCTIONS.get(AR), stub.streamConfigFunction());
	}

	@Test
	public void testStreamFunction() {
		assertEquals(ArchiveConfigs.INPUT_STREAM_FUNCTIONS.get(AR), stub.streamFunction());
	}

}
