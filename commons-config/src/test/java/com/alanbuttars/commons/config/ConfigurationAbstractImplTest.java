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
import static org.junit.Assert.assertNotNull;
import static org.mockito.Matchers.any;
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
	private EventBus eventBus;
	private TestConfiguration config;

	@Before
	public void setup() throws IOException {
		file = File.createTempFile(getClass().getName(), ".tmp");
		file.deleteOnExit();
		otherFile = File.createTempFile(getClass().getName(), ".tmp");
		otherFile.deleteOnExit();
		eventBus = spy(new EventBusSyncImpl());
		config = spy(new TestConfiguration(file, eventBus));
	}

	@Test
	public void testConstructor() {
		assertEquals("test", config.getSourceId());
		assertEquals(eventBus, config.getEventBus());
		assertNotNull(config.getValue());
		verify(eventBus, times(1)).subscribe(any(TestConfiguration.class));
	}

	@Test
	public void testCreatedSource() throws IOException {
		config.onFileEvent(new FileEvent("test", file, FileEventType.CREATED));
		verify(config, times(1)).onFileEventMatch(file, FileEventType.CREATED);
		verify(config, times(1)).load(file);
	}

	@Test
	public void testNewFileCreatedSource() throws IOException {
		config.onFileEvent(new FileEvent("other", otherFile, FileEventType.CREATED));
		verify(config, never()).onFileEventMatch(otherFile, FileEventType.CREATED);
	}

	@Test
	public void testUnmatchingCreatedSource() throws IOException {
		config.onFileEvent(new FileEvent("other", file, FileEventType.CREATED));
		verify(config, never()).onFileEventMatch(file, FileEventType.CREATED);
	}

	@Test
	public void testUpdatedSource() throws IOException {
		config.onFileEvent(new FileEvent("test", file, FileEventType.UPDATED));
		verify(config, times(1)).onFileEventMatch(file, FileEventType.UPDATED);
		verify(config, times(1)).load(file);
	}

	@Test
	public void testUpdatedNewFileSource() throws IOException {
		config.onFileEvent(new FileEvent("test", otherFile, FileEventType.UPDATED));
		verify(config, times(1)).onFileEventMatch(otherFile, FileEventType.UPDATED);
		verify(config, times(1)).load(otherFile);
	}

	@Test
	public void testUnmatchingUpdatedSource() throws IOException {
		config.onFileEvent(new FileEvent("other", file, FileEventType.UPDATED));
		verify(config, never()).onFileEventMatch(file, FileEventType.UPDATED);
	}

	@Test
	public void testDeletedSource() throws IOException {
		config.onFileEvent(new FileEvent("test", file, FileEventType.DELETED));
		verify(config, times(1)).onFileEventMatch(file, FileEventType.DELETED);
		verify(config, never()).load(file);
	}

	@Test
	public void testDeletedNewFileSource() throws IOException {
		config.onFileEvent(new FileEvent("test", otherFile, FileEventType.DELETED));
		verify(config, times(1)).onFileEventMatch(otherFile, FileEventType.DELETED);
		verify(config, never()).load(otherFile);
	}

	@Test
	public void testUnmatchingDeletedSource() throws IOException {
		config.onFileEvent(new FileEvent("other", file, FileEventType.DELETED));
		verify(config, never()).onFileEventMatch(file, FileEventType.DELETED);
	}

	static class TestConfiguration extends ConfigurationAbstractImpl<User> {

		TestConfiguration(File configFile, EventBus eventBus) throws IOException {
			super("test", eventBus);
			init(configFile);
		}

		@Override
		public User load(File configFile) throws IOException {
			return new User();
		}

	}

}
