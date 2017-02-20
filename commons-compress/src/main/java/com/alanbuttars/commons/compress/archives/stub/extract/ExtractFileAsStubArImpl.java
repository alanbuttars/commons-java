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
package com.alanbuttars.commons.compress.archives.stub.extract;

import java.io.File;

import com.alanbuttars.commons.compress.archives.config.ArchiveConfigs;
import com.alanbuttars.commons.compress.archives.config.input.ArchiveInputStreamConfig;
import com.alanbuttars.commons.compress.archives.input.ArchiveInputStream;
import com.alanbuttars.commons.compress.archives.util.Archives;
import com.alanbuttars.commons.util.functions.Function;

/**
 * Extension of {@link ExtractFileAsStub} for {@link Archives#AR} archives.
 * 
 * @author Alan Buttars
 *
 */
class ExtractFileAsStubArImpl extends ExtractFileAsStub {

	ExtractFileAsStubArImpl(File archive) {
		super(archive, Archives.AR);
	}

	@Override
	protected Function<ArchiveInputStreamConfig, ArchiveInputStream> streamFunction() {
		return ArchiveConfigs.INPUT_STREAM_FUNCTIONS.get(archiveType);
	}

}
