/*
 * Copyright 2004-2005, 2010 the original author or authors.
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
 */package net.sf.morph.util;

import java.lang.reflect.Array;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import net.sf.morph.Morph;

/**
 * @author Matt Sgarlata
 * @since Mar 11, 2005
 */
public class StringUtils {

	/**
	 * Find the number of occurrences of <code>searchFor</code> in <code>toSearch</code>.
	 * @param toSearch
	 * @param searchFor
	 * @return int
	 */
	public static int numOccurrences(String toSearch, String searchFor) {
		if (toSearch == null && searchFor == null) {
			return 0;
		}
		if (searchFor == null || searchFor.length() == 0) {
			return 0;
		}
		int matchingIndex = 0;
		int numOccurrences = 0;
		for (int i = 0; i < toSearch.length(); i++) {
			if (toSearch.charAt(i) == searchFor.charAt(matchingIndex)) {
				if (matchingIndex == searchFor.length() - 1) {
					matchingIndex = 0;
					numOccurrences++;
				}
				else {
					matchingIndex++;
				}
			}
		}
		return numOccurrences;
	}

	/**
	 * Get a string consisting of <code>n</code> repetitions of <code>str</code>. 
	 * @param str
	 * @param n
	 * @return String
	 */
	public static String repeat(String str, int n) {
		int size = n * (str == null ? 0 : str.length());
		StringBuffer buffer = new StringBuffer(size);
		for (int i = 0; i < n; i++) {
			buffer.append(str);
		}
		return buffer.toString();
	}

	/**
	 * Separate the elements of an array with commas, and add "and" before the last value.
	 * @param values
	 * @return String
	 */
	public static String englishJoin(Object values) {
		if (values == null) {
			return null;
		}
		StringBuffer buffer = new StringBuffer();
		int length = Array.getLength(values);
		for (int i = 0; i < length; i++) {
			buffer.append(Array.get(values, i));
			if (i < length - 2) {
				buffer.append(", ");
			}
			if (i == length - 2) {
				buffer.append(" and ");
			}
		}
		return buffer.toString();
	}

	/**
	 * Separate the elements of a Collection with commas, and add "and" before the last value.
	 * @param values
	 * @return String
	 */
	public static String englishJoin(Collection values) {
		return englishJoin(values.toArray(new Object[values.size()]));
	}

	/**
	 * Remove any whitespace from the given string. 
	 * @param expression
	 * @return String
	 */
	public static String removeWhitespace(String expression) {
		if (expression == null) {
			return null;
		}

		// first make sure there is whitespace.  Usually there won't be so we
		// can save ourselves from creating a new StringBuffer and String
		for (int i = 0; i < expression.length(); i++) {
			if (Character.isWhitespace(expression.charAt(i))) {
				return constructStringWithoutWhitespace(expression);
			}
		}

		return expression;
	}

	private static String constructStringWithoutWhitespace(String expression) {
		StringBuffer buffer = new StringBuffer(expression.length());
		for (int i = 0; i < expression.length(); i++) {
			if (!Character.isWhitespace(expression.charAt(i))) {
				buffer.append(expression.charAt(i));
			}
		}
		return buffer.toString();
	}

	/**
	 * Join the elements of a container, separated by <code>separator</code>.
	 * @param object
	 * @param separator
	 * @return String
	 */
	public static String join(Object object, String separator) {
		if (object == null) {
			return null;
		}

		//build the metric id string
		StringBuffer idBuff = new StringBuffer();
		boolean firstIteration = true;
		Collection collection = (Collection) Morph.convert(List.class, object);
		for (Iterator iter = collection.iterator(); iter.hasNext();) {
			if (firstIteration) {
				firstIteration = false;
			}
			else {
				idBuff.append(separator);
			}
			idBuff.append(iter.next());
		}
		return idBuff.toString();
	}

	/**
	 * Join the elements of a container, separated by ", ".
	 * @param object
	 * @return String
	 */
	public static String join(Object object) {
		return join(object, ", ");
	}

}
