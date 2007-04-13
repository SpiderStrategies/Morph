/*
 * Copyright 2007 the original author or authors.
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
import net.sf.morph.transform.transformers.BaseReflectorTransformer;

/**
 * A Converter that converts an object to one of its own properties.
 * 
 * @author Matt Benson
 * @since Morph 1.0.2
 */
public class GetPropertyConverter extends BaseReflectorTransformer implements DecoratedConverter {
	private String propertyName;
	private Converter propertyConverter;

	/**
	 * Construct a new GetPropertyConverter.
	 */
	public GetPropertyConverter() {
		super();
	}

	/**
	 * Construct a new GetPropertyConverter.
	 * @param propertyName
	 */
	public GetPropertyConverter(String propertyName) {
		this();
		setPropertyName(propertyName);
	}

	/* (non-Javadoc)
	 * @see net.sf.morph.transform.transformers.BaseReflectorTransformer#getDestinationClassesImpl()
	 */
	protected Class[] getDestinationClassesImpl() throws Exception {
		return getPropertyConverter().getDestinationClasses();
	}

	/* (non-Javadoc)
	 * @see net.sf.morph.transform.transformers.BaseTransformer#convertImpl(java.lang.Class, java.lang.Object, java.util.Locale)
	 */
	protected Object convertImpl(Class destinationClass, Object source, Locale locale)
			throws Exception {
		Object intermediate = getBeanReflector().get(source, getPropertyName());
		return getPropertyConverter().convert(destinationClass, intermediate, locale);
	}

	/**
	 * Get the propertyName of this GetPropertyConverter.
	 * @return the propertyName
	 */
	public String getPropertyName() {
		return propertyName;
	}

	/**
	 * Set the propertyName of this GetPropertyConverter.
	 * @param propertyName the propertyName to set
	 */
	public void setPropertyName(String propertyName) {
		this.propertyName = propertyName;
	}

	/**
	 * Get the propertyConverter of this GetPropertyConverter.
	 * @return the propertyConverter
	 */
	public synchronized Converter getPropertyConverter() {
		if (propertyConverter == null) {
			setPropertyConverter(Defaults.createConverter());
		}
		return propertyConverter;
	}

	/**
	 * Set the propertyConverter of this GetPropertyConverter.
	 * @param propertyConverter the propertyConverter to set
	 */
	public synchronized void setPropertyConverter(Converter propertyConverter) {
		this.propertyConverter = propertyConverter;
	}
}
