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
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayOutputStream;
import java.util.zip.ZipEntry;

import org.apache.commons.compress.archivers.zip.Zip64Mode;
import org.apache.commons.compress.archivers.zip.ZipArchiveOutputStream;
import org.apache.commons.compress.archivers.zip.ZipArchiveOutputStream.UnicodeExtraFieldPolicy;
import org.junit.Test;

import com.alanbuttars.commons.compress.archives.config.output.ArchiveOutputStreamConfigZipImpl;

/**
 * Test class for {@link ArchiveOutputStreamConfigZipImpl}.
 * 
 * @author Alan Buttars
 *
 */
public class ArchiveOutputStreamConfigZipImplTest {

	@Test
	public void testConstructor() {
		ArchiveOutputStreamConfigZipImpl config = new ArchiveOutputStreamConfigZipImpl(new ByteArrayOutputStream());
		assertNotNull(config.getOutputStream());
		assertEquals("", config.getComment());
		assertEquals("UTF8", config.getEncoding());
		assertFalse(config.fallbackToUTF8());
		assertEquals(ZipArchiveOutputStream.DEFAULT_COMPRESSION, config.getLevel());
		assertEquals(ZipEntry.DEFLATED, config.getMethod());
		assertEquals(UnicodeExtraFieldPolicy.NEVER, config.getUnicodeExtraFieldPolicy());
		assertTrue(config.useLanguageEncoding());
		assertEquals(Zip64Mode.AsNeeded, config.getZip64Mode());
	}
}
