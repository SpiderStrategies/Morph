/*
 * Copyright 2004-2005, 2008 the original author or authors.
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
package net.sf.morph2.transform.copiers;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import net.sf.composite.util.ObjectUtils;
import net.sf.morph2.transform.TransformationException;
import net.sf.morph2.util.ContainerUtils;

/**
 * Copies properties from the source to the destination based on a mapping of
 * property names.
 * 
 * <p>
 * By default, property name mappings are considered bidirectional, so if you
 * call <code>addMapping("foo", "bar")</code>, the copy method will copy the
 * value of <code>foo</code> to <code>bar</code> and will also copy
 * <code>bar</code> to <code>foo</code>. Mappings specified by the
 * <code>setMapping</code> method are considered bidirectional as well. If
 * this behavior is not desired, just set the <code>bidirectional</code>
 * property to false.
 * 
 * <p>
 * If a property in the <code>mapping</code> cannot be copied, by default a
 * <code>TransformationException</code> will be thrown. To turn off this
 * behavior, set <code>errorOnMissingProperty</code> to <code>false</code>.
 * 
 * @author Matt Sgarlata
 * @author Alexander Volanis
 * @since Jan 25, 2005
 */
public class PropertyNameMappingCopier extends BasePropertyNameCopier {

	private Map mapping;
	private boolean bidirectional = true;

	/**
	 * Create a new PropertyNameMappingCopier.
	 */
	public PropertyNameMappingCopier() {
		super();
		setErrorOnMissingProperty(true);
	}

	/**
	 * Create a new PropertyNameMappingCopier.
	 * @param errorOnMissingProperty
	 */
	public PropertyNameMappingCopier(boolean errorOnMissingProperty) {
		super(errorOnMissingProperty);
	}

	/**
	 * {@inheritDoc}
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
		if (bidirectional) {
			// make sure there are no duplicate mappings
			Set propertiesWithMappings = ContainerUtils.createOrderedSet();
			Iterator iterator = mapping.values().iterator();
			while (iterator.hasNext()) {
				String propertyName = (String) iterator.next();
				if (propertiesWithMappings.contains(propertyName)) {
					throw new TransformationException(
							"A duplicate mapping was detected for property '"
									+ propertyName
									+ "'.  Please remote the duplicate mapping or set bidirectional to false.");
				}
				propertiesWithMappings.add(propertyName);
			}
		}
	}

	/**
	 * Ensure all collection entries are Strings.
	 * @param collection
	 */
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

	/**
	 * {@inheritDoc}
	 */
	protected void copyImpl(Object destination, Object source, Locale locale,
			Integer preferredTransformationType) throws TransformationException {

		Entry propertyMapping;
		String sourceProperty;
		String destinationProperty;
		boolean missingProperty;

		for (Iterator i = mapping.entrySet().iterator(); i.hasNext();) {
			propertyMapping = (Entry) i.next();
			sourceProperty = (String) propertyMapping.getKey();
			destinationProperty = (String) propertyMapping.getValue();
			missingProperty = false;

			if (getBeanReflector().isReadable(source, sourceProperty)
					&& getBeanReflector().isWriteable(destination, destinationProperty)) {
				copyProperty(sourceProperty, source, destinationProperty, destination,
						locale, preferredTransformationType);
			}
			// try copying in reverse order for bidirectional processing
			else if (isBidirectional()
					&& getBeanReflector().isReadable(source, destinationProperty)
					&& getBeanReflector().isWriteable(destination, sourceProperty)) {
				copyProperty(destinationProperty, source, sourceProperty, destination,
						locale, preferredTransformationType);
			}
			else {
				missingProperty = true;
			}

			if (missingProperty
					&& (getLog().isWarnEnabled() || isErrorOnMissingProperty())) {
				String message = "Failed to copy property '" + sourceProperty + "' of "
						+ ObjectUtils.getObjectDescription(source) + " to property '"
						+ destinationProperty + "' of  "
						+ ObjectUtils.getObjectDescription(destination);
				if (isErrorOnMissingProperty()) {
					throw new TransformationException(message);
				}
				getLog().warn(message);
			}
		}
	}

	/**
	 * Get the property mapping.
	 * @return Map
	 */
	protected Map getMapping() {
		return mapping;
	}

	/**
	 * Set the property mapping.
	 * @param mapping
	 */
	public void setMapping(Map mapping) {
		this.mapping = mapping;
	}

	/**
	 * Add a single mapping.
	 * @param sourcePropertyName
	 * @param destinationPropertyName
	 */
	public void addMapping(String sourcePropertyName, String destinationPropertyName) {
		if (mapping == null) {
			mapping = new HashMap();
		}
		mapping.put(sourcePropertyName, destinationPropertyName);
	}

	/**
	 * Learn whether this copier is bidirectional.
	 * @return boolean
	 */
	public boolean isBidirectional() {
		return bidirectional;
	}

	/**
	 * Set whether this copier is bidirectional.
	 * @param bidirectional
	 */
	public void setBidirectional(boolean bidirectional) {
		this.bidirectional = bidirectional;
	}
}
