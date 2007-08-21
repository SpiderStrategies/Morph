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

import java.util.Arrays;
import java.util.HashSet;
import java.util.Locale;

import net.sf.morph.transform.Converter;
import net.sf.morph.transform.DecoratedConverter;
import net.sf.morph.transform.DecoratedCopier;
import net.sf.morph.transform.ExplicitTransformer;
import net.sf.morph.transform.TransformationException;
import net.sf.morph.transform.Transformer;
import net.sf.morph.transform.converters.DefaultToBooleanConverter;
import net.sf.morph.transform.transformers.BaseTransformer;
import net.sf.morph.util.Assert;
import net.sf.morph.util.ClassUtils;
import net.sf.morph.util.TransformerUtils;

/**
 * This copier uses an <code>if</code> <code>Converter</code> to convert an incoming object to a Boolean.
 * If this results in a Boolean object corresponding to boolean true, the then
 * Transformer is invoked; otherwise the optional else Transformer is invoked if present. 
 *
 * @author Matt Benson
 * @since Morph 1.0.2
 */
public class ConditionalCopier extends BaseTransformer implements DecoratedConverter,
		DecoratedCopier, ExplicitTransformer {
	private static final Converter DEFAULT_IF = new DefaultToBooleanConverter();

	private Converter ifConverter;
	private Transformer thenTransformer;
	private Transformer elseTransformer;

	/*
	 * (non-Javadoc)
	 * @see net.sf.morph.transform.transformers.BaseTransformer#getDestinationClassesImpl()
	 */
	protected Class[] getDestinationClassesImpl() throws Exception {
		Transformer tt = getThenTransformer();
		Transformer et = getElseTransformer();
		return et == null ? tt.getDestinationClasses() : tt == null ? et
				.getDestinationClasses() : merge(tt.getDestinationClasses(), et
				.getDestinationClasses());
	}

	/*
	 * (non-Javadoc)
	 * @see net.sf.morph.transform.transformers.BaseTransformer#getSourceClassesImpl()
	 */
	protected Class[] getSourceClassesImpl() throws Exception {
		Transformer tt = getThenTransformer();
		Transformer et = getElseTransformer();
		return et == null ? tt.getSourceClasses() : tt == null ? et.getSourceClasses()
				: merge(tt.getSourceClasses(), et.getSourceClasses());
	}

	/* (non-Javadoc)
	 * @see net.sf.morph.transform.transformers.BaseTransformer#isAutomaticallyHandlingNulls()
	 */
	protected boolean isAutomaticallyHandlingNulls() {
		return false;
	}

	/* (non-Javadoc)
	 * @see net.sf.morph.transform.transformers.BaseTransformer#isTransformableImpl(java.lang.Class, java.lang.Class)
	 */
	protected boolean isTransformableImpl(Class destinationType, Class sourceType)
			throws Exception {
		return TransformerUtils.isTransformable(getIfConverter(), Boolean.class,
				sourceType)
				&& (TransformerUtils.isTransformable(getThenTransformer(),
						destinationType, sourceType) || TransformerUtils.isTransformable(
						getElseTransformer(), destinationType, sourceType));
	}

	/**
	 * {@inheritDoc}
	 */
	public synchronized void setDestinationClasses(Class[] destinationClasses) {
		super.setDestinationClasses(destinationClasses);
	}

	private Class[] merge(Class[] a, Class[] b) {
		HashSet s = new HashSet();
		s.addAll(Arrays.asList(a));
		s.addAll(Arrays.asList(b));
		return (Class[]) s.toArray(new Class[s.size()]);
	}

	/**
	 * {@inheritDoc}
	 */
	public synchronized void setSourceClasses(Class[] sourceClasses) {
		super.setSourceClasses(sourceClasses);
	}

	/*
	 * (non-Javadoc)
	 * @see net.sf.morph.transform.transformers.BaseTransformer#convertImpl(java.lang.Class, java.lang.Object, java.util.Locale)
	 */
	protected Object convertImpl(Class destinationClass, Object source, Locale locale)
			throws Exception {
		return transform(destinationClass, null, source, locale,
				TRANSFORMATION_TYPE_CONVERT);
	}

	/*
	 * (non-Javadoc)
	 * @see net.sf.morph.transform.transformers.BaseTransformer#copyImpl(java.lang.Object, java.lang.Object, java.util.Locale, java.lang.Integer)
	 */
	protected void copyImpl(Object destination, Object source, Locale locale,
			Integer preferredTransformationType) throws Exception {
		transform(ClassUtils.getClass(destination), destination, source, locale,
				TRANSFORMATION_TYPE_COPY);
	}

	/**
	 * Do the transformation.
	 * @param destinationType
	 * @param destination
	 * @param source
	 * @param locale
	 * @param preferredTransformationType
	 * @return
	 * @throws TransformationException
	 */
	protected Object transform(Class destinationType, Object destination, Object source,
			Locale locale, Integer preferredTransformationType)
			throws TransformationException {
		Transformer t;
		if (evaluateIf(source, locale)) {
			t = getThenTransformer();
			//note that the thenTransformer is not required until it is intended to be used
			Assert.notNull(t, "thenTransformer");
		}
		else {
			t = getElseTransformer();
		}
		return t == null ? destination : TransformerUtils.transform(t, destinationType,
				destination, source, locale, preferredTransformationType);
	}

	private boolean evaluateIf(Object source, Locale locale) {
		Boolean b = (Boolean) getIfConverter().convert(Boolean.class, source, locale);
		return Boolean.TRUE.equals(b);
	}

	/**
	 * Get the ifConverter.
	 * @return Converter.
	 */
	public Converter getIfConverter() {
		return ifConverter == null ? DEFAULT_IF : ifConverter;
	}

	/**
	 * Set the ifConverter. By default it will be an instance of {@link DefaultToBooleanConverter}.
	 * @param ifConverter the Converter ifConverter to set.
	 */
	public void setIfConverter(Converter ifConverter) {
		this.ifConverter = ifConverter;
	}

	/**
	 * Get the thenTransformer.
	 * @return Transformer.
	 */
	public Transformer getThenTransformer() {
		return thenTransformer;
	}

	/**
	 * Set the thenTransformer.
	 * @param thenTransformer the Transformer thenTransformer to set.
	 */
	public void setThenTransformer(Transformer thenTransformer) {
		this.thenTransformer = thenTransformer;
		setInitialized(false);
	}

	/**
	 * Get the elseTransformer.
	 * @return Transformer.
	 */
	public Transformer getElseTransformer() {
		return elseTransformer;
	}

	/**
	 * Set the elseTransformer.
	 * @param elseTransformer the Transformer elseTransformer to set.
	 */
	public void setElseTransformer(Transformer elseTransformer) {
		this.elseTransformer = elseTransformer;
		setInitialized(false);
	}

}
