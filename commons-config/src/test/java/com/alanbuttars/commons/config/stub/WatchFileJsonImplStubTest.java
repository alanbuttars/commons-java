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
import static org.junit.Assert.assertNotNull;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.junit.Test;

import com.alanbuttars.commons.config.ConfigurationJsonCollectionImpl;
import com.alanbuttars.commons.config.ConfigurationJsonImpl;
import com.alanbuttars.commons.config.eventbus.EventBus;
import com.alanbuttars.commons.config.eventbus.EventBusSyncImpl;
import com.fasterxml.jackson.core.type.TypeReference;

/**
 * Test class for {@link WatchFileJsonImplStub}.
 * 
 * @author Alan Buttars
 *
 */
public class WatchFileJsonImplStubTest {

	@Test
	public void testMappedToClass() throws IOException {
		ConfigurationJsonImpl<User> config = stub("user-json").mappedTo(User.class);
		assertEquals("user-json", config.getSourceId());
		assertNotNull(config.getValue());
	}

	@Test
	public void testMappedToNullClass() throws IOException {
		Class<User> clazz = null;
		try {
			stub("user-json").mappedTo(clazz);
		}
		catch (IllegalArgumentException e) {
			assertEquals("Clazz must be non-null", e.getMessage());
		}
	}

	@Test
	public void testMappedToTypeReference() throws IOException {
		TypeReference<List<User>> typeReference = new TypeReference<List<User>>() {
		};
		ConfigurationJsonCollectionImpl<List<User>> typeReferenceStub = stub("users-json").mappedTo(typeReference);
		assertEquals("users-json", typeReferenceStub.getSourceId());
		assertNotNull(typeReferenceStub.getValue());
	}

	@Test
	public void testMappedToNullTypeReference() throws IOException {
		TypeReference<List<User>> typeReference = null;
		try {
			stub("users-json").mappedTo(typeReference);
		}
		catch (IllegalArgumentException e) {
			assertEquals("Type reference must be non-null", e.getMessage());
		}
	}

	private WatchFileJsonImplStub stub(String sourceId) throws IOException {
		File file = WatchTestHelper.getSourceFile(sourceId);
		EventBus eventBus = new EventBusSyncImpl();
		return new WatchFileJsonImplStub(sourceId, file, eventBus);
	}

}
