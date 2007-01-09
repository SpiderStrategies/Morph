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
package net.sf.morph.reflect.reflectors;

import java.util.Iterator;

import net.sf.morph.reflect.ContainerReflector;

/**
 * <p>Convenient base class for ContainerReflectors. Validates arguments and takes
 * care of logging and exception handling.  All types of ContainerReflectors
 * can use this class as a Base, because all methods defined on subinterfaces
 * of ContainerReflector are supported by this base class.  Note, however, that
 * this class <em>only</em> states that it implements ContainerReflector so that
 * subclasses can choose which interfaces they wish to expose.</p>
 * 
 * @author Matt Sgarlata
 * @since Nov 20, 2004
 */
public abstract class BaseContainerReflector extends BaseReflector implements
	ContainerReflector {

	protected abstract Class getContainedTypeImpl(Class clazz) throws Exception;
	
	protected abstract Iterator getIteratorImpl(Object container) throws Exception;
}