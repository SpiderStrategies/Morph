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
package net.sf.morph.reflect.reflectors;

import net.sf.composite.Defaults;
import net.sf.composite.specialize.SpecializationException;
import net.sf.composite.specialize.Specializer;
import net.sf.composite.specialize.specializers.CachingSpecializerProxy;
import net.sf.composite.specialize.specializers.CloningSpecializer;
import net.sf.composite.util.CompositeUtils;
import net.sf.composite.validate.ComponentValidator;
import net.sf.morph.reflect.Reflector;

/**
 * 
 * @author Matt Sgarlata
 * @since Oct 25, 2007
 */
public abstract class BaseCompositeReflector extends BaseReflector {

	private Object[] components;
	private Specializer specializer;
	private ComponentValidator componentValidator;


	public boolean isSpecializable(Class type) throws SpecializationException {
		initialize();
		return getSpecializer().isSpecializable(this, type);
	}

	public Object specialize(Class type) {
		initialize();
		return getSpecializer().specialize(this, type);
	}
	
	public Class getComponentType() {
		return Reflector.class;
	}

	protected boolean isPerformingLogging() {
		// let the delegate do the logging
		return false;
	}

	public Object clone() throws CloneNotSupportedException {
		return super.clone();
	}

	public String toString() {
		return CompositeUtils.toString(this);
	}

	public Object[] getComponents() {
		return components;
	}

	public void setComponents(Object[] components) {
		setInitialized(false);
		this.components = components;
	}

	public Specializer getSpecializer() {
		if (specializer == null) {
			specializer = new CachingSpecializerProxy(new CloningSpecializer());
		}
		return specializer;
	}

	public void setSpecializer(Specializer specializer) {
		this.specializer = specializer;
	}

	public ComponentValidator getComponentValidator() {
		if (componentValidator == null) {
			componentValidator = Defaults.createComponentValidator();
		}
		return componentValidator;
	}

	public void setComponentValidator(ComponentValidator validator) {
		this.componentValidator = validator;
	}

}
