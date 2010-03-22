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
package net.sf.morph2.transform.converters;

import java.math.BigDecimal;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.text.ParsePosition;
import java.util.Locale;

import net.sf.composite.util.ObjectUtils;
import net.sf.morph2.Defaults;
import net.sf.morph2.transform.Converter;
import net.sf.morph2.transform.DecoratedConverter;
import net.sf.morph2.transform.TransformationException;
import net.sf.morph2.transform.transformers.BaseTransformer;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Converts basic text types into primitive numbers or {@link java.lang.Number}
 * objects.
 *
 * @author Matt Sgarlata
 * @since Jan 4, 2005
 */
public class TextToNumberConverter extends BaseTransformer implements DecoratedConverter {

	private static final char RIGHT_PARENTHESES = ')';

	private static final char LEFT_PARENTHESES = '(';

	private static final Log logger = LogFactory.getLog(TextToNumberConverter.class);

	/**
	 * Constant indicating whitespace characters should be ignored when
	 * converting text to numbers. This is the default treatment of whitespace
	 * characters by this converter.
	 */
	public static final int WHITESPACE_IGNORE = 0;

	/**
	 * Constant indicating the presence of whitespace characters in text should
	 * prevent conversion of the text into a number (i.e. a
	 * TransformationException will be thrown if whitespace is in the text).
	 */
	public static final int WHITESPACE_REJECT = 1;

	/**
	 * Constant indicating currency symbols should be ignored when converting
	 * text to numbers. This is the default treatment of currency symbols by
	 * this converter.
	 */
	public static final int CURRENCY_IGNORE = 0;

	/**
	 * Constant indicating the presence of currency symbols in text should
	 * prevent conversion of the text into a number (i.e. a
	 * TransformationException will be thrown if currency symbols are present in
	 * the text).
	 */
	public static final int CURRENCY_REJECT = 1;

	/**
	 * Constant indicating percentage symbols should be ignored when converting
	 * text to numbers.
	 */
	public static final int PERCENTAGE_IGNORE = 0;

	/**
	 * Constant indicating the presence of percentage symbols in text should
	 * prevent conversion of the text into a number (i.e. a
	 * TransformationException will be thrown if percentage symbols are present
	 * in the text).
	 */
	public static final int PERCENTAGE_REJECT = 1;

	/**
	 * Constant indicating a percentage symbol at the end of text should cause
	 * the text to be treated as a percentage and converted into a corresponding
	 * decimal number. For example, 10 would be converted to 10 and 10% would be
	 * converted to .10. If there are percentage symbols in any position other
	 * than the last character of the text, a TransformationException will be
	 * thrown. This is the default treatment of percentages by this converter.
	 */
	public static final int PERCENTAGE_CONVERT_TO_DECIMAL = 2;

	/**
	 * Constant indicating parentheses should be ignored when converting
	 * text to numbers.
	 */
	public static final int PARENTHESES_IGNORE = 0;

	/**
	 * Constant indicating the presence of parentheses in text should
	 * prevent conversion of the text into a number (i.e. a
	 * TransformationException will be thrown if parentheses are present
	 * in the text).
	 */
	public static final int PARENTHESES_REJECT = 1;

	/**
	 * Constant indicating parentheses enclosing a number should cause the text
	 * to be treated as a negative number. For example, 10 would be converted to
	 * 10 and (10) would be converted to -10. If there are parentheses in
	 * positions other than the first or last character of the text, a
	 * TransformationException will be thrown. This is the default treatment of
	 * parentheses by this converter.
	 */
	public static final int PARENTHESES_NEGATE = 2;

	/**
	 * The converter used to convert text types from one type to another.
	 */
	private Converter textConverter;
	/**
	 * The converter used to convert number types from one type to another.
	 */
	private Converter numberConverter;

	/**
	 * Configuration option indicating how whitespace should be treated
	 * by this converter.  Default is {@link #WHITESPACE_IGNORE}.
	 */
	private int whitespaceHandling = WHITESPACE_IGNORE;
	/**
	 * Configuration option indicating how currencies should be treated by
	 * this converter.  Default is {@link #CURRENCY_IGNORE}.
	 */
	private int currencyHandling = CURRENCY_IGNORE;
	/**
	 * Configuration option indicating how percentages should be treated by
	 * this converter.  Default is {@link #PERCENTAGE_CONVERT_TO_DECIMAL}.
	 */
	private int percentageHandling = PERCENTAGE_CONVERT_TO_DECIMAL;
	/**
	 * Configuration option indicating how parantheses should be treated by
	 * this converter.  Default is {@link #PARENTHESES_NEGATE}.
	 */
	private int parenthesesHandling = PARENTHESES_NEGATE;

