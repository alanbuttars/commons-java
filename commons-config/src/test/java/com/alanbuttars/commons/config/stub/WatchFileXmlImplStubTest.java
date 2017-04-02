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
 * Test class for {@link WatchFileXmlImplStub}.
 * 
 * @author Alan Buttars
 *
 */
public class WatchFileXmlImplStubTest {

	private File file;
	private WatchFileXmlImplStub stub;

	@Before
	public void setup() throws IOException {
		file = File.createTempFile(getClass().getName(), ".json");
		file.deleteOnExit();

		stub = new WatchFileXmlImplStub(file);
	}

	@Test
	public void testMappedToClass() {
		WatchFileXmlClassImplStub<User> classStub = stub.mappedTo(User.class);
		assertEquals(file, classStub.file);
		assertEquals(User.class, classStub.clazz);
	}

	@Test
	public void testMappedToNullClass() {
		Class<User> clazz = null;
		try {
			stub.mappedTo(clazz);
		}
		catch (IllegalArgumentException e) {
			assertEquals("Clazz must be non-null", e.getMessage());
		}
	}

}
