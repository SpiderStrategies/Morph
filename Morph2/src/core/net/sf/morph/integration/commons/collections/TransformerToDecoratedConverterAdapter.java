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
package net.sf.morph.integration.commons.collections;

import java.util.Locale;

import org.apache.commons.collections.Transformer;

import net.sf.morph.transform.DecoratedConverter;
import net.sf.morph.transform.transformers.BaseTransformer;
import net.sf.morph.util.Assert;
import net.sf.morph.util.ClassUtils;

/**
 * Adapt a org.apache.commons.collections.Transformer to our DecoratedConverter interface.
 *
 * @author mbenson
 * @since Morph 1.1
 */
public class TransformerToDecoratedConverterAdapter extends BaseTransformer implements
		DecoratedConverter {

	private Transformer delegate;

	/**
	 * Create a new TransformerToDecoratedConverterAdapter.
	 */
	public TransformerToDecoratedConverterAdapter() {
	}

	/**
	 * Create a new TransformerToDecoratedConverterAdapter.
	 * @param delegate
	 */
	public TransformerToDecoratedConverterAdapter(Transformer delegate) {
		setDelegate(delegate);
	}

	/**
	 * {@inheritDoc}
	 * @see net.sf.morph.transform.transformers.BaseTransformer#convertImpl(java.lang.Class, java.lang.Object, java.util.Locale)
	 */
	protected Object convertImpl(Class destinationClass, Object source, Locale locale)
			throws Exception {
		Assert.notNull(delegate, "delegate");
		return delegate.transform(source);
	}

	/**
	 * {@inheritDoc}
	 * @see net.sf.morph.transform.transformers.BaseTransformer#setDestinationClasses(java.lang.Class[])
	 */
	public synchronized void setDestinationClasses(Class[] destinationClasses) {
		super.setDestinationClasses(destinationClasses);
	}

	/**
	 * {@inheritDoc}
	 * @see net.sf.morph.transform.transformers.BaseTransformer#getDestinationClassesImpl()
	 */
	protected Class[] getDestinationClassesImpl() throws Exception {
		return ClassUtils.getAllClasses();
	}

	/**
	 * {@inheritDoc}
	 * @see net.sf.morph.transform.transformers.BaseTransformer#setSourceClasses(java.lang.Class[])
	 */
	public synchronized void setSourceClasses(Class[] sourceClasses) {
		super.setSourceClasses(sourceClasses);
	}

	/**
	 * {@inheritDoc}
	 * @see net.sf.morph.transform.transformers.BaseTransformer#getSourceClassesImpl()
	 */
	protected Class[] getSourceClassesImpl() throws Exception {
		return ClassUtils.getAllClasses();
	}

	/**
	 * Get the Transformer delegate.
	 * @return Transformer
	 */
	public Transformer getDelegate() {
		return delegate;
	}

	/**
	 * Set the Transformer delegate.
	 * @param delegate Transformer
	 */
	public void setDelegate(Transformer delegate) {
		this.delegate = delegate;
	}

	/**
	 * {@inheritDoc}
	 * @see net.sf.morph.transform.transformers.BaseTransformer#isAutomaticallyHandlingNulls()
	 */
	protected boolean isAutomaticallyHandlingNulls() {
		return false;
	}

	/**
	 * {@inheritDoc}
	 * @see net.sf.morph.transform.transformers.BaseTransformer#isWrappingRuntimeExceptions()
	 */
	protected boolean isWrappingRuntimeExceptions() {
		return false;
	}
}
