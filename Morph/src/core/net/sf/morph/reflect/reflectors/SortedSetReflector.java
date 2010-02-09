/*
 * Copyright 2004-2005, 2010 the original author or authors.
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
package net.sf.morph.reflect.reflectors;

import java.util.Collection;
import java.util.Iterator;
import java.util.SortedSet;
import java.util.TreeSet;

import net.sf.morph.reflect.BeanReflector;
import net.sf.morph.reflect.IndexedContainerReflector;

/**
 * Exposes the information found in a {@link java.util.SortedSet}.
 * 
 * @author Matt Sgarlata
 * @since Nov 26, 2004
 */
public class SortedSetReflector extends SetReflector implements IndexedContainerReflector,
		BeanReflector {

	private static final Class[] REFLECTABLE_TYPES = new Class[] { SortedSet.class };

	/**
	 * {@inheritDoc}
	 */
	protected Object newInstanceImpl(Class interfaceClass, Object parameters) throws Exception {
		return interfaceClass == SortedSet.class ? new TreeSet() : super.newInstanceImpl(
				interfaceClass, parameters);
	}

	/**
	 * {@inheritDoc}
	 */
	public Class[] getReflectableClassesImpl() {
		return REFLECTABLE_TYPES;
	}

	/**
	 * {@inheritDoc}
	 */
	protected Object getImpl(Object container, int index) throws Exception {
		Collection c = getCollection(container);
		if (c == null || c.size() <= index) {
			throw new IndexOutOfBoundsException(Integer.toString(index));
		}
		Iterator it = c.iterator();
		for (int i = 0; i < index && it.hasNext(); i++) {
			it.next();
		}
		return it.next();
	}

}
