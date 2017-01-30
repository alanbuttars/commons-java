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

import static com.alanbuttars.commons.compress.util.Archives.AR;
import static com.alanbuttars.commons.compress.util.Archives.TAR;
import static com.alanbuttars.commons.compress.util.Archives.ZIP;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.ar.ArArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
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
	public void testAr() {
		DoubleInputFunction<String, Long, ArchiveEntryConfig> configFunction = ArchiveEntryFunctions.defaultConfigFunctions().get(AR);
		Function<ArchiveEntryConfig, ArchiveEntry> entryFunction = ArchiveEntryFunctions.defaultEntryFunctions().get(AR);

		ArchiveEntryConfig config = configFunction.apply("entryName.txt", 123L);
		assertEquals("entryName.txt", config.getEntryName());

		ArchiveEntry entry = entryFunction.apply(config);
		assertEquals(ArArchiveEntry.class, entry.getClass());
		ArArchiveEntry arEntry = (ArArchiveEntry) entry;
		assertEquals("entryName.txt", arEntry.getName());
		assertEquals(123L, arEntry.getLength());
		assertEquals(0, arEntry.getGroupId());
		assertEquals(0, arEntry.getUserId());
		assertEquals(33188, arEntry.getMode());
		long now = System.currentTimeMillis() + 50;
		assertTrue(now > arEntry.getLastModified());
	}

	@Test
	public void testTar() {
		DoubleInputFunction<String, Long, ArchiveEntryConfig> configFunction = ArchiveEntryFunctions.defaultConfigFunctions().get(TAR);
		Function<ArchiveEntryConfig, ArchiveEntry> entryFunction = ArchiveEntryFunctions.defaultEntryFunctions().get(TAR);

		ArchiveEntryConfig config = configFunction.apply("entryName.txt", 123L);
		assertEquals("entryName.txt", config.getEntryName());

		ArchiveEntry entry = entryFunction.apply(config);
		assertEquals(TarArchiveEntry.class, entry.getClass());
		TarArchiveEntry tarEntry = (TarArchiveEntry) entry;
		assertEquals("entryName.txt", tarEntry.getName());
		assertEquals(123L, tarEntry.getSize());
	}

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
