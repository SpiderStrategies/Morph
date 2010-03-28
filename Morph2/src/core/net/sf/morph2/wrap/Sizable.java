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
 * A wrapper for objects that can tell how many elements are contained within
 * themselves.
 * 
 * @author Matt Sgarlata
 * @since Jan 16, 2005
 */
public interface Sizable extends Wrapper {

	/**
	 * Returns the number of elements contained within this wrapper. If this
	 * wrapper is a bean, the number of properties is returned. If this wrapper
	 * is a container, the number of elements in the container is returned.
	 * 
	 * @return the number of elements contained in the given object
	 * @throws WrapperException
	 *             if the number of elements in the object could not be
	 *             determined
	 */
	public int getSize() throws WrapperException;

}
