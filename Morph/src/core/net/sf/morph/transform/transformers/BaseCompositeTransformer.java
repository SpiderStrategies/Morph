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
import net.sf.morph.util.ContainerUtils;

/**
 * @author Matt Sgarlata
 * @since Dec 12, 2004
 */
public abstract class BaseCompositeTransformer extends BaseTransformer implements Transformer, SimpleComposite, StrictlyTypedComposite {

	protected Object[] components;
	private ComponentValidator componentValidator;
	
// internal state validation
	
	// TODO consider doing validation when data is specified rather than waiting
	// until the user tries to perform an operation
	protected void initializeImpl() throws Exception {
		super.initializeImpl();
		getComponentValidator().validate(this);
		updateNestedTransformerComponents(getNestedTransformer(), null);
	}

// information about the components of a composite transformer 	
	
	public Class getComponentType() {
		return Transformer.class;
	}
	public Object[] getComponents() {
		return components;
	}
	public void setComponents(Object[] components) {
		setInitialized(false);
		this.components = components;
	}
	
	public ComponentValidator getComponentValidator() {
		if (componentValidator == null) {
			componentValidator = Defaults.createComponentValidator();
		}
		return componentValidator;
	}

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

	protected boolean isWrappingRuntimeExceptions() {
		// composite transformers will often have user specified components,
		// in which case we want to allow user user defined RuntimeExceptions
		// to propogate up the stack.  Morph transformers will already throw
		// TransformationExceptions so if all the composites are transformers
		// that come with Morph, TransformationExceptions will get thrown
	    return false;
    }

}
