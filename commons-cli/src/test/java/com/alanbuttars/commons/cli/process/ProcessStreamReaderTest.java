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
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.powermock.api.mockito.PowerMockito.doReturn;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.junit.Test;

import com.alanbuttars.commons.cli.evaluator.evaluation.ConclusiveEvaluation;
import com.alanbuttars.commons.cli.evaluator.evaluation.Evaluation;
import com.alanbuttars.commons.cli.util.Function;

/**
 * Test class for {@link ProcessStreamReader}.
 * 
 * @author Alan Buttars
 *
 */
public class ProcessStreamReaderTest extends ProcessAbstractTest {

	@Test
	public void testSuccessByEvaluation() throws IOException {
		try (InputStream inputStream = new ByteArrayInputStream("a\nb".getBytes())) {
			ProcessStreamReader reader = new ProcessStreamReader(inputStream, new Function<String, Evaluation>() {

				@Override
				public Evaluation apply(String input) {
					return ConclusiveEvaluation.SUCCESS;
				}

			}, false, false);
			reader.run();
			ProcessStreamResult result = reader.getResult();
			assertFalse(result.failedWithoutException());
			assertNull(result.getException());
			assertEquals("a\nb\n", result.getStream());
			assertFalse(result.interrupted());
			assertTrue(result.succeeded());
		}
	}

	@Test
	public void testSuccessByEvaluationAndInterruption() throws IOException {
		try (InputStream inputStream = new ByteArrayInputStream("a\nb".getBytes())) {
			ProcessStreamReader reader = new ProcessStreamReader(inputStream, new Function<String, Evaluation>() {

				@Override
				public Evaluation apply(String input) {
					return ConclusiveEvaluation.SUCCESS;
				}

			}, false, true);
			reader.run();
			ProcessStreamResult result = reader.getResult();
			assertFalse(result.failedWithoutException());
			assertNull(result.getException());
			assertEquals("a\n", result.getStream());
			assertTrue(result.interrupted());
			assertTrue(result.succeeded());
		}
	}

	@Test
	public void testFailureByEvaluation() throws IOException {
		try (InputStream inputStream = new ByteArrayInputStream("a\nb".getBytes())) {
			ProcessStreamReader reader = new ProcessStreamReader(inputStream, new Function<String, Evaluation>() {

				@Override
				public Evaluation apply(String input) {
					return ConclusiveEvaluation.FAILURE;
				}

			}, false, false);
			reader.run();
			ProcessStreamResult result = reader.getResult();
			assertTrue(result.failedWithoutException());
			assertNull(result.getException());
			assertEquals("a\nb\n", result.getStream());
			assertFalse(result.interrupted());
			assertTrue(result.failed());
		}
	}

	@Test
	public void testFailureByEvaluationAndInterruption() throws IOException {
		try (InputStream inputStream = new ByteArrayInputStream("a\nb".getBytes())) {
			ProcessStreamReader reader = new ProcessStreamReader(inputStream, new Function<String, Evaluation>() {

				@Override
				public Evaluation apply(String input) {
					return ConclusiveEvaluation.FAILURE;
				}

			}, true, false);
			reader.run();
			ProcessStreamResult result = reader.getResult();
			assertTrue(result.failedWithoutException());
			assertNull(result.getException());
			assertEquals("a\n", result.getStream());
			assertTrue(result.interrupted());
			assertTrue(result.failed());
		}
	}

	@Test
	public void testFailureByIOException() throws IOException {
		try (InputStream inputStream = new ByteArrayInputStream("info 1".getBytes())) {
			InputStreamReader mockStreamReader = mock(InputStreamReader.class);
			BufferedReader mockBufferedReader = mock(BufferedReader.class);

			ProcessStreamReader reader = spy(new ProcessStreamReader(inputStream, new Function<String, Evaluation>() {

				@Override
				public Evaluation apply(String input) {
					return Evaluation.NON_CONCLUSIVE;
				}

			}, false, false));

			doReturn(mockStreamReader).when(reader).inputStreamReader();
			doReturn(mockBufferedReader).when(reader).bufferedReader(mockStreamReader);
			doThrow(new IOException("mock")).when(mockBufferedReader).readLine();

			reader.run();
			ProcessStreamResult result = reader.getResult();
			assertFalse(result.failedWithoutException());
			assertNotNull(result.getException());
			assertEquals(IOException.class, reader.getResult().getException().getClass());
			assertEquals("mock", reader.getResult().getException().getMessage());
			assertEquals("", result.getStream());
			assertFalse(result.interrupted());
			assertTrue(result.failed());
		}
	}

	@Test
	public void testNonConclusiveByEvaluation() throws IOException {
		try (InputStream inputStream = new ByteArrayInputStream("a\nb".getBytes())) {
			ProcessStreamReader reader = new ProcessStreamReader(inputStream, new Function<String, Evaluation>() {

				@Override
				public Evaluation apply(String input) {
					return Evaluation.NON_CONCLUSIVE;
				}

			}, false, false);
			reader.run();
			ProcessStreamResult result = reader.getResult();
			assertFalse(result.failedWithoutException());
			assertNull(result.getException());
			assertEquals("a\nb\n", result.getStream());
			assertFalse(result.interrupted());
			assertTrue(result.nonConclusive());
		}
	}

}
