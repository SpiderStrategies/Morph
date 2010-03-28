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
package net.sf.morph2.transform.converters;

import java.util.Locale;

import net.sf.composite.util.ObjectUtils;
import net.sf.morph2.transform.DecoratedConverter;
import net.sf.morph2.transform.transformers.BaseTransformer;

/**
 * Converts any object to a Boolean.  Empty objects will be converted to
 * <code>false</code> and non-empty objects will be converted to
 * <code>true</code>.
 *
 * @author Matt Sgarlata
 * @since Dec 31, 2004
 * @see net.sf.morph2.util.MorphUtils#isEmpty(Object)
 */
public class ObjectToBooleanConverter extends BaseTransformer implements DecoratedConverter {

	private static final Class[] DESTINATION_TYPES = { Boolean.class,
		boolean.class };

	private static final Class[] SOURCE_TYPES = { Object.class, null };

	/**
	 * {@inheritDoc}
	 */
	protected Object convertImpl(Class destinationClass, Object source,
		Locale locale) throws Exception {

		return ObjectUtils.isEmpty(source) ? Boolean.FALSE : Boolean.TRUE;
	}

	/**
	 * {@inheritDoc}
	 */
	protected boolean isWrappingRuntimeExceptions() {
	    return true;
    }

	/**
	 * {@inheritDoc}
	 */
	protected Class[] getSourceClassesImpl() throws Exception {
		return SOURCE_TYPES;
	}

	/**
	 * {@inheritDoc}
	 */
	protected Class[] getDestinationClassesImpl() throws Exception {
		return DESTINATION_TYPES;
	}

	/**
	 * {@inheritDoc}
	 */
	protected boolean isAutomaticallyHandlingNulls() {
		return false;
	};
}
