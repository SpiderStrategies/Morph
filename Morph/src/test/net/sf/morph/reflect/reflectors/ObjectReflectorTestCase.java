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
package net.sf.morph.reflect.reflectors;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.morph.reflect.ReflectionException;
import net.sf.morph.reflect.Reflector;
import net.sf.morph.util.ContainerUtils;
import net.sf.morph.util.TestClass;
import net.sf.morph.util.TestUtils;

/**
 * @author Matt Sgarlata
 * @since Nov 20, 2004
 */
public class ObjectReflectorTestCase extends BaseReflectorTestCase {

	protected List createNonReflectableObjects() {
		return null;
	}
	
	public void testGettersAndSetters() {
		TestClass test = new TestClass();
		
		getBeanReflector().set(test, "anObject", new Long(13));
		assertEquals(test.getAnObject(), new Long(13));
		assertEquals(getBeanReflector().get(test, "anObject"), new Long(13));
		
		getBeanReflector().set(test, "myInteger", new Integer(6));
		assertEquals(new Integer(test.getMyInteger()), new Integer(6));
		assertEquals(getBeanReflector().get(test, "myInteger"), new Integer(6));
		
		Number[] modifiedNumberArray = { new BigDecimal(6.2), new Long(2) };
		test.funkyArray = new Number[2];
		getBeanReflector().set(test, "funkyArray", modifiedNumberArray);
		TestUtils.assertEquals(modifiedNumberArray, test.funkyArray);
		TestUtils.assertEquals(modifiedNumberArray, getBeanReflector().get(test, "funkyArray"));
	}
	
	public void testGetPropertyNames() {
		TestClass test = new TestClass();
		String[] propertyNames = getBeanReflector().getPropertyNames(test);
		assertTrue(ContainerUtils.contains(propertyNames, "string"));
		assertTrue(ContainerUtils.contains(propertyNames, "myInteger"));
		assertTrue(ContainerUtils.contains(propertyNames, "myLongValue"));
		assertTrue(ContainerUtils.contains(propertyNames, "myMap"));
		assertTrue(ContainerUtils.contains(propertyNames, "anObject"));
		assertTrue(ContainerUtils.contains(propertyNames, "array"));
		assertTrue(ContainerUtils.contains(propertyNames, "numberArray"));
		assertTrue(ContainerUtils.contains(propertyNames, "bigDecimal"));
		assertTrue(ContainerUtils.contains(propertyNames, "funkyArray"));
		assertFalse(ContainerUtils.contains(propertyNames, "methodThatIsNotAProperty"));
		assertFalse(ContainerUtils.contains(propertyNames, "methodThatIsNotAProperty2"));
	}
	
	public void testIsWriteable() {
		TestClass test = new TestClass();
		assertTrue(getBeanReflector().isWriteable(test, "string"));
		assertTrue(getBeanReflector().isWriteable(test, "myInteger"));
		assertTrue(getBeanReflector().isWriteable(test, "myLongValue"));
		assertTrue(getBeanReflector().isWriteable(test, "myMap"));
		assertTrue(getBeanReflector().isWriteable(test, "anObject"));
		assertTrue(getBeanReflector().isWriteable(test, "array"));
		assertTrue(getBeanReflector().isWriteable(test, "numberArray"));
		assertTrue(getBeanReflector().isWriteable(test, "bigDecimal"));
		assertTrue(getBeanReflector().isWriteable(test, "funkyArray"));
		assertFalse(getBeanReflector().isWriteable(test, "methodThatIsNotAProperty"));
		assertFalse(getBeanReflector().isWriteable(test, "methodThatIsNotAProperty2"));
		assertFalse(getBeanReflector().isWriteable(test, "invalidProperty"));
	}
	
	public void testIsReadable() {
		TestClass test = new TestClass();
		assertTrue(getBeanReflector().isReadable(test, "string"));
		assertTrue(getBeanReflector().isReadable(test, "myInteger"));
		assertTrue(getBeanReflector().isReadable(test, "myLongValue"));
		assertTrue(getBeanReflector().isReadable(test, "myMap"));
		assertTrue(getBeanReflector().isReadable(test, "anObject"));
		assertTrue(getBeanReflector().isReadable(test, "array"));
		assertTrue(getBeanReflector().isReadable(test, "numberArray"));
		assertTrue(getBeanReflector().isReadable(test, "bigDecimal"));
		assertTrue(getBeanReflector().isReadable(test, "funkyArray"));
		assertFalse(getBeanReflector().isReadable(test, "methodThatIsNotAProperty"));
		assertFalse(getBeanReflector().isReadable(test, "methodThatIsNotAProperty2"));
		assertFalse(getBeanReflector().isReadable(test, "invalidProperty"));
	}
	
	public void testGetType() {
		super.testGetType();
		
		TestClass test = new TestClass();
		assertEquals(getBeanReflector().getType(test, "string"), String.class);
		assertEquals(getBeanReflector().getType(test, "myInteger"), int.class);
		assertEquals(getBeanReflector().getType(test, "myLongValue"), Long.class);
		assertEquals(getBeanReflector().getType(test, "myMap"), Map.class);
		assertEquals(getBeanReflector().getType(test, "anObject"), Object.class);
		assertEquals(getBeanReflector().getType(test, "array"), Object[].class);
		assertEquals(getBeanReflector().getType(test, "numberArray"), Number[].class);
		assertEquals(getBeanReflector().getType(test, "bigDecimal"), BigDecimal.class);
		assertEquals(getBeanReflector().getType(test, "funkyArray"), Number[].class);
	}
	
	protected List createReflectableObjects() {
		List beans = new ArrayList();
		beans.add(new ArrayList());
		beans.add(new HashMap());
		// this is probably not a fair test b/c the array reflector is intended
		// to be used with arrays
		//beans.add(new Object[0]);
		beans.add(TestClass.getEmptyObject());
		beans.add(TestClass.getFullObject());
		beans.add(TestClass.getPartialObject());
		return beans;
	}

	protected Reflector createReflector() {
		return new ObjectReflector();
	}

	protected void setPropertyToRandomValue(Object bean, String propertyName) throws Exception {
		// make sure writeable properties are writeable and
		// properties that aren't writeable aren't writeable
		if (getBeanReflector().isWriteable(bean, propertyName)) {
			try {
				getBeanReflector().set(bean, propertyName, null);
			}
			catch (ReflectionException e) {
				if (!((ObjectReflector) reflector).isPrimitiveSetter(bean, propertyName)) {
					fail("ReflectionException was thrown but it shouldn't have been.  Error msg: " + e.getMessage());
				}
			}
		}
		else {
			try {
				getBeanReflector().set(bean, propertyName, null);
				fail("The property shouldn't be writeable");
			}
			catch (ReflectionException e) { }
		}
	}

	
}
