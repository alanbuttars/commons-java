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
package com.alanbuttars.commons.compress.archives.config.entry;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.alanbuttars.commons.compress.archives.config.entry.ArchiveEntryConfigArImpl;

/**
 * Test class for {@link ArchiveEntryConfigArImpl}.
 * 
 * @author Alan Buttars
 *
 */
public class ArchiveEntryConfigArImplTest {

	@Test
	public void testConstructor() {
		ArchiveEntryConfigArImpl config = new ArchiveEntryConfigArImpl("entryName.txt", 123L);
		assertEquals("entryName.txt", config.getEntryName());
		assertEquals(123L, config.getLength());
		assertEquals(0, config.getGroupId());
		assertEquals(0, config.getUserId());
		assertEquals(33188, config.getMode());
		long now = System.currentTimeMillis() / 1000 + 50;
		assertTrue(now > config.getLastModified());
	}

}