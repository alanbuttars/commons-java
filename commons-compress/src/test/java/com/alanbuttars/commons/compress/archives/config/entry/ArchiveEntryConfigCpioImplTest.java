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

import org.apache.commons.compress.archivers.cpio.CpioConstants;
import org.junit.Test;

import com.alanbuttars.commons.compress.archives.config.entry.ArchiveEntryConfigCpioImpl;

/**
 * Test class for {@link ArchiveEntryConfigCpioImpl}.
 * 
 * @author Alan Buttars
 *
 */
public class ArchiveEntryConfigCpioImplTest {

	@Test
	public void testConstructor() {
		ArchiveEntryConfigCpioImpl config = new ArchiveEntryConfigCpioImpl("entryName.txt", 123L);
		assertEquals("entryName.txt", config.getEntryName());
		assertEquals(123L, config.getLength());
		assertEquals(CpioConstants.FORMAT_NEW, config.getFormat());
	}
}
