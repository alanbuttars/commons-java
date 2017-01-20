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
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.powermock.api.mockito.PowerMockito.spy;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import com.alanbuttars.commons.cli.evaluator.evaluation.ConclusiveEvaluation;
import com.alanbuttars.commons.cli.evaluator.evaluation.Evaluation;
import com.alanbuttars.commons.util.functions.Function;

/**
 * Test class for {@link ProcessStreamListener}.
 * 
 * @author Alan Buttars
 *
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({ ProcessStreamReader.class, ProcessStreamListener.class })
public class ProcessStreamListenerTest {

	private Process process;
	private ProcessStreamReader reader;
	private ProcessStreamListener listener;

	private void setup(Function<String, Evaluation> evaluationFunction, boolean interruptOnFailure, boolean interruptOnSuccess, long interruptAfter) throws IOException {
		process = mock(Process.class);
		try (InputStream infoStream = new ByteArrayInputStream("info 1".getBytes());
				InputStream errorStream = new ByteArrayInputStream("info 1".getBytes())) {
			doReturn(infoStream).when(process).getInputStream();
			doReturn(errorStream).when(process).getErrorStream();

			reader = spy(new ProcessStreamReader(process.getInputStream(), evaluationFunction, interruptOnFailure, interruptOnSuccess));
			listener = spy(new ProcessStreamListener(process, reader, interruptOnFailure, interruptOnSuccess, interruptAfter));
		}
	}

	private void run() {
		reader.start();
		listener.run();
	}

	@After
	public void teardown() {
		process = null;
		reader = null;
		listener = null;
	}

	@Test
	public void testSuccessByEvaluation() throws IOException {
		setup(new Function<String, Evaluation>() {

			@Override
			public Evaluation apply(String input) {
				return ConclusiveEvaluation.SUCCESS;
			}

		}, false, false, 0);
		run();

		verify(process, never()).destroy();
		verify(reader, never()).interrupt();
		assertTrue(reader.getResult().succeeded());
		assertNull(reader.getResult().getException());
	}

	@Test
	public void testSuccessByEvaluationAndInterruption() throws IOException {
		setup(new Function<String, Evaluation>() {

			@Override
			public Evaluation apply(String input) {
				return ConclusiveEvaluation.SUCCESS;
			}

		}, false, true, 0);
		run();

		verify(process).destroy();
		verify(reader, never()).interrupt();
		assertTrue(reader.getResult().succeeded());
		assertNull(reader.getResult().getException());
	}

	@Test
	public void testFailureByEvaluation() throws IOException {
		setup(new Function<String, Evaluation>() {

			@Override
			public Evaluation apply(String input) {
				return ConclusiveEvaluation.FAILURE;
			}

		}, false, false, 0);
		run();

		verify(process, never()).destroy();
		verify(reader, never()).interrupt();
		assertFalse(reader.getResult().succeeded());
		assertNull(reader.getResult().getException());
	}

	@Test
	public void testFailureByEvaluationAndInterruption() throws IOException {
		setup(new Function<String, Evaluation>() {

			@Override
			public Evaluation apply(String input) {
				return ConclusiveEvaluation.FAILURE;
			}

		}, true, false, 0);
		run();

		verify(process).destroy();
		verify(reader, never()).interrupt();
		assertFalse(reader.getResult().succeeded());
		assertNull(reader.getResult().getException());
	}

	@Test
	public void testFailureByIOException() throws Exception {
		setup(new Function<String, Evaluation>() {

			@Override
			public Evaluation apply(String input) {
				return ConclusiveEvaluation.SUCCESS;
			}

		}, false, false, 0);

		InputStreamReader mockStreamReader = mock(InputStreamReader.class);
		BufferedReader mockBufferedReader = mock(BufferedReader.class);
		doReturn(mockStreamReader).when(reader).inputStreamReader();
		doReturn(mockBufferedReader).when(reader).bufferedReader(mockStreamReader);
		doThrow(new IOException("mock")).when(mockBufferedReader).readLine();

		run();

		verify(process, never()).destroy();
		verify(reader, never()).interrupt();
		assertTrue(reader.getResult().failed());
		assertNotNull(reader.getResult().getException());
		assertEquals(IOException.class, reader.getResult().getException().getClass());
		assertEquals("mock", reader.getResult().getException().getMessage());
	}

	@Test
	public void testFailureByInterruptedException() throws Exception {
		setup(new Function<String, Evaluation>() {

			@Override
			public Evaluation apply(String input) {
				return ConclusiveEvaluation.SUCCESS;
			}

		}, false, false, 24);

		doThrow(new InterruptedException("mock")).when(reader).join(24);
		run();

		verify(process, never()).destroy();
		verify(reader, never()).interrupt();
		assertTrue(reader.getResult().failed());
		assertNotNull(reader.getResult().getException());
		assertEquals(InterruptedException.class, reader.getResult().getException().getClass());
		assertEquals("mock", reader.getResult().getException().getMessage());
	}

	@Test
	public void testFailureByInterruptionAfter() throws IOException {
		setup(new Function<String, Evaluation>() {

			@Override
			public Evaluation apply(String input) {
				try {
					Thread.sleep(1000);
				}
				catch (InterruptedException e) {
				}
				return Evaluation.NON_CONCLUSIVE;
			}

		}, false, false, 1);
		run();

		verify(process, never()).destroy();
		verify(listener).interruptReader();
		assertFalse(reader.getResult().succeeded());
		assertNotNull(reader.getResult().getException());
		assertEquals(InterruptedException.class, reader.getResult().getException().getClass());
		assertEquals("Process interrupted after 1 millis", reader.getResult().getException().getMessage());
	}

	@Test
	public void testNonConclusiveByEvaluation() throws IOException {
		setup(new Function<String, Evaluation>() {

			@Override
			public Evaluation apply(String input) {
				return ConclusiveEvaluation.SUCCESS;
			}

		}, false, false, 0);
		run();

		verify(process, never()).destroy();
		verify(reader, never()).interrupt();
		assertTrue(reader.getResult().succeeded());
		assertNull(reader.getResult().getException());
	}

	@Test
	public void testNonConclusiveByEvaluationAndInterruption() throws IOException {
		setup(new Function<String, Evaluation>() {

			@Override
			public Evaluation apply(String input) {
				return ConclusiveEvaluation.SUCCESS;
			}

		}, false, true, 0);
		run();

		verify(process).destroy();
		verify(reader, never()).interrupt();
		assertTrue(reader.getResult().succeeded());
		assertNull(reader.getResult().getException());
	}

}
