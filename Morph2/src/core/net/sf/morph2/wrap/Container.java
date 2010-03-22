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
package net.sf.morph2.wrap;

import java.util.Iterator;

/**
 * A wrapper around 'container-like' structures. A 'container-like' structure
 * contains a certain number of objects, each of which has a particular type.
 * Examples of 'container-like' objects include arrays and
 * java.util.Collections.
 * 
 * @author Matt Sgarlata
 * @since Jan 16, 2005
 */
public interface Container extends Wrapper {
	
	/**
	 * Returns the type of the elements that are contained in this container.
	 * For example, if this container wraps an array of
	 * <code>int</code>s, <code>Integer.TYPE</code> should be returned.
	 * 
	 * @param clazz
	 *            the container's type
	 * @return the type of the elements that are container by the given object
	 * @throws WrapperException
	 *             if <code>container</code> is null or <br>
	 *             the type of the elements that are container could not be
	 *             determined
	 */
	public Class getContainedType() throws WrapperException;

	/**
	 * Exposes an iterator over the contents of the container. Note that in many
	 * cases, an Iterator may only be used once and is then considered invalid.
	 * If you need to loop through the contents of the iterator multiple times,
	 * you will have to copy the contents of the iterator to some other
	 * structure, such as a java.util.List.
	 * 
	 * @return an Iterator over the elements in the container
	 * @throws WrapperException
	 *             if <code>container</code> is <code>null</code> or <br>
	 *             the Iterator could not be created for some reason
	 */
	public Iterator getIterator() throws WrapperException;
	
}
