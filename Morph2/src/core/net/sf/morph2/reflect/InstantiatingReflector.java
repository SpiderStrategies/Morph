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
 * A reflector that can instantiate objects. This allows the reflector to pick
 * implementations for interfaces, instantiate arrays, etc. Only classes that
 * are reflectable by this reflector should be instantiable.
 *
 * <p>This reflector is designed to facilitate construction of both simple
 * objects and objects that do not offer no-argument constructors.
 *
 * @author Matt Sgarlata
 * @since Nov 27, 2004
 */
public interface InstantiatingReflector extends Reflector {

	/**
	 * Creates a new instance of the given type.
	 * 
	 * @param clazz
	 *            the type for which we would like a new instance to be created
	 * @param parameters
	 *            additional information that may be necessary for the implementation to
	 *            create the required object (this may be null if the implementation does
	 *            not require additional information or the particular object being
	 *            requested does not require additional information)
	 * @throws ReflectionException if an error occurs
	 */
	public Object newInstance(Class clazz, Object parameters) throws ReflectionException;

}
