/*
 * Copyright 2005, 2008 the original author or authors.
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
package net.sf.morph2.wrap.support;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import net.sf.composite.util.CompositeUtils;
import net.sf.composite.util.ObjectUtils;
import net.sf.morph2.Defaults;
import net.sf.morph2.reflect.BeanReflector;
import net.sf.morph2.reflect.ContainerReflector;
import net.sf.morph2.reflect.GrowableContainerReflector;
import net.sf.morph2.reflect.IndexedContainerReflector;
import net.sf.morph2.reflect.Reflector;
import net.sf.morph2.reflect.SizableReflector;
import net.sf.morph2.util.BidirectionalMap;
import net.sf.morph2.wrap.Bean;
import net.sf.morph2.wrap.Container;
import net.sf.morph2.wrap.GrowableContainer;
import net.sf.morph2.wrap.IndexedContainer;
import net.sf.morph2.wrap.MutableIndexedContainer;
import net.sf.morph2.wrap.Sizable;

/**
 * Default WrapperInvocationHandler implementation.
 * @author Matt Sgarlata
 * @since Jan 16, 2005
 */
public class DefaultWrapperInvocationHandler implements WrapperInvocationHandler {

	private static final Map DEFAULT_REFLECTOR_WRAPPER_MAP;

	static {
		DEFAULT_REFLECTOR_WRAPPER_MAP = new BidirectionalMap(6);
		DEFAULT_REFLECTOR_WRAPPER_MAP.put(BeanReflector.class, Bean.class);
		DEFAULT_REFLECTOR_WRAPPER_MAP.put(ContainerReflector.class, Container.class);
		DEFAULT_REFLECTOR_WRAPPER_MAP.put(GrowableContainerReflector.class, GrowableContainer.class);
		DEFAULT_REFLECTOR_WRAPPER_MAP.put(IndexedContainerReflector.class, IndexedContainer.class);
		DEFAULT_REFLECTOR_WRAPPER_MAP.put(MutableIndexedContainer.class, MutableIndexedContainer.class);
		DEFAULT_REFLECTOR_WRAPPER_MAP.put(SizableReflector.class, Sizable.class);
	}

	private Map reflectorWrapperMap;
	private Reflector reflector;
	private Object wrapped;

	/**
	 * Create a new DefaultWrapperInvocationHandler.
	 * @param wrapped object
	 */
	public DefaultWrapperInvocationHandler(Object wrapped) {
		super();
		this.wrapped = wrapped;
	}

	/**
	 * Create a new DefaultWrapperInvocationHandler.
	 * @param wrapped object
	 * @param reflector to use
	 */
	public DefaultWrapperInvocationHandler(Object wrapped, Reflector reflector) {
		this(wrapped);
		this.reflector = reflector;
	}

	/**
	 * {@inheritDoc}
	 */
	public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {

		try {

			Class declaringClass = method.getDeclaringClass();
			if (declaringClass.equals(getWrapped().getClass())) {
				return method.invoke(getWrapped(), args);
			}
			Class wrapperClass = method.getDeclaringClass();
			Class reflectorClass = (Class) getBiDirectionalReflectorWrapperMap().getKey(wrapperClass);
			if (reflectorClass == null) {
				throw new IllegalArgumentException(
						"Cannot invoke method "
								+ method
								+ " because it is not declared in one of the recognized wrapper classes, which are: "
								+ ObjectUtils
										.getObjectDescription(getReflectorWrapperMap()
												.values()));
			}

			Reflector reflector = (Reflector) CompositeUtils.specialize(getReflector(), reflectorClass);

			int wrapperNumArgs = method.getParameterTypes().length;
			int reflectorNumArgs = wrapperNumArgs + 1;

			Class[] reflectorParameterTypes = new Class[reflectorNumArgs];
			reflectorParameterTypes[0] = Object.class;
			if (method.getParameterTypes() != null) {
				System.arraycopy(method.getParameterTypes(), 0,
					reflectorParameterTypes, 1, wrapperNumArgs);
			}

			Object[] reflectorArgs = new Object[reflectorNumArgs];
			reflectorArgs[0] = getWrapped();
			if (args != null) {
				System.arraycopy(args, 0, reflectorArgs, 1, wrapperNumArgs);
			}

			Method reflectorMethod = reflector.getClass().getMethod(method.getName(), reflectorParameterTypes);

			return reflectorMethod.invoke(reflector, reflectorArgs);
		}
		// if an exception is thrown by the invoke method, just rethrow it
		// without it wrapped in an InvocationTargetException
		catch (InvocationTargetException e) {
			throw e.getTargetException();
		}

	}

	/**
	 * {@inheritDoc}
	 */
	public Class[] getInterfaces(Object object) {
		Class[] baseInterfaces = object.getClass().getInterfaces();
		List interfaces;
		if (baseInterfaces == null) {
			interfaces = new ArrayList();
		}
		else {
			// wrap the list in an ArrayList so that new elements can be added
			interfaces = new ArrayList(Arrays.asList(baseInterfaces));
		}
		Map reflectorWrapperMap = getReflectorWrapperMap();
		Iterator reflectorClasses =  reflectorWrapperMap.keySet().iterator();
		while (reflectorClasses.hasNext()) {
			Class reflectorClass = (Class) reflectorClasses.next();
			Class wrapperClass = (Class) reflectorWrapperMap.get(reflectorClass);
			if (CompositeUtils.isSpecializable(getReflector(), reflectorClass)) {
				interfaces.add(wrapperClass);
			}
		}
		return (Class[]) interfaces.toArray(new Class[interfaces.size()]);
	}

	/**
	 * Get the bidirectional reflector map.
	 * @return BidirectionalMap
	 */
	protected BidirectionalMap getBiDirectionalReflectorWrapperMap() {
		return (BidirectionalMap) getReflectorWrapperMap();
	}

	/**
	 * Get the reflectorWrapperMap.
	 * @return Map
	 */
	public Map getReflectorWrapperMap() {
		if (reflectorWrapperMap == null) {
			setReflectorWrapperMap(DEFAULT_REFLECTOR_WRAPPER_MAP);
		}
		return reflectorWrapperMap;
	}

	/**
	 * Set the reflectorWrapperMap.
	 * @param reflectorWrapperMap to set
	 */
	public void setReflectorWrapperMap(Map reflectorWrapperMap) {
		this.reflectorWrapperMap = BidirectionalMap.getInstance(reflectorWrapperMap);
	}

	/**
	 * Get the reflector.
	 * @return Reflector
	 */
	public synchronized Reflector getReflector() {
		if (reflector == null) {
			setReflector(Defaults.createReflector());
		}
		return reflector;
	}

	/**
	 * Set the reflector.
	 * @param reflector to set
	 */
	public synchronized void setReflector(Reflector reflector) {
		this.reflector = reflector;
	}

	/**
	 * Get the wrapped object.
	 * @return Object
	 */
	public Object getWrapped() {
		return wrapped;
	}

	/**
	 * Set the wrapped object.
	 * @param wrapped object to set
	 */
	public void setWrapped(Object wrapped) {
		this.wrapped = wrapped;
	}
}
