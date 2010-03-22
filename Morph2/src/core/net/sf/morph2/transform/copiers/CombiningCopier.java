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

import java.util.Locale;

import net.sf.morph2.transform.DecoratedConverter;
import net.sf.morph2.transform.DecoratedCopier;
import net.sf.morph2.transform.TransformationException;
import net.sf.morph2.transform.TransformationType;
import net.sf.morph2.transform.transformers.BaseReflectorTransformer;

/**
 * Combines all of the contents of a container into a single container. If any
 * of the contents of the source container are containers themselves, the
 * elements are extracted and put into the destination container. Examples:
 * <ul>
 * <li>combines an array of Lists into just a single array, with all the
 * elements from each List copied into the destination array</li>
 * <li>combines [14, "hello", ["this", "is", "another", "array"], "the", "end"]
 * (this is a list with 5 elements, a number, a string, an array, and two more
 * strings) into [14, "hello", "this", "is", "another", "array", "the", "end]
 * (this list has 8 elements, a number and 7 strings)</li>
 * </ul>
 * UNFINISHED 
 * @author Matt Sgarlata
 * @since Dec 5, 2004
 */
public class CombiningCopier extends BaseReflectorTransformer implements DecoratedCopier,
		DecoratedConverter {

	/**
	 * Create a new CombiningCopier.
	 */
	public CombiningCopier() {
		super();
	}

	/**
	 * {@inheritDoc}
	 */
	protected void copyImpl(Object destination, Object source, Locale locale, TransformationType preferredTransformationType) throws TransformationException {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException("not implemented");
	}

	/**
	 * {@inheritDoc}
	 */
	protected Class[] getSourceClassesImpl() throws Exception {
		return getContainerReflector().getReflectableClasses();
	}

	/**
	 * {@inheritDoc}
	 */
	protected Class[] getDestinationClassesImpl() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

}
