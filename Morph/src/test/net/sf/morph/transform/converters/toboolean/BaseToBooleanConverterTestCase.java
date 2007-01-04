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
package net.sf.morph.transform.converters.toboolean;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.morph.transform.converters.BaseConverterTestCase;


public abstract class BaseToBooleanConverterTestCase extends BaseConverterTestCase {
	
	public void testNullConversion() {
		// don't run this test b/c null isn't always converted to null
		// for the boolean converter
	}
	
	public List createDestinationClasses() throws Exception {
		List list = new ArrayList();
		list.add(Boolean.TYPE);
		list.add(Boolean.class);
		list.add(boolean.class);
		return list;
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
	public List createInvalidPairs() throws Exception {
		return null;
	}
	
	public void addIdentityConversions(List list) {
		list.add(new ConvertedSourcePair(Boolean.TRUE, Boolean.TRUE));
		list.add(new ConvertedSourcePair(Boolean.FALSE, Boolean.FALSE));
	}

	public void addTextConversions(List list) {
		list.add(new ConvertedSourcePair(Boolean.TRUE, "true"));
		list.add(new ConvertedSourcePair(Boolean.TRUE, "tRuE"));
		list.add(new ConvertedSourcePair(Boolean.TRUE, new Character('t')));
		list.add(new ConvertedSourcePair(Boolean.TRUE, new StringBuffer("yEs")));
		list.add(new ConvertedSourcePair(Boolean.TRUE, new Character('Y')));
		
		list.add(new ConvertedSourcePair(Boolean.FALSE, "false"));
		list.add(new ConvertedSourcePair(Boolean.FALSE, "fAlSe"));
		list.add(new ConvertedSourcePair(Boolean.FALSE, new Character('f')));
		list.add(new ConvertedSourcePair(Boolean.FALSE, new StringBuffer("nO")));
		list.add(new ConvertedSourcePair(Boolean.FALSE, new Character('N')));
	}
	
	public void addObjectConversions(List list) {
		// make sure non-empty values are converted to true
		list.add(new ConvertedSourcePair(Boolean.TRUE, new Integer(3)));
		list.add(new ConvertedSourcePair(Boolean.TRUE, Class.class));
		
		// make sure empty values are converted to false
		list.add(new ConvertedSourcePair(Boolean.FALSE, new Object[0]));
		list.add(new ConvertedSourcePair(Boolean.FALSE, new ArrayList()));			
		list.add(new ConvertedSourcePair(Boolean.FALSE, new HashMap()));
	}
	
	public void addNumberConversions(List list) {
		// make sure zeros are converted to false
		list.add(new ConvertedSourcePair(Boolean.FALSE, new Integer("0")));
		list.add(new ConvertedSourcePair(Boolean.FALSE, new Short("0")));
		list.add(new ConvertedSourcePair(Boolean.FALSE, new Byte("0")));
		list.add(new ConvertedSourcePair(Boolean.FALSE, new Float("0.0")));
		list.add(new ConvertedSourcePair(Boolean.FALSE, new Double("0.0")));
		list.add(new ConvertedSourcePair(Boolean.FALSE, new BigDecimal("0.0")));
		list.add(new ConvertedSourcePair(Boolean.FALSE, new BigInteger("0")));
		
		// make sure non-zero numers are converted to true
		list.add(new ConvertedSourcePair(Boolean.TRUE, new Integer("1")));
		list.add(new ConvertedSourcePair(Boolean.TRUE, new Double("0.000001")));
		list.add(new ConvertedSourcePair(Boolean.TRUE, new BigDecimal("0.000001")));
		list.add(new ConvertedSourcePair(Boolean.TRUE, new BigInteger("-1")));
		list.add(new ConvertedSourcePair(Boolean.TRUE, new Long("-13214124")));
	}		
	
}