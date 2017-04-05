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

import java.io.File;

import com.alanbuttars.commons.config.event.FileEvent;
import com.alanbuttars.commons.config.event.FileEventType;
import com.alanbuttars.commons.config.eventbus.EventBus;
import com.alanbuttars.commons.util.annotations.VisibleForTesting;

/**
 * Models a runnable task which continually checks a given file to see if it has been changed. This task is responsible
 * for publishing {@link FileEvent}s to the {@link #eventBus} when it detects a file change.
 * 
 * @author Alan Buttars
 *
 */
public class FilePoll implements Runnable {

	private final String sourceId;
	private final File file;
	private final EventBus eventBus;
	private long lastModified = 0;

	public FilePoll(String sourceId, File file, EventBus eventBus) {
		this.sourceId = sourceId;
		this.file = file;
		this.eventBus = eventBus;
		this.lastModified = file.lastModified();
	}

	@Override
	public void run() {
		if (file.exists()) {
			if (lastModified == -1) {
				lastModified = file.lastModified();
				postCreatedEvent();
			}
			else if (lastModified < file.lastModified()) {
				lastModified = file.lastModified();
				postUpdatedEvent();
			}
		}
		else if (lastModified > -1) {
			lastModified = -1;
			postDeletedEvent();
		}
	}

	@VisibleForTesting
	protected void postCreatedEvent() {
		eventBus.publish(new FileEvent(sourceId, file, FileEventType.CREATED));
	}

	@VisibleForTesting
	protected void postUpdatedEvent() {
		eventBus.publish(new FileEvent(sourceId, file, FileEventType.UPDATED));
	}

	@VisibleForTesting
	protected void postDeletedEvent() {
		eventBus.publish(new FileEvent(sourceId, file, FileEventType.DELETED));
	}

}
