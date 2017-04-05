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
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;

import org.junit.Before;
import org.junit.Test;

import com.alanbuttars.commons.config.stub.User;
import com.alanbuttars.commons.config.stub.Watch;
import com.alanbuttars.commons.config.util.FileTestHelper;
import com.google.common.base.Joiner;

/**
 * Integration test for {@link ConfigurationJsonImpl}.
 * 
 * @author Alan Buttars
 *
 */
public class ConfigurationJsonImplIntegrationTest extends ConfigurationIntegrationTest<ConfigurationJsonImpl<User>, User> {

	@Before
	public void setup() throws IOException {
		String content = Joiner.on("\n").join(//
				"{", //
				"  \"firstName\": \"Alan\",", //
				"  \"lastName\": \"Buttars\",", //
				"  \"age\": 25,", //
				"  \"male\": true", //
				"}");
		setup(content);
	}

	@Override
	protected ConfigurationJsonImpl<User> config(Watch watch, String sourceId) throws IOException {
		return watch.json(sourceId).mappedTo(User.class);
	}

	@Test
	public void testInitProperties() {
		assertEquals("Alan", config.getValue().getFirstName());
		assertEquals("Buttars", config.getValue().getLastName());
		assertEquals(25, config.getValue().getAge());
		assertTrue(config.getValue().isMale());
	}

	@Test
	public void testFileUpdated() throws IOException {
		sleep(1);
		assertEquals("Alan", config.getValue().getFirstName());

		FileTestHelper.write("{ \"firstName\": \"Sir Alan\" }", configFile);
		sleep(2);
		assertEquals("Sir Alan", config.getValue().getFirstName());
	}

	@Test
	public void testFileDeleted() throws IOException {
		sleep(1);
		assertEquals("Alan", config.getValue().getFirstName());

		configFile.delete();
		sleep(1);
		assertEquals("Alan", config.getValue().getFirstName());
	}

	@Test
	public void testFileCreated() throws IOException {
		sleep(1);
		assertEquals("Alan", config.getValue().getFirstName());

		configFile.delete();
		sleep(2);
		assertEquals("Alan", config.getValue().getFirstName());

		configFile.createNewFile();
		FileTestHelper.write("{ \"firstName\": \"Sir Alan\" }", configFile);
		sleep(2);
		assertEquals("Sir Alan", config.getValue().getFirstName());
	}

	@Test
	public void testFileAttributeChanged() throws IOException {
		sleep(1);
		assertEquals("Alan", config.getValue().getFirstName());

		File newConfigFile = FileTestHelper.file();
		FileTestHelper.write("{}", newConfigFile);
		updateConfigFile(newConfigFile);
		sleep(2);
		assertNull(config.getValue().getFirstName());
	}

	@Test
	public void testPollEveryAttributeChanged() throws IOException {
		sleep(1);
		assertEquals("Alan", config.getValue().getFirstName());

		updateConfigPollEvery(4);
		sleep(2);

		FileTestHelper.write("{ \"firstName\": \"Sir Alan\" }", configFile);
		sleep(2);
		assertEquals("Alan", config.getValue().getFirstName());

		sleep(2);
		assertEquals("Sir Alan", config.getValue().getFirstName());
	}

	@Test
	public void testMasterPollEveryAttributeChanged() throws IOException {
		sleep(1);
		assertEquals("Alan", config.getValue().getFirstName());

		updateMasterPollEvery(4);
		sleep(2);

		File newConfigFile = FileTestHelper.file();
		FileTestHelper.write("{}", newConfigFile);
		updateConfigFile(newConfigFile);
		sleep(2);
		assertEquals("Alan", config.getValue().getFirstName());

		sleep(2);
		assertNull("Alan", config.getValue().getFirstName());
	}

}
