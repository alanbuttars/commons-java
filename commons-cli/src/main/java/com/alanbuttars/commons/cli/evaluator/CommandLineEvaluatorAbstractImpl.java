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

import com.alanbuttars.commons.cli.evaluator.evaluation.ConclusiveEvaluation;
import com.alanbuttars.commons.cli.response.CommandLineResponse;

/**
 * Default abstract implementation of {@link CommandLineEvaluator}.
 *
 * @author Alan Buttars
 *
 */
public abstract class CommandLineEvaluatorAbstractImpl implements CommandLineEvaluator {

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ConclusiveEvaluation evaluateExitCode(int exitCode) {
		if (exitCode == 0) {
			return ConclusiveEvaluation.SUCCESS;
		}
		return ConclusiveEvaluation.FAILURE;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public CommandLineResponse evaluateException(Exception e) {
		CommandLineResponse response = new CommandLineResponse();
		response.setEvaluation(ConclusiveEvaluation.FAILURE);
		if (e instanceof InterruptedException) {
			response.setExitCode(CommandLineResponse.INTERRUPTED_BEFORE_COMPLETION_EXIT_CODE);
		}
		else {
			response.setExitCode(CommandLineResponse.EXCEPTION_THROWN_EXIT_CODE);
		}
		response.setException(e);
		return response;
	}

}
