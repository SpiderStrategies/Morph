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
package net.sf.morph2.transform.converters;

import java.util.Iterator;
import java.util.Locale;

import net.sf.morph2.Defaults;
import net.sf.morph2.transform.Converter;
import net.sf.morph2.transform.DecoratedConverter;
import net.sf.morph2.transform.transformers.BaseReflectorTransformer;

/**
 * Converts an object to a traverser type (an Iterator or an Enumeration).  If
 * the source object is reflectable as a container object by the <code>reflector</code> of this
 * 
 * @author Matt Sgarlata
 * @since Dec 18, 2004
 */
public class ContainerToTraverserConverter extends BaseReflectorTransformer implements DecoratedConverter {

	private static final Class[] SOURCE_TYPES = new Class[] {
		Object.class
	};

	private Converter traverserConverter;

	/**
	 * {@inheritDoc}
	 */
	protected Object convertImpl(Class destinationClass, Object source,
		Locale locale) throws Exception {

		Iterator iterator = getContainerReflector().getIterator(source);
		return getTraverserConverter().convert(destinationClass, iterator, locale);
	}

	/**
	 * {@inheritDoc}
	 */
	protected Class[] getSourceClassesImpl() throws Exception {
		return SOURCE_TYPES;
	}

	/**
	 * {@inheritDoc}
	 */
	protected Class[] getDestinationClassesImpl() throws Exception {
		return getTraverserConverter().getDestinationClasses();
	}

	/**
	 * Get the traverser converter used by this ContainerToTraverserConverter.
	 * @return Converter
	 */
	public Converter getTraverserConverter() {
		if (traverserConverter == null) {
			setTraverserConverter(Defaults.createConverter());
		}
		return traverserConverter;
	}

	/**
	 * Set the traverser converter used by this ContainerToTraverserConverter.
	 * @param traverserConverter
	 */
	public void setTraverserConverter(Converter traverserConverter) {
		this.traverserConverter = traverserConverter;
	}
}
