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
package net.sf.morph.transform.converters.totext;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

import net.sf.morph.transform.converters.BaseConverterTestCase;
import net.sf.morph.util.TestObjects;

/**
 * @author Matt Sgarlata
 * @since Jan 9, 2005
 */
public abstract class BaseToTextConverterTestCase extends BaseConverterTestCase {
	
	public BaseToTextConverterTestCase() {
		super();
	}
	public BaseToTextConverterTestCase(String arg0) {
		super(arg0);
	}
	
	protected Character y = new Character('Y');
	protected Character n = new Character('N');
	protected Character t = new Character('t');
	protected Character f = new Character('f');
	protected Character zero = new Character('0');
	protected Character one = new Character('1');
	protected Character nine = new Character('9');
	protected TestObjects to = new TestObjects();

	public List createInvalidDestinationClasses() throws Exception {
		List list = new ArrayList();
		list.add(Object.class);
		list.add(Number.class);
		list.add(Long.TYPE);
		list.add(Date.class);
		list.add(Map.class);
		list.add(List.class);
		return list;
	}

	public List createInvalidSources() throws Exception {
		return null;
	}
	
	public List createValidSources() throws Exception {
		return null;
	}

	public List createDestinationClasses() throws Exception {
		List list = new ArrayList();
		list.add(String.class);
		list.add(Character.class);
		list.add(StringBuffer.class);
		return list;
	}

	protected void addBooleanConversions(List list) {
		list.add(new ConvertedSourcePair(t, Boolean.TRUE));
		list.add(new ConvertedSourcePair(f, Boolean.FALSE));
		list.add(new ConvertedSourcePair(new StringBuffer("true"), Boolean.TRUE));
		list.add(new ConvertedSourcePair(new StringBuffer("false"), Boolean.FALSE));
		list.add(new ConvertedSourcePair("true", Boolean.TRUE));
		list.add(new ConvertedSourcePair("false", Boolean.FALSE));
	}
	
	protected void addContainerConversions(List list) {
		list.add(new ConvertedSourcePair("{}", to.emptyList));
		list.add(new ConvertedSourcePair("{1,2,3}", to.oneTwoThreeList));
	}
	
	protected void addObjectConversions(List list) {
		list.add(new ConvertedSourcePair(zero, new Integer(0)));
		list.add(new ConvertedSourcePair(nine, new BigDecimal(9)));
		list.add(new ConvertedSourcePair(one, new Double(1.0)));
		list.add(new ConvertedSourcePair("0", new Integer("0")));
		list.add(new ConvertedSourcePair("9", new BigDecimal("9")));
		list.add(new ConvertedSourcePair(toString(), this));
	}
	
	protected void addTimeConversions(List list) {
		Calendar calendar = new GregorianCalendar();
		calendar.set(Calendar.YEAR, 2005);
		calendar.set(Calendar.MONTH, Calendar.JANUARY);
		calendar.set(Calendar.DAY_OF_MONTH, 30);
		// set to 11 o'clock eastern, compensating for the time zone offset
		calendar.set(Calendar.HOUR_OF_DAY, 11);
		calendar.set(Calendar.MINUTE, 51);
		calendar.set(Calendar.SECOND, 2);
		calendar.set(Calendar.MILLISECOND, 0);
		calendar.setTimeZone(TimeZone.getTimeZone("America/New_York"));
		String converted = "January 30, 2005 11:51:02 AM EST";
		list.add(new ConvertedSourcePair(converted, calendar));
		//wrt Date conversion, we lose the TZ:
		calendar = (Calendar) calendar.clone();
		calendar.setTimeZone(TimeZone.getDefault());
		DateFormat df = DateFormat.getDateTimeInstance(DateFormat.LONG, DateFormat.LONG);
		df.setCalendar(calendar);
		Date d = calendar.getTime();
		list.add(new ConvertedSourcePair(df.format(d), d));
	}

}
