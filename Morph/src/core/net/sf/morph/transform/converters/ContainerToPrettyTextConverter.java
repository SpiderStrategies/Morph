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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Locale;

/**
 * <p>
 * Converts a container to a textual representation (String or StringBuffer only).
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
 * @since Dec 25, 2004
 */
public class ContainerToPrettyTextConverter extends BaseToPrettyTextConverter {
	
	public static final String DEFAULT_PREFIX = "{";
	public static final String DEFAULT_SUFFIX = "}";	
	public static final String DEFAULT_SEPARATOR = "|";

	protected Object convertImpl(Class destinationClass, Object source,
		Locale locale) throws Exception {
		
		boolean endsInSeparator = false;
		StringBuffer buffer = new StringBuffer(getPrefix());
		Iterator iterator = getContainerReflector().getIterator(source);
		while (iterator != null && iterator.hasNext()) {
			Object next = iterator.next();
			if (next != null || isShowNullValues()) {
				String text = (String) getToTextConverter().convert(String.class,
					next, locale);
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
		// the container reflector can treat an object like a container,
		// but for this converter we don't want objects to be valid sources
		// to convert objects to text, the ObjectToTextConverter should be used
		// instead
		ArrayList al = null;
		Class[] c = getContainerReflector().getReflectableClasses();
		for (int i = 0; i < c.length; i++) {
			if (c[i] == Object.class) {
				if (al == null) {
					al = new ArrayList(c.length - 1);
					for (int j = 0; j < i; j++) {
						al.add(j, c[j]);
					}
				}
			} else if (al != null) {
				al.add(c[i]);
			}
		}
		return al == null ? c : (Class[]) al.toArray(new Class[al.size()]);
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

}
