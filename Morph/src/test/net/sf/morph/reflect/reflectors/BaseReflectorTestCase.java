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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import junit.framework.TestCase;
import net.sf.composite.util.CompositeUtils;
import net.sf.composite.util.ObjectUtils;
import net.sf.morph.reflect.BeanReflector;
import net.sf.morph.reflect.ContainerReflector;
import net.sf.morph.reflect.DecoratedReflector;
import net.sf.morph.reflect.GrowableContainerReflector;
import net.sf.morph.reflect.IndexedContainerReflector;
import net.sf.morph.reflect.InstantiatingReflector;
import net.sf.morph.reflect.MutableIndexedContainerReflector;
import net.sf.morph.reflect.ReflectionException;
import net.sf.morph.reflect.Reflector;
import net.sf.morph.reflect.SizableReflector;
import net.sf.morph.util.ClassUtils;
import net.sf.morph.util.ContainerUtils;
import net.sf.morph.util.TestUtils;

/**
 * @author Matt Sgarlata
 * @since Nov 20, 2004
 */
public abstract class BaseReflectorTestCase extends TestCase {

	protected Reflector reflector;
	protected List reflectableObjects;
	protected List nonReflectableObjects;
	protected List implicitPropertyNames;
	
	public BaseReflectorTestCase() {
		super();
	}
	public BaseReflectorTestCase(String arg0) {
		super(arg0);
	}
	
	protected void setUp() throws Exception {
		super.setUp();
		reflector = createReflector();
		reflectableObjects = createReflectableObjects();
		nonReflectableObjects = createNonReflectableObjects();
		implicitPropertyNames = createImplicitPropertyNames();
	}
	
	protected abstract Reflector createReflector();
	/**
	 * Returns a list of beans that are reflectable by this converter
	 */
	protected abstract List createReflectableObjects();
	/**
	 * Returns a list of beans that are not reflectable by this converter
	 */
	protected abstract List createNonReflectableObjects();
	
	protected List createImplicitPropertyNames() {
		List implicitPropertyNames = new ArrayList();
		if (canRunTest(BeanReflector.class)) {
			implicitPropertyNames.add(BeanReflector.IMPLICIT_PROPERTY_CLASS);
			implicitPropertyNames.add(BeanReflector.IMPLICIT_PROPERTY_PROPERTY_NAMES);
		}
		if (canRunTest(SizableReflector.class)) {
			implicitPropertyNames.add(SizableReflector.IMPLICIT_PROPERTY_SIZE);
		}
		
		return implicitPropertyNames;
	}
	
	public void testGetReflectableClasses() {
		assertFalse(ObjectUtils.isEmpty(reflector.getReflectableClasses()));
	}
	
	public void testNewInstance() throws Exception {
		if (canRunTest(InstantiatingReflector.class)) {
			// test arguments
			try {
				getInstantiatingReflector().newInstance(null, null);
				fail("a class must be specified to instantiate; if one is not specified, an exception should be thrown");
			}
			catch (ReflectionException e) { }
			
			for (int i=0; i<reflectableObjects.size(); i++) {
				getInstantiatingReflector().newInstance(reflectableObjects.get(i).getClass(), null);
			}
			
			if (nonReflectableObjects != null) {
				for (int i=0; i<nonReflectableObjects.size(); i++) {
					try {
						getInstantiatingReflector().newInstance(nonReflectableObjects.get(i).getClass(), null);
						fail("If the object isn't reflectable, it shouldn't be instantiatable by this reflector");
					}
					catch (ReflectionException e) { }
				}
			}
		}
	}
	
