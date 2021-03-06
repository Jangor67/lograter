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
package nl.stokpop.lograter.store;

import net.jcip.annotations.NotThreadSafe;
import nl.stokpop.lograter.counter.RequestCounter;
import nl.stokpop.lograter.util.time.TimePeriod;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

@NotThreadSafe
public class RequestCounterStoreExternalSort implements RequestCounterStore {

	private static final int BUFFER_SIZE = 100000;
	private final Map<String, RequestCounter> counters = new HashMap<>();
	private final String name;
	private final TimePeriod timePeriod;
	private RequestCounter totalRequestCounter;
	private File rootStorageDir;

	/* package private */ RequestCounterStoreExternalSort(File rootStorageDir, String storeName, String totalRequestCounterName, TimePeriod timePeriod) {
		this.name = storeName;
		this.rootStorageDir = rootStorageDir;
		this.totalRequestCounter = new RequestCounter(totalRequestCounterName, new TimeMeasurementStoreToFiles(this.rootStorageDir, this.name, totalRequestCounterName, BUFFER_SIZE));
		this.timePeriod = timePeriod;
	}
	
	public void add(String counterKey, long logTimestamp, int durationInMilliseconds) {
		RequestCounter counter = addEmptyRequestCounterIfNotExists(counterKey);
		counter.incRequests(logTimestamp, durationInMilliseconds);
		totalRequestCounter.incRequests(logTimestamp, durationInMilliseconds);
	}

	@Override
	public String toString() {
		return "RequestCounterStoreHashMap{" +
				"name='" + name + '\'' +
				", timePeriod=" + timePeriod +
				'}';
	}

	public Iterator<RequestCounter> iterator() {
		List<RequestCounter> values = new ArrayList<>(counters.values());
		Collections.sort(values);
		return values.iterator();
	}
	
	@Override
	public boolean isEmpty() {
		return counters.isEmpty();
	}

	@Override
	public RequestCounter addEmptyRequestCounterIfNotExists(String counterKey) {
		if (!counters.containsKey(counterKey)) {
			RequestCounter counter = new RequestCounter(counterKey, new TimeMeasurementStoreToFiles(rootStorageDir, name, counterKey, BUFFER_SIZE));
			counters.put(counterKey, counter);
			return counter;
		}
		else {
			return counters.get(counterKey);
		}
	}

	@Override
	public RequestCounter get(String counterKey) {
		return counters.get(counterKey);
	}

	@Override
	public boolean contains(String counterKey) {
		return counters.containsKey(counterKey);
	}

	@Override
	public String getName() {
		return name;
	}

    @Override
    public List<String> getCounterKeys() {
        return new ArrayList<>(counters.keySet());
    }

	@Override
	public RequestCounter getTotalRequestCounter() {
		return totalRequestCounter;
	}

	@Override
	public boolean equals(Object o) {
		throw new UnsupportedOperationException();
	}

	@Override
	public int hashCode() {
		throw new UnsupportedOperationException();
	}

}
