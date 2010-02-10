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

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import net.sf.morph.transform.Transformer;

/**
 * @author Matt Sgarlata
 * @since Jan 4, 2005
 */
public class NumberConverterTestCase extends BaseConverterTestCase {

	public List createDestinationClasses() throws Exception {
		List list = new ArrayList();
		list.add(Number.class);
		list.add(BigDecimal.class);
		list.add(Short.class);
		list.add(byte.class);
		list.add(short.class);
		list.add(int.class);
		list.add(long.class);
		list.add(float.class);
		list.add(double.class);
		return list;
	}
	public List createInvalidDestinationClasses() throws Exception {
		List list = new ArrayList();
		list.add(List.class);
		list.add(Object.class);
		list.add(Map.class);
		list.add(Object[].class);
		return list;
	}
	public List createInvalidSources() throws Exception {
		List list = new ArrayList();
		list.add("hello");
		list.add("13q");
		return list;
	}
	
	public List createInvalidSourceClasses() throws Exception {
		List list = new ArrayList();
		list.add(String.class);
		list.add(List.class);
		list.add(Object.class);
		list.add(Map.class);
		list.add(Object[].class);
		return list;
	}
	protected Transformer createTransformer() {
		return new NumberConverter();
	}
	
	public List createValidPairs() throws Exception {
		List list = new ArrayList();
		list.add(new ConvertedSourcePair(new Integer("4"), new Integer("4")));		
		list.add(new ConvertedSourcePair(new Float("4"), new Integer("4")));		
		list.add(new ConvertedSourcePair(new Short("4"), new Integer("4")));		
		list.add(new ConvertedSourcePair(new Byte("4"), new Integer("4")));		
		list.add(new ConvertedSourcePair(new BigInteger("4"), new Integer("4")));		
		list.add(new ConvertedSourcePair(new BigDecimal("4"), new Integer("4")));		
		list.add(new ConvertedSourcePair(new Double("4"), new Integer("4")));
		
		list.add(new ConvertedSourcePair(new Integer("4"), new Float("4")));		
		list.add(new ConvertedSourcePair(new Float("4"), new Float("4")));		
		list.add(new ConvertedSourcePair(new Short("4"), new Float("4")));		
		list.add(new ConvertedSourcePair(new Byte("4"), new Float("4")));		
		list.add(new ConvertedSourcePair(new BigInteger("4"), new Float("4")));		
		list.add(new ConvertedSourcePair(new BigDecimal("4"), new Float("4")));		
		list.add(new ConvertedSourcePair(new Double("4"), new Float("4")));
		
		list.add(new ConvertedSourcePair(new Integer("4"), new Short("4")));		
		list.add(new ConvertedSourcePair(new Float("4"), new Short("4")));		
		list.add(new ConvertedSourcePair(new Short("4"), new Short("4")));		
		list.add(new ConvertedSourcePair(new Byte("4"), new Short("4")));		
		list.add(new ConvertedSourcePair(new BigInteger("4"), new Short("4")));		
		list.add(new ConvertedSourcePair(new BigDecimal("4"), new Short("4")));		
		list.add(new ConvertedSourcePair(new Double("4"), new Short("4")));
		
		list.add(new ConvertedSourcePair(new Integer("4"), new Byte("4")));		
		list.add(new ConvertedSourcePair(new Float("4"), new Byte("4")));		
		list.add(new ConvertedSourcePair(new Short("4"), new Byte("4")));		
		list.add(new ConvertedSourcePair(new Byte("4"), new Byte("4")));		
		list.add(new ConvertedSourcePair(new BigInteger("4"), new Byte("4")));		
		list.add(new ConvertedSourcePair(new BigDecimal("4"), new Byte("4")));		
		list.add(new ConvertedSourcePair(new Double("4"), new Byte("4")));
		
		list.add(new ConvertedSourcePair(new Integer("4"), new BigInteger("4")));		
		list.add(new ConvertedSourcePair(new Float("4"), new BigInteger("4")));		
		list.add(new ConvertedSourcePair(new Short("4"), new BigInteger("4")));		
		list.add(new ConvertedSourcePair(new Byte("4"), new BigInteger("4")));		
		list.add(new ConvertedSourcePair(new BigInteger("4"), new BigInteger("4")));		
		list.add(new ConvertedSourcePair(new BigDecimal("4"), new BigInteger("4")));		
		list.add(new ConvertedSourcePair(new Double("4"), new BigInteger("4")));
		
		list.add(new ConvertedSourcePair(new Integer("4"), new BigDecimal("4")));		
		list.add(new ConvertedSourcePair(new Float("4"), new BigDecimal("4")));		
		list.add(new ConvertedSourcePair(new Short("4"), new BigDecimal("4")));		
		list.add(new ConvertedSourcePair(new Byte("4"), new BigDecimal("4")));		
		list.add(new ConvertedSourcePair(new BigInteger("4"), new BigDecimal("4")));		
		list.add(new ConvertedSourcePair(new BigDecimal("4"), new BigDecimal("4")));		
		list.add(new ConvertedSourcePair(new Double("4"), new BigDecimal("4")));
		
		list.add(new ConvertedSourcePair(new Integer("4"), new Double("4")));		
		list.add(new ConvertedSourcePair(new Float("4"), new Double("4")));		
		list.add(new ConvertedSourcePair(new Short("4"), new Double("4")));		
		list.add(new ConvertedSourcePair(new Byte("4"), new Double("4")));		
		list.add(new ConvertedSourcePair(new BigInteger("4"), new Double("4")));		
		list.add(new ConvertedSourcePair(new BigDecimal("4"), new Double("4")));		
		list.add(new ConvertedSourcePair(new Double("4"), new Double("4")));
		
		list.add(new ConvertedSourcePair(new Long("4"), new Double("4")));
		list.add(new ConvertedSourcePair(new Double("4"), new Long("4")));
				
		list.add(new ConvertedSourcePair(new Double(2.1d), new Float("2.10000")));
		list.add(new ConvertedSourcePair(new Double(2.5d), new Float("2.5")));
		list.add(new ConvertedSourcePair(new Double(2.9d), new Float("2.9")));

		list.add(new ConvertedSourcePair(new Float("4.7"), new Double("4.7")));		
		list.add(new ConvertedSourcePair(new BigDecimal("4.7"), new Double("4.7")));		
		list.add(new ConvertedSourcePair(new Double("4.7"), new Double("4.7")));
				
		list.add(new ConvertedSourcePair(new Float("4.7"), new Float("4.7")));		
		list.add(new ConvertedSourcePair(new BigDecimal("4.7"), new Float("4.7")));		
		list.add(new ConvertedSourcePair(new Double("4.7"), new Float("4.7")));
				
		list.add(new ConvertedSourcePair(new Float("4.7"), new BigDecimal("4.7")));		
		list.add(new ConvertedSourcePair(new BigDecimal("4.7"), new BigDecimal("4.7")));		
		list.add(new ConvertedSourcePair(new Double("4.7"), new BigDecimal("4.7")));
				
		return list;
	}
	
	public void testPrimativeConversions() throws Exception {
		assertEquals(4, ((Integer) getConverter().convert(int.class, new Integer(4))).intValue());		
		assertEquals(4.0, ((Float) getConverter().convert(float.class, new Integer(4))).floatValue(), 0.001);		
		assertEquals(4l, ((Long) getConverter().convert(long.class, new Integer(4))).longValue());
		
		assertEquals(2.0d, ((Double) getConverter().convert(double.class, new Integer(2))).doubleValue(), 0.001d);
		assertEquals(2.0d, ((Double) getConverter().convert(double.class, new Float(2.0000))).doubleValue(), 0.001d);
		assertEquals(2.5d, ((Double) getConverter().convert(double.class, new Float(2.5))).doubleValue(), 0.001d);
		assertEquals(2.9d, ((Double) getConverter().convert(double.class, new BigDecimal(2.9))).doubleValue(), 0.001d);

	}	

}
