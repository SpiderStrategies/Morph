/*
 * Copyright 2004-2005, 2007-2008 the original author or authors.
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
package net.sf.morph.transform.converters;

import java.math.BigDecimal;
import java.util.Locale;

import net.sf.morph.transform.DecoratedConverter;
import net.sf.morph.transform.TransformationException;
import net.sf.morph.transform.support.NumberRounder;
import net.sf.morph.transform.transformers.BaseTransformer;
import net.sf.morph.util.ClassUtils;
import net.sf.morph.util.NumberUtils;

/**
 * Converts a number from one number type to another.
 * 
 * @author Matt Sgarlata
 * @since Dec 14, 2004
 */
public class NumberConverter extends BaseTransformer implements DecoratedConverter {
	
	private static final Class[] SOURCE_AND_DESTINATION_TYPES = {
		Number.class, byte.class, short.class, int.class, long.class,
		float.class, double.class, null
	};

	/** Default rounding method */
	public static final String DEFAULT_ROUNDING_METHOD = NumberRounder.ROUND_HALF_UP;

//	/**
//	 * The source classes for number converters.
//	 */
//	public static final Class[] SOURCE_TYPES = new Class[] {
//		Number.class,
//		String.class,
//		StringBuffer.class,
//		Character.class
//	};
	
//	/**
//	 * Whether this converter should ensure that data remains consistent when
//	 * conversions are performed. The default behavior for Java allows for
//	 * rollover to occur when converting larger numbers into smaller numbers.
//	 * This causes numbers with a large absolute value to roll over to a random
//	 * value which might not even have the same sign as the original number.
//	 * (Actually usually the lower bits of the number are retained and the
//	 * higher bits are discarded, but I think most end users would tell you
//	 * that's pretty random to them!) Usually this behavior is problematic, so
//	 * an exception should be thrown. If this value is set to true, a
//	 * TransformationException is thrown when such a conversion is attempted.
//	 * By default, this value is initialized to <code>true</code> for all
//	 * transformers included in the Morph framework.
//	 */
//	private boolean ensureDataConsistency;

	private String roundingMethod;

	/**
	 * Creates a number converter that is ensuring data consistency.
	 */
	public NumberConverter() {
		super();
//		setEnsureDataConsistency(true);
		setRoundingMethod(DEFAULT_ROUNDING_METHOD);
	}

	/**
	 * {@inheritDoc}
	 */
	protected Class[] getSourceClassesImpl() throws Exception {
		return SOURCE_AND_DESTINATION_TYPES;
	}

	/**
	 * {@inheritDoc}
	 */
	protected Class[] getDestinationClassesImpl() throws Exception {
		return SOURCE_AND_DESTINATION_TYPES;
	}

	/**
	 * Verify <code>number</code> is within the bounds of <code>destinationClass</code>.  
	 * @param destinationClass
	 * @param number
	 * @throws Exception if validation fails
	 */
	protected void checkNotOutOfBounds(Class destinationClass, Number number) throws Exception {
//		if (isEnsureDataConsistency() &&
			if (NumberUtils.isTooBigForType(number, destinationClass)) {
				throw new TransformationException(destinationClass, number,
					null, number + " is too large to be contained in a "
						+ destinationClass.getName());
			}
			if (NumberUtils.isTooSmallForType(number, destinationClass)) {
				throw new TransformationException(destinationClass, number,
					null, number + " is too small to be contained in a "
						+ destinationClass.getName());
			}
//		}
	}

	/**
	 * {@inheritDoc}
	 */
	protected Object convertImpl(Class destinationClass, Object source,
		Locale locale) throws Exception {

		if (destinationClass.isPrimitive() && source == null) {
			throw new TransformationException(destinationClass, source);
		}

		// basically this check is to allow conversions of specific types like
		// java.util.BigDecimal to the more general java.lang.Number
		if (destinationClass.isAssignableFrom(ClassUtils.getClass(source))) {
			return source;
		}
		checkNotOutOfBounds(destinationClass, (Number) source);

		String numberStr;
		if (isDecimal(destinationClass)) {
			numberStr = source.toString();
		}
		else {
			BigDecimal bigDecimal = new BigDecimal(source.toString());
			bigDecimal = bigDecimal.setScale(0,
				NumberRounder.getBigDecimalRoundMode(getRoundingMethod()));
			numberStr = bigDecimal.toString();
		}
		return NumberUtils.getNumber(destinationClass, numberStr);
	}

	/**
	 * Learn whether <code>numberType</code> is a decimal type
	 * @param numberType
	 * @return boolean
	 */
	protected boolean isDecimal(Class numberType) {
		return numberType == double.class || numberType == Double.class
				|| numberType == float.class || numberType == Float.class
				|| BigDecimal.class.isAssignableFrom(numberType);
	}

//	/**
//	 * Gets whether this converter should ensure that data remains consistent
//	 * when conversions are performed. The default behavior for Java allows for
//	 * rollover to occur when converting larger numbers into smaller numbers.
//	 * This causes numbers with a large absolute value to roll over to a random
//	 * value which might not even have the same sign as the original number.
//	 * (Actually usually the lower bits of the number are retained and the
//	 * higher bits are discarded, but I think most end users would tell you
//	 * that's pretty random to them!) Usually this behavior is problematic, so
//	 * an exception should be thrown. If this value is set to true, a
//	 * TransformationException is thrown when such a conversion is attempted.
//	 * By default, this value is initialized to <code>true</code> for all
//	 * transformers included in the Morph framework.
//	 * 
//	 * @return whether data consistency should be ensured
//	 */
//	public boolean isEnsureDataConsistency() {
//		return ensureDataConsistency;
//	}
//
//	/**
//	 * Sets whether this converter should ensure that data remains consistent
//	 * when conversions are performed. The default behavior for Java allows for
//	 * rollover to occur when converting larger numbers into smaller numbers.
//	 * This causes numbers with a large absolute value to roll over to a random
//	 * value which might not even have the same sign as the original number.
//	 * (Actually usually the lower bits of the number are retained and the
//	 * higher bits are discarded, but I think most end users would tell you
//	 * that's pretty random to them!) Usually this behavior is problematic, so
//	 * an exception should be thrown. If this value is set to true, a
//	 * TransformationException is thrown when such a conversion is attempted.
//	 * By default, this value is initialized to <code>true</code> for all
//	 * transformers included in the Morph framework.
//	 * 
//	 * @param ensureDataConsistency
//	 *            whether data consistency should be ensured
//	 */
//	public void setEnsureDataConsistency(boolean ensureDataConsistency) {
//		this.ensureDataConsistency = ensureDataConsistency;
//	}

	/**
	 * Get the rounding method used by this NumberConverter.
	 * @return String
	 */
	public String getRoundingMethod() {
		if (roundingMethod == null) {
			setRoundingMethod(DEFAULT_ROUNDING_METHOD);
		}
		return roundingMethod;
	}

	/**
	 * Set the rounding method used by this NumberConverter.
	 * @param roundingMethod
	 */
	public void setRoundingMethod(
		String roundingMethod) {
		this.roundingMethod = roundingMethod;
	}

	/**
	 * {@inheritDoc}
	 */
	protected boolean isWrappingRuntimeExceptions() {
	    return true;
    }

}