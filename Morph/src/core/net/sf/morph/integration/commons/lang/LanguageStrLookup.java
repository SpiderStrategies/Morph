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
package net.sf.morph.integration.commons.lang;

import net.sf.morph.Defaults;
import net.sf.morph.lang.DecoratedLanguage;

import org.apache.commons.lang.text.StrLookup;

/**
 * Implement StrLookup using a Morph Language and a source object.
 *
 * @author Matt Benson
 * @since Morph 1.1
 */
public class LanguageStrLookup extends StrLookup {
	private Object source;
	private DecoratedLanguage language;

	/**
	 * Create a new LanguageStrLookup.
	 * @param source Object
	 */
	public LanguageStrLookup(Object source) {
		this.source = source;
	}

	/**
	 * Create a new LanguageStrLookup.
	 * @param source Object
	 * @param language DecoratedLanguage
	 */
	public LanguageStrLookup(Object source, DecoratedLanguage language) {
		this(source);
		this.language = language;
	}

	/**
	 * Resolve the specified "property".
	 * @param key String
	 */
	public String lookup(String key) {
		return (String) getLanguage().get(source, key, String.class);
	}

	/**
	 * Get the language.
	 * @return the language
	 */
	public synchronized DecoratedLanguage getLanguage() {
		if (language == null) {
			setLanguage(Defaults.createLanguage());
		}
		return language;
	}

	/**
	 * Set the language.
	 * @param language the language to set
	 */
	public synchronized void setLanguage(DecoratedLanguage language) {
		this.language = language;
	}

}
