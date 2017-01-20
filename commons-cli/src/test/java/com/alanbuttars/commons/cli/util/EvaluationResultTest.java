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
package com.alanbuttars.commons.cli.util;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.alanbuttars.commons.cli.evaluator.evaluation.ConclusiveEvaluation;
import com.alanbuttars.commons.cli.evaluator.evaluation.Evaluation;

/**
 * Test for {@link EvaluationResultTest}.
 * 
 * @author Alan Buttars
 *
 */
public class EvaluationResultTest {

	@Test
	public void testSucceeded() {
		EvaluationResult result = new EvaluationResult() {
		};
		result.setEvaluation(ConclusiveEvaluation.SUCCESS);

		assertTrue(result.succeeded());
		assertFalse(result.failed());
		assertFalse(result.nonConclusive());
	}

	@Test
	public void testFailed() {
		EvaluationResult result = new EvaluationResult() {
		};
		result.setEvaluation(ConclusiveEvaluation.FAILURE);

		assertFalse(result.succeeded());
		assertTrue(result.failed());
		assertFalse(result.nonConclusive());
	}

	@Test
	public void testNonConclusive() {
		EvaluationResult result = new EvaluationResult() {
		};
		result.setEvaluation(Evaluation.NON_CONCLUSIVE);

		assertFalse(result.succeeded());
		assertFalse(result.failed());
		assertTrue(result.nonConclusive());
	}

}
