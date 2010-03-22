/*
 * Copyright 2007-2008 the original author or authors.
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

import java.util.Iterator;
import java.util.Locale;

import net.sf.morph2.transform.DecoratedConverter;
import net.sf.morph2.transform.DecoratedCopier;
import net.sf.morph2.transform.Transformer;
import net.sf.morph2.util.TransformerUtils;

/**
 * A copier that copies multiple source objects to a single destination object, implementing an "Assembler."
 *
 * @see http://www.martinfowler.com/eaaCatalog/dataTransferObject.html
 *
 * @author Matt Benson
 * @since Morph 1.1
 */
public class AssemblerCopier extends AssemblyCopierSupport implements DecoratedCopier,
		DecoratedConverter {

	/**
	 * Create a new AssemblerCopier.
	 */
	public AssemblerCopier() {
		super();
	}

	/**
	 * Create a new AssemblerCopier.
	 * @param components
	 */
	public AssemblerCopier(Object[] components) {
		super(components);
	}

	/**
	 * {@inheritDoc}
	 * @see net.sf.morph2.transform.transformers.BaseTransformer#copyImpl(java.lang.Object, java.lang.Object, java.util.Locale, java.lang.Integer)
	 */
	protected void copyImpl(Object destination, Object source, Locale locale,
			Integer preferredTransformationType) throws Exception {

		int index = 0;
		for (Iterator iter = getContainerReflector().getIterator(source); iter.hasNext(); index++) {
			getCopier(index).copy(destination, iter.next(), locale);
		}
	}

	/**
	 * {@inheritDoc}
	 * @see net.sf.morph2.transform.transformers.BaseTransformer#getSourceClassesImpl()
	 */
	protected Class[] getSourceClassesImpl() throws Exception {
		return getContainerReflector().getReflectableClasses();
	}

	/**
	 * {@inheritDoc}
	 * @see net.sf.morph2.transform.transformers.BaseTransformer#getDestinationClassesImpl()
	 */
	protected synchronized Class[] getDestinationClassesImpl() throws Exception {
		Object[] components = getComponents();
		if (components == null) {
			return getNestedTransformer().getDestinationClasses();
		}
		if (components.length == 0) {
			return new Class[0];
		}
		return TransformerUtils
				.getDestinationClassIntersection((Transformer[]) getComponents());
	}
}
