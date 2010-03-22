/*
 * Copyright 2004-2005, 2008 the original author or authors.
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
package net.sf.morph2.transform.converters;

import java.text.DateFormat;
import java.util.Date;
import java.util.Locale;

import net.sf.composite.util.ObjectUtils;
import net.sf.morph2.Defaults;
import net.sf.morph2.transform.Converter;
import net.sf.morph2.transform.DecoratedConverter;
import net.sf.morph2.transform.transformers.BaseTransformer;

public class TextToTimeConverter extends BaseTransformer implements DecoratedConverter {

	private static final DateFormat DEFAULT_DATE_FORMAT = DateFormat.getDateTimeInstance(DateFormat.LONG, DateFormat.LONG);

	private DateFormat dateFormat;
	private Converter timeConverter;
	private Converter textConverter;

	/**
	 * {@inheritDoc}
	 */
	protected Object convertImpl(Class destinationClass, Object source,
			Locale locale) throws Exception {

		String text = (String) getTextConverter().convert(String.class, source, locale);

		if (ObjectUtils.isEmpty(text)) {
			return null;
		}
		Date date = getDateFormat().parse(text);
		return getTimeConverter().convert(destinationClass, date, locale);
	}

	/**
	 * {@inheritDoc}
	 */
	protected Class[] getSourceClassesImpl() throws Exception {
		return getTextConverter().getDestinationClasses();
	}

	/**
	 * {@inheritDoc}
	 */
	protected Class[] getDestinationClassesImpl() throws Exception {
		return getTimeConverter().getSourceClasses();
	}

	/**
	 * {@inheritDoc}
	 */
	protected boolean isWrappingRuntimeExceptions() {
	    return true;
    }

	/**
	 * Get the DateFormat used by this TextToTimeConverter.
	 * @return DateFormat
	 */
	public DateFormat getDateFormat() {
		if (dateFormat == null) {
			setDateFormat(DEFAULT_DATE_FORMAT);
		}
		return dateFormat;
	}

	/**
	 * Set the DateFormat to be used by this TextToTimeConverter.
	 * @param dateFormat
	 */
	public void setDateFormat(DateFormat dateFormat) {
		this.dateFormat = dateFormat;
	}

	/**
	 * Get the text converter used by this TextToTimeConverter.
	 * @return Converter
	 */
	public Converter getTextConverter() {
		if (textConverter == null) {
			setTextConverter(Defaults.createTextConverter());
		}
		return textConverter;
	}

	/**
	 * Set the text converter used by this TextToTimeConverter.
	 * @param textConverter
	 */
	public void setTextConverter(Converter textConverter) {
		this.textConverter = textConverter;
	}

	/**
	 * Get the time converter used by this TextToTimeConverter.
	 * @return Converter
	 */
	public Converter getTimeConverter() {
		if (timeConverter == null) {
			setTimeConverter(Defaults.createTimeConverter());
		}
		return timeConverter;
	}

	/**
	 * Set the time converter used by this TextToTimeConverter.
	 * @param timeConverter
	 */
	public void setTimeConverter(Converter timeConverter) {
		this.timeConverter = timeConverter;
	}

}
