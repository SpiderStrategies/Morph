package net.sf.morph.reflect.reflectors;

import java.util.ArrayList;
import java.util.List;

import net.sf.morph.reflect.Reflector;
import net.sf.morph.util.TestClass;
import net.sf.morph.util.TestObjects;

import org.apache.velocity.VelocityContext;

/**
 * @author Matt Sgarlata
 * @since Dec 29, 2004
 */
public class VelocityContextReflectorTestCase extends BaseReflectorTestCase {

	protected Reflector createReflector() {
		return new VelocityContextReflector();
	}

	protected List createReflectableObjects() {
		TestObjects testObjects = new TestObjects();
		
		List list = new ArrayList();
		list.add(new VelocityContext());
		list.add(new VelocityContext(testObjects.emptyMap));
		list.add(new VelocityContext(testObjects.singleElementMap));
		list.add(new VelocityContext(testObjects.multiElementMap));
		list.add(new VelocityContext(TestClass.getEmptyMap()));
		list.add(new VelocityContext(TestClass.getFullMap()));
		list.add(new VelocityContext(TestClass.getPartialMap()));
		return list;
	}

	protected List createNonReflectableObjects() {
		List list = new ArrayList();
		list.add(new Integer(2));
		list.add(new Long(4));
		list.add("something");
		list.add(new Object[] { "something" });
		list.add(new int[] { 1 });
		return list;
	}

}
