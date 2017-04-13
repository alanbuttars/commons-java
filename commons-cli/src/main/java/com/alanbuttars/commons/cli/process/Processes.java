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

import static com.alanbuttars.commons.util.validators.Arguments.verifyNonNull;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.alanbuttars.commons.cli.evaluator.CommandLineEvaluator;
import com.alanbuttars.commons.cli.evaluator.evaluation.ConclusiveEvaluation;
import com.alanbuttars.commons.cli.evaluator.evaluation.Evaluation;
import com.alanbuttars.commons.cli.request.CommandLineRequest;
import com.alanbuttars.commons.cli.request.CommandLineRequestBuilder;
import com.alanbuttars.commons.cli.response.CommandLineResponse;
import com.alanbuttars.commons.cli.util.Argument;
import com.alanbuttars.commons.util.annotations.VisibleForTesting;
import com.alanbuttars.commons.util.functions.Function;

/**
 * Utility function class for {@link Process}es. This class is the entrypoint to the <code>commons-cli</code> library:
 * 
 * <pre>
 * CommandLineRequest request = new CommandLineRequestBuilder().build("/path/to/script --key1 val1 --key2 val2");
 * CommandLineResponse response = Processes.execute(request);
 * </pre>
 * 
 * For more details on how to construct a {@link CommandLineRequest}, see {@link CommandLineRequestBuilder}.<br/>
 * To see how to interpret a {@link CommandLineResponse}, see that class.
 *
 * @author Alan Buttars
 *
 */
public class Processes {

	/**
	 * Synchronously executes a given {@link CommandLineRequest}. This method swallows all exceptions and wraps them in
	 * the returned {@link CommandLineResponse}.
	 *
	 * @param request
	 *            Non-null request
	 */
	public static CommandLineResponse execute(final CommandLineRequest request) {
		verifyNonNull(request, "Request must be non-null");
		try {
			Process process = startProcess(request);

			ProcessStreamReader inputStreamReader = createInputStreamReader(process, request);
			ProcessStreamReader errorStreamReader = createErrorStreamReader(process, request);

			ProcessStreamListener inputStreamListener = createStreamListener(process, request, inputStreamReader);
			ProcessStreamListener errorStreamListener = createStreamListener(process, request, errorStreamReader);

			inputStreamReader.start();
			errorStreamReader.start();

			inputStreamListener.start();
			errorStreamListener.start();

			inputStreamListener.join();
			errorStreamListener.join();

			ProcessStreamResult inputStreamResult = inputStreamReader.getResult();
			ProcessStreamResult errorStreamResult = errorStreamReader.getResult();

			CommandLineResponse mergedResponse = mergeResults(process, inputStreamResult, errorStreamResult, request.getEvaluator());
			return mergedResponse;
		}
		catch (IOException | InterruptedException e) {
			return request.getEvaluator().evaluateException(e);
		}
	}

	/**
	 * Builds and kicks off the underlying {@link Process}.
	 * 
	 * @throws IOException
	 */
	@VisibleForTesting
	protected static Process startProcess(CommandLineRequest request) throws IOException {
		List<String> arguments = new ArrayList<>();
		for (Argument argument : request.getArguments()) {
			arguments.add(argument.getValue());
		}
		return new ProcessBuilder(arguments).start();
	}

	/**
	 * Creates the thread which will read and process the {@link Process}'s input stream.
	 */
	@VisibleForTesting
	protected static ProcessStreamReader createInputStreamReader(Process process, final CommandLineRequest request) {
		return new ProcessStreamReader(process.getInputStream(), new Function<String, Evaluation>() {

			@Override
			public Evaluation apply(String input) {
				return request.getEvaluator().evaluateInfoStream(input);
			}

		}, request.interruptOnFailure(), request.interruptOnSuccess());
	}

