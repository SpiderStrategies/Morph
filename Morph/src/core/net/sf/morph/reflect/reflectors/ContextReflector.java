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
package net.sf.morph.reflect.reflectors;

import net.sf.morph.context.Context;
import net.sf.morph.context.contexts.MapContext;
import net.sf.morph.reflect.InstantiatingReflector;

/**
 * A reflector that can expose the properties of any Context.
 * 
 * @author Matt Sgarlata
 * @since Nov 28, 2004
 */
public class ContextReflector extends BaseBeanReflector implements InstantiatingReflector {

	private static final Class[] REFLECTABLE_TYPES = new Class[] {
		Context.class
	};
	
	private Context getContext(Object bean) {
		return (Context) bean;
	}
	
//	private ReflectorContext getReflectorContext(Object bean) {
//		return (ReflectorContext) bean;
//	}
	
	public Class[] getReflectableClassesImpl() {
		return REFLECTABLE_TYPES;
	}
	
	protected String[] getPropertyNamesImpl(Object bean) throws Exception {
//		if (bean instanceof ReflectorContext) {
//			return getReflectorContext(bean).getBeanReflector().getPropertyNames(bean);
//		}
//		else {
			return getContext(bean).getPropertyNames();
//		}
	}

	protected Class getTypeImpl(Object bean, String propertyName)
		throws Exception {
//		if (bean instanceof ReflectorContext) {
//			return getReflectorContext(bean).getBeanReflector().getType(bean, propertyName);
//		}
//		else {
			Object object = getContext(bean).get(propertyName);
			return object == null ? null : object.getClass();
//		}
	}

	/**
	 * Returns <code>true</code> because all properties of a context are
	 * considered readable. If the property isn't a defined property returned by
	 * getPropertyNames, <code>null</code> is returned when the
	 * {@link net.sf.morph.reflect.BeanReflector#get(Object, String)} method is
	 * called.
	 */
	protected boolean isReadableImpl(Object bean, String propertyName)
		throws Exception {
		return true;
	}

	/**
	 * Returns <code>true</code> because all properties in a context are
	 * assumed to be writeable. If they're not (e.g. - on an Object has been
	 * exposed as a Context), an exception is thrown when the set method is
	 * called on the context.
	 */
	protected boolean isWriteableImpl(Object bean, String propertyName)
		throws Exception {
		return true;
	}

	protected Object getImpl(Object bean, String propertyName) throws Exception {
		return getContext(bean).get(propertyName);
	}

	protected void setImpl(Object bean, String propertyName, Object value)
		throws Exception {
		getContext(bean).set(propertyName, value);
	}

	protected Object newInstanceImpl(Class clazz) throws Exception {
		if (clazz.equals(Context.class)) {
			return new MapContext();
		}
		else {
			return super.newInstanceImpl(clazz);
		}
	}
}
