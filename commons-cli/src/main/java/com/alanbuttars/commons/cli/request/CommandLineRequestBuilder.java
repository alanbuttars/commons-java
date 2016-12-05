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

import java.util.ArrayList;
import java.util.List;

import com.alanbuttars.commons.cli.evaluator.CommandLineEvaluator;
import com.alanbuttars.commons.cli.evaluator.CommandLineEvaluatorExitStatusImpl;
import com.alanbuttars.commons.cli.util.Argument;

/**
 * A builder pattern class which generates a {@link CommandLineRequest}. An explanation of methods:
 *
 * <p>
 * <strong>{@link #withEvaluator(CommandLineEvaluator)}</strong><br/>
 * By default, process results are evaluated with a {@link CommandLineEvaluatorExitStatusImpl}, which does nothing more
 * than fail processes which return a non-zero exit code. This behavior can be changed by invoking
 * {@link #withEvaluator(CommandLineEvaluator)} with either an existing or anonymous evaluation function.
 * </p>
 *
 * <p>
 * <strong>{@link #interruptOnFailure()}</strong><br/>
 * By default, processes are <strong>not</strong> interrupted when a failure is first detected. For example, let us take
 * the request:
 *
 * <pre>
 * new CommandLineRequestBuilder().build(&quot;echo ERROR; sleep 5; echo END&quot;);
 * </pre>
 *
 * Even if the {@link #evaluator} detects that "ERROR" indicates a failure, by default the process will not be halted
 * until the command completes. This behavior can be circumvented with:
 *
 * <pre>
 * new CommandLineRequestBuilder().interruptOnFailure().build(&quot;echo ERROR; sleep 5; echo END&quot;);
 * </pre>
 *
 * </p>
 *
 * @author Alan Buttars
 *
 */
public class CommandLineRequestBuilder {

	private CommandLineEvaluator evaluator;
	private boolean interruptOnFailure;
	private long interruptAfter;

	/**
	 * Instantiates a new builder with defaults:
	 */
	public CommandLineRequestBuilder() {
		this.evaluator = new CommandLineEvaluatorExitStatusImpl();
		this.interruptOnFailure = false;
		this.interruptAfter = 0;
	}

	/**
	 * Sets the function used to evaluate {@link Process} results.
	 *
	 * @param evaluator
	 *            Non-null evaluation function
	 */
	public CommandLineRequestBuilder withEvaluator(CommandLineEvaluator evaluator) {
		this.evaluator = evaluator;
		return this;
	}

	/**
	 * Indicates that the {@link Process} should be interrupted at the first indication of a failure identifed by the
	 * {@link #evaluator}.
	 */
	public CommandLineRequestBuilder interruptOnFailure() {
		this.interruptOnFailure = true;
		return this;
	}

	/**
	 * The time, in milliseconds, that the {@link Process} should be allowed to run. If the thread runs longer than this
	 * time limit, the thread will be interrupted and {@link CommandLineEvaluator#evaluateException(Exception)} will be
	 * invoked with a {@link InterruptedException}.
	 *
	 * @param milliseconds
	 */
	public CommandLineRequestBuilder interruptAfter(int milliseconds) {
		this.interruptAfter = milliseconds;
		return this;
	}

	/**
	 * Builds a {@link CommandLineRequest} with <code>String</code> command.
	 *
	 * @param command
	 *            Non-null and non-blank command line arguments
	 * @return the request
	 */
	public CommandLineRequest build(String command) {
		List<Argument> arguments = new ArrayList<Argument>();
		for (String commandPart : command.split(" ")) {
			arguments.add(new Argument(commandPart));
		}
		return build(arguments);
	}

	/**
	 * Builds a {@link CommandLineRequest} with arguments.
	 *
	 * @param arguments
	 *            Non-null and non-empty command line arguments
	 * @return the request
	 */
	public CommandLineRequest build(List<Argument> arguments) {
		CommandLineRequest request = new CommandLineRequest();
		request.setArguments(arguments);
		request.setEvaluator(evaluator);
		request.setInterruptOnFailure(interruptOnFailure);
		request.setInterruptAfter(interruptAfter);
		return request;
	}

}
