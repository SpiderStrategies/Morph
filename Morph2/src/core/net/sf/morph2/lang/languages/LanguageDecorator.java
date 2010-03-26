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
package net.sf.morph2.lang.languages;

import net.sf.morph2.Defaults;
import net.sf.morph2.lang.DecoratedLanguage;
import net.sf.morph2.lang.Language;
import net.sf.morph2.lang.LanguageException;

/**
 * Decorates any language so that it implements
 * {@link net.sf.morph2.lang.DecoratedLanguage}.
 *
 * @author Matt Sgarlata
 * @since Nov 28, 2004
 */
public class LanguageDecorator extends BaseLanguage implements DecoratedLanguage {

	private Language language;

	/**
	 * Create a new LanguageDecorator.
	 */
	public LanguageDecorator() {
		super();
	}

	/**
	 * Create a new LanguageDecorator.
	 * @param language to decorate
	 */
	public LanguageDecorator(Language language) {
		this();
		this.language = language;
	}

	/**
	 * {@inheritDoc}
	 */
	protected Object getImpl(Object target, String expression) throws LanguageException {
		return getLanguage().get(target, expression);
	}

	/**
	 * {@inheritDoc}
	 */
	protected Class getTypeImpl(Object target, String expression)
			throws LanguageException {
		return getLanguage().getType(target, expression);
	}

	/**
	 * {@inheritDoc}
	 */
	protected void setImpl(Object target, String expression, Object value)
			throws Exception {
		getLanguage().set(target, expression, value);
	}

	/**
	 * {@inheritDoc}
	 */
	protected boolean isPropertyImpl(String expression) {
		return getLanguage().isProperty(expression);
	}

	/**
	 * Get the decorated language.
	 * @return Language
	 */
	public Language getLanguage() {
		if (language == null) {
			setLanguage(Defaults.createLanguage());
		}
		return language;
	}

	/**
	 * Set the decorated language.
	 * @param language to set
	 */
	public void setLanguage(Language language) {
		this.language = language;
	}

}
