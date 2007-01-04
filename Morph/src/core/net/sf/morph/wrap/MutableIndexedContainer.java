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
 * A wrapper for an indexed container that allows modification of an element at
 * a given index. Most objects that are indexed are also "mutably indexed", if
 * you will, but some are not. Most notably, you can't change an element at a
 * specified index of a SortedSet because by virtue of adding something new to
 * the set you may be changing the indices of the elements.
 * 
 * @author Matt Sgarlata
 * @since Jan 16, 2005
 */
public interface MutableIndexedContainer extends IndexedContainer {

	/**
	 * Sets the element at the specified index. Valid indexes range between 0
	 * and one less than the container object's size, inclusive.
	 * 
	 * @param index
	 *            a number indiciating which element should be set
	 * @param propertyValue
	 *            the value to be set
	 * @return the element previously at the specified position
	 * @throws WrapperException
	 *             if <code>index</code> is not a valid index for this
	 *             container object or <br>
	 *             the object at the specified index could not be set for some
	 *             reason
	 */
	public Object set(int index, Object propertyValue) throws WrapperException;

}