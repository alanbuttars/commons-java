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
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.powermock.api.mockito.PowerMockito.mock;

import java.io.File;
import java.io.IOException;

import org.junit.Before;
import org.junit.Test;

import com.alanbuttars.commons.config.eventbus.EventBus;

/**
 * Test class for {@link WatchStub}.
 * 
 * @author Alan Buttars
 *
 */
public class WatchStubTest {

	private File file;
	private EventBus eventBus;

	@Before
	public void setup() throws IOException {
		this.file = new File(WatchTestHelper.getYaml());
		this.eventBus = mock(EventBus.class);
	}

	@Test
	public void testWithEventBus() throws IOException {
		Watch watch = new WatchStub(file).withEventBus(eventBus);
		verify(eventBus, times(1)).subscribe(watch);
	}

	@Test
	public void testWithNullEventBus() throws IOException {
		try {
			new WatchStub(file).withEventBus(null);
		}
		catch (IllegalArgumentException e) {
			assertEquals("Event bus must be non-null", e.getMessage());
		}
	}
}
