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
package net.sf.morph2.wrap;


/**
 * A wrapper around 'bean-like' structures. Examples of 'bean-like' structures
 * include a java.lang.Object, a java.util.Map, or any other class that
 * logically has a set of properties that can be manipulated by name.
 * 
 * @author Matt Sgarlata
 * @since Jan 16, 2005
 */
public interface Bean extends Wrapper, Sizable {

	/**
	 * Gets the names of the properties which are currently defined for this
	 * bean. Note that some beans (e.g. - Maps) allow the creation of new
	 * properties, which means isWriteable may return true for property names
	 * that are not included in the return value of this method.
	 * 
	 * @return the names of the properties which are currently defined for this
	 *         bean. Note that some beans (e.g. - Maps) allow the creation of
	 *         new properties, which means isWriteable may return true for
	 *         property names that are not included in the return value of this
	 *         method.
	 */
	public String[] getPropertyNames();

	/**
	 * Specifies the least restrictive type that may be assigned to the given
	 * property. In the case of a weakly typed bean, the correct value to return
	 * is simply <code>Object.class</code>, which indicates that any type can
	 * be assigned to the given property.
	 * 
	 * @param propertyName
	 *            the name of the property
	 * @return the least restrictive type that may be assigned to the given
	 *         property. In the case of a weakly typed bean, the correct value
	 *         to return is simply <code>Object.class</code>, which indicates
	 *         that any type can be assigned to the given property
	 * @throws WrapperException
	 *             if <code>getPropertyNames</code> returns <code>null</code>
	 *             or if the type could not be retrieved for some reason
	 */
	public Class getType(String propertyName) throws WrapperException;

	/**
	 * Specifies whether the given property is readable. A reflector can always
	 * determine if a property is readable by attempting to read the property
	 * value, so this method can be counted on to truly indicate whether or not
	 * the given property is readable.
	 * 
	 * @param propertyName
	 *            the name of the property
	 * @return <code>true</code> if the property is readable, or <br>
	 *         <code>false</code>, otherwise
	 * @throws WrapperException
	 *             if <code>getPropertyNames</code> returns <code>null</code>
	 *             or if the readability of the property cannot be determined
	 */
	public boolean isReadable(String propertyName) throws WrapperException;

	/**
	 * Specifies whether the given property is writeable. If it is not possible
	 * to determine whether the given property is writeable, this method may
	 * simply return <code>true</code>. This method only guarantees that if
	 * <code>isWriteable</code> returns false, the method is not writeable.
	 * The method may or may not be writeable if this method returns
	 * <code>true</code>.
	 * 
	 * @param propertyName
	 *            the name of the property
	 * @return <code>false</code> if the property is not writeable or <br>
	 *         <code>true</code> if the property is writeable or if this
	 *         reflector cannot determine for sure whether or not the property
	 *         is writeable
	 * @throws WrapperException
	 *             if <code>getPropertyNames</code> returns <code>null</code>
	 *             or if the writeability of the property cannot be determined
	 */
	public boolean isWriteable(String propertyName)
		throws WrapperException;

	/**
	 * Retrieves the value of the given property.
	 * 
	 * @param propertyName
	 *            the name of the property
	 * @return the property's value
	 * @throws WrapperException
	 *             if <code>getPropertyNames</code> returns <code>null</code>
	 *             or if the value of the property cannot be determined
	 */
	public Object get(String propertyName) throws WrapperException;

	/**
	 * Sets the value of the given property.
	 * 
	 * @param propertyName
	 *            the name of the property
	 * @param propertyValue
	 *            the value to assign to the given property
	 * @throws WrapperException
	 *             if <code>getPropertyNames</code> returns <code>null</code>
	 *             or if the property cannot be set for some other reason (e.g.
	 *             because <code>propertyValue</code> is of the wrong type)
	 */
	public void set(String propertyName, Object propertyValue)
		throws WrapperException;

}
