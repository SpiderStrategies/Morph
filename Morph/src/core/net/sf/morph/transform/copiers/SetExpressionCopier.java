/*
 * Copyright 2007-2008 the original author or authors.
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
package net.sf.morph.transform.copiers;

import java.util.Locale;

import net.sf.composite.util.ObjectUtils;
import net.sf.morph.lang.DecoratedLanguage;
import net.sf.morph.lang.languages.SimpleLanguage;
import net.sf.morph.reflect.BeanReflector;
import net.sf.morph.transform.Converter;
import net.sf.morph.transform.DecoratedConverter;
import net.sf.morph.transform.DecoratedCopier;
import net.sf.morph.transform.NodeCopier;
import net.sf.morph.transform.Transformer;
import net.sf.morph.transform.transformers.BaseTransformer;
import net.sf.morph.util.Assert;
import net.sf.morph.util.ClassUtils;

/**
 * Uses a DecoratedLanguage to set the property denoted by an expression
 * on the destination object.
 *
 * @author mbenson
 * @since Morph 1.1
 */
public class SetExpressionCopier extends BaseTransformer implements DecoratedCopier,
		DecoratedConverter, NodeCopier {
	private String expression;
	private DecoratedLanguage language;

	/**
	 * Create a new SetExpressionCopier.
	 */
	public SetExpressionCopier() {
		super();
	}

	/**
	 * Create a new SetExpressionCopier.
	 */
	public SetExpressionCopier(String expression) {
		this();
		setExpression(expression);
	}

	/**
	 * {@inheritDoc}
	 * @see net.sf.morph.transform.transformers.BaseTransformer#initializeImpl()
	 */
	protected void initializeImpl() throws Exception {
		super.initializeImpl();
		Assert.notEmpty(getExpression(), "expression");
	}

	/**
	 * {@inheritDoc}
	 * @see net.sf.morph.transform.transformers.BaseTransformer#copyImpl(java.lang.Object, java.lang.Object, java.util.Locale, java.lang.Integer)
	 */
	protected void copyImpl(Object destination, Object source, Locale locale,
			Integer preferredTransformationType) throws Exception {
		getLanguage().set(destination, expression, source, locale);
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
		return ClassUtils.getAllClasses();
	}

	/**
	 * Get the String expression.
	 * @return String
	 */
	public String getExpression() {
		return expression;
	}

	/**
	 * Set the String expression.
	 * @param expression String
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
			if (getNestedTransformer() instanceof Converter) {
				lang.setConverter((Converter) getNestedTransformer());
			}
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

	/**
	 * {@inheritDoc}
	 * @since Morph 1.1.2
	 */
	public Object createReusableSource(Class destinationClass, Object source) {
		return super.createReusableSource(destinationClass, source);
	}

	/**
	 * {@inheritDoc}
	 * @since Morph 1.1.2
	 */
	public Transformer getNestedTransformer() {
		return super.getNestedTransformer();
	}

	/**
	 * {@inheritDoc}
	 * @since Morph 1.1.2
	 */
	public synchronized void setNestedTransformer(Transformer nestedTransformer) {
		DecoratedLanguage language = getLanguage();
		if (nestedTransformer instanceof Converter && language instanceof SimpleLanguage) {
			SimpleLanguage simpleLanguage = (SimpleLanguage) language;
			if (ObjectUtils.equals(simpleLanguage.getConverter(), getNestedTransformer())) {
				simpleLanguage.setConverter((Converter) nestedTransformer);
			}
		}
		super.setNestedTransformer(nestedTransformer);
	}

	/**
	 * {@inheritDoc}
	 */
	protected boolean isAutomaticallyHandlingNulls() {
		return false;
	}

	/**
	 * {@inheritDoc}
	 * @see net.sf.morph.transform.transformers.BaseTransformer#isWrappingRuntimeExceptions()
	 */
	protected boolean isWrappingRuntimeExceptions() {
		//we throw LanguageExceptions which should be wrapped as TransformationExceptions:
	    return true;
    }

}
