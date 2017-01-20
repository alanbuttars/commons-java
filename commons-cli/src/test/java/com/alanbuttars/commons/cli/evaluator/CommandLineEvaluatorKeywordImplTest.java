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

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.alanbuttars.commons.cli.evaluator.evaluation.ConclusiveEvaluation;
import com.alanbuttars.commons.cli.evaluator.evaluation.Evaluation;

/**
 * Test class for {@link CommandLineEvaluatorKeywordImpl}.
 * 
 * @author Alan Buttars
 *
 */
public class CommandLineEvaluatorKeywordImplTest extends CommandLineEvaluatorTest<CommandLineEvaluatorKeywordImpl> {

	@Override
	public CommandLineEvaluatorKeywordImpl setupEvaluator() {
		return new CommandLineEvaluatorKeywordImpl();
	}

	@Test
	public void testSucceedOn() {
		evaluator.succeedOn("abc");
		testEvaluateStream("abc", ConclusiveEvaluation.SUCCESS);
		testEvaluateStream("def", Evaluation.NON_CONCLUSIVE);
	}

	@Test
	public void testFailOn() {
		evaluator.failOn("abc");
		testEvaluateStream("abc", ConclusiveEvaluation.FAILURE);
		testEvaluateStream("def", Evaluation.NON_CONCLUSIVE);
	}

	@Test
	public void testIgnore() {
		evaluator.ignore("abc");
		testEvaluateStream("abc", Evaluation.NON_CONCLUSIVE);
		testEvaluateStream("def", Evaluation.NON_CONCLUSIVE);
	}

	@Test
	public void testPrecedence() {
		evaluator.ignore("a")//
				.failOn("b")//
				.succeedOn("c");
		
		testEvaluateStream("abc", Evaluation.NON_CONCLUSIVE);
		testEvaluateStream("acb", Evaluation.NON_CONCLUSIVE);
		testEvaluateStream("bac", Evaluation.NON_CONCLUSIVE);
		testEvaluateStream("bca", Evaluation.NON_CONCLUSIVE);
		testEvaluateStream("cab", Evaluation.NON_CONCLUSIVE);
		testEvaluateStream("cba", Evaluation.NON_CONCLUSIVE);

		testEvaluateStream("ab", Evaluation.NON_CONCLUSIVE);
		testEvaluateStream("ac", Evaluation.NON_CONCLUSIVE);
		testEvaluateStream("ba", Evaluation.NON_CONCLUSIVE);
		testEvaluateStream("bc", ConclusiveEvaluation.FAILURE);
		testEvaluateStream("ca", Evaluation.NON_CONCLUSIVE);
		testEvaluateStream("cb", ConclusiveEvaluation.FAILURE);
	}

	private void testEvaluateStream(String streamLine, Evaluation expectedEvaluation) {
		assertEquals(expectedEvaluation, evaluator.evaluateInfoStream(streamLine));
		assertEquals(expectedEvaluation, evaluator.evaluateErrorStream(streamLine));
	}
}
