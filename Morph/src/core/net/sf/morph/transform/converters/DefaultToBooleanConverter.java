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

import net.sf.morph.transform.Converter;
import net.sf.morph.transform.DecoratedConverter;
import net.sf.morph.transform.TransformationException;
import net.sf.morph.transform.transformers.SimpleDelegatingTransformer;
import net.sf.morph.util.ClassUtils;

/**
 * Converts any object to a Boolean by delegating to
 * {@link net.sf.morph.transform.converters.TextToBooleanConverter},
 * {@link net.sf.morph.transform.converters.NumberToBooleanConverter} and
 * {@link net.sf.morph.transform.converters.ObjectToBooleanConverter}.
 * 
 * @author Matt Sgarlata
 * @since Dec 29, 2004
 * @see net.sf.morph.transform.transformers.SimpleDelegatingTransformer
 * @see DefaultToBooleanConverter#COMPONENTS
 * @see net.sf.morph.transform.converters.TextToBooleanConverter 
 * @see net.sf.morph.transform.converters.NumberToBooleanConverter 
 * @see net.sf.morph.transform.converters.ObjectToBooleanConverter 
 */
public class DefaultToBooleanConverter extends SimpleDelegatingTransformer implements Converter, DecoratedConverter {
	
	private static final Class[] DESTINATION_TYPES = { Boolean.class,
		boolean.class };
	
	public static final Converter[] COMPONENTS = {
		new IdentityConverter(),
		new PrimitiveWrapperConverter(),
		new TextToBooleanConverter(),
		new NumberToBooleanConverter(),
		new ObjectToBooleanConverter()
	};
	
	public DefaultToBooleanConverter() {
		super();
		setComponents(COMPONENTS);
	}
	
	protected Object convertImpl(Class destinationType, Object source, Locale locale) throws Exception {
		if (destinationType == Boolean.class && source == null) {
			return null;
		}
		if (destinationType == boolean.class && source == null) {
			throw new TransformationException(destinationType, source);
		}
		return super.convertImpl(destinationType, source, locale);
	}

	protected boolean isAutomaticallyHandlingNulls() {
		return false;
	}

	public Object[] getComponents() {
		return COMPONENTS;
	}

	public Class[] getDestinationClassesImpl() throws Exception {
		return DESTINATION_TYPES;
	}

	protected boolean isTransformableImpl(Class destinationType,
		Class sourceType) throws Exception {
		return ClassUtils.inheritanceContains(DESTINATION_TYPES, destinationType);
	}

}