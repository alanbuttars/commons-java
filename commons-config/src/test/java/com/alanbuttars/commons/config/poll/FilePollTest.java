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
package com.alanbuttars.commons.config.poll;

import static org.mockito.Mockito.never;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.powermock.api.mockito.PowerMockito.mock;

import java.io.File;
import java.io.IOException;

import org.junit.Before;
import org.junit.Test;

import com.alanbuttars.commons.config.eventbus.EventBus;
import com.alanbuttars.commons.config.util.FileTestHelper;

/**
 * Test class for {@link FilePoll}.
 * 
 * @author Alan Buttars
 *
 */
public class FilePollTest {

	private File file;
	private EventBus mockEventBus;
	private FilePoll poll;

	@Before
	public void setup() throws IOException {
		this.file = FileTestHelper.file();
		this.mockEventBus = mock(EventBus.class);
		this.poll = spy(new FilePoll("test", file, mockEventBus));
	}

	@Test
	public void testFileUnchanged() {
		poll.run();

		verify(poll, never()).postCreatedEvent();
		verify(poll, never()).postUpdatedEvent();
		verify(poll, never()).postDeletedEvent();
	}

	@Test
	public void testFileDeleted() throws IOException {
		poll.run();
		FileTestHelper.delete(file);
		poll.run();

		verify(poll, never()).postCreatedEvent();
		verify(poll, never()).postUpdatedEvent();
		verify(poll, times(1)).postDeletedEvent();
	}

	@Test
	public void testFileUpdated() throws IOException, InterruptedException {
		poll.run();
		Thread.sleep(1000);

		FileTestHelper.append("a", file);
		poll.run();

		verify(poll, never()).postCreatedEvent();
		verify(poll, times(1)).postUpdatedEvent();
		verify(poll, never()).postDeletedEvent();
	}

	@Test
	public void testFileCreated() throws IOException {
		poll.run();
		FileTestHelper.delete(file);
		poll.run();
		file.createNewFile();
		file.deleteOnExit();
		poll.run();

		verify(poll, times(1)).postCreatedEvent();
		verify(poll, never()).postUpdatedEvent();
		verify(poll, times(1)).postDeletedEvent();
	}

}
