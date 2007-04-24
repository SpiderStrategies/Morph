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
package net.sf.morph.transform.transformers;

import net.sf.composite.Defaults;
import net.sf.composite.SimpleComposite;
import net.sf.composite.StrictlyTypedComposite;
import net.sf.composite.validate.ComponentValidator;
import net.sf.morph.transform.NodeCopier;
import net.sf.morph.transform.Transformer;
import net.sf.morph.util.ClassUtils;
import net.sf.morph.util.ContainerUtils;

/**
 * @author Matt Sgarlata
 * @since Dec 12, 2004
 */
public abstract class BaseCompositeTransformer extends BaseTransformer implements Transformer, SimpleComposite, StrictlyTypedComposite {

	/** Our component list */
	protected Object[] components;
	private ComponentValidator componentValidator;

// internal state validation

	// TODO consider doing validation when data is specified rather than waiting
	// until the user tries to perform an operation

	/**
	 * {@inheritDoc}
	 * @see net.sf.morph.transform.transformers.BaseTransformer#initializeImpl()
	 */
	protected void initializeImpl() throws Exception {
		super.initializeImpl();
		getComponentValidator().validate(this);
		if (isNarrowingComponentArray()) {
			if (components != null
					&& !getComponentType().isAssignableFrom(
							components.getClass().getComponentType())) {
				Object[] newComponents = (Object[]) ClassUtils.createArray(
						getComponentType(), components.length);
				System.arraycopy(components, 0, newComponents, 0, components.length);
				components = newComponents;
			}
		}
		updateNestedTransformerComponents(getNestedTransformer(), null);
	}

// information about the components of a composite transformer

	/**
	 * Return the component type of this transformer.
	 * @return Class
	 */
	public Class getComponentType() {
		return Transformer.class;
	}

	/**
	 * {@inheritDoc}
	 * @see net.sf.composite.SimpleComposite#getComponents()
	 */
	public Object[] getComponents() {
		return components;
	}

	/**
	 * {@inheritDoc}
	 * @see net.sf.composite.SimpleComposite#setComponents(java.lang.Object[])
	 */
	public void setComponents(Object[] components) {
		setInitialized(false);
		this.components = components;
	}

	/**
	 * Return the ComponentValidator of this transformer.
	 * @return ComponentValidator
	 */
	public ComponentValidator getComponentValidator() {
		if (componentValidator == null) {
			componentValidator = Defaults.createComponentValidator();
		}
		return componentValidator;
	}

	/**
	 * Set the ComponentValidator for this transformer.
	 * @param componentValidator ComponentValidator
	 */
	public void setComponentValidator(ComponentValidator componentValidator) {
		this.componentValidator = componentValidator;
	}

	/* (non-Javadoc)
	 * @see net.sf.morph.transform.transformers.BaseTransformer#setNestedTransformer(net.sf.morph.transform.Transformer)
	 */
	protected void setNestedTransformer(Transformer nestedTransformer) {
		Transformer old = getNestedTransformer();
		super.setNestedTransformer(nestedTransformer);
		updateNestedTransformerComponents(nestedTransformer, old);
	}

	/**
	 * Propagate our nested transformer to any component NodeCopiers.
	 * @param incoming
	 * @param outgoing
	 */
	protected void updateNestedTransformerComponents(Transformer incoming, Transformer outgoing) {
		if (incoming == outgoing) {
			return;
		}
		NodeCopier[] nodeCopiers = (NodeCopier[]) ContainerUtils.getElementsOfType(getComponents(), NodeCopier.class);
		for (int i = 0; i < nodeCopiers.length; i++) {
			if (nodeCopiers[i].getNestedTransformer() == outgoing) {
				nodeCopiers[i].setNestedTransformer(incoming);
			}
		}
	}

	/**
	 * {@inheritDoc}
	 * @see net.sf.morph.transform.transformers.BaseTransformer#isWrappingRuntimeExceptions()
	 */
	protected boolean isWrappingRuntimeExceptions() {
		// composite transformers will often have user specified components,
		// in which case we want to allow user user defined RuntimeExceptions
		// to propogate up the stack.  Morph transformers will already throw
		// TransformationExceptions so if all the composites are transformers
		// that come with Morph, TransformationExceptions will get thrown
	    return false;
    }

	/**
	 * Learn whether we should automatically narrow the array type of <code>components</code>
	 * to that returned by {@link #getComponentType()} after validating the components.
	 * @return default <code>true</code>
	 */
	protected boolean isNarrowingComponentArray() {
		return true;
	}
}
