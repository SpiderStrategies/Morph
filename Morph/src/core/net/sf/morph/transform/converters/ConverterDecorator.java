/*
 * Copyright 2004-2005, 2007 the original author or authors.
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
import net.sf.morph.transform.transformers.BaseTransformer;

/**
 * Decorates any Converter so that it implements 
 * {@link net.sf.morph.transform.DecoratedConverter}.  Example usage:
 * 
 * <pre>
 * Converter myConverter = new MyConverter();
 * DecoratedConverter decoratedConverter = new DecoratedConverter(myConverter);
 * 
 * // now use decoratedConverter instead of myConverter
 * StringBuffer buffer = decoratedConverter.convert(StringBuffer.class, "no locale needed");
 * </pre>
 * 
 * @author Matt Sgarlata
 * @since Dec 5, 2004
 */
public class ConverterDecorator extends BaseTransformer implements Converter,
		DecoratedConverter {

	private Converter nestedConverter;

	/**
	 * Create a new ConverterDecorator.
	 */
	public ConverterDecorator() {
		super();
	}

	/**
	 * Create a new ConverterDecorator.
	 * @param converter
	 */
	public ConverterDecorator(Converter converter) {
		this.nestedConverter = converter;
	}

	/**
	 * @return Returns the converter.
	 */
	public Converter getNestedConverter() {
		return nestedConverter;
	}

	/**
	 * @param converter The converter to set.
	 */
	public void setNestedConverter(Converter converter) {
		this.nestedConverter = converter;
	}

	/**
	 * {@inheritDoc}
	 * @see net.sf.morph.transform.transformers.BaseTransformer#convertImpl(java.lang.Class, java.lang.Object, java.util.Locale)
	 */
	protected Object convertImpl(Class destinationClass, Object source, Locale locale)
			throws Exception {
		return getNestedConverter().convert(destinationClass, source, locale);
	}

	/**
	 * {@inheritDoc}
	 * @see net.sf.morph.transform.transformers.BaseTransformer#setSourceClasses(java.lang.Class[])
	 */
	public synchronized void setSourceClasses(Class[] sourceClasses) {
		super.setSourceClasses(sourceClasses);
	}

	/**
	 * {@inheritDoc}
	 * @see net.sf.morph.transform.transformers.BaseTransformer#getSourceClassesImpl()
	 */
	protected Class[] getSourceClassesImpl() {
		return getNestedConverter().getSourceClasses();
	}

	/**
	 * {@inheritDoc}
	 * @see net.sf.morph.transform.transformers.BaseTransformer#setDestinationClasses(java.lang.Class[])
	 */
	public synchronized void setDestinationClasses(Class[] destinationClasses) {
		super.setDestinationClasses(destinationClasses);
	}

	/**
	 * {@inheritDoc}
	 * @see net.sf.morph.transform.transformers.BaseTransformer#getDestinationClassesImpl()
	 */
	protected Class[] getDestinationClassesImpl() {
		return getNestedConverter().getDestinationClasses();
	}

	/**
	 * {@inheritDoc}
	 * @see net.sf.morph.transform.transformers.BaseTransformer#isAutomaticallyHandlingNulls()
	 */
	protected boolean isAutomaticallyHandlingNulls() {
		return false;
	}

	/**
	 * {@inheritDoc}
	 * @see net.sf.morph.transform.transformers.BaseTransformer#isWrappingRuntimeExceptions()
	 */
	protected boolean isWrappingRuntimeExceptions() {
		// the whole point of this converter is for decorating user defined
		// transformers, so we don't want to eat their exceptions ;)
		return false;
	}

}
