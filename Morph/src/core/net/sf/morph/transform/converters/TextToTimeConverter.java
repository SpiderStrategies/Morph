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
import java.util.Date;
import java.util.Locale;

import net.sf.composite.util.ObjectUtils;
import net.sf.morph.Defaults;
import net.sf.morph.transform.Converter;
import net.sf.morph.transform.DecoratedConverter;
import net.sf.morph.transform.transformers.BaseTransformer;

public class TextToTimeConverter extends BaseTransformer implements Converter, DecoratedConverter {

	private static final DateFormat DEFAULT_DATE_FORMAT = DateFormat.getDateTimeInstance(DateFormat.LONG, DateFormat.LONG);

	private DateFormat dateFormat;
	private Converter timeConverter;
	private Converter textConverter;

	protected Object convertImpl(Class destinationClass, Object source,
			Locale locale) throws Exception {

		String text = (String) getTextConverter().convert(String.class, source, locale);

		if (ObjectUtils.isEmpty(text)) {
			return null;
		}
		Date date = getDateFormat().parse(text);
		return getTimeConverter().convert(destinationClass, date, locale);
	}

	protected Class[] getSourceClassesImpl() throws Exception {
		return getTextConverter().getDestinationClasses();
	}

	protected Class[] getDestinationClassesImpl() throws Exception {
		return getTimeConverter().getSourceClasses();
	}

	public DateFormat getDateFormat() {
		if (dateFormat == null) {
			setDateFormat(DEFAULT_DATE_FORMAT);
		}
		return dateFormat;
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
