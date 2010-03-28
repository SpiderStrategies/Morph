/*
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package net.sf.morph2.util;

import java.util.HashMap;
import java.util.Map;

import net.sf.composite.util.ObjectUtils;

/**
 * @author Matt Sgarlata
 * @since Jan 16, 2005
 */
public class BidirectionalMap extends HashMap {

	private final Map reverseMap;

	/**
	 * Get a {@link BidirectionalMap} instance.
	 * @param m
	 * @return {@link BidirectionalMap}
	 */
	public static BidirectionalMap getInstance(Map m) {
		return m instanceof BidirectionalMap ? (BidirectionalMap) m : new BidirectionalMap(m);
	}

	/**
	 * Create a new BidirectionalMap instance.
	 */
	public BidirectionalMap() {
		super();
		reverseMap = new HashMap();
	}

	/**
	 * Create a new BidirectionalMap instance.
	 * @param initialCapacity
	 */
	public BidirectionalMap(int initialCapacity) {
		super(initialCapacity);
		reverseMap = new HashMap(initialCapacity);
	}

	/**
	 * Create a new BidirectionalMap instance.
	 * @param initialCapacity
	 * @param loadFactor
	 */
	public BidirectionalMap(int initialCapacity, float loadFactor) {
		super(initialCapacity, loadFactor);
		reverseMap = new HashMap(initialCapacity, loadFactor);
	}

	/**
	 * Create a new BidirectionalMap instance.
	 * @param m
	 */
	public BidirectionalMap(Map m) {
		this(m.size());
		putAll(m);
	}

	/**
	 * Retrieves the key that is registered for the given entry
	 */
	public Object getKey(Object entry) {
		return reverseMap.get(entry);
	}

	/**
	 * {@inheritDoc}
	 */
	public void clear() {
		super.clear();
		reverseMap.clear();
	}

	/**
	 * {@inheritDoc}
	 */
	public Object clone() {
		HashMap clone = (HashMap) super.clone();
		return new BidirectionalMap(clone);
	}

	/**
	 * {@inheritDoc}
	 */
	public Object put(Object key, Object value) {
		if (reverseMap.containsKey(value)) {
			throw new IllegalArgumentException("The value '"
					+ ObjectUtils.getObjectDescription(value)
					+ "' has already been added to the map");
		}
		reverseMap.put(value, key);
		return super.put(key, value);
	}

	/**
	 * {@inheritDoc}
	 */
	public Object remove(Object key) {
		Object value = get(key);
		reverseMap.remove(value);
		return super.remove(key);
	}

	/**
	 * Get the reverse Map.
	 * @return Map
	 */
	public Map getReverseMap() {
		return reverseMap;
	}
}
