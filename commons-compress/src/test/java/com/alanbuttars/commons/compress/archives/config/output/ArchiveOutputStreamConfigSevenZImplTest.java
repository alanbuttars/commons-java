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
package com.alanbuttars.commons.compress.archives.config.output;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.File;
import java.io.IOException;

import org.apache.commons.compress.archivers.sevenz.SevenZMethod;
import org.apache.commons.compress.utils.Lists;
import org.junit.Test;

/**
 * Test class for {@link ArchiveOutputStreamConfigSevenZImpl}.
 * 
 * @author Alan Buttars
 *
 */
public class ArchiveOutputStreamConfigSevenZImplTest {

	@Test
	public void testConstructor() throws IOException {
		ArchiveOutputStreamConfigSevenZImpl config = new ArchiveOutputStreamConfigSevenZImpl(File.createTempFile(getClass().getName(), ".tmp"));
		assertNotNull(config.getOutputStream());
		assertNotNull(config.getFile());
		assertEquals(1, Lists.newArrayList(config.getContentMethods().iterator()).size());
		assertEquals(SevenZMethod.LZMA2, config.getContentMethods().iterator().next().getMethod());
	}
}
