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

import javax.servlet.ServletRequest;

import net.sf.composite.util.ObjectUtils;
import net.sf.morph2.reflect.ReflectionException;
import net.sf.morph2.util.ContainerUtils;

/**
 * Exposes servlet request parameters.
 * 
 * @author Matt Sgarlata
 * @since Nov 21, 2004
 */
public class ServletRequestParameterReflector extends BaseServletReflector {

	private static final Class[] REFLECTABLE_TYPES = new Class[] {
		ServletRequest.class
	};
	
	private ServletRequest getRequest(Object bean) {
		return (ServletRequest) bean;
	}

	protected String[] getPropertyNamesImpl(Object bean) throws Exception {
		return enumerationToStringArray(getRequest(bean).getParameterNames());
	}

	protected Class getTypeImpl(Object bean, String propertyName)
		throws Exception {
		String[] values = getRequest(bean).getParameterValues(propertyName);
		return ObjectUtils.isEmpty(values) || values.length == 1 ? String.class : String[].class;
	}

	protected boolean isReadableImpl(Object bean, String propertyName)
		throws Exception {
		// all possible property names are readable.  If the property was not
		// part of the request, the Servlet API specifies that null will be
		// returned if request.getParameter is called
		// return ContainerUtils.contains(getPropertyNames(bean), propertyName);
		return true;
	}
	
	protected boolean isWriteableImpl(Object bean, String propertyName)
		throws Exception {
		return false;
	}

	protected Object getImpl(Object bean, String propertyName) throws Exception {
		String[] values = getRequest(bean).getParameterValues(propertyName);
		return ObjectUtils.isEmpty(values) || values.length == 1 ? (Object) getRequest(bean).getParameter(propertyName) : values;
	}

	protected void setImpl(Object bean, String propertyName, Object value)
		throws Exception {
		// won't actually ever throw this exception since isWriteable
		// always returns false
		throw new ReflectionException("Can't set HTTP request parameters");
	}
	
	public Class[] getReflectableClassesImpl() {
		return REFLECTABLE_TYPES;
	}
	
}
