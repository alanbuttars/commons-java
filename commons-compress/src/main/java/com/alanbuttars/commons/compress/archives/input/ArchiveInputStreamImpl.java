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
package com.alanbuttars.commons.compress.archives.input;

import java.io.Closeable;
import java.io.IOException;

import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.ar.ArArchiveInputStream;
import org.apache.commons.compress.archivers.cpio.CpioArchiveInputStream;
import org.apache.commons.compress.archivers.dump.DumpArchiveInputStream;
import org.apache.commons.compress.archivers.jar.JarArchiveInputStream;
import org.apache.commons.compress.archivers.tar.TarArchiveInputStream;
import org.apache.commons.compress.archivers.zip.ZipArchiveInputStream;

/**
 * Extension of {@link ArchiveInputStream} which wraps {@link ArchiveInputStream}s.
 * 
 * @author Alan Buttars
 *
 */
public class ArchiveInputStreamImpl implements ArchiveInputStream {

	private final org.apache.commons.compress.archivers.ArchiveInputStream archiveInputStream;

	public ArchiveInputStreamImpl(org.apache.commons.compress.archivers.ArchiveInputStream archiveInputStream) {
		this.archiveInputStream = archiveInputStream;
	}

	@Override
	public int read(byte[] content) throws IOException {
		return archiveInputStream.read(content);
	}

	@Override
	public ArchiveEntry getNextEntry() throws IOException {
		if (archiveInputStream instanceof ArArchiveInputStream) {
			return ((ArArchiveInputStream) archiveInputStream).getNextArEntry();
		}
		else if (archiveInputStream instanceof CpioArchiveInputStream) {
			return ((CpioArchiveInputStream) archiveInputStream).getNextCPIOEntry();
		}
		else if (archiveInputStream instanceof DumpArchiveInputStream) {
			return ((DumpArchiveInputStream) archiveInputStream).getNextDumpEntry();
		}
		else if (archiveInputStream instanceof JarArchiveInputStream) {
			return ((JarArchiveInputStream) archiveInputStream).getNextJarEntry();
		}
		else if (archiveInputStream instanceof TarArchiveInputStream) {
			return ((TarArchiveInputStream) archiveInputStream).getNextTarEntry();
		}
		else if (archiveInputStream instanceof ZipArchiveInputStream) {
			return ((ZipArchiveInputStream) archiveInputStream).getNextZipEntry();
		}
		return archiveInputStream.getNextEntry();
	}

	@Override
	public void close() throws IOException {
		archiveInputStream.close();
	}

	@Override
	public Closeable getStream() {
		return archiveInputStream;
	}

}