	public void testIsReflectable() {
		if (canRunTest(DecoratedReflector.class)) {
			// test argument checking
			try {
				getDecoratedReflector().isReflectable(null);
				fail("reflectedClass must be specified");
			}
			catch (ReflectionException e) { }
			
			// test that isReflectable returns true for all the reflectable objects
			for (int i=0; i<reflectableObjects.size(); i++) {
				assertTrue("should be able to reflect " + ObjectUtils.getObjectDescription(reflectableObjects.get(i)),
					getDecoratedReflector().isReflectable(reflectableObjects.get(i).getClass()));
			}			
		}
//		if (reflector instanceof CompositeReflector) {
//			try {
//				getDelegatingReflector().isReflectable(null, Reflector.class);
//				fail("reflectedClass must be specified");
//			}
//			catch (ReflectionException e) { }
//
//			// this call should succeed with no exception
//			getDelegatingReflector().isReflectable(Object.class, null);
//
//			try {
//				getDelegatingReflector().isReflectable(Object.class, Object.class);
//				fail("Object.class isn't a valid reflectorType (it must be a subclass of Reflector.class)");
//			}
//			catch (ReflectionException e) { }
//			// this should throw no exception
//			getDelegatingReflector().isReflectable(Object.class, BeanReflector.class);
//		}

		if (nonReflectableObjects != null) {
			for (int i=0; i<nonReflectableObjects.size(); i++) {
				// make sure 
				if (canRunTest(DecoratedReflector.class)) {
					assertFalse(
						"should not be able to reflect "
							+ ObjectUtils.getObjectDescription(nonReflectableObjects.get(i)),
						getDecoratedReflector().isReflectable(
							nonReflectableObjects.get(i).getClass()));
				}

				if (canRunTest(BeanReflector.class)) {
					Object bean = nonReflectableObjects.get(i);

					// check that calling all methods throws a reflectionexception
					try {
						getBeanReflector().getPropertyNames(bean);
						fail("Bean isn't reflectable");
					}
					catch (ReflectionException e) { }
					
					try {
						getBeanReflector().isReadable(bean, "myRandomProperty234");
						fail("Bean isn't reflectable");
					}
					catch (ReflectionException e) { }
					
					try {
						getBeanReflector().isWriteable(bean, "myOtherRandomProperty124");
						fail("Bean isn't reflectable");
					}
					catch (ReflectionException e) { }

					// check that calling all methods throws a reflectionexception
					try {
						getBeanReflector().get(bean, "myRandomProperty234");
						fail("Bean isn't reflectable");
					}
					catch (ReflectionException e) { }
					
					try {
						getBeanReflector().set(bean, "myOtherRandomProperty124", null);
						fail("Bean isn't reflectable");
					}
					catch (ReflectionException e) { }
				}				
			}
		}
	}
	
	public void testIterator() {
		if (canRunTest(ContainerReflector.class)) {
			try {
				getContainerReflector().getIterator(null);
				fail("Cannot retrieve an iterator over a null collection");
			}
			catch (ReflectionException e) { }
			
			for (int i=0; i<reflectableObjects.size(); i++) {
				// make sure we can get an iterator
				Iterator iterator = getContainerReflector().getIterator(reflectableObjects.get(i));
				// make sure the iterator works
				while (iterator.hasNext()) {
					iterator.next();
				}
			}
		}
	}
	
	public void testGetContainedType() {
		if (canRunTest(ContainerReflector.class)) {
			try {
				getContainerReflector().getContainedType(null);
				fail("Should not be able to tell what's inside a null container");
			}
			catch (ReflectionException e) { }
			
			for (int i=0; i<reflectableObjects.size(); i++) {
				Class containedType = getContainerReflector().getContainedType(reflectableObjects.get(i).getClass());
				if (containedType == null) {
					fail("The contained type was null");
				}
			}
		}
	}
	
	public void testGetSize() {
		if (canRunTest(SizableReflector.class)) {
			SizableReflector r = getSizableReflector();

			try {
				r.getSize(null);
				fail("Can't determine size of null object");
			}
			catch (ReflectionException e) { }
		}
	}
	
	public void testGetArgumentChecking() {
		if (canRunTest(BeanReflector.class)) {
			try {
				getBeanReflector().get(null, "property");
				fail("Should not be able to get properties on null beans");
			}
			catch (ReflectionException e) { }
			try {
				getBeanReflector().get(reflectableObjects.get(0), "");
				fail("Should not be able to get empty properties");
			}
			catch (ReflectionException e) { }
			try {
				getBeanReflector().get(reflectableObjects.get(0), null);
				fail("Should not be able to get null properties");
			}
			catch (ReflectionException e) { }
			
		}

		if (canRunTest(IndexedContainerReflector.class)) {
			IndexedContainerReflector ir = getIndexedContainerReflector();
			
			try {
				ir.get(null, 0);
				fail("Can't retrieve a value from an empty object");
			}
			catch (ReflectionException e) { };
			
			try {
				ir.get(new Object(), -1);
				fail("Can't retrieve a value from an object with a negative index");
			}
			catch (ReflectionException e) { };
			
			if (getReflector() instanceof SizableReflector) {
				for (int i=0; i<reflectableObjects.size(); i++) {
					Object reflectable = reflectableObjects.get(i);
					try {
						ir.get(reflectable, ((SizableReflector) ir).getSize(reflectable));
						fail("Can't retrieve items that are out of bounds");
					}
					catch (ReflectionException e) { }

				}
			}
		}
	}
	
