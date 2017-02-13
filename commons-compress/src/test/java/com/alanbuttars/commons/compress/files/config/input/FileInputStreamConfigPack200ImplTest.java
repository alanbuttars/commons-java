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
package com.alanbuttars.commons.compress.files.config.input;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.io.ByteArrayInputStream;

import org.apache.commons.compress.compressors.pack200.Pack200Strategy;
import org.junit.Test;

/**
 * Test class for {@link FileInputStreamConfigPack200Impl}.
 * 
 * @author Alan Buttars
 *
 */
public class FileInputStreamConfigPack200ImplTest {

	@Test
	public void testConstructor() {
		FileInputStreamConfigPack200Impl config = new FileInputStreamConfigPack200Impl(new ByteArrayInputStream("".getBytes()));
		assertNotNull(config.getInputStream());
		assertEquals(Pack200Strategy.IN_MEMORY, config.getMode());
		assertNull(config.getProperties());
	}
}