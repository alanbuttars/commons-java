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
package com.alanbuttars.commons.compress.archives.config.input;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.io.File;
import java.io.IOException;

import org.apache.commons.compress.archivers.tar.TarConstants;
import org.junit.Test;

/**
 * Test class for {@link ArchiveInputStreamConfigTarImpl}.
 * 
 * @author Alan Buttars
 *
 */
public class ArchiveInputStreamConfigTarImplTest {

	@Test
	public void testConstructor() throws IOException {
		ArchiveInputStreamConfigTarImpl config = new ArchiveInputStreamConfigTarImpl(File.createTempFile(getClass().getName(), ".tmp"));
		assertNotNull(config.getInputStream());
		assertNull(config.getEncoding());
		assertEquals(TarConstants.DEFAULT_BLKSIZE, config.getBlockSize());
		assertEquals(TarConstants.DEFAULT_RCDSIZE, config.getRecordSize());
	}

}
