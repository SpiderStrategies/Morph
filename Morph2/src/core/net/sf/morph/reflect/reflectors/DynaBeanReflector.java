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
package net.sf.morph.reflect.reflectors;

import org.apache.commons.beanutils.DynaBean;
import org.apache.commons.beanutils.DynaProperty;

/**
 * @author Matt Sgarlata
 * @since Nov 20, 2004
 */
public class DynaBeanReflector extends BaseBeanReflector {

	/**
	 * "dynaClass" pseudo-property
	 */
	public static final String IMPLICIT_PROPERTY_DYNA_CLASS = "dynaClass";

	private static final Class[] REFLECTABLE_TYPES = new Class[] { DynaBean.class };

	/**
	 * Create a new DynaBeanReflector instance.
	 */
	public DynaBeanReflector() {
		super();
	}

	private DynaBean getDynaBean(Object bean) {
		return (DynaBean) bean;
	}

	/**
	 * {@inheritDoc}
	 */
	protected String[] getPropertyNamesImpl(Object bean) throws Exception {
		// note that we are not removing the "class" implicit property because
		// there is no way to tell if this was an explicitly defined property
		// of the DynaBean or if it was just included because of reflection
		DynaProperty[] properties = getDynaBean(bean).getDynaClass().getDynaProperties();
		String[] propertyNames = new String[properties.length];
		for (int i = 0; i < properties.length; i++) {
			propertyNames[i] = properties[i].getName();
		}
		return propertyNames;
	}

	/**
	 * {@inheritDoc}
	 */
	protected Class getTypeImpl(Object bean, String propertyName) throws Exception {
		try {
			return getDynaBean(bean).getDynaClass().getDynaProperty(propertyName).getType();
		}
		// this is inelegant, but the DynaBean API isn't very expressive
		// so there's not much we can do
		catch (Exception e) {
			return Object.class;
		}
	}

	/**
	 * {@inheritDoc}
	 */
	protected boolean isReadableImpl(Object bean, String propertyName) throws Exception {
		// any property of the bean, including implicit properties, are assumed
		// to be readable, because there is no API in BeanUtils to determine
		// otherwise
		return IMPLICIT_PROPERTY_DYNA_CLASS.equals(propertyName)
				|| super.isReadableImpl(bean, propertyName);
	}

	/**
	 * {@inheritDoc}
	 */
	protected Object getImpl(Object bean, String propertyName) throws Exception {
		DynaBean dyna = getDynaBean(bean);
		return IMPLICIT_PROPERTY_DYNA_CLASS.equals(propertyName) ? dyna.getDynaClass() : dyna
				.get(propertyName);
	}

	/**
	 * {@inheritDoc}
	 */
	protected void setImpl(Object bean, String propertyName, Object value) throws Exception {
		getDynaBean(bean).set(propertyName, value);
	}

	/**
	 * {@inheritDoc}
	 */
	public Class[] getReflectableClassesImpl() {
		return REFLECTABLE_TYPES;
	}

}