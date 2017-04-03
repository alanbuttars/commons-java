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

import org.junit.Before;

import com.alanbuttars.commons.config.eventbus.EventBus;
import com.alanbuttars.commons.config.eventbus.EventBusSyncImpl;
import com.alanbuttars.commons.config.stub.User;
import com.alanbuttars.commons.config.stub.Watch;
import com.alanbuttars.commons.config.stub.WatchTestHelper;

/**
 * Abstract integration test.
 * 
 * @author Alan Buttars
 *
 */
abstract class ConfigurationAbstractIntegrationTest {

	public void verifyHarry(User harry) {
		assertEquals("Harry", harry.getFirstName());
		assertEquals("Potter", harry.getLastName());
		assertEquals(11, harry.getAge());
		assertEquals(1.3, harry.getHeight(), 0.001);
		assertTrue(harry.isMale());
		assertEquals("The Cupboard Under the Stairs", harry.getAddress().getLine1());
		assertEquals("4 Privet Drive", harry.getAddress().getLine2());
		assertEquals("Little Whinging", harry.getAddress().getCity());
		assertEquals("Surrey", harry.getAddress().getCounty());
		assertEquals("England", harry.getAddress().getCountry());
	}

	public void verifySherman(User sherman) {
		assertEquals("Philip", sherman.getFirstName());
		assertEquals("Sherman", sherman.getLastName());
		assertEquals(45, sherman.getAge());
		assertEquals(1.9, sherman.getHeight(), 0.001);
		assertTrue(sherman.isMale());
		assertEquals("42 Wallaby Way", sherman.getAddress().getLine1());
		assertNull(sherman.getAddress().getLine2());
		assertEquals("Sydnay", sherman.getAddress().getCity());
		assertEquals("New South Wales", sherman.getAddress().getCounty());
		assertEquals("Australia", sherman.getAddress().getCountry());
	}

}
