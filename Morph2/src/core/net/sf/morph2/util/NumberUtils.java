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
package net.sf.morph2.util;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import net.sf.composite.util.ObjectUtils;
import net.sf.morph2.reflect.ReflectionException;

/**
 * Various values and utility functions that are useful when working with
 * numbers.
 * 
 * @author Matt Sgarlata
 * @since Dec 15, 2004
 */
public abstract class NumberUtils {

	private static abstract class NumberFactory {
		static Class[] paramTypes = new Class[] { String.class };
		Number get(String s) {
			try {
				return (Number) iget(s);
			} catch (Exception e) {
				throw new ReflectionException(e);
			}
		}
		protected abstract Object iget(String s) throws Exception;
	}

	private static class MethodNumberFactory extends NumberFactory {
		private Method m;
		MethodNumberFactory(Class c) throws Exception {
			m = c.getMethod("valueOf", paramTypes);
		}
		protected Object iget(String s) throws Exception {
			return m.invoke(null, new Object[] { s });
		}
	}

	private static class ConstructorNumberFactory extends NumberFactory {
		private Constructor cs;
		ConstructorNumberFactory(Class c) throws Exception {
			cs = c.getConstructor(paramTypes);
		}
		protected Object iget(String s) throws Exception {
			return cs.newInstance(new Object[] { s });
		}
	}

	private static class NarrownessComparator implements Comparator {
		/**
		 * {@inheritDoc}
		 */
		public int compare(Object arg0, Object arg1) {
			if (arg0 == arg1) {
				return 0;
			}
			Class c0 = getType(arg0);
			Class c1 = getType(arg1);
			if (c0 == c1 || c0 == null || c1 == null) {
				return 0;
			}
			return getMaximumForType(c0).compareTo(getMaximumForType(c1));
		}

		private Class getType(Object o) {
			if (MAXIMUMS_FOR_TYPES.containsKey(o)) {
				return (Class) o;
			}
			Class test = ClassUtils.getClass(o);
			return MAXIMUMS_FOR_TYPES.containsKey(test) ? test : null;
		}
	}

	/**
	 * A Map of BigDecimals keyed by Class that indicate the maximum value that
	 * the given (Number) Class may taken on.
	 */
	public static final Map MAXIMUMS_FOR_TYPES;

	/**
	 * A Map of BigDecimals keyed by Class that indicate the minimum value that
	 * the given (Number) Class may taken on.
	 */
	public static final Map MINIMUMS_FOR_TYPES;

	/**
	 * Comparator of class/object type narrowness.
	 */
	public static final Comparator NARROWNESS_COMPARATOR = new NarrownessComparator();

	private static final Map WRAPPERS_FOR_PRIMITIVE_TYPES;

	private static final Map NUMBER_FACTORIES;

	/**
	 * Used by {@link NumberUtils#isNumber(Class)}.
	 */
	protected static final Class[] BASE_NUMBER_TYPES;
	
	/**
	 * The maximum value a long can have.
	 * 
	 * @see Long#MAX_VALUE
	 */
	public static final BigDecimal MAX_LONG = new BigDecimal("" + Long.MAX_VALUE);

	/**
	 * The maximum value an integer can have.
	 * 
	 * @see Integer#MAX_VALUE
	 */
	public static final BigDecimal MAX_INTEGER = new BigDecimal("" + Integer.MAX_VALUE);

	/**
	 * The maximum value a short can have.
	 * 
	 * @see Short#MAX_VALUE
	 */
	public static final BigDecimal MAX_SHORT = new BigDecimal("" + Short.MAX_VALUE);

	/**
	 * The maximum value a byte can have.
	 * 
	 * @see Byte#MAX_VALUE
	 */
	public static final BigDecimal MAX_BYTE = new BigDecimal("" + Byte.MAX_VALUE);

	/**
	 * The maximum value a double can have.
	 * 
	 * @see Double#MAX_VALUE
	 */
	public static final BigDecimal MAX_DOUBLE = new BigDecimal("" + Double.MAX_VALUE);

