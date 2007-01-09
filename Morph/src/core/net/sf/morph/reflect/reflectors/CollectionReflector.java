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

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import net.sf.morph.reflect.ContainerReflector;
import net.sf.morph.reflect.GrowableContainerReflector;
import net.sf.morph.reflect.InstantiatingReflector;
import net.sf.morph.reflect.SizableReflector;

/**
 * A reflector for {@link java.util.Collection}s.
 * 
 * @author Matt Sgarlata
 * @since Nov 20, 2004
 */
public class CollectionReflector extends BaseContainerReflector implements ContainerReflector, SizableReflector, InstantiatingReflector, GrowableContainerReflector {

	private static final Class[] REFLECTABLE_TYPES = new Class[] {
		Collection.class
	};
	
	protected Collection getCollection(Object container) {
		return (Collection) container;
	}
	
	public Class[] getReflectableClassesImpl() {
		return REFLECTABLE_TYPES;
	}

	/**
	 * TODO JDK 1.5 compatability
	 */
	protected Class getContainedTypeImpl(Class clazz) throws Exception {
		return Object.class;
	}

	protected int getSizeImpl(Object container) throws Exception {
		return getCollection(container).size();
	}

	protected Iterator getIteratorImpl(Object container) throws Exception {
		return getCollection(container).iterator();
	}

	protected Object newInstanceImpl(Class clazz) throws Exception {
		return clazz == Collection.class ? new ArrayList() : super.newInstanceImpl(clazz);
	}

	protected boolean addImpl(Object container, Object value) {
		return ((Collection) container).add(value);
	}

}