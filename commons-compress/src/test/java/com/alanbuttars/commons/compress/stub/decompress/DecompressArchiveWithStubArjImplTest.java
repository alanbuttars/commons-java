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
package com.alanbuttars.commons.compress.stub.decompress;

import static com.alanbuttars.commons.compress.archives.util.Archives.ARJ;
import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import org.apache.commons.compress.archivers.ArchiveException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import com.alanbuttars.commons.compress.files.util.CompressedFiles;

/**
 * Test class for {@link DecompressArchiveWithStubArjImpl}.
 * 
 * @author Alan Buttars
 *
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({ CompressedFiles.class })
public class DecompressArchiveWithStubArjImplTest {

	private File source;
	private File destination;
	private DecompressArchiveWithStubArjImpl stub;

	@Before
	public void setup() throws IOException {
		source = File.createTempFile(getClass().getName(), ".tmp");
		destination = Files.createTempDirectory(getClass().getName()).toFile();
		stub = spy(new DecompressArchiveWithStubArjImpl(source));
	}
	
	@After
	public void teardown() {
		source.deleteOnExit();
		destination.deleteOnExit();
	}

	@Test
	public void testConstructor() {
		assertEquals(source, stub.source);
		assertEquals(ARJ, stub.fileType);
	}

	@Test(expected = RuntimeException.class)
	public void testDecompressionFunction() throws ArchiveException, IOException {
		stub.to(destination);
		verify(stub, times(1)).createArchiveInputStream(eq(source), eq("CP437"));
	}

	@Test(expected = RuntimeException.class)
	public void testCustomDecompressionFunction() throws ArchiveException, IOException {
		stub.andEncoding("UTF16").to(destination);
		verify(stub, times(1)).createArchiveInputStream(eq(source), eq("UTF16"));
	}
}