	/**
	 * Creates the thread which will read and process the {@lnk Process}'s error stream.
	 */
	@VisibleForTesting
	protected static ProcessStreamReader createErrorStreamReader(Process process, final CommandLineRequest request) {
		return new ProcessStreamReader(process.getErrorStream(), new Function<String, Evaluation>() {

			@Override
			public Evaluation apply(String input) {
				return request.getEvaluator().evaluateErrorStream(input);
			}

		}, request.interruptOnFailure(), request.interruptOnSuccess());
	}

	/**
	 * Creates a thread which will listen to a {@link ProcessStreamReaderTest} and react appropriately to its results.
	 */
	@VisibleForTesting
	protected static ProcessStreamListener createStreamListener(Process process, CommandLineRequest request, ProcessStreamReader streamReader) {
		return new ProcessStreamListener(process, streamReader, request.interruptOnFailure(), request.interruptOnSuccess(), request.interruptAfter());
	}

	/**
	 * From the results obtained by parsing the info and error streams, constructs a {@link CommandLineResponse}.
	 */
	private static CommandLineResponse mergeResults(Process process, ProcessStreamResult inputStreamResult, ProcessStreamResult errorStreamResult, CommandLineEvaluator evaluator) {
		CommandLineResponse response = new CommandLineResponse();

		Exception mergedException = mergeResultExceptions(inputStreamResult, errorStreamResult);
		if (mergedException instanceof InterruptedException) {
			response.setExitCode(CommandLineResponse.INTERRUPTED_BEFORE_COMPLETION_EXIT_CODE);
			response.setEvaluation(ConclusiveEvaluation.FAILURE);
		}
		else if (mergedException != null) {
			response.setExitCode(CommandLineResponse.EXCEPTION_THROWN_EXIT_CODE);
			response.setEvaluation(ConclusiveEvaluation.FAILURE);
		}
		else if (inputStreamResult.interrupted() || errorStreamResult.interrupted()) {
			response.setExitCode(CommandLineResponse.INTERRUPTED_BEFORE_COMPLETION_EXIT_CODE);
			response.setEvaluation(ConclusiveEvaluation.FAILURE);
		}
		else {
			int exitCode = process.exitValue();
			response.setExitCode(exitCode);

			Evaluation exitCodeEvaluation = evaluator.evaluateExitCode(exitCode);
			if (inputStreamResult.nonConclusive() && errorStreamResult.nonConclusive()) {
				response.setEvaluation(exitCodeEvaluation);
			}
			else if (inputStreamResult.failed() || errorStreamResult.failed()) {
				response.setEvaluation(ConclusiveEvaluation.FAILURE);
			}
			else if (exitCodeEvaluation == Evaluation.NON_CONCLUSIVE) {
				response.setEvaluation(ConclusiveEvaluation.SUCCESS);
			}
			else {
				response.setEvaluation(exitCodeEvaluation);
			}
		}
		response.setException(mergedException);
		response.setInfoStream(inputStreamResult.getStream());
		response.setErrorStream(errorStreamResult.getStream());
		return response;
	}

	/**
	 * From the exceptions which may or may not have been thrown from the info and error streams, chooses the preferred
	 * <code>Exception</code> to attach to the {@link CommandLineResponse}.
	 *
	 * <p>
	 * There is a preference to return to the user the most relevant exception. Since
	 * </p>
	 *
	 * @return Possibly null exception
	 */
	@VisibleForTesting
	private static Exception mergeResultExceptions(ProcessStreamResult inputStreamResult, ProcessStreamResult errorStreamResult) {
		if (inputStreamResult.failedWithoutException() || errorStreamResult.failedWithoutException()) {
			return null;
		}

		Exception inputStreamException = inputStreamResult.getException();
		Exception errorStreamException = errorStreamResult.getException();
		if (inputStreamException != null) {
			if (inputStreamException instanceof InterruptedException) {
				if (errorStreamException != null) {
					if (errorStreamException instanceof InterruptedException) {
						return inputStreamException;
					}
					return errorStreamException;
				}
				return inputStreamException;
			}
			return inputStreamException;
		}
		return errorStreamException;
	}
}
