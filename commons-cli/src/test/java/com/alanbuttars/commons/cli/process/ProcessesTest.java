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
package com.alanbuttars.commons.cli.process;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.powermock.api.mockito.PowerMockito.doReturn;
import static org.powermock.api.mockito.PowerMockito.doThrow;
import static org.powermock.api.mockito.PowerMockito.mock;
import static org.powermock.api.mockito.PowerMockito.spy;
import static org.powermock.api.mockito.PowerMockito.verifyStatic;

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import com.alanbuttars.commons.cli.request.CommandLineRequest;
import com.alanbuttars.commons.cli.request.CommandLineRequestBuilder;
import com.alanbuttars.commons.cli.response.CommandLineResponse;

/**
 * Test class for {@link Processes}.
 * 
 * @author Alan Buttars
 *
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest(Processes.class)
public class ProcessesTest {

	private CommandLineRequest request;

	@Before
	public void setup() {
		spy(Processes.class);
		request = new CommandLineRequestBuilder()//
				.build("blah blah");
	}

	@Test
	public void testNullRequest() {
		try {
			Processes.execute(null);
			fail();
		}
		catch (IllegalArgumentException e) {
			assertEquals("Request must be non-null", e.getMessage());
		}
	}

	@Test
	public void testFailureByInputStreamInterrupted() throws Exception {
		Process mockProcess = mock(Process.class);
		ProcessStreamReader mockInputStreamReader = mock(ProcessStreamReader.class);
		ProcessStreamReader mockErrorStreamReader = mock(ProcessStreamReader.class);
		ProcessStreamListener mockInputStreamListener = mock(ProcessStreamListener.class);
		ProcessStreamListener mockErrorStreamListener = mock(ProcessStreamListener.class);

		doReturn(mockProcess).when(Processes.class, "startProcess", request);
		doReturn(mockInputStreamReader).when(Processes.class, "createInputStreamReader", mockProcess, request);
		doReturn(mockErrorStreamReader).when(Processes.class, "createErrorStreamReader", mockProcess, request);
		doReturn(mockInputStreamListener).when(Processes.class, "createStreamListener", mockProcess, request, mockInputStreamReader);
		doReturn(mockErrorStreamListener).when(Processes.class, "createStreamListener", mockProcess, request, mockErrorStreamReader);
		doThrow(new InterruptedException("mock")).when(mockInputStreamListener).join();

		CommandLineResponse response = Processes.execute(request);

		verifyStatic();

		assertEquals(CommandLineResponse.INTERRUPTED_BEFORE_COMPLETION_EXIT_CODE, response.getExitCode());
		assertTrue(response.failed());
		assertNull(response.getInfoStream());
		assertNull(response.getErrorStream());
		assertEquals(InterruptedException.class, response.getException().getClass());
		assertEquals("mock", response.getException().getMessage());
		assertFalse(response.exceptionThrown());
		assertTrue(response.interrupted());
	}

	@Test
	public void testFailureByErrorStreamInterrupted() throws Exception {
		Process mockProcess = mock(Process.class);
		ProcessStreamReader mockInputStreamReader = mock(ProcessStreamReader.class);
		ProcessStreamReader mockErrorStreamReader = mock(ProcessStreamReader.class);
		ProcessStreamListener mockInputStreamListener = mock(ProcessStreamListener.class);
		ProcessStreamListener mockErrorStreamListener = mock(ProcessStreamListener.class);

		doReturn(mockProcess).when(Processes.class, "startProcess", request);
		doReturn(mockInputStreamReader).when(Processes.class, "createInputStreamReader", mockProcess, request);
		doReturn(mockErrorStreamReader).when(Processes.class, "createErrorStreamReader", mockProcess, request);
		doReturn(mockInputStreamListener).when(Processes.class, "createStreamListener", mockProcess, request, mockInputStreamReader);
		doReturn(mockErrorStreamListener).when(Processes.class, "createStreamListener", mockProcess, request, mockErrorStreamReader);
		doThrow(new InterruptedException("mock")).when(mockErrorStreamListener).join();

		CommandLineResponse response = Processes.execute(request);

		verifyStatic();

		assertEquals(CommandLineResponse.INTERRUPTED_BEFORE_COMPLETION_EXIT_CODE, response.getExitCode());
		assertTrue(response.failed());
		assertNull(response.getInfoStream());
		assertNull(response.getErrorStream());
		assertEquals(InterruptedException.class, response.getException().getClass());
		assertEquals("mock", response.getException().getMessage());
		assertFalse(response.exceptionThrown());
		assertTrue(response.interrupted());
	}

	@Test
	public void testFailureByIOException() throws Exception {
		doThrow(new IOException("mock")).when(Processes.class, "startProcess", request);

		CommandLineResponse response = Processes.execute(request);

		verifyStatic();

		assertEquals(CommandLineResponse.EXCEPTION_THROWN_EXIT_CODE, response.getExitCode());
		assertTrue(response.failed());
		assertNull(response.getInfoStream());
		assertNull(response.getErrorStream());
		assertEquals(IOException.class, response.getException().getClass());
		assertEquals("mock", response.getException().getMessage());
		assertTrue(response.exceptionThrown());
		assertFalse(response.interrupted());
	}

}
