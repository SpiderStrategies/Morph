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
import java.util.List;
import java.util.Locale;
import java.util.Map;

import net.sf.morph2.transform.Transformer;

/**
 * @author Matt Sgarlata
 * @since Jan 4, 2005
 */
public class TextToNumberConverterTestCase extends BaseConverterTestCase {

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
		list.add(Number.class);
		list.add(List.class);
		list.add(Object.class);
		list.add(Map.class);
		list.add(Object[].class);
		return list;
	}
	protected Transformer createTransformer() {
		return new TextToNumberConverter();
	}
	
	public List createValidPairs() throws Exception {
		List list = new ArrayList();
		list.add(new ConvertedSourcePair(new Integer("4"), new Character('4')));		
		list.add(new ConvertedSourcePair(new Float("4"), new Character('4')));		
		list.add(new ConvertedSourcePair(new Short("4"), new Character('4')));		
		list.add(new ConvertedSourcePair(new Byte("4"), new Character('4')));		
		list.add(new ConvertedSourcePair(new BigInteger("4"), new Character('4')));		
		list.add(new ConvertedSourcePair(new BigDecimal("4"), new Character('4')));		
		list.add(new ConvertedSourcePair(new Double("4"), new Character('4')));
		
		list.add(new ConvertedSourcePair(new Float("4"), "4"));		
		list.add(new ConvertedSourcePair(new Float("4"), new StringBuffer("4")));		
		list.add(new ConvertedSourcePair(new Float("-4"), new StringBuffer("-4.0")));		

		list.add(new ConvertedSourcePair(new Double(2.0d), "2"));
		list.add(new ConvertedSourcePair(new Double(2.0d), "2.0"));
		list.add(new ConvertedSourcePair(new Double(2.1d), "2.10000"));
		list.add(new ConvertedSourcePair(new Double(2.5d), "2.5"));
		list.add(new ConvertedSourcePair(new Double(2.9d), "2.9"));

		list.add(new ConvertedSourcePair(new Float(2.0), "2"));
		list.add(new ConvertedSourcePair(new Float(2.0), "2.0"));
		list.add(new ConvertedSourcePair(new Float(2.1), "2.10000"));
		list.add(new ConvertedSourcePair(new Float(2.5), "2.5"));
		list.add(new ConvertedSourcePair(new Float(2.9), "2.9"));
		
		list.add(new ConvertedSourcePair(new Float(12345.67), "12345.67"));
		list.add(new ConvertedSourcePair(new Float(12345.67), "12,345.67"));
		
		return list;
	}
	
	public void testLocaleSpecificConversions() throws Exception {
		double precision = 0.000001d;
		
		// text in English
		
		Double convertedDouble = (Double)
			getConverter().convert(Double.class, "$4,444.44", Locale.US);
		assertEquals(4444.44d, convertedDouble.doubleValue(), precision);
		
		convertedDouble = (Double)
			getConverter().convert(Double.class, "  $ 4,444.44", Locale.US);
		assertEquals(4444.44d, convertedDouble.doubleValue(), precision);

		Float convertedFloat = (Float)
			getConverter().convert(Float.class, "35%", Locale.US);
		assertEquals(.35, convertedFloat.floatValue(), precision);
	
		convertedFloat = (Float)
			getConverter().convert(float.class, ".35%", Locale.US);
		assertEquals(.0035, convertedFloat.floatValue(), precision);
	
		convertedDouble = (Double)
			getConverter().convert(double.class, "350%", Locale.US);
		assertEquals(3.5, convertedDouble.floatValue(), precision);

		convertedDouble = (Double)
			getConverter().convert(double.class, "3,500,000.1234", Locale.US);
		assertEquals(3500000.1234d, convertedDouble.doubleValue(), precision);
		
		// test in Dutch
		
		Locale dutch = new Locale("nl", "");
		
		convertedDouble = (Double)
			getConverter().convert(Double.class, "\u20ac4.444,44", dutch);
		assertEquals(4444.44d, convertedDouble.doubleValue(), precision);
	
		convertedDouble = (Double)
			getConverter().convert(Double.class, "\u20ac 4.444,44", dutch);
		assertEquals(4444.44d, convertedDouble.doubleValue(), precision);

		convertedFloat = (Float)
			getConverter().convert(Float.class, "35%", dutch);
		assertEquals(.35, convertedFloat.floatValue(), precision);
	
		convertedFloat = (Float)
			getConverter().convert(float.class, ",35%", dutch);
		assertEquals(.0035, convertedFloat.floatValue(), precision);
	
		convertedDouble = (Double)
			getConverter().convert(double.class, "350%", dutch);
		assertEquals(3.5, convertedDouble.floatValue(), precision);
	
		convertedDouble = (Double)
			getConverter().convert(double.class, "3.500.000,1234", dutch);
		assertEquals(3500000.1234d, convertedDouble.doubleValue(), precision);
		
		Locale belgium = new Locale("nl", "BE");
		
		convertedDouble = (Double)
			getConverter().convert(Double.class, "4.444,44\u20ac", belgium);
		assertEquals(4444.44d, convertedDouble.doubleValue(), precision);
	
		convertedDouble = (Double)
			getConverter().convert(Double.class, "4.444,44 \u20ac", belgium);
		assertEquals(4444.44d, convertedDouble.doubleValue(), precision);
	}
	
	public void testNegativeSignHandling() throws Exception {
		Locale iraq = new Locale("ar", "iq");
		
		assertEquals(new Integer(-1), (Integer) getConverter().convert(Integer.class, "-1", iraq));
		assertEquals(new Integer(-1), (Integer) getConverter().convert(Integer.class, "1-", iraq));
		
		assertEquals(new Double(-.01), (Double) getConverter().convert(Double.class, "-1%", Locale.US));
		assertEquals(new Double(-.01), (Double) getConverter().convert(Double.class, "1%-", Locale.US));		
	}

}
