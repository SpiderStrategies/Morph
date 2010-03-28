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
package net.sf.morph2.transform.support;

import java.math.BigDecimal;

import net.sf.morph2.util.NumberUtils;

/**
 * @author Matt Sgarlata
 * @since Dec 15, 2004
 */
public abstract class NumberRounder {

	/**
	 * Rounding mode specified in {@link BigDecimal}.
	 * 
	 * @see BigDecimal#ROUND_CEILING
	 */
	public static final String ROUND_CEILING = "ROUND_CEILING";

	/**
	 * Rounding mode specified in {@link BigDecimal}.
	 * 
	 * @see BigDecimal#ROUND_DOWN
	 */
	public static final String ROUND_DOWN = "ROUND_DOWN";

	/**
	 * Rounding mode specified in {@link BigDecimal}.
	 * 
	 * @see BigDecimal#ROUND_FLOOR
	 */
	public static final String ROUND_FLOOR = "ROUND_FLOOR";

	/**
	 * Rounding mode specified in {@link BigDecimal}.
	 * 
	 * @see BigDecimal#ROUND_HALF_DOWN
	 */
	public static final String ROUND_HALF_DOWN = "ROUND_HALF_DOWN";

	/**
	 * Rounding mode specified in {@link BigDecimal}.
	 * 
	 * @see BigDecimal#ROUND_HALF_EVEN
	 */
	public static final String ROUND_HALF_EVEN = "ROUND_HALF_EVEN";

	/**
	 * Rounding mode specified in {@link BigDecimal}.
	 * 
	 * @see BigDecimal#ROUND_HALF_UP
	 */
	public static final String ROUND_HALF_UP = "ROUND_HALF_UP";

	/**
	 * Rounding mode specified in {@link BigDecimal}.
	 * 
	 * @see BigDecimal#ROUND_UP
	 */
	public static final String ROUND_UP = "ROUND_UP";

	/**
	 * Converts a rounding mode defined in this class to one of the rounding
	 * mode <code>int</code>s specified in the {@link BigDecimal} class.
	 * 
	 * @param mode
	 *            the String representation of the mode, as defined in one of
	 *            the constants of this class
	 * @return the int representation of the mode, as defined in the
	 *         {@link BigDecimal} class.
	 * @throws IllegalArgumentException
	 *             if an invalid rounding mode was specified
	 */
	public static int getBigDecimalRoundMode(String mode) throws IllegalArgumentException {
		if (ROUND_CEILING.equals(mode)) {
			return BigDecimal.ROUND_CEILING;
		}
		if (ROUND_DOWN.equals(mode)) {
			return BigDecimal.ROUND_DOWN;
		}
		if (ROUND_FLOOR.equals(mode)) {
			return BigDecimal.ROUND_FLOOR;
		}
		if (ROUND_HALF_DOWN.equals(mode)) {
			return BigDecimal.ROUND_HALF_DOWN;
		}
		if (ROUND_HALF_EVEN.equals(mode)) {
			return BigDecimal.ROUND_HALF_EVEN;
		}
		if (ROUND_HALF_UP.equals(mode)) {
			return BigDecimal.ROUND_HALF_UP;
		}
		if (ROUND_UP.equals(mode)) {
			return BigDecimal.ROUND_UP;
		}
		throw new IllegalArgumentException(
				"'"
						+ mode
						+ "' is not a valid rounding mode.  Please specify one of the constants defined in "
						+ NumberRounder.class.getName());
	}

	/**
	 * Rounds the given number using the given rounding mode. The type of the
	 * number is maintained (e.g. - if the number was a Float, it will remain
	 * so).
	 * 
	 * @param number
	 *            the number to round
	 * @param mode
	 *            the rounding mode, as specified by one of the constants in
	 *            this class
	 * @return <code>null</code>, if <code>number</code> is
	 *         <code>null</code> or <br>
	 *         <code>number</code> rounded as requested, otherwise
	 * @throws IllegalArgumentException
	 *             if an invalid rounding mode is specified
	 */
	public static Number round(Number number, String mode) throws IllegalArgumentException {
		if (NumberUtils.isDecimal(number)) {
			BigDecimal bigDecimal = NumberUtils.numberToBigDecimal(number);
			BigDecimal rounded = bigDecimal.setScale(0, getBigDecimalRoundMode(mode));
			if (number instanceof BigDecimal) {
				return rounded;
			}
			if (number instanceof Double) {
				return new Double(rounded.doubleValue());
			}
			if (number instanceof Float) {
				return new Float(rounded.floatValue());
			}
		}
		return number;
	}

}
