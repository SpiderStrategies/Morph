/*
 * Copyright 2004-2005 the original author or authors.
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

import java.lang.reflect.Array;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;

import junit.framework.TestCase;
import net.sf.composite.util.ObjectUtils;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * @author Matt Sgarlata
 * @since Nov 7, 2004
 */
public class TestUtils {

	private static final Log log = LogFactory.getLog(TestUtils.class);
	
	public static boolean contains(Object[] array, Object object) {
		if (object == null || array == null) {
			return false;
		}
		else {
			for (int i=0; i<array.length; i++) {
				if (equals(array[i], object)) {
					return true;
				}
			}
			return false;
		}
	}
	
	
	public static boolean contains(Collection collection, Object value) {
		if (collection == null) {
			return false;
		}
		else {
			return contains(collection.toArray(), value);
		}			
	}

	public static boolean equals(Object object1, Object object2) {
		if (log.isTraceEnabled()) {
			log.trace("Testing for equality between "
				+ ObjectUtils.getObjectDescription(object1) + " and "
				+ ObjectUtils.getObjectDescription(object2));
		}
		// if both objects are null they are equal
		if (object1 == null && object2 == null) {
			return true;
		}
		// if one object is null and the other is not, the two objects aren't
		// equal
		else if (object1 == null || object2 == null) {
			return false;
		}
		else if (object1 instanceof Calendar && object2 instanceof Calendar) {
			return equals(((Calendar) object1).getTime(), ((Calendar) object2).getTime());
//			Date date1 = (Date) object1;
//			Date date2 = (Date) object2;
//			return date1.getTime() == date2.getTime();
		}
		else if (object1 instanceof Comparable && object2 instanceof Comparable &&
			object1.getClass().equals(object2.getClass())) {
			Comparable comparable1 = (Comparable) object1;
			Comparable comparable2 = (Comparable) object2;
			return comparable1.compareTo(comparable2) == 0;
		}
		else if (object1 instanceof Map.Entry && object2 instanceof Map.Entry) {
			if (!object1.getClass().equals(object2.getClass())) {
				return false;
			}
			else {
				Map.Entry me1 = (Map.Entry) object1;
				Map.Entry me2 = (Map.Entry) object2;
				if (equals(me1.getKey(), me2.getKey()) &&
					equals(me1.getValue(), me2.getValue())) {
					return true;
				}
				else {
					return false;
				}
			}
		}
		// if both objets are arrays
		else if (object1.getClass().isArray() && object2.getClass().isArray()) {
			// if the arrays aren't of the same class, the two objects aren't
			// equal
			if (!object1.getClass().equals(object2.getClass())) {
				return false;
			}
			else { // same type of array
				// if the arrays are different sizes, they aren't equal
				if (Array.getLength(object1) != Array.getLength(object2)) {
					return false;
				}
				else { // arrays are the same size
					// iterate through the arrays and check if all elements are
					// equal
					for (int i=0; i<Array.getLength(object1); i++) {
						// if both of the items we are examining are null
						if (Array.get(object1, i) == null && Array.get(object2, i) == null) {
							// keep looping through the array (do nothing here)
						}
						// if one item is null and the other isn't
						else if (Array.get(object1, i) == null || Array.get(object2, i) == null) {
							// the arrays aren't equal							
							return false;
						}
						// if one item isn't equal to the other
						else if (!TestUtils.equals(Array.get(object1, i), Array.get(object2, i))) {
							// the arrays aren't equal
							return false;
						}
					}
					// if we iterated through both arrays and found no items
					// that weren't equal to each other, the collections are
					// equal
					return true;
				}
			}
		}
		else if (object1 instanceof Set && object2 instanceof Set) {
			// if the sets aren't of the same type, they aren't equal
			if (!object1.getClass().equals(object2.getClass())) {
				return false;
			}
			else { // same type of set
				Set set1 = (Set) object1;
				Set set2 = (Set) object2;
				// if the sets aren't the same size, they aren't equal
				if (set1.size() != set2.size()) {
					return false;
				}
				else { // sets are the same size
					// if both the sets are empty, they are equal
					if (set1.isEmpty() && set2.isEmpty()) {
						return true;
					}
					else { // sets aren't empty
						Iterator iterator1 = set1.iterator();
						while (iterator1.hasNext()) {
							if (!contains(set2, iterator1.next())) {
								return false;
							}
						}
						return true;
					}
				}
			}
		}
		else if (object1 instanceof Iterator && object2 instanceof Iterator) {
			Iterator iterator1 = (Iterator) object1;
			Iterator iterator2 = (Iterator) object2;
			while (iterator1.hasNext()) {
				Object item1 = iterator1.next();
				Object item2 = iterator2.next();
				// if both of the items we are examining are null
				if (item1 == null && item2 == null) {
					// keep looping through the array (do nothing here)
				}
				// if one item is null and the other isn't
				else if (item1 == null || item2 == null) {
					// the arrays aren't equal							
					return false;
				}
				// if one item isn't equal to the other
				else if (!equals(item1, item2)) {
					// the arrays aren't equal
					return false;
				}
			}
			// if we iterated through both collections and found
			// no items that weren't equal to each other, the
			// collections are equal
			return true;
		}
		else if (object1 instanceof Enumeration && object2 instanceof Enumeration) {
			Enumeration e1 = (Enumeration) object1;
			Enumeration e2 = (Enumeration) object2;
			return equals(new EnumerationIterator(e1), new EnumerationIterator(e2));
		}
		else if ((object1 instanceof List && object2 instanceof List) ||
			(object1 instanceof SortedSet && object2 instanceof SortedSet)) {
			// if the collections aren't of the same type, they aren't equal
			if (!object1.getClass().equals(object2.getClass())) {
				return false;
			}
			else { // same type of collection
				Collection collection1 = (Collection) object1;
				Collection collection2 = (Collection) object2;
				// if the collections aren't the same size, they aren't equal
				if (collection1.size() != collection2.size()) {
					return false;
				}
				else { // collections are the same size
					// if both the collections are empty, they are equal
					if (collection1.isEmpty() && collection2.isEmpty()) {
						return true;
					}
					else { // collections aren't empty
						return equals(collection1.iterator(), collection2.iterator());
					}
				}
			}
		}
		else if (object1 instanceof Map && object2 instanceof Map) {
			return equals(((Map) object1).entrySet(), ((Map) object2).entrySet());
		}
		else if (object1.getClass().equals(object2.getClass()) &&
			object1 instanceof StringBuffer) {
			return object1.toString().equals(object2.toString());
		}
		// for primitives, use their equals methods
		else if (object1.getClass().equals(object2.getClass()) &&
			(object1 instanceof Date || object1 instanceof Calendar ||
			object1 instanceof String || object1 instanceof Number ||
			object1 instanceof Boolean || object1 instanceof StringBuffer ||
			object1 instanceof Character)) {
			return object1.equals(object2);
		}
		// for non-primitives, compare field-by-field
		else {
			return MorphEqualsBuilder.reflectionEquals(object1, object2);
		}
	}
	
