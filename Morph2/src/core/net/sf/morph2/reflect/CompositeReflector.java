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
package net.sf.morph2.reflect;

/**
 * A reflector that is composed of multiple component reflectors.
 * 
 * @author Matt Sgarlata
 * @since Feb 2, 2006
 */
public interface CompositeReflector extends Reflector {

	/**
	 * Indicates whether this reflector can support the operations specified in
	 * <code>reflectorType</code> when reflecting instances of
	 * <code>reflectedType</code>.
	 * 
	 * @param reflectedType
	 *            the type of the object for which we wish to determine
	 *            reflectability
	 * @param reflectorType
	 *            the interface which defines the operations we would like to
	 *            perform on instances of <code>reflectedType</code>
	 * @return <code>true</code>, if this reflector can support the
	 *         operations specified in <code>reflectorType</code> when
	 *         reflecting instances of <code>reflectedType</code>
	 * @throws ReflectionException
	 *             if this reflector could not determine whether it could
	 *             support the operations specified in
	 *             <code>reflectorType</code>.
	 */
	public boolean isReflectable(Class reflectedType, Class reflectorType)
		throws ReflectionException;

}
