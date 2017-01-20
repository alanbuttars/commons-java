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

import java.util.HashSet;
import java.util.Set;

import com.alanbuttars.commons.cli.evaluator.evaluation.ConclusiveEvaluation;
import com.alanbuttars.commons.cli.evaluator.evaluation.Evaluation;

/**
 * Extension of {@link CommandLineEvaluatorAbstractImpl} which takes a white-and-blacklist approach to interpreting
 * {@link Process}es. When instances of this class evaluate a line from an info (see
 * {@link #evaluateInfoStream(String)}) or exit stream (see {@link #evaluateErrorStream(String)}), they use a collection
 * of keywords of three types:
 * <ul>
 * <li><strong>ignorable</strong> - indicates that the line may be ignored entirely</li>
 * <li><strong>failure</strong> - indicates that the line indicates an {@link ConclusiveEvaluation#FAILURE}</li>
 * <li><strong>success</strong> - indicates that the line indicates an {@link ConclusiveEvaluation#SUCCESS}</li>
 * </ul>
 * 
 * <p>
 * These keywords are evaluated in the order that they are listed above. In other words, a line which contains both an
 * <strong>ignorable</strong> and a <strong>failure</strong> keyword will be evaluated as
 * {@link Evaluation#NON_CONCLUSIVE}, even if the <strong>failure</strong> keyword appears earlier in the line.
 * </p>
 * 
 * @author Alan Buttars
 *
 */
public class CommandLineEvaluatorKeywordImpl extends CommandLineEvaluatorAbstractImpl {

	private final Set<String> successFlags;
	private final Set<String> failureFlags;
	private final Set<String> ignorableFlags;

	public CommandLineEvaluatorKeywordImpl() {
		this.successFlags = new HashSet<>();
		this.failureFlags = new HashSet<>();
		this.ignorableFlags = new HashSet<>();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Evaluation evaluateInfoStream(String infoStreamLine) {
		return evaluateStream(infoStreamLine);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Evaluation evaluateErrorStream(String errorStreamLine) {
		return evaluateStream(errorStreamLine);
	}

	/**
	 * Evaluates the contents of a single {@link Process}'s stream line.
	 * 
	 * @param streamLine
	 *            Non-null input stream line
	 */
	private Evaluation evaluateStream(String streamLine) {
		for (String ignorableFlag : ignorableFlags) {
			if (streamLine.contains(ignorableFlag)) {
				return Evaluation.NON_CONCLUSIVE;
			}
		}
		for (String failureFlag : failureFlags) {
			if (streamLine.contains(failureFlag)) {
				return ConclusiveEvaluation.FAILURE;
			}
		}
		for (String successFlag : successFlags) {
			if (streamLine.contains(successFlag)) {
				return ConclusiveEvaluation.SUCCESS;
			}
		}
		return Evaluation.NON_CONCLUSIVE;
	}

	/**
	 * Adds a success keyword.
	 */
	public CommandLineEvaluatorKeywordImpl succeedOn(String keyword) {
		successFlags.add(keyword);
		return this;
	}

	/**
	 * Adds a failure keyword.
	 */
	public CommandLineEvaluatorKeywordImpl failOn(String keyword) {
		failureFlags.add(keyword);
		return this;
	}

	/**
	 * Adds an ignorable keyword.
	 */
	public CommandLineEvaluatorKeywordImpl ignore(String keyword) {
		ignorableFlags.add(keyword);
		return this;
	}
}