	public void testSetArgumentChecking() {
		if (canRunTest(BeanReflector.class)) {
			try {
				getBeanReflector().set(null, "property", "value");
				fail("Should not be able to set properties on null beans");
			}
			catch (ReflectionException e) { }
			try {
				getBeanReflector().set(reflectableObjects.get(0), "", "value");
				fail("Should not be able to set empty properties");
			}
			catch (ReflectionException e) { }
			try {
				getBeanReflector().set(reflectableObjects.get(0), null, "value");
				fail("Should not be able to set null properties");
			}
			catch (ReflectionException e) { }
			
		}
		
		if (canRunTest(MutableIndexedContainerReflector.class)) {
			try {
				getMutableIndexedReflector().set(null, 0, null);
				fail("Can't set a property on an empty object");
			}
			catch (ReflectionException e) { }
			
			try {
				getMutableIndexedReflector().set(new Object(), -1, null);
				fail("Invalid index should throw an exception");
			}
			catch (ReflectionException e) { }
			
			if (getMutableIndexedReflector() instanceof SizableReflector) {
				for (int i=0; i<reflectableObjects.size(); i++) {
					Object reflectable = reflectableObjects.get(i);
					try {
						getMutableIndexedReflector().set(reflectable,
							getSizableReflector().getSize(reflectable), null);
						fail("Can't retrieve items that are out of the bounds");
					}
					catch (ReflectionException e) { }
				}
			}
		}
	}
	
	public void testAddArgumentChecking() {
		if (canRunTest(GrowableContainerReflector.class)) {
			try {
				getGrowableContainerReflector().add(null, null);
				fail("Should not be able to add items to a null collection!");
			}
			catch (ReflectionException e) { }
		}
	}
	
	public void testGetForIndexedReflectors() {
		if (canRunTest(IndexedContainerReflector.class)) {
			// loop through all the reflectable objects
			for (int i=0; i<reflectableObjects.size(); i++) {
				Object reflectable = reflectableObjects.get(i);
				for (int j=0; j<getSizableReflector().getSize(reflectable); j++) {
					getIndexedContainerReflector().get(reflectable, j);
				}
			}
		}
	}
	
	public void testSetForMutableIndexedReflectors() throws ReflectionException, InstantiationException, IllegalAccessException {
		if (canRunTest(MutableIndexedContainerReflector.class)) {
			// loop through all the reflectable objects
			for (int i=0; i<reflectableObjects.size(); i++) {
				Object reflectable = reflectableObjects.get(i);
				if (ClassUtils.inheritanceContains(getMutableIndexedReflector().getReflectableClasses(), reflectable.getClass())) {
					for (int j=0; j<getSizableReflector().getSize(reflectable); j++) {
						if (reflectable == null || !getContainerReflector().getContainedType(reflectable.getClass()).isPrimitive()) {
							getMutableIndexedReflector().set(reflectable, j, null);
						}
						else {
							getMutableIndexedReflector().set(reflectable, j, TestUtils.getInstance(ClassUtils.getContainedClass(reflectable.getClass())));
						}
					}
				}
			}
		}
	}
	
