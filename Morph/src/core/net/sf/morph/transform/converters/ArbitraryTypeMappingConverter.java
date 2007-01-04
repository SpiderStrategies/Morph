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
package net.sf.morph.transform.converters;

import java.util.Iterator;
import java.util.Locale;
import java.util.Map;

import net.sf.composite.util.ObjectUtils;
import net.sf.morph.transform.Converter;
import net.sf.morph.transform.DecoratedConverter;
import net.sf.morph.transform.TransformationException;
import net.sf.morph.transform.transformers.BaseTransformer;

/**
 * A transformer which transforms objects which are instances of a type into
 * other defined objects based on this class' <code>mapping</code> property.
 * The mapping property has types (instances of {@link java.lang.Class}) as keys and
 * objects as values. 
 * 
 * @author Matthew Sgarlata
 * @since June 15, 2005
 */
public class ArbitraryTypeMappingConverter extends BaseTransformer implements Converter, DecoratedConverter {

	private static final Class[] DESTINATION_CLASSES = new Class[] { Object.class };
	
	private Map mapping;
	
	protected Object convertImpl(Class destinationClass, Object source,
			Locale locale) throws Exception {
		Iterator iterator = getMapping().keySet().iterator();
		while (iterator.hasNext()) {
			Class type = (Class) iterator.next();
			if (source == null) {
				if (type == null) {
					return getMapping().get(type);
				}
			}
			else {
				if (type != null && type.isAssignableFrom(source.getClass())) {
					return getMapping().get(type);
				}
			}
		}
		
		throw new TransformationException(destinationClass, source);
	}

	protected Class[] getSourceClassesImpl() throws Exception {
		if (ObjectUtils.isEmpty(mapping)) {
			throw new IllegalStateException("The mapping property of this converter must be set");
		}
		return (Class[]) getMapping().keySet().toArray(new Class[getMapping().keySet().size()]);
	}

	protected Class[] getDestinationClassesImpl() throws Exception {
		return DESTINATION_CLASSES;
	}

	/**
	 * Returns the mapping of types to objects
	 * @return Returns the mapping.
	 */
	public Map getMapping() {
		return mapping;
	}
	/**
	 * @param mapping The mapping to set.
	 */
	public void setMapping(Map mapping) {
		this.mapping = mapping;
	}
}
