/*
 * Copyright 2004-2005, 2010 the original author or authors.
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
package net.sf.morph2.wrap;

/**
 * A wrapper for 'container-like' structures that do not have a fixed size.
 * 
 * @author Matt Sgarlata
 * @since Jan 16, 2005
 */
public interface GrowableContainer extends Container {

	/**
	 * Adds a new <code>value</code> to the end of this container.
	 * 
	 * @param value
	 *            the value to be added
	 * @return <code>true</code> if the container changed as a result of the
	 *         call or <br>
	 *         <code>false</code>, otherwise
	 * @throws WrapperException
	 *             if an error occurrs
	 */
	public boolean add(Object value) throws WrapperException;

}
