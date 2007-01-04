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

import javax.servlet.ServletContext;

/**
 * Exposes servlet context attributes.
 * 
 * @author Matt Sgarlata
 * @since Nov 21, 2004
 */
public class ServletContextAttributeReflector extends BaseServletReflector {
	
//	public static final String PSEUDO_PROPERTY_INIT_PARAMETER_NAMES = "initParameterNames";
//	public static final String PSEUDO_PROPERTY_MAJOR_VERSION = "majorVersion";
//	public static final String PSEUDO_PROPERTY_MINOR_VERSION = "minorVersion";
//	public static final String PSEUDO_PROPERTY_SERRVER_INFO = "serverInfo";
//	public static final String PSEUDO_PROPERTY_SERVLET_CONTEXT_NAME = "servletContextName";

	private static final Class[] REFLECTABLE_TYPES = new Class[] {
		ServletContext.class
	};
	
	protected ServletContext getServletContext(Object bean) {
		return (ServletContext) bean;
	}
	
	protected String[] getPropertyNamesImpl(Object bean) throws Exception {
		return enumerationToStringArray(getServletContext(bean).getAttributeNames());
	}

	protected Object getImpl(Object bean, String propertyName) throws Exception {
		return getServletContext(bean).getAttribute(propertyName);
	}

	protected void setImpl(Object bean, String propertyName, Object value) throws Exception {
		getServletContext(bean).setAttribute(propertyName, value);
	}

	public Class[] getReflectableClassesImpl() {
		return REFLECTABLE_TYPES;
	}
	
}
