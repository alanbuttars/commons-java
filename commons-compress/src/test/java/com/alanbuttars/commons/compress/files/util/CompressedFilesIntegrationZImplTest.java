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
package com.alanbuttars.commons.compress.files.util;

import static com.alanbuttars.commons.compress.files.util.CompressedFiles.Z;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.io.IOException;

import org.junit.Test;

/**
 * Integration test class for {@link CompressedFiles} for {@link CompressedFiles#Z} files.
 * 
 * @author Alan Buttars
 *
 */
public class CompressedFilesIntegrationZImplTest extends CompressedFilesIntegrationAbstractTest {

	@Test
	public void testDecompress() throws IOException {
		testDecompress(Z);
	}

	@Test
	public void testCompress() throws IOException {
		try {
			testCompress(Z);
			fail();
		}
		catch (IllegalArgumentException e) {
			assertEquals("File type z is not recognized", e.getMessage());
		}
	}
}
