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
package net.sf.morph2.transform.converters.totext;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import net.sf.morph2.transform.Transformer;
import net.sf.morph2.transform.converters.BaseConverterTestCase;
import net.sf.morph2.transform.converters.NumberToTextConverter;
import net.sf.morph2.util.TestUtils;

/**
 * @author Matt Sgarlata
 * @since Jan 4, 2005
 */
public class NumberToTextConverterTestCase extends BaseConverterTestCase {

	public List createDestinationClasses() throws Exception {
		List list = new ArrayList();
		list.add(String.class);
		list.add(StringBuffer.class);
		list.add(Character.class);
		list.add(char.class);
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
		return new NumberToTextConverter();
	}
	
	public List createValidPairs() throws Exception {
		List list = new ArrayList();
		list.add(new ConvertedSourcePair(new Character('4'), new Integer("4")));		
		list.add(new ConvertedSourcePair(new Character('4'), new Float("4")));		
		list.add(new ConvertedSourcePair(new Character('4'), new Short("4")));		
		list.add(new ConvertedSourcePair(new Character('4'), new Byte("4")));		
		list.add(new ConvertedSourcePair(new Character('4'), new BigInteger("4")));		
		list.add(new ConvertedSourcePair(new Character('4'), new BigDecimal("4")));		
		list.add(new ConvertedSourcePair(new Character('4'), new Double("4")));
		
		list.add(new ConvertedSourcePair("4", new Float("4")));		
		list.add(new ConvertedSourcePair(new StringBuffer("4"), new Float("4")));		
		list.add(new ConvertedSourcePair(new StringBuffer("-4"), new Float("-4")));

		list.add(new ConvertedSourcePair("2", new Double(2.0d)));
		list.add(new ConvertedSourcePair("2.5", new Double(2.5d)));

		list.add(new ConvertedSourcePair("2", new Float(2.0)));
		list.add(new ConvertedSourcePair("2.5", new Float(2.5)));
		
		return list;
	}
	
	public void testLocaleSpecificConversions() throws Exception {
		// text in English
		
		String convertedString = (String)
			getConverter().convert(String.class, new Double(4444.44d), Locale.US);
		assertEquals("4444.44", convertedString);
		
		StringBuffer convertedStringBuffer = (StringBuffer)
			getConverter().convert(StringBuffer.class, new Double(4444.44d), Locale.US);
		TestUtils.assertEquals(new StringBuffer("4444.44"), convertedStringBuffer);

		convertedString = (String)
			getConverter().convert(String.class, new Double(3500000.123400d), Locale.US);
		assertEquals("3500000.1234", convertedString);
	
		// test in Dutch
		
		Locale dutch = new Locale("nl", "");
		
		convertedString = (String)
			getConverter().convert(String.class, new Double(4444.44d), dutch);
		assertEquals("4444,44", convertedString);
		
		convertedStringBuffer = (StringBuffer)
			getConverter().convert(StringBuffer.class, new Double(4444.44d), dutch);
		TestUtils.assertEquals(new StringBuffer("4444,44"), convertedStringBuffer);
	
		convertedString = (String)
			getConverter().convert(String.class, new Double(3500000.1234d), dutch);
		assertEquals("3500000,1234", convertedString);
		
		Locale belgium = new Locale("nl", "BE");
		
		convertedString = (String)
			getConverter().convert(String.class, new Double(4444.44d), belgium);
		assertEquals("4444,44", convertedString);
		
		convertedStringBuffer = (StringBuffer)
			getConverter().convert(StringBuffer.class, new Double(4444.44d), belgium);
		TestUtils.assertEquals(new StringBuffer("4444,44"), convertedStringBuffer);
	
		convertedString = (String)
			getConverter().convert(String.class, new Double(3500000.1234d), belgium);
		assertEquals("3500000,1234", convertedString);
		
	}

}
