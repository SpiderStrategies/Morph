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
package net.sf.morph2.integration.commons.collections;

import java.util.Iterator;
import java.util.Locale;
import java.util.Map;

import net.sf.composite.util.ObjectUtils;
import net.sf.morph2.Defaults;
import net.sf.morph2.transform.DecoratedConverter;

import org.apache.commons.collections.Transformer;

/**
 * Adapts a Morph DecoratedConverter to the org.apache.commons.collections.Transformer interface.
 * @author mbenson
 * @since Morph 1.1
 */
public class DecoratedConverterToTransformerAdapter implements Transformer {
	private Class destinationClass;
	private Map destinationClassMap;
	private DecoratedConverter delegate;
	private Locale locale;

	/**
	 * Create a new DecoratedConverterToTransformerAdapterTestCase.
	 */
	public DecoratedConverterToTransformerAdapter() {
	}

	/**
	 * Create a new DecoratedConverterToTransformerAdapterTestCase.
	 * @param delegate
	 */
	public DecoratedConverterToTransformerAdapter(DecoratedConverter delegate) {
		this();
		setDelegate(delegate);
	}

	/**
	 * Create a new DecoratedConverterToTransformerAdapter.
	 * @param delegate
	 * @param destinationClass
	 */
	public DecoratedConverterToTransformerAdapter(DecoratedConverter delegate, Class destinationClass) {
		this(delegate);
		setDestinationClass(destinationClass);
	}

	/**
	 * Create a new DecoratedConverterToTransformerAdapter.
	 * @param destinationClass
	 */
	public DecoratedConverterToTransformerAdapter(Class destinationClass) {
		this();
		setDestinationClass(destinationClass);
	}

	/**
	 * {@inheritDoc}
	 * @see org.apache.commons.collections.Transformer#transform(java.lang.Object)
	 */
	public Object transform(Object source) {
		return getDelegate().convert(determineDestinationClass(source), source, locale);
	}

	private Class determineDestinationClass(Object source) {
		if (!ObjectUtils.isEmpty(destinationClassMap)) {
			for (Iterator it = destinationClassMap.keySet().iterator(); it.hasNext();) {
				Class c = (Class) it.next();
				if (c.isAssignableFrom(source.getClass())) {
					return (Class) destinationClassMap.get(c);
				}
			}
		}
		return destinationClass == null ? Object.class : destinationClass;
	}

	/**
	 * Get the DecoratedConverter delegate.
	 * @return DecoratedConverter
	 */
	public synchronized DecoratedConverter getDelegate() {
		if (delegate == null) {
			setDelegate(Defaults.createConverter());
		}
		return delegate;
	}

	/**
	 * Set the DecoratedConverter delegate.
	 * @param delegate DecoratedConverter
	 */
	public synchronized void setDelegate(DecoratedConverter delegate) {
		this.delegate = delegate;
	}

	/**
	 * Get the Class destinationClass.
	 * @return Class
	 */
	public Class getDestinationClass() {
		return destinationClass;
	}

	/**
	 * Set the Class destinationClass.
	 * @param destinationClass Class
	 */
	public void setDestinationClass(Class destinationClass) {
		this.destinationClass = destinationClass;
	}

	/**
	 * Get the Map destinationClassMap.
	 * @return Map
	 */
	public Map getDestinationClassMap() {
		return destinationClassMap;
	}

	/**
	 * Set the Map destinationClassMap.
	 * @param destinationClassMap Map
	 */
	public void setDestinationClassMap(Map destinationClassMap) {
		this.destinationClassMap = destinationClassMap;
	}

	/**
	 * Get the Locale locale.
	 * @return Locale
	 */
	public Locale getLocale() {
		return locale;
	}

	/**
	 * Set the Locale locale.
	 * @param locale Locale
	 */
	public void setLocale(Locale locale) {
		this.locale = locale;
	}
}
