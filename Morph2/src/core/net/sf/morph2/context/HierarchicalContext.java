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
 * A context which participates in a context hierarchy (i.e. - a context that
 * has one or more parents). Hierarchical contexts lookup property values first
 * in the current context, and then through in any parent context(s). This
 * allows properties in child context to override properties in parent contexts.
 * 
 * @author Matt Sgarlata
 * @since Nov 21, 2004
 */
public interface HierarchicalContext extends Context {

	/**
	 * Gets the names of the properties which are currently defined for this
	 * context and all parents of this context. It will often be an O(n)
	 * operation to list all property names in the context, so callers should
	 * avoid frequent calls to this method.
	 * 
	 * @throws ContextException
	 *             if the properties could not be retieved for some reason
	 * @return the property names
	 */
	public String[] getPropertyNames() throws ContextException;

	/**
	 * Retrieve the property named <code>propertyName</code> from this
	 * context, or if it's not found, from the nearest ancestor context.
	 * 
	 * @param propertyName
	 *            the name of the property to be retrieved
	 * @throws ContextException
	 *             if <code>propertyName</code> is empty or <br>
	 *             the property can't be accessed for some reason
	 */
	public Object get(String propertyName) throws ContextException;

	/**
	 * Sets <code>propertyName</code> to <code>propertyValue</code> in the
	 * current context. No change will be made to the property values in any
	 * ancestor contexts.
	 * 
	 * @param propertyName
	 *            the name of the property to set
	 * @param propertyValue
	 *            the new value for the property
	 * @throws ContextExcception
	 *             if <code>propertyName</code> is empty or <br>
	 *             the property can't be accessed for some reason
	 */
	public void set(String propertyName, Object propertyValue)
		throws ContextException;

	/**
	 * Retrieves this context's parent context.
	 * 
	 * @return this context's parent context
	 */
	public Context getParentContext();
	
	/**
	 * Sets this context's parent context.
	 * 
	 * @param context this context's parent context
	 */
	public void setParentContext(Context context);
}
