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

import javax.servlet.jsp.PageContext;

import net.sf.morph2.reflect.ReflectionException;

/**
 * Exposes page context attributes.
 * 
 * @author Matt Sgarlata
 * @since Dec 4, 2004
 */
public class PageContextAttributeReflector extends BaseServletReflector {

	private static final Class[] REFLECTABLE_TYPES = new Class[] { PageContext.class };

	/**
	 * Get the PageContext associated with the specified Object (base implementation is to cast <code>bean</code>).
	 * @param bean
	 * @return PageContext
	 */
	protected PageContext getPageContext(Object bean) {
		return (PageContext) bean;
	}

	/**
	 * {@inheritDoc}
	 */
	protected String[] getPropertyNamesImpl(Object bean) throws Exception {
		return enumerationToStringArray(getPageContext(bean).getAttributeNamesInScope(
				PageContext.PAGE_SCOPE));
	}

	/**
	 * {@inheritDoc}
	 */
	protected Object getImpl(Object bean, String propertyName) throws Exception {
		return getPageContext(bean).getAttribute(propertyName);
	}

	/**
	 * {@inheritDoc}
	 */
	protected void setImpl(Object bean, String propertyName, Object value) throws Exception {
		if (value == null) {
			throw new ReflectionException("Cannot set null for property '" + propertyName
					+ "' because null values are not allowed for " + PageContext.class.getName()
					+ " attributes");
		}
		getPageContext(bean).setAttribute(propertyName, value);
	}

	/**
	 * {@inheritDoc}
	 */
	public Class[] getReflectableClassesImpl() {
		return REFLECTABLE_TYPES;
	}

}