	/**
	 * {@inheritDoc}
	 */
	protected Object convertImpl(Class destinationClass, Object source,
		Locale locale) throws Exception {

		if (ObjectUtils.isEmpty(source)) {
			return null;
		}
		// convert the source to a String
		String string = (String) getTextConverter().convert(String.class,
			source, locale);

//			// if a custom numberFormat has been specified, ues that for the
//			// conversion
//			if (numberFormat != null) {
//				Number number;
//				synchronized (numberFormat) {
//					number = numberFormat.parse(string);
//				}
//
//				// convert the number to the destination class requested
//				return getNumberConverter().convert(destinationClass, number,
//					locale);
//			}

		StringBuffer charactersToParse = 
			// remove characters that should be ignored, such as currency symbols
			// when currency handling is set to CURRENCY_IGNORE
			removeIgnoredCharacters(string, locale);

		// keep track of whether the conversion result needs to be negated
		// before it is returned
		boolean negate = handleParenthesesNegation(charactersToParse, locale);	
		negate = negate || handleNegativeSignNegation(charactersToParse, locale);

		NumberFormat format = null;
		ParsePosition position = null;
		Number number = null;
		Object returnVal = null;
		String stringToParse = charactersToParse.toString();

// could not get this to work for some reason
//			// try to do the conversion assuming the source is a currency value
//			format = NumberFormat.getCurrencyInstance(locale);
//			position = new ParsePosition(0);
//			number = format.parse(stringWithoutIgnoredSymbolsStr, position);
//			if (isParseSuccessful(stringWithoutIgnoredSymbolsStr, position)) {
//				// convert the number to the destination class requested
//				returnVal = getNumberConverter().convert(destinationClass, number,
//					locale);
//				if (logger.isDebugEnabled()) {
//					logger.debug("Successfully parsed '" + source + "' as a currency value of " + returnVal);
//				}
//				return returnVal;
//			}
//			else {
//				if (logger.isDebugEnabled()) {
//					logger.debug("Could not perform conversion of '" + source + "' by treating the source as a currency value");
//				}
//			}

		// try to do the conversion to decimal assuming the source is a
		// percentage
		if (getPercentageHandling() == PERCENTAGE_CONVERT_TO_DECIMAL) {
			format = NumberFormat.getPercentInstance(locale);
			position = new ParsePosition(0);
			number = format.parse(stringToParse, position);
			if (isParseSuccessful(stringToParse, position)) {
				// negate the number if needed
				returnVal = negateIfNecessary(number, negate, locale);
				// convert the number to the destination class requested
				returnVal = getNumberConverter().convert(destinationClass, returnVal,
					locale);
				if (logger.isDebugEnabled()) {
					logger.debug("Successfully parsed '" + source + "' as a percentage with value " + returnVal);
				}
				return returnVal;
			}
			if (logger.isDebugEnabled()) {
				logger.debug("Could not perform conversion of '" + source + "' by treating the source as a percentage");
			}
		}

		// try to do the conversion as a regular number
		format = NumberFormat.getInstance(locale);
		position = new ParsePosition(0);
		number = format.parse(stringToParse, position);
		if (isParseSuccessful(stringToParse, position)) {
			// negate the number if needed
			returnVal = negateIfNecessary(number, negate, locale);
			// convert the number to the destination class requested
			returnVal = getNumberConverter().convert(destinationClass, returnVal,
				locale);
			if (logger.isDebugEnabled()) {
				logger.debug("Successfully parsed '" + source + "' as a number or currency value of " + returnVal);
			}
			return returnVal;
		}
		if (logger.isDebugEnabled()) {
			logger.debug("Could not perform conversion of '" + source + "' by treating the source as a regular number or currency value");
		}

//			// if the first character of the string is a currency symbol
//			if (Character.getType(stringWithoutIgnoredSymbolsStr.charAt(0)) == Character.CURRENCY_SYMBOL) {
//				// try doing the conversion as a regular number by stripping off the first character
//				format = NumberFormat.getInstance(locale);
//				position = new ParsePosition(1);
//				number = format.parse(stringWithoutIgnoredSymbolsStr, position);
//				if (isParseSuccessful(stringWithoutIgnoredSymbolsStr, position)) {
//					// convert the number to the destination class requested
//					return getNumberConverter().convert(destinationClass, number,
//						locale);
//				}
//				if (logger.isDebugEnabled()) {
//					logger.debug("Could not perform conversion of '" + source + "' by stripping the first character and treating as a normal number");
//				}
//			}

		throw new TransformationException(destinationClass, source);
	}

