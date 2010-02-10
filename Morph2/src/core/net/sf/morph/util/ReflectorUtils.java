/*
 * Copyright 2004-2005, 2007-2008 the original author or authors.
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
package net.sf.morph.util;

import net.sf.morph.reflect.CompositeReflector;
import net.sf.morph.reflect.Reflector;

/**
 * Utility functions useful for implementing and interacting with Reflectors.
 * 
 * @author Matt Sgarlata
 * @since Nov 26, 2004
 */
public abstract class ReflectorUtils {

	/**
	 * Indicates whether the given reflector can support the operations
	 * specified in <code>reflectorType</code> when reflecting instances of
	 * <code>reflectedType</code>.
	 * 
	 * @param reflector
	 *            the reflector
	 * @param reflectedType
	 *            the type of the object for which we wish to determine
	 *            reflectability
	 * @param reflectorType
	 *            the interface which defines the operations we would like to
	 *            perform on instances of <code>reflectedType</code>
	 * @return <code>true</code>, if the given reflector can support the
	 *         operations specified in <code>reflectorType</code> when
	 *         reflecting instances of <code>reflectedType</code>
	 * @throws IllegalArgumentException
	 *             if any of the arguments to this function are
	 *             <code>null</code>
	 * @throws net.sf.morph.reflect.ReflectionException
	 *             if reflectability could not be determined
	 */
	public static boolean isReflectable(Reflector reflector, Class reflectedType,
		Class reflectorType) throws IllegalArgumentException {
		if (reflector == null) {
			throw new IllegalArgumentException("The reflector must be specified");
		}
		if (reflectedType == null) {
			throw new IllegalArgumentException("The reflectedType must be specified");
		}
		if (reflectorType == null) {
			throw new IllegalArgumentException("The reflectorType must be specified");
		}
		if (reflector instanceof CompositeReflector) {
			return ((CompositeReflector) reflector).isReflectable(reflectedType, reflectorType);
		}
		return reflectorType.isInstance(reflector)
				&& ClassUtils.inheritanceContains(reflector.getReflectableClasses(),
						reflectedType);
	}

	/**
	 * Indicates whether the given reflector can support the operations
	 * specified in <code>reflectorType</code> when reflecting
	 * <code>reflectedObject</code>.
	 * 
	 * @param reflector
	 *            the reflector
	 * @param reflectedObject
	 *            the object for which we wish to determine reflectability
	 * @param reflectorType
	 *            the interface which defines the operations we would like to
	 *            perform on instances of <code>reflectedType</code>
	 * @return <code>true</code>, if the given reflector can support the
	 *         operations specified in <code>reflectorType</code> when
	 *         reflecting instances of <code>reflectedType</code>
	 * @throws IllegalArgumentException
	 *             if any of the arguments to this function are
	 *             <code>null</code>
	 * @throws net.sf.morph.reflect.ReflectionException
	 *             if reflectability could not be determined
	 * @since Morph 1.1
	 */
	public static boolean isReflectable(Reflector reflector, Object reflectedObject, Class reflectorType) {
	    return isReflectable(reflector, ClassUtils.getClass(reflectedObject), reflectorType);
    }

}