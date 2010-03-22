package net.sf.morph2.context.contexts;

import java.util.Map;

import junit.framework.TestCase;
import net.sf.composite.util.ObjectUtils;
import net.sf.morph2.context.Context;
import net.sf.morph2.reflect.BeanReflector;

/**
 * @author Matt Sgarlata
 * @since Nov 29, 2004
 */
public abstract class BaseContextTestCase extends TestCase {
	
	public BaseContextTestCase() {
		super();
	}
	public BaseContextTestCase(String arg0) {
		super(arg0);
	}
	
	protected Context context;
	
	protected abstract Context createContext();

	protected void setUp() throws Exception {
		super.setUp();
		context = createContext();
	}

	public void testGetPropertyNames() {
		// just check to make sure no exception is thrown
		context.getPropertyNames();
	}
	
	public void testGet() {
		// assumes all properties are readable
		String[] propertyNames = context.getPropertyNames();
		if (!ObjectUtils.isEmpty(propertyNames)) {
			for (int i=0; i<propertyNames.length; i++) {
				context.get(propertyNames[i]);
			}			
		}
		
		// try to read some bogus property name.
		assertNull(context.get("myBogusPropertyNameForTestingPurposes154813232"));
	}
	
	public void testSet() {
		// assumes all properties are writeable unless the context is a
		// reflector context and the reflector says the property isn't writeable
		String[] propertyNames = context.getPropertyNames();
		if (!ObjectUtils.isEmpty(propertyNames)) {
			for (int i=0; i<propertyNames.length; i++) {
				Object value = context.get(propertyNames[i]);
				
				if (!(context instanceof ReflectorHierarchicalContext &&
					!((ReflectorHierarchicalContext) context).getBeanReflector().isWriteable(((ReflectorHierarchicalContext) context).getDelegate(), propertyNames[i]))) {
					if (!propertyNames[i].equals(BeanReflector.IMPLICIT_PROPERTY_PROPERTY_NAMES)) {
						context.set(propertyNames[i], value);
						
						if (value != null && !value.getClass().isPrimitive()) {
							context.set(propertyNames[i], null);
						}
					}
				}
			}
		}
	}
	
	/**
	 * Tests the Map.put methods exposed by a context
	 */
	public void testPut() {
		String test = "test";
		Map map = new ContextDecorator(context);
		map.put(test, test);
		assertEquals(context.get(test), test);
	}
	
	protected void runBaseTests() {
		testGetPropertyNames();
		testGet();
		testSet();
		testPut();
	}
	
}