	/**
	 * Do basic tests of the BeanReflector.getType method. Subclasses may
	 * override this method to perform additional tests.
	 */
	public void testGetType() {
		if (canRunTest(BeanReflector.class)) {
			try {
				getBeanReflector().getType(null, "property");
				fail("Shouldn't be able to specify a null bean");
			}
			catch (ReflectionException e) { }
			try {
				getBeanReflector().getType(new Object(), "");
				fail("Shouldn't be able to specify an empty property name");
			}
			catch (ReflectionException e) { }
			try {				
				getBeanReflector().getType(new Object(), null);
				fail("Shouldn't be able to specify a null property name");
			}
			catch (ReflectionException e) { }
			try {
				if (getBeanReflector() instanceof BaseReflector) {
					BaseReflector reflector = (BaseReflector) getBeanReflector();
					getBeanReflector().getType(reflectableObjects.get(0), "invalidPropertyName");
					// if this is a strictly typed reflector
					if (reflector.isStrictlyTyped()) {
						// the getType call above should have failed
						fail("For a strictly typed reflector, you shouldn't be able to retrieve the type of a property that doesn't exist");
					}
				}
			}
			catch (ReflectionException e) { }

			assertEquals(Class.class, getBeanReflector().getType(
				reflectableObjects.get(0),
				BeanReflector.IMPLICIT_PROPERTY_CLASS));
			assertEquals(String[].class, getBeanReflector().getType(
				reflectableObjects.get(0),
				BeanReflector.IMPLICIT_PROPERTY_PROPERTY_NAMES));
			assertEquals(int.class,
				getBeanReflector().getType(reflectableObjects.get(0),
					BeanReflector.IMPLICIT_PROPERTY_SIZE));

		}
	}
	
	public void testIsReadableArgumentChecking() {
		if (canRunTest(BeanReflector.class)) {
			try {
				getBeanReflector().isReadable(null, "someProperty");
				fail("Shouldn't be able to read properties from a null bean");
			}
			catch (ReflectionException e) { }
			
			try {
				getBeanReflector().isReadable(new Object(), null);
				fail("Shouldn't be able to read a null property");
			}
			catch (ReflectionException e) { }
			
			try {
				getBeanReflector().isReadable(new Object(), "");
				fail("Shouldn't be able to read an empty property");
			}
			catch (ReflectionException e) { }
		}
	}
	
	public void testIsWriteableArgumentChecking() {
		if (canRunTest(BeanReflector.class)) {
			try {
				getBeanReflector().isWriteable(null, "someProperty");
				fail("Shouldn't be able to write properties from a null bean");
			}
			catch (ReflectionException e) { }
			
			try {
				getBeanReflector().isWriteable(new Object(), null);
				fail("Shouldn't be able to write a null property");
			}
			catch (ReflectionException e) { }
			
			try {
				getBeanReflector().isWriteable(new Object(), "");
				fail("Shouldn't be able to write an empty property");
			}
			catch (ReflectionException e) { }
		}
	}
	
	public void testBeanReflectors() throws Exception {
		if (canRunTest(BeanReflector.class)) {
			// test beans that are reflectable
			if (reflectableObjects != null) {
				// loop through all the beans
				for (int i=0; i<reflectableObjects.size(); i++) {
					Object bean = reflectableObjects.get(i);
					String[] propertyNames = getBeanReflector().getPropertyNames(bean);
					
					// check that calling isReadable and isWriteable on random
					// methods doesn't throw an exception
					getBeanReflector().isReadable(bean, "myRandomProperty234");
					getBeanReflector().isWriteable(bean, "myOtherRandomProperty124");
					
					doTestImplicitProperties(bean, propertyNames);
					
					doTestProperties(bean, propertyNames);
				}
			}
		}
	}
	
	protected void doTestProperties(Object bean, String[] propertyNames) throws ReflectionException, Exception, InstantiationException, IllegalAccessException {
		// test out all the bean's properties
		for (int j=0; j<propertyNames.length; j++) {
			
			String name = propertyNames[j];
			Class type = getBeanReflector().getType(bean, name);
			// if the property is readable
			if (getBeanReflector().isReadable(bean, name)) {
				doTestReadableProperty(bean, name);
			}
			// if the property isn't readable
			else {
				doTestUnreadableProperty(bean, name, type);
			}
		}
	}
	