	/**
	 * Negate if necessary
	 * @param returnVal
	 * @param negate
	 * @param locale
	 * @return negated value if negate, else returnValue
	 */
	private Object negateIfNecessary(Number returnVal, boolean negate, Locale locale) {
		if (negate) {
			BigDecimal bd = (BigDecimal) getNumberConverter().convert(BigDecimal.class, returnVal, locale);
			return bd.negate();
		}
		return returnVal;
    }

	/**
	 * Remove any characters that should be ignored when performing the
	 * conversion.
	 * 
	 * @param string
	 *            the input string
	 * @param locale
	 *            the locale
	 * @return <code>string</code>, with all characters that should be
	 *         ignored removed
	 */
	private StringBuffer removeIgnoredCharacters(String string, Locale locale) {
		StringBuffer charactersToParse = new StringBuffer();
	    for (int i = 0; i < string.length(); i++) {
			char currentChar = string.charAt(i);
			if (getWhitespaceHandling() == WHITESPACE_IGNORE
				&& Character.isWhitespace(currentChar)) {
				continue;
			}
			if (getCurrencyHandling() == CURRENCY_IGNORE
				&& Character.getType(currentChar) == Character.CURRENCY_SYMBOL) {
				continue;
			}
			if (getPercentageHandling() == PERCENTAGE_IGNORE) {
				DecimalFormatSymbols symbols = new DecimalFormatSymbols(locale);
				if (currentChar == symbols.getPercent()) {
					continue;
				}
			}
			if (getParenthesesHandling() == PARENTHESES_IGNORE) {
				if (currentChar == LEFT_PARENTHESES
					|| currentChar == RIGHT_PARENTHESES) {
					continue;
				}
			}
			charactersToParse.append(currentChar);
		}
	    return charactersToParse;
    }

	/**
	 * Determines whether negation of the conversion result is needed based on
	 * the presence and handling method of parentheses.
	 * 
	 * @param charactersToParse
	 *            the characters to parse
	 * @param locale
	 *            the locale
	 * @return <code>true</code>, if the number is enclosed by parantheses
	 *         and parantheses handling is set to PARENTHESES_NEGATE or<br>
	 *         <code>false</code>, otherwise
	 */
	private boolean handleParenthesesNegation(StringBuffer charactersToParse, Locale locale) {
	    int lastCharIndex = charactersToParse.length() - 1;
		// if this is a number enclosed with parentheses and we should be
		// negating values in parentheses
		if (getParenthesesHandling() == PARENTHESES_NEGATE &&
			charactersToParse.charAt(0) == LEFT_PARENTHESES &&
			charactersToParse.charAt(lastCharIndex) == RIGHT_PARENTHESES) {
			// delete the closing paran
			charactersToParse.deleteCharAt(lastCharIndex);
			// delete the opening paran
			charactersToParse.deleteCharAt(0);
			// return true to indicate negation should take place
			return true;
		}
		// return false to indicate negation should not happen
		return false;
    }

	/**
	 * Determines whether negation of the conversion result is needed based on
	 * the presence of the minus sign character.
	 * 
	 * @param charactersToParse
	 *            the characters to parse
	 * @param locale
	 *            the locale
	 * @return <code>true</code>, if the number is enclosed by parantheses
	 *         and parantheses handling is set to PARENTHESES_NEGATE or<br>
	 *         <code>false</code>, otherwise
	 */
	private boolean handleNegativeSignNegation(StringBuffer charactersToParse, Locale locale) {
		if (charactersToParse.charAt(0) == '-') {
			charactersToParse.deleteCharAt(0);
			return true;
		}
		if (charactersToParse.charAt(charactersToParse.length() - 1) == '-') {
			charactersToParse.deleteCharAt(charactersToParse.length() - 1);
			return true;
		}
		return false;
    }

	/**
	 * Learn whether the entire string was consumed.
	 * @param stringWithoutIgnoredSymbolsStr
	 * @param position
	 * @return boolean
	 */
	protected boolean isParseSuccessful(String stringWithoutIgnoredSymbolsStr, ParsePosition position) {
		return position.getIndex() != 0 &&
			position.getIndex() == stringWithoutIgnoredSymbolsStr.length();
	}

	/**
	 * {@inheritDoc}
	 */
	protected boolean isWrappingRuntimeExceptions() {
	    return true;
    }

	/**
	 * {@inheritDoc}
	 */
	protected Class[] getSourceClassesImpl() throws Exception {
		return getTextConverter().getSourceClasses();
	}

	/**
	 * {@inheritDoc}
	 */
	protected Class[] getDestinationClassesImpl() throws Exception {
		return getNumberConverter().getDestinationClasses();
	}

