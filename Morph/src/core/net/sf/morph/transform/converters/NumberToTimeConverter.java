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

import java.util.Date;
import java.util.Locale;

import net.sf.morph.Defaults;
import net.sf.morph.transform.Converter;
import net.sf.morph.transform.DecoratedConverter;
import net.sf.morph.transform.transformers.BaseTransformer;

/**
 * @author Matt Sgarlata
 * @author Alexander Volanis
 * @since Jan 8, 2005
 */
public class NumberToTimeConverter extends BaseTransformer implements Converter, DecoratedConverter {
	
	private Converter numberConverter;
	private Converter timeConverter;
	
	protected Object convertImpl(Class destinationClass, Object source,
		Locale locale) throws Exception {
		
		Long number =
			(Long) getNumberConverter().convert(Long.class, source, locale);
		Date date = new Date();
		date.setTime(number.longValue());
		
		return getTimeConverter().convert(destinationClass, date, locale);
	}

	protected Class[] getSourceClassesImpl() throws Exception {
		return getNumberConverter().getSourceClasses();
	}

	protected Class[] getDestinationClassesImpl() throws Exception {
		return getTimeConverter().getDestinationClasses();
	}

	public Converter getNumberConverter() {
		if (numberConverter == null) {
			setNumberConverter(Defaults.createNumberConverter());
		}
		return numberConverter;
	}
	public void setNumberConverter(Converter numberConverter) {
		this.numberConverter = numberConverter;
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
