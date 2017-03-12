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

import static com.alanbuttars.commons.compress.archives.util.Archives.TAR;
import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.eq;
import static org.mockito.Matchers.isNull;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import org.apache.commons.compress.archivers.tar.TarConstants;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import com.alanbuttars.commons.compress.files.util.CompressedFiles;

/**
 * Test class for {@link DecompressArchiveWithStubTarImpl}.
 * 
 * @author Alan Buttars
 *
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({ CompressedFiles.class })
public class DecompressArchiveWithStubTarImplTest {

	private File source;
	private File destination;
	private DecompressArchiveWithStubTarImpl stub;

	@Before
	public void setup() throws IOException {
		source = File.createTempFile(getClass().getName(), ".tmp");
		destination = Files.createTempDirectory(getClass().getName()).toFile();
		stub = spy(new DecompressArchiveWithStubTarImpl(source));
	}

	@Test
	public void testConstructor() {
		assertEquals(source, stub.source);
		assertEquals(TAR, stub.fileType);
	}

	@Test
	public void testDecompressionFunction() throws IOException {
		stub.to(destination);
		verify(stub, times(1)).createArchiveInputStream(eq(source), eq(TarConstants.DEFAULT_BLKSIZE), isNull(String.class), eq(TarConstants.DEFAULT_RCDSIZE));
	}

	@Test
	public void testCustomDecompressionFunction() throws IOException {
		stub.andBlockSize(1).andEncoding("UTF16").andRecordSize(2).to(destination);
		verify(stub, times(1)).createArchiveInputStream(eq(source), eq(1), eq("UTF16"), eq(2));
	}
}
