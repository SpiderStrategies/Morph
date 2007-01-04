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
package net.sf.morph.transform.converters;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

import net.sf.morph.transform.Converter;
import net.sf.morph.transform.DecoratedConverter;
import net.sf.morph.transform.TransformationException;
import net.sf.morph.transform.transformers.BaseTransformer;

/**
 * Converts information from one of the basic time types to the other. The basic
 * time types are {@link java.util.Date} and {@link java.util.Calendar}.
 * 
 * @author Matt Sgarlata
 * @since Jan 4, 2005
 */
public class TimeConverter extends BaseTransformer implements Converter, DecoratedConverter {

	private static final Class[] SOURCE_AND_DESTINATION_TYPES = { Date.class, Calendar.class };

	protected Object convertImpl(Class destinationClass, Object source, Locale locale) throws Exception {

		Date date;
		if (source instanceof Date) {
			date = (Date) source;
		}
		else if (source instanceof Calendar) {
			date = ((Calendar) source).getTime();
		}
		else {
			throw new TransformationException(destinationClass, source);
		}
		
		if (Calendar.class.isAssignableFrom(destinationClass)) {
			Calendar calendar = new GregorianCalendar();
			calendar.setTime(date);
			return calendar;
		}
		else if (Date.class.isAssignableFrom(destinationClass)) {
			Date newDate = new Date();
			newDate.setTime(date.getTime());
			return newDate;
		}
		else {
			throw new TransformationException(destinationClass, source);
		}
		
	}
	
	protected Class[] getSourceClassesImpl() throws Exception {
		return SOURCE_AND_DESTINATION_TYPES;
	}

	protected Class[] getDestinationClassesImpl() throws Exception {
		return SOURCE_AND_DESTINATION_TYPES;
	}

}
