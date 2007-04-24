/*
 * Copyright 2007 the original author or authors.
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

import java.util.Locale;

import net.sf.morph.transform.Copier;
import net.sf.morph.transform.DecoratedConverter;
import net.sf.morph.transform.DecoratedCopier;
import net.sf.morph.transform.Transformer;
import net.sf.morph.transform.transformers.BaseCompositeTransformer;
import net.sf.morph.util.TransformerUtils;

/**
 * Composite Transformer whose children must be Copiers and which invokes
 * each child Copier in turn in the course of performing a copy operation.
 *
 * @author Matt Benson
 * @since Morph 1.0.2
 */
public class CumulativeCopier extends BaseCompositeTransformer implements
		DecoratedCopier, DecoratedConverter {

	/**
	 * Returns the destination classes supported by <em>all</em> components.
	 * 
	 * @return Class[]
	 */
	protected synchronized Class[] getDestinationClassesImpl() throws Exception {
		return TransformerUtils.getDestinationClassIntersection((Transformer[]) getComponents());
	}

	/**
	 * Returns the source classes supported by <em>all</em> components.
	 * 
	 * @return Class[]
	 */
	protected Class[] getSourceClassesImpl() throws Exception {
		return TransformerUtils.getSourceClassIntersection((Transformer[]) getComponents());
	}

	/*
	 * (non-Javadoc)
	 * @see net.sf.morph.transform.transformers.BaseCompositeTransformer#getComponentType()
	 */
	public Class getComponentType() {
		return Copier.class;
	}

	/**
	 * {@inheritDoc}
	 * @see net.sf.morph.transform.transformers.BaseTransformer#copyImpl(java.lang.Object, java.lang.Object, java.util.Locale, java.lang.Integer)
	 */
	protected void copyImpl(Object destination, Object source, Locale locale,
			Integer preferredTransformationType) throws Exception {
		Copier[] copiers = (Copier[]) getComponents();
		for (int i = 0; i < copiers.length; i++) {
			copiers[i].copy(destination, source, locale);
		}
	}

	/**
	 * {@inheritDoc}
	 * @see net.sf.morph.transform.transformers.BaseCompositeTransformer#isWrappingRuntimeExceptions()
	 */
	protected boolean isWrappingRuntimeExceptions() {
	    return false;
    }

}
