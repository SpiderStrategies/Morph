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

import net.sf.morph.lang.DecoratedLanguage;
import net.sf.morph.lang.languages.SimpleLanguage;
import net.sf.morph.reflect.BeanReflector;
import net.sf.morph.transform.DecoratedConverter;
import net.sf.morph.transform.transformers.BaseTransformer;

/**
 * A Converter that returns the result of evaluating a property against an object using a DecoratedLanguage.
 *
 * @author Matt Benson
 * @since Morph 1.0.2
 */
public class GetPropertyConverter extends BaseTransformer implements DecoratedConverter {
	private String expression;
	private DecoratedLanguage language;

	/**
	 * Construct a new GetPropertyConverter.
	 */
	public GetPropertyConverter() {
		super();
	}

	/**
	 * Construct a new GetPropertyConverter.
	 * @param expression
	 */
	public GetPropertyConverter(String expression) {
		this();
		setExpression(expression);
	}

	/**
	 * {@inheritDoc}
	 * @see net.sf.morph.transform.transformers.BaseTransformer#setDestinationClasses(java.lang.Class[])
	 */
	public synchronized void setDestinationClasses(Class[] destinationClasses) {
		super.setDestinationClasses(destinationClasses);
	}

	/* (non-Javadoc)
	 * @see net.sf.morph.transform.transformers.BaseReflectorTransformer#getDestinationClassesImpl()
	 */
	protected Class[] getDestinationClassesImpl() throws Exception {
		return new Class[] { Object.class };
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
	protected Class[] getSourceClassesImpl() throws Exception {
		return new Class[] { Object.class };
	}

	/* (non-Javadoc)
	 * @see net.sf.morph.transform.transformers.BaseTransformer#convertImpl(java.lang.Class, java.lang.Object, java.util.Locale)
	 */
	protected Object convertImpl(Class destinationClass, Object source, Locale locale)
			throws Exception {
		return getLanguage().get(source, getExpression(), destinationClass, locale);
	}

	/**
	 * Get the expression of this GetPropertyConverter.
	 * @return the expression
	 */
	public String getExpression() {
		return expression;
	}

	/**
	 * Set the expression of this GetPropertyConverter.
	 * @param expression the expression to set
	 */
	public void setExpression(String expression) {
		this.expression = expression;
	}

	/**
	 * Get the DecoratedLanguage language.
	 * @return DecoratedLanguage
	 */
	public synchronized DecoratedLanguage getLanguage() {
		if (language == null) {
			SimpleLanguage lang = new SimpleLanguage();
			lang.setReflector((BeanReflector) getReflector(BeanReflector.class));
			setLanguage(lang);
		}
		return language;
	}

	/**
	 * Set the DecoratedLanguage language.
	 * @param language DecoratedLanguage
	 */
	public synchronized void setLanguage(DecoratedLanguage language) {
		this.language = language;
	}
}
