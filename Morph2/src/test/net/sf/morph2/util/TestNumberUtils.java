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

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Matt Sgarlata
 * @since Dec 15, 2004
 */
public class TestNumberUtils extends NumberUtils {
	
	/**
	 * A Map of BigDecimals keyed by Class that indicate the maximum value that
	 * the given (Number) Class may taken on.
	 */
	public static final Map TOO_BIG_FOR_TYPE;
	/**
	 * A Map of BigDecimals keyed by Class that indicate the minimum value that
	 * the given (Number) Class may taken on.
	 */
	public static final Map TOO_SMALL_FOR_TYPE;

	public static final BigDecimal ONE = new BigDecimal(1);
	
	/**
	 * One plus the maximum value a long can have.
	 * 
	 * @see Long#MAX_VALUE
	 */
	public static final BigDecimal MAX_LONG_PLUS_ONE = MAX_LONG.add(ONE);
	/**
	 * One plus the maximum value an integer can have.
	 * 
	 * @see Integer#MAX_VALUE
	 */
	public static final BigDecimal MAX_INTEGER_PLUS_ONE = MAX_INTEGER.add(ONE);
	/**
	 * One plus the maximum value a short can have.
	 * 
	 * @see Short#MAX_VALUE
	 */
	public static final BigDecimal MAX_SHORT_PLUS_ONE = MAX_SHORT.add(ONE);
	/**
	 * One plus the maximum value a byte can have.
	 * 
	 * @see Byte#MAX_VALUE
	 */
	public static final BigDecimal MAX_BYTE_PLUS_ONE = MAX_BYTE.add(ONE);
	/**
	 * One plus the maximum value a double can have.
	 * 
	 * @see Double#MAX_VALUE
	 */
	public static final BigDecimal MAX_DOUBLE_PLUS_ONE = MAX_DOUBLE.add(ONE);
	/**
	 * One plus the maximum value a float can have.
	 * 
	 * @see Float#MAX_VALUE
	 */
	public static final BigDecimal MAX_FLOAT_PLUS_ONE = MAX_FLOAT.add(ONE);

	/**
	 * The minimum value a long can have minus one.
	 * 
	 * @see Long#MIN_VALUE
	 */
	public static final BigDecimal MIN_LONG_MINUS_ONE = MIN_LONG.subtract(ONE);
	/**
	 * The minimum value an integer can have minus one.
	 * 
	 * @see Long#MIN_VALUE
	 */
	public static final BigDecimal MIN_INTEGER_MINUS_ONE = MIN_INTEGER.subtract(ONE);
	/**
	 * The minimum value a short can have minus one.
	 * 
	 * @see Long#MIN_VALUE
	 */
	public static final BigDecimal MIN_SHORT_MINUS_ONE = MIN_SHORT.subtract(ONE);
	/**
	 * The minimum value a byte can have minus one.
	 * 
	 * @see Long#MIN_VALUE
	 */
	public static final BigDecimal MIN_BYTE_MINUS_ONE = MIN_BYTE.subtract(ONE);
	/**
	 * The minimum value a double can have minus one.
	 * 
	 * @see Long#MIN_VALUE
	 */
	public static final BigDecimal MIN_DOUBLE_MINUS_ONE = MIN_DOUBLE.subtract(ONE);
	/**
	 * The minimum value a float can have minus one.
	 * 
	 * @see Long#MIN_VALUE
	 */
	public static final BigDecimal MIN_FLOAT_MINUS_ONE = MIN_FLOAT.subtract(ONE);
	
	static {
		// the .TYPE entries probably aren't needed, but they don't hurt
		// anything :)
		TOO_BIG_FOR_TYPE = new HashMap();
		TOO_BIG_FOR_TYPE.put(Long.class, MAX_LONG_PLUS_ONE);
		TOO_BIG_FOR_TYPE.put(Long.TYPE, MAX_LONG_PLUS_ONE);
		TOO_BIG_FOR_TYPE.put(Integer.class, MAX_INTEGER_PLUS_ONE);
		TOO_BIG_FOR_TYPE.put(Integer.TYPE, MAX_INTEGER_PLUS_ONE);
		TOO_BIG_FOR_TYPE.put(Short.class, MAX_SHORT_PLUS_ONE);
		TOO_BIG_FOR_TYPE.put(Short.TYPE, MAX_SHORT_PLUS_ONE);
		TOO_BIG_FOR_TYPE.put(Byte.class, MAX_BYTE_PLUS_ONE);
		TOO_BIG_FOR_TYPE.put(Byte.TYPE, MAX_BYTE_PLUS_ONE);
		TOO_BIG_FOR_TYPE.put(Double.class, MAX_DOUBLE_PLUS_ONE);
		TOO_BIG_FOR_TYPE.put(Double.TYPE, MAX_DOUBLE_PLUS_ONE);
		TOO_BIG_FOR_TYPE.put(Float.class, MAX_FLOAT_PLUS_ONE);
		TOO_BIG_FOR_TYPE.put(Float.TYPE, MAX_FLOAT_PLUS_ONE);
		
		// the .TYPE entries probably aren't needed, but they don't hurt
		// anything :)
		TOO_SMALL_FOR_TYPE = new HashMap();
		TOO_SMALL_FOR_TYPE.put(Long.class, MIN_LONG_MINUS_ONE);
		TOO_SMALL_FOR_TYPE.put(Long.TYPE, MIN_LONG_MINUS_ONE);
		TOO_SMALL_FOR_TYPE.put(Integer.class, MIN_INTEGER_MINUS_ONE);
		TOO_SMALL_FOR_TYPE.put(Integer.TYPE, MIN_INTEGER_MINUS_ONE);
		TOO_SMALL_FOR_TYPE.put(Short.class, MIN_SHORT_MINUS_ONE);
		TOO_SMALL_FOR_TYPE.put(Short.TYPE, MIN_SHORT_MINUS_ONE);
		TOO_SMALL_FOR_TYPE.put(Byte.class, MIN_BYTE_MINUS_ONE);
		TOO_SMALL_FOR_TYPE.put(Byte.TYPE, MIN_BYTE_MINUS_ONE);
		TOO_SMALL_FOR_TYPE.put(Double.class, MIN_DOUBLE_MINUS_ONE);
		TOO_SMALL_FOR_TYPE.put(Double.TYPE, MIN_DOUBLE_MINUS_ONE);
		TOO_SMALL_FOR_TYPE.put(Float.class, MIN_FLOAT_MINUS_ONE);
		TOO_SMALL_FOR_TYPE.put(Float.TYPE, MIN_FLOAT_MINUS_ONE);
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
	public static BigDecimal getTooBigForType(Class type) {
		return (BigDecimal) TOO_BIG_FOR_TYPE.get(type);
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
	public static BigDecimal getTooSmallForType(Class type) {
		return (BigDecimal) TOO_SMALL_FOR_TYPE.get(type);
	}
	

}
