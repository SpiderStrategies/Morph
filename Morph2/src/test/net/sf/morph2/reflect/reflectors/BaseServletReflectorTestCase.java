package net.sf.morph2.reflect.reflectors;


/**
 * @author Matt Sgarlata
 * @since Apr 5, 2005
 */
public abstract class BaseServletReflectorTestCase extends BaseReflectorTestCase {

	public void testIsReadable() {
		for (int i=0; i<reflectableObjects.size(); i++) {
			Object reflectableObject = reflectableObjects.get(i);
			assertTrue(getBeanReflector().isReadable(reflectableObject, "randomProperty1942"));
			assertTrue(getBeanReflector().isReadable(reflectableObject, "randomPropertyAbC"));
		}
	}
	
	public void testIsWriteable() {
		for (int i=0; i<reflectableObjects.size(); i++) {
			Object reflectableObject = reflectableObjects.get(i);
			assertTrue(getBeanReflector().isWriteable(reflectableObject, "randomProperty1432"));
			assertTrue(getBeanReflector().isWriteable(reflectableObject, "randomPropertyAbC"));
		}
	}

}
