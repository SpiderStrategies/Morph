/*
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

import java.util.Enumeration;

import javax.servlet.ServletContext;

/**
 * Exposes the init-parameters of a ServletContext.
 * 
 * @author Matt Sgarlata
 * @since Dec 21, 2004
 */
public class ServletContextInitParameterReflector extends BaseServletReflector {

	private static final Class[] REFLECTABLE_TYPES = new Class[] { ServletContext.class };

	/**
	 * Get the ServletContext associated with the specified Object (default implementation: cast <code>bean</code>).
	 * @param bean
	 * @return {@link ServletContext}
	 */
	protected ServletContext getServletContext(Object bean) {
		return (ServletContext) bean;
	}

	/**
	 * {@inheritDoc}
	 */
	protected String[] getPropertyNamesImpl(Object bean) throws Exception {
		Enumeration initParameterNames = getServletContext(bean).getInitParameterNames();
		return enumerationToStringArray(initParameterNames);
	}

	/**
	 * {@inheritDoc}
	 */
	protected Object getImpl(Object bean, String propertyName) throws Exception {
		return getServletContext(bean).getInitParameter(propertyName);
	}

	/**
	 * {@inheritDoc}
	 */
	protected void setImpl(Object bean, String propertyName, Object value) throws Exception {
		throw new UnsupportedOperationException();
	}

	/**
	 * {@inheritDoc}
	 */
	protected boolean isWriteableImpl(Object bean, String propertyName) throws Exception {
		return false;
	}

	/**
	 * {@inheritDoc}
	 */
	protected Class[] getReflectableClassesImpl() throws Exception {
		return REFLECTABLE_TYPES;
	}

}
