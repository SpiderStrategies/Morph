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

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import net.sf.composite.util.ObjectUtils;
import net.sf.morph2.Defaults;
import net.sf.morph2.reflect.BeanReflector;
import net.sf.morph2.transform.TransformationException;
import net.sf.morph2.transform.converters.BaseConverterTestCase;
import net.sf.morph2.util.NumberUtils;
import net.sf.morph2.util.TestClass;
import net.sf.morph2.util.TestNumberUtils;

/**
 * @author Matt Sgarlata
 * @since Oct 25, 2004
 */
public abstract class BaseNumberConverterTestCase extends BaseConverterTestCase {

	protected Class destinationClass;
	protected BeanReflector beanReflector;
	protected Date nowDate;
	protected Calendar nowCalendar;
	protected Number minValue;
	protected Number maxValue;
	protected Number tooBig;
	protected Number tooSmall;
	

	protected void setUp() throws Exception {
		destinationClass = (Class) createDestinationClasses().get(0);
		minValue = NumberUtils.getMinimumForType(destinationClass);
		maxValue = NumberUtils.getMaximumForType(destinationClass);
		tooBig = TestNumberUtils.getTooBigForType(destinationClass);
		tooSmall = TestNumberUtils.getTooSmallForType(destinationClass);
		
		nowDate = new Date();
		nowCalendar = new GregorianCalendar();
		beanReflector = Defaults.createBeanReflector();

		super.setUp();
	}
	
	public BaseNumberConverterTestCase() { super(); }
	
	public BaseNumberConverterTestCase(String arg0) {
		super(arg0);
	}

	public List createInvalidDestinationClasses() throws Exception {
		List list = new ArrayList();
		list.add(Object.class);
		list.add(Number.class);
		list.add(String.class);
		list.add(Long.TYPE);
		list.add(Date.class);
		list.add(Map.class);
		list.add(List.class);
		return list;
	}

	public List createInvalidSources() throws Exception {
		List list = new ArrayList();
		list.add(new HashMap());
		list.add(new Hashtable());
		list.add(this);
		return list;  
	}

	public List createInvalidPairs() throws Exception {
		List list = new ArrayList();
		if (minValue != null && maxValue != null) {
			list.add(new ConvertedSourcePair(maxValue, minValue));
			list.add(new ConvertedSourcePair(minValue, maxValue));
			list.add(new ConvertedSourcePair(maxValue,
					new BigDecimal(minValue.toString())));
			list.add(new ConvertedSourcePair(minValue,
					new BigDecimal(maxValue.toString())));
			list.add(new ConvertedSourcePair(maxValue, tooBig));
			list.add(new ConvertedSourcePair(minValue, tooSmall));
		}
		list.add(new ConvertedSourcePair(new Long(3), nowDate));
		list.add(new ConvertedSourcePair(new Long(3), nowCalendar));
		return list;
	}
	
	public Number getNumber(String s) throws Exception {
		return NumberUtils.getNumber(destinationClass, s);
	}

	public List createValidPairs() throws Exception {
		List list = new ArrayList();
		list.add(new ConvertedSourcePair(getNumber("0"), new Double(0.0)));		
		list.add(new ConvertedSourcePair(getNumber("-4"), new Byte("-4")));
		list.add(new ConvertedSourcePair(getNumber("-3"), new Long(-3l)));
		list.add(new ConvertedSourcePair(getNumber("12"), new Short("12")));
		list.add(new ConvertedSourcePair(getNumber("-2"), new BigDecimal("-2.0000000")));
		list.add(new ConvertedSourcePair(getNumber("1"), new BigInteger("1")));
		
		if (isDecimalNumberConverter()) {
			list.add(new ConvertedSourcePair(getNumber("-14.0"), new Float(-14.0)));
			list.add(new ConvertedSourcePair(getNumber("-14.1"), new Double(-14.1)));
			list.add(new ConvertedSourcePair(getNumber("-13.9"), new Float(-13.9)));
			list.add(new ConvertedSourcePair(getNumber("-14.23"), new BigDecimal("-14.23")));
		}
		else {
			list.add(new ConvertedSourcePair(getNumber("-14"), new Float(-14.0)));
			list.add(new ConvertedSourcePair(getNumber("-14"), new Double(-14.1)));
			list.add(new ConvertedSourcePair(getNumber("-14"), new Float(-13.9)));
			list.add(new ConvertedSourcePair(getNumber("-14"), new BigDecimal("-14.23")));
		}

		// retrieve a primitive integer using reflection and convert it
		list.add(new ConvertedSourcePair(getNumber("4"), beanReflector.get(TestClass.getPartialObject(), "myInteger")));
		return list;
	}
	
	public void testEndpoints() {
		if (minValue != null && maxValue != null) {
			try {
				getConverter().convert(destinationClass, tooBig, null);
				fail("Exception should have been thrown because trying to convert from the tooBig value " + ObjectUtils.getObjectDescription(tooBig));
			}
			catch (TransformationException e) { }
			try {
				getConverter().convert(destinationClass, tooSmall, null);
				fail("Exception should have been thrown because trying to convert from the tooSmall value " + ObjectUtils.getObjectDescription(tooSmall));
			}
			catch (TransformationException e) { }
			
			getConverter().convert(destinationClass, maxValue, null);
			getConverter().convert(destinationClass, minValue, null);
			
//			((NumberConverter) getConverter()).setEnsureDataConsistency(false);
//			
//			// now these shouldn't cause exceptions
//			getConverter().convert(destinationClass, tooBig, null);
//			getConverter().convert(destinationClass, tooSmall, null);
//
//			((NumberConverter) getConverter()).setEnsureDataConsistency(true);	
		}
	}

	protected abstract boolean isDecimalNumberConverter();
	
}
