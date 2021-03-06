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
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.powermock.api.mockito.PowerMockito.mock;

import java.io.File;
import java.io.IOException;

import org.junit.BeforeClass;
import org.junit.Test;

import com.alanbuttars.commons.config.eventbus.EventBus;
import com.alanbuttars.commons.config.eventbus.EventBusSyncImpl;
import com.alanbuttars.commons.config.stub.Watch;
import com.alanbuttars.commons.config.stub.WatchTestHelper;
import com.alanbuttars.commons.util.functions.Function;

/**
 * Test class for {@link ConfigurationPropertiesImpl}.
 * 
 * @author Alan Buttars
 *
 */
public class ConfigurationPropertiesImplTest {

	private static ConfigurationPropertiesImpl config;
	private static final String GOOD_KEY = "good_key";
	private static final String BAD_KEY = "bad_key";

	@BeforeClass
	public static void setup() throws IOException {
		File propertiesFile = File.createTempFile(ConfigurationPropertiesImplTest.class.getName(), ".properties");
		propertiesFile.deleteOnExit();

		config = new ConfigurationPropertiesImpl("test", propertiesFile, mock(EventBus.class)) {

			@Override
			protected String getValue(String key) {
				if (GOOD_KEY.equals(key)) {
					return "2";
				}
				return null;
			}
		};
	}

	@Test
	public void testGetByte() {
		assertEquals((byte) 2, config.getByte(GOOD_KEY, (byte) 1));
		assertEquals((byte) 1, config.getByte(BAD_KEY, (byte) 1));
	}

	@Test
	public void testGetShort() {
		assertEquals((short) 2, config.getShort(GOOD_KEY, (short) 1));
		assertEquals((short) 1, config.getShort(BAD_KEY, (short) 1));
	}

	@Test
	public void testGetInt() {
		assertEquals(2, config.getInt(GOOD_KEY, 1));
		assertEquals(1, config.getInt(BAD_KEY, 1));
	}

	@Test
	public void testGetLong() {
		assertEquals(2l, config.getLong(GOOD_KEY, 1l));
		assertEquals(1l, config.getLong(BAD_KEY, 1l));
	}

	@Test
	public void testGetFloat() {
		assertEquals(2.0f, config.getFloat(GOOD_KEY, 1.0f), 0.001);
		assertEquals(1.0f, config.getFloat(BAD_KEY, 1.0f), 0.001);
	}

	@Test
	public void testGetDouble() {
		assertEquals(2.0, config.getDouble(GOOD_KEY, 1.0), 0.001);
		assertEquals(1.0, config.getDouble(BAD_KEY, 1.0), 0.001);
	}

	@Test
	public void testGetBoolean() {
		assertFalse(config.getBoolean(GOOD_KEY, false));
		assertFalse(config.getBoolean(BAD_KEY, false));
	}

	@Test
	public void testGetChar() {
		assertEquals('2', config.getChar(GOOD_KEY, '1'));
		assertEquals('1', config.getChar(BAD_KEY, '1'));
	}

	@Test
	public void testGetString() {
		assertEquals("2", config.getString(GOOD_KEY, "1"));
		assertEquals("1", config.getString(BAD_KEY, "1"));
	}

	@Test
	public void testGetValue() {
		Function<String, Integer> function = new Function<String, Integer>() {

			@Override
			public Integer apply(String input) {
				if (input != null) {
					return Integer.parseInt(input);
				}
				return null;
			}

		};
		assertEquals(2, config.getValue(GOOD_KEY, function).intValue());
		assertNull(config.getValue(BAD_KEY));
	}

	@Test
	public void testObject() throws IOException {
		Watch watch = Watch.config(WatchTestHelper.getYaml()).withEventBus(new EventBusSyncImpl());

		ConfigurationPropertiesImpl config = watch.properties("user-properties");
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
