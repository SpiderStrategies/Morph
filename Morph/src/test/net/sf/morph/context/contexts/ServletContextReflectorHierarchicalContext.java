package net.sf.morph.context.contexts;

import net.sf.morph.Morph;
import net.sf.morph.context.Context;
import net.sf.morph.reflect.BeanReflector;
import net.sf.morph.util.ContainerUtils;
import net.sf.morph.util.TestObjects;

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