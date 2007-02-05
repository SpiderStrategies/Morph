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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import net.sf.composite.util.ObjectUtils;
import net.sf.morph.transform.TransformationException;
import net.sf.morph.util.StringUtils;

/**
 * <p>Copies the properties specified by the <code>propertiesToCopy</code>
 * property of this class from the source to the destination. If
 * <code>propertiesToCopy</code> is not specified, all of the source
 * properties will be copied to the destination.
 *
 * <p>Copies properties that have the same name from the source to the destination.
 * By default, if a property found on the source is missing on the destination,
 * an exception will <em>not</em> be thrown and copying will continue.  If you
 * want to ensure all properties from the source are copied to the destination,
 * set the <em>errorOnMissingProperty</em> property of this class to
 * <code>true</code>.
 *
 * @author Matt Sgarlata
 * @author Alexander Volanis
 * @since Oct 31, 2004
 */
public class PropertyNameMatchingCopier extends BasePropertyNameCopier {

	private Set propertiesToCopy = new HashSet();
	private Set propertiesToIgnore = new HashSet();

	public PropertyNameMatchingCopier() {
		super();
		setErrorOnMissingProperty(false);
	}

	public PropertyNameMatchingCopier(boolean errorOnMissingProperty) {
		super(errorOnMissingProperty);
	}

	public void copyImpl(Object destination, Object source, Locale locale,
			Integer preferredTransformationType) throws Exception {
		String[] properties = evaluateIncludedProperties(source);
		if (log.isInfoEnabled()) {
			if (ObjectUtils.isEmpty(properties)) {
				getLog().info("No properties available for copying");
			}
			else {
				getLog()
						.info("Copying properties " + StringUtils.englishJoin(properties));
			}
		}

		List unreadableProperties = null;
		List unwriteableProperties = null;
		if (getLog().isTraceEnabled()) {
			unreadableProperties = new ArrayList();
			unwriteableProperties = new ArrayList();
		}
		for (int i = 0; i < properties.length; i++) {
			String property = properties[i];
			boolean sourceReadable = getBeanReflector().isReadable(source, property);
			boolean destinationWriteable = getBeanReflector().isWriteable(destination,
					property);

			if (sourceReadable && destinationWriteable) {
				copyProperty(property, source, property, destination, locale,
						preferredTransformationType);
			}
			else {
				// this check isn't necessary, but is included for performance
				// reasons so that we don't construct these strings unnecessarily
				if (isErrorOnMissingProperty() || getLog().isTraceEnabled()) {
					if (!sourceReadable) {
						unreadableProperties.add(property);
					}
					if (!destinationWriteable) {
						unwriteableProperties.add(property);
					}
				}
			}
		}

		if (isErrorOnMissingProperty() || getLog().isTraceEnabled()) {
			int skippedPropertiesSize = unreadableProperties.size()
					+ unwriteableProperties.size();
			List skippedProperties = new ArrayList(skippedPropertiesSize);
			skippedProperties.addAll(unreadableProperties);
			skippedProperties.addAll(unwriteableProperties);

			String message = "The following properties were not copied "
					+ "because they were not readable on the source object, not "
					+ "writeable on the destination object or both: "
					+ StringUtils.englishJoin(skippedProperties)
					+ ".  The properties that were not readable are: "
					+ StringUtils.englishJoin(unreadableProperties)
					+ ".  The properties that were not writeable are: "
					+ StringUtils.englishJoin(unwriteableProperties);
			if (isErrorOnMissingProperty()) {
				throw new TransformationException(message);
			}
			// the message is already constructed, so no need for
			// another if getLog().isTraceEnabled call
			if (!skippedProperties.isEmpty()) {
				getLog().trace(message);
			}
		}
	}

	/**
	 * Get the properties to copy.
	 * @return String[]
	 */
	public synchronized String[] getPropertiesToCopy() {
		return (String[]) propertiesToCopy.toArray(new String[propertiesToCopy.size()]);
	}

	/**
	 * Set the properties to copy.
	 * @param propertiesToCopy String[]
	 */
	public synchronized void setPropertiesToCopy(String[] propertiesToCopy) {
		this.propertiesToCopy.clear();
		this.propertiesToCopy.addAll(Arrays.asList(propertiesToCopy));
	}

	/**
	 * Add a property to copy.
	 * @param propertyName
	 */
	public synchronized void addPropertyToCopy(String propertyName) {
		propertiesToCopy.add(propertyName);
	}

	/**
	 * Get the properties to ignore.
	 * @return String[]
	 */
	public synchronized String[] getPropertiesToIgnore() {
		return (String[]) propertiesToIgnore
				.toArray(new String[propertiesToIgnore.size()]);
	}

	/**
	 * Set the properties to ignore.
	 * @param propertiesToIgnore String[]
	 */
	public synchronized void setPropertiesToIgnore(String[] propertiesToIgnore) {
		this.propertiesToIgnore.clear();
		this.propertiesToIgnore.addAll(Arrays.asList(propertiesToIgnore));
	}

	/**
	 * Add a property to ignore.
	 * @param propertyName
	 */
	public synchronized void addPropertyToIgnore(String propertyName) {
		propertiesToIgnore.add(propertyName);
	}

	private String[] evaluateIncludedProperties(Object source) {
		Set result = new HashSet(propertiesToCopy);
		result.retainAll(propertiesToIgnore);
		if (!result.isEmpty()) {
			throw new IllegalStateException("Overlapping included/ignored properties: "
					+ result);
		}
		if (ObjectUtils.isEmpty(propertiesToCopy)) {
			result.addAll(Arrays.asList(getBeanReflector().getPropertyNames(source)));
			result.removeAll(propertiesToIgnore);
		}
		else {
			result = propertiesToCopy;
		}
		return (String[]) result.toArray(new String[result.size()]);
	}
}