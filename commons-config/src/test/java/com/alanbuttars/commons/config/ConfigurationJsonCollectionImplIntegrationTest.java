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
package com.alanbuttars.commons.config;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.alanbuttars.commons.config.stub.User;
import com.alanbuttars.commons.config.stub.Watch;
import com.alanbuttars.commons.config.util.FileTestHelper;
import com.fasterxml.jackson.core.type.TypeReference;
import com.google.common.base.Joiner;

/**
 * Integration test for {@link ConfigurationJsonCollectionImpl}.
 * 
 * @author Alan Buttars
 *
 */
public class ConfigurationJsonCollectionImplIntegrationTest extends ConfigurationIntegrationTest<ConfigurationJsonCollectionImpl<List<User>>, List<User>> {

	@Before
	public void setup() throws IOException {
		String content = Joiner.on("\n").join(//
				"[", //
				"  {", //
				"    \"firstName\": \"Alan\",", //
				"    \"lastName\": \"Buttars\",", //
				"    \"age\": 25,", //
				"    \"male\": true", //
				"  },", //
				"  {", //
				"    \"firstName\": \"Harry\",", //
				"    \"lastName\": \"Potter\",", //
				"    \"age\": 11,", //
				"    \"male\": true", //
				"  }", //
				"]");
		setup(content);
	}

	@Override
	protected ConfigurationJsonCollectionImpl<List<User>> config(Watch watch, String sourceId) throws IOException {
		return watch.json(sourceId).mappedTo(new TypeReference<List<User>>() {
		});
	}

	@Test
	public void testInitProperties() {
		assertEquals(2, config.getValue().size());
		assertEquals("Alan", config.getValue().get(0).getFirstName());
		assertEquals("Harry", config.getValue().get(1).getFirstName());
	}

	@Test
	public void testFileUpdated() throws IOException {
		sleep(1);
		assertEquals(2, config.getValue().size());
		assertEquals("Alan", config.getValue().get(0).getFirstName());

		FileTestHelper.write("[{ \"firstName\": \"Sir Alan\" }]", configFile);
		sleep(2);
		assertEquals(1, config.getValue().size());
		assertEquals("Sir Alan", config.getValue().get(0).getFirstName());
	}

	@Test
	public void testFileDeleted() throws IOException {
		sleep(1);
		assertEquals(2, config.getValue().size());
		assertEquals("Alan", config.getValue().get(0).getFirstName());

		configFile.delete();
		sleep(1);
		assertEquals(2, config.getValue().size());
		assertEquals("Alan", config.getValue().get(0).getFirstName());
	}

	@Test
	public void testFileCreated() throws IOException {
		sleep(1);
		assertEquals(2, config.getValue().size());
		assertEquals("Alan", config.getValue().get(0).getFirstName());

		configFile.delete();
		sleep(2);
		assertEquals(2, config.getValue().size());
		assertEquals("Alan", config.getValue().get(0).getFirstName());

		configFile.createNewFile();
		FileTestHelper.write("[{ \"firstName\": \"Sir Alan\" }]", configFile);
		sleep(2);
		assertEquals(1, config.getValue().size());
		assertEquals("Sir Alan", config.getValue().get(0).getFirstName());
	}

	@Test
	public void testFileAttributeChanged() throws IOException {
		sleep(1);
		assertEquals(2, config.getValue().size());
		assertEquals("Alan", config.getValue().get(0).getFirstName());

		File newConfigFile = FileTestHelper.file();
		FileTestHelper.write("[]", newConfigFile);
		updateConfigFile(newConfigFile);
		sleep(2);
		assertEquals(0, config.getValue().size());
	}

	@Test
	public void testPollEveryAttributeChanged() throws IOException {
		sleep(1);
		assertEquals(2, config.getValue().size());
		assertEquals("Alan", config.getValue().get(0).getFirstName());

		updateConfigPollEvery(4);
		sleep(2);

		FileTestHelper.write("[{ \"firstName\": \"Sir Alan\" }]", configFile);
		sleep(2);
		assertEquals(2, config.getValue().size());
		assertEquals("Alan", config.getValue().get(0).getFirstName());

		sleep(2);
		assertEquals(1, config.getValue().size());
		assertEquals("Sir Alan", config.getValue().get(0).getFirstName());
	}

	@Test
	public void testMasterPollEveryAttributeChanged() throws IOException {
		sleep(1);
		assertEquals(2, config.getValue().size());
		assertEquals("Alan", config.getValue().get(0).getFirstName());

		updateMasterPollEvery(4);
		sleep(2);

		File newConfigFile = FileTestHelper.file();
		FileTestHelper.write("[]", newConfigFile);
		updateConfigFile(newConfigFile);
		sleep(2);
		assertEquals(2, config.getValue().size());
		assertEquals("Alan", config.getValue().get(0).getFirstName());

		sleep(2);
		assertEquals(0, config.getValue().size());
	}

}