	/**
	 * The maximum value a float can have.
	 * 
	 * @see Float#MAX_VALUE
	 */
	public static final BigDecimal MAX_FLOAT = new BigDecimal("" + Float.MAX_VALUE);
	
	/**
	 * The minimum value a long can have.
	 * 
	 * @see Long#MIN_VALUE
	 */
	public static final BigDecimal MIN_LONG = new BigDecimal("" + Long.MIN_VALUE);

	/**
	 * The minimum value an integer can have.
	 * 
	 * @see Long#MIN_VALUE
	 */
	public static final BigDecimal MIN_INTEGER = new BigDecimal("" + Integer.MIN_VALUE);

	/**
	 * The minimum value a short can have.
	 * 
	 * @see Long#MIN_VALUE
	 */
	public static final BigDecimal MIN_SHORT = new BigDecimal("" + Short.MIN_VALUE);

	/**
	 * The minimum value a byte can have.
	 * 
	 * @see Long#MIN_VALUE
	 */
	public static final BigDecimal MIN_BYTE = new BigDecimal("" + Byte.MIN_VALUE);

	/**
	 * The minimum value a double can have.
	 * 
	 * @see Long#MIN_VALUE
	 */
	public static final BigDecimal MIN_DOUBLE = new BigDecimal("-" + Double.MAX_VALUE);

	/**
	 * The minimum value a float can have.
	 * 
	 * @see Long#MIN_VALUE
	 */
	public static final BigDecimal MIN_FLOAT = new BigDecimal("-" + Float.MAX_VALUE);

	/**
	 * A BigDecimal containing the value zero (0).
	 */
	public static final BigDecimal ZERO = new BigDecimal("0");

	static {
		// the .TYPE entries probably aren't needed, but they don't hurt
		// anything :)
		MAXIMUMS_FOR_TYPES = new HashMap();
		MAXIMUMS_FOR_TYPES.put(Long.class, MAX_LONG);
		MAXIMUMS_FOR_TYPES.put(Long.TYPE, MAX_LONG);
		MAXIMUMS_FOR_TYPES.put(Integer.class, MAX_INTEGER);
		MAXIMUMS_FOR_TYPES.put(Integer.TYPE, MAX_INTEGER);
		MAXIMUMS_FOR_TYPES.put(Short.class, MAX_SHORT);
		MAXIMUMS_FOR_TYPES.put(Short.TYPE, MAX_SHORT);
		MAXIMUMS_FOR_TYPES.put(Byte.class, MAX_BYTE);
		MAXIMUMS_FOR_TYPES.put(Byte.TYPE, MAX_BYTE);
		MAXIMUMS_FOR_TYPES.put(Double.class, MAX_DOUBLE);
		MAXIMUMS_FOR_TYPES.put(Double.TYPE, MAX_DOUBLE);
		MAXIMUMS_FOR_TYPES.put(Float.class, MAX_FLOAT);
		MAXIMUMS_FOR_TYPES.put(Float.TYPE, MAX_FLOAT);

		// the .TYPE entries probably aren't needed, but they don't hurt
		// anything :)
		MINIMUMS_FOR_TYPES = new HashMap();
		MINIMUMS_FOR_TYPES.put(Long.class, MIN_LONG);
		MINIMUMS_FOR_TYPES.put(Long.TYPE, MIN_LONG);
		MINIMUMS_FOR_TYPES.put(Integer.class, MIN_INTEGER);
		MINIMUMS_FOR_TYPES.put(Integer.TYPE, MIN_INTEGER);
		MINIMUMS_FOR_TYPES.put(Short.class, MIN_SHORT);
		MINIMUMS_FOR_TYPES.put(Short.TYPE, MIN_SHORT);
		MINIMUMS_FOR_TYPES.put(Byte.class, MIN_BYTE);
		MINIMUMS_FOR_TYPES.put(Byte.TYPE, MIN_BYTE);
		MINIMUMS_FOR_TYPES.put(Double.class, MIN_DOUBLE);
		MINIMUMS_FOR_TYPES.put(Double.TYPE, MIN_DOUBLE);
		MINIMUMS_FOR_TYPES.put(Float.class, MIN_FLOAT);
		MINIMUMS_FOR_TYPES.put(Float.TYPE, MIN_FLOAT);

		WRAPPERS_FOR_PRIMITIVE_TYPES = new HashMap();
		WRAPPERS_FOR_PRIMITIVE_TYPES.put(Long.TYPE, Long.class);
		WRAPPERS_FOR_PRIMITIVE_TYPES.put(Integer.TYPE, Integer.class);
		WRAPPERS_FOR_PRIMITIVE_TYPES.put(Short.TYPE, Short.class);
		WRAPPERS_FOR_PRIMITIVE_TYPES.put(Byte.TYPE, Byte.class);
		WRAPPERS_FOR_PRIMITIVE_TYPES.put(Double.TYPE, Double.class);
		WRAPPERS_FOR_PRIMITIVE_TYPES.put(Float.TYPE, Float.class);

		// not sure if this is valid, but putting it in for now
		WRAPPERS_FOR_PRIMITIVE_TYPES.put(Void.TYPE, Void.class);

		NUMBER_FACTORIES = new HashMap();
		try {
			for (Iterator it = WRAPPERS_FOR_PRIMITIVE_TYPES.entrySet().iterator(); it.hasNext();) {
				Map.Entry e = (Map.Entry) it.next();
				Class c = (Class) e.getValue();
				if (c == Void.class) {
					continue;
				}
				NumberFactory nf = new MethodNumberFactory(c);
				NUMBER_FACTORIES.put(c, nf);
				NUMBER_FACTORIES.put(e.getKey(), nf);
			}
			NUMBER_FACTORIES.put(BigInteger.class, new ConstructorNumberFactory(BigInteger.class));
			NUMBER_FACTORIES.put(BigDecimal.class, new ConstructorNumberFactory(BigDecimal.class));
		} catch (Exception e) {
			throw new ReflectionException(e);
		}

		Set baseNumberTypes = ContainerUtils.createOrderedSet();
		baseNumberTypes.addAll(MAXIMUMS_FOR_TYPES.keySet());
		baseNumberTypes.add(Number.class);
		BASE_NUMBER_TYPES = (Class[]) baseNumberTypes.toArray(new Class[baseNumberTypes.size()]);
	}

