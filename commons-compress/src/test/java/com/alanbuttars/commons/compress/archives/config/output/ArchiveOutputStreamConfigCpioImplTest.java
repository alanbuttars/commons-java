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

import java.io.ByteArrayOutputStream;

import org.apache.commons.compress.archivers.cpio.CpioConstants;
import org.apache.commons.compress.utils.CharsetNames;
import org.junit.Test;

import com.alanbuttars.commons.compress.archives.config.output.ArchiveOutputStreamConfigCpioImpl;

/**
 * Test class for {@link ArchiveOutputStreamConfigCpioImpl}.
 * 
 * @author Alan Buttars
 *
 */
public class ArchiveOutputStreamConfigCpioImplTest {

	@Test
	public void testConstructor() {
		ArchiveOutputStreamConfigCpioImpl config = new ArchiveOutputStreamConfigCpioImpl(new ByteArrayOutputStream());
		assertNotNull(config.getOutputStream());
		assertEquals(CharsetNames.US_ASCII, config.getEncoding());
		assertEquals(CpioConstants.BLOCK_SIZE, config.getBlockSize());
		assertEquals(CpioConstants.FORMAT_NEW, config.getFormat());
	}
}