	protected void doTestUnreadableProperty(Object bean, String name, Class type) throws ReflectionException, Exception, InstantiationException, IllegalAccessException {
		// make sure the get method doesn't work
		try {
			getBeanReflector().get(bean, name);
			fail("The property '" + name + "' is not readable so an exception should be thrown when the get method is called");
		}
		catch (ReflectionException e) { }
		
//		if (isWeaklyTypedBeanReflector()) {
//			assertEquals(
//				"Weakly typed reflectors should return Object.class when getType is called on an unreadable property",
//				Object.class, getBeanReflector().getType(bean, name));			
//		}
//		else {
//			try {
//				getBeanReflector().getType(bean, name);
//				fail("Strongly typed reflectors should throw an exception if the getType method is called ")
//			}
//			catch (ReflectionException e) { }
//		}
		

		if (getBeanReflector().isWriteable(bean, name)) {
			// make sure the set method works
			setPropertyToRandomValue(bean, name);
		}
		else {
			// make sure the set method doesn't work
			try {
				getBeanReflector().set(bean, name, TestUtils.getInstance(type));
				fail("The property shouldn't be writeable");
			}
			catch (ReflectionException e) { }
		}
	}
	
	protected void doTestReadableProperty(Object bean, String name) throws ReflectionException {
		Object value = getBeanReflector().get(bean, name);
		
		// if the property is writeable
		if (getBeanReflector().isWriteable(bean, name)) {
			// make sure the set method works
			getBeanReflector().set(bean, name, value);
		}
		else {
			// make sure the set method doesn't work
			try {
				getBeanReflector().set(bean, name, value);
				fail("The property shouldn't be writeable");
			}
			catch (ReflectionException e) { }
		}
	}
	
	protected void doTestImplicitProperties(Object bean, String[] propertyNames) throws ReflectionException {
		// test out the implicit properties
		for (int j=0; j<implicitPropertyNames.size(); j++) {
			// make sure getPropertyNames does *not* return the implicit
			// property names
			String implicitPropertyName = (String) implicitPropertyNames.get(j);
			assertFalse(
				"The '"
					+ implicitPropertyName
					+ "' property is an implicit property and should not be returned by the getPropertyNames method",
				ContainerUtils.contains(propertyNames, implicitPropertyName));
			// make sure all the implicit properties have values
			assertNotNull(getBeanReflector().get(bean,
				implicitPropertyName));
		}
	}
	
	protected void setPropertyToRandomValue(Object bean, String propertyName) throws Exception {
		if (canRunTest(BeanReflector.class)) {
			// make sure writeable properties are writeable and
			// properties that aren't writeable aren't writeable
			if (getBeanReflector().isWriteable(bean, propertyName)) {
				Class type = getBeanReflector().getType(bean, propertyName);
				if (type.isPrimitive()) {
					getBeanReflector().set(bean, propertyName, TestUtils.getInstance(type));
				}
				else {
					getBeanReflector().set(bean, propertyName, null);
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
	
	protected boolean canRunTest(Class reflectorType) {
//		return CompositeUtils.isSpecializable(getReflector(), reflectorType);
		return reflectorType.isAssignableFrom(getReflector().getClass());
	}

	public Reflector getReflector() {
		return reflector;
	}
	
	public Reflector getReflector(Class reflectorType) {
		return (Reflector) CompositeUtils.specialize(getReflector(), reflectorType);
//		return getReflector();
	}

	protected BeanReflector getBeanReflector() {
		return (BeanReflector) getReflector(BeanReflector.class);
	}

	protected ContainerReflector getContainerReflector() {
		return (ContainerReflector) getReflector(ContainerReflector.class);
	}
	
	protected IndexedContainerReflector getIndexedContainerReflector() {
		return (IndexedContainerReflector) getReflector(IndexedContainerReflector.class);
	}
	
	protected MutableIndexedContainerReflector getMutableIndexedReflector() {
		return (MutableIndexedContainerReflector) getReflector(MutableIndexedContainerReflector.class);
	}

	protected SizableReflector getSizableReflector() {
		return (SizableReflector) getReflector(SizableReflector.class);
	}
	
	protected InstantiatingReflector getInstantiatingReflector() {
		return (InstantiatingReflector) getReflector(InstantiatingReflector.class);
	}
	
	protected DecoratedReflector getDecoratedReflector() {
		return (DecoratedReflector) getReflector(DecoratedReflector.class);
	}
	
	protected GrowableContainerReflector getGrowableContainerReflector() {
		return (GrowableContainerReflector) getReflector(GrowableContainerReflector.class);
	}
}