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
package net.sf.morph2.reflect;

/**
 * A reflector that can tell how many elements are contained in a given object.
 * 
 * @author Matt Sgarlata
 * @since Dec 5, 2004
 */
public interface SizableReflector extends Reflector {

	/** "size" pseudo-property */
	public static final String IMPLICIT_PROPERTY_SIZE = "size";

	/**
	 * Returns the number of elements contained in a given object.  If the
	 * object is a bean, the number of properties is returned.  If the object
	 * is a container, the number of elements in the container is returned. 
	 * 
	 * @param object
	 *            the object
	 * @return the number of elements contained in the given object
	 * @throws ReflectionException
	 *             if <code>object</code> is <code>null</code> or the
	 *             number of elements in the object could not be determined
	 */
	public int getSize(Object object) throws ReflectionException;

}
