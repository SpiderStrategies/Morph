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
package net.sf.morph2.reflect.reflectors;

import net.sf.morph2.context.Context;
import net.sf.morph2.context.contexts.MapContext;
import net.sf.morph2.reflect.InstantiatingReflector;
import net.sf.morph2.util.ClassUtils;

/**
 * A reflector that can expose the properties of any Context.
 * 
 * @author Matt Sgarlata
 * @since Nov 28, 2004
 */
public class ContextReflector extends BaseBeanReflector implements InstantiatingReflector {

	private static final Class[] REFLECTABLE_TYPES = new Class[] { Context.class };

	private Context getContext(Object bean) {
		return (Context) bean;
	}

	/**
	 * {@inheritDoc}
	 */
	public Class[] getReflectableClassesImpl() {
		return REFLECTABLE_TYPES;
	}

	/**
	 * {@inheritDoc}
	 */
	protected String[] getPropertyNamesImpl(Object bean) throws Exception {
		return getContext(bean).getPropertyNames();
	}

	/**
	 * {@inheritDoc}
	 */
	protected Class getTypeImpl(Object bean, String propertyName) throws Exception {
		return ClassUtils.getClass(getContext(bean).get(propertyName));
	}

	/**
	 * Returns <code>true</code> because all properties of a context are
	 * considered readable. If the property isn't a defined property returned by
	 * getPropertyNames, <code>null</code> is returned when the
	 * {@link net.sf.morph2.reflect.BeanReflector#get(Object, String)} method is
	 * called.
	 */
	protected boolean isReadableImpl(Object bean, String propertyName) throws Exception {
		return true;
	}

	/**
	 * Returns <code>true</code> because all properties in a context are
	 * assumed to be writeable. If they're not (e.g. - on an Object has been
	 * exposed as a Context), an exception is thrown when the set method is
	 * called on the context.
	 */
	protected boolean isWriteableImpl(Object bean, String propertyName) throws Exception {
		return true;
	}

	/**
	 * {@inheritDoc}
	 */
	protected Object getImpl(Object bean, String propertyName) throws Exception {
		return getContext(bean).get(propertyName);
	}

	/**
	 * {@inheritDoc}
	 */
	protected void setImpl(Object bean, String propertyName, Object value) throws Exception {
		getContext(bean).set(propertyName, value);
	}

	/**
	 * {@inheritDoc}
	 */
	protected Object newInstanceImpl(Class clazz, Object parameters) throws Exception {
		return clazz == Context.class ? new MapContext() : super.newInstanceImpl(clazz, parameters);
	}
}
