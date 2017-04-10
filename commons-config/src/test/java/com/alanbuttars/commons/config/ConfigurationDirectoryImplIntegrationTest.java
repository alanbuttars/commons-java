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
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import com.alanbuttars.commons.config.eventbus.EventBusSyncImpl;
import com.alanbuttars.commons.config.stub.Watch;
import com.alanbuttars.commons.config.util.FileTestHelper;

/**
 * Integration test for {@link ConfigurationDirectoryImpl}.
 * 
 * @author Alan Buttars
 *
 */
public class ConfigurationDirectoryImplIntegrationTest extends ConfigurationIntegrationTest<ConfigurationDirectoryImpl, Map<String, File>> {

	private File file1;
	private File file2;
	private File file3;

	@Before
	public void setup() throws IOException {
		this.eventBus = new EventBusSyncImpl();

		this.configFile = FileTestHelper.directory();
		file1 = new File(configFile, "file1.txt");
		file1.createNewFile();

		File file2Parent = new File(configFile, "nest");
		file2Parent.mkdir();
		file2 = new File(file2Parent, "file2.txt");
		file2.createNewFile();

		File file3Parent = new File(file2Parent, "nest");
		file3Parent.mkdir();
		file3 = new File(file3Parent, "file3.txt");
		file3.createNewFile();

		this.yamlConfig = FileTestHelper.file();
		updateConfigFile(configFile);

		Watch watch = Watch.config(yamlConfig.getAbsolutePath()).withEventBus(eventBus);

		config = config(watch, "config");
	}

	@Override
	protected ConfigurationDirectoryImpl config(Watch watch, String sourceId) throws IOException {
		return watch.directory(sourceId);
	}

	@Test
	public void testInitProperties() {
		assertEquals(file1, config.getValue().get("file1.txt"));
		assertEquals(file2, config.getValue().get("nest/file2.txt"));
		assertEquals(file3, config.getValue().get("nest/nest/file3.txt"));
	}

	@Test
	public void testFileUpdated() throws IOException {
		sleep(1);
		assertEquals(file1, config.getValue().get("file1.txt"));
		long timestamp = file1.lastModified();

		FileTestHelper.write("{ \"firstName\": \"Sir Alan\" }", file1);
		sleep(2);
		assertEquals(file1, config.getValue().get("file1.txt"));
		assertTrue(config.getValue().get("file1.txt").lastModified() > timestamp);
	}

	@Test
	public void testFileDeleted() throws IOException {
		sleep(1);
		assertEquals(file1, config.getValue().get("file1.txt"));

		file1.delete();
		sleep(2);
		assertNull(config.getValue().get("file1.txt"));
	}

	@Test
	public void testFileCreated() throws IOException {
		sleep(1);
		assertEquals(file1, config.getValue().get("file1.txt"));

		file1.delete();
		sleep(2);
		assertNull(config.getValue().get("file1.txt"));

		file1.createNewFile();
		sleep(2);
		assertEquals(file1, config.getValue().get("file1.txt"));
	}

	@Test
	public void testFileAttributeChanged() throws IOException {
		sleep(1);
		assertEquals(file1, config.getValue().get("file1.txt"));

		File newConfigFile = FileTestHelper.directory();
		updateConfigFile(newConfigFile);
		sleep(2);
		assertNull(config.getValue().get("file1.txt"));
	}

	@Test
	public void testMasterPollEveryAttributeChanged() throws IOException {
		sleep(1);
		assertEquals(file1, config.getValue().get("file1.txt"));

		updateMasterPollEvery(4);
		sleep(2);

		File newConfigFile = FileTestHelper.directory();
		updateConfigFile(newConfigFile);
		sleep(2);
		assertEquals(file1, config.getValue().get("file1.txt"));

		sleep(2);
		assertNull(config.getValue().get("file1.txt"));
	}

}
