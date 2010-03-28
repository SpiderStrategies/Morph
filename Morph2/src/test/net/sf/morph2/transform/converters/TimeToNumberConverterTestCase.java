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

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.morph2.transform.Transformer;

/**
 * @author Matt Sgarlata
 * @since Feb 3, 2005
 */
public class TimeToNumberConverterTestCase extends BaseConverterTestCase {

	public List createInvalidDestinationClasses() throws Exception {
		List list = new ArrayList();
		list.add(List.class);
		list.add(Object.class);
		list.add(Map.class);
		list.add(Object[].class);
		list.add(Date.class);
		list.add(Calendar.class);
		return list;
	}

	public List createInvalidSources() throws Exception {
		List list = new ArrayList();
		list.add(new Integer(3));
		list.add(new Long(14));
		list.add(new HashMap());
		return list;
	}

	public List createValidPairs() throws Exception {
		Calendar now = new GregorianCalendar();
		String nowString = "" + now.getTime().getTime();
		
		List list = new ArrayList();
		list.add(new ConvertedSourcePair(new BigInteger(nowString), now));
		list.add(new ConvertedSourcePair(new Long(nowString), now));
		list.add(new ConvertedSourcePair(new BigDecimal(nowString), now));
		list.add(new ConvertedSourcePair(new Double(nowString), now));
		list.add(new ConvertedSourcePair(new BigInteger(nowString), now.getTime()));
		list.add(new ConvertedSourcePair(new Long(nowString), now.getTime()));
		list.add(new ConvertedSourcePair(new BigDecimal(nowString), now.getTime()));
		list.add(new ConvertedSourcePair(new Double(nowString), now.getTime()));		
		return list;
	}

	public List createDestinationClasses() throws Exception {
		List list = new ArrayList();
		list.add(Number.class);
		list.add(int.class);
		list.add(long.class);
		return list;
	}

	protected Transformer createTransformer() {
		return new TimeToNumberConverter();
	}

}
