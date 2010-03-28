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

import java.lang.reflect.Array;
import java.util.Iterator;

import net.sf.composite.util.ArrayIterator;
import net.sf.morph2.Defaults;
import net.sf.morph2.reflect.BeanReflector;
import net.sf.morph2.reflect.ContainerReflector;
import net.sf.morph2.reflect.IndexedContainerReflector;
import net.sf.morph2.reflect.InstantiatingReflector;
import net.sf.morph2.reflect.MutableIndexedContainerReflector;
import net.sf.morph2.reflect.SizableReflector;
import net.sf.morph2.util.ClassUtils;

/**
 * A container reflector for Arrays.
 * 
 * @author Matt Sgarlata
 * @since Nov 20, 2004
 */
public class ArrayReflector extends BaseContainerReflector implements ContainerReflector,
		IndexedContainerReflector, MutableIndexedContainerReflector, SizableReflector,
		BeanReflector, InstantiatingReflector {

	private SizableReflector sourceReflector;

	/**
	 * Create a new ArrayReflector instance.
	 */
	public ArrayReflector() {
		super();
		// by default use the best reflectors available to Morph to try to
		// determine the size of the (optional) parameter to the newInstance
		// method which indicates the size of the array to be created.  If
		// a static list of reflectors is enumerated here, new reflectors
		// added to morph won't automatically get added here (as was recently
		// the case with the StringTokenizerReflector)
		sourceReflector = Defaults.createSizableReflector();
	}

	/**
	 * Create a new ArrayReflector instance.
	 * @param sourceReflector
	 */
	public ArrayReflector(SizableReflector sourceReflector) {
		super();
		this.sourceReflector = sourceReflector;
	}

	/**
	 * {@inheritDoc}
	 */
	protected Class[] getReflectableClassesImpl() {
		return ClassUtils.ARRAY_TYPES;
	}

	/**
	 * {@inheritDoc}
	 */
	protected Iterator getIteratorImpl(Object container) throws Exception {
		return new ArrayIterator(container);
	}

	/**
	 * {@inheritDoc}
	 */
	protected int getSizeImpl(Object container) throws Exception {
		return Array.getLength(container);
	}

	/**
	 * {@inheritDoc}
	 */
	protected Object getImpl(Object container, int index) throws Exception {
		return Array.get(container, index);
	}

	/**
	 * {@inheritDoc}
	 */
	protected Object setImpl(Object container, int index, Object propertyValue) throws Exception {
		Object oldValue = getImpl(container, index);
		Array.set(container, index, propertyValue);
		return oldValue;
	}

	/**
	 * {@inheritDoc}
	 */
	protected Class getContainedTypeImpl(Class clazz) throws Exception {
		return ClassUtils.getContainedClass(clazz);
	}

	/**
	 * {@inheritDoc}
	 */
	/* The default implementation is correct, but this is faster because it
	 * doesn't require looping through all the reflectable classes
	 */
	protected boolean isReflectableImpl(Class clazz) throws Exception {
		return clazz.isArray();
	}

	/**
	 * {@inheritDoc}
	 */
	protected Object newInstanceImpl(Class clazz, Object parameters) throws Exception {
		int length = parameters == null ? 0 : getSourceReflector().getSize(parameters);
		return ClassUtils.createArray(clazz.getComponentType(), length);
	}

	/**
	 * Get the sizable source reflector
	 * @return {@link SizableReflector}
	 */
	public SizableReflector getSourceReflector() {
		return sourceReflector;
	}

	/**
	 * Set the sizable source reflector.
	 * @param sourceReflector
	 */
	public void setSourceReflector(SizableReflector sourceReflector) {
		this.sourceReflector = sourceReflector;
	}
}
