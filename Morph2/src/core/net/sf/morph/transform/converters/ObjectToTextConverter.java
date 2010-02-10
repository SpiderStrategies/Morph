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
package net.sf.morph.transform.converters;

import java.util.Locale;

import net.sf.morph.Defaults;
import net.sf.morph.transform.Converter;
import net.sf.morph.transform.DecoratedConverter;
import net.sf.morph.transform.ImpreciseTransformer;
import net.sf.morph.transform.transformers.BaseTransformer;
import net.sf.morph.util.TransformerUtils;

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
public class ObjectToTextConverter extends BaseTransformer implements DecoratedConverter, ImpreciseTransformer {

	private Converter textConverter;

	private static final Class[] SOURCE_TYPES = new Class[] {
		Object.class
	};

	/**
	 * {@inheritDoc}
	 */
	protected Object convertImpl(Class destinationClass, Object source,
		Locale locale) throws Exception {
		return getTextConverter().convert(destinationClass, source.toString(),
			locale);
	}

	/**
	 * {@inheritDoc}
	 */
	protected boolean isImpreciseTransformationImpl(Class destinationClass, Class sourceClass) {
		return TransformerUtils.isImpreciseTransformation(getTextConverter(), destinationClass, String.class);
	}

	/**
	 * {@inheritDoc}
	 */
	protected boolean isWrappingRuntimeExceptions() {
	    return true;
    }

	/**
	 * {@inheritDoc}
	 */
	protected Class[] getSourceClassesImpl() throws Exception {
		return SOURCE_TYPES;
	}

	/**
	 * {@inheritDoc}
	 */
	protected Class[] getDestinationClassesImpl() throws Exception {
		return getTextConverter().getDestinationClasses();
	}

	/**
	 * Get the text converter used by this ObjectToTextConverter.
	 * @return Converter
	 */
	public Converter getTextConverter() {
		if (textConverter == null) {
			setTextConverter(Defaults.createTextConverter());
		}
		return textConverter;
	}

	/**
	 * Set the text converter used by this ObjectToTextConverter.
	 * @param textConverter
	 */
	public void setTextConverter(Converter textConverter) {
		this.textConverter = textConverter;
	}
}
