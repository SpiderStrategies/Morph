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
package net.sf.morph.lang.languages;

import java.util.Locale;

import net.sf.composite.util.ObjectUtils;
import net.sf.morph.Defaults;
import net.sf.morph.lang.DecoratedLanguage;
import net.sf.morph.lang.LanguageException;
import net.sf.morph.transform.Converter;
import net.sf.morph.transform.TransformationException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * A convenient base class for Languages that takes care of exception
 * handling and logging.  Also exposes the
 * {@link net.sf.morph.lang.DecoratedLanguage} interface while only requiring
 * subclasses to implement the methods in {@link net.sf.morph.lang.Language}.
 *
 * @author Matt Sgarlata
 * @since Nov 28, 2004
 */
public abstract class BaseLanguage implements DecoratedLanguage {

	private Converter converter;
	private String languageName;

	private transient Log log;

	/**
	 * Create a new BaseLanguage.
	 */
	public BaseLanguage() {
		super();
		establishLog(null);
	}

	/**
	 * Implement <code>isProperty(expression)</code>.
	 * @param expression
	 * @return boolean
	 * @throws Exception
	 */
	protected abstract boolean isPropertyImpl(String expression) throws Exception;

	/**
	 * Implement <code>getType(target, expression)</code>.
	 * @param target bean
	 * @param expression to read
	 * @return Class
	 * @throws Exception
	 */
	protected abstract Class getTypeImpl(Object target, String expression)
			throws Exception;

	/**
	 * Implement <code>get(target, expression)</code>.
	 * @param target bean
	 * @param expression to get
	 * @return Object
	 * @throws Exception
	 */
	protected abstract Object getImpl(Object target, String expression) throws Exception;

	/**
	 * Implement <code>set(target, expression, value)</code>.
	 * @param target bean
	 * @param expression to set
	 * @param value to set
	 * @throws Exception
	 */
	protected abstract void setImpl(Object target, String expression, Object value)
			throws Exception;

	/**
	 * {@inheritDoc}
	 */
	public final boolean isProperty(String expression) throws LanguageException {
		try {
			boolean isProperty = isPropertyImpl(expression);

			if (log.isTraceEnabled()) {
				log.trace("Expression '"
					+ expression
					+ "' denotes a"
					+ (isProperty ? " simple property of an object"
						: "n expression that involves traversal of an object graph"));
			}
			return isProperty;
		}
		catch (LanguageException e) {
			throw e;
		}
		catch (Exception e) {
			throw new LanguageException("Could not determine if expression '"
				+ expression + "' references a nested property", e);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public final Class getType(Object target, String expression) throws LanguageException {
		if (target == null) {
			throw new LanguageException("The target object cannot be null");
		}
		if (log.isTraceEnabled()) {
			log.trace("Retrieving type of '" + expression + "' from target "
					+ ObjectUtils.getObjectDescription(target));
		}
		try {
			Class type = getTypeImpl(target, expression);
			return type == null ? Object.class : type;
		}
		catch (LanguageException e) {
			throw e;
		}
		catch (Exception e) {
			throw new LanguageException("Could not retrieve type of '"
					+ expression + "' from target "
					+ ObjectUtils.getObjectDescription(target), e);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public final Object get(Object target, String expression) throws LanguageException {
		if (target == null) {
			throw new LanguageException("The target object cannot be null");
		}
		if (log.isTraceEnabled()) {
			log.trace("Retrieving '" + expression + "' from target "
					+ ObjectUtils.getObjectDescription(target));
		}
		try {
			return getImpl(target, expression);
		}
		catch (LanguageException e) {
			throw e;
		}
		catch (Exception e) {
			throw new LanguageException("Could not retrieve '" + expression
					+ "' from target "
					+ ObjectUtils.getObjectDescription(target), e);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public final void set(Object target, String expression, Object value)
			throws LanguageException {
		set(target, expression, value, null);
	}

	/**
	 * {@inheritDoc}
	 */
	public final Object get(Object target, String expression, Class destinationClass)
			throws LanguageException, TransformationException {
		return get(target, expression, destinationClass, null);
	}

	/**
	 * {@inheritDoc}
	 */
	public final Object get(Object target, String expression, Class destinationClass,
			Locale locale) throws LanguageException, TransformationException {
		Object object = get(target, expression);
		return getConverter().convert(destinationClass, object, locale);
	}

	/**
	 * {@inheritDoc}
	 */
	public final Object get(Object target, String expression, Locale locale,
			Class destinationClass) throws LanguageException, TransformationException {
		return get(target, expression, destinationClass, locale);
	}

	/**
	 * {@inheritDoc}
	 */
	public void set(Object target, String expression, Object value, Locale locale)
			throws LanguageException, TransformationException {

		if (target == null) {
			throw new LanguageException("The target object cannot be null");
		}
		if (log.isTraceEnabled()) {
			log.trace("Setting '" + expression + "' to "
				+ ObjectUtils.getObjectDescription(value) + " on target "
				+ ObjectUtils.getObjectDescription(target));
		}
		// first do any needed type conversion
		Class type = getType(target, expression);
		Object converted = type.isInstance(value) ? value : getConverter()
				.convert(type, value, locale);

		try {
			setImpl(target, expression, converted);
		}
		catch (LanguageException e) {
			throw e;
		}
		catch (Exception e) {
			throw new LanguageException("Could not set '" + expression
					+ "' to " + ObjectUtils.getObjectDescription(value)
					+ " on target " + ObjectUtils.getObjectDescription(target),
					e);
		}
	}

	/**
	 * Get the Converter.
	 * @return Converter
	 */
	public Converter getConverter() {
		if (converter == null) {
			setConverter(Defaults.createConverter());
		}
		return converter;
	}

	/**
	 * Set the Converter.
	 * @param converter to set
	 */
	public void setConverter(Converter converter) {
		this.converter = converter;
	}

	/**
	 * Get the languageName.
	 * @return String
	 */
	public String getLanguageName() {
		return languageName;
	}

	/**
	 * Set the languageName.
	 * @param languageName the String to set
	 */
	public void setLanguageName(String languageName) {
		if (ObjectUtils.equals(languageName, this.languageName)) {
			return;
		}
		this.languageName = languageName;
		establishLog(languageName);
	}

	private void establishLog(String name) {
		log = name == null ? LogFactory.getLog(getClass()) : LogFactory.getLog(name);
	}

	/**
	 * {@inheritDoc}
	 */
	public String toString() {
		String name = getLanguageName();
		return name == null ? super.toString() : name;
	}
}