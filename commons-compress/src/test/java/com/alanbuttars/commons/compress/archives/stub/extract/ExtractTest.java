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
package com.alanbuttars.commons.compress.archives.stub.extract;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import com.alanbuttars.commons.compress.archives.util.Archives;

/**
 * Test class for {@link Extract}.
 * 
 * @author Alan Buttars
 *
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({ Archives.class })
public class ExtractTest {

	private File file;
	private File directory;

	@Before
	public void setup() throws IOException {
		this.file = File.createTempFile(getClass().getName(), ".tmp");
		this.directory = Files.createTempDirectory(getClass().getName()).toFile();
	}

	@Test
	public void testAr() throws Exception {
		PowerMockito.mockStatic(Archives.class);
		Extract.archive(file).asAr().toDirectory(directory);
		PowerMockito.verifyStatic();
	}

	@Test
	public void testArj() throws Exception {
		PowerMockito.mockStatic(Archives.class);
		Extract.archive(file).asArj().withEncoding("UTF8").toDirectory(directory);
		PowerMockito.verifyStatic();
	}

	@Test
	public void testCpio() throws Exception {
		PowerMockito.mockStatic(Archives.class);
		Extract.archive(file).asCpio().withEncoding("UTF8").withBlockSize(2).toDirectory(directory);
		PowerMockito.verifyStatic();
	}

	@Test
	public void testDump() throws Exception {
		PowerMockito.mockStatic(Archives.class);
		Extract.archive(file).asDump().withEncoding("UTF8").toDirectory(directory);
		PowerMockito.verifyStatic();
	}

	@Test
	public void testJar() throws Exception {
		PowerMockito.mockStatic(Archives.class);
		Extract.archive(file).asJar().withEncoding("UTF8").toDirectory(directory);
		PowerMockito.verifyStatic();
	}

	@Test
	public void testSevenZ() throws Exception {
		PowerMockito.mockStatic(Archives.class);
		Extract.archive(file).asSevenZ().withPassword("password".getBytes()).toDirectory(directory);
		PowerMockito.verifyStatic();
	}

	@Test
	public void testTar() throws Exception {
		PowerMockito.mockStatic(Archives.class);
		Extract.archive(file).asTar().withEncoding("UTF8").withBlockSize(2).withRecordSize(4).toDirectory(directory);
		PowerMockito.verifyStatic();
	}

	@Test
	public void testZip() throws Exception {
		PowerMockito.mockStatic(Archives.class);
		Extract.archive(file).asZip().withEncoding("UTF8").withStoredEntriesWithDataDescriptor(false).withUnicodeExtraFields(false).toDirectory(directory);
		PowerMockito.verifyStatic();
	}

}
