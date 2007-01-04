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

import java.util.Locale;

import net.sf.composite.util.ObjectUtils;
import net.sf.morph.transform.Converter;
import net.sf.morph.transform.DecoratedConverter;
import net.sf.morph.transform.TransformationException;
import net.sf.morph.transform.transformers.BaseTransformer;
import net.sf.morph.util.ContainerUtils;

/**
 * Converts text values to Booleans.  Text values include Characers, Strings and
 * StringBuffers.
 * 
 * @author Matt Sgarlata
 * @since Dec 31, 2004
 */
public class TextToBooleanConverter extends BaseTransformer implements Converter, DecoratedConverter {

	private static final Class[] SOURCE_TYPES = {
		String.class, Character.class, char.class,
		StringBuffer.class
	};
	
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
	
	protected Object convertImpl(Class destinationClass, Object source,
		Locale locale) throws Exception {
		
		String str = source.toString().toLowerCase();
		if (ContainerUtils.contains(getTrueText(), str)) {
			return Boolean.TRUE;
		}
		else if (ContainerUtils.contains(getFalseText(), str)) {
			return Boolean.FALSE;
		}
		else if ("".equals(str)) {
			return null;
		}

		throw new TransformationException(destinationClass, source);
	}

	protected Class[] getSourceClassesImpl() throws Exception {
		return SOURCE_TYPES;
	}

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
	 * Sets the String, StringBuffer, and Character values that will be
	 * converted to <code>false</code>. The strings are not case-sensitive.
	 * 
	 * @param falseText
	 *            the String, StringBuffer, and Character values that will be
	 *            converted to <code>false</code>. The strings are not
	 *            case-sensitive.
	 */
	public void setFalseText(String[] falseStrings) {
		this.falseText = falseStrings;
		changeToLowerCase(falseStrings);
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
	 * Sets the String, StringBuffer, and Character values that will be
	 * converted to <code>true</code>. The strings are not case-sensitive.
	 * 
	 * @param trueText
	 *            the String, StringBuffer, and Character values that will be
	 *            converted to <code>true</code>. The strings are not
	 *            case-sensitive.
	 */
	public void setTrueText(String[] trueStrings) {
		this.trueText = trueStrings;
		changeToLowerCase(trueStrings);
	}

	private void changeToLowerCase(String[] array) {
		for (int i = 0; i < array.length; i++) {
			array[i] = array[i].toLowerCase();
		}
	}


}
