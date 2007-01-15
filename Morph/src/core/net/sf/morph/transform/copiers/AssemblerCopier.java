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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
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
import net.sf.morph.util.ContainerUtils;

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
	private static final Copier DEFAULT_COPIER = Defaults.createCopier();

	//allow null components; means we have a different strategy
	private static final ComponentValidator DEFAULT_VALIDATOR = new SimpleComponentValidator() {
		protected void validateImpl(Object composite) throws Exception {
			List components = getComponentAccessor().getComponents(composite);
			if (components != null) {
				super.validateImpl(composite);
			}
		}
	};

	private Copier copier;
	private Class[] destinationClasses;

	{
		setComponentValidator(DEFAULT_VALIDATOR);
	}

	protected void copyImpl(Object destination, Object source, Locale locale,
			Integer preferredTransformationType) throws Exception {

		int index = 0;
		for (Iterator iter = getContainerReflector().getIterator(source); iter.hasNext(); index++) {
			getCopier(index).copy(destination, iter.next(), locale);
		}
	}

	public Class getComponentType() {
		return Transformer.class;
	}

	protected Class[] getSourceClassesImpl() throws Exception {
		return getContainerReflector().getReflectableClasses();
	}

	protected synchronized Class[] getDestinationClassesImpl() throws Exception {
		if (destinationClasses == null) {
			Object[] components = getComponents();
			if (components == null) {
				destinationClasses = getCopier().getDestinationClasses();
			} else if (components.length == 0) {
				destinationClasses = new Class[0];
			} else {
				//find all classes supported by all components:
				HashSet set = new HashSet(Arrays.asList(((Copier) components[0])
						.getDestinationClasses()));
				for (int i = 1; i < components.length; i++) {
					set.retainAll(Arrays.asList(((Copier) components[i]).getDestinationClasses()));
				}
				destinationClasses = (Class[]) set.toArray(new Class[set.size()]);
			}
		}
		return destinationClasses;
	}

	protected ContainerReflector getContainerReflector() {
		return (ContainerReflector) getReflector(ContainerReflector.class);
	}

	protected Copier getCopier(int index) {
		Object[] components = getComponents();
		if (components != null) {
			if (components.length > index + 1) {
				throw new TransformationException("Invalid copier requested: " + index
						+ " of " + components.length);
			}
			return (Copier) components[index];
		}
		return getCopier();
	}

	public synchronized void setComponents(Object[] components) {
		super.setComponents(components);
		destinationClasses = null;
	}

	/**
	 * Get the Copier which, in the absence of a components list, will be used
	 * for all copies.
	 * @return a Copier instance
	 */
	public synchronized Copier getCopier() {
		return copier == null ? DEFAULT_COPIER : copier;
	}

	/**
	 * Set the Copier which, in the absence of a components list, will be used
	 * for all copies.
	 * @param copier the Copier to use
	 */
	public synchronized void setCopier(Copier copier) {
		this.copier = copier;
	}

	public void setComponentValidator(ComponentValidator componentValidator) {
		super.setComponentValidator(componentValidator == null ? DEFAULT_VALIDATOR
				: componentValidator);
	}
}
