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
package net.sf.morph.reflect;

import net.sf.composite.Component;
import net.sf.morph.wrap.Wrapper;

/**
 * A reflector exposes the information contained within another object. There
 * are two main types of reflectors: {@link net.sf.morph.reflect.BeanReflector}s
 * and {@link net.sf.morph.reflect.ContainerReflector}s.
 * 
 * @author Matt Sgarlata
 * @since Nov 14, 2004
 */
public interface Reflector extends Component {

	/**
	 * Defines which classes are reflectable by this reflector. Each returned
	 * class and all its subclasses are reflectable by this reflector.
	 * 
	 * @return the classes that are reflectable by this reflector.
	 */
	public Class[] getReflectableClasses();
	
	/**
	 * Optional operation: returns a wrapper for the given object.
	 * 
	 * @param object
	 *            the object for which a wrapper is desired
	 * @return the wrapper
	 * @throws UnsupportedOperationException
	 *             if the reflector does not support retrieving wrappers
	 */
	public Wrapper getWrapper(Object object);
}