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

import java.util.Arrays;

import org.junit.Test;

import com.alanbuttars.commons.cli.evaluator.CommandLineEvaluatorAbstractImpl;
import com.alanbuttars.commons.cli.evaluator.evaluation.ConclusiveEvaluation;
import com.alanbuttars.commons.cli.evaluator.evaluation.Evaluation;
import com.alanbuttars.commons.cli.request.CommandLineRequest;
import com.alanbuttars.commons.cli.request.CommandLineRequestBuilder;
import com.alanbuttars.commons.cli.response.CommandLineResponse;

/**
 * Integration test class for {@link Processes}.
 * 
 * @author Alan Buttars
 *
 */
public class ProcessesIntegrationTest extends ProcessesIntegrationAbstractTest {

	@Test
	public void testSuccess() {
		CommandLineRequest request = new CommandLineRequestBuilder()//
				.build(Arrays.asList(sh(), exitCode0Script()));
		CommandLineResponse response = Processes.execute(request);
		assertEquals(0, response.getExitCode());
		assertTrue(response.succeeded());
		assertEquals("info 1\ninfo 2\n", response.getInfoStream());
		assertEquals("error 1\nerror 2\n", response.getErrorStream());
		assertNull(response.getException());
		assertFalse(response.exceptionThrown());
		assertFalse(response.interrupted());
	}

	@Test
	public void testFailureByEvaluatorInfoStream() {
		CommandLineRequest request = new CommandLineRequestBuilder()//
				.withEvaluator(new CommandLineEvaluatorAbstractImpl() {

					@Override
					public Evaluation evaluateInfoStream(String infoStreamLine) {
						if (infoStreamLine.contains("info 1")) {
							return Evaluation.NON_CONCLUSIVE;
						}
						return ConclusiveEvaluation.FAILURE;
					}

					@Override
					public Evaluation evaluateErrorStream(String errorStreamLine) {
						return Evaluation.NON_CONCLUSIVE;
					}

				})//
				.build(Arrays.asList(sh(), exitCode0Script()));
		CommandLineResponse response = Processes.execute(request);
		assertEquals(0, response.getExitCode());
		assertTrue(response.failed());
		assertEquals("info 1\ninfo 2\n", response.getInfoStream());
		assertEquals("error 1\nerror 2\n", response.getErrorStream());
		assertNull(response.getException());
		assertFalse(response.exceptionThrown());
		assertFalse(response.interrupted());
	}

	@Test
	public void testFailureByEvaluatorErrorStream() {
		CommandLineRequest request = new CommandLineRequestBuilder()//
				.withEvaluator(new CommandLineEvaluatorAbstractImpl() {

					@Override
					public Evaluation evaluateInfoStream(String infoStreamLine) {
						return Evaluation.NON_CONCLUSIVE;
					}

					@Override
					public Evaluation evaluateErrorStream(String errorStreamLine) {
						if (errorStreamLine.contains("error 1")) {
							return Evaluation.NON_CONCLUSIVE;
						}
						return ConclusiveEvaluation.FAILURE;
					}

				})//
				.build(Arrays.asList(sh(), exitCode0Script()));
		CommandLineResponse response = Processes.execute(request);
		assertEquals(0, response.getExitCode());
		assertTrue(response.failed());
		assertEquals("info 1\ninfo 2\n", response.getInfoStream());
		assertEquals("error 1\nerror 2\n", response.getErrorStream());
		assertNull(response.getException());
		assertFalse(response.exceptionThrown());
		assertFalse(response.interrupted());
	}

	@Test
	public void testFailureByExitCode() {
		CommandLineRequest request = new CommandLineRequestBuilder()//
				.build(Arrays.asList(sh(), exitCode1Script()));
		CommandLineResponse response = Processes.execute(request);
		assertEquals(1, response.getExitCode());
		assertTrue(response.failed());
		assertEquals("info 1\ninfo 2\n", response.getInfoStream());
		assertEquals("error 1\nerror 2\n", response.getErrorStream());
		assertNull(response.getException());
		assertFalse(response.exceptionThrown());
		assertFalse(response.interrupted());
	}

	@Test
	public void testFailureByInterruption() {
		CommandLineRequest request = new CommandLineRequestBuilder()//
				.interruptAfter(200)//
				.build(Arrays.asList(sh(), exitCode0SlowScript()));
		CommandLineResponse response = Processes.execute(request);
		assertEquals(CommandLineResponse.INTERRUPTED_BEFORE_COMPLETION_EXIT_CODE, response.getExitCode());
		assertTrue(response.failed());
		assertEquals("info 1\n", response.getInfoStream());
		assertEquals("", response.getErrorStream());
		assertEquals(InterruptedException.class, response.getException().getClass());
		assertEquals("Process interrupted after 200 millis", response.getException().getMessage());
		assertFalse(response.exceptionThrown());
		assertTrue(response.interrupted());
	}

	@Test
	public void testInterruptOnFailureByInfoStream() {
		CommandLineRequest request = new CommandLineRequestBuilder()//
				.interruptOnFailure()//
				.withEvaluator(new CommandLineEvaluatorAbstractImpl() {

					@Override
					public ConclusiveEvaluation evaluateInfoStream(String infoStreamLine) {
						return ConclusiveEvaluation.FAILURE;
					}

					@Override
					public Evaluation evaluateErrorStream(String errorStreamLine) {
						return Evaluation.NON_CONCLUSIVE;
					}

				})//
				.build(Arrays.asList(sh(), exitCode0SlowScript()));
		CommandLineResponse response = Processes.execute(request);
		assertEquals(CommandLineResponse.INTERRUPTED_BEFORE_COMPLETION_EXIT_CODE, response.getExitCode());
		assertTrue(response.failed());
		assertEquals("info 1\n", response.getInfoStream());
		assertEquals("", response.getErrorStream());
		assertNull(response.getException());
		assertFalse(response.exceptionThrown());
		assertTrue(response.interrupted());
	}

	@Test
	public void testInterruptOnFailureByErrorStream() {
		CommandLineRequest request = new CommandLineRequestBuilder()//
				.interruptOnFailure()//
				.withEvaluator(new CommandLineEvaluatorAbstractImpl() {

					@Override
					public Evaluation evaluateInfoStream(String infoStreamLine) {
						return Evaluation.NON_CONCLUSIVE;
					}

					@Override
					public Evaluation evaluateErrorStream(String errorStreamLine) {
						return ConclusiveEvaluation.FAILURE;
					}

				})//
				.build(Arrays.asList(sh(), exitCode0Script()));
		CommandLineResponse response = Processes.execute(request);
		assertEquals(CommandLineResponse.INTERRUPTED_BEFORE_COMPLETION_EXIT_CODE, response.getExitCode());
		assertTrue(response.failed());
		assertEquals("info 1\ninfo 2\n", response.getInfoStream());
		assertEquals("error 1\n", response.getErrorStream());
		assertNull(response.getException());
		assertFalse(response.exceptionThrown());
		assertTrue(response.interrupted());
	}

}
