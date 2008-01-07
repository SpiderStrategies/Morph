/*
 * Copyright 2004-2005, 2008 the original author or authors.
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
package net.sf.morph.util;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Utility functions for working with container-like objects such as Collections
 * and arrays.
 *
 * @author Matt Sgarlata
 * @since Mar 11, 2005
 */
public class ContainerUtils extends net.sf.composite.util.ContainerUtils {

	/**
	 * Determines if the given <code>array</code> contains the given
	 * <code>value</code>.
	 *
	 * @param array
	 *            the array to test
	 * @param value
	 *            the value to test
	 * @return <code>true</code> if the given array contains the given value
	 *         or <br>
	 *         <code>false</code>, otherwise
	 */
	public static boolean contains(Object[] array, Object value) {
		if (array == null) {
			return false;
		}
		for (int i = 0; i < array.length; i++) {
			if (array[i] == value || (array[i] != null && array[i].equals(value))) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Determines if the given <code>collection</code> contains the given
	 * <code>value</code>.
	 *
	 * @param collection
	 *            the collection to test
	 * @param value
	 *            the value to test
	 * @return <code>true</code> if the given collection contains the given value
	 *         or <br>
	 *         <code>false</code>, otherwise
	 */
	public static boolean contains(Collection collection, Object value) {
		return collection != null && collection.contains(value);
	}

//	/**
//	 * Returns the union of multiple arrays as an array.  Implementation is O(n<sup>2</sup>).
//	 *
//	 * @param arrays
//	 *            a List of arrays
//	 * @param componentType
//	 *            the runtime type of the returned array
//	 * @return the union of the arrays
//	 * @deprecated this is silly, just use a HashSet instead
//	 */
//	public static Object[] union(List arrays, Class componentType) {
//		if (componentType == null) {
//			throw new IllegalArgumentException("componentType must be speciifed");
//		}
//		if (arrays == null) {
//			return null;
//		}
//
//		Set unionSet = new HashSet();
//		for (int i=0; i<arrays.size(); i++) {
//			if (!ObjectUtils.isEmpty(arrays.get(i))) {
//				unionSet.addAll(Arrays.asList( ((Object[]) arrays.get(i)) ));
//			}
//		}
//
//		Object[] unionArray = (Object[]) ClassUtils.createArray(componentType, unionSet.size());
//		return unionSet.toArray(unionArray);
//	}
//
//	/**
//	 * Returns the union of multiple arrays as an array.  Implementation is O(n<sup>2</sup>).
//	 *
//	 * @param arrays
//	 *            a List of arrays
//	 * @return the union of the arrays
//	 */
//	public static Object[] union(List arrays) {
//		return union(arrays, Object[].class);
//	}

	/**
	 * Returns the intersection of multiple arrays as an array.  Implementation is O(n<sup>3</sup>).
	 *
	 * @param arrays
	 *            a List of arrays
	 * @param componentType
	 *            the runtime type of the returned array
	 * @return the intersection of the arrays
	 */
	public static Object[] intersection(List arrays, Class componentType) {
		if (componentType == null) {
			throw new IllegalArgumentException("componentType must be speciifed");
		}
		if (arrays == null) {
			return null;
		}

		Set intersectionSet = new HashSet();
		intersectionSet.addAll(Arrays.asList( ((Object[]) arrays.get(0)) ));
		for (int i=1; i<arrays.size(); i++) {
			Object[] array = (Object[]) arrays.get(i);
			for (int j=0; j<array.length; j++) {
				if (!contains(intersectionSet, array[j])) {
					intersectionSet.remove(array[j]);
				}
			}
		}

		Object[] intersectionArray = (Object[]) ClassUtils.createArray(componentType, intersectionSet.size());
		return intersectionSet.toArray(intersectionArray);
	}

	/**
	 * Returns the intersection of multiple arrays as an array.  Implementation is O(n<sup>3</sup>).
	 *
	 * @param arrays
	 *            a List of arrays
	 * @return the intersection of the arrays
	 */
	public static Object[] intersection(List arrays) {
		return intersection(arrays, Object[].class);
	}
	
	public static Set createOrderedSet() {
		if (ClassUtils.isJdk14OrHigherPresent()) {
			try {
				return (Set) Class.forName("java.util.LinkedHashSet").newInstance();	
			}
			catch (Exception e) { }
		}
		
		if (ClassUtils.isCommonsCollectionsPresent()) {
			try {
				return (Set) Class.forName("org.apache.commons.collections.set.ListOrderedSet").newInstance();			
			}
			catch (Exception e) { }
		}
		
		return new ListOrderedSet();
	}

}