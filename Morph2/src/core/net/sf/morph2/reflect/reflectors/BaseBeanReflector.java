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

import net.sf.morph2.reflect.BeanReflector;

/**
 * Convenient base class for BeanReflectors.  Validates arguments and takes care
 * of logging and exception handling.
 * 
 * @author Matt Sgarlata
 * @since Nov 14, 2004
 */
public abstract class BaseBeanReflector extends BaseReflector implements BeanReflector {

	/**
	 * Template method must be overridden.
	 * {@inheritDoc}
	 */
	protected abstract String[] getPropertyNamesImpl(Object bean) throws Exception;

	/**
	 * Template method must be overridden.
	 * {@inheritDoc}
	 */
	protected abstract Class getTypeImpl(Object bean, String propertyName)
			throws Exception;

	/**
	 * Template method must be overridden.
	 * {@inheritDoc}
	 */
	protected abstract Object getImpl(Object bean, String propertyName) throws Exception;

	/**
	 * Template method must be overridden.
	 * {@inheritDoc}
	 */
	protected abstract void setImpl(Object bean, String propertyName, Object value)
			throws Exception;

	// isReadable and isWriteable are not marked abstract here because default
	// implementations are provided in BaseReflector

}