	/**
	 * Sets the converter used to convert text types from one type to another.
	 *
	 * @return the converter used to convert text types from one type to another
	 */
	public Converter getNumberConverter() {
		if (numberConverter == null) {
			setNumberConverter(Defaults.createNumberConverter());
		}
		return numberConverter;
	}

	/**
	 * Sets the converter used to convert text types from one type to another.
	 *
	 * @param numberConverter
	 *            the converter used to convert text types from one type to
	 *            another
	 */
	public void setNumberConverter(Converter numberConverter) {
		this.numberConverter = numberConverter;
	}

	/**
	 * Gets the converter used to convert text types from one type to another.
	 *
	 * @return the converter used to convert text types from one type to another
	 */
	public Converter getTextConverter() {
		if (textConverter == null) {
			setTextConverter(Defaults.createTextConverter());
		}
		return textConverter;
	}

	/**
	 * Sets the converter used to convert text types from one type to another.
	 *
	 * @param textConverter
	 *            the converter used to convert text types from one type to
	 *            another
	 */
	public void setTextConverter(Converter textConverter) {
		this.textConverter = textConverter;
	}

	/**
	 * Retrieves the configuration option indicating how currencies should be
	 * treated by this converter. Default is {@link #CURRENCY_IGNORE}.
	 *
	 * @return the configuration option indicating how currencies should be
	 *         treated by this converter
	 */
	public int getCurrencyHandling() {
		return currencyHandling;
	}

	/**
	 * Sets the configuration option indicating how currencies should be treated
	 * by this converter. Default is {@link #CURRENCY_IGNORE}.
	 *
	 * @param currencyHandling
	 *            the configuration option indicating how currencies should be
	 *            treated by this converter. Default is {@link #CURRENCY_IGNORE}.
	 */
	public void setCurrencyHandling(int currencyHandling) {
		this.currencyHandling = currencyHandling;
	}

	/**
	 * Retrieves the configuration option indicating how parantheses should be
	 * treated by this converter. Default is {@link #PARENTHESES_NEGATE}.
	 *
	 * @return the configuration option indicating how parantheses should be
	 *         treated by this converter. Default is {@link #PARENTHESES_NEGATE}.
	 */
	public int getParenthesesHandling() {
		return parenthesesHandling;
	}

	/**
	 * Sets the configuration option indicating how parantheses should be
	 * treated by this converter. Default is {@link #PARENTHESES_NEGATE}.
	 *
	 * @param parenthesesHandling
	 *            the configuration option indicating how parantheses should be
	 *            treated by this converter. Default is
	 *            {@link #PARENTHESES_NEGATE}.
	 */
	public void setParenthesesHandling(int parenthesesHandling) {
		this.parenthesesHandling = parenthesesHandling;
	}

	/**
	 * Gets the configuration option indicating how percentages should be treated by
	 * this converter.  Default is {@link #PERCENTAGE_CONVERT_TO_DECIMAL}.
	 * @return the configuration option indicating how percentages should be treated by
	 * this converter.  Default is {@link #PERCENTAGE_CONVERT_TO_DECIMAL}.
	 */
	public int getPercentageHandling() {
		return percentageHandling;
	}

	/**
	 * Sets the configuration option indicating how percentages should be
	 * treated by this converter. Default is
	 * {@link #PERCENTAGE_CONVERT_TO_DECIMAL}.
	 *
	 * @param percentageHandling
	 *            the configuration option indicating how percentages should be
	 *            treated by this converter. Default is
	 *            {@link #PERCENTAGE_CONVERT_TO_DECIMAL}.
	 */
	public void setPercentageHandling(int percentageHandling) {
		this.percentageHandling = percentageHandling;
	}

	/**
	 * Gets the configuration option indicating how whitespace should be treated
	 * by this converter. Default is {@link #WHITESPACE_IGNORE}.
	 *
	 * @return the configuration option indicating how whitespace should be
	 *         treated by this converter. Default is {@link #WHITESPACE_IGNORE}.
	 */
	public int getWhitespaceHandling() {
		return whitespaceHandling;
	}

	/**
	 * Sets the configuration option indicating how whitespace should be treated
	 * by this converter. Default is {@link #WHITESPACE_IGNORE}.
	 *
	 * @param whitespaceHandling
	 *            the configuration option indicating how whitespace should be
	 *            treated by this converter. Default is
	 *            {@link #WHITESPACE_IGNORE}.
	 */
	public void setWhitespaceHandling(int whitespaceHandling) {
		this.whitespaceHandling = whitespaceHandling;
	}

}
