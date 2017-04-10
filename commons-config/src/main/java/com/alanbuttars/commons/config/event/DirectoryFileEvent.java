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
package com.alanbuttars.commons.config.event;

import java.io.File;

/**
 * Models an event thrown when a directory change is detected.
 * 
 * @author Alan Buttars
 *
 */
public class DirectoryFileEvent extends FileEvent {

	private final File directory;

	public DirectoryFileEvent(String sourceId, File directory, File file, FileEventType fileEventType) {
		super(sourceId, file, fileEventType);
		this.directory = directory;
	}

	public File getDirectory() {
		return directory;
	}

}