	/**
	 * Returns the maximum allowed value for the given type, which must be a
	 * number.
	 * 
	 * @param type
	 *            the type
	 * @return the maximum allowed value for the given type, if
	 *         <code>type</code> is a number or <br>
	 *         <code>null</code>, otherwise
	 */
	public static BigDecimal getMaximumForType(Class type) {
		return (BigDecimal) MAXIMUMS_FOR_TYPES.get(type);
	}

	/**
	 * Returns the minimum allowed value for the given type, which must be a
	 * number.
	 * 
	 * @param type
	 *            the type
	 * @return the minimum allowed value for the given type, if
	 *         <code>type</code> is a number or <br>
	 *         <code>null</code>, otherwise
	 */
	public static BigDecimal getMinimumForType(Class type) {
		return (BigDecimal) MINIMUMS_FOR_TYPES.get(type);
	}

	/**
	 * Converts the given number to a BigDecimal.
	 * 
	 * @param number
	 *            the number to convert
	 * @return <code>null</code>, if number is <code>null</code> or <br>
	 *         the given number as a BigDecimal, otherwise
	 */
	public static BigDecimal numberToBigDecimal(Number number) {
		return number == null ? null : new BigDecimal(number.toString());
	}

	/**
	 * Determines if the given type is a number type. A number type is any type
	 * that is a subclass of <code>java.lang.Number</code> or is a primitive
	 * number type.
	 * 
	 * @param type
	 *            the type to test
	 * @return <code>true</code> if the given type is a number type or
	 *         <code>false</code>, otherwise
	 */
	public static boolean isNumber(Class type) {
		return ClassUtils.inheritanceContains(BASE_NUMBER_TYPES, type);
	}

