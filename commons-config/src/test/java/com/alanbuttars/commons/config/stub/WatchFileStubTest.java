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
package com.alanbuttars.commons.config.stub;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.IOException;

import org.junit.Before;
import org.junit.Test;

/**
 * Test class for {@link WatchFileStub}.
 * 
 * @author Alan Buttars
 *
 */
public class WatchFileStubTest {

	private File file;
	private WatchFileStub stub;

	@Before
	public void setup() throws IOException {
		file = File.createTempFile(getClass().getName(), ".tmp");
		file.deleteOnExit();

		stub = new WatchFileStub(file);
	}

	@Test
	public void testAsJson() {
		assertEquals(file, stub.asJson().file);
	}

	@Test
	public void testAsProperties() {
		assertEquals(file, stub.asProperties().file);
	}

	@Test
	public void testAsXml() {
		assertEquals(file, stub.asXml().file);
	}

	@Test
	public void testAsYaml() {
		assertEquals(file, stub.asYaml().file);
	}

}
