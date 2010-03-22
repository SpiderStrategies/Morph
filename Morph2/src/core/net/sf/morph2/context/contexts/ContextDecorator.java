/*
 * Copyright 2004-2005, 2010 the original author or authors.
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

import net.sf.morph2.Defaults;
import net.sf.morph2.context.Context;
import net.sf.morph2.context.ContextException;
import net.sf.morph2.context.DecoratedContext;
import net.sf.morph2.transform.Converter;

/**
 * Decorates any context so that it implements {@link DecoratedContext}.
 * 
 * @author Matt Sgarlata
 * @since Dec 5, 2004
 */
public class ContextDecorator extends BaseContext implements Context, DecoratedContext {

	private Converter converter;
	private Context context;

	/**
	 * Create a new ContextDecorator instance.
	 */
	public ContextDecorator() {
		super();
		this.converter = Defaults.createConverter();
	}

	/**
	 * Create a new ContextDecorator instance.
	 * @param context
	 */
	public ContextDecorator(Context context) {
		this();
		this.context = context;
	}

	/**
	 * Check initialization.
	 */
	protected void checkInitialization() {
	}

	/**
	 * {@inheritDoc}
	 */
	public String[] getPropertyNamesImpl() throws ContextException {
		checkInitialization();
		return context.getPropertyNames();
	}

	/**
	 * {@inheritDoc}
	 */
	public Object getImpl(String propertyName) throws Exception {
		return getLanguage().get(context, propertyName);
	}

	/**
	 * {@inheritDoc}
	 */
	public void setImpl(String propertyName, Object propertyValue) throws ContextException {
		getLanguage().set(context, propertyName, propertyValue);
	}

	/**
	 * Get the context of this ContextDecorator.
	 * @return the context
	 */
	public Context getContext() {
		return context;
	}

	/**
	 * Set the context of this ContextDecorator.
	 * @param context the Context to set
	 */
	public void setContext(Context context) {
		this.context = context;
	}

	/**
	 * Get the converter of this ContextDecorator.
	 * @return the converter
	 */
	public Converter getConverter() {
		return converter;
	}

	/**
	 * Set the converter of this ContextDecorator.
	 * @param converter the Converter to set
	 */
	public void setConverter(Converter converter) {
		this.converter = converter;
	}

}
