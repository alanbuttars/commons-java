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

import java.io.ByteArrayOutputStream;

import org.apache.commons.compress.archivers.tar.TarArchiveOutputStream;
import org.apache.commons.compress.archivers.tar.TarConstants;
import org.junit.Test;

import com.alanbuttars.commons.compress.archives.config.output.ArchiveOutputStreamConfigTarImpl;

/**
 * Test class for {@link ArchiveOutputStreamConfigTarImpl}.
 * 
 * @author Alan Buttars
 *
 */
public class ArchiveOutputStreamConfigTarImplTest {

	@Test
	public void testConstructor() {
		ArchiveOutputStreamConfigTarImpl config = new ArchiveOutputStreamConfigTarImpl(new ByteArrayOutputStream());
		assertNotNull(config.getOutputStream());
		assertFalse(config.addPaxHeadersForNonAsciiNames());
		assertEquals(TarArchiveOutputStream.BIGNUMBER_ERROR, config.getBigNumberMode());
		assertEquals(TarConstants.DEFAULT_BLKSIZE, config.getBlockSize());
		assertEquals(TarArchiveOutputStream.LONGFILE_ERROR, config.getLongFileMode());
		assertEquals(TarConstants.DEFAULT_RCDSIZE, config.getRecordSize());
	}
}
