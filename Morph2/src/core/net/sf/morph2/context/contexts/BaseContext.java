/*
 * Copyright 2004-2005, 2008 the original author or authors.
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
package net.sf.morph2.context.contexts;

import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import net.sf.composite.util.ObjectUtils;
import net.sf.morph2.Defaults;
import net.sf.morph2.context.Context;
import net.sf.morph2.context.ContextException;
import net.sf.morph2.context.DecoratedContext;
import net.sf.morph2.context.support.ContextMapBridge;
import net.sf.morph2.lang.Language;
import net.sf.morph2.transform.Converter;
import net.sf.morph2.util.ContainerUtils;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * <p>
 * Convenient base class for Contexts. Validates arguments and takes care of
 * logging and exception handling. Also implements the {@link java.util.Map}
 * interface.
 * </p>
 * 
 * <p>
 * As a Map, this class only supports non-null Strings for keys (i.e. - calling
 * <code>Map.put(new Object(), new Object())</code> will throw a
 * {@link net.sf.morph2.context.ContextException}).
 * </p>
 * 
 * @author Matt Sgarlata
 * @since Nov 19, 2004
 */
public abstract class BaseContext implements Context, Map, DecoratedContext {

	private static final ContextMapBridge DEFAULT_CONTEXT_MAP_BRIDGE = new ContextMapBridge();

	private transient Log log = LogFactory.getLog(getClass());

	private ContextMapBridge contextMapBridge;
	private Converter converter;
	private Language language;

	/**
	 * Create a new BaseContext.
	 */
	public BaseContext() {
		super();
		setContextMapBridge(DEFAULT_CONTEXT_MAP_BRIDGE);
		setConverter(Defaults.createConverter());
		setLanguage(Defaults.createLanguage());
	}

	/**
	 * Implement getPropertyNames()
	 * @return String[] of property names
	 * @throws Exception if errors occur
	 */
	protected abstract String[] getPropertyNamesImpl() throws Exception;

	/**
	 * Implement <code>get(propertyName)</code>.
	 * @param propertyName to get
	 * @return Object value
	 * @throws Exception in case of errors
	 */
	protected abstract Object getImpl(String propertyName)
		throws Exception;

	/**
	 * Implement <code>set(propertyName, propertyValue)</code>.
	 * @param propertyName to set
	 * @param propertyValue to set
	 * @throws Exception in case of errors
	 */
	protected abstract void setImpl(String propertyName, Object propertyValue)
		throws Exception;

