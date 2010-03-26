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

import javax.servlet.http.HttpSession;

/**
 * Exposes HTTP session attributes.
 * 
 * @author Matt Sgarlata
 * @since Nov 21, 2004
 */
public class HttpSessionAttributeReflector extends BaseServletReflector {

	private static final Class[] REFLECTABLE_TYPES = new Class[] { HttpSession.class };

	/**
	 * Get the HttpSession from the specified Object (default, cast and return <code>bean</code>).
	 * @param bean
	 * @return HttpSession
	 */
	protected HttpSession getSession(Object bean) {
		return (HttpSession) bean;
	}

	/**
	 * {@inheritDoc}
	 */
	protected String[] getPropertyNamesImpl(Object bean) throws Exception {
		return enumerationToStringArray(getSession(bean).getAttributeNames());
	}

	/**
	 * {@inheritDoc}
	 */
	protected Object getImpl(Object bean, String propertyName) throws Exception {
		return getSession(bean).getAttribute(propertyName);
	}

	/**
	 * {@inheritDoc}
	 */
	protected void setImpl(Object bean, String propertyName, Object value) throws Exception {
		getSession(bean).setAttribute(propertyName, value);
	}

	/**
	 * {@inheritDoc}
	 */
	public Class[] getReflectableClassesImpl() {
		return REFLECTABLE_TYPES;
	}

}
