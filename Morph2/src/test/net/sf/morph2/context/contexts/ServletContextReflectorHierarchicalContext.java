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
package net.sf.morph2.context.contexts;

import net.sf.morph2.Morph;
import net.sf.morph2.context.Context;
import net.sf.morph2.reflect.BeanReflector;
import net.sf.morph2.util.ContainerUtils;
import net.sf.morph2.util.TestObjects;

/**
 * @author Matt Sgarlata
 * @since Dec 21, 2004
 */
public class ServletContextReflectorHierarchicalContext extends
	BaseContextTestCase {

	protected Context createContext() {
		return new ReflectorHierarchicalContext((new TestObjects()).servletContext);
	}
	
	public void testGetAndSet() {
		Integer one = new Integer(1), two = new Integer(2), three = new Integer(3);
		String four = "4";
		String testing = "testing";
		
		assertFalse(ContainerUtils.contains(context.getPropertyNames(), BeanReflector.IMPLICIT_PROPERTY_PROPERTY_NAMES));
		
		context.set(testing, one);
		assertEquals(one, context.get(testing));
		assertEquals(Morph.get(context, testing), one);		
		assertTrue(ContainerUtils.contains(context.getPropertyNames(), testing));
		assertFalse(ContainerUtils.contains(context.getPropertyNames(), BeanReflector.IMPLICIT_PROPERTY_PROPERTY_NAMES));
		
//		runBaseTests();
		
		context.set(testing, two);
		assertEquals(two, context.get(testing));
		assertEquals(Morph.get(context, testing), two);
		assertTrue(ContainerUtils.contains(context.getPropertyNames(), testing));
		assertFalse(ContainerUtils.contains(context.getPropertyNames(), BeanReflector.IMPLICIT_PROPERTY_PROPERTY_NAMES));
		
//		runBaseTests();
		
		context.set(testing, three);
		assertEquals(three, context.get(testing));
		assertEquals(Morph.get(context, testing), three);
		assertTrue(ContainerUtils.contains(context.getPropertyNames(), testing));
		assertFalse(ContainerUtils.contains(context.getPropertyNames(), BeanReflector.IMPLICIT_PROPERTY_PROPERTY_NAMES));
		
//		runBaseTests();
		
		context.set(testing, four);
		assertEquals(four, context.get(testing));
		assertEquals(Morph.get(context, testing), four);
		assertTrue(ContainerUtils.contains(context.getPropertyNames(), testing));
		assertFalse(ContainerUtils.contains(context.getPropertyNames(), BeanReflector.IMPLICIT_PROPERTY_PROPERTY_NAMES));
		
		runBaseTests();

		// the "five" property doesn't exist in context, but
		// this should not cause an error
		Morph.get(context, "five");
	}


}
