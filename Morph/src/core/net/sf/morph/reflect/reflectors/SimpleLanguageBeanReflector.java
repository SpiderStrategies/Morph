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
package net.sf.morph.reflect.reflectors;

import net.sf.morph.Defaults;
import net.sf.morph.lang.languages.SimpleLanguage;
import net.sf.morph.reflect.BeanReflector;

/**
 * A Reflector that uses Morph's SimpleLanguage to reflect complex properties.
 * Note that <code>getPropertyNames()</code> will only report simple properties.
 *
 * @author Matt Benson
 */
public class SimpleLanguageBeanReflector extends BaseBeanReflector {

	private SimpleLanguage language;
	private BeanReflector nestedReflector;

	/* (non-Javadoc)
	 * @see net.sf.morph.reflect.reflectors.BaseReflector#initializeImpl()
	 */
	protected void initializeImpl() throws Exception {
		super.initializeImpl();
		language = new SimpleLanguage();
		language.setReflector(getNestedReflector());
	}

	/**
	 * {@inheritDoc}
	 */
	protected Object getImpl(Object bean, String propertyName) throws Exception {
		return language.get(bean, propertyName);
	}

	/**
	 * {@inheritDoc}
	 */
	protected String[] getPropertyNamesImpl(Object bean) throws Exception {
		return getNestedReflector().getPropertyNames(bean);
	}

	/**
	 * {@inheritDoc}
	 */
	protected Class getTypeImpl(Object bean, String propertyName) throws Exception {
		return language.getType(bean, propertyName);
	}

	/**
	 * {@inheritDoc}
	 */
	protected void setImpl(Object bean, String propertyName, Object value)
			throws Exception {
		language.set(bean, propertyName, value);
	}

	/**
	 * {@inheritDoc}
	 */
	protected Class[] getReflectableClassesImpl() throws Exception {
		return getNestedReflector().getReflectableClasses();
	}

	/* (non-Javadoc)
	 * @see net.sf.morph.reflect.reflectors.BaseReflector#isWriteableImpl(java.lang.Object, java.lang.String)
	 */
	protected boolean isWriteableImpl(Object bean, String propertyName) throws Exception {
		String[] tokens = language.getExpressionParser().parse(propertyName);
		Object o = bean;
		for (int i = 0; i < tokens.length; i++) {
			if (i > 0) {
				o = getNestedReflector().get(o, tokens[i - 1]);
			}
			if (!getNestedReflector().isWriteable(o, tokens[i])) {
				return false;
			}
		}
		return true;
	}

	/* (non-Javadoc)
	 * @see net.sf.morph.reflect.reflectors.BaseReflector#isReadableImpl(java.lang.Object, java.lang.String)
	 */
	protected boolean isReadableImpl(Object bean, String propertyName) throws Exception {
		String[] tokens = language.getExpressionParser().parse(propertyName);
		Object o = bean;
		for (int i = 0; i < tokens.length; i++) {
			if (i > 0) {
				o = getNestedReflector().get(o, tokens[i - 1]);
			}
			if (!getNestedReflector().isReadable(o, tokens[i])) {
				return false;
			}
		}
		return true;
	}

	/**
	 * Get the nestedReflector of this SimpleLanguageObjectReflector.
	 * @return the nestedReflector
	 */
	public synchronized BeanReflector getNestedReflector() {
		if (nestedReflector == null) {
			setNestedReflector(Defaults.createBeanReflector());
		}
		return nestedReflector;
	}

	/**
	 * Set the nestedReflector of this SimpleLanguageObjectReflector.
	 * @param nestedReflector the nestedReflector to set
	 */
	public synchronized void setNestedReflector(BeanReflector nestedReflector) {
		this.nestedReflector = nestedReflector;
	}

}
