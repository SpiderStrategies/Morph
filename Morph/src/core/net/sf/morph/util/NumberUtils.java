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

import java.lang.reflect.Constructor;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import net.sf.composite.util.ObjectUtils;

/**
 * Various values and utility functions that are useful when working with
 * numbers.
 * 
 * @author Matt Sgarlata
 * @since Dec 15, 2004
 */
public abstract class NumberUtils {
	
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
	
	public static final Map WRAPPERS_FOR_PRIMITIVE_TYPES;
	
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
		
		Set baseNumberTypes = new HashSet(MAXIMUMS_FOR_TYPES.keySet());
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
		if (number == null) {
			return null;
		}
		else {
			return new BigDecimal(number.toString());
		}
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
		if (number == null) {
			return false;
		}
		else if (number instanceof Float || number instanceof Double ||
			number instanceof BigDecimal) {
			return true;
		}
		else {
			return false;
		}
	}

	/**
	 * Implementation of isTooBigForType and isTooSmallForType
	 */
	protected static boolean isOutOfBoundsForType(Map boundMap, Number number,
		Class type, int badCompareToResult) {
		if (number == null || type.equals(BigInteger.class) || type.equals(BigDecimal.class)) {
			return false;
		}
		BigDecimal numberForComparison = NumberUtils.numberToBigDecimal(number);
		BigDecimal boundForType = (BigDecimal) boundMap.get(type);
		// if the comparison equals the bad compare to result, return true
		// to indicate that the number is indeed out of bounds
		if (boundForType == null) {
			throw new IllegalArgumentException("Unable to determine bounds for type "
				+ ObjectUtils.getObjectDescription(type));
		}
		else {
			return numberForComparison.compareTo(boundForType) == 
				badCompareToResult;
		}
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
		return isOutOfBoundsForType(MAXIMUMS_FOR_TYPES, number,
			type, 1);
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
		return isOutOfBoundsForType(MINIMUMS_FOR_TYPES, number,
			type, -1);
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
	
	public static Class getWrapperForPrimitiveType(Class type) {
		return (Class) WRAPPERS_FOR_PRIMITIVE_TYPES.get(type);
	}

	public static Number getNumber(Class type, String s) throws Exception {
		if (type.isPrimitive()) {
			type = getWrapperForPrimitiveType(type);
		}
		Constructor constructor =
			type.getConstructor(new Class[] { String.class });
		return (Number) constructor.newInstance(new Object[] { s });
	}


}
