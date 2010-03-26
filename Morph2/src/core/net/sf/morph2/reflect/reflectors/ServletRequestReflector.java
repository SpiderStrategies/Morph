/*
 * Copyright 2004-2005, 2007-2008, 2010 the original author or authors.
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

import javax.servlet.http.HttpServletRequest;

import net.sf.composite.util.ObjectUtils;
import net.sf.morph2.reflect.Reflector;
import net.sf.morph2.util.ClassUtils;

/**
 * ServletRequest reflector.
 * 
 * @author Matt Sgarlata
 * @since Morph 1.1 (Oct 25, 2007)
 */
public class ServletRequestReflector extends StubbornDelegatingReflector {

	/**
	 * Create a new ServletRequestReflector instance.
	 */
	public ServletRequestReflector() {
		super(new Reflector[] {
				new ServletRequestParameterReflector(), new ServletRequestAttributeReflector() });
	}

	/**
	 * {@inheritDoc}
	 */
	protected Class getTypeImpl(Object bean, String propertyName) throws Exception {
		HttpServletRequest request = (HttpServletRequest) bean;

		// first check request parameters
		String[] values = request.getParameterValues(propertyName);
		if (!ObjectUtils.isEmpty(values)) {
			return ObjectUtils.isEmpty(values) || values.length == 1 ? String.class
					: String[].class;
		}

		// next check request attributes
		Object attr = request.getAttribute(propertyName);
		if (attr != null) {
			return ClassUtils.getClass(attr);
		}

		// if neither a parameter nor an attribute is present, just return
		// Object.class
		return Object.class;
	}

}
