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
package net.sf.morph2.transform.converters;

import java.util.Locale;

import net.sf.composite.util.ObjectUtils;
import net.sf.morph2.Defaults;
import net.sf.morph2.transform.Converter;
import net.sf.morph2.transform.DecoratedConverter;
import net.sf.morph2.transform.TransformationException;
import net.sf.morph2.transform.transformers.BaseTransformer;
import net.sf.morph2.util.ContainerUtils;

/**
 * Converts text values to Booleans.  Text values include Characters, Strings and
 * StringBuffers.
 * 
 * @author Matt Sgarlata
 * @since Dec 31, 2004
 */
public class TextToBooleanConverter extends BaseTransformer implements DecoratedConverter {
	private Converter textConverter;

	private static final Class[] DESTINATION_TYPES = { Boolean.class,
		boolean.class };	

	/**
	 * Default values for the <code>trueText</code> attribute.
	 */
	public static final String[] DEFAULT_TRUE_TEXT = { "true", "t", "yes", "y" };

	/**
	 * Default values for the <code>falseText</code> attribute.
	 */
	public static final String[] DEFAULT_FALSE_TEXT = { "false", "f", "no", "n" };

	/**
	 * Defines the String, StringBuffer, and Character values that will be
	 * converted to <code>true</code>. The strings are not case-sensitive.
	 */
	private String[] trueText;
	/**
	 * Defines the String, StringBuffer, and Character values that will be
	 * converted to <code>false</code>. The strings are not case-sensitive.
	 */
	private String[] falseText;

	/**
	 * {@inheritDoc}
	 */
	protected Object convertImpl(Class destinationClass, Object source,
		Locale locale) throws Exception {

		String str = (String) getTextConverter().convert(String.class, source, locale);
		if (str != null) {
			str = str.toLowerCase();
		}
		if (ContainerUtils.contains(getTrueText(), str)) {
			return Boolean.TRUE;
		}
		if (ContainerUtils.contains(getFalseText(), str)) {
			return Boolean.FALSE;
		}
		if (ObjectUtils.isEmpty(str) && !destinationClass.isPrimitive()) {
			return null;
		}
		throw new TransformationException(destinationClass, source);
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
		return DESTINATION_TYPES;
	}

	/**
	 * Returns the String, StringBuffer, and Character values that will be
	 * converted to <code>false</code>. The strings are not case-sensitive.
	 * 
	 * @return the String, StringBuffer, and Character values that will be
	 *         converted to <code>false</code>. The strings are not
	 *         case-sensitive.
	 */
	public String[] getFalseText() {
		if (ObjectUtils.isEmpty(falseText)) {
			setFalseText(DEFAULT_FALSE_TEXT);
		}
		return falseText;
	}

	/**
	 * Sets the values that will be converted to <code>false</code>. The
	 * strings are not case-sensitive.
	 * 
	 * @param falseText
	 *            the values that will be converted to <code>false</code>.
	 *            The strings are not case-sensitive.
	 */
	public void setFalseText(String[] falseStrings) {
		falseStrings = changeToLowerCase(falseStrings);
		this.falseText = falseStrings;
	}

	/**
	 * Returns the String, StringBuffer, and Character values that will be
	 * converted to <code>true</code>. The strings are not case-sensitive.
	 * 
	 * @return the String, StringBuffer, and Character values that will be
	 *         converted to <code>true</code>. The strings are not
	 *         case-sensitive.
	 */
	public String[] getTrueText() {
		if (ObjectUtils.isEmpty(trueText)) {
			setTrueText(DEFAULT_TRUE_TEXT);
		}
		return trueText;
	}

	/**
	 * Sets the values that will be converted to <code>true</code>. The
	 * strings are not case-sensitive.
	 * 
	 * @param trueText
	 *            the values that will be converted to <code>true</code>. The
	 *            strings are not case-sensitive.
	 */
	public void setTrueText(String[] trueStrings) {
		trueStrings = changeToLowerCase(trueStrings);
		this.trueText = trueStrings;
	}

	/**
	 * {@inheritDoc}
	 */
	protected boolean isAutomaticallyHandlingNulls() {
		return false;
	}

	/**
	 * {@inheritDoc}
	 */
	protected boolean isWrappingRuntimeExceptions() {
	    return true;
    }

	/**
	 * Get an array of Strings obtained by converting contents of <code>array</code> to lowercase.
	 * @param array
	 * @return String[]
	 */
	private String[] changeToLowerCase(String[] array) {
		if (array == null) {
			return null;
		}

		String[] lowerCase = new String[array.length];
		for (int i = 0; i < array.length; i++) {
			lowerCase[i] = array[i].toLowerCase();
		}
		return lowerCase;
	}

	/**
	 * Get the text converter of this TextToBooleanConverter.
	 * @return Converter
	 */
	public Converter getTextConverter() {
		if (textConverter == null) {
			setTextConverter(Defaults.createTextConverter());
		}
		return textConverter;
	}

	/**
	 * Set the text converter of this TextToBooleanConverter.
	 * @param textConverter the Converter to set
	 */
	public void setTextConverter(Converter textConverter) {
		this.textConverter = textConverter;
	}

}
