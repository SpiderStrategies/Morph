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

import java.util.Collection;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;

import net.sf.composite.util.ObjectUtils;
import net.sf.morph.Defaults;
import net.sf.morph.lang.DecoratedLanguage;
import net.sf.morph.lang.Language;
import net.sf.morph.lang.languages.LanguageDecorator;
import net.sf.morph.lang.languages.SimpleLanguage;
import net.sf.morph.transform.Converter;
import net.sf.morph.transform.DecoratedConverter;
import net.sf.morph.transform.DecoratedCopier;
import net.sf.morph.transform.NodeCopier;
import net.sf.morph.transform.TransformationException;
import net.sf.morph.transform.Transformer;
import net.sf.morph.transform.transformers.BaseTransformer;
import net.sf.morph.util.TransformerUtils;

/**
 * Maps property expressions between objects using a Morph Language.
 *
 * @author Matt Benson
 */
public class PropertyExpressionMappingCopier extends BaseTransformer implements
		DecoratedConverter, DecoratedCopier, NodeCopier {
	private static final Class[] SOURCE_AND_DEST_CLASSES = new Class[] { Object.class };

	private Map mapping;

	private Language language;

	/**
	 * Get the language of this PropertyExpressionMappingCopier.
	 * @return the language
	 */
	public synchronized Language getLanguage() {
		if (language == null) {
			SimpleLanguage lang = Defaults.createLanguage();
			lang.setConverter((Converter) getNestedTransformer());
			setLanguage(lang);
		}
		return language;
	}

	/**
	 * Set the language of this PropertyExpressionMappingCopier.
	 * @param language the language to set
	 */
	public synchronized void setLanguage(Language language) {
		this.language = language instanceof DecoratedLanguage ? language
				: new LanguageDecorator(language);
	}

	/* (non-Javadoc)
	 * @see net.sf.morph.transform.transformers.BaseTransformer#initializeImpl()
	 */
	protected void initializeImpl() throws Exception {
		super.initializeImpl();
		if (ObjectUtils.isEmpty(mapping)) {
			throw new TransformationException(
					"You must specify which properties you would like the "
							+ getClass().getName()
							+ " to copy by setting the mapping property");
		}
		ensureOnlyStrings(mapping.keySet());
		ensureOnlyStrings(mapping.values());
	}

	private void ensureOnlyStrings(Collection collection) {
		for (Iterator i = collection.iterator(); i.hasNext();) {
			Object value = i.next();
			if (!(value instanceof String)) {
				throw new TransformationException(
						"An invalid mapping element was specified: "
								+ ObjectUtils.getObjectDescription(value)
								+ ".  Mapping elements must be Strings");
			}
		}
	}

	/* (non-Javadoc)
	 * @see net.sf.morph.transform.transformers.BaseTransformer#copyImpl(java.lang.Object, java.lang.Object, java.util.Locale, java.lang.Integer)
	 */
	protected void copyImpl(Object destination, Object source, Locale locale,
			Integer preferredTransformationType) throws Exception {
		for (Iterator it = getMapping().entrySet().iterator(); it.hasNext();) {
			Map.Entry e = (Map.Entry) it.next();
			copyProperty((String) e.getKey(), source, (String) e.getValue(), destination,
					locale, preferredTransformationType);
		}
	}

	protected void copyProperty(String sourceProperty, Object source,
			String destinationProperty, Object destination, Locale locale,
			Integer preferredTransformationType) {
		if (getLog().isTraceEnabled()) {
			getLog().trace(
					"Copying property '" + sourceProperty + "' of "
							+ ObjectUtils.getObjectDescription(source) + " to property '"
							+ destinationProperty + "' of "
							+ ObjectUtils.getObjectDescription(destination));
		}

		// determine the destination type
		Class destinationType = getLanguage().getType(destination, destinationProperty);
		// determine the value of the source property
		Object sourceValue = getLanguage().get(source, sourceProperty);
		// determine the current value of the destination property, if any
		Object destinationValue = getLanguage().get(destination, destinationProperty);

		// choose a transformer to use
		Transformer transformer = getNestedTransformer();

		// determine the new value that will be set on the destination
		Object newDestinationValue = TransformerUtils.transform(transformer,
				destinationType, destinationValue, sourceValue, locale,
				preferredTransformationType);
		// set the transformed value on the destination
		getLanguage().set(destination, destinationProperty, newDestinationValue);

		if (getLog().isTraceEnabled()) {
			getLog().trace(
					"Done copying property '" + sourceProperty + "' to property '"
							+ destinationProperty + "'.  sourceValue was "
							+ ObjectUtils.getObjectDescription(sourceValue)
							+ " and destinationValue was "
							+ ObjectUtils.getObjectDescription(destinationValue));
		}
	}

	/* (non-Javadoc)
	 * @see net.sf.morph.transform.transformers.BaseTransformer#getDestinationClassesImpl()
	 */
	protected Class[] getDestinationClassesImpl() throws Exception {
		return SOURCE_AND_DEST_CLASSES;
	}

	/* (non-Javadoc)
	 * @see net.sf.morph.transform.transformers.BaseTransformer#getSourceClassesImpl()
	 */
	protected Class[] getSourceClassesImpl() throws Exception {
		return SOURCE_AND_DEST_CLASSES;
	}

	/**
	 * Get the mapping of this PropertyExpressionMappingCopier.
	 * @return the mapping
	 */
	public synchronized Map getMapping() {
		return mapping;
	}

	/**
	 * Set the mapping of this PropertyExpressionMappingCopier.
	 * @param mapping the mapping to set
	 */
	public synchronized void setMapping(Map mapping) {
		this.mapping = mapping;
	}

	/* (non-Javadoc)
	 * @see net.sf.morph.transform.transformers.BaseTransformer#setSourceClasses(java.lang.Class[])
	 */
	public synchronized void setSourceClasses(Class[] sourceClasses) {
		super.setSourceClasses(sourceClasses);
	}

	/* (non-Javadoc)
	 * @see net.sf.morph.transform.transformers.BaseTransformer#setDestinationClasses(java.lang.Class[])
	 */
	public synchronized void setDestinationClasses(Class[] destinationClasses) {
		super.setDestinationClasses(destinationClasses);
	}

	/* (non-Javadoc)
	 * @see net.sf.morph.transform.transformers.BaseTransformer#createReusableSource(java.lang.Class, java.lang.Object)
	 */
	public Object createReusableSource(Class destinationClass, Object source) {
		return super.createReusableSource(destinationClass, source);
	}

	/* (non-Javadoc)
	 * @see net.sf.morph.transform.transformers.BaseTransformer#setNestedTransformer(net.sf.morph.transform.Transformer)
	 */
	public void setNestedTransformer(Transformer nestedTransformer) {
		super.setNestedTransformer(nestedTransformer);
	}

	/* (non-Javadoc)
	 * @see net.sf.morph.transform.transformers.BaseTransformer#getNestedTransformer()
	 */
	public Transformer getNestedTransformer() {
		return super.getNestedTransformer();
	}
}