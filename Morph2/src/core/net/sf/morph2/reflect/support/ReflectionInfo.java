/*
 * Copyright 2005, 2007, 2010 the original author or authors.
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
package net.sf.morph2.reflect.support;

import java.beans.Introspector;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;

import net.sf.morph2.util.NumberUtils;

/**
 * Provides easy access to the property information for a class.
 *
 * @author Matt Sgarlata
 * @author Alexander Volanis
 * @since Feb 3, 2005
 */
public class ReflectionInfo {
	private Map propertyAccessMethods = new HashMap();
	private String[] propertyNames;

	/**
	 * Create a new ReflectionInfo instance.
	 * @param clazz
	 */
	public ReflectionInfo(Class clazz) {
		String propertyName;
		Method[] methods = clazz.getMethods();
		for (int i = 0; i < methods.length; i++) {
			String methodName = methods[i].getName();
            Class[] parameterTypes = methods[i].getParameterTypes();
			if (!Modifier.isStatic(methods[i].getModifiers())) {
				// if setter method detected
				if (methodName.length() > 3 &&
					methodName.indexOf("set") == 0 &&
					Character.isUpperCase(methodName.charAt(3))) {
					propertyName = Introspector.decapitalize(methodName.substring(3));
                    // check for number of arguments
                    if (parameterTypes.length == 1) {
                        registerMutator(propertyName, methods[i]);
                    }
                    // check for indexed mutator
                    else if (parameterTypes.length == 2 &&
						NumberUtils.isNumber(parameterTypes[0])) {
                        registerIndexedMutator(propertyName, methods[i]);
                    }
				}
				// if normal getter method detected
				else if (methodName.length() > 3 &&
					methodName.indexOf("get") == 0 &&
					Character.isUpperCase(methodName.charAt(3))) {
					propertyName = Introspector.decapitalize(methodName.substring(3));
                    // check for standard accesor
                    if (parameterTypes.length == 0) {
                        registerAccessor(propertyName, methods[i]);
                    }
                    // special case for indexed accessor
                    else if (parameterTypes.length == 1 &&
                    	NumberUtils.isNumber(parameterTypes[0])) {
                        registerIndexedAccessor(propertyName, methods[i]);
                    }
				}
				// if boolean getter method detected - PRIMITIVE ONLY like java.beans!
				else if (methodName.length() > 2 &&
					methods[i].getReturnType().equals(Boolean.TYPE) &&
                    parameterTypes.length == 0 &&
					methodName.indexOf("is") == 0 &&
					Character.isUpperCase(methodName.charAt(2))) {
					propertyName = Introspector.decapitalize(methodName.substring(2));
					registerAccessor(propertyName, methods[i]);
				}
			}
		}
		propertyNames = (String[]) propertyAccessMethods.keySet().toArray(
			new String[propertyAccessMethods.size()]);
	}

	/**
	 * Get the non-null MethodHolder for the specified property.
	 * @param propertyName
	 * @return MethodHolder
	 */
	protected MethodHolder createOrRetrieveMethodHolder(String propertyName) {
		MethodHolder holder = (MethodHolder) propertyAccessMethods.get(propertyName);
		if (holder == null) {
			holder = new MethodHolder();
			propertyAccessMethods.put(propertyName, holder);
		}
		return holder;
	}

	/**
	 * Register an accessor.
	 * @param propertyName
	 * @param method
	 */
    protected void registerAccessor(String propertyName, Method method) {
        createOrRetrieveMethodHolder(propertyName).setAccessor(method);
    }

    /**
     * Register a mutator.
     * @param propertyName
     * @param method
     */
    protected void registerMutator(String propertyName, Method method) {
        createOrRetrieveMethodHolder(propertyName).setMutator(method);
    }

    /**
     * Register an indexed accessor.
     * @param propertyName
     * @param method
     */
    protected void registerIndexedAccessor(String propertyName, Method method) {
        createOrRetrieveMethodHolder(propertyName).setIndexedAccessor(method);
    }

    /**
     * Register an indexed mutator.
     * @param propertyName
     * @param method
     */
    protected void registerIndexedMutator(String propertyName, Method method) {
        createOrRetrieveMethodHolder(propertyName).setIndexedMutator(method);
    }

    /**
     * Get the MethodHolder for a given property, if it exists.
     * @param propertyName
     * @return MethodHolder or null
     */
	public MethodHolder getMethodHolder(String propertyName) {
		return (MethodHolder) propertyAccessMethods.get(propertyName);
	}

	/**
	 * Set a property.
	 * @param bean
	 * @param propertyName
	 * @param value
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 */
	public void set(Object bean, String propertyName, Object value)
		throws IllegalArgumentException, IllegalAccessException,
		InvocationTargetException {
		getMethodHolder(propertyName).invokeMutator(bean, value);
	}

	/**
	 * Set an indexed property.
	 * @param bean
	 * @param propertyName
	 * @param index
	 * @param value
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 */
    public void set(Object bean, String propertyName, Object index,
		Object value) throws IllegalArgumentException,
		IllegalAccessException, InvocationTargetException {
		getMethodHolder(propertyName).invokeIndexedMutator(bean, index,
			value);
	}

    /**
     * Learn whether a property is writable.
     * @param propertyName
     * @return boolean
     */
	public boolean isWriteable(String propertyName) {
		return
			getMethodHolder(propertyName) != null &&
			(
				getMethodHolder(propertyName).getMutator() != null ||
				getMethodHolder(propertyName).getIndexedMutator() != null
			);
	}

	/**
	 * Learn whether a property is readable.
	 * @param propertyName
	 * @return boolean
	 */
	public boolean isReadable(String propertyName) {
		return
			getMethodHolder(propertyName) != null &&
			(
				getMethodHolder(propertyName).getAccessor() != null ||
				getMethodHolder(propertyName).getIndexedAccessor() != null
			);
	}

	/**
	 * Get the property names known by this {@link ReflectionInfo}.
	 * @return String[]
	 */
	public String[] getPropertyNames() {
		return propertyNames;
	}

	/**
	 * Read a property.
	 * @param bean
	 * @param propertyName
	 * @return value
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 */
    public Object get(Object bean, String propertyName) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException {
        return getMethodHolder(propertyName).invokeAccessor(bean);
    }

    /**
     * Read an indexed property.
     * @param bean
     * @param propertyName
     * @param index
     * @return value
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     */
    public Object get(Object bean, String propertyName, Object index) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException {
        return getMethodHolder(propertyName).invokeIndexedAccessor(bean, index);
    }

}
