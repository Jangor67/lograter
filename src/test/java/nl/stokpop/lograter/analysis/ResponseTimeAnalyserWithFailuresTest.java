/*
 * Copyright (C) 2019 Peter Paul Bakker, Stokpop Software Solutions
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package nl.stokpop.lograter.analysis;

import nl.stokpop.lograter.counter.RequestCounter;
import nl.stokpop.lograter.counter.RequestCounterPair;
import nl.stokpop.lograter.store.TimeMeasurementStoreInMemory;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class ResponseTimeAnalyserWithFailuresTest {

	@Test
	public void countFailureHitsTotalPeriod() {
		RequestCounter successes = createSuccessCounter();
		RequestCounter failures = createFailureCounter();

		ResponseTimeAnalyserWithFailures analyser = new ResponseTimeAnalyserWithFailures(new RequestCounterPair(successes, failures));

		assertEquals("contain all hits of failures and successes", 6, analyser.totalHits());
		assertEquals(2, analyser.failureHits());
		assertEquals(2/6d * 100, analyser.failurePercentage(), 0.0000001d);
		assertEquals(1, analyser.min());
		assertEquals(20, analyser.max());
		assertEquals("duration is total duration of failures and successes",2501, analyser.getAnalysisTimePeriod().getDurationInMillis());
	}

	@Test
	public void countFailureHitsSuccessPeriod() {
		RequestCounter successes = createSuccessCounter();
		RequestCounter failures = createFailureCounter();

		ResponseTimeAnalyserWithFailures analyser = new ResponseTimeAnalyserWithFailures(new RequestCounterPair(successes, failures), successes.getTimePeriod());

		assertEquals("contain all hits of successes period", 5, analyser.totalHits());
		assertEquals(1, analyser.failureHits());
		assertEquals(1/5d * 100, analyser.failurePercentage(), 0.0000001d);
		assertEquals("duration is total duration of successes",2101, analyser.getAnalysisTimePeriod().getDurationInMillis());
	}

	private RequestCounter createFailureCounter() {
		RequestCounter failures = new RequestCounter("failures", new TimeMeasurementStoreInMemory());
		failures.incRequests(2500, 1);
		failures.incRequests(3500, 1);
		return failures;
	}

	private RequestCounter createSuccessCounter() {
		RequestCounter successes = new RequestCounter("successes", new TimeMeasurementStoreInMemory());
		successes.incRequests(1000, 20);
		successes.incRequests(2000, 20);
		successes.incRequests(3000, 20);
		successes.incRequests(3100, 20);
		return successes;
	}

}