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
package net.sf.morph2.transform.converters;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.morph2.transform.Transformer;

/**
 * @author Matt Sgarlata
 * @since Apr 28, 2005
 */
public class TimeConverterTestCase extends BaseConverterTestCase {

	public List createInvalidDestinationClasses() throws Exception {
		List list = new ArrayList();
		list.add(Collection.class);
		list.add(Object.class);
		list.add(int.class);
		list.add(Integer.class);
		list.add(Map.class);
		return list;
	}

	public List createInvalidSources() throws Exception {
		List list = new ArrayList();
		list.add(new ArrayList());
		list.add("");
		list.add(new Integer(4));
		list.add(new HashMap());
		return list;
	}

	public List createValidPairs() throws Exception {
		Date date1 = new Date("May 19, 1980 12:42:43 AM");
		Calendar calendar1 = new GregorianCalendar();
		calendar1.set(Calendar.MONTH, Calendar.MAY);
		calendar1.set(Calendar.DAY_OF_MONTH, 19);
		calendar1.set(Calendar.YEAR, 1980);
		calendar1.set(Calendar.HOUR, 0);
		calendar1.set(Calendar.MINUTE, 42);
		calendar1.set(Calendar.AM_PM, Calendar.AM);
		calendar1.set(Calendar.SECOND, 43);
		calendar1.set(Calendar.MILLISECOND, 0);
		
		Date date2 = new Date("May 19, 1980");
		Calendar calendar2 = new GregorianCalendar();
		calendar2.set(Calendar.MONTH, Calendar.MAY);
		calendar2.set(Calendar.DAY_OF_MONTH, 19);
		calendar2.set(Calendar.YEAR, 1980);
		calendar2.set(Calendar.HOUR_OF_DAY, 0);
		calendar2.set(Calendar.MINUTE, 0);
		calendar2.set(Calendar.SECOND, 0);
		calendar2.set(Calendar.MILLISECOND, 0);
		
		List list = new ArrayList();
		list.add(new ConvertedSourcePair(calendar1, date1));
		list.add(new ConvertedSourcePair(date1, calendar1));
		list.add(new ConvertedSourcePair(calendar2, date2));
		list.add(new ConvertedSourcePair(date2, calendar2));
		return list;
	}

	public List createDestinationClasses() throws Exception {
		List list = new ArrayList();
		list.add(Date.class);
		list.add(java.sql.Date.class);
		list.add(java.sql.Timestamp.class);
		list.add(Calendar.class);
		list.add(GregorianCalendar.class);
		return list;
	}

	protected Transformer createTransformer() {
		return new TimeConverter();
	}

}