	/**
	 * {@inheritDoc}
	 */
	public final String[] getPropertyNames() throws ContextException {
		try {
			// retrieve the property names
			String[] cachedPropertyNames = getPropertyNamesImpl();
			Set propertyNames = ContainerUtils.createOrderedSet();
			if (!ObjectUtils.isEmpty(cachedPropertyNames)) {
				propertyNames.addAll(Arrays.asList(cachedPropertyNames));
			}
//			// include the propertyNames property
//			int size = cachedPropertyNames == null ? 0 : cachedPropertyNames.length;
//			Set propertyNames = new ContainerUtils.createOrderedSet(size+1);
//			propertyNames.add(PROPERTY_NAMES_PROPERTY);
//			if (!ObjectUtils.isEmpty(cachedPropertyNames)) {
//				propertyNames.addAll(Arrays.asList(cachedPropertyNames));
//			}
			// loop through and remove properties that aren't valid according to
			// the language
			Iterator iterator = propertyNames.iterator();
			while (iterator.hasNext()) {
				String propertyName = (String) iterator.next();
				if (!getLanguage().isProperty(propertyName)) {
					iterator.remove();
				}
			}
			return (String[]) propertyNames.toArray(new String[propertyNames.size()]);
		}
		catch (ContextException e) {
			throw e;
		}
		catch (Exception e) {
			throw new ContextException("Unable to retrieve property names", e);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public final Object get(String expression) throws ContextException {
		if (getLanguage().isProperty(expression)) {
			// make sure a property name is specified
			if (ObjectUtils.isEmpty(expression)) {
				throw new ContextException("You must specify a propertyName to retrieve");
			}
			// make sure the propertyName is a valid property
			String[] propertyNames = getPropertyNames();		
			if (!(ContainerUtils.contains(propertyNames, expression))) {
				return null;
			}
			try {
//				Object returnVal = getImpl(expression);
//				// exposes propertyNames as a property of the Map, if desired
//				if (returnVal == null &&
//					PROPERTY_NAMES_PROPERTY.equals(expression)) {
//					return getPropertyNames();
//				}
				return getImpl(expression);
			}
			catch (ContextException e) {
				throw e;
			}
			catch (Exception e) {
				throw new ContextException("Could not retrieve property '" + expression + "' from this context", e);
			}
		}
		return getLanguage().get(this, expression);
	}

	/**
	 * {@inheritDoc}
	 */
	public final void set(String expression, Object value) throws ContextException {
		if (getLanguage().isProperty(expression)) {
			// make sure a property name is specified
			if (ObjectUtils.isEmpty(expression)) {
				throw new ContextException("You must specify an expression to set");
			}
//			// make sure the propertyName is a valid property
//			String[] propertyNames = getPropertyNames();		
//			if (!(MorphUtils.contains(propertyNames, expression))) {
//				return null;
//			}
			try {
				setImpl(expression, value);
			}
			catch (ContextException e) {
				throw e;
			}
			catch (Exception e) {
				throw new ContextException("Could not set property '" + expression + "' in context " + ObjectUtils.getObjectDescription(this), e);
			}
		}
		else {
			getLanguage().set(this, expression, value);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public final Object get(String expression, Class destinationClass)
			throws ContextException {
		return get(expression, destinationClass, Locale.getDefault());
	}

	/**
	 * {@inheritDoc}
	 */
	public final Object get(String expression, Class destinationClass, Locale locale)
			throws ContextException {
		try {
			Object object = get(expression);
			return getConverter().convert(destinationClass, object, locale);
		}
		catch (Exception e) {
			throw new ContextException("Unable to retrieve value for expression '" + expression + "' as destination " + ObjectUtils.getObjectDescription(destinationClass), e);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public final Object get(String expression, Locale locale, Class destinationClass)
			throws ContextException {
		return get(expression, destinationClass, locale);
	}

	/**
	 * {@inheritDoc}
	 */
	public final void set(String expression, Object value, Locale locale)
			throws ContextException {
		if (ObjectUtils.isEmpty(expression)) {
			throw new ContextException("You must specify a propertyName to set");
		}

		try {
			setImpl(expression, value);
		}
		catch (ContextException e) {
			throw e;
		}
		catch (Exception e) {
			throw new ContextException("Could not set '" + expression + "' to " + ObjectUtils.getObjectDescription(value), e);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public void clear() {
		getContextMapBridge().clear(this);
	}

	/**
	 * The implementation of this method has O(n) time complexity.
	 * @see java.util.Map#containsKey(java.lang.Object)
	 */
	public boolean containsKey(Object key) {
		return getContextMapBridge().containsKey(this, key);
	}

	/**
	 * {@inheritDoc}
	 */
	public boolean containsValue(Object value) {
		return getContextMapBridge().containsValue(this, value);
	}

	/**
	 * {@inheritDoc}
	 */
	public Set entrySet() {
		return getContextMapBridge().entrySet(this);
	}

	/**
	 * {@inheritDoc}
	 */
	public Object get(Object key) {
		return getContextMapBridge().get(this, key);
	}

	/**
	 * {@inheritDoc}
	 */
	public boolean isEmpty() {
		return getContextMapBridge().isEmpty(this);
	}

	/**
	 * {@inheritDoc}
	 */
	public Set keySet() {
		return getContextMapBridge().keySet(this);
	}

	/**
	 * {@inheritDoc}
	 */
	public Object put(Object key, Object value) {
		return getContextMapBridge().put(this, key, value);
	}

	/**
	 * {@inheritDoc}
	 */
	public void putAll(Map t) {
		getContextMapBridge().putAll(this, t);
	}

	/**
	 * {@inheritDoc}
	 */
	public Object remove(Object key) {
		return getContextMapBridge().remove(this, key);
	}

	/**
	 * {@inheritDoc}
	 */
	public int size() {
		return getContextMapBridge().size(this);
	}

	/**
	 * {@inheritDoc}
	 */
	public Collection values() {
		return getContextMapBridge().values(this);
	}

	/**
	 * Get the converter.
	 * @return Returns the converter.
	 */
	public Converter getConverter() {
		if (converter == null) {
			setConverter(Defaults.createConverter());
		}
		return converter;
	}

	/**
	 * Set the converter.
	 * @param converter The converter to set.
	 */
	public void setConverter(Converter converter) {
		this.converter = converter;
	}

	/**
	 * Get the Language.
	 * @return Returns the language.
	 */
	public Language getLanguage() {
		if (language == null) {
			setLanguage(Defaults.createLanguage());
		}
		return language;
	}

	/**
	 * Set the Language.
	 * @param language The language to set.
	 */
	public void setLanguage(Language language) {
		this.language = language;
	}

	/**
	 * Set the ContextMapBridge.
	 * @param contextMapBridge to set
	 */
	public void setContextMapBridge(ContextMapBridge contextMapBridge) {
		this.contextMapBridge = contextMapBridge;
	}

	/**
	 * Get the ContextMapBridge. 
	 * @return ContextMapBridge
	 */
	public ContextMapBridge getContextMapBridge() {
		return contextMapBridge == null ? DEFAULT_CONTEXT_MAP_BRIDGE : contextMapBridge;
	}

	/**
	 * Get the log.
 	 * @return Log
	 */
	protected Log getLog() {
		return log;
	}

	/**
	 * Set the Log.
	 * @param log to set
	 */
	protected void setLog(Log log) {
		this.log = log;
	}

// this causes an infinite loop
//	public String toString() {
//		return (new HashMap(this)).toString();
//	}

}
