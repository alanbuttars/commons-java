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
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.core.type.TypeReference;

/**
 * Test class for {@link WatchFileJsonImplStub}.
 * 
 * @author Alan Buttars
 *
 */
public class WatchFileJsonImplStubTest {

	private File file;
	private WatchFileJsonImplStub stub;

	@Before
	public void setup() throws IOException {
		file = File.createTempFile(getClass().getName(), ".json");
		file.deleteOnExit();

		stub = new WatchFileJsonImplStub(file);
	}

	@Test
	public void testMappedToClass() {
		WatchFileJsonClassImplStub<User> classStub = stub.mappedTo(User.class);
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

	@Test
	public void testMappedToTypeReference() {
		TypeReference<List<User>> typeReference = new TypeReference<List<User>>() {
		};
		WatchFileJsonTypeReferenceImplStub<List<User>> typeReferenceStub = stub.mappedTo(typeReference);
		assertEquals(file, typeReferenceStub.file);
		assertEquals(typeReference, typeReferenceStub.typeReference);
	}

	@Test
	public void testMappedToNullTypeReference() {
		TypeReference<List<User>> typeReference = null;
		try {
			stub.mappedTo(typeReference);
		}
		catch (IllegalArgumentException e) {
			assertEquals("Type reference must be non-null", e.getMessage());
		}
	}

}
