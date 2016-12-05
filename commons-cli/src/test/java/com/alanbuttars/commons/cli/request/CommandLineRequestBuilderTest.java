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
import static org.junit.Assert.assertNull;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;

import com.alanbuttars.commons.cli.evaluator.CommandLineEvaluatorExitStatusImpl;
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
		CommandLineRequest response = new CommandLineRequestBuilder()//
				.withEvaluator(null)//
				.build("bash");
		assertNull(response.getEvaluator());
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
}
