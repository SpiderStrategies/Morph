/*
 * Copyright 2004-2005, 2007 the original author or authors.
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

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Map;
import java.util.Set;

import net.sf.composite.util.ObjectUtils;
import net.sf.morph.reflect.InstantiatingReflector;
import net.sf.morph.reflect.ReflectionException;
import net.sf.morph.util.ClassUtils;
import net.sf.morph.util.TransformerUtils;
import net.sf.morph.util.TypeMap;

/**
 * A basic instantiating reflector that allows an arbitrary number of requested
 * class types to be mapped to instantiated types.  The default instance of this
 * class can instantiate common interfaces such as {@link java.util.Calendar}
 * and {@link java.lang.CharSequence}.
 *
 * @author Matt Sgarlata
 * @since Feb 27, 2005
 */
public class SimpleInstantiatingReflector extends BaseReflector implements InstantiatingReflector {

	private Map requestedToInstantiatedTypeMap;

	public SimpleInstantiatingReflector() {
		super();
	}

	public SimpleInstantiatingReflector(Class instantiatedType) {
		this(instantiatedType, instantiatedType);
	}

	public SimpleInstantiatingReflector(Class requestedType, Class instantiatedType) {
		super();
		setRequestedType(requestedType);
		setInstantiatedType(instantiatedType);
	}

	public SimpleInstantiatingReflector(Map requestedToInstantiatedClassMap) {
		super();
		this.requestedToInstantiatedTypeMap = requestedToInstantiatedClassMap;
	}

	protected Class[] getReflectableClassesImpl() throws Exception {
		Set reflectableClasses = getRequestedToInstantiatedTypeMap().keySet();
		return (Class[]) reflectableClasses.toArray(new Class[reflectableClasses.size()]);
	}

	protected Object newInstanceImpl(Class requestedType, Object parameters) throws Exception {
		Class typeToInstantiate = TransformerUtils.getMappedDestinationType(
				getRequestedToInstantiatedTypeMap(), requestedType);
		if (typeToInstantiate == null) {
			throw new Exception("Unable to instantiate " + requestedType);
		}
		return super.newInstanceImpl(typeToInstantiate, parameters);
	}

	/**
	 * Returns the instantiated type if only a single mapping of requested type
	 * to instantiated type has been specified.
	 *
	 * @return the instantiated type, if one has been specified or <br>
	 *         <code>null</code> if no instantiated type has been specified
	 * @throws IllegalStateException
	 *             if there is more than one mapping of requested type to
	 *             instantiated type
	 */
	public final Class getInstantiatedType() {
		if (ObjectUtils.isEmpty(getRequestedToInstantiatedTypeMap())) {
			return null;
		}
		if (getRequestedToInstantiatedTypeMap().size() == 1) {
			return (Class) getRequestedToInstantiatedTypeMap().values().iterator().next();
		}
		throw new IllegalStateException("This reflector has multiple "
			+ "mappings of requested types to instantiated types, so a "
			+ "single instantiated type cannot be returned.  The mappings "
			+ "are " + getRequestedToInstantiatedTypeMap());
	}

	/**
	 * Sets the instantiated type if only a single mapping of requested type to
	 * instantiated type is needed.
	 *
	 * @param instantiatedType
	 *            the instantiated type
	 */
	public final void setInstantiatedType(Class instantiatedType) {
		if (getRequestedToInstantiatedTypeMap() == null) {
			setRequestedToInstantiatedTypeMap(new TypeMap());
		}

		Class requestedType;
		try {
			requestedType = getRequestedType();
		}
		catch (IllegalStateException e) {
			requestedType = null;
		}

		getRequestedToInstantiatedTypeMap().clear();
		getRequestedToInstantiatedTypeMap().put(requestedType, instantiatedType);
	}

	/**
	 * Returns the requested type if only a single mapping of requested type to
	 * instantiated type has been specified.
	 *
	 * @return the requested type, if one has been specified or <br>
	 *         <code>null</code> if no requested type has been specified
	 * @throws IllegalStateException
	 *             if there is more than one mapping of requested type to
	 *             instantiated type
	 */
	public final Class getRequestedType() {
		if (ObjectUtils.isEmpty(getRequestedToInstantiatedTypeMap())) {
			return null;
		}
		if (getRequestedToInstantiatedTypeMap().size() == 1) {
			return (Class) getRequestedToInstantiatedTypeMap().keySet().iterator().next();
		}
		throw new IllegalStateException("This reflector has multiple "
			+ "mappings of requested types to instantiated types, so a "
			+ "single requested type cannot be returned.  The mappings "
			+ "are " + getRequestedToInstantiatedTypeMap());
	}

	/**
	 * Sets the requested type if only a single mapping of requested type to
	 * instantiated type is needed.
	 *
	 * @param requestedType
	 *            the requested type
	 */
	public final void setRequestedType(Class requestedType) {
		if (getRequestedToInstantiatedTypeMap() == null) {
			setRequestedToInstantiatedTypeMap(new TypeMap());
		}

		Class instantiatedType;
		try {
			instantiatedType = getInstantiatedType();
		}
		catch (IllegalStateException e) {
			instantiatedType = null;
		}

		getRequestedToInstantiatedTypeMap().clear();
		getRequestedToInstantiatedTypeMap().put(requestedType, instantiatedType);
	}

	/**
	 * Returns the mapping of requested types to instantiated types.
	 *
	 * @return the mapping of requested types to instantiated types
	 */
	public synchronized Map getRequestedToInstantiatedTypeMap() {
		if (requestedToInstantiatedTypeMap == null) {
			Map map = new TypeMap();
			map.put(Calendar.class, GregorianCalendar.class);

			if (ClassUtils.isJdk14OrHigherPresent()) {
				try {
					map.put(Class.forName("java.lang.CharSequence"), StringBuffer.class);
				}
				// this shouldn't ever happen because we explicitly check that the
				// CharSequence class exists before requesting a reference to it
				catch (ClassNotFoundException e) {
					throw new ReflectionException(e);
				}
			}
			setRequestedToInstantiatedTypeMap(map);
		}
		return requestedToInstantiatedTypeMap;
	}

	/**
	 * Sets the mapping of requested types to instantiated types.
	 *
	 * @param interfaceToImplementationMap
	 *            the mapping of requested types to instantiated types
	 */
	public synchronized void setRequestedToInstantiatedTypeMap(
		Map interfaceToImplementationMap) {
		this.requestedToInstantiatedTypeMap = new TypeMap(interfaceToImplementationMap);
	}
}
