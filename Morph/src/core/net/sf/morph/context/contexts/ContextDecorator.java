/*
 * Copyright 2004-2005 the original author or authors.
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
package net.sf.morph.context.contexts;

import net.sf.morph.Defaults;
import net.sf.morph.context.Context;
import net.sf.morph.context.ContextException;
import net.sf.morph.context.DecoratedContext;
import net.sf.morph.transform.Converter;

/**
 * Decorates any context so that it implements
 * {@link net.sf.morph.lang.DecoratedLanguage}.
 * 
 * @author Matt Sgarlata
 * @since Dec 5, 2004
 */
public class ContextDecorator extends BaseContext implements Context, DecoratedContext {
	
	private Converter converter;
	private Context context;
	
	public ContextDecorator() {
		super();
		this.converter = Defaults.createConverter();
	}
	
	public ContextDecorator(Context context) {
		this();
		this.context = context;
	}

	protected void checkInitialization() {
		
	}
	
	public String[] getPropertyNamesImpl() throws ContextException {
		checkInitialization();
		return context.getPropertyNames();
	}

	public Object getImpl(String propertyName) throws Exception {
		return getLanguage().get(context, propertyName);
	}

	public void setImpl(String propertyName, Object propertyValue)
		throws ContextException {
		getLanguage().set(context, propertyName, propertyValue);
	}

	/**
	 * @return Returns the context.
	 */
	public Context getContext() {
		return context;
	}
	/**
	 * @param context The context to set.
	 */
	public void setContext(Context context) {
		this.context = context;
	}
	/**
	 * @return Returns the converter.
	 */
	public Converter getConverter() {
		if (converter == null) {
			setConverter(Defaults.createConverter());
		}
		return converter;
	}
	/**
	 * @param converter The converter to set.
	 */
	public void setConverter(Converter converter) {
		this.converter = converter;
	}
	
}
