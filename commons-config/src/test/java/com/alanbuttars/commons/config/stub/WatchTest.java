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
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.powermock.api.mockito.PowerMockito.doReturn;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import com.alanbuttars.commons.config.ConfigurationJsonCollectionImpl;
import com.alanbuttars.commons.config.ConfigurationJsonImpl;
import com.alanbuttars.commons.config.ConfigurationPropertiesImpl;
import com.alanbuttars.commons.config.ConfigurationXmlImpl;
import com.alanbuttars.commons.config.ConfigurationYamlCollectionImpl;
import com.alanbuttars.commons.config.ConfigurationYamlImpl;
import com.alanbuttars.commons.config.event.FileEvent;
import com.alanbuttars.commons.config.event.FileEventType;
import com.alanbuttars.commons.config.eventbus.EventBus;
import com.alanbuttars.commons.config.eventbus.EventBusSyncImpl;
import com.alanbuttars.commons.config.master.YamlConfig;
import com.alanbuttars.commons.config.master.YamlFileConfig;
import com.fasterxml.jackson.core.type.TypeReference;
import com.google.common.io.Files;

/**
 * Test class for {@link Watch}.
 * 
 * @author Alan Buttars
 *
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest(Watch.class)
public class WatchTest {

	private static final String YAML_FILE_PATH = WatchTestHelper.getYaml();
	private EventBus eventBus;
	private Watch watch;

	@Before
	public void setup() throws IOException {
		this.eventBus = new EventBusSyncImpl();
		this.watch = spy(Watch.config(YAML_FILE_PATH).withEventBus(eventBus));
	}

	@Test
	public void testConfigSystemProperty() throws IOException {
		System.setProperty("commons.config", YAML_FILE_PATH);
		Watch watch = Watch.config().withEventBus(eventBus);
		assertEquals("master-yaml", watch.getSourceId());
		YamlConfig config = watch.getValue();
		verifyConfig(config);
	}

	@Test
	public void testConfigNullSystemProperty() throws IOException {
		try {
			System.clearProperty("commons.config");
			Watch.config().withEventBus(eventBus);
			fail();
		}
		catch (IllegalArgumentException e) {
			assertEquals("System property for commons.config must be non-null", e.getMessage());
		}
	}

	@Test
	public void testConfigEmptySystemProperty() throws IOException {
		try {
			System.setProperty("commons.config", "");
			Watch.config().withEventBus(eventBus);
			fail();
		}
		catch (IllegalArgumentException e) {
			assertEquals("System property for commons.config must be non-empty", e.getMessage());
		}
	}

	@Test
	public void testConfigBlankSystemProperty() throws IOException {
		try {
			System.setProperty("commons.config", "  ");
			Watch.config().withEventBus(eventBus);
			fail();
		}
		catch (IllegalArgumentException e) {
			assertEquals("System property for commons.config must be non-empty", e.getMessage());
		}
	}

	@Test
	public void testConfigNonexistingSystemProperty() throws IOException {
		File yaml = new File("i-dont-exist");
		try {
			System.setProperty("commons.config", yaml.getName());
			Watch.config().withEventBus(eventBus);
			fail();
		}
		catch (IllegalArgumentException e) {
			assertEquals("System property for commons.config " + yaml.getAbsolutePath() + " does not exist", e.getMessage());
		}
	}

	@Test
	public void testConfigDirectorySystemProperty() throws IOException {
		File yaml = Files.createTempDir();
		yaml.deleteOnExit();
		try {
			System.setProperty("commons.config", yaml.getAbsolutePath());
			Watch.config().withEventBus(eventBus);
			fail();
		}
		catch (IllegalArgumentException e) {
			assertEquals("System property for commons.config " + yaml.getAbsolutePath() + " is a directory; it must be a file", e.getMessage());
		}
	}

	@Test
	public void testConfigUnreadableSystemProperty() throws IOException {
		File yaml = File.createTempFile(getClass().getName(), ".tmp");
		yaml.deleteOnExit();
		yaml.setReadable(false);
		try {
			System.setProperty("commons.config", yaml.getAbsolutePath());
			Watch.config().withEventBus(eventBus);
			fail();
		}
		catch (IllegalArgumentException e) {
			assertEquals("System property for commons.config " + yaml.getAbsolutePath() + " is not readable", e.getMessage());
		}
	}

	@Test
	public void testConfigFilePath() throws IOException {
		assertEquals("master-yaml", watch.getSourceId());
		YamlConfig config = watch.getValue();
		verifyConfig(config);
	}

	@Test
	public void testConfigNullFilePath() throws IOException {
		try {
			Watch.config(null).withEventBus(eventBus);
			fail();
		}
		catch (IllegalArgumentException e) {
			assertEquals("YAML file path must be non-null", e.getMessage());
		}
	}

	@Test
	public void testConfigEmptyFilePath() throws IOException {
		try {
			Watch.config("").withEventBus(eventBus);
			fail();
		}
		catch (IllegalArgumentException e) {
			assertEquals("YAML file path must be non-empty", e.getMessage());
		}
	}

	@Test
	public void testConfigBlankFilePath() throws IOException {
		try {
			Watch.config("  ").withEventBus(eventBus);
			fail();
		}
		catch (IllegalArgumentException e) {
			assertEquals("YAML file path must be non-empty", e.getMessage());
		}
	}

	@Test
	public void testConfigNonexistingFilePath() throws IOException {
		File yaml = new File("i-dont-exist");
		try {
			Watch.config(yaml.getName()).withEventBus(eventBus);
			fail();
		}
		catch (IllegalArgumentException e) {
			assertEquals("YAML file path " + yaml.getAbsolutePath() + " does not exist", e.getMessage());
		}
	}

	@Test
	public void testConfigDirectoryFilePath() throws IOException {
		File yaml = Files.createTempDir();
		yaml.deleteOnExit();
		try {
			Watch.config(yaml.getAbsolutePath()).withEventBus(eventBus);
			fail();
		}
		catch (IllegalArgumentException e) {
			assertEquals("YAML file path " + yaml.getAbsolutePath() + " is a directory; it must be a file", e.getMessage());
		}
	}

	@Test
	public void testConfigUnreadableFilePath() throws IOException {
		File yaml = File.createTempFile(getClass().getName(), ".tmp");
		yaml.deleteOnExit();
		yaml.setReadable(false);
		try {
			Watch.config(yaml.getAbsolutePath()).withEventBus(eventBus);
			fail();
		}
		catch (IllegalArgumentException e) {
			assertEquals("YAML file path " + yaml.getAbsolutePath() + " is not readable", e.getMessage());
		}
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
	public void testJsonClass() throws IOException {
		ConfigurationJsonImpl<User> config = watch.json("user-json").mappedTo(User.class);
		assertNotNull(config);
		assertNotNull(config.getValue());
		assertEquals(User.class, config.getValue().getClass());
	}

	@Test
	public void testJsonTypeReference() throws IOException {
		ConfigurationJsonCollectionImpl<List<User>> config = watch.json("users-json").mappedTo(new TypeReference<List<User>>() {
		});
		assertNotNull(config);
		assertNotNull(config.getValue());
		assertEquals(ArrayList.class, config.getValue().getClass());
	}

	@Test
	public void testProperties() throws IOException {
		ConfigurationPropertiesImpl config = watch.properties("user-properties");
		assertNotNull(config);
	}

	@Test
	public void testXmlClass() throws IOException {
		ConfigurationXmlImpl<User> config = watch.xml("user-xml").mappedTo(User.class);
		assertNotNull(config);
		assertNotNull(config.getValue());
		assertEquals(User.class, config.getValue().getClass());
	}

	@Test
	public void testYamlClass() throws IOException {
		ConfigurationYamlImpl<User> config = watch.yaml("user-yaml").mappedTo(User.class);
		assertNotNull(config);
		assertNotNull(config.getValue());
		assertEquals(User.class, config.getValue().getClass());
	}

	@Test
	public void testYamlTypeReference() throws IOException {
		ConfigurationYamlCollectionImpl<List<User>> config = watch.yaml("users-yaml").mappedTo(new TypeReference<List<User>>() {
		});
		assertNotNull(config);
		assertNotNull(config.getValue());
		assertEquals(ArrayList.class, config.getValue().getClass());
	}

	@Test
	public void testReloadSchedulesExecutor() throws IOException {
		ScheduledThreadPoolExecutor mockExecutor = mock(ScheduledThreadPoolExecutor.class);
		doReturn(mockExecutor).when(watch).getExecutor();

		watch.onFileEvent(new FileEvent(Watch.SOURCE_ID, new File(YAML_FILE_PATH), FileEventType.UPDATED));

		verify(mockExecutor, times(1)).shutdownNow();
		verify(mockExecutor, times(7)).scheduleAtFixedRate(any(Runnable.class), anyLong(), anyLong(), any(TimeUnit.class));
	}

	@Test
	public void testReloadReinitializesExecutor() throws IOException {
		assertEquals(10, watch.getExecutor().getCorePoolSize());

		YamlConfig config = watch.getValue();
		config.getMaster().setPoolSize(5);
		doReturn(config).when(watch).load(new File(YAML_FILE_PATH));
		watch.onFileEvent(new FileEvent(Watch.SOURCE_ID, new File(YAML_FILE_PATH), FileEventType.UPDATED));

		assertEquals(5, watch.getExecutor().getCorePoolSize());
	}

	private void verifyConfig(YamlConfig source) {
		assertEquals(60, source.getMaster().getPollEvery());
		assertEquals(TimeUnit.SECONDS, source.getMaster().getPollEveryUnit());
		assertEquals(6, source.getFileConfigs().size());

		YamlFileConfig properties = source.getFileConfigs().get("user-properties");
		assertEquals("src/test/resources/com/alanbuttars/commons/config/stub/user.properties", properties.getFile());
		assertEquals(30, properties.getPollEvery());
		assertEquals(TimeUnit.SECONDS, properties.getPollEveryUnit());

		YamlFileConfig json = source.getFileConfigs().get("users-json");
		assertEquals("src/test/resources/com/alanbuttars/commons/config/stub/users.json", json.getFile());
		assertEquals(5, json.getPollEvery());
		assertEquals(TimeUnit.MINUTES, json.getPollEveryUnit());

		YamlFileConfig xml = source.getFileConfigs().get("user-xml");
		assertEquals("src/test/resources/com/alanbuttars/commons/config/stub/user.xml", xml.getFile());
		assertEquals(10, xml.getPollEvery());
		assertEquals(TimeUnit.MINUTES, xml.getPollEveryUnit());

		YamlFileConfig yaml = source.getFileConfigs().get("users-yaml");
		assertEquals("src/test/resources/com/alanbuttars/commons/config/stub/users.yml", yaml.getFile());
		assertEquals(10, yaml.getPollEvery());
		assertEquals(TimeUnit.MINUTES, yaml.getPollEveryUnit());
	}

}
