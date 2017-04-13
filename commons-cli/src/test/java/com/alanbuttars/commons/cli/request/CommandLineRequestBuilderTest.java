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
package com.alanbuttars.commons.cli.request;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;

import com.alanbuttars.commons.cli.evaluator.CommandLineEvaluatorExitStatusImpl;
import com.alanbuttars.commons.cli.evaluator.CommandLineEvaluatorKeywordImpl;
import com.alanbuttars.commons.cli.util.Argument;

/**
 * Test class for {@link CommandLineRequestBuilder}.
 * 
 *
 * @author Alan Buttars
 *
 */
public class CommandLineRequestBuilderTest {

	@Test
	public void testConstructor() {
		CommandLineRequest request = new CommandLineRequestBuilder()//
				.build("bash");
		assertFalse(request.interruptOnFailure());
		assertEquals(0, request.interruptAfter());
		assertEquals(CommandLineEvaluatorExitStatusImpl.class, request.getEvaluator().getClass());
	}

	@Test
	public void testWithEvaluator() {
		CommandLineEvaluatorKeywordImpl evaluator = new CommandLineEvaluatorKeywordImpl();
		CommandLineRequest request = new CommandLineRequestBuilder()//
				.withEvaluator(evaluator)//
				.build("bash");
		assertEquals(evaluator, request.getEvaluator());
	}

	@Test
	public void testWithEvaluatorNull() {
		try {
			new CommandLineRequestBuilder().withEvaluator(null);
			fail();
		}
		catch (IllegalArgumentException e) {
			assertEquals("Evaluator must be non-null", e.getMessage());
		}
	}

	@Test
	public void testInterruptOnFailure() {
		CommandLineRequest request = new CommandLineRequestBuilder().interruptOnFailure().build("bash");
		assertTrue(request.interruptOnFailure());
	}

	@Test
	public void testInterruptAfterZero() {
		CommandLineRequest request = new CommandLineRequestBuilder().interruptAfter(0).build("bash");
		assertEquals(0, request.interruptAfter());
	}

	@Test
	public void testInterruptAfterPositive() {
		CommandLineRequest request = new CommandLineRequestBuilder().interruptAfter(10).build("bash");
		assertEquals(10, request.interruptAfter());
	}

	@Test
	public void testInterruptAfterNegative() {
		try {
			new CommandLineRequestBuilder().interruptAfter(-1);
			fail();
		}
		catch (IllegalArgumentException e) {
			assertEquals("Milliseconds must be non-negative", e.getMessage());
		}
	}

	@Test
	public void testBuildString() {
		CommandLineRequest request = new CommandLineRequestBuilder()//
				.build("exe --key1 value1 --key2 value2");
		List<Argument> arguments = request.getArguments();
		assertEquals(5, arguments.size());
		assertEquals("exe", arguments.get(0).getValue());
		assertEquals("--key1", arguments.get(1).getValue());
		assertEquals("value1", arguments.get(2).getValue());
		assertEquals("--key2", arguments.get(3).getValue());
		assertEquals("value2", arguments.get(4).getValue());
	}

	@Test
	public void testBuildStringNull() {
		String command = null;
		try {
			new CommandLineRequestBuilder().build(command);
			fail();
		}
		catch (IllegalArgumentException e) {
			assertEquals("Command must be non-null", e.getMessage());
		}
	}

	@Test
	public void testBuildStringEmpty() {
		String command = " ";
		try {
			new CommandLineRequestBuilder().build(command);
			fail();
		}
		catch (IllegalArgumentException e) {
			assertEquals("Command must be non-empty", e.getMessage());
		}
	}

	@Test
	public void testBuildArguments() {
		List<Argument> inputArguments = Arrays.asList(//
				new Argument("exe"), //
				new Argument("--key1"), //
				new Argument("value with spaces", '"'), //
				new Argument("--key2"), //
				new Argument("value_without_spaces", '"'));
		CommandLineRequest request = new CommandLineRequestBuilder()//
				.build(inputArguments);
		List<Argument> arguments = request.getArguments();
		assertEquals(5, arguments.size());
		assertEquals("exe", arguments.get(0).getValue());
		assertEquals("--key1", arguments.get(1).getValue());
		assertEquals("\"value with spaces\"", arguments.get(2).getValue());
		assertEquals("--key2", arguments.get(3).getValue());
		assertEquals("value_without_spaces", arguments.get(4).getValue());
	}

	@Test
	public void testBuildArgumentsNull() {
		List<Argument> arguments = null;
		try {
			new CommandLineRequestBuilder().build(arguments);
			fail();
		}
		catch (IllegalArgumentException e) {
			assertEquals("Arguments must be non-null", e.getMessage());
		}
	}

	@Test
	public void testBuildArgumentsEmpty() {
		List<Argument> arguments = new ArrayList<>();
		try {
			new CommandLineRequestBuilder().build(arguments);
			fail();
		}
		catch (IllegalArgumentException e) {
			assertEquals("Arguments must be non-empty", e.getMessage());
		}
	}
}
