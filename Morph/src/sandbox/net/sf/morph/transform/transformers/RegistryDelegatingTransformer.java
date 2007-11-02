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
package net.sf.morph.transform.transformers;

import java.util.Map;

import net.sf.composite.RegistryComposite;
import net.sf.composite.validate.ComponentValidator;
import net.sf.composite.validate.validators.SimpleComponentValidator;

/**
 * 
 * @author Matt Sgarlata
 * @since Apr 22, 2007
 */
public class RegistryDelegatingTransformer extends BaseTransformer implements RegistryComposite {
	
	private Map components;
	private ComponentValidator componentValidator;
	
	protected synchronized ComponentValidator getComponentValidator() {
		if (componentValidator == null) {
			SimpleComponentValidator validator = new SimpleComponentValidator();
			//validator.setComponentAccessor(new MapPropertyComponentAccessor());
		}		
	    return componentValidator;
    }
	
	protected synchronized void setComponentValidator(
            ComponentValidator componentValidator) {
    	this.componentValidator = componentValidator;
    }

	protected Class[] getDestinationClassesImpl() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	protected Class[] getSourceClassesImpl() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	public Map getComponents() {
    	return components;
    }

	public void setComponents(Map components) {
    	this.components = components;
    }

}
