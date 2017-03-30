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
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import org.junit.Test;

import com.alanbuttars.commons.config.ConfigurationPropertiesImpl;

/**
 * Test class for {@link Watch}.
 * 
 * @author Alan Buttars
 *
 */
public class WatchTest {

	private static final String YAML_FILE_PATH = WatchTest.class.getResource("commons.config.1.yml").getFile();

	@Test
	public void testYaml() throws IOException {
		System.setProperty("commons.config", YAML_FILE_PATH);
		YamlConfig config = Watch.yaml().getConfig();
		verifyConfig(config);
	}

	@Test
	public void testYamlFilePath() throws IOException {
		YamlConfig config = Watch.yaml(YAML_FILE_PATH).getConfig();
		verifyConfig(config);
	}

	@Test
	public void testNullSourceId() throws IOException {
		try {
			Watch.yaml(YAML_FILE_PATH).properties(null);
			fail();
		}
		catch (IllegalArgumentException e) {
			assertEquals("Source ID must be non-null", e.getMessage());
		}
	}

	@Test
	public void testEmptySourceId() throws IOException {
		try {
			Watch.yaml(YAML_FILE_PATH).properties("");
			fail();
		}
		catch (IllegalArgumentException e) {
			assertEquals("Source ID must be non-empty", e.getMessage());
		}
	}

	@Test
	public void testNonexistentSourceId() throws IOException {
		try {
			Watch.yaml(YAML_FILE_PATH).properties("i-dont-exist");
			fail();
		}
		catch (IllegalArgumentException e) {
			assertEquals("Configuration does not exist for source ID 'i-dont-exist'", e.getMessage());
		}
	}

	@Test
	public void testNullFileAttribute() throws IOException {
		try {
			Watch.yaml(YAML_FILE_PATH).properties("null-file");
			fail();
		}
		catch (IllegalArgumentException e) {
			assertEquals("Configuration for source ID 'null-file' is missing the file attribute", e.getMessage());
		}
	}

	@Test
	public void testEmptyFileAttribute() throws IOException {
		try {
			Watch.yaml(YAML_FILE_PATH).properties("empty-file");
			fail();
		}
		catch (IllegalArgumentException e) {
			assertEquals("Configuration for source ID 'empty-file' is missing the file attribute", e.getMessage());
		}
	}

	@Test
	public void testNonexistingFileAttribute() throws IOException {
		try {
			Watch.yaml(YAML_FILE_PATH).properties("nonexisting-file");
			fail();
		}
		catch (IllegalArgumentException e) {
			assertEquals("Configuration for source ID 'nonexisting-file' has file attribute 'i-dont-exist', which does not exist", e.getMessage());
		}
	}

	@Test
	public void testUnreadableFileAttribute() throws IOException {
		Watch watch = Watch.yaml(YAML_FILE_PATH);
		YamlConfigFile unreadableConfig = watch.getConfig().getConfigFiles().get("unreadable-file");
		File tempFile = File.createTempFile(getClass().getName(), ".tmp");
		tempFile.deleteOnExit();
		tempFile.setReadable(false);
		unreadableConfig.setFile(tempFile.getAbsolutePath());
		try {
			watch.properties("unreadable-file");
			fail();
		}
		catch (IllegalArgumentException e) {
			assertEquals("Configuration for source ID 'unreadable-file' has file attribute '" + tempFile.getAbsolutePath() + "', which is unreadable", e.getMessage());
		}
	}

	@Test
	public void testProperties() throws IOException {
		ConfigurationPropertiesImpl properties = Watch.yaml(YAML_FILE_PATH).properties("database");
		assertEquals(100, properties.getInt("int.property", -1));
		assertEquals(2.2, properties.getDouble("double.property", -1.0), 0.001);
		assertTrue(properties.getBoolean("boolean.property", false));
		assertEquals("freaksandgeeks", properties.getString("string.property", null));
	}

	private void verifyConfig(YamlConfig source) {
		assertEquals(60, source.getPollEvery());
		assertEquals(TimeUnit.SECONDS, source.getPollEveryUnit());
		assertEquals(9, source.getConfigFiles().size());

		YamlConfigFile properties = source.getConfigFiles().get("database");
		assertEquals("src/test/resources/com/alanbuttars/commons/config/stub/database.properties", properties.getFile());
		assertEquals(30, properties.getPollEvery());
		assertEquals(TimeUnit.SECONDS, properties.getPollEveryUnit());

		YamlConfigFile json = source.getConfigFiles().get("users");
		assertEquals("/path/to/users.json", json.getFile());
		assertEquals(5, json.getPollEvery());
		assertEquals(TimeUnit.MINUTES, json.getPollEveryUnit());

		YamlConfigFile xml = source.getConfigFiles().get("page-permissions");
		assertEquals("/path/to/page-permissions.xml", xml.getFile());
		assertEquals(10, xml.getPollEvery());
		assertEquals(TimeUnit.MINUTES, xml.getPollEveryUnit());

		YamlConfigFile custom = source.getConfigFiles().get("custom");
		assertEquals("/path/to/custom.csv", custom.getFile());
		assertEquals(5, custom.getPollEvery());
		assertEquals(TimeUnit.MINUTES, custom.getPollEveryUnit());

		YamlConfigFile directory = source.getConfigFiles().get("client-properties");
		assertEquals("/path/to/client-properties/", directory.getFile());
		assertEquals(6, directory.getPollEvery());
		assertEquals(TimeUnit.HOURS, directory.getPollEveryUnit());
	}

}
