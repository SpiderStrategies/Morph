/*
 * Copyright 2004-2005, 2010 the original author or authors.
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
package net.sf.morph2.reflect.reflectors;

import java.util.HashSet;
import java.util.Set;

import net.sf.morph2.reflect.GrowableContainerReflector;
import net.sf.morph2.reflect.InstantiatingReflector;

/**
 * Exposes the information found in a {@link java.util.Set}.
 * 
 * @author Matt Sgarlata
 * @since Nov 26, 2004
 */
public class SetReflector
	extends CollectionReflector
	implements GrowableContainerReflector, InstantiatingReflector {

	private static final Class[] REFLECTABLE_TYPES = new Class[] { Set.class };

	/**
	 * {@inheritDoc}
	 */
	protected Object newInstanceImpl(Class interfaceClass, Object parameters) throws Exception {
		return interfaceClass == Set.class ? new HashSet() : super.newInstanceImpl(interfaceClass, parameters);
	}

	/**
	 * {@inheritDoc}
	 */
	public Class[] getReflectableClassesImpl() {
		return REFLECTABLE_TYPES;
	}

}
