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

/**
 * Simple extension of {@link CommandLineEvaluatorAbstractImpl} which ignores the output of a {@link Process}'s info and
 * error streams, instead evaluating success based only on the exit code.
 * <p>
 * By default this function will interpret any {@link Process} which completes with a non-zero exit code to be a
 * failure, but this may be easily amended:
 * </p>
 *
 * <pre>
 * new CommandLineRequestBuilder().evaluateWith(new CommandLineEvaluatorExitStatusImpl() {
 *
 * 	&#64;Override
 * 	public boolean evaluateExitCode(int exitCode) {
 * 		return exitCode != -1;
 * 	}
 * });
 * </pre>
 *
 * @author Alan Buttars
 *
 */
public class CommandLineEvaluatorExitStatusImpl extends CommandLineEvaluatorAbstractImpl {

	@Override
	public boolean evaluateInfoStream(String infoStreamLine) {
		return true;
	}

	@Override
	public boolean evaluateErrorStream(String errorStreamLine) {
		return true;
	}
}
