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

import static org.mockito.Mockito.never;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.io.File;
import java.io.IOException;

import org.junit.Before;
import org.junit.Test;

import com.alanbuttars.commons.config.event.FileEvent;
import com.alanbuttars.commons.config.event.FileEventType;
import com.alanbuttars.commons.config.eventbus.EventBus;
import com.alanbuttars.commons.config.eventbus.EventBusSyncImpl;
import com.alanbuttars.commons.config.stub.User;

/**
 * Test class for {@link ConfigurationAbstractImpl}.
 * 
 * @author Alan Buttars
 *
 */
public class ConfigurationAbstractImplTest {

	private File file;
	private File otherFile;
	private TestConfiguration config;

	@Before
	public void setup() throws IOException {
		file = File.createTempFile(getClass().getName(), ".tmp");
		otherFile = File.createTempFile(getClass().getName(), ".tmp");
		EventBus eventBus = new EventBusSyncImpl();
		config = spy(new TestConfiguration(file, eventBus));
	}

	@Test
	public void testCreatedFile() throws IOException {
		config.onFileEvent(new FileEvent(file, FileEventType.CREATED));
		verify(config, times(1)).onFileEventMatch(FileEventType.CREATED);
		verify(config, times(1)).load(file);
	}

	@Test
	public void testUnmatchingCreatedFile() throws IOException {
		config.onFileEvent(new FileEvent(otherFile, FileEventType.CREATED));
		verify(config, never()).onFileEventMatch(FileEventType.CREATED);
	}

	@Test
	public void testUpdatedFile() throws IOException {
		config.onFileEvent(new FileEvent(file, FileEventType.UPDATED));
		verify(config, times(1)).onFileEventMatch(FileEventType.UPDATED);
		verify(config, times(1)).load(file);
	}

	@Test
	public void testUnmatchingUpdatedFile() throws IOException {
		config.onFileEvent(new FileEvent(otherFile, FileEventType.UPDATED));
		verify(config, never()).onFileEventMatch(FileEventType.UPDATED);
	}

	@Test
	public void testDeletedFile() throws IOException {
		config.onFileEvent(new FileEvent(file, FileEventType.DELETED));
		verify(config, times(1)).onFileEventMatch(FileEventType.DELETED);
		verify(config, never()).load(file);
	}

	@Test
	public void testUnmatchingDeletedFile() throws IOException {
		config.onFileEvent(new FileEvent(otherFile, FileEventType.DELETED));
		verify(config, never()).onFileEventMatch(FileEventType.DELETED);
	}

	static class TestConfiguration extends ConfigurationAbstractImpl<User> {

		TestConfiguration(File configFile, EventBus eventBus) throws IOException {
			super(configFile);
			initEventBus(eventBus);
		}

		@Override
		public User load(File configFile) throws IOException {
			return new User();
		}

	}

}
