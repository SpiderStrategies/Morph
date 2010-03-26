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
package net.sf.morph2.transform.converters.number;

import java.util.ArrayList;
import java.util.List;

import net.sf.morph2.transform.Transformer;
import net.sf.morph2.transform.converters.NumberConverter;

/**
 * @author Matt Sgarlata
 * @since Oct 24, 2004
 */
public class LongConverterTestCase extends BaseNumberConverterTestCase {
	
	public LongConverterTestCase() { super(); }
	
	public LongConverterTestCase(String arg0) {
		super(arg0);
	}
	
	public Transformer createTransformer() {
		return new NumberConverter();
	}
	
//	public ConvertedSourcePair[] createValidPairsForTimeOn() throws Exception {
//		List list = super.createValidPairs();
//		
//		list.add(new ConvertedSourcePair(new Long(nowDate.getTime()), nowDate));
//		list.add(new ConvertedSourcePair(new Long(nowCalendar.getTime().getTime()), nowCalendar));
//		list.add(new ConvertedSourcePair(new Long(nowCalendar.getTime().getTime()), nowCalendar.getTime()));
//		
//		return (ConvertedSourcePair[]) list.toArray(new ConvertedSourcePair[list.size()]);
//	}
//
//	public void testWithConvertTimeOn() throws Exception {
//		((DefaultLongConverter) getConverter()).setConvertTime(true);
//
//		setValidPairs(createValidPairsForTimeOn());
//		
//		runAllTests();
//		((DefaultLongConverter) getConverter()).setConvertTime(false);
//	}	
	
	public List createDestinationClasses() throws Exception {
		List list = new ArrayList();
		list.add(Long.class);
		list.add(Long.TYPE);
		return list;
	}

	protected boolean isDecimalNumberConverter() {
		return false;
	}

}
