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

/**
 * Models a command line {@link Process} result. This object may be evaluated in
 * the following manner: <br/>
 * 
 * <pre>
 * CommandLineResponse response = getResponse();
 * if (response.succeeded()) {
 * 	logger.info(response.getInfoStream());
 * }
 * else if (response.getExitCode() == CommandLineResponse.EXCEPTION_THROWN_EXIT_CODE) {
 * 	throw response.getException();
 * }
 * else {
 * 	logger.error(response.getErrorStream());
 * }
 * </pre>
 * 
 * @author Alan Buttars
 *
 */
public class CommandLineResponse {

	public static final int EXCEPTION_THROWN_EXIT_CODE = Integer.MIN_VALUE;
	private int exitCode;
	private String infoStream;
	private String errorStream;
	private boolean success;
	private Exception exception;

	private CommandLineResponse() {
	}

	/**
	 * Creates a response for which {@link #succeeded()} evaluates to
	 * <code>true</code>.
	 * 
	 * @param exitCode
	 * @param infoStream
	 * @param errorStream
	 * @return the response
	 */
	public static CommandLineResponse success(int exitCode, String infoStream, String errorStream) {
		CommandLineResponse response = new CommandLineResponse();
		response.exitCode = exitCode;
		response.infoStream = infoStream;
		response.errorStream = errorStream;
		response.success = true;
		return response;
	}

	/**
	 * Creates a response for which {@link #succeeded()} evaluates to
	 * <code>false</code>.
	 * 
	 * @param exitCode
	 * @param infoStream
	 * @param errorStream
	 * @return the response
	 */
	public static CommandLineResponse failure(int exitCode, String infoStream, String errorStream) {
		CommandLineResponse response = new CommandLineResponse();
		response.exitCode = exitCode;
		response.infoStream = infoStream;
		response.errorStream = errorStream;
		response.success = false;
		return response;
	}

	/**
	 * Creates a response for which {@link #succeeded()} evaluates to
	 * <code>false</code>. This constructor is appropriate for when a
	 * {@link Process} throws an <code>Exception</code> either during its
	 * execution or evaluation.
	 * 
	 * @param e the thrown exception
	 * @return the response
	 */
	public static CommandLineResponse failure(Exception e) {
		CommandLineResponse response = new CommandLineResponse();
		response.exitCode = EXCEPTION_THROWN_EXIT_CODE;
		response.success = false;
		response.exception = e;
		return response;
	}

	/**
	 * @return the process's exit status
	 */
	public int getExitCode() {
		return exitCode;
	}

	/**
	 * @return the process's info stream
	 */
	public String getInfoStream() {
		return infoStream;
	}

	/**
	 * @return the process's error stream
	 */
	public String getErrorStream() {
		return errorStream;
	}

	/**
	 * @return whether the command line process succeeded
	 */
	public boolean succeeded() {
		return success;
	}

	/**
	 * @return <code>null</code> if the process executed cleanly
	 */
	public Exception getException() {
		return exception;
	}

}
