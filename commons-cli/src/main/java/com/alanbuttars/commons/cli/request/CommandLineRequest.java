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

import java.util.List;

import com.alanbuttars.commons.cli.evaluator.CommandLineEvaluator;
import com.alanbuttars.commons.cli.util.Argument;

/**
 * Models a command line request, including:
 * <ul>
 * <li>The arguments to be deployed</li>
 * <li>The behavior for evaluating the {@link Process} result</li>
 * <li>Any additional behavior dictating how the {@link Process} is deployed</li>
 * </ul>
 * 
 *
 * @author Alan Buttars
 *
 */
public class CommandLineRequest {

	private List<Argument> arguments;
	private CommandLineEvaluator evaluator;
	private boolean interruptOnFailure;
	private boolean interruptOnSuccess;
	private long interruptAfter;

	CommandLineRequest() {
	}

	/**
	 * @return the command line arguments
	 */
	public List<Argument> getArguments() {
		return arguments;
	}

	void setArguments(List<Argument> arguments) {
		this.arguments = arguments;
	}

	/**
	 * @return the evaluation function
	 */
	public CommandLineEvaluator getEvaluator() {
		return evaluator;
	}

	void setEvaluator(CommandLineEvaluator evaluator) {
		this.evaluator = evaluator;
	}

	/**
	 * @return <code>true</code> if the command line process should be halted as soon as a failure is detected
	 */
	public boolean interruptOnFailure() {
		return interruptOnFailure;
	}

	void setInterruptOnFailure(boolean interruptOnFailure) {
		this.interruptOnFailure = interruptOnFailure;
	}

	/**
	 * @return <code>true</code> if the command line process should be halted as soon as a success is detected
	 */
	public boolean interruptOnSuccess() {
		return interruptOnSuccess;
	}

	void setInterruptOnSuccess(boolean interruptOnSuccess) {
		this.interruptOnSuccess = interruptOnSuccess;
	}

	/**
	 * @return number of milliseconds to wait until interrupting the {@link Process}
	 */
	public long interruptAfter() {
		return interruptAfter;
	}

	void setInterruptAfter(long interruptAfter) {
		this.interruptAfter = interruptAfter;
	}

}
