/*
 * Copyright 2004-2005, 2007 the original author or authors.
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
import net.sf.morph.transform.NodeCopier;
import net.sf.morph.transform.TransformationException;
import net.sf.morph.transform.Transformer;
import net.sf.morph.transform.transformers.BaseTransformer;

/**
 * Decorates any Copier so that it implements DecoratedCopier.
 * 
 * @author Matt Sgarlata
 * @since Dec 5, 2004
 */
public class CopierDecorator extends BaseTransformer implements DecoratedCopier,
		DecoratedConverter {

	private Copier nestedCopier;

	/**
	 * Create a new CopierDecorator.
	 */
	public CopierDecorator() {
		super();
	}

	/**
	 * Create a new CopierDecorator.
	 * @param copier
	 */
	public CopierDecorator(Copier copier) {
		setNestedCopier(copier);
	}

	/* (non-Javadoc)
	 * @see net.sf.morph.transform.transformers.BaseTransformer#copyImpl(java.lang.Object, java.lang.Object, java.util.Locale, java.lang.Integer)
	 */
	protected void copyImpl(Object destination, Object source, Locale locale,
			Integer preferredTransformationType) throws TransformationException {
		getNestedCopier().copy(destination, source, locale);
	}

	/**
	 * @return Returns the nested copier.
	 */
	public Copier getNestedCopier() {
		return nestedCopier;
	}

	/**
	 * @param copier The nested copier to set.
	 */
	public void setNestedCopier(Copier copier) {
		this.nestedCopier = copier;
		if (copier instanceof NodeCopier) {
			((NodeCopier) copier).setNestedTransformer(getNestedTransformer());
		}
	}

	/**
	 * {@inheritDoc}
	 * @see net.sf.morph.transform.transformers.BaseTransformer#setSourceClasses(java.lang.Class[])
	 */
	public synchronized void setSourceClasses(Class[] sourceClasses) {
		super.setSourceClasses(sourceClasses);
	}

	/* (non-Javadoc)
	 * @see net.sf.morph.transform.transformers.BaseTransformer#getSourceClassesImpl()
	 */
	protected Class[] getSourceClassesImpl() {
		return nestedCopier.getSourceClasses();
	}

	/**
	 * {@inheritDoc}
	 * @see net.sf.morph.transform.transformers.BaseTransformer#setDestinationClasses(java.lang.Class[])
	 */
	public synchronized void setDestinationClasses(Class[] destinationClasses) {
		super.setDestinationClasses(destinationClasses);
	}

	/* (non-Javadoc)
	 * @see net.sf.morph.transform.transformers.BaseTransformer#getDestinationClassesImpl()
	 */
	protected Class[] getDestinationClassesImpl() {
		return nestedCopier.getDestinationClasses();
	}

	/* (non-Javadoc)
	 * @see net.sf.morph.transform.transformers.BaseTransformer#isWrappingRuntimeExceptions()
	 */
	protected boolean isWrappingRuntimeExceptions() {
		// the whole point of this copier is for decorating user defined
		// transformers, so we don't want to eat their exceptions ;)
		return false;
	}

	/* (non-Javadoc)
	 * @see net.sf.morph.transform.transformers.BaseTransformer#setNestedTransformer(net.sf.morph.transform.Transformer)
	 */
	protected void setNestedTransformer(Transformer nestedTransformer) {
		super.setNestedTransformer(nestedTransformer);
		if (nestedCopier instanceof NodeCopier) {
			((NodeCopier) nestedCopier).setNestedTransformer(nestedTransformer);
		}
	}
}
