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
package net.sf.morph.transform.converters;

import java.text.NumberFormat;
import java.text.ParsePosition;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

import net.sf.composite.util.ObjectUtils;
import net.sf.morph.Defaults;
import net.sf.morph.transform.Converter;
import net.sf.morph.transform.DecoratedConverter;
import net.sf.morph.transform.TransformationException;
import net.sf.morph.transform.transformers.BaseTransformer;

import org.apache.log4j.Logger;

/**
 * Converts a basic text type ({@link java.lang.String},
 * {@link java.lang.StringBuffer} or {@link java.lang.Character}) into a
 * {@link java.lang.Number}.
 * 
 * @author Matt Sgarlata
 * @since Jan 4, 2005
 */
public class TextToNumberConverter extends BaseTransformer implements Converter, DecoratedConverter {
	
	private static final Logger logger = Logger.getLogger(TextToNumberConverter.class);
	
	private Converter textConverter;
	private Converter numberConverter;
	private char[] symbolsToIgnore = { '(', ')', ' ', '\t', '\r', '\n' };
//	private NumberFormat numberFormat;
	
	// values derived from the symbolsToIgnore array
	private transient boolean negateValuesInParantheses;
	private transient Set ignoredSet;
	
	protected void initializeImpl() throws Exception {
		super.initializeImpl();
		// initialize the transient member variables of this class
		setSymbolsToIgnore(symbolsToIgnore);
	}

	protected Object convertImpl(Class destinationClass, Object source,
		Locale locale) throws Exception {
		
		if (destinationClass.isPrimitive() && source == null) {
			throw new TransformationException(destinationClass, source);
		}
		
		if (ObjectUtils.isEmpty(source)) {
			return null;
		}
		else {
			
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
			
			// strip out the characters that should be ignored
			StringBuffer stringWithoutIgnoredSymbols = new StringBuffer();
			if (symbolsToIgnore == null	) {
				stringWithoutIgnoredSymbols.append(string);
			}
			else {
				for (int i=0; i<string.length(); i++) {
					char currentChar = string.charAt(i);
					Character currentCharacter = new Character(currentChar); 
					if (!ignoredSet.contains(currentCharacter) &&
						!(Character.getType(currentChar) == Character.CURRENCY_SYMBOL)) {
						stringWithoutIgnoredSymbols.append(currentCharacter);
					}
				}
			}
			
			int lastCharIndex = stringWithoutIgnoredSymbols.length() - 1;
			// if this is a number enclosed with parantheses and we should be
			// negating negative numbers
			if (negateValuesInParantheses &&
				stringWithoutIgnoredSymbols.charAt(0) == '(' &&
				stringWithoutIgnoredSymbols.charAt(lastCharIndex) == ')') {
				// delete the closing paran
				stringWithoutIgnoredSymbols.deleteCharAt(lastCharIndex);				
				// delete the opening paran
				stringWithoutIgnoredSymbols.deleteCharAt(0);
				// add a - symbol, to indicate the number is negative
				stringWithoutIgnoredSymbols.insert(0, '-');
			}
			
			NumberFormat format = null;
			ParsePosition position = null;
			Number number = null;
			Object returnVal = null;
			String stringWithoutIgnoredSymbolsStr = stringWithoutIgnoredSymbols.toString();
			
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
			
			// try to do the conversion assuming the source is a percentage
			format = NumberFormat.getPercentInstance(locale);
			position = new ParsePosition(0);
			number = format.parse(stringWithoutIgnoredSymbolsStr, position);
			if (isParseSuccessful(stringWithoutIgnoredSymbolsStr, position)) {			
				// convert the number to the destination class requested
				returnVal = getNumberConverter().convert(destinationClass, number,
					locale);
				if (logger.isDebugEnabled()) {
					logger.debug("Successfully parsed '" + source + "' as a percentage with value " + returnVal);
				}
				return returnVal;
			}
			else {
				if (logger.isDebugEnabled()) {
					logger.debug("Could not perform conversion of '" + source + "' by treating the source as a percentage");
				}
			}

			// try to do the conversion as a regular number
			format = NumberFormat.getInstance(locale);
			position = new ParsePosition(0);
			number = format.parse(stringWithoutIgnoredSymbolsStr, position);
			if (isParseSuccessful(stringWithoutIgnoredSymbolsStr, position)) {
				// convert the number to the destination class requested
				returnVal = getNumberConverter().convert(destinationClass, number,
					locale);
				if (logger.isDebugEnabled()) {
					logger.debug("Successfully parsed '" + source + "' as a number or currency value of " + returnVal);
				}
				return returnVal;
			}
			else {
				if (logger.isDebugEnabled()) {
					logger.debug("Could not perform conversion of '" + source + "' by treating the source as a regular number or currency value");
				}
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
//				else {
//					if (logger.isDebugEnabled()) {
//						logger.debug("Could not perform conversion of '" + source + "' by stripping the first character and treating as a normal number");
//					}	
//				}				
//			}			
			
			throw new TransformationException(destinationClass, source);
		}
	}
	
	protected boolean isAutomaticallyHandlingNulls() {
		return false;
	}

	private boolean isParseSuccessful(String stringWithoutIgnoredSymbolsStr, ParsePosition position) {
		return position.getIndex() != 0 &&
			position.getIndex() == stringWithoutIgnoredSymbolsStr.length();
	}

	protected Class[] getSourceClassesImpl() throws Exception {
		return getTextConverter().getSourceClasses();
	}

	protected Class[] getDestinationClassesImpl() throws Exception {
		return getNumberConverter().getDestinationClasses();
	}

	public Converter getNumberConverter() {
		if (numberConverter == null) {
			setNumberConverter(Defaults.createNumberConverter());
		}
		return numberConverter;
	}
	public void setNumberConverter(Converter numberConverter) {
		this.numberConverter = numberConverter;
	}
	public Converter getTextConverter() {
		if (textConverter == null) {
			setTextConverter(Defaults.createTextConverter());
		}
		return textConverter;
	}
	public void setTextConverter(Converter textConverter) {
		this.textConverter = textConverter;
	}

	public char[] getSymbolsToIgnore() {
		return symbolsToIgnore;
	}

	public synchronized void setSymbolsToIgnore(char[] symbolsToIgnore) {
		
		// construct a set containing each of the ignored characters,
		// except parantheses.  this is done for efficiency reasons
		ignoredSet = new HashSet();
		boolean containsOpenParantheses = false;
		boolean containsCloseParantheses = false;
		for (int i=0; symbolsToIgnore != null && i<symbolsToIgnore.length; i++) {
			if (symbolsToIgnore[i] == '(') {
				containsOpenParantheses = true;
			}
			else if (symbolsToIgnore[i] == ')') {
				containsCloseParantheses = true;
			}
			// the symbol is not a parantheses
			else {
				ignoredSet.add(new Character(symbolsToIgnore[i]));
			}
		}
		
		if (containsOpenParantheses && containsCloseParantheses) {
			negateValuesInParantheses = true;
		}
		else {
			negateValuesInParantheses = false;
			if (containsOpenParantheses) {
				ignoredSet.add(new Character('('));				
			}
			if (containsCloseParantheses) {
				ignoredSet.add(new Character(')'));
			}
		}
		
		this.symbolsToIgnore = symbolsToIgnore;
	}

//	/**
//	 * Retrieve the custom NumberFormat used by this converter to convert text
//	 * into numbers.
//	 * 
//	 * @return the custom NumberFormat used by this converter to convert text
//	 *         into numbers
//	 */
//	public NumberFormat getNumberFormat() {
//		return numberFormat;
//	}
//
//	/**
//	 * Sets the custom NumberFormat used by this converter to convert text into
//	 * numbers
//	 * 
//	 * @param numberFormat
//	 *            the custom NumberFormat used by this converter to convert text
//	 *            into numbers
//	 */
//	public void setNumberFormat(NumberFormat numberFormat) {
//		this.numberFormat = numberFormat;
//	}
	
}
