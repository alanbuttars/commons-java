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
package com.alanbuttars.commons.compress.archives.config;

import static com.alanbuttars.commons.compress.archives.util.Archives.AR;
import static com.alanbuttars.commons.compress.archives.util.Archives.ARJ;
import static com.alanbuttars.commons.compress.archives.util.Archives.CPIO;
import static com.alanbuttars.commons.compress.archives.util.Archives.DUMP;
import static com.alanbuttars.commons.compress.archives.util.Archives.JAR;
import static com.alanbuttars.commons.compress.archives.util.Archives.TAR;
import static com.alanbuttars.commons.compress.archives.util.Archives.ZIP;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.ar.ArArchiveEntry;
import org.apache.commons.compress.archivers.arj.ArjArchiveEntry;
import org.apache.commons.compress.archivers.cpio.CpioArchiveEntry;
import org.apache.commons.compress.archivers.dump.DumpArchiveEntry;
import org.apache.commons.compress.archivers.jar.JarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.junit.Test;

import com.alanbuttars.commons.compress.archives.config.ArchiveEntryFunctions;
import com.alanbuttars.commons.compress.archives.config.entry.ArchiveEntryConfig;
import com.alanbuttars.commons.util.functions.BiFunction;
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
		BiFunction<String, Long, ArchiveEntryConfig> configFunction = ArchiveEntryFunctions.defaultConfigFunctions().get(AR);
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
	public void testArj() {
		BiFunction<String, Long, ArchiveEntryConfig> configFunction = ArchiveEntryFunctions.defaultConfigFunctions().get(ARJ);
		Function<ArchiveEntryConfig, ArchiveEntry> entryFunction = ArchiveEntryFunctions.defaultEntryFunctions().get(ARJ);

		ArchiveEntryConfig config = configFunction.apply("entryName.txt", 123L);
		assertEquals("entryName.txt", config.getEntryName());

		ArchiveEntry entry = entryFunction.apply(config);
		assertEquals(ArjArchiveEntry.class, entry.getClass());
		ArjArchiveEntry arjEntry = (ArjArchiveEntry) entry;
		assertNull(arjEntry.getName());
	}

	@Test
	public void testCpio() {
		BiFunction<String, Long, ArchiveEntryConfig> configFunction = ArchiveEntryFunctions.defaultConfigFunctions().get(CPIO);
		Function<ArchiveEntryConfig, ArchiveEntry> entryFunction = ArchiveEntryFunctions.defaultEntryFunctions().get(CPIO);

		ArchiveEntryConfig config = configFunction.apply("entryName.txt", 123L);
		assertEquals("entryName.txt", config.getEntryName());

		ArchiveEntry entry = entryFunction.apply(config);
		assertEquals(CpioArchiveEntry.class, entry.getClass());
		CpioArchiveEntry cpioEntry = (CpioArchiveEntry) entry;
		assertEquals("entryName.txt", cpioEntry.getName());
	}

	@Test
	public void testDump() {
		BiFunction<String, Long, ArchiveEntryConfig> configFunction = ArchiveEntryFunctions.defaultConfigFunctions().get(DUMP);
		Function<ArchiveEntryConfig, ArchiveEntry> entryFunction = ArchiveEntryFunctions.defaultEntryFunctions().get(DUMP);

		ArchiveEntryConfig config = configFunction.apply("entryName.txt", 123L);
		assertEquals("entryName.txt", config.getEntryName());

		ArchiveEntry entry = entryFunction.apply(config);
		assertEquals(DumpArchiveEntry.class, entry.getClass());
		DumpArchiveEntry dumpEntry = (DumpArchiveEntry) entry;
		assertEquals("entryName.txt", dumpEntry.getName());
		assertNull(dumpEntry.getSimpleName());
	}

	@Test
	public void testJar() {
		BiFunction<String, Long, ArchiveEntryConfig> configFunction = ArchiveEntryFunctions.defaultConfigFunctions().get(JAR);
		Function<ArchiveEntryConfig, ArchiveEntry> entryFunction = ArchiveEntryFunctions.defaultEntryFunctions().get(JAR);

		ArchiveEntryConfig config = configFunction.apply("entryName.txt", 123L);
		assertEquals("entryName.txt", config.getEntryName());

		ArchiveEntry entry = entryFunction.apply(config);
		assertEquals(JarArchiveEntry.class, entry.getClass());
		JarArchiveEntry jarEntry = (JarArchiveEntry) entry;
		assertEquals("entryName.txt", jarEntry.getName());
	}

	@Test
	public void testTar() {
		BiFunction<String, Long, ArchiveEntryConfig> configFunction = ArchiveEntryFunctions.defaultConfigFunctions().get(TAR);
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
		BiFunction<String, Long, ArchiveEntryConfig> configFunction = ArchiveEntryFunctions.defaultConfigFunctions().get(ZIP);
		Function<ArchiveEntryConfig, ArchiveEntry> entryFunction = ArchiveEntryFunctions.defaultEntryFunctions().get(ZIP);

		ArchiveEntryConfig config = configFunction.apply("entryName.txt", 123L);
		assertEquals("entryName.txt", config.getEntryName());

		ArchiveEntry entry = entryFunction.apply(config);
		assertEquals(ZipArchiveEntry.class, entry.getClass());
		ZipArchiveEntry zipEntry = (ZipArchiveEntry) entry;
		assertEquals("entryName.txt", zipEntry.getName());
	}

}
