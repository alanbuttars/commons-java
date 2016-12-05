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
package com.alanbuttars.commons.cli.response;

import com.alanbuttars.commons.cli.response.CommandLineResponse;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

/**
 * Test class for {@link CommandLineResponse}.
 * 
 *
 * @author Alan Buttars
 *
 */
public class CommandLineResponseTest {

	@Test
	public void testExceptionThrown() {
		CommandLineResponse response = new CommandLineResponse();
		response.setExitCode(CommandLineResponse.EXCEPTION_THROWN_EXIT_CODE);
		assertTrue(response.exceptionThrown());
		assertFalse(response.interrupted());
	}

	@Test
	public void testInterrupted() {
		CommandLineResponse response = new CommandLineResponse();
		response.setExitCode(CommandLineResponse.INTERRUPTED_BEFORE_COMPLETION_EXIT_CODE);
		assertTrue(response.interrupted());
		assertFalse(response.exceptionThrown());
	}
}
