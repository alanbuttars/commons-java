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

import com.alanbuttars.commons.cli.evaluator.evaluation.Evaluation;

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
 * 	public Evaluation evaluateExitCode(int exitCode) {
 * 		if (exitCode == -1) {
 * 			return Evaluation.SUCCESS;
 * 		}
 * 		return Evaluation.FAILURE;
 * 	}
 * });
 * </pre>
 *
 * @author Alan Buttars
 *
 */
public class CommandLineEvaluatorExitStatusImpl extends CommandLineEvaluatorAbstractImpl {

	/**
	 * {@inheritDoc} This class ignores the info stream.
	 */
	@Override
	public Evaluation evaluateInfoStream(String infoStreamLine) {
		return Evaluation.NON_CONCLUSIVE;
	}

	/**
	 * {@inheritDoc} This class ignores the error stream.
	 */
	@Override
	public Evaluation evaluateErrorStream(String errorStreamLine) {
		return Evaluation.NON_CONCLUSIVE;
	}
}
