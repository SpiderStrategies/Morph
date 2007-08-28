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
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

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
	private static final Class[] ONE_STRING = new Class[] { String.class };

	public static boolean contains(Object[] array, Object object) {
		for (int i=0; array != null && i<array.length; i++) {
			if (equals(array[i], object)) {
				return true;
			}
		}
		return false;
	}

	public static boolean contains(Collection collection, Object value) {
		return collection != null && contains(collection.toArray(), value);
	}

	public static boolean equals(Object object1, Object object2) {
		if (log.isTraceEnabled()) {
			log.trace("Testing for equality between "
				+ ObjectUtils.getObjectDescription(object1) + " and "
				+ ObjectUtils.getObjectDescription(object2));
		}
		// if both objects are == (incl. null) they are equal
		if (object1 == object2) {
			return true;
		}
		// if one object is null and the other is not, the two objects aren't
		// equal
		if (object1 == null || object2 == null) {
			return false;
		}
		if (object1 instanceof Calendar && object2 instanceof Calendar) {
			return equals(((Calendar) object1).getTime(), ((Calendar) object2).getTime());
		}
		if (object1 instanceof Comparable && object1.getClass() == object2.getClass()) {
			return ((Comparable) object1).compareTo(object2) == 0;
		}
		if (object1 instanceof Map.Entry && object2 instanceof Map.Entry) {
			if (object1.getClass() != object2.getClass()) {
				return false;
			}
			Map.Entry me1 = (Map.Entry) object1;
			Map.Entry me2 = (Map.Entry) object2;
			return equals(me1.getKey(), me2.getKey())
					&& equals(me1.getValue(), me2.getValue());
		}
		// if both objects are arrays
		if (object1.getClass().isArray() && object2.getClass().isArray()) {
			// if the arrays aren't of the same class, the two objects aren't
			// equal
			if (object1.getClass() != object2.getClass()) {
				return false;
			}
			// else, same type of array
			// if the arrays are different sizes, they aren't equal
			if (Array.getLength(object1) != Array.getLength(object2)) {
				return false;
			}
			// else arrays are the same size
			// iterate through the arrays and check if all elements are
			// equal
			for (int i=0; i<Array.getLength(object1); i++) {
				// if one item isn't equal to the other
				if (!equals(Array.get(object1, i), Array.get(object2, i))) {
					// the arrays aren't equal
					return false;
				}
			}
			// if we iterated through both arrays and found no items
			// that weren't equal to each other, the collections are
			// equal
			return true;
		}
		if (object1 instanceof Iterator && object2 instanceof Iterator) {
			Iterator iterator1 = (Iterator) object1;
			Iterator iterator2 = (Iterator) object2;
			while (iterator1.hasNext()) {
				if (!iterator2.hasNext()) {
					return false;
				}
				// if one item isn't equal to the other
				if (!equals(iterator1.next(), iterator2.next())) {
					// the arrays aren't equal
					return false;
				}
			}
			// if we iterated through both collections and found
			// no items that weren't equal to each other, the
			// collections are equal
			return !iterator2.hasNext();
		}
		if (object1 instanceof Enumeration && object2 instanceof Enumeration) {
			return equals(new EnumerationIterator((Enumeration) object1),
					new EnumerationIterator((Enumeration) object2));
		}
		if ((object1 instanceof List && object2 instanceof List) ||
			(object1 instanceof SortedSet && object2 instanceof SortedSet)) {
			// if the collections aren't of the same type, they aren't equal
			if (object1.getClass() != object2.getClass()) {
				return false;
			}
			// else same type of collection
			return equals(((Collection) object1).iterator(), ((Collection) object2).iterator());
		}
		if (object1 instanceof Set && object2 instanceof Set) {
			// if the sets aren't of the same type, they aren't equal
			if (object1.getClass() != object2.getClass()) {
				return false;
			}
			// else same type of set
			Set set1 = (Set) object1;
			Set set2 = (Set) object2;
			// if the sets aren't the same size, they aren't equal
			if (set1.size() != set2.size()) {
				return false;
			}
			// else sets are the same size
			Iterator iterator1 = set1.iterator();
			while (iterator1.hasNext()) {
				Object next = iterator1.next();
				if (!contains(set2, next)) {
					return false;
				}
			}
			return true;
		}
		if (object1 instanceof Map && object2 instanceof Map) {
			return equals(((Map) object1).entrySet(), ((Map) object2).entrySet());
		}
		if (object1.getClass() == object2.getClass() && object1 instanceof StringBuffer) {
			return object1.toString().equals(object2.toString());
		}
		// for primitives, use their equals methods
		if (object1.getClass() == object2.getClass() &&
			(object1 instanceof String || object1 instanceof Number ||
			object1 instanceof Boolean || //object1 instanceof StringBuffer ||
			object1 instanceof Character)) {
			return object1.equals(object2);
		}
		// for non-primitives, compare field-by-field
		return MorphEqualsBuilder.reflectionEquals(object1, object2);
	}

	public static void assertEquals(Object expected, Object actual) {
		TestCase.assertTrue("Expected " + 
			ObjectUtils.getObjectDescription(expected) + " but was "
				+ ObjectUtils.getObjectDescription(actual), equals(expected,
				actual));
	}

	public static Object getInstance(Class type) throws InstantiationException, IllegalAccessException {
		return getDifferentInstance(type, null);
	}

	/**
	 * Return a "not-same" instance.
	 * @param type
	 * @param o
	 * @return
	 */
	public static Object getDifferentInstance(Class type, Object o) {
		if (type == null) {
			throw new IllegalArgumentException("Non-null type must be specified");
		}
		if (type.isPrimitive()) {
			type = ClassUtils.getPrimitiveWrapper(type);
		}
		if (o != null && !type.isInstance(o)) {
			throw new IllegalArgumentException("Negative example object should be of type " + type);
		}
		if (type == Number.class) {
			type = Integer.class;
		}
		if (Number.class.isAssignableFrom(type)) {
			byte b = (byte) (o == null ? 0 : ((Number) o).byteValue() + 1);
			try {
				return type.getConstructor(ONE_STRING).newInstance(new Object[] { Byte.toString(b) });
			} catch (Exception e) {
				throw e instanceof RuntimeException ? (RuntimeException) e : new NestableRuntimeException(e);
			}
		}
		if (type == Character.class) {
			char c = (char) (o == null ? 0 : ((Character) o).charValue() + 1);
			return new Character(c);
		}
		if (type == Boolean.class) {
			return o != null && ((Boolean) o).booleanValue() ? Boolean.FALSE : Boolean.TRUE;
		}
		if (type.isArray()) {
			return Array.newInstance(type.getComponentType(), 0);
		}
		if (type == Class.class) {
			return o == Object.class ? Class.class : Object.class;
		}
		return ClassUtils.newInstance(convertCommonInterfaces(type));
	}

	private static Class convertCommonInterfaces(Class type) {
		return type == Map.class ? HashMap.class : type == Set.class ? HashSet.class
				: type == SortedSet.class ? TreeSet.class : Collection.class
						.isAssignableFrom(type) ? ArrayList.class : type;
	}
}