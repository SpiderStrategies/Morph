/*
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
package net.sf.morph2.reflect.reflectors;

import net.sf.composite.Composite;
import net.sf.composite.Defaults;
import net.sf.composite.SimpleComposite;
import net.sf.composite.SpecializableComposite;
import net.sf.composite.StrictlyTypedComposite;
import net.sf.composite.ValidatableComposite;
import net.sf.composite.specialize.SpecializationException;
import net.sf.composite.specialize.Specializer;
import net.sf.composite.specialize.specializers.CachingSpecializerProxy;
import net.sf.composite.specialize.specializers.CloningSpecializer;
import net.sf.composite.util.CompositeUtils;
import net.sf.composite.validate.ComponentValidationException;
import net.sf.composite.validate.ComponentValidator;
import net.sf.morph2.reflect.Reflector;

/**
 * Base Reflector composed of other Reflectors.
 * @author Matt Sgarlata
 * @since Oct 25, 2007
 */
public abstract class BaseCompositeReflector extends BaseReflector {

	private Object[] components;
	private Specializer specializer;
	private ComponentValidator componentValidator;

	/**
	 * Learn whether this {@link BaseCompositeReflector} can be specialized to <code>type</code>.
	 * @param type
	 * @return boolean
	 * @throws SpecializationException
	 * @see {@link SpecializableComposite#isSpecializable(Class)}
	 */
	public boolean isSpecializable(Class type) throws SpecializationException {
		initialize();
		return getSpecializer().isSpecializable(this, type);
	}

	/**
	 * Specialize this {@link BaseCompositeReflector} as <code>type</code>.
	 * @param type
	 * @return type
	 * @see {@link SpecializableComposite#specialize(Class)}
	 */
	public Object specialize(Class type) {
		initialize();
		return getSpecializer().specialize(this, type);
	}
	
	/**
	 * Get the component type of this {@link Composite}.
	 * @return Class
	 * @see {@link StrictlyTypedComposite#getComponentType()}
	 */
	public Class getComponentType() {
		return Reflector.class;
	}

	/**
	 * {@inheritDoc}
	 */
	protected boolean isPerformingLogging() {
		// let the delegate do the logging
		return false;
	}

	/**
	 * {@inheritDoc}
	 */
	public Object clone() throws CloneNotSupportedException {
		return super.clone();
	}

	/**
	 * {@inheritDoc}
	 */
	public String toString() {
		return CompositeUtils.toString(this);
	}

	/**
	 * Get the component array of this {@link BaseCompositeReflector}.
	 * @return Object[]
	 * @see {@link SimpleComposite#getComponents()}
	 */
	public Object[] getComponents() {
		return components;
	}

	/**
	 * Set the components of this {@link BaseCompositeReflector}.
	 * @param components
	 * @see {@link SimpleComposite#setComponents(Object[])}
	 */
	public void setComponents(Object[] components) {
		setInitialized(false);
		this.components = components;
	}

	/**
	 * Get the specializer used by this {@link BaseCompositeReflector}.
	 * @return Specializer
	 */
	public synchronized Specializer getSpecializer() {
		if (specializer == null) {
			specializer = new CachingSpecializerProxy(new CloningSpecializer());
		}
		return specializer;
	}

	/**
	 * Set this {@link BaseCompositeReflector}'s {@link Specializer}.
	 * @param specializer
	 */
	public void setSpecializer(Specializer specializer) {
		this.specializer = specializer;
	}

	/**
	 * Get this {@link BaseCompositeReflector}'s {@link ComponentValidator}.
	 * @return {@link ComponentValidator}
	 */
	public synchronized ComponentValidator getComponentValidator() {
		if (componentValidator == null) {
			componentValidator = Defaults.createComponentValidator();
		}
		return componentValidator;
	}

	/**
	 * Set this {@link BaseCompositeReflector}'s {@link ComponentValidator}.
	 * @param validator
	 */
	public void setComponentValidator(ComponentValidator validator) {
		this.componentValidator = validator;
	}

	/**
	 * Validate this {@link BaseCompositeReflector}.
	 * @throws ComponentValidationException
	 * @see {@link ValidatableComposite#validate()}
	 */
	public void validate() throws ComponentValidationException {
		getComponentValidator().validate(this);
	};
}