	/**
	 * Returns <code>true</code> if <code>number</code> is capable of
	 * containing a decimal (fractional) component. This method returns
	 * <code>true</code> for Floats, Doubles, and Longs.
	 * 
	 * @param number
	 *            the number to test
	 * @return <code>false</code>, if <code>number</code> is
	 *         <code>null</code> or <br>
	 *         <code>true</code> if <code>number</code> is capable of
	 *         containing a decimal (fractional) component or <br>
	 *         <code>false</code>, otherwise.
	 */
	public static boolean isDecimal(Number number) {
		return number != null && (number instanceof Float || number instanceof Double || number instanceof BigDecimal);
	}

	/**
	 * Implementation of isTooBigForType and isTooSmallForType
	 */
	protected static boolean isOutOfBoundsForType(Map boundMap, Number number,
		Class type, int badCompareToResult) {
		if (number == null || type == BigInteger.class || type == BigDecimal.class) {
			return false;
		}
		BigDecimal boundForType = (BigDecimal) boundMap.get(type);
		// if the comparison equals the bad compare to result, return true
		// to indicate that the number is indeed out of bounds
		if (boundForType == null) {
			throw new IllegalArgumentException("Unable to determine bounds for type "
				+ ObjectUtils.getObjectDescription(type));
		}
		return NumberUtils.numberToBigDecimal(number).compareTo(boundForType) == badCompareToResult;
	}

	/**
	 * Returns <code>true</code> if <code>number</code> represents a value
	 * too large to be stored in an instance of the given <code>type</code>.
	 * 
	 * @param number
	 *            the number to test
	 * @param the
	 *            type to test
	 * @return <code>true</code>, if <code>number</code> represents a value
	 *         too large to be stored in an instance of the given
	 *         <code>type</code> or <br>
	 *         <code>false</code>, otherwise
	 * @throws IllegalArgumentException
	 *             if the maximum value cannot be determined for the given type
	 */
	public static boolean isTooBigForType(Number number, Class type) {
		return isOutOfBoundsForType(MAXIMUMS_FOR_TYPES, number, type, 1);
	}

	/**
	 * Returns <code>true</code> if <code>number</code> represents a value
	 * too small (i.e. - with too large a negative absolute value) to be stored
	 * in an instance of the given <code>type</code>.
	 * 
	 * @param number
	 *            the number to test
	 * @param the
	 *            type to test
	 * @return <code>true</code>, if <code>number</code> represents a value
	 *         too small (i.e. - with too large a negative absolute value) to be
	 *         stored in an instance of the given <code>type</code> or <br>
	 *         <code>false</code>, otherwise
	 * @throws IllegalArgumentException
	 *             if the minimum value cannot be determined for the given type
	 */
	public static boolean isTooSmallForType(Number number, Class type) {
		return isOutOfBoundsForType(MINIMUMS_FOR_TYPES, number, type, -1);
	}

	/**
	 * Returns <code>true</code> if <code>number</code> has too large an
	 * absolute value to be stored in an instance of the given <code>type</code>.
	 * 
	 * @param number
	 *            the number to test
	 * @param the
	 *            type to test
	 * @return <code>true</code>, if <code>number</code> has too large an
	 *         absolute value to be stored in an instance of the given
	 *         <code>type</code> or <br>
	 *         <code>false</code>, otherwise
	 */
	public static boolean isOutOfBoundsForType(Number number, Class type) {
		return
			number != null &&
			(isTooBigForType(number, type) ||
			isTooSmallForType(number, type));
	}

	/**
	 * @deprecated for {@link ClassUtils#getPrimitiveWrapper(Class)}
	 * @param type
	 * @return Class
	 */
	public static Class getWrapperForPrimitiveType(Class type) {
		return (Class) WRAPPERS_FOR_PRIMITIVE_TYPES.get(type);
	}

	/**
	 * Get a number from a String.
	 * @param type
	 * @param s
	 * @return Number
	 * @throws Exception
	 */
	public static Number getNumber(Class type, String s) throws Exception {
		return ((NumberFactory) NUMBER_FACTORIES.get(type)).get(s);
	}

}
