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
package com.alanbuttars.commons.cli.evaluator.evaluation;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.alanbuttars.commons.cli.evaluator.evaluation.ConclusiveEvaluation;
import com.alanbuttars.commons.cli.evaluator.evaluation.Evaluation;

/**
 * Test class for {@link Evaluation}.
 * 
 * @author Alan Buttars
 *
 */
public class EvaluationTest {

	@Test
	public void testSucceeded() {
		assertTrue(ConclusiveEvaluation.SUCCESS.succeeded());
		assertFalse(ConclusiveEvaluation.FAILURE.succeeded());
		assertFalse(Evaluation.NON_CONCLUSIVE.succeeded());
	}

	@Test
	public void testFailed() {
		assertFalse(ConclusiveEvaluation.SUCCESS.failed());
		assertTrue(ConclusiveEvaluation.FAILURE.failed());
		assertFalse(Evaluation.NON_CONCLUSIVE.failed());
	}

	@Test
	public void testNonConclusive() {
		assertFalse(ConclusiveEvaluation.SUCCESS.nonConclusive());
		assertFalse(ConclusiveEvaluation.FAILURE.nonConclusive());
		assertTrue(Evaluation.NON_CONCLUSIVE.nonConclusive());
	}

}
