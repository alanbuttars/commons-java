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
package com.alanbuttars.commons.compress.archives.stub.archive;

import static com.alanbuttars.commons.compress.archives.util.Archives.ZIP;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyBoolean;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.powermock.api.mockito.PowerMockito.doReturn;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.util.zip.ZipEntry;

import org.apache.commons.compress.archivers.ArchiveException;
import org.apache.commons.compress.archivers.zip.Zip64Mode;
import org.apache.commons.compress.archivers.zip.ZipArchiveOutputStream;
import org.apache.commons.compress.archivers.zip.ZipArchiveOutputStream.UnicodeExtraFieldPolicy;
import org.junit.Before;
import org.junit.Test;

import com.alanbuttars.commons.compress.archives.config.ArchiveConfigs;
import com.alanbuttars.commons.compress.archives.config.output.ArchiveOutputStreamConfig;

/**
 * Test class for {@link ArchiveFileAsStubZipImpl}.
 * 
 * @author Alan Buttars
 *
 */
public class ArchiveFileAsStubZipImplTest {

	private File directory;
	private File destination;
	private ArchiveFileAsStubZipImpl stub;

	@Before
	public void setup() throws IOException {
		directory = Files.createTempDirectory(getClass().getName()).toFile();
		destination = File.createTempFile(getClass().getName(), ".tmp");
		stub = spy(new ArchiveFileAsStubZipImpl(directory));
	}

	@Test
	public void testConstructor() {
		assertEquals(directory, stub.directory);
		assertEquals(ZIP, stub.archiveType);
		assertEquals(ArchiveConfigs.OUTPUT_CONFIG_FUNCTIONS.get(ZIP), stub.streamConfigFunction());
		assertNotEquals(ArchiveConfigs.OUTPUT_STREAM_FUNCTIONS.get(ZIP), stub.streamFunction());
	}

	@Test
	public void testStreamFunction() throws ArchiveException {
		doReturn(null).when(stub).createArchiveOutputStream(any(OutputStream.class), //
				anyString(), //
				any(UnicodeExtraFieldPolicy.class), //
				anyString(), //
				anyBoolean(), //
				anyInt(), //
				anyInt(), //
				anyBoolean(), //
				any(Zip64Mode.class));

		ArchiveOutputStreamConfig config = stub.streamConfigFunction().apply(destination);
		stub.streamFunction().apply(config);

		verify(stub).createArchiveOutputStream(any(OutputStream.class), //
				eq(""), //
				eq(UnicodeExtraFieldPolicy.NEVER), //
				eq("UTF8"), //
				eq(false), //
				eq(ZipArchiveOutputStream.DEFAULT_COMPRESSION), //
				eq(ZipEntry.DEFLATED), //
				eq(true), //
				eq(Zip64Mode.AsNeeded));
	}

	@Test
	public void testCustomizedStreamFunction() throws ArchiveException {
		stub.withEncoding("blah") //
				.withComment("comment") //
				.withLevel(2) //
				.withMethod(4) //
				.withUnicodeExtraFieldPolicy(UnicodeExtraFieldPolicy.ALWAYS) //
				.withZip64Mode(Zip64Mode.Always)//
				.fallbackToUTF8(true)//
				.useLanguageEncoding(false);

		doReturn(null).when(stub).createArchiveOutputStream(any(OutputStream.class), //
				anyString(), //
				any(UnicodeExtraFieldPolicy.class), //
				anyString(), //
				anyBoolean(), //
				anyInt(), //
				anyInt(), //
				anyBoolean(), //
				any(Zip64Mode.class));

		ArchiveOutputStreamConfig config = stub.streamConfigFunction().apply(destination);
		stub.streamFunction().apply(config);

		verify(stub).createArchiveOutputStream(any(OutputStream.class), //
				eq("comment"), //
				eq(UnicodeExtraFieldPolicy.ALWAYS), //
				eq("blah"), //
				eq(true), //
				eq(2), //
				eq(4), //
				eq(false), //
				eq(Zip64Mode.Always));
	}

}