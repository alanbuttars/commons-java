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

import java.io.File;
import java.io.IOException;

import org.junit.Test;

import com.alanbuttars.commons.config.eventbus.EventBus;
import com.alanbuttars.commons.config.eventbus.EventBusSyncImpl;
import com.alanbuttars.commons.config.util.ConfigurationUsersImpl;
import com.alanbuttars.commons.util.functions.Function;

/**
 * Test class for {@link WatchFileCustomImplStub}.
 * 
 * @author Alan Buttars
 *
 */
public class WatchFileCustomImplStubTest {

	@Test
	public void testMappedWith() throws IOException {
		ConfigurationUsersImpl config = stub("users-csv").mappedWith(new Function<CustomConfigurationParams, ConfigurationUsersImpl>() {

			@Override
			public ConfigurationUsersImpl apply(CustomConfigurationParams input) {
				try {
					return new ConfigurationUsersImpl(input.getSourceId(), input.getFile(), input.getEventBus());
				}
				catch (IOException e) {
					throw new RuntimeException(e);
				}
			}

		});
		assertEquals("users-csv", config.getSourceId());
		assertNotNull(config.getValue());
	}

	@Test
	public void testMappedWithNull() throws IOException {
		try {
			stub("users-csv").mappedWith(null);
		}
		catch (IllegalArgumentException e) {
			assertEquals("Config function must be non-null", e.getMessage());
		}
	}

	private WatchFileCustomImplStub stub(String sourceId) throws IOException {
		File file = WatchTestHelper.getSourceFile(sourceId);
		EventBus eventBus = new EventBusSyncImpl();
		return new WatchFileCustomImplStub(sourceId, file, eventBus);
	}
}
