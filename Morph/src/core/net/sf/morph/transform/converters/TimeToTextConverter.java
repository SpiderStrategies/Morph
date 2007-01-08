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

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Locale;

import net.sf.morph.Defaults;
import net.sf.morph.transform.Converter;
import net.sf.morph.transform.DecoratedConverter;
import net.sf.morph.transform.transformers.BaseTransformer;

/**
 * Converts the basic time types ({@link java.util.Date} and
 * {@link java.util.Calendar}) to one of the text types (
 * {@link java.lang.String}, {@link java.lang.StringBuffer} and
 * {@link java.lang.Character}).
 * 
 * @author Matt Sgarlata
 * @since Dec 31, 2004
 */
public class TimeToTextConverter extends BaseTransformer implements Converter, DecoratedConverter {
	
	private DateFormat dateFormat;
	private Converter timeConverter;
	private Converter textConverter;

	protected Object convertImpl(Class destinationClass, Object source,
		Locale locale) throws Exception {
		
		Calendar calendar = (Calendar) getTimeConverter().convert(Calendar.class, source, locale);

		String string = getDateFormat(calendar).format(calendar.getTime());

		return getTextConverter().convert(destinationClass, string, locale);
	}

	protected Class[] getSourceClassesImpl() throws Exception {
		return getTimeConverter().getSourceClasses();
	}

	protected Class[] getDestinationClassesImpl() throws Exception {
		return getTextConverter().getDestinationClasses();
	}

	public DateFormat getDateFormat() {
		return getDateFormat(null);
	}

	protected DateFormat getDateFormat(Calendar calendar) {
		if (dateFormat != null) {
			return dateFormat;
		}
		DateFormat result = DateFormat.getDateTimeInstance(DateFormat.LONG, DateFormat.LONG);
		if (calendar != null) {
			result.setCalendar(calendar);
		}
		return result;
	}

	public void setDateFormat(DateFormat dateFormat) {
		this.dateFormat = dateFormat;
	}

	public Converter getTextConverter() {
		if (textConverter == null) {
			setTextConverter(Defaults.createTextConverter());
		}
		return textConverter;
	}

	public void setTextConverter(Converter textConverter) {
		this.textConverter = textConverter;
	}

	public Converter getTimeConverter() {
		if (timeConverter == null) {
			setTimeConverter(Defaults.createTimeConverter());
		}
		return timeConverter;
	}

	public void setTimeConverter(Converter timeConverter) {
		this.timeConverter = timeConverter;
	}
}
