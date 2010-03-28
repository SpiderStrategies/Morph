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

import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.mock.web.MockServletContext;

/**
 * @author Matt Sgarlata
 * @since Nov 29, 2004
 */
public class HttpServletContextTestCase extends BaseContextTestCase {

	public HttpServletContextTestCase() {
		super();
	}
	public HttpServletContextTestCase(String arg0) {
		super(arg0);
	}
	protected MockHttpServletRequest request;
	protected MockHttpSession session;
	protected MockServletContext application;
	
	protected Context createContext() {
		application = new MockServletContext();
		request = new MockHttpServletRequest(application);
		session = new MockHttpSession(application);
		request.setSession(session);
		
		HttpServletContext context = new HttpServletContext(request);
		return context;
	}
	
	public void testInheritance() {
		Integer one = new Integer(1), two = new Integer(2), three = new Integer(3);
		String four = "4";
		String testing = "testing";
		
		assertFalse(ContainerUtils.contains(context.getPropertyNames(), BeanReflector.IMPLICIT_PROPERTY_PROPERTY_NAMES));
		
		application.setAttribute(testing, one);
		assertEquals(one, application.getAttribute(testing));
		assertEquals(one, context.get(testing));
		assertEquals(Morph.get(context, testing), one);		
		assertTrue(ContainerUtils.contains(context.getPropertyNames(), testing));
		assertFalse(ContainerUtils.contains(context.getPropertyNames(), BeanReflector.IMPLICIT_PROPERTY_PROPERTY_NAMES));
//		assertEquals("net.sf.morph2.context.contexts.HttpServletContext[testing=1]",
//			context.toString());
		
//		runBaseTests();
		
		session.setAttribute(testing, two);
		assertEquals(one, application.getAttribute(testing));
		assertEquals(two, session.getAttribute(testing));
		assertEquals(two, context.get(testing));
		assertEquals(Morph.get(context, testing), two);
		assertTrue(ContainerUtils.contains(context.getPropertyNames(), testing));
		assertFalse(ContainerUtils.contains(context.getPropertyNames(), BeanReflector.IMPLICIT_PROPERTY_PROPERTY_NAMES));
//		assertEquals("net.sf.morph2.context.contexts.HttpServletContext[testing=2]",
//			context.toString());
		
//		runBaseTests();
		
		request.setAttribute(testing, three);
		assertEquals(one, application.getAttribute(testing));
		assertEquals(two, session.getAttribute(testing));
		assertEquals(three, request.getAttribute(testing));
		assertEquals(three, context.get(testing));
		assertEquals(Morph.get(context, testing), three);
		assertTrue(ContainerUtils.contains(context.getPropertyNames(), testing));
		assertFalse(ContainerUtils.contains(context.getPropertyNames(), BeanReflector.IMPLICIT_PROPERTY_PROPERTY_NAMES));
//		assertEquals("net.sf.morph2.context.contexts.HttpServletContext[testing=3]",
//			context.toString());
		
//		runBaseTests();
		
		request.addParameter(testing, four);
		assertEquals(one, application.getAttribute(testing));
		assertEquals(two, session.getAttribute(testing));
		assertEquals(three, request.getAttribute(testing));
		assertEquals(four, context.get(testing));
		assertEquals(Morph.get(context, testing), four);
		assertTrue(ContainerUtils.contains(context.getPropertyNames(), testing));
		assertFalse(ContainerUtils.contains(context.getPropertyNames(), BeanReflector.IMPLICIT_PROPERTY_PROPERTY_NAMES));
//		assertEquals("net.sf.morph2.context.contexts.HttpServletContext[testing=4]",
//			context.toString());
		
//		runBaseTests();
		
		// the "five" property doesn't exist in context, but
		// this should not cause an error
		Morph.get(context, "five");
	}
	
// unfortunately this test isn't actually testing anything
//	public static class ClassWithBadProperty {
//		public void setBadyProperty(String badProperty) {
//			
//		}
//		public String getBadyProperty() {
//			return "";
//		}
//	}
//	
//	public void testGetBadyProperty() {
//		Context context = createContext();
//		ClassWithBadProperty badObject = new ClassWithBadProperty();
//		try {
//			context.set("badObject", badObject);
//			context.get("badObject.badProperty");
//		}
//		catch (MorphException e) {
//			// it's good if a Morph exception is thrown, bad if a
//			// StackOverflowException or some other type of exception is thrown
//			e.printStackTrace();
//		}
//	}

}
 
