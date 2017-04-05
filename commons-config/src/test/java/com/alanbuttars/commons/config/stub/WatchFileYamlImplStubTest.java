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

import com.alanbuttars.commons.config.ConfigurationYamlCollectionImpl;
import com.alanbuttars.commons.config.ConfigurationYamlImpl;
import com.alanbuttars.commons.config.eventbus.EventBus;
import com.alanbuttars.commons.config.eventbus.EventBusSyncImpl;
import com.fasterxml.jackson.core.type.TypeReference;

/**
 * Test class for {@link WatchFileYamlImplStub}.
 * 
 * @author Alan Buttars
 *
 */
public class WatchFileYamlImplStubTest {

	@Test
	public void testMappedToClass() throws IOException {
		ConfigurationYamlImpl<User> config = stub("user-yaml").mappedTo(User.class);
		assertEquals("user-yaml", config.getSourceId());
		assertNotNull(config.getValue());
	}

	@Test
	public void testMappedToNullClass() throws IOException {
		Class<User> clazz = null;
		try {
			stub("user-yaml").mappedTo(clazz);
		}
		catch (IllegalArgumentException e) {
			assertEquals("Clazz must be non-null", e.getMessage());
		}
	}

	@Test
	public void testMappedToTypeReference() throws IOException {
		TypeReference<List<User>> typeReference = new TypeReference<List<User>>() {
		};
		ConfigurationYamlCollectionImpl<List<User>> config = stub("users-yaml").mappedTo(typeReference);
		assertEquals("users-yaml", config.getSourceId());
		assertNotNull(config.getValue());
	}

	@Test
	public void testMappedToNullTypeReference() throws IOException {
		TypeReference<List<User>> typeReference = null;
		try {
			stub("users-yaml").mappedTo(typeReference);
		}
		catch (IllegalArgumentException e) {
			assertEquals("Type reference must be non-null", e.getMessage());
		}
	}

	private WatchFileYamlImplStub stub(String sourceId) throws IOException {
		File file = WatchTestHelper.getSourceFile(sourceId);
		EventBus eventBus = new EventBusSyncImpl();
		return new WatchFileYamlImplStub(sourceId, file, eventBus);
	}

}
