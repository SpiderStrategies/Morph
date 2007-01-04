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

import javax.servlet.ServletRequest;

/**
 * Exposes servlet request attributes.
 * 
 * @author Matt Sgarlata
 * @since Nov 21, 2004
 */
public class ServletRequestAttributeReflector extends BaseServletReflector {

	private static final Class[] REFLECTABLE_TYPES = new Class[] {
		ServletRequest.class
	};
	
	protected ServletRequest getRequest(Object bean) {
		return (ServletRequest) bean;
	}

	protected String[] getPropertyNamesImpl(Object bean) throws Exception {
		return enumerationToStringArray(getRequest(bean).getAttributeNames());
	}

	protected Object getImpl(Object bean, String propertyName) throws Exception {
		return getRequest(bean).getAttribute(propertyName);
	}
	
	protected void setImpl(Object bean, String propertyName, Object value)
		throws Exception {
		getRequest(bean).setAttribute(propertyName, value);
	}

	public Class[] getReflectableClassesImpl() {
		return REFLECTABLE_TYPES;
	}
	
}
