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
package com.alanbuttars.commons.compress.config;

import static com.alanbuttars.commons.compress.util.Archives.ZIP;
import static org.junit.Assert.assertEquals;

import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.junit.Test;

import com.alanbuttars.commons.compress.config.entry.ArchiveEntryConfig;
import com.alanbuttars.commons.util.functions.DoubleInputFunction;
import com.alanbuttars.commons.util.functions.Function;

/**
 * Test class for {@link ArchiveEntryFunctions}.
 * 
 * @author Alan Buttars
 *
 */
public class ArchiveEntryFunctionsTest {

	@Test
	public void testZip() {
		DoubleInputFunction<String, Long, ArchiveEntryConfig> configFunction = ArchiveEntryFunctions.defaultConfigFunctions().get(ZIP);
		Function<ArchiveEntryConfig, ArchiveEntry> entryFunction = ArchiveEntryFunctions.defaultEntryFunctions().get(ZIP);

		ArchiveEntryConfig config = configFunction.apply("entryName.txt", 123L);
		assertEquals("entryName.txt", config.getEntryName());

		ArchiveEntry entry = entryFunction.apply(config);
		assertEquals(ZipArchiveEntry.class, entry.getClass());
		ZipArchiveEntry zipEntry = (ZipArchiveEntry) entry;
		assertEquals("entryName.txt", zipEntry.getName());
	}

}