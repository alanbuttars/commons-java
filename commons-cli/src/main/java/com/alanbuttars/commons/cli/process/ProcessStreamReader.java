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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import com.alanbuttars.commons.cli.evaluator.evaluation.ConclusiveEvaluation;
import com.alanbuttars.commons.cli.evaluator.evaluation.Evaluation;
import com.alanbuttars.commons.cli.util.Function;
import com.alanbuttars.commons.util.annotations.VisibleForTesting;

/**
 * A thread which parses a {@link Process}'s stream. This thread is responsible for determining the success or failure
 * of said stream via an evaluation function and embedding that information in a {@link ProcessStreamResult}.
 * 
 * @author Alan Buttars
 *
 */
class ProcessStreamReader extends Thread {

	private final InputStream inputStream;
	private final Function<String, Evaluation> evaluationFunction;
	private final boolean interruptOnFailure;
	private final boolean interruptOnSuccess;
	private final ProcessStreamResult result;

	/**
	 * Constructs an instance of this class.
	 * 
	 * @param inputStream
	 *            an open, non-null input stream from the {@link Process}
	 * @param evaluationFunction
	 *            non-null function to determine whether this {@link #inputStream} indicates success or not
	 * @param interruptOnFailure
	 *            if <code>true</code>, this thread will return after the first indication of failure
	 * @param interruptOnSuccess
	 *            if <code>true</code>, this thread will return after the first indication of success
	 */
	public ProcessStreamReader(InputStream inputStream, Function<String, Evaluation> evaluationFunction, boolean interruptOnFailure, boolean interruptOnSuccess) {
		this.inputStream = inputStream;
		this.evaluationFunction = evaluationFunction;
		this.interruptOnFailure = interruptOnFailure;
		this.interruptOnSuccess = interruptOnSuccess;
		this.result = new ProcessStreamResult();
	}

	@Override
	public void run() {
		try (InputStreamReader reader = inputStreamReader();
				BufferedReader bufferedReader = bufferedReader(reader)) {

			String line = null;
			while ((line = bufferedReader.readLine()) != null) {
				result.appendToStream(line);
				if (result.nonConclusive()) {
					Evaluation evaluation = evaluationFunction.apply(line);
					if (evaluation.succeeded()) {
						result.setEvaluation(evaluation);
						if (interruptOnSuccess) {
							result.setInterrupted(true);
							return;
						}
					}
					else if (evaluation.failed()) {
						result.setEvaluation(evaluation);
						if (interruptOnFailure) {
							result.setInterrupted(true);
							return;
						}
					}
				}
			}
		}
		catch (IOException e) {
			result.setEvaluation(ConclusiveEvaluation.FAILURE);
			result.setException(e);
		}
	}

	@VisibleForTesting
	protected InputStreamReader inputStreamReader() {
		return new InputStreamReader(inputStream);
	}

	@VisibleForTesting
	protected BufferedReader bufferedReader(InputStreamReader reader) {
		return new BufferedReader(reader);
	}

	/**
	 * @return the result of the parsed stream. Note that this method should <strong>only</strong> be called after the
	 *         completion or interruption of this thread
	 */
	public ProcessStreamResult getResult() {
		return result;
	}

}
