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

import static org.mockito.Matchers.any;
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
 * Test class for {@link DirectoryPoll}.
 * 
 * @author Alan Buttars
 *
 */
public class DirectoryPollTest {

	private File directory;
	private File file1;
	private File file2;
	private File file3;
	private EventBus mockEventBus;
	private DirectoryPoll poll;

	@Before
	public void setup() throws IOException {
		this.directory = FileTestHelper.directory();
		this.file1 = new File(directory, "file1.txt");
		this.file1.createNewFile();
		this.file2 = new File(directory, "file2.txt");
		this.file2.createNewFile();
		this.file3 = new File(directory, "file3.txt");
		this.file3.createNewFile();

		this.mockEventBus = mock(EventBus.class);
		this.poll = spy(new DirectoryPoll("test", directory, mockEventBus));
	}

	@Test
	public void testFileUnchanged() throws IOException {
		poll.run();

		verify(poll, never()).postCreatedEvent(any(File.class));
		verify(poll, never()).postUpdatedEvent(any(File.class));
		verify(poll, never()).postDeletedEvent(any(File.class));
	}

	@Test
	public void testFileDeleted() throws IOException {
		poll.run();

		FileTestHelper.delete(file1);
		FileTestHelper.delete(file2);
		poll.run();

		verify(poll, never()).postCreatedEvent(any(File.class));
		verify(poll, never()).postUpdatedEvent(any(File.class));
		verify(poll, times(1)).postDeletedEvent(file1);
		verify(poll, times(1)).postDeletedEvent(file2);
		verify(poll, never()).postDeletedEvent(file3);
	}

	@Test
	public void testFileUpdated() throws IOException, InterruptedException {
		poll.run();

		Thread.sleep(1000);
		FileTestHelper.append("a", file1);
		FileTestHelper.append("b", file2);
		poll.run();

		verify(poll, never()).postCreatedEvent(any(File.class));
		verify(poll, times(1)).postUpdatedEvent(file1);
		verify(poll, times(1)).postUpdatedEvent(file2);
		verify(poll, never()).postUpdatedEvent(file3);
		verify(poll, never()).postDeletedEvent(any(File.class));
	}

	@Test
	public void testFileCreated() throws IOException {
		poll.run();

		FileTestHelper.delete(file1);
		FileTestHelper.delete(file2);
		poll.run();

		file1.createNewFile();
		file1.deleteOnExit();
		file2.createNewFile();
		file2.deleteOnExit();
		poll.run();

		verify(poll, times(1)).postCreatedEvent(file1);
		verify(poll, times(1)).postCreatedEvent(file2);
		verify(poll, never()).postCreatedEvent(file3);
		verify(poll, never()).postUpdatedEvent(any(File.class));
		verify(poll, times(1)).postDeletedEvent(file1);
		verify(poll, times(1)).postDeletedEvent(file2);
		verify(poll, never()).postDeletedEvent(file3);
	}

}
