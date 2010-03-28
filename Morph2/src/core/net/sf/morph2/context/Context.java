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
package net.sf.morph2.context;

/**
 * A context is an object that can be passed between tiers of an application
 * without exposing the APIs that are particular to any given tier. For example,
 * in a web application the information submitted by a user in an HttpRequest
 * object could be exposed to a business object through the Context interface.
 * This leaves objects in the business tier independent of the servlet API and
 * thus testable outside a Servlet container.
 * 
 * @author Matt Sgarlata
 * @since Nov 19, 2004
 */
public interface Context {

	/**
	 * Gets the names of the properties which are currently defined for this
	 * context. It will often be an O(n) operation to list all property names in
	 * the context, so callers should avoid frequent calls to this method. This
	 * method should always include {@link Context#PROPERTY_NAMES_PROPERTY} as
	 * one of the properties in the returned array.
	 * 
	 * @return the property names
	 * @throws ContextException
	 *             if the properties could not be retieved for some reason
	 */
	public String[] getPropertyNames() throws ContextException;

	/**
	 * Retrieve the property named <code>propertyName</code>. The property
	 * {@link Context#PROPERTY_NAMES_PROPERTY} has special symantics. Unless a
	 * value for this property has already been explicitly specified for the
	 * context, <code>context.get(PROPERTY_NAMES_PROPERTY)</code> will return
	 * the same result as {@link Context#getPropertyNames()}.
	 * 
	 * @param propertyName
	 *            the name of the property to be retrieved, if the property has
	 *            been defined or <br>
	 *            <code>null</code>, if the property is not defined
	 * @return the value of the property
	 * @throws ContextException
	 *             if <code>propertyName</code> is empty or <br>
	 *             an error occurrs when accessing the property
	 */
	public Object get(String propertyName) throws ContextException;

	/**
	 * Sets <code>propertyName</code> to <code>propertyValue</code>.
	 * 
	 * @param propertyName
	 *            the name of the property to set
	 * @param propertyValue
	 *            the new value for the property
	 * @throws ContextException
	 *             if <code>propertyName</code> is empty or <br>
	 *             the property can't be set for some reason
	 */
	public void set(String propertyName, Object propertyValue)
		throws ContextException;
}
