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
package net.sf.morph.wrap;


/**
 * A wrapper for 'container-like' structures that have a logical ordering which
 * can be used to retrieve elements at a specific index within the structure.
 * Lists, SortedSets, and arrays are good examples of indexed structures. Note
 * that some indexed structures will have O(1) methods for retrieving objects at
 * a given index (ArrayList, TreeSet, array) and others will have O(n) methods
 * for retrieving objects at a given index (LinkedList).
 * 
 * @author Matt Sgarlata
 * @since Jan 16, 2005
 */
public interface IndexedContainer extends Container, Sizable {

	/**
	 * Gets the element at the specified index. Valid indexes range between 0
	 * and one less than the container object's size, inclusive.
	 * 
	 * @param index
	 *            a number indiciating which element should be retrieved
	 * @return the object at the specified index
	 * @throws WrapperException
	 *             if <code>index</code> is not a valid index for this
	 *             container object or <br>
	 *             the object at the specified index could not be retrieved for
	 *             some reason
	 */
	public Object get(Object container, int index) throws WrapperException;

}