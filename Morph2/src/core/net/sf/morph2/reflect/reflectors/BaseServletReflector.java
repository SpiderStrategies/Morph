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

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

/**
 * Base class for reflectors that reflect objects from the Servlet API. 
 * 
 * @author Matt Sgarlata
 * @since Nov 30, 2004
 */
public abstract class BaseServletReflector extends BaseBeanReflector {

	/**
	 * Turn an Enumeration <code>e</code> into a string array.
	 * @param e
	 * @return String[]
	 */
	protected String[] enumerationToStringArray(Enumeration e) {
		// don't use a converter here for performance issues (reflectors are
		// low level components so they need high performance) and because
		// it's better not to have reflectors depending on converters since
		// reflectors are lower on the call stack
		List list = new ArrayList();
		while (e.hasMoreElements()) {
			list.add(e.nextElement());
		}
		return (String[]) list.toArray(new String[list.size()]);
	}

	/**
	 * {@inheritDoc}
	 */
	protected boolean isReadableImpl(Object bean, String propertyName) throws Exception {
		return true;
	}

	/**
	 * {@inheritDoc}
	 */
	protected Class getTypeImpl(Object bean, String propertyName) throws Exception {
		return Object.class;
	}

}
