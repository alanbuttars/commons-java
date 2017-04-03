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
import static org.junit.Assert.fail;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.xml.bind.JAXBException;

import org.junit.Before;
import org.junit.Test;

import com.alanbuttars.commons.config.ConfigurationJsonCollectionImpl;
import com.alanbuttars.commons.config.ConfigurationJsonImpl;
import com.alanbuttars.commons.config.ConfigurationPropertiesImpl;
import com.alanbuttars.commons.config.ConfigurationXmlImpl;
import com.alanbuttars.commons.config.ConfigurationYamlCollectionImpl;
import com.alanbuttars.commons.config.ConfigurationYamlImpl;
import com.alanbuttars.commons.config.eventbus.EventBus;
import com.alanbuttars.commons.config.eventbus.EventBusSyncImpl;
import com.fasterxml.jackson.core.type.TypeReference;

/**
 * Test class for {@link Watch}.
 * 
 * @author Alan Buttars
 *
 */
public class WatchTest {

	private static final String YAML_FILE_PATH = WatchTestHelper.getYaml();
	private EventBus eventBus;
	private Watch watch;

	@Before
	public void setup() throws IOException {
		this.eventBus = new EventBusSyncImpl();
		this.watch = Watch.config(YAML_FILE_PATH).withEventBus(eventBus);
	}

	@Test
	public void testConfig() throws IOException {
		System.setProperty("commons.config", YAML_FILE_PATH);
		YamlConfig config = Watch.config().withEventBus(eventBus).getConfig();
		verifyConfig(config);
	}

	@Test
	public void testConfigFilePath() throws IOException {
		YamlConfig config = watch.getConfig();
		verifyConfig(config);
	}

	@Test
	public void testNullSourceId() throws IOException {
		try {
			watch.properties(null);
			fail();
		}
		catch (IllegalArgumentException e) {
			assertEquals("Source ID must be non-null", e.getMessage());
		}
	}

	@Test
	public void testEmptySourceId() throws IOException {
		try {
			watch.properties("");
			fail();
		}
		catch (IllegalArgumentException e) {
			assertEquals("Source ID must be non-empty", e.getMessage());
		}
	}

	@Test
	public void testNonexistentSourceId() throws IOException {
		try {
			watch.properties("i-dont-exist");
			fail();
		}
		catch (IllegalArgumentException e) {
			assertEquals("Configuration does not exist for source ID 'i-dont-exist'", e.getMessage());
		}
	}

	@Test
	public void testNullFileAttribute() throws IOException {
		try {
			watch.properties("null-file");
			fail();
		}
		catch (IllegalArgumentException e) {
			assertEquals("Configuration for source ID 'null-file' is missing the file attribute", e.getMessage());
		}
	}

	@Test
	public void testEmptyFileAttribute() throws IOException {
		try {
			watch.properties("empty-file");
			fail();
		}
		catch (IllegalArgumentException e) {
			assertEquals("Configuration for source ID 'empty-file' is missing the file attribute", e.getMessage());
		}
	}

	@Test
	public void testNonexistingFileAttribute() throws IOException {
		try {
			watch.properties("nonexisting-file");
			fail();
		}
		catch (IllegalArgumentException e) {
			assertEquals("Configuration for source ID 'nonexisting-file' has file attribute 'i-dont-exist', which does not exist", e.getMessage());
		}
	}

	@Test
	public void testUnreadableFileAttribute() throws IOException {
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
	public void testJsonClass() throws IOException {
		ConfigurationJsonImpl<User> config = watch.json("user-json").mappedTo(User.class).withEventBus(eventBus);
		assertNotNull(config);
		assertNotNull(config.getValue());
		assertEquals(User.class, config.getValue().getClass());
	}

	@Test
	public void testJsonTypeReference() throws IOException {
		ConfigurationJsonCollectionImpl<List<User>> config = watch.json("users-json").mappedTo(new TypeReference<List<User>>() {
		}).withEventBus(eventBus);
		assertNotNull(config);
		assertNotNull(config.getValue());
		assertEquals(ArrayList.class, config.getValue().getClass());
	}

	@Test
	public void testProperties() throws IOException {
		ConfigurationPropertiesImpl config = watch.properties("user-properties").withEventBus(eventBus);
		assertNotNull(config);
	}

	@Test
	public void testXmlClass() throws IOException, JAXBException {
		ConfigurationXmlImpl<User> config = watch.xml("user-xml").mappedTo(User.class).withEventBus(eventBus);
		assertNotNull(config);
		assertNotNull(config.getValue());
		assertEquals(User.class, config.getValue().getClass());
	}

	@Test
	public void testYamlClass() throws IOException {
		ConfigurationYamlImpl<User> config = watch.yaml("user-yaml").mappedTo(User.class).withEventBus(eventBus);
		assertNotNull(config);
		assertNotNull(config.getValue());
		assertEquals(User.class, config.getValue().getClass());
	}

	@Test
	public void testYamlTypeReference() throws IOException {
		ConfigurationYamlCollectionImpl<List<User>> config = watch.yaml("users-yaml").mappedTo(new TypeReference<List<User>>() {
		}).withEventBus(eventBus);
		assertNotNull(config);
		assertNotNull(config.getValue());
		assertEquals(ArrayList.class, config.getValue().getClass());
	}

	private void verifyConfig(YamlConfig source) {
		assertEquals(60, source.getPollEvery());
		assertEquals(TimeUnit.SECONDS, source.getPollEveryUnit());
		assertEquals(13, source.getConfigFiles().size());

		YamlConfigFile properties = source.getConfigFiles().get("user-properties");
		assertEquals("src/test/resources/com/alanbuttars/commons/config/stub/user.properties", properties.getFile());
		assertEquals(30, properties.getPollEvery());
		assertEquals(TimeUnit.SECONDS, properties.getPollEveryUnit());

		YamlConfigFile json = source.getConfigFiles().get("users-json");
		assertEquals("src/test/resources/com/alanbuttars/commons/config/stub/users.json", json.getFile());
		assertEquals(5, json.getPollEvery());
		assertEquals(TimeUnit.MINUTES, json.getPollEveryUnit());

		YamlConfigFile xml = source.getConfigFiles().get("users-xml");
		assertEquals("src/test/resources/com/alanbuttars/commons/config/stub/users.xml", xml.getFile());
		assertEquals(10, xml.getPollEvery());
		assertEquals(TimeUnit.MINUTES, xml.getPollEveryUnit());

		YamlConfigFile yaml = source.getConfigFiles().get("users-yaml");
		assertEquals("src/test/resources/com/alanbuttars/commons/config/stub/users.yml", yaml.getFile());
		assertEquals(10, yaml.getPollEvery());
		assertEquals(TimeUnit.MINUTES, yaml.getPollEveryUnit());

		YamlConfigFile custom = source.getConfigFiles().get("users-custom");
		assertEquals("src/test/resources/com/alanbuttars/commons/config/stub/users.csv", custom.getFile());
		assertEquals(5, custom.getPollEvery());
		assertEquals(TimeUnit.MINUTES, custom.getPollEveryUnit());

		YamlConfigFile directory = source.getConfigFiles().get("client-properties");
		assertEquals("/path/to/client-properties/", directory.getFile());
		assertEquals(6, directory.getPollEvery());
		assertEquals(TimeUnit.HOURS, directory.getPollEveryUnit());
	}

}
