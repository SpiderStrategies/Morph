/*
 * Copyright 2004-2005, 2007 the original author or authors.
 * 
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

/**
 * A map where all keys and values are of type {@link java.lang.Class}. This is
 * accomplished by making the {@link #put(Object, Object)}method convert the
 * key and value supplied to it to a <code>Class</code> object using
 * {@link net.sf.morph2.util.ClassUtils#newInstance(Object)}. This class is
 * included to ease the configuration of Morph. One example of where it is used
 * is in
 * {@link net.sf.morph2.reflect.reflectors.SimpleInstantiatingReflector#setRequestedToInstantiatedTypeMap(Map)}.
 * 
 * @author Matt Sgarlata
 * @since Apr 14, 2005
 */
public class TypeMap extends HashMap {

	/**
	 * Create a new TypeMap.
	 */
	public TypeMap() {
		super();
	}

	/**
	 * Create a new TypeMap.
	 * @param initialCapacity
	 */
	public TypeMap(int initialCapacity) {
		super(initialCapacity);
	}

	/**
	 * Create a new TypeMap.
	 * @param initialCapacity
	 * @param loadFactor
	 */
	public TypeMap(int initialCapacity, float loadFactor) {
		super(initialCapacity, loadFactor);
	}

	/**
	 * Create a new TypeMap.
	 * @param map
	 */
	public TypeMap(Map map) {
		this(map.size());
		putAll(map);
	}

	/**
	 * {@inheritDoc}
	 */
	public Object put(Object key, Object value) {
		return super.put(key == null ? null : ClassUtils.convertToClass(key),
				value == null ? null : ClassUtils.convertToClass(value));
	}
	
}
