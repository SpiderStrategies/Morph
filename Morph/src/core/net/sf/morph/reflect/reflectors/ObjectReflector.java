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

import java.lang.reflect.Array;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;

import net.sf.composite.util.ObjectUtils;
import net.sf.morph.reflect.BeanReflector;
import net.sf.morph.reflect.ContainerReflector;
import net.sf.morph.reflect.InstantiatingReflector;
import net.sf.morph.reflect.ReflectionException;
import net.sf.morph.reflect.support.MethodHolder;
import net.sf.morph.reflect.support.ObjectIterator;
import net.sf.morph.reflect.support.ReflectionInfo;
import net.sf.morph.util.ClassUtils;
import net.sf.morph.util.ContainerUtils;


/**
 * <p>
 * A Reflector that exposes the properties of any Object as they are defined by
 * the <a href="http://java.sun.com/products/javabeans/index.jsp">JavaBeans </a>
 * specification. Also exposes any Object as a container.
 * </p>
 * 
 * <p>
 * When an object is exposed as a container, the <code>getContainer</code>
 * method returns an iterator that just iterates over the one reflected object.
 * The <code>getType</code> method returns the type as specified by the
 * property's setter method, if one is available.  If no mutator is available, the
 * <code>getType</code> method returns the type as specified by the property's
 * getter method. 
 * </p>
 * 
 * @author Matt Sgarlata
 * @author Alexander Volanis
 * @since Nov 7, 2004
 */
public class ObjectReflector extends BaseBeanReflector implements InstantiatingReflector, ContainerReflector {
	
	private static final Class[] REFLECTABLE_TYPES = new Class[] {
		Object.class
	};
	
	/**
	 * Indicates whether primitive assignments to <code>null</code> are allowed.
	 * If they are allowed, primitive assignments to <code>null</code> will be
	 * ignored.  If they are not allowed, a ReflectionException will be thrown.
	 */
	private boolean allowNullPrimitiveAssignment = true;
	
	private static final Map reflectionCache =
		Collections.synchronizedMap(new WeakHashMap());
	
	public Class[] getReflectableClassesImpl() {
		return REFLECTABLE_TYPES;
	}
	
	protected String[] getPropertyNamesImpl(Object bean) throws Exception {
		String[] propertyNames = getReflectionInfo(bean.getClass()).getPropertyNames();
		List propertyNamesWithoutClass = new ArrayList();
		for (int i=0; i<propertyNames.length; i++) {
			if (!BeanReflector.IMPLICIT_PROPERTY_CLASS.equals(propertyNames[i])) {
				propertyNamesWithoutClass.add(propertyNames[i]);
			}
		}
		return (String[]) propertyNamesWithoutClass.toArray(new String[propertyNamesWithoutClass.size()]);
	}
	
	private Method getMutator(Object bean, String propertyName) throws Exception {
		return getMethodHolder(bean, propertyName).getMutator();
	}
	
	private Method getAccessor(Object bean, String propertyName) throws Exception {
		return getMethodHolder(bean, propertyName).getAccessor();
	}
	
	private Method getIndexedMutator(Object bean, String propertyName) throws Exception {
		return getMethodHolder(bean, propertyName).getIndexedMutator();
	}
	
	private Method getIndexedAccessor(Object bean, String propertyName) throws Exception {
		return getMethodHolder(bean, propertyName).getIndexedAccessor();
	}
	
	private MethodHolder getMethodHolder(Object bean, String propertyName) {
		return getReflectionInfo(bean.getClass()).getMethodHolder(propertyName);
	}

	/**
	 * Returns the type of the property based on the parameter type for the
	 * property's setter method.  If there is no setter method available,
	 * returns the return type of the property accessor method.
	 */
	protected Class getTypeImpl(Object bean, String propertyName) throws Exception {
		if (getReflectionInfo(bean.getClass()).isWriteable(propertyName)) {
			Method mutator = getMutator(bean, propertyName);
			if (mutator == null) {
				return ClassUtils.getArrayClass(getIndexedMutator(bean, propertyName).getParameterTypes()[1]);
			}
			return mutator.getParameterTypes()[0];	
		}
		Method accessor = getAccessor(bean, propertyName);
		if (accessor == null) {
			Method indexedAccessor = getIndexedAccessor(bean, propertyName);
			return ClassUtils.getArrayClass(indexedAccessor.getReturnType());
		}
		return accessor.getReturnType();
	}
	
	protected boolean isReadableImpl(Object bean, String propertyName)
		throws Exception {
		return ContainerUtils.contains(getPropertyNames(bean), propertyName)
				&& getReflectionInfo(bean.getClass()).isReadable(propertyName);
	}
	
	protected boolean isWriteableImpl(Object bean, String propertyName)
		throws Exception {
		return ContainerUtils.contains(getPropertyNames(bean), propertyName)
				&& getReflectionInfo(bean.getClass()).isWriteable(propertyName);
	}
	
