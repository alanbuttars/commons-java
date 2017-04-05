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

import java.io.IOException;
import java.util.Properties;

import org.junit.Before;
import org.junit.Test;

import com.alanbuttars.commons.config.stub.Watch;
import com.alanbuttars.commons.config.util.FileTestHelper;
import com.google.common.base.Joiner;

/**
 * Integration test for {@link ConfigurationPropertiesImpl}.
 * 
 * @author Alan Buttars
 *
 */
public class ConfigurationPropertiesImplIntegrationTest extends ConfigurationIntegrationTest<ConfigurationPropertiesImpl, Properties> {

	@Before
	public void setup() throws IOException {
		String content = Joiner.on("\n").join(//
				"first=Alan", //
				"last=Buttars", //
				"age=25", //
				"male=true");
		setup(content);
	}

	@Override
	protected ConfigurationPropertiesImpl config(Watch watch, String sourceId) throws IOException {
		return watch.properties(sourceId);
	}

	@Test
	public void testInitProperties() {
		assertEquals("Alan", config.getString("first", null));
		assertEquals("Buttars", config.getString("last", null));
		assertEquals(25, config.getInt("age", -1));
		assertTrue(config.getBoolean("male", false));
		assertEquals("brown", config.getString("eye.color", "brown"));
		assertEquals(2, config.getInt("eye.count", 2));
	}

	@Test
	public void testFileUpdated() throws IOException {
		sleep(1);
		assertEquals("brown", config.getString("eye.color", "brown"));

		FileTestHelper.append("\neye.color=green\n", configFile);
		sleep(2);
		assertEquals("green", config.getString("eye.color", "brown"));
	}

	@Test
	public void testFileDeleted() throws IOException {
		sleep(1);
		assertEquals("brown", config.getString("eye.color", "brown"));

		configFile.delete();
		sleep(1);
		assertEquals("brown", config.getString("eye.color", "brown"));
	}

	@Test
	public void testFileCreated() throws IOException {
		sleep(1);
		assertEquals("brown", config.getString("eye.color", "brown"));

		configFile.delete();
		sleep(2);
		assertEquals("brown", config.getString("eye.color", "brown"));

		configFile.createNewFile();
		FileTestHelper.append("\neye.color=green\n", configFile);
		sleep(2);
		assertEquals("green", config.getString("eye.color", "brown"));
	}

	@Test
	public void testFileAttributeChanged() throws IOException {
		sleep(1);
		assertEquals("Alan", config.getString("first", null));

		updateConfigFile(FileTestHelper.file());
		sleep(2);
		assertNull("Alan", config.getString("first", null));
	}

	@Test
	public void testPollEveryAttributeChanged() throws IOException {
		sleep(1);
		assertEquals("brown", config.getString("eye.color", "brown"));

		updateConfigPollEvery(4);
		sleep(2);

		FileTestHelper.append("\neye.color=green\n", configFile);
		sleep(2);
		assertEquals("brown", config.getString("eye.color", "brown"));

		sleep(2);
		assertEquals("green", config.getString("eye.color", "brown"));
	}

	@Test
	public void testMasterPollEveryAttributeChanged() throws IOException {
		sleep(1);
		assertEquals("Alan", config.getString("first", null));

		updateMasterPollEvery(4);
		sleep(2);

		updateConfigFile(FileTestHelper.file());
		sleep(2);
		assertEquals("Alan", config.getString("first", null));

		sleep(2);
		assertNull("Alan", config.getString("first", null));
	}

}
