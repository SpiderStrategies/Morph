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
package net.sf.morph2.transform.transformers;

import net.sf.composite.util.CompositeUtils;
import net.sf.morph2.reflect.BeanReflector;
import net.sf.morph2.reflect.ContainerReflector;
import net.sf.morph2.reflect.GrowableContainerReflector;
import net.sf.morph2.reflect.IndexedContainerReflector;
import net.sf.morph2.reflect.MutableIndexedContainerReflector;

/**
 * A base class for transformers that are implemented using a Reflector.
 *
 * @author Matt Sgarlata
 * @since Dec 24, 2004
 */
public class BaseReflectorTransformer extends BaseTransformer {

	/**
	 * Create a new BaseReflectorTransformer instance.
	 */
	public BaseReflectorTransformer() {
		super();
	}

	/**
	 * {@inheritDoc}
	 */
	protected Class[] getDestinationClassesImpl() throws Exception {
		return getReflector().getReflectableClasses();
	}

	/**
	 * {@inheritDoc}
	 */
	protected Class[] getSourceClassesImpl() throws Exception {
		return getReflector().getReflectableClasses();
	}

	/**
	 * Get this {@link BaseReflectorTransformer}'s {@link BeanReflector}.
	 * @return {@link BeanReflector}
	 */
	protected BeanReflector getBeanReflector() {
		return (BeanReflector) getReflector(BeanReflector.class);
	}

	/**
	 * Get this {@link BaseReflectorTransformer}'s {@link ContainerReflector}
	 * @return {@link ContainerReflector}
	 */
	protected ContainerReflector getContainerReflector() {
		return (ContainerReflector) getReflector(ContainerReflector.class);
	}

	/**
	 * Get this {@link BaseReflectorTransformer}'s {@link MutableIndexedContainerReflector}.
	 * @return {@link MutableIndexedContainerReflector}
	 */
	protected MutableIndexedContainerReflector getMutableIndexedContainerReflector() {
		return (MutableIndexedContainerReflector) getReflector(MutableIndexedContainerReflector.class);
	}

	/**
	 * Get this {@link BaseReflectorTransformer}'s {@link IndexedContainerReflector}.
	 * @return {@link IndexedContainerReflector}
	 */
	protected IndexedContainerReflector getIndexedContainerReflector() {
		return (IndexedContainerReflector) getReflector(IndexedContainerReflector.class);
	}

	/**
	 * Get this {@link BaseReflectorTransformer}'s {@link GrowableContainerReflector}.
	 * @return {@link GrowableContainerReflector}
	 */
	protected GrowableContainerReflector getGrowableContainerReflector() {
		return (GrowableContainerReflector) getReflector(GrowableContainerReflector.class);
	}

	/**
	 * Learn whether this {@link BaseReflectorTransformer} has a Reflector of the specified type.
	 * @param reflectorType
	 * @return boolean
	 */
	protected boolean hasReflector(Class reflectorType) {
		return CompositeUtils.isSpecializable(getReflector(), reflectorType);
	}
}