	/**
	 * Retrieves the value of the property <code>propertyName</code> in bean
	 * <code>bean</code>. If the property is an indexed property that is only
	 * accessible via an indexed getter method of the form <code>get(int)</code>,
	 * this implementation is O(n). Otherwise, this implementation is O(1).
	 */
	protected Object getImpl(Object bean, String propertyName) throws Exception {
		ReflectionInfo reflectionInfo = getReflectionInfo(bean.getClass());
		// if a simple getter is available
		if (reflectionInfo.getMethodHolder(propertyName).getAccessor() != null) {
			// use it
			return reflectionInfo.get(bean, propertyName);	
		}
		// we're using an indexed getter
		List contents = new ArrayList();
		boolean hasMoreElements = true;
		Exception exception = null;
		// try to read elements from the indexed getter
		while (hasMoreElements) {
			try {
				Object value = reflectionInfo.get(bean, propertyName,
					new Integer(contents.size()));
				contents.add(value);
			}
			catch (Exception e) {
				exception = e;
				hasMoreElements = false;
			}
		}
		
		// if there are no elements ...
		if (contents.size() == 0) {
			// ... and our exception was NullPointer, that probably means
			// there was no array available to start with, so return null
			if (isExceptionOfType(exception, NullPointerException.class)) {
				return null;
			}
			// ... and our exception was ArrayIndexOutOfBoundsException, 
			// return an empty array
			if (isExceptionOfType(exception, ArrayIndexOutOfBoundsException.class)) {
				return ClassUtils.createArray(getType(bean, propertyName).getComponentType(), 0);
			}
			// ... and we encountered a random exception
			// probably need to propagate this to the user
			throw (Exception) exception.fillInStackTrace();
		}
		// if there are some elements ...
		// ... and we found the end of the list of valid elements
		if (isExceptionOfType(exception, ArrayIndexOutOfBoundsException.class)) {
			// create a new array of the required type
			Object array = ClassUtils.createArray(getType(bean, propertyName).getComponentType(), contents.size());
			// copy the contents we've constructed into the array
			for (int i=0; i<contents.size(); i++) {
				Array.set(array, i, contents.get(i));
			}
			return array;
		}
		// .. and we encountered an exception
		// rethrow the exception
		throw (Exception) exception.fillInStackTrace();
	}
	
	private boolean isExceptionOfType(Exception exception, Class type) {
		if (exception instanceof InvocationTargetException) {
			InvocationTargetException ite = (InvocationTargetException) exception;
			if (ite.getTargetException().getClass().equals(type)) {
				return true;
			}
			else {
				return false;
			}
		}
		else {
			return false;
		}
	}

	protected void setImpl(Object bean, String propertyName, Object value)
		throws Exception {
		if (isPrimitiveSetter(bean, propertyName) && value == null) {
			if (isAllowNullPrimitiveAssignment()) {
				if (log.isWarnEnabled()) {
					log.warn("Attempted to set primitive property " + propertyName + " to null in bean " + ObjectUtils.getObjectDescription(bean));
				}
			}
			else {
				throw new ReflectionException("Cannot set the primitive property " + propertyName + " to null");
			}
		}
		else {
			ReflectionInfo reflectionInfo = getReflectionInfo(bean.getClass());
			if (reflectionInfo.getMethodHolder(propertyName).getMutator() != null) {
				reflectionInfo.set(bean, propertyName, value);
			}
			else {
				// assume the value is an array, and loop through all elements,
				// using the indexed setter to set each method.  The base
				// reflector should be checking to make sure the value passed
				// to this method is of the correct type, so it should be ok to
				// just assume we're dealing with an array
				if (value != null) {
					for (int i=0; i<Array.getLength(value); i++) {
						reflectionInfo.set(bean, propertyName, new Integer(i),
							Array.get(value, i));
					}
				}
			}
		}
	}
	
	protected Object newInstanceImpl(Class clazz, Object parameters) throws Exception {
		return clazz.newInstance();
	}
	
	protected Class getContainedTypeImpl(Class clazz) throws Exception {
		return Object.class;
	}
	
	protected Iterator getIteratorImpl(Object container) throws Exception {
		return new ObjectIterator(container);
	}
	
	protected boolean isPrimitiveSetter(Object bean, String propertyName) throws Exception {
		Method mutator = getMutator(bean, propertyName);
		// if we're dealing with an indexed mutator
		if (mutator == null) {
			// if there's no indexed mutator
			if (getIndexedMutator(bean, propertyName) == null) {
				// error... shouldn't even be calling this in the first place
				throw new IllegalArgumentException(
					propertyName
						+ " does not have any mutators, so it doesn't make sense to be checking if the setter for the property is primtive, since the property doesn't exist");
			}
			// there is an indexed mutator, which means it's an array, which means its not a primitive type
			return false;
		}
		// we're dealing with a simple mutator
		return mutator.getParameterTypes()[0].isPrimitive();
	}
	
	protected ReflectionInfo getReflectionInfo(Class clazz) {
		ReflectionInfo reflectionInfo =
			(ReflectionInfo) reflectionCache.get(clazz);
		if (reflectionInfo == null) {
			reflectionInfo = new ReflectionInfo(clazz);
			reflectionCache.put(clazz, reflectionInfo);
		}
		return reflectionInfo;
	}
	
	/**
	 * Returns <code>true</code>.
	 * @return <code>true</code>
	 */
	public boolean isStrictlyTyped() {
		return true;
	}

	/**
	 * Indicates whether primitive assignments to <code>null</code> are
	 * allowed. If they are allowed, primitive assignments to <code>null</code>
	 * will be ignored. If they are not allowed, a ReflectionException will be
	 * thrown.
	 * 
	 * @return whether primitive assignments to <code>null</code> are allowed
	 */
	public boolean isAllowNullPrimitiveAssignment() {
		return allowNullPrimitiveAssignment;
	}

	/**
	 * Sets whether primitive assignments to <code>null</code> are allowed. If
	 * they are allowed, primitive assignments to <code>null</code> will be
	 * ignored. If they are not allowed, a ReflectionException will be thrown.
	 * 
	 * @param allowNullPrimitiveAssignment
	 *            whether primitive assignments to <code>null</code> are
	 *            allowed
	 */
	public void setAllowNullPrimitiveAssignment(
		boolean allowNullPrimitiveAssignment) {
		this.allowNullPrimitiveAssignment = allowNullPrimitiveAssignment;
	}

}