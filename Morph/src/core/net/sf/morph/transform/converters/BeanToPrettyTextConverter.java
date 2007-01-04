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
		
		boolean endsInSeparator = false;
		StringBuffer buffer = new StringBuffer(getPrefix());
		String[] propertyNames = getBeanReflector().getPropertyNames(source);
		for (int i=0; propertyNames != null && i < propertyNames.length; i++) {
			String propertyName = propertyNames[i];
			Object next = getBeanReflector().get(source, propertyName);
			if (next != null || isShowNullValues()) {
				String text = (String) getToTextConverter().convert(String.class,
					next, locale);
				if (isShowPropertyNames()) {
					buffer.append(propertyName);
					buffer.append(getNameValueSeparator());
				}
				buffer.append(text);
				buffer.append(getSeparator());
				endsInSeparator = true;
			}
		}
		if (endsInSeparator) {
			buffer.delete(buffer.length() - getSeparator().length(), buffer.length());	
		}
		buffer.append(getSuffix());
		
		return getTextConverter().convert(destinationClass, buffer, locale);
	}

	protected Class[] getSourceClassesImpl() throws Exception {
		return getBeanReflector().getReflectableClasses();
	}
	
	public String getSeparator() {
		if (super.getSeparator() == null) {
			setSeparator(DEFAULT_SEPARATOR);
		}
		return super.getSeparator();
	}

	public String getPrefix() {
		if (super.getPrefix() == null) {
			setPrefix(DEFAULT_PREFIX);
		}
		return super.getPrefix();
	}
	public String getSuffix() {
		if (super.getSuffix() == null) {
			setSuffix(DEFAULT_SUFFIX);
		}
		return super.getSuffix();
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
