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
package com.alanbuttars.commons.cli.evaluator;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;

import com.alanbuttars.commons.cli.response.CommandLineResponse;

/**
 * Test class for {@link CommandLineEvaluatorAbstractImpl}.
 * 
 * @author Alan Buttars
 *
 */
public class CommandLineEvaluatorAbstractImplTest {

	private CommandLineEvaluator evaluator;

	@Before
	public void setup() {
		evaluator = new CommandLineEvaluatorAbstractImpl() {

			@Override
			public boolean evaluateInfoStream(String infoStreamLine) {
				return false;
			}

			@Override
			public boolean evaluateErrorStream(String errorStreamLine) {
				return false;
			}
		};
	}

	@Test
	public void testEvaluateExitCode() {
		assertTrue(evaluator.evaluateExitCode(0));
		assertFalse(evaluator.evaluateExitCode(-1));
		assertFalse(evaluator.evaluateExitCode(1));
	}

	@Test
	public void testEvaluateInterruptedException() {
		Exception exception = new InterruptedException();
		CommandLineResponse response = evaluator.evaluateException(exception);
		assertFalse(response.succeeded());
		assertTrue(response.interrupted());
		assertFalse(response.exceptionThrown());
		assertEquals(exception, response.getException());
		assertEquals(CommandLineResponse.INTERRUPTED_BEFORE_COMPLETION_EXIT_CODE, response.getExitCode());
	}

	@Test
	public void testEvaluateNonInterruptedException() {
		Exception exception = new IOException();
		CommandLineResponse response = evaluator.evaluateException(exception);
		assertFalse(response.succeeded());
		assertFalse(response.interrupted());
		assertTrue(response.exceptionThrown());
		assertEquals(exception, response.getException());
		assertEquals(CommandLineResponse.EXCEPTION_THROWN_EXIT_CODE, response.getExitCode());
	}
}
