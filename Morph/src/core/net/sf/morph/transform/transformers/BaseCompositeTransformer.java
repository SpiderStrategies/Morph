/*
 * Copyright 2004-2005 the original author or authors.
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
import net.sf.morph.transform.Transformer;

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

}
