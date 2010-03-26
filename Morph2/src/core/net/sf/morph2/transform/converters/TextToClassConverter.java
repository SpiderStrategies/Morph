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

import java.lang.reflect.Array;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import net.sf.morph2.Defaults;
import net.sf.morph2.transform.Converter;
import net.sf.morph2.transform.DecoratedConverter;
import net.sf.morph2.transform.transformers.BaseTransformer;
import net.sf.morph2.util.StringUtils;

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
	/** Array indicator */
	public static final String ARRAY_INDICATOR = "[]";

	private static final HashMap CACHE_MAP = new HashMap();
	private static final Class[] DESTINATION_TYPES = { Class.class };

	//one default instance:
	private static final Converter DEFAULT_TEXT_CONVERTER = Defaults.createTextConverter();

	private Converter textConverter;
	private boolean useCache = true;

	/**
	 * {@inheritDoc}
	 */
	protected Object convertImpl(Class destinationClass, Object source,
		Locale locale) throws Exception {
		String string = StringUtils.removeWhitespace((String) getTextConverter().convert(
				String.class, source, locale));
		Class result;
		Map cache = useCache ? getCache() : null;
		Object lock = useCache ? (Object) cache : string; //trick to synch on optional cache w/o duplicating code
		synchronized (lock) {
			result = useCache ? (Class) cache.get(string) : null;
			if (result == null) {
				int arrayAt = string.indexOf(ARRAY_INDICATOR);
				Class c = Class.forName(arrayAt < 0 ? string : string.substring(0, arrayAt));
				result = arrayAt < 0 ? c : Array.newInstance(c,
						new int[StringUtils.numOccurrences(string, ARRAY_INDICATOR)])
						.getClass();
				if (useCache) {
					cache.put(string, result);
				}
			}
		}
		return result;
	}

	/**
	 * {@inheritDoc}
	 */
	protected Class[] getDestinationClassesImpl() throws Exception {
		return DESTINATION_TYPES;
	}

	/**
	 * {@inheritDoc}
	 */
	protected Class[] getSourceClassesImpl() throws Exception {
		return getTextConverter().getSourceClasses();
	}

	/**
	 * Get the text converter used by this TextToClassConverter.
	 * @return Converter
	 */
	public Converter getTextConverter() {
		if (textConverter == null) {
			setTextConverter(DEFAULT_TEXT_CONVERTER);
		}
		return textConverter;
	}

	/**
	 * Set the text converter used by this TextToClassConverter.
	 * @param textConverter
	 */
	public void setTextConverter(Converter textConverter) {
		this.textConverter = textConverter;
	}

	/**
	 * Get the useCache of this TextToClassConverter.
	 * @return the useCache
	 */
	public boolean isUseCache() {
		return useCache;
	}

	/**
	 * Set the useCache of this TextToClassConverter.
	 * @param useCache the useCache to set
	 */
	public void setUseCache(boolean useCache) {
		this.useCache = useCache;
	}

	/**
	 * Get the cache map associated with the operative text converter.
	 * @return Map
	 */
	private Map getCache() {
		Map result;
		synchronized (CACHE_MAP) {
			Converter cnv = getTextConverter();
			result = (Map) CACHE_MAP.get(cnv);
			if (result == null) {
				result = new HashMap();
				CACHE_MAP.put(cnv, result);
			}
		}
		return result;
	}

	/**
	 * {@inheritDoc}
	 */
	protected boolean isWrappingRuntimeExceptions() {
	    return true;
    }

}
