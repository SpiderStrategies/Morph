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
package net.sf.morph.transform.transformers;

import net.sf.morph.reflect.BeanReflector;
import net.sf.morph.reflect.ContainerReflector;
import net.sf.morph.reflect.GrowableContainerReflector;
import net.sf.morph.reflect.IndexedContainerReflector;
import net.sf.morph.reflect.MutableIndexedContainerReflector;

/**
 * A base class for transformers that are implemented using a Reflector.
 * 
 * @author Matt Sgarlata
 * @since Dec 24, 2004
 */
public class BaseReflectorTransformer extends BaseTransformer {
	
	public BaseReflectorTransformer() {
		super();
	}
	
	protected Class[] getDestinationClassesImpl() throws Exception {
		return getReflector().getReflectableClasses();
	}
	protected Class[] getSourceClassesImpl() throws Exception {
		return getReflector().getReflectableClasses();
	}
	
	protected BeanReflector getBeanReflector() {
		return (BeanReflector) getReflector(BeanReflector.class);
	}

	protected ContainerReflector getContainerReflector() {
		return (ContainerReflector) getReflector(ContainerReflector.class);
	}

	protected MutableIndexedContainerReflector getMutableIndexedContainerReflector() {
		return (MutableIndexedContainerReflector) getReflector(MutableIndexedContainerReflector.class);
	}

	protected IndexedContainerReflector getIndexedContainerReflector() {
		return (IndexedContainerReflector) getReflector(IndexedContainerReflector.class);
	}

	protected GrowableContainerReflector getGrowableContainerReflector() {
		return (GrowableContainerReflector) getReflector(GrowableContainerReflector.class);
	}

}