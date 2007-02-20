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
package net.sf.morph.lang.languages;

import net.sf.morph.Defaults;
import net.sf.morph.lang.DecoratedLanguage;
import net.sf.morph.lang.Language;
import net.sf.morph.lang.LanguageException;

/**
 * Decorates any language so that it implements
 * {@link net.sf.morph.lang.DecoratedLanguage}.
 * 
 * @author Matt Sgarlata
 * @since Nov 28, 2004
 */
public class LanguageDecorator extends BaseLanguage implements DecoratedLanguage {
	
	private Language language;
	
	public LanguageDecorator() {
		super();
	}
	
	public LanguageDecorator(Language language) {
		this();
		this.language = language;
	}

	protected Object getImpl(Object target, String expression)
		throws LanguageException {
		return getLanguage().get(target, expression);
	}
	protected Class getTypeImpl(Object target, String expression)
		throws LanguageException {
		return getLanguage().getType(target, expression);
	}

	protected void setImpl(Object target, String expression, Object value) throws Exception {
		getLanguage().set(target, expression, value);
	}
	
	protected boolean isPropertyImpl(String expression) {
		return getLanguage().isProperty(expression);
	}

	public Language getLanguage() {
		if (language == null) {
			setLanguage(Defaults.createLanguage());
		}
		return language;
	}
	public void setLanguage(Language language) {
		this.language = language;
	}

}