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
package net.sf.morph2.reflect;

/**
 * Exposes information available in 'bean-like' structures. Examples of
 * 'bean-like' structures include a java.lang.Object, a java.util.Map, or any
 * other class that logically has a set of properties that can be manipulated by
 * name.
 * 
 * @author Matt Sgarlata
 * @since Nov 6, 2004
 */
public interface BeanReflector extends Reflector, SizableReflector {

	/** implicit "class" property */
	public static final String IMPLICIT_PROPERTY_CLASS = "class";

	/** implicit "propertyNames" property */
	public static final String IMPLICIT_PROPERTY_PROPERTY_NAMES = "propertyNames";

	/** implicit "this" property */
	public static final String IMPLICIT_PROPERTY_THIS = "this";

	/**
	 * Gets the names of the properties which are currently defined for the
	 * given bean. Note that some beans (e.g. - Maps) allow the creation of new
	 * properties, which means isWriteable may return true for property names
	 * that are not included in the return value of this method.
	 * 
	 * @param bean
	 *            the bean for which we would like a list of properties
	 * @return the names of the properties which are currently defined for the
	 *         given bean. Note that some beans (e.g. - Maps) allow the creation
	 *         of new properties, which means isWriteable may return true for
	 *         property names that are not included in the return value of this
	 *         method.
	 * @throws ReflectionException
	 *             if bean is <code>null</code>
	 */
	public String[] getPropertyNames(Object bean) throws ReflectionException;

	/**
	 * Specifies the least restrictive type that may be assigned to the given
	 * property. In the case of a weakly typed bean, the correct value to return
	 * is simply <code>Object.class</code>, which indicates that any type can
	 * be assigned to the given property.
	 * 
	 * @param bean
	 *            the bean
	 * @param propertyName
	 *            the name of the property
	 * @return the least restrictive type that may be assigned to the given
	 *         property. In the case of a weakly typed bean, the correct value
	 *         to return is simply <code>Object.class</code>, which indicates
	 *         that any type can be assigned to the given property
	 * @throws ReflectionException
	 *             if <code>bean</code> or <code>propertyName</code> are
	 *             <code>null</code> or <br>
	 *             this is a strongly typed bean reflector and the given
	 *             property does not exist or <br>
	 *             if the type could not be retrieved for some reason
	 */
	public Class getType(Object bean, String propertyName)
		throws ReflectionException;

	/**
	 * Specifies whether the given property is readable. A reflector can always
	 * determine if a property is readable by attempting to read the property
	 * value, so this method can be counted on to truly indicate whether or not
	 * the given property is readable.
	 * 
	 * @param bean
	 *            the bean
	 * @param propertyName
	 *            the name of the property
	 * @return <code>true</code> if the property is readable, or <br>
	 *         <code>false</code>, otherwise
	 * @throws ReflectionException
	 *             if <code>bean</code> or <code>propertyName</code> are
	 *             <code>null</code> or <br>
	 *             if the readability of the property cannot be determined
	 */
	public boolean isReadable(Object bean, String propertyName)
		throws ReflectionException;

	/**
	 * Specifies whether the given property is writeable. If the reflector
	 * cannot determine whether the given property is writeable, it may simply
	 * return <code>true</code>. This method only guarantees that if
	 * <code>isWriteable</code> returns false, the method is not writeable.
	 * The method may or may not be writeable if this method returns
	 * <code>true</code>.
	 * 
	 * @param bean
	 *            the bean
	 * @param propertyName
	 *            the name of the property
	 * @return <code>false</code> if the property is not writeable or <br>
	 *         <code>true</code> if the property is writeable or if this
	 *         reflector cannot determine for sure whether or not the property
	 *         is writeable
	 * @throws ReflectionException
	 *             if <code>bean</code> or <code>propertyName</code> are
	 *             <code>null</code> or <br>
	 *             if the writeability of the property cannot be determined
	 */
	public boolean isWriteable(Object bean, String propertyName)
		throws ReflectionException;

	/**
	 * Retrieves the value of the given property.
	 * 
	 * @param bean
	 *            the bean
	 * @param propertyName
	 *            the name of the property
	 * @return the property's value
	 * @throws ReflectionException
	 *             if <code>bean</code> or <code>propertyName</code> is
	 *             <code>null</code> or <br>
	 *             if the value of the property cannot be determined or <br>
	 */
	public Object get(Object bean, String propertyName)
		throws ReflectionException;

	/**
	 * Sets the value of the given property.
	 * 
	 * @param bean
	 *            the bean
	 * @param propertyName
	 *            the name of the property
	 * @param propertyValue
	 *            the value to assign to the given property
	 * @throws ReflectionException
	 *             if <code>bean</code> or <code>propertyName</code> are
	 *             <code>null</code> or <br>
	 *             if the property cannot be set for some other reason (e.g.
	 *             because <code>propertyValue</code> is of the wrong type)
	 */
	public void set(Object bean, String propertyName, Object propertyValue)
		throws ReflectionException;

}
