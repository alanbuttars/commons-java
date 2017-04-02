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
import static org.junit.Assert.assertTrue;

import java.io.IOException;

import org.junit.Test;

import com.alanbuttars.commons.config.stub.Watch;
import com.alanbuttars.commons.config.stub.WatchTestHelper;

/**
 * Test class for {@link ConfigurationPropertiesImpl}.
 * 
 * @author Alan Buttars
 *
 */
public class ConfigurationPropertiesImplIntegrationTest {

	@Test
	public void testObject() throws IOException {
		ConfigurationPropertiesImpl config = Watch.config(WatchTestHelper.getYaml()).properties("user-properties");
		assertEquals("Harry", config.getString("first.name", null));
		assertEquals("Potter", config.getString("last.name", null));
		assertEquals(11, config.getInt("age", -1));
		assertEquals(1.3, config.getDouble("height", -1.0), 0.001);
		assertTrue(config.getBoolean("male", false));
		assertEquals("The Cupboard Under the Stairs", config.getString("address.line1", null));
		assertEquals("4 Privet Drive", config.getString("address.line2", null));
		assertEquals("Little Whinging", config.getString("address.city", null));
		assertEquals("Surrey", config.getString("address.county", null));
		assertEquals("England", config.getString("address.country", null));
	}

}