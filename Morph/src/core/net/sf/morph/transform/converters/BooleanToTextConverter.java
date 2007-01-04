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

import net.sf.morph.Defaults;
import net.sf.morph.transform.Converter;
import net.sf.morph.transform.DecoratedConverter;
import net.sf.morph.transform.TransformationException;
import net.sf.morph.transform.transformers.BaseTransformer;

/**
 * Converts boolean values to text values.  Subclasses can build in support for
 * locale-sensitive translations of true and false to text values by overriding
 * {@link BooleanToTextConverter#getTrueString(Locale)} and
 * {@link BooleanToTextConverter#getFalseString(Locale)}.
 * 
 * @author Matt Sgarlata
 * @since Jan 9, 2005
 */
public class BooleanToTextConverter extends BaseTransformer implements Converter, DecoratedConverter {
	
	private static final Class[] SOURCE_TYPES = { Boolean.class, boolean.class };
	
	private Converter textConverter;

	protected Object convertImpl(Class destinationClass, Object source,
		Locale locale) throws Exception {
		
		String string;
		if (source.equals(Boolean.TRUE)) {
			string = getTrueString(locale);
		}
		else if (source.equals(Boolean.FALSE)) {
			string = getFalseString(locale);
		}
		else {
			throw new TransformationException(destinationClass, source);
		}
		
		return getTextConverter().convert(destinationClass, string, locale);
	}

	protected Class[] getSourceClassesImpl() throws Exception {
		return SOURCE_TYPES;
	}

	protected Class[] getDestinationClassesImpl() throws Exception {
		return getTextConverter().getDestinationClasses();
	}
	
	public String getTrueString(Locale locale) {
		return Boolean.TRUE.toString();
	}
	
	public String getFalseString(Locale locale) {
		return Boolean.FALSE.toString();
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
