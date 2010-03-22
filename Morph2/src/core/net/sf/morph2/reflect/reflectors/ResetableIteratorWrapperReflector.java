/*
 * Copyright 2007, 2010 the original author or authors.
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

import net.sf.morph2.reflect.SizableReflector;
import net.sf.morph2.transform.support.ResetableIteratorWrapper;

/**
 * Exposes the information available in a ResetableIteratorWrapper.
 * Intended for internal use.
 * 
 * @author Matt Sgarlata
 * @author Matt Benson
 */
public class ResetableIteratorWrapperReflector extends IteratorReflector implements SizableReflector {

	private static final Class[] REFLECTABLE_TYPES = new Class[] { ResetableIteratorWrapper.class };

	/**
	 * {@inheritDoc}
	 */
	protected Class[] getReflectableClassesImpl() {
		return REFLECTABLE_TYPES;
	}

	/**
	 * {@inheritDoc}
	 */
	protected int getSizeImpl(Object container) throws Exception {
		return ((ResetableIteratorWrapper) container).size();
	}
}
