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

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import net.sf.composite.util.ObjectUtils;
import net.sf.morph.MorphException;
import net.sf.morph.reflect.ReflectionException;
import net.sf.morph.transform.Immutable;
import net.sf.morph.transform.TransformationException;

/**
 * Class manipulation utilities.  Note that some code was copied from the
 * Spring framework.  Some other code was copied from Apache Ant.
 * 
 * @author Matt Sgarlata
 * @author Keith Donald
 * @author Rob Harrop
 * @author Juergen Hoeller
 * @author Matt Benson
 * @since Nov 6, 2004
 */
public abstract class ClassUtils extends net.sf.composite.util.ClassUtils {

	/**
	 * All the base array classes.  Multidimensional arrays are subclasses of
	 * these fundamental array types.
	 */
	public static final Class[] ARRAY_TYPES = {
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

	private static final Set ALL_CLASSES;
	private static final Set IMMUTABLE_TYPES;
	private static final Map PRIMITIVE_TYPE_MAP;

	static {
		//from Ant:
		Class[] primitives = {
				Boolean.TYPE, Byte.TYPE, Character.TYPE, Short.TYPE, Integer.TYPE,
				Long.TYPE, Float.TYPE, Double.TYPE };
		Class[] wrappers = {
				Boolean.class, Byte.class, Character.class, Short.class, Integer.class,
				Long.class, Float.class, Double.class };
		Map ptm = new HashMap(8);
		for (int i = 0; i < primitives.length; i++) {
			ptm.put(primitives[i], wrappers[i]);
		}
		PRIMITIVE_TYPE_MAP = Collections.unmodifiableMap(ptm);

		//we couldn't use all Numbers for immutables even if we wanted to:
		//Java 1.6 adds AtomicInteger and AtomicLong, which ARE mutable!
		Set immutable = ContainerUtils.createOrderedSet();
		immutable.addAll(Arrays.asList(primitives));
		immutable.addAll(Arrays.asList(wrappers));
		immutable.add(String.class);

		//TBD: BigInteger and BigDecimal are not mutable but are also not FINAL:
		immutable.add(BigInteger.class);
		immutable.add(BigDecimal.class);

		immutable.add(null);
		immutable.add(Class.class);
		immutable.add(Immutable.class);
		IMMUTABLE_TYPES = Collections.unmodifiableSet(immutable);

		//add primitives, null, and Objects to ALL_CLASSES:
		Set allClasses = ContainerUtils.createOrderedSet();
		allClasses.add(Object.class);
		allClasses.addAll(Arrays.asList(primitives));
		allClasses.add(null);
		ALL_CLASSES = Collections.unmodifiableSet(allClasses);
	}

	/**
	 * Returns an array version of the given class (for example, converts Long to Long[]).
	 */
	public static Class getArrayClass(Class componentType) {
		return createArray(componentType, 0).getClass();
	}
	
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
			return type instanceof Class ? (Class) type : Class.forName(type.toString());
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
	 * Indicates whether the JSP API is available.
	 * 
	 * @return <code>true</code> if the JSP API is available or <br>
	 *         <code>false</code> otherwise
	 */
	public static boolean isJspApiPresent() {
		return isClassPresent("javax.servlet.jsp.PageContext");
	}

	/**
	 * Indicates whether the Apache Commons BeanUtils API is available.
	 * 
	 * @return <code>true</code> if the BeanUtils API is available or <br>
	 *         <code>false</code> otherwise
	 */
	public static boolean isBeanUtilsPresent() {
		return isClassPresent("org.apache.commons.beanutils.DynaBean");
	}

	/**
	 * Indicates whether Velocity is available.
	 * 
	 * @return <code>true</code> if Velocity is available or <br>
	 *         <code>false</code> otherwise
	 */
	public static boolean isVelocityPresent() {
		return isClassPresent("org.apache.velocity.VelocityContext");
	}

	/**
	 * Indicates whether Commons Collections 3.x is available on the classpath.
	 * 
	 * @return <code>true</code> Commons Collections 3.x is available on the
	 *         classpath<br>
	 *         <code>false</code> otherwise
	 */
	public static boolean isCommonsCollections3Present() {
		return isClassPresent("org.apache.commons.collections.set.ListOrderedSet");
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
		for (int i = 0; i < typeArray.length; i++) {
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
	 * String).  Note that JDK 1.6 adds AtomicLong and AtomicInteger, which
	 * <em>are</em> mutable. Since Morph 1.1.2 this method will also
	 * return <code>true</code> if the specified class implements Morph's
	 * {@link Immutable} marker interface.
	 * 
	 * @param destinationType
	 *            the type to examine
	 * @return <code>true</code> if the <code>destinationType</code> is an immutable
	 *         {@link Number}, a {@link String}, or an instance of {@link Immutable};<br>
	 *         <code>false</code>, otherwise
	 */
	public static boolean isImmutable(Class destinationType) {
		return IMMUTABLE_TYPES.contains(destinationType)
				|| Immutable.class.isAssignableFrom(destinationType);
	}

	/**
	 * Determines whether the given object is an immutable object.
	 * @param o
	 * @return
	 */
	public static boolean isImmutableObject(Object o) {
		return isImmutable(getClass(o));
	}

	/**
	 * Get an array containing the basic JRE classes known to be intrinsically immutable.
	 * This does <em>not</em> locate classes implementing the {@link Immutable} interface;
	 * see {@link #isImmutable(Class)} for support in that regard.
	 * @return Class[]
	 */
	public static Class[] getImmutableTypes() {
		return (Class[]) IMMUTABLE_TYPES.toArray(new Class[IMMUTABLE_TYPES.size()]);
	}

	/**
	 * Get the wrapper type for the specified class (if any).
	 * @param c a (presumably primitive) Class.
	 * @return the wrapper class for <code>c</code>, if <code>c</code> is primitive, else null. 
	 */
	public static Class getPrimitiveWrapper(Class c) {
		return (Class) PRIMITIVE_TYPE_MAP.get(c);
	}

	/**
	 * Get all the primitive classes.
	 * @return Class[]
	 */
	public static Class[] getPrimitiveTypes() {
		return (Class[]) PRIMITIVE_TYPE_MAP.keySet().toArray(new Class[PRIMITIVE_TYPE_MAP.size()]);
	}

	/**
	 * Get all the primitive wrapper classes.
	 * @return Class[]
	 */
	public static Class[] getWrapperTypes() {
		return (Class[]) PRIMITIVE_TYPE_MAP.values().toArray(new Class[PRIMITIVE_TYPE_MAP.size()]);
	}

	/**
	 * Returns the set of classes for which any class will match.
	 * @return Class[]
	 */
	public static Class[] getAllClasses() {
		return (Class[]) ALL_CLASSES.toArray(new Class[ALL_CLASSES.size()]);
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
