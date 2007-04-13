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
import net.sf.morph.transform.transformers.BaseTransformer;

/**
 * Converts an object to a textual representation by calling the object's
 * <code>toString</code> method. Textual representations include
 * <code>String</code>s, <code>StringBuffer</code>s and
 * <code>Character</code>s. Conversions to characters will only succeed if
 * the result of the conversion is a single character in length.
 * 
 * @author Matt Sgarlata
 * @since Dec 24, 2004
 */
public class ObjectToTextConverter extends BaseTransformer implements Converter, DecoratedConverter {
	
	private Converter textConverter;
	
	private static final Class[] SOURCE_TYPES = new Class[] {
		Object.class
	};

	protected Object convertImpl(Class destinationClass, Object source,
		Locale locale) throws Exception {
		return getTextConverter().convert(destinationClass, source.toString(),
			locale);
	}

	protected boolean isWrappingRuntimeExceptions() {
	    return true;
    }

	protected Class[] getSourceClassesImpl() throws Exception {
		return SOURCE_TYPES;
	}

	protected Class[] getDestinationClassesImpl() throws Exception {
		return getTextConverter().getDestinationClasses();
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
