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
package net.sf.morph.transform.copiers;

import java.util.Locale;
import java.util.Map;

import net.sf.composite.util.ObjectUtils;
import net.sf.morph.transform.Converter;
import net.sf.morph.transform.Copier;
import net.sf.morph.transform.DecoratedConverter;
import net.sf.morph.transform.DecoratedCopier;
import net.sf.morph.transform.NodeCopier;
import net.sf.morph.transform.Transformer;
import net.sf.morph.transform.transformers.BaseReflectorTransformer;
import net.sf.morph.util.ClassUtils;
import net.sf.morph.util.TransformerUtils;

/**
 * Base class for copiers that copy information from one bean to another based
 * on the property names in the source and destination objects.
 * 
 * @author Matt Sgarlata
 * @author Alexander Volanis
 * @since Feb 5, 2005
 */
public abstract class BasePropertyNameCopier extends BaseReflectorTransformer implements Copier, DecoratedCopier, Converter, DecoratedConverter, NodeCopier {
	
	private static final Class[] SOURCE_AND_DESTINATION_TYPES = { Object.class };

	private boolean errorOnMissingProperty = false;
	private Map propertyTransformers;

	public BasePropertyNameCopier() {
		super();
	}

	public BasePropertyNameCopier(boolean errorOnMissingProperty) {
		super();
		this.errorOnMissingProperty = errorOnMissingProperty;
	}
	
	public Object createReusableSource(Class destinationClass, Object source) {
		return super.createReusableSource(destinationClass, source);
	}
	
    /**
	 * Return <code>true</code> if errors should should be thrown when a
	 * property is missing.
	 * 
	 * @return <code>true</code> if errors should be thrown when a property is
	 *         missing or <br>
	 *         <code>false</code>, otherwise.
	 */
	public boolean isErrorOnMissingProperty() {
		return errorOnMissingProperty;
	}

	/**
	 * Set the value of the errorOnMissingProperty flag.
	 * 
	 * @param errorOnMissingProperty
	 *            The value of errorOnMissingProperty to set.
	 */
	public void setErrorOnMissingProperty(boolean isStrict) {
		this.errorOnMissingProperty = isStrict;
	}
	
	protected Transformer chooseTransformer(String sourceProperty, Object source, String destinationProperty, Object destination, Locale locale, Integer preferredTransformationType) {
		if (getPropertyTransformers() != null &&
			getPropertyTransformers().containsKey(sourceProperty)) {
			return (Transformer) getPropertyTransformers().get(sourceProperty);
		}
		else {
			return getNestedTransformer();	
		}		
	}
	
	protected void copyProperty(String sourceProperty, Object source, String destinationProperty, Object destination, Locale locale, Integer preferredTransformationType) {
		if (getLog().isTraceEnabled()) {
			getLog().trace(
				"Copying property '" + sourceProperty + "' of "
					+ ObjectUtils.getObjectDescription(source)
					+ " to property '" + destinationProperty + "' of "
					+ ObjectUtils.getObjectDescription(destination));
		}
		
		// determine the destination type
		Class destinationType = getBeanReflector().getType(
			destination, destinationProperty);
		// determine the value of the source property
		Object sourceValue = getBeanReflector().get(source,
			sourceProperty);
		// determine the current value of the destination property, if any
		Object destinationValue = null;
		if (getBeanReflector().isReadable(destination, destinationProperty)) {
			destinationValue = getBeanReflector().get(destination,
					destinationProperty);	
		}
		if (isImmutable(destinationType)) {
			preferredTransformationType = Converter.TRANSFORMATION_TYPE_CONVERT;
		}
		
		// choose a transformer to use
		Transformer transformer = chooseTransformer(sourceProperty,
			source, destinationProperty, destination, locale,
			preferredTransformationType);
		// determine the new value that will be set on the destination
		Object newDestinationValue = TransformerUtils.transform(transformer,
			destinationType, destinationValue, sourceValue, locale,
			preferredTransformationType);
		// set the transformed value on the destination
		getBeanReflector().set(destination, destinationProperty,
			newDestinationValue);

		if (getLog().isTraceEnabled()) {
			getLog().trace(
				"Done copying property '" + sourceProperty
					+ "' to property '" + destinationProperty
					+ "'.  sourceValue was "
					+ ObjectUtils.getObjectDescription(sourceValue)
					+ " and destinationValue was "
					+ ObjectUtils.getObjectDescription(destinationValue));
		}
	}

	protected boolean isImmutable(Class destinationType) {
	    return ClassUtils.isImmutable(destinationType);
    }

	public Transformer getNestedTransformer() {
		return super.getNestedTransformer();
	}
	public void setNestedTransformer(Transformer transformer) {
		super.setNestedTransformer(transformer);
	}
	
	public Map getPropertyTransformers() {
		return propertyTransformers;
	}
	public void setPropertyTransformers(Map propertyTransformers) {
		this.propertyTransformers = propertyTransformers;
	}
	protected Class[] getSourceClassesImpl() throws Exception {
		return sourceClasses == null ? SOURCE_AND_DESTINATION_TYPES : sourceClasses;
	}
	public void setSourceClasses(Class[] sourceClasses) {
		super.setSourceClasses(sourceClasses);
	}
	protected Class[] getDestinationClassesImpl() throws Exception {
		return destinationClasses == null ? SOURCE_AND_DESTINATION_TYPES : destinationClasses;
	}
	public void setDestinationClasses(Class[] destinationClasses) {
		super.setDestinationClasses(destinationClasses);
	}
}