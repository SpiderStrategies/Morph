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
package net.sf.morph.util;

import net.sf.composite.util.ObjectUtils;
import net.sf.morph.MorphException;
import net.sf.morph.reflect.ReflectionException;
import net.sf.morph.transform.TransformationException;

/**
 * Class manipulation utilities.  Note that some code was copied from the
 * Spring framework.
 * 
 * @author Matt Sgarlata
 * @author Keith Donald
 * @author Rob Harrop
 * @author Juergen Hoeller
 * @since Nov 6, 2004
 */
public abstract class ClassUtils extends net.sf.composite.util.ClassUtils {
	
	/**
	 * Suffix for array class name.  "Borrowed" from the Spring framework.  
	 */
	public static final String ARRAY_SUFFIX = "[]";

	/**
	 * All primitive classes.  "Borrowed" from the Spring framework.
	 */
	public static Class[] PRIMITIVE_TYPES = { boolean.class, byte.class,
		char.class, short.class, int.class, long.class, float.class,
		double.class };
	
	/**
	 * All the base array classes.  Multidimensional arrays are subclasses of
	 * these fundamental array types.
	 */
	public static Class[] ARRAY_TYPES = {
		Object[].class,
		long[].class,
		int[].class,
		short[].class,
		char[].class,
		byte[].class,
		double[].class,
		float[].class,
		boolean[].class
	};
	
	/**
	 * Returns an array version of the given class (for example, converts Long
	 * to Long[]).
	 */
	public static Class getArrayClass(Class componentType) {
		return createArray(componentType, 0).getClass();
	}
	
//	/**
//	 * Replacement for <code>Class.newInstance()</code> that throws a
//	 * {@link ReflectionException} instead of a {@link ClassNotFoundException}.
//	 * 
//	 * @param clazz
//	 *            the class for which a new instance is to be created
//	 * @return an instance of the specified class
//	 * @throws ReflectionException
//	 *             if a new instance of the requested class could not be created
//	 */
//	public static Object newInstance(Class clazz) throws ReflectionException {
//		try {
//			clazz
//		}
//		()
//		return reflector.newInstance(clazz);
//	}
	
	/**
	 * Returns a new instance of the class denoted by <code>type</code>. The
	 * type may be expressed as a Class object, a String or a StringBuffer.
	 * 
	 * @param type
	 *            an object that specifies the class of the new instance
	 * @return an instance of the specified class
	 * @throws ReflectionException
	 *             if a new instance of the requested class could not be created
	 * @throws TransformationException
	 *             if the class denoted by the given type could not be retrieved
	 * @throws IllegalArgumentException
	 *             if the type parameter is null or not a Class, String or
	 *             StringBuffer
	 */
	public static Object newInstance(Object type) {
		try {
			return convertToClass(type).newInstance();
		}
		catch (MorphException e) {
			throw e;
		}
		catch (IllegalArgumentException e) {
			throw e;
		}
		catch (Exception e) {
			throw new ReflectionException("Could not create a new instance of "
				+ ObjectUtils.getObjectDescription(type), e);
		}
	}

	/**
	 * Converts the given object to a Class object. The conversion will only
	 * succeed if the object is a Class, String or StringBuffer.
	 * 
	 * @param type
	 *            an object that specifies the class
	 * @return the specified class
	 * @throws TransformationException
	 *             if the class could not be retrieved for some reason
	 * @throws IllegalArgumentException
	 *             if the type parameter is null or not a Class, String or
	 *             StringBuffer
	 */
	public static Class convertToClass(Object type) {
		if (type == null) {
			throw new IllegalArgumentException(
				"You must specify the class to instantiate");
		}
		if (!(type instanceof String || type instanceof StringBuffer || type instanceof Class)) {
			throw new IllegalArgumentException(
				"The type to be instantiated must be specified as a Class, String or StringBuffer object");
		}

		try {
			Class clazz;
			if (type instanceof Class) {
				clazz = (Class) type;
			}
			else {
				clazz = Class.forName(type.toString());
			}
			return clazz;
		}
		catch (Exception e) {
			throw new TransformationException(
				"Could not convert " + ObjectUtils.getObjectDescription(type)
					+ " to a Class object: " + e.getMessage(), e);
		}
	}
	
	/**
	 * Indicates whether the Servlet API is available.
	 * 
	 * @return <code>true</code> if the servlet API is available or <br>
	 *         <code>false</code> otherwise
	 */
	public static boolean isServletApiPresent() {
		return isClassPresent("javax.servlet.http.HttpServletRequest");
	}
	
	/**
	 * Indicates whether the BeanUtils API is available.
	 * 
	 * @return <code>true</code> if the BeanUtils API is available or <br>
	 *         <code>false</code> otherwise
	 */
	public static boolean isBeanUtilsPresent() {
		return isClassPresent("org.apache.commons.beanutils.DynaBean");
	}
	
	public static boolean isJdk14OrHigherPresent() {
		return isClassPresent("java.lang.CharSequence");
	}
	
	/**
	 * Indicates whether Velocity is available.
	 * 
	 * @return <code>true</code> if the Velocity is available or <br>
	 *         <code>false</code> otherwise
	 */
	public static boolean isVelocityPresent() {
		return isClassPresent("org.apache.velocity.VelocityContext");
	}
	
	/**
	 * Determines if the given <code>object</code> is a primitive (int, long,
	 * etc).
	 * 
	 * @param object
	 *            the object to test
	 * @return <code>true</code> if the object is a primitive or <br>
	 *         <code>false</code>, otherwise
	 * @throws IllegalArgumentException
	 *             if <code>object</code> is <code>null</code>.
	 */
	public static boolean isPrimitive(Object object) {
		if (object == null) {
			throw new IllegalArgumentException("Object cannot be null");
		}
		return ContainerUtils.contains(PRIMITIVE_TYPES, object.getClass());
	}

	/**
	 * Determines if <code>type</code> is equal to or a subtype of any of the
	 * types in <code>typeArray</code>.
	 * 
	 * @param type
	 *            the type to test
	 * @param typeArray
	 *            the array of types
	 * @return <code>true</code>, if <code>type</code> if <code>type</code>
	 *         is equal to or a subtype of any of the types in
	 *         <code>typeArray</code> or <br>
	 *         <code>false</code>, otherwise
	 * @throws IllegalArgumentException
	 *             if any of the types in the provided <code>typeArray</code>
	 *             are <code>null</code>
	 */
	public static boolean inheritanceContains(Class[] typeArray, Class type) {
		if (typeArray == null) {
			return false;
		}
		else {
			for (int i=0; i<typeArray.length; i++) {
				if (type == null) {
					if (typeArray[i] == null) {
						return true;
					}
				}
				else if (typeArray[i] != null &&
					typeArray[i].isAssignableFrom(type)) {
					return true;
				}
			}
		}
		return false;
	}
	
	/**
	 * Returns the class of the given object.
	 * 
	 * @param object
	 *            the object
	 * @return <code>null</code>, if <code>type</code> is <code>null</code>
	 *         or <br>
	 *         the class of the given object, otherwise
	 */
	public static Class getClass(Object object) {
		return object == null ? null : object.getClass();
	}
	
	/**
	 * Determines whether the given <code>destinationType</code> is one of the
	 * primitive immutable types provided by the JDK (i.e. a Number or a
	 * String).
	 * 
	 * @param destinationType
	 *            the type to examine
	 * @return <code>true</code> if the <code>destinationType</code> is a
	 *         number or a String or <br>
	 *         <code>false</code>, otherwise
	 */
	public static boolean isImmutable(Class destinationType) {
		return
			destinationType.isPrimitive() ||
			Number.class.isAssignableFrom(destinationType) ||
			String.class.isAssignableFrom(destinationType);
	}

//	public static Class inheritanceIntersection(Class[] types) {
//	Assert.contentsNotNull(types);
//	
//	if (ObjectUtils.isEmpty(types) || types.length == 1) {
//		return types;
//	}
//	
//	// types.length >= 2
//	Class type = types[0];
//	for (int i=1; i<types.length; i++) {
//		Class nextType = 
//	}
//	
//	else if (types.length == 2) {
//		Class type1 = types[0];
//		Class type2 = types[1];
//		if (type1.isAssignableFrom(type2)) {
//			return type2;
//		}
//	}
//	else { //types.length >= 3
//		Arrays.
//		return inheritanceIntersection()
//	}
//	// if we get to here, types.length >= 2
//	
//}

}
