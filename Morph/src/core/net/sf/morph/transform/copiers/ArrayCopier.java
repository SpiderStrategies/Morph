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
package net.sf.morph.transform.copiers;

import net.sf.morph.transform.TransformationException;
import net.sf.morph.util.ClassUtils;

/**
 * TODO tests with 3-dimensional arrays
 * 
 * @author Matt Sgarlata
 * @since Dec 11, 2004
 */
public class ArrayCopier extends ContainerCopier {
	
	public ArrayCopier() {
		super();
	}
	
	protected boolean isTransformableImpl(Class destinationType,
		Class sourceType) throws Exception {
		return
			destinationType.isArray() &&
			(
				ClassUtils.inheritanceContains(getReflector().getReflectableClasses(), sourceType) ||
				destinationType.getComponentType().isAssignableFrom(sourceType)
			);
	}
	
	public Class[] getDestinationClassesImpl() throws TransformationException {
		return ClassUtils.ARRAY_TYPES;
	}


}