/*
 * Copyright 2004-2005, 2008 the original author or authors.
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
package net.sf.morph2.transform.copiers;

import net.sf.morph2.transform.ExplicitTransformer;
import net.sf.morph2.transform.TransformationException;
import net.sf.morph2.util.ClassUtils;

/**
 * TODO tests with 3-dimensional arrays
 * 
 * @author Matt Sgarlata
 * @since Dec 11, 2004
 */
public class ArrayCopier extends ContainerCopier implements ExplicitTransformer {

	/**
	 * Create a new ArrayCopier.
	 */
	public ArrayCopier() {
		super();
	}

	/**
	 * {@inheritDoc}
	 */
	protected boolean isTransformableImpl(Class destinationType,
		Class sourceType) throws Exception {
		return
			destinationType.isArray() &&
			(
				ClassUtils.inheritanceContains(getReflector().getReflectableClasses(), sourceType) ||
				destinationType.getComponentType().isAssignableFrom(sourceType)
			);
	}

	/**
	 * {@inheritDoc}
	 */
	protected Class[] getDestinationClassesImpl() throws TransformationException {
		return ClassUtils.ARRAY_TYPES;
	}

}
