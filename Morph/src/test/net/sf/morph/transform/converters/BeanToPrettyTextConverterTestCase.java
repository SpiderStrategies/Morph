/*
 * Copyright 2007 the original author or authors.
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

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.collections.OrderedMap;
import org.apache.commons.collections.map.ListOrderedMap;

import junit.framework.TestCase;
import net.sf.morph.transform.DecoratedConverter;

/**
 * 
 * @author Matt Sgarlata
 * @since May 31, 2007 (Morph 1.0.2)
 */
public class BeanToPrettyTextConverterTestCase extends TestCase {
	
	private DecoratedConverter defaultConverter;
	
	public class TrickyObject {
		private String readAndWrite;
		private String readOnly;
		private String writeOnly;
		
		public TrickyObject(String readOnly) {
			this.readOnly = readOnly;
		}
		
		public String getReadAndWrite() {
        	return readAndWrite;
        }
		public void setReadAndWrite(String readAndWrite) {
        	this.readAndWrite = readAndWrite;
        }
		public String getReadOnly() {
        	return readOnly;
        }
		public void setWriteOnly(String writeOnly) {
        	this.writeOnly = writeOnly;
        }
	}
	
	protected void setUp() throws Exception {
	    defaultConverter = new BeanToPrettyTextConverter();
    }

	public void testSimpleObject() {
		Map source = new ListOrderedMap();
		source.put("one", new Integer(1));
		source.put("two", new Integer(2));
		source.put("three", new Integer(3));
		String destination = (String) defaultConverter.convert(String.class, source);
		assertEquals("[one=1,two=2,three=3]", destination);
	}
	
	public void testTrickyObject() {
		TrickyObject source = new TrickyObject("r");
		source.setReadAndWrite("rw");
		source.setWriteOnly("w");
		String destination = (String) defaultConverter.convert(String.class, source);
		// not sure if this will work across JVMs because depends on the order
		// the properties are returned in
		assertEquals("[readAndWrite=rw,readOnly=r]", destination);
	}
	
	public void testWithNulls() {
		BeanToPrettyTextConverter converter = new BeanToPrettyTextConverter();
		converter.setSeparator("&");
		converter.setPrefix(null);
		converter.setSuffix(null);
		
		Map source = new ListOrderedMap();
		source.put("calendarId", null);
		source.put("calendarPeriodId", new Integer(1));
		source.put("organizationId", new int[] { 2, 3});
		String destination = (String) converter.convert(String.class, source);
		assertEquals("calendarPeriodId=1&organizationId={2,3}", destination);
		
		source = new ListOrderedMap();
		source.put("calendarId", null);
		source.put("calendarPeriodId", new Integer(1));
		source.put("organizationId", null);
		destination = (String) converter.convert(String.class, source);
		assertEquals("calendarPeriodId=1", destination);		
		
		source = new ListOrderedMap();
		source.put("calendarId1", null);
		source.put("calendarId2", null);
		source.put("calendarPeriodId", new Integer(1));
		source.put("middleNull", null);
		source.put("organizationId", "hi");
		source.put("organizationId2", null);
		destination = (String) converter.convert(String.class, source);
		assertEquals("calendarPeriodId=1&organizationId=hi", destination);		
	}
	
}
