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

import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import net.sf.morph2.transform.DecoratedConverter;
import net.sf.morph2.transform.TransformationException;
import net.sf.morph2.transform.transformers.BaseTransformer;

/**
 * Converts information from one of the basic time types to the other. The basic
 * time types are {@link java.util.Date} and {@link java.util.Calendar}.
 * 
 * @author Matt Sgarlata
 * @since Jan 4, 2005
 */
public class TimeConverter extends BaseTransformer implements DecoratedConverter {

	private static final Class[] SOURCE_AND_DESTINATION_TYPES = { Date.class, Calendar.class };

	/**
	 * {@inheritDoc}
	 */
	protected Object convertImpl(Class destinationClass, Object source, Locale locale) throws Exception {
		if (destinationClass.isInstance(source)) {
			return source instanceof Date ? ((Date) source).clone() : ((Calendar) source).clone();
		}
		if (Date.class.isAssignableFrom(destinationClass)) {
			return ((Calendar) source).getTime();
		}
		if (Calendar.class.isAssignableFrom(destinationClass)) {
			Calendar calendar = Calendar.getInstance();
			calendar.setTime((Date) source);
			return calendar;
		}
		throw new TransformationException(destinationClass, source);
	}

	/**
	 * {@inheritDoc}
	 */
	protected Class[] getSourceClassesImpl() throws Exception {
		return SOURCE_AND_DESTINATION_TYPES;
	}

	/**
	 * {@inheritDoc}
	 */
	protected Class[] getDestinationClassesImpl() throws Exception {
		return SOURCE_AND_DESTINATION_TYPES;
	}

	/**
	 * {@inheritDoc}
	 */
	protected boolean isWrappingRuntimeExceptions() {
	    return true;
    }

}
