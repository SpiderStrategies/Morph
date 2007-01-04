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

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Locale;

import net.sf.morph.Defaults;
import net.sf.morph.transform.Converter;
import net.sf.morph.transform.DecoratedConverter;
import net.sf.morph.transform.transformers.BaseTransformer;
import net.sf.morph.util.StringUtils;

/**
 * Converts a basic text type ({@link java.lang.String} or
 * {@link java.lang.StringBuffer}) into a {@link java.lang.Class}.  Array types
 * are supported, by appending <code>[]</code> to the end of the contained
 * class name.  If <code>[]</code> is appended <i>n</i> times, the returned
 * class will be <i>n</i>-dimensional.
 * 
 * @author Matt Sgarlata
 * @since Jan 2, 2005
 */
public class TextToClassConverter extends BaseTransformer implements Converter, DecoratedConverter {

	private static final Class[] DESTINATION_TYPES = { Class.class };
	public static final String ARRAY_INDICATOR = "[]";
	
	private Converter textConverter;
	
	protected Object convertImpl(Class destinationClass, Object source,
		Locale locale) throws Exception {
		String string = (String) getTextConverter().convert(String.class,
			source, locale);
		string = StringUtils.removeWhitespace(string);
		int n = StringUtils.numOccurrences(string, ARRAY_INDICATOR);
		if (n == 0) {
			return Class.forName(string);
		}
		else {
			String className = string.substring(0, string.indexOf(ARRAY_INDICATOR));
			Class containedType = Class.forName(className);
			int[] dimensions = new int[n];
			Arrays.fill(dimensions, 0);
			return Array.newInstance(containedType, dimensions).getClass();
		}
		
	}
	protected Class[] getDestinationClassesImpl() throws Exception {
		return DESTINATION_TYPES;
	}
	protected Class[] getSourceClassesImpl() throws Exception {
		return getTextConverter().getSourceClasses();
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
	
}
