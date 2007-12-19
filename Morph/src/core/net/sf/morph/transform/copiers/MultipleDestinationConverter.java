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

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import net.sf.morph.Defaults;
import net.sf.morph.transform.Converter;
import net.sf.morph.transform.DecoratedConverter;
import net.sf.morph.transform.Transformer;
import net.sf.morph.transform.transformers.BaseCompositeTransformer;
import net.sf.morph.util.TransformerUtils;

/**
 * A converter which takes a single object and converts it into multiple objects.
 *
 * @author Matt Sgarlata
 * @since Apr 18, 2005
 * @deprecated since v1.0.2 in favor of {@link DisassemblerCopier}
 */
public class MultipleDestinationConverter extends BaseCompositeTransformer implements
		DecoratedConverter {

	private Converter containerConverter;
	private Class[] destinationClassesForEachDestination;

	/**
	 * {@inheritDoc}
	 * @see net.sf.morph.transform.transformers.BaseTransformer#convertImpl(java.lang.Class, java.lang.Object, java.util.Locale)
	 */
	protected Object convertImpl(Class destinationClass, Object source, Locale locale)
			throws Exception {
		Converter[] converters = (Converter[]) getComponents();
		Class[] destTypes = getDestinationClassesForEachDestination();
		List destinationObjects = new ArrayList(converters.length);
		for (int i = 0; i < components.length; i++) {
			Object converted = converters[i].convert(destTypes[i], source, locale);
			destinationObjects.add(converted);
		}
		return getContainerConverter().convert(destinationClass, destinationObjects, locale);
	}

	/**
	 * {@inheritDoc}
	 * @see net.sf.morph.transform.transformers.BaseCompositeTransformer#getComponentType()
	 */
	public Class getComponentType() {
		return Converter.class;
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
		return TransformerUtils
				.getSourceClassIntersection((Transformer[]) getComponents());
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
		return getContainerConverter().getDestinationClasses();
	}

	/**
	 * Get the container converter that will be used by this converter.
	 * @return Converter
	 */
	public Converter getContainerConverter() {
		if (containerConverter == null) {
			containerConverter = Defaults.createContainerCopier();
		}
		return containerConverter;
	}

	/**
	 * Set the container converter that will be used by this converter.
	 * @param containerTransformer Converter
	 */
	public void setContainerConverter(Converter containerTransformer) {
		this.containerConverter = containerTransformer;
	}

	/**
	 * Get the ordered array of destination classes.
	 * @return Class[]
	 */
	public Class[] getDestinationClassesForEachDestination() {
		return destinationClassesForEachDestination;
	}

	/**
	 * Set an ordered array of destination classes.
	 * @param destinations
	 */
	public void setDestinationClassesForEachDestination(Class[] destinations) {
		this.destinationClassesForEachDestination = destinations;
	}

	/**
	 * {@inheritDoc}
	 * @see net.sf.morph.transform.transformers.BaseCompositeTransformer#isWrappingRuntimeExceptions()
	 */
	protected boolean isWrappingRuntimeExceptions() {
		return false;
	}

}
