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

import net.sf.morph.reflect.ContainerReflector;
import net.sf.morph.reflect.InstantiatingReflector;
import net.sf.morph.reflect.Reflector;
import net.sf.morph.util.TestObjects;
import net.sf.morph.util.TestUtils;

import org.springframework.mock.web.MockHttpServletRequest;

/**
 * @author Matt Sgarlata
 * @since Dec 21, 2004
 */
public class ServletRequestReflectorTestCase extends
	BaseServletRequestReflectorTestCase {

	protected Reflector createReflector() {
		return new ServletRequestReflector();
	}

	public void testGetType() {
		super.testGetType();
		assertEquals(String.class, getBeanReflector().getType(new TestObjects().servletRequest, "param1"));
		assertEquals(String[].class,
			getBeanReflector().getType(new TestObjects().servletRequest, "three"));
		
		assertEquals(Integer.class, getBeanReflector().getType(new TestObjects().servletRequest, "integer"));
		assertEquals(Integer.class, getBeanReflector().getType(new TestObjects().servletRequest, "integer"));
		assertEquals(String.class, getBeanReflector().getType(new TestObjects().servletRequest, "inBothParamsAndAttrs"));
	}

	public void testGet() {
		TestUtils.assertEquals("one", getBeanReflector().get(new TestObjects().servletRequest, "param1"));
		TestUtils.assertEquals(new TestObjects().threeStringsArray, getBeanReflector().get(new TestObjects().servletRequest, "three"));
		assertNull(getBeanReflector().get(new TestObjects().servletRequest, "four"));

		TestUtils.assertEquals("paramValue", getBeanReflector().get(new TestObjects().servletRequest, "inBothParamsAndAttrs"));
		TestUtils.assertEquals("one", getBeanReflector().get(new TestObjects().servletRequest, "attribute1"));
		TestUtils.assertEquals(new TestObjects().multidimensionalPrimitiveArray, getBeanReflector().get(new TestObjects().servletRequest, "multidimensionalPrimitiveArray"));
	}
	
	public void testIsReadable() {
		for (int i=0; i<reflectableObjects.size(); i++) {
			Object reflectableObject = reflectableObjects.get(i);
			assertTrue(getBeanReflector().isReadable(reflectableObject, "randomProperty1942"));
			assertTrue(getBeanReflector().isReadable(reflectableObject, "inBothParamsAndAttrs"));
			assertTrue(getBeanReflector().isReadable(reflectableObject, "attribute1"));			
		}
	}
	
	public void testIsWriteable() {
		for (int i=0; i<reflectableObjects.size(); i++) {
			Object reflectableObject = reflectableObjects.get(i);
			assertTrue(getBeanReflector().isWriteable(reflectableObject, "randomProperty1942"));
			assertTrue(getBeanReflector().isWriteable(reflectableObject, "inBothParamsAndAttrs"));
			assertTrue(getBeanReflector().isWriteable(reflectableObject, "attribute1"));
		}
	}	

	public void testSetNull() {
		MockHttpServletRequest req = new MockHttpServletRequest();
		req.setAttribute("test", "non-null");
		// this should not throw an exception
		getBeanReflector().set(req, "test", null);
	}

	protected boolean canRunTest(Class reflectorType) {
	    if (InstantiatingReflector.class.equals(reflectorType)) {
	    	return false;
	    }
	    if (ContainerReflector.class.isAssignableFrom(reflectorType)) {
	    	return false;
	    }
	    
	    return super.canRunTest(reflectorType);
    }
	
}
