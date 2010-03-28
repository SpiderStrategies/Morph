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
package net.sf.morph2.reflect.reflectors;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import net.sf.composite.SpecializableComposite;
import net.sf.composite.StrictlyTypedComposite;
import net.sf.morph2.reflect.BeanReflector;
import net.sf.morph2.reflect.CompositeReflector;
import net.sf.morph2.reflect.ContainerReflector;
import net.sf.morph2.reflect.DecoratedReflector;
import net.sf.morph2.reflect.GrowableContainerReflector;
import net.sf.morph2.reflect.IndexedContainerReflector;
import net.sf.morph2.reflect.InstantiatingReflector;
import net.sf.morph2.reflect.MutableIndexedContainerReflector;
import net.sf.morph2.reflect.NoReflectorFoundException;
import net.sf.morph2.reflect.ReflectionException;
import net.sf.morph2.reflect.Reflector;
import net.sf.morph2.reflect.SizableReflector;
import net.sf.morph2.util.ClassUtils;
import net.sf.morph2.util.ContainerUtils;
import net.sf.morph2.util.ReflectorUtils;

/**
 * Delegating Reflector that... is stubborn.
 * 
 * @author Matt Sgarlata
 * @since Morph 1.1 (Oct 25, 2007)
 */
//TODO explain properly above
public class StubbornDelegatingReflector extends BaseCompositeReflector implements
		DecoratedReflector, StrictlyTypedComposite, SpecializableComposite, BeanReflector,
		ContainerReflector, GrowableContainerReflector, IndexedContainerReflector,
		InstantiatingReflector, MutableIndexedContainerReflector, CompositeReflector, Cloneable {

	/**
	 * Create a new StubbornDelegatingReflector instance.
	 */
	public StubbornDelegatingReflector() {
		this(null);
	}

	/**
	 * Create a new StubbornDelegatingReflector instance.
	 * @param components
	 */
	public StubbornDelegatingReflector(Object[] components) {
		setComponents(components);
	}

	// internal state initialization/validation

	/**
	 * {@inheritDoc}
	 */
	protected void initializeImpl() throws Exception {
		super.initializeImpl();
		getComponentValidator().validate(this);
	}

	/**
	 * {@inheritDoc}
	 */
	protected Class[] getReflectableClassesImpl() {
		Set set = ContainerUtils.createOrderedSet();
		Object[] reflectors = getComponents();
		for (int i = 0; i < reflectors.length; i++) {
			Class[] reflectableClasses = ((Reflector) reflectors[i]).getReflectableClasses();
			for (int j = 0; j < reflectableClasses.length; j++) {
				set.add(reflectableClasses[j]);
			}
		}
		return (Class[]) set.toArray(new Class[set.size()]);
	}

	// bean reflectors

	/**
	 * {@inheritDoc}
	 */
	protected Object getImpl(Object bean, String propertyName) throws Exception {
		Object[] reflectors = getComponents();
		boolean reflectorFound = false;
		Object value = null;
		for (int i = 0; i < reflectors.length; i++) {
			if (ReflectorUtils.isReflectable((Reflector) reflectors[i], bean, BeanReflector.class)) {
				reflectorFound = true;
				BeanReflector beanReflector = (BeanReflector) reflectors[i];
				value = beanReflector.get(bean, propertyName);
				if (value != null) {
					return value;
				}
			}
		}

		if (reflectorFound) {
			// value will be null, but returning value here for the sake of
			// clarity
			return value;
		}
		else {
			throw new NoReflectorFoundException(bean, BeanReflector.class);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	protected String[] getPropertyNamesImpl(Object bean) throws Exception {
		Set propertyNames = ContainerUtils.createOrderedSet();
		Object[] reflectors = getComponents();
		boolean reflectorFound = false;
		for (int i = 0; i < reflectors.length; i++) {
			if (ReflectorUtils.isReflectable((Reflector) reflectors[i], bean, BeanReflector.class)) {
				reflectorFound = true;
				BeanReflector beanReflector = (BeanReflector) reflectors[i];
				String[] propertyNamesArray = beanReflector.getPropertyNames(bean);
				List propertyNamesList = Arrays.asList(propertyNamesArray);
				propertyNames.addAll(propertyNamesList);
			}
		}
		if (reflectorFound) {
			return (String[]) propertyNames.toArray(new String[propertyNames.size()]);
		}
		else {
			throw new NoReflectorFoundException(bean, BeanReflector.class);
		}
	}

	/**
	 * Default implementation finds the first reflector that returns a non-null
	 * value for the given property and returns the type of that value.  If no
	 * non-null value could be found, returns the type that is returned by
	 * the first BeanReflector that is a component of this reflector.
	 */
	protected Class getTypeImpl(Object bean, String propertyName) throws Exception {
		// first search for a non-null value
		Object[] reflectors = getComponents();
		Object value = null;
		for (int i = 0; i < reflectors.length; i++) {
			if (ReflectorUtils.isReflectable((Reflector) reflectors[i], bean, BeanReflector.class)) {
				BeanReflector beanReflector = (BeanReflector) reflectors[i];
				value = beanReflector.get(bean, propertyName);
				if (value != null) {
					break;
				}
			}
		}
		// if a non-null value was found, return that value's type
		if (value != null) {
			return ClassUtils.getClass(value);
		}

		// if no non-null value was found, return the type of that is returned
		// by the first BeanReflector that is a component of this reflector
		reflectors = getComponents();
		for (int i = 0; i < reflectors.length; i++) {
			if (ReflectorUtils.isReflectable((Reflector) reflectors[i], bean, BeanReflector.class)) {
				BeanReflector reflector = (BeanReflector) reflectors[i];
				return reflector.getType(bean, propertyName);
			}
		}

		throw new NoReflectorFoundException(bean, BeanReflector.class);
	}

	/**
	 * {@inheritDoc}
	 */
	protected boolean isReadableImpl(Object bean, String propertyName) throws Exception {
		Object[] reflectors = getComponents();
		boolean reflectorFound = false;
		for (int i = 0; i < reflectors.length; i++) {
			if (ReflectorUtils.isReflectable((Reflector) reflectors[i], bean, BeanReflector.class)) {
				reflectorFound = true;
				BeanReflector beanReflector = (BeanReflector) reflectors[i];
				if (beanReflector.isReadable(bean, propertyName)) {
					return true;
				}
			}
		}

		if (reflectorFound) {
			// if we reached here without returning true, the property isn't
			// readable
			return false;
		}
		else {
			throw new NoReflectorFoundException(bean, BeanReflector.class);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	protected boolean isWriteableImpl(Object bean, String propertyName) throws Exception {
		Object[] reflectors = getComponents();
		boolean reflectorFound = false;
		for (int i = 0; i < reflectors.length; i++) {
			if (ReflectorUtils.isReflectable((Reflector) reflectors[i], bean, BeanReflector.class)) {
				reflectorFound = true;
				BeanReflector reflector = (BeanReflector) reflectors[i];
				if (reflector.isWriteable(bean, propertyName)) {
					return true;
				}
			}
		}

		if (reflectorFound) {
			// if we reached here without returning true, the property isn't
			// readable
			return false;
		}
		else {
			throw new NoReflectorFoundException(bean, BeanReflector.class);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	protected void setImpl(Object bean, String propertyName, Object value) throws Exception {
		Object[] reflectors = getComponents();
		Exception exception = null;
		for (int i = 0; i < reflectors.length; i++) {
			if (ReflectorUtils.isReflectable((Reflector) reflectors[i], bean, BeanReflector.class)) {
				BeanReflector reflector = (BeanReflector) reflectors[i];
				if (reflector.isWriteable(bean, propertyName)) {
					try {
						reflector.set(bean, propertyName, value);
						return;
					} catch (ReflectionException e) {
						exception = e;
					}
				}
			}
		}

		if (exception == null) {
			throw new NoReflectorFoundException(bean, BeanReflector.class);
		}
		else {
			throw exception;
		}
	}

	// container reflectors

	/**
	 * {@inheritDoc}
	 */
	protected Iterator getIteratorImpl(Object container) throws Exception {
		Object[] reflectors = getComponents();
		Exception exception = null;
		for (int i = 0; i < reflectors.length; i++) {
			if (ReflectorUtils.isReflectable((Reflector) reflectors[i], container,
					ContainerReflector.class)) {
				ContainerReflector reflector = (ContainerReflector) reflectors[i];
				try {
					return reflector.getIterator(container);
				} catch (ReflectionException e) {
					exception = e;
				}
			}
		}

		if (exception == null) {
			throw new NoReflectorFoundException(container, ContainerReflector.class);
		}
		else {
			throw exception;
		}
	}

	/**
	 * {@inheritDoc}
	 */
	protected Class getContainedTypeImpl(Class clazz) throws Exception {
		Object[] reflectors = getComponents();
		Exception exception = null;
		for (int i = 0; i < reflectors.length; i++) {
			if (ReflectorUtils.isReflectable((Reflector) reflectors[i], clazz,
					ContainerReflector.class)) {
				ContainerReflector reflector = (ContainerReflector) reflectors[i];
				try {
					return reflector.getContainedType(clazz);
				} catch (ReflectionException e) {
					exception = e;
				}
			}
		}

		if (exception == null) {
			throw new NoReflectorFoundException(clazz, ContainerReflector.class);
		}
		else {
			throw exception;
		}
	}

	// sizable reflectors

	/**
	 * {@inheritDoc}
	 */
	protected int getSizeImpl(Object container) throws Exception {
		Object[] reflectors = getComponents();
		Exception exception = null;
		for (int i = 0; i < reflectors.length; i++) {
			if (ReflectorUtils.isReflectable((Reflector) reflectors[i], container,
					ContainerReflector.class)) {
				SizableReflector reflector = (SizableReflector) reflectors[i];
				try {
					return reflector.getSize(container);
				} catch (ReflectionException e) {
					exception = e;
				}
			}
		}

		if (exception == null) {
			throw new NoReflectorFoundException(container, ContainerReflector.class);
		}
		else {
			throw exception;
		}
	}

	// growable reflectors

	/**
	 * {@inheritDoc}
	 */
	protected boolean addImpl(Object container, Object value) throws Exception {
		Object[] reflectors = getComponents();
		Exception exception = null;
		for (int i = 0; i < reflectors.length; i++) {
			if (ReflectorUtils.isReflectable((Reflector) reflectors[i], container,
					GrowableContainerReflector.class)) {
				GrowableContainerReflector reflector = (GrowableContainerReflector) reflectors[i];
				try {
					return reflector.add(container, value);
				} catch (ReflectionException e) {
					exception = e;
				}
			}
		}

		if (exception == null) {
			throw new NoReflectorFoundException(container, GrowableContainerReflector.class);
		}
		else {
			throw exception;
		}
	}

	// indexed reflectors

	/**
	 * {@inheritDoc}
	 */
	protected Object getImpl(Object container, int index) throws Exception {
		Object[] reflectors = getComponents();
		Exception exception = null;
		for (int i = 0; i < reflectors.length; i++) {
			if (ReflectorUtils.isReflectable((Reflector) reflectors[i], container,
					IndexedContainerReflector.class)) {
				IndexedContainerReflector reflector = (IndexedContainerReflector) reflectors[i];
				try {
					return reflector.get(container, index);
				} catch (ReflectionException e) {
					exception = e;
				}
			}
		}

		if (exception == null) {
			throw new NoReflectorFoundException(container, IndexedContainerReflector.class);
		}
		else {
			throw exception;
		}
	}

	// mutable indexed reflectors

	/**
	 * {@inheritDoc}
	 */
	protected Object setImpl(Object container, int index, Object propertyValue) throws Exception {
		Exception exception = null;
		Object[] reflectors = getComponents();
		for (int i = 0; i < reflectors.length; i++) {
			if (ReflectorUtils.isReflectable((Reflector) reflectors[i], container,
					MutableIndexedContainerReflector.class)) {
				MutableIndexedContainerReflector reflector = (MutableIndexedContainerReflector) reflectors[i];
				try {
					return reflector.set(container, index, propertyValue);
				} catch (ReflectionException e) {
					exception = e;
				}
			}
		}

		if (exception == null) {
			throw new NoReflectorFoundException(container, MutableIndexedContainerReflector.class);
		}
		else {
			throw exception;
		}
	}

	// instantiating reflectors	

	/**
	 * {@inheritDoc}
	 */
	protected Object newInstanceImpl(Class clazz, Object parameters) throws Exception {
		Object[] reflectors = getComponents();
		Exception exception = null;
		for (int i = 0; i < reflectors.length; i++) {
			if (ReflectorUtils.isReflectable((Reflector) reflectors[i], clazz,
					InstantiatingReflector.class)) {
				InstantiatingReflector reflector = (InstantiatingReflector) reflectors[i];
				try {
					return reflector.newInstance(clazz, parameters);
				} catch (ReflectionException e) {
					exception = e;
				}
			}
		}

		if (exception == null) {
			throw new NoReflectorFoundException(clazz, InstantiatingReflector.class);
		}
		else {
			throw exception;
		}
	}

	/**
	 * {@inheritDoc}
	 */
	protected boolean isReflectableImpl(Class reflectedType, Class reflectorType)
			throws ReflectionException {
		Object[] reflectors = getComponents();
		for (int i = 0; i < reflectors.length; i++) {
			Reflector reflector = (Reflector) reflectors[i];
			if (ReflectorUtils.isReflectable(reflector, reflectedType, reflectorType)) {
				return true;
			}
		}
		return false;
	}

}
