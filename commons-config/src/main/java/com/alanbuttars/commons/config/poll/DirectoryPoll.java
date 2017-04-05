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
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import com.alanbuttars.commons.config.event.FileEvent;
import com.alanbuttars.commons.config.event.FileEventType;
import com.alanbuttars.commons.config.eventbus.EventBus;
import com.alanbuttars.commons.util.annotations.VisibleForTesting;

/**
 * Models a runnable task which continually checks a given directory to see if the files within it have been changed.
 * This task is responsible for publishing {@link FileEvent}s to the {@link EventBus} when it detects a file change.
 * 
 * @author Alan Buttars
 *
 */
public class DirectoryPoll implements Runnable {

	private final String sourceId;
	private final File directory;
	private final EventBus eventBus;
	private final Map<File, Long> pollEntries;

	public DirectoryPoll(String sourceId, File directory, EventBus eventBus) {
		this.sourceId = sourceId;
		this.directory = directory;
		this.eventBus = eventBus;
		this.pollEntries = new TreeMap<File, Long>();
		initPollEntries(directory);
	}

	/**
	 * Recursively iterates through the directory, registering existing files with this instance.
	 * 
	 * @param root
	 */
	private void initPollEntries(File root) {
		if (root.isFile()) {
			pollEntries.put(root, root.lastModified());
		}
		else {
			for (File file : root.listFiles()) {
				initPollEntries(file);
			}
		}
	}

	@Override
	public void run() {
		pollForDeletedFiles();
		pollForCreatedAndUpdatedFiles(directory);
	}

	/**
	 * Iterates through files which have already been registered with this instance. For each file which has been
	 * deleted since the last run, a {@link FileEventType#DELETED} is posted to the {@link #eventBus}.
	 */
	private void pollForDeletedFiles() {
		for (Entry<File, Long> pollEntry : pollEntries.entrySet()) {
			File file = pollEntry.getKey();
			Long lastModified = pollEntry.getValue();
			if (!file.exists() && lastModified > -1) {
				pollEntries.put(file, -1L);
				postDeletedEvent(file);
			}
		}
	}

	/**
	 * Recursively iterates through the directory. For each file which has not been registered with this instance, a
	 * {@link FileEventType#CREATED} event is posted to the {@link #eventBus}. For each file whose timestamp is newer, a
	 * {@link FileEventType#UPDATED} is posted.
	 * 
	 * @param root
	 *            The current directory in the recursive iteration
	 */
	private void pollForCreatedAndUpdatedFiles(File root) {
		if (root.isFile()) {
			Long lastModified = pollEntries.get(root);
			if (lastModified == null || lastModified == -1L) {
				pollEntries.put(root, root.lastModified());
				postCreatedEvent(root);
			}
			else if (lastModified < root.lastModified()) {
				pollEntries.put(root, root.lastModified());
				postUpdatedEvent(root);
			}
		}
		else {
			for (File file : root.listFiles()) {
				pollForCreatedAndUpdatedFiles(file);
			}
		}
	}
	
	@VisibleForTesting
	protected void postCreatedEvent(File file) {
		eventBus.publish(new FileEvent(sourceId, file, FileEventType.CREATED));
	}

	@VisibleForTesting
	protected void postUpdatedEvent(File file) {
		eventBus.publish(new FileEvent(sourceId, file, FileEventType.UPDATED));
	}

	@VisibleForTesting
	protected void postDeletedEvent(File file) {
		eventBus.publish(new FileEvent(sourceId, file, FileEventType.DELETED));
	}

}