	public static void assertEquals(Object expected, Object actual) {
		TestCase.assertTrue("Expected " + 
			ObjectUtils.getObjectDescription(expected) + " but was "
				+ ObjectUtils.getObjectDescription(actual), equals(expected,
				actual));
	}


	public static Object getInstance(Class type) throws InstantiationException, IllegalAccessException {
		if (type == null) {
			throw new IllegalArgumentException("Non-null type must be specified");
		}
		if (type.isPrimitive()) {
			if (Long.TYPE.equals(type)) {
				return new Long(0);
			}
			else if (Integer.TYPE.equals(type)) {
				return new Integer(0);
			}
			else if (type.equals(Short.TYPE)) {
				return new Short("0");
			}
			else if (type.equals(Character.TYPE)) {
				return new Character('0');
			}
			else if (type.equals(Byte.TYPE)) {
				return new Byte("0");
			}
			else if (type.equals(Double.TYPE)) {
				return new Double(0);
			}
			else if (type.equals(Float.TYPE)) {
				return new Float(0);
			}
			else if (type.equals(Boolean.TYPE)) {
				return Boolean.FALSE;
			}
			else { // shouldn't ever make it here
				throw new IllegalStateException("The supplied type, '" + ObjectUtils.getObjectDescription(type) + "' is not one of the primitive types defined as of J2SE 1.5");
			}
		}
		else {
			return ClassUtils.newInstance(type);
		}
	}

}