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
package com.alanbuttars.commons.compress.files.config.output;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.ByteArrayOutputStream;

import org.apache.commons.compress.compressors.bzip2.BZip2CompressorOutputStream;
import org.junit.Test;

/**
 * Test class for {@link FileOutputStreamConfigBzip2Impl}.
 * 
 * @author Alan Buttars
 *
 */
public class FileOutputStreamConfigBzip2ImplTest {

	@Test
	public void testConstructor() {
		FileOutputStreamConfigBzip2Impl config = new FileOutputStreamConfigBzip2Impl(new ByteArrayOutputStream());
		assertNotNull(config.getOutputStream());
		assertEquals(BZip2CompressorOutputStream.MAX_BLOCKSIZE, config.getBlockSize());
	}

}
