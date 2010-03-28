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
package net.sf.morph2.wrap;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import junit.framework.TestCase;
import net.sf.morph2.Defaults;
import net.sf.morph2.reflect.ReflectionException;
import net.sf.morph2.util.TestClass;
import net.sf.morph2.util.TestUtils;


/**
 * @author Matt Sgarlata
 * @since Jan 16, 2005
 */
public class BeanTestCase extends TestCase {
	
	public BeanTestCase() {
		super();
	}

	public BeanTestCase(String arg0) {
		super(arg0);
	}
	
	private Bean bean;
	
	protected void setUp() throws Exception {
		bean = createBean(TestClass.getFullObject());
	}
	
	protected Bean createBean(Object object) {
		return (Bean) Defaults.createReflector().getWrapper(object);
	}

	public void testGetPropertyNames() {
		Set propertyNames = new HashSet(Arrays.asList(bean.getPropertyNames()));
		Set correctPropertyNames = new HashSet(TestClass.getFullMap().keySet());
//		correctPropertyNames.add(BeanReflector.IMPLICIT_PROPERTY_CLASS);
//		correctPropertyNames.add(BeanReflector.IMPLICIT_PROPERTY_PROPERTY_NAMES);
//		correctPropertyNames.add(BeanReflector.IMPLICIT_PROPERTY_SIZE);
		TestUtils.assertEquals(propertyNames, correctPropertyNames); 
	}

	public void testGetType() {
		assertEquals(bean.getType("anObject"), Object.class);
		assertEquals(bean.getType("myInteger"), Integer.TYPE);
		assertEquals(bean.getType("myMap"), Map.class);
		assertEquals(bean.getType("myLongValue"), Long.class);
		assertEquals(bean.getType("array"), Object[].class);
		assertEquals(bean.getType("bigDecimal"), BigDecimal.class);
		assertEquals(bean.getType("numberArray"), Number[].class);
		assertEquals(bean.getType("string"), String.class);
		try {
			bean.getType("invalidPropertyName");
			fail("should not be able to retrieve the type of an invalid property");
		}
		catch (ReflectionException e) { }
	}

	public void testIsReadable() {
		assertFalse(bean.isReadable("invalidPropertyName"));
		assertTrue(bean.isReadable("anObject"));
		assertTrue(bean.isReadable("myInteger"));
		assertTrue(bean.isReadable("myMap"));
		assertTrue(bean.isReadable("myLongValue"));
		assertTrue(bean.isReadable("array"));
		assertTrue(bean.isReadable("bigDecimal"));
		assertTrue(bean.isReadable("numberArray"));
		assertTrue(bean.isReadable("string"));
	}

	public void testIsWriteable() {
		assertFalse(bean.isWriteable("invalidPropertyName"));
		assertTrue(bean.isWriteable("anObject"));
		assertTrue(bean.isWriteable("myInteger"));
		assertTrue(bean.isWriteable("myMap"));
		assertTrue(bean.isWriteable("myLongValue"));
		assertTrue(bean.isWriteable("array"));
		assertTrue(bean.isWriteable("bigDecimal"));
		assertTrue(bean.isWriteable("numberArray"));
		assertTrue(bean.isWriteable("string"));
	}

	public void testGet() {		
		TestUtils.equals(bean.get("anObject"), new Long(14));
		TestUtils.equals(bean.get("myInteger"), new Integer(4));
		TestUtils.equals(bean.get("myMap"), TestClass.getMyMapProperty());
		TestUtils.equals(bean.get("myLongValue"), new Long(13));
		TestUtils.equals(bean.get("array"), new Object[] { "hi" });
		TestUtils.equals(bean.get("bigDecimal"), new BigDecimal(3.5));
		TestUtils.equals(bean.get("numberArray"), TestClass.NUMBER_ARRAY);
		TestUtils.equals(bean.get("string"), "string");
	}

	public void testSet() throws Exception {
		TestClass object = new TestClass();
		object.allocateTwoSpacesForFunkyArray();
		Bean bean = createBean(object);
		bean.set("anObject", new Long(14));
		bean.set("myInteger", new Integer(4));
		bean.set("myMap", TestClass.getMyMapProperty());
		bean.set("myLongValue", new Long(13));
		bean.set("array", new Object[] { "hi" });
		bean.set("bigDecimal", new BigDecimal(3.5));
		bean.set("numberArray", TestClass.NUMBER_ARRAY);
		bean.set("funkyArray", TestClass.NUMBER_ARRAY);
		bean.set("string", "string");
		TestUtils.assertEquals(TestClass.getFullObject(), object);
	}
	
}
