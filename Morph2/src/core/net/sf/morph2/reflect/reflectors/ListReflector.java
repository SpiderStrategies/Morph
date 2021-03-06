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
package net.sf.morph2.reflect.reflectors;

import java.util.ArrayList;
import java.util.List;

import net.sf.morph2.reflect.BeanReflector;
import net.sf.morph2.reflect.GrowableContainerReflector;
import net.sf.morph2.reflect.IndexedContainerReflector;
import net.sf.morph2.reflect.InstantiatingReflector;
import net.sf.morph2.reflect.MutableIndexedContainerReflector;

/**
 * A reflector for {@link java.util.List}s.
 * 
 * @author Matt Sgarlata
 * @since Nov 26, 2004
 */
public class ListReflector extends SortedSetReflector implements IndexedContainerReflector,
		MutableIndexedContainerReflector, GrowableContainerReflector, InstantiatingReflector,
		BeanReflector {

	private static final Class[] REFLECTABLE_TYPES = new Class[] { List.class };

	/**
	 * {@inheritDoc}
	 */
	protected Object newInstanceImpl(Class interfaceClass, Object parameters) throws Exception {
		return interfaceClass == List.class ? new ArrayList() : super.newInstanceImpl(
				interfaceClass, parameters);
	}

	/**
	 * {@inheritDoc}
	 */
	public Class[] getReflectableClassesImpl() {
		return REFLECTABLE_TYPES;
	}

	/**
	 * {@inheritDoc}
	 */
	protected Object setImpl(Object container, int index, Object propertyValue) throws Exception {
		return ((List) container).set(index, propertyValue);
	}

}
