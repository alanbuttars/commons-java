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

import com.alanbuttars.commons.util.annotations.VisibleForTesting;

/**
 * A thread hook belonging to {@link ProcessStreamReaderTest}. This hook activates when the stream reader either (1)
 * concludes, or (2) is interrupted. In either case, this thread is responsible for:
 * <ol>
 * <li>Performing cleanup operations on the {@link ProcessStreamResult}, if necessary</li>
 * <li>Killing the process, if requested, if the process failed</li>
 * </ol>
 * 
 * @author Alan Buttars
 *
 */
class ProcessStreamListener extends Thread {

	private final Process process;
	private final ProcessStreamReader reader;
	private final boolean interruptOnFailure;
	private final long interruptAfter;

	public ProcessStreamListener(Process process, ProcessStreamReader reader, boolean interruptOnFailure, long interruptAfter) {
		this.process = process;
		this.reader = reader;
		this.interruptOnFailure = interruptOnFailure;
		this.interruptAfter = interruptAfter;
	}

	@Override
	public void run() {
		try {
			reader.join(interruptAfter);
			if (reader.isAlive()) {
				interruptReader();
			}
			maybeKillProcess();
		}
		catch (InterruptedException e) {
			maybeKillProcess();
			updateInterruptedProcess(e);
		}
	}

	/**
	 * If {@link #interruptOnFailure} is <code>true</code>, interrupts the running process.
	 */
	private void maybeKillProcess() {
		ProcessStreamResult result = reader.getResult();
		if (!result.succeeded() && interruptOnFailure) {
			process.destroy();
		}
	}

	/**
	 * Interrupts the running {@link #reader}.
	 */
	@VisibleForTesting
	protected void interruptReader() {
		reader.interrupt();
		updateInterruptedProcess(new InterruptedException("Process interrupted after " + interruptAfter + " millis"));
	}

	/**
	 * Updates the {@link #reader}'s contained {@link ProcessStreamResult} following an interruption.
	 * 
	 * @param e
	 *            Exception thrown either by the {@link #reader} or this class
	 */
	private void updateInterruptedProcess(InterruptedException e) {
		ProcessStreamResult result = reader.getResult();
		result.setSuccess(false);
		if (result.getException() == null) {
			result.setException(e);
		}
	}

}
