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

import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import net.sf.composite.validate.ComponentValidator;
import net.sf.composite.validate.validators.SimpleComponentValidator;
import net.sf.morph.Defaults;
import net.sf.morph.reflect.ContainerReflector;
import net.sf.morph.transform.Copier;
import net.sf.morph.transform.DecoratedConverter;
import net.sf.morph.transform.DecoratedCopier;
import net.sf.morph.transform.TransformationException;
import net.sf.morph.transform.Transformer;
import net.sf.morph.transform.transformers.BaseCompositeTransformer;
import net.sf.morph.util.TransformerUtils;

/**
 * A copier that copies multiple source objects to a single destination object, implementing an "Assembler."
 *
 * @see http://www.martinfowler.com/eaaCatalog/dataTransferObject.html
 *
 * @author Matt Benson
 * @since Morph 1.0.2
 */
public class AssemblerCopier extends BaseCompositeTransformer implements DecoratedCopier,
		DecoratedConverter {

	//allow null components; means we have a different strategy
	private static final ComponentValidator DEFAULT_VALIDATOR = new SimpleComponentValidator() {
		protected void validateImpl(Object composite) throws Exception {
			List components = getComponentAccessor().getComponents(composite);
			if (components != null) {
				super.validateImpl(composite);
			}
		}
	};

	{
		setComponentValidator(DEFAULT_VALIDATOR);
	}

	/**
	 * {@inheritDoc}
	 */
	protected void initializeImpl() throws Exception {
		if (getNestedTransformer() == null) {
			setNestedTransformer(Defaults.createCopier());
		}
		super.initializeImpl();
	}

	/**
	 * {@inheritDoc}
	 * @see net.sf.morph.transform.transformers.BaseTransformer#copyImpl(java.lang.Object, java.lang.Object, java.util.Locale, java.lang.Integer)
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
	 * @see net.sf.morph.transform.transformers.BaseCompositeTransformer#getComponentType()
	 */
	public Class getComponentType() {
		return Copier.class;
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
		return getContainerReflector().getReflectableClasses();
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

	/**
	 * Get the ContainerReflector used by this Transformer.
	 * @return ContainerReflector
	 */
	protected ContainerReflector getContainerReflector() {
		return (ContainerReflector) getReflector(ContainerReflector.class);
	}

	/**
	 * Set the transformer (Copier) which, in the absence of a components list, will be
	 * used to perform nested transformations.
	 * 
	 * @param nestedTransformer
	 *            the transformer used to perform nested transformations
	 */
	public void setNestedTransformer(Transformer nestedTransformer) {
		super.setNestedTransformer(nestedTransformer);
	}

	/**
	 * Get the transformer (Copier) which, in the absence of a components list, will be
	 * used to perform nested transformations.
	 * 
	 * @return nested Transformer
	 */
	public Transformer getNestedTransformer() {
		return super.getNestedTransformer();
	}

	/**
	 * Get the Copier that will perform the copy for index <code>index</code>.
	 * @param index
	 * @return Copier
	 */
	protected Copier getCopier(int index) {
		Object[] components = getComponents();
		if (components != null) {
			if (components.length > index + 1) {
				throw new TransformationException("Invalid copier requested: " + index
						+ " of " + components.length);
			}
			return (Copier) components[index];
		}
		return (Copier) getNestedTransformer();
	}

	/**
	 * {@inheritDoc}
	 * @see net.sf.morph.transform.transformers.BaseCompositeTransformer#setComponents(java.lang.Object[])
	 */
	public synchronized void setComponents(Object[] components) {
		super.setComponents(components);
	}

	/**
	 * {@inheritDoc}
	 * @see net.sf.morph.transform.transformers.BaseCompositeTransformer#setComponentValidator(net.sf.composite.validate.ComponentValidator)
	 */
	public void setComponentValidator(ComponentValidator componentValidator) {
		super.setComponentValidator(componentValidator == null ? DEFAULT_VALIDATOR
				: componentValidator);
	}
}
