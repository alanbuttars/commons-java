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
package com.alanbuttars.commons.config.source;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import org.junit.Test;

/**
 * Test class for {@link ConfigLoader}.
 * 
 * @author Alan Buttars
 *
 */
public class ConfigLoaderTest {

	@Test
	public void testEnvironmentVariableFile() throws IOException {
		System.setProperty("commons.config", getClass().getResource("example1.yml").getFile());
		Config config = ConfigLoader.load();
		verifyConfig(config);
	}

	@Test
	public void testPassedFile() throws IOException {
		String sourceFilePath = getClass().getResource("example1.yml").getFile();
		File sourceFile = new File(sourceFilePath);
		Config config = ConfigLoader.load(sourceFile);
		verifyConfig(config);
	}

	private void verifyConfig(Config source) {
		assertEquals(60, source.getPollEvery());
		assertEquals(TimeUnit.SECONDS, source.getPollEveryUnit());
		assertEquals(5, source.getConfigItems().size());

		ConfigItem properties = source.getConfigItems().get(0);
		assertEquals("/path/to/database.properties", properties.getFile());
		assertEquals("properties", properties.getType());
		assertEquals(30, properties.getPollEvery());
		assertEquals(TimeUnit.SECONDS, properties.getPollEveryUnit());

		ConfigItem json = source.getConfigItems().get(1);
		assertEquals("/path/to/users.json", json.getFile());
		assertEquals("json", json.getType());
		assertEquals(5, json.getPollEvery());
		assertEquals(TimeUnit.MINUTES, json.getPollEveryUnit());

		ConfigItem xml = source.getConfigItems().get(2);
		assertEquals("/path/to/page-permissions.xml", xml.getFile());
		assertEquals("xml", xml.getType());
		assertEquals(10, xml.getPollEvery());
		assertEquals(TimeUnit.MINUTES, xml.getPollEveryUnit());

		ConfigItem custom = source.getConfigItems().get(3);
		assertEquals("/path/to/custom.csv", custom.getFile());
		assertEquals("com.yourdomain.subdomain.ConfigurationCustomImpl", custom.getType());
		assertEquals(5, custom.getPollEvery());
		assertEquals(TimeUnit.MINUTES, custom.getPollEveryUnit());

		ConfigItem directory = source.getConfigItems().get(4);
		assertEquals("/path/to/client-properties/", directory.getFile());
		assertEquals("directory", directory.getType());
		assertEquals(6, directory.getPollEvery());
		assertEquals(TimeUnit.HOURS, directory.getPollEveryUnit());
	}

}
