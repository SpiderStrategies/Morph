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

import net.sf.morph.reflect.ReflectionException;
import net.sf.morph.reflect.Reflector;
import net.sf.morph.util.TestObjects;
import net.sf.morph.util.TestUtils;

/**
 * @author Matt Sgarlata
 * @since Dec 21, 2004
 */
public class ServletRequestParameterReflectorTestCase extends
	BaseServletRequestReflectorTestCase {

	protected Reflector createReflector() {
		return new ServletRequestParameterReflector();
	}

	public void testGetType() {
		super.testGetType();
		assertEquals(String.class, getBeanReflector().getType((new TestObjects()).servletRequest, "param1"));
		assertEquals(String.class, getBeanReflector().getType((new TestObjects()).servletRequest, "param2"));
		assertEquals(String[].class,
			getBeanReflector().getType((new TestObjects()).servletRequest, "three"));
		TestUtils.assertEquals(getBeanReflector().get((new TestObjects()).servletRequest, "param1"), "one");
		TestUtils.assertEquals(getBeanReflector().get((new TestObjects()).servletRequest, "param2"), "two");
		TestUtils.assertEquals(getBeanReflector().get((new TestObjects()).servletRequest, "three"), (new TestObjects()).threeStringsArray);
	}
	
	public void testGetHasNoError() {
		try {
			getBeanReflector().get((new TestObjects()).servletRequest, "four");
			fail("'four' isn't one of the (new TestObjects()).servletRequest parameters, so an exception should be thrown");
		}
		catch (ReflectionException e) { }
	}
	
	public void testIsReadable() {
		for (int i=0; i<reflectableObjects.size(); i++) {
			Object reflectableObject = reflectableObjects.get(i);
			assertFalse(getBeanReflector().isReadable(reflectableObject, "randomProperty1942"));
			assertFalse(getBeanReflector().isReadable(reflectableObject, "randomPropertyAbC"));
			assertFalse(getBeanReflector().isWriteable(reflectableObject, "param1"));
			assertFalse(getBeanReflector().isWriteable(reflectableObject, "param2"));
		}
	}
	
	public void testIsWriteable() {
		for (int i=0; i<reflectableObjects.size(); i++) {
			Object reflectableObject = reflectableObjects.get(i);
			assertFalse(getBeanReflector().isWriteable(reflectableObject, "randomProperty1432"));
			assertFalse(getBeanReflector().isWriteable(reflectableObject, "randomPropertyAbC"));
			assertFalse(getBeanReflector().isWriteable(reflectableObject, "param1"));
			assertFalse(getBeanReflector().isWriteable(reflectableObject, "param2"));
		}
	}	

}
