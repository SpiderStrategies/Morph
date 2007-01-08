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
import net.sf.morph.reflect.BeanReflector;
import net.sf.morph.transform.Converter;

/**
 * <p>
 * Converts a bean to a textual representation (String or StringBuffer only).
 * The string representation is comprised of a prefix, a textual representation
 * of the array contents, and a suffix. The textual representation of the array
 * contents is in turn made up of the string representation of each of the
 * elements in the array separated by a separator character.  Conversions to
 * characters will only succeed if the result of the conversion is a single
 * character in length.
 * </p>
 * 
 * <p>
 * For example, if the prefix is <code>{</code>, the suffix is <code>}</code>,
 * the separator is <code>,</code> and the contents of the array are the
 * Integers <code>1</code>,<code>2</code> and <code>3</code>, the array
 * will be converted to the text <code>{1,2,3}</code>.
 * </p>
 * 
 * @author Matt Sgarlata
 * @since Feb 15, 2005
 */
public class BeanToPrettyTextConverter extends BaseToPrettyTextConverter {
	
	public static final String DEFAULT_PREFIX = "[";
	public static final String DEFAULT_SUFFIX = "]";	
	public static final String DEFAULT_SEPARATOR = ",";
	public static final String DEFAULT_NAME_VALUE_SEPARATOR = "=";
	
	private String nameValueSeparator = DEFAULT_NAME_VALUE_SEPARATOR;
	private boolean showPropertyNames = true;

	protected Object convertImpl(Class destinationClass, Object source,
		Locale locale) throws Exception {
		
		StringBuffer buffer = new StringBuffer(getPrefix());
		BeanReflector beanReflector = getBeanReflector();
		String[] propertyNames = beanReflector.getPropertyNames(source);
		if (!ObjectUtils.isEmpty(propertyNames)) {
			Converter toText = getToTextConverter();
			append(source, propertyNames[0], buffer, beanReflector, toText, locale);
			for (int i = 1; i < propertyNames.length; i++) {
				buffer.append(getSeparator());
				append(source, propertyNames[i], buffer, beanReflector, toText, locale);
			}
		}
		return getTextConverter().convert(destinationClass, buffer, locale);
	}

	private void append(Object source, String propertyName, StringBuffer buffer,
			BeanReflector reflector, Converter converter, Locale locale) {
		Object p = reflector.get(source, propertyName);
		if (p == null && !isShowNullValues()) {
			return;
		}
		if (isShowPropertyNames()) {
			buffer.append(propertyName).append(getNameValueSeparator());
		}
		buffer.append(converter.convert(String.class, p, locale));
	}

	protected Class[] getSourceClassesImpl() throws Exception {
		return getBeanReflector().getReflectableClasses();
	}
	
	public String getSeparator() {
		String separator = super.getSeparator();
		return separator == null ? DEFAULT_SEPARATOR : separator;
	}

	public String getPrefix() {
		String prefix = super.getPrefix();
		return prefix == null ? DEFAULT_PREFIX : prefix;
	}
	public String getSuffix() {
		String suffix = super.getSuffix();
		return suffix == null ? DEFAULT_SUFFIX : suffix;
	}	
	
	public String getNameValueSeparator() {
		return nameValueSeparator;
	}
	public void setNameValueSeparator(String nameValueSeparator) {
		this.nameValueSeparator = nameValueSeparator;
	}
	public boolean isShowPropertyNames() {
		return showPropertyNames;
	}
	public void setShowPropertyNames(boolean showPropertyNames) {
		this.showPropertyNames = showPropertyNames;
	}
}
