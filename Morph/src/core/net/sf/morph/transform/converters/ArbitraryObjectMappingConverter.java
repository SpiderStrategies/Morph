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
package net.sf.morph.transform.converters;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import net.sf.composite.util.ObjectUtils;
import net.sf.morph.transform.DecoratedConverter;
import net.sf.morph.transform.TransformationException;
import net.sf.morph.transform.transformers.BaseTransformer;
import net.sf.morph.util.BidirectionalMap;
import net.sf.morph.util.ClassUtils;

/**
 * A transformer which transforms defined arbitrary objects to other defined
 * arbitrary objects based on this class' <code>mapping</code> property.  For
 * example, an arbitrary mapping might specify that the Integer 1 be converted
 * to the String "one".  As many mappings as are needed may be specified.  By
 * default, mappings are assumed to be bidirectional.  Continuing with the
 * example above, this means that in addition to the Integer 1 being mapped
 * transformed to the String "one", the String "one" will be transformed to the
 * Integer 1.
 * 
 * @author Matt Sgarlata
 * @since Apr 11, 2005
 */
public class ArbitraryObjectMappingConverter extends BaseTransformer implements DecoratedConverter {
	
	private boolean bidirectional = true;
	private Map mapping;

	/**
	 * {@inheritDoc}
	 */
	protected Object convertImpl(Class destinationClass, Object source,
		Locale locale) throws Exception {
		if (getMapping().containsKey(source)) {
			return getMapping().get(source);
		}
		if (isBidirectional()) {
			BidirectionalMap bidirectionalMap = (BidirectionalMap) mapping;
			if (bidirectionalMap.getReverseMap().containsKey(bidirectionalMap)) {
				return bidirectionalMap.getKey(source);
			}
		}
		throw new TransformationException(
			"No mapping was specified for source object "
				+ ObjectUtils.getObjectDescription(source));
	}

	/**
	 * Get the forward classes, plus backward classes if this converter is bidi. 
	 * @param forwardMappingKeys
	 * @param backwardMappingKeys
	 * @return Class[]
	 * @throws Exception not likely
	 */
	protected Class[] getClasses(Collection forwardMappingKeys, Collection backwardMappingKeys) throws Exception {
		// pre-calculate the maximum size of the result for efficiency
		// this may lead to extra memory costs but will ensure space allocation
		// for the destination set will occur only once
		int maxNumClasses = forwardMappingKeys.size();
		if (isBidirectional()) {
			maxNumClasses += backwardMappingKeys.size();
		}

		Set classes = new HashSet(maxNumClasses);
		addContainedClasses(classes, forwardMappingKeys);

		if (isBidirectional()) {
			addContainedClasses(classes, backwardMappingKeys);
		}

		return (Class[]) classes.toArray(new Class[classes.size()]); 
	}

	/**
	 * Add the classes of the contents of <code>objects</code> to <code>classes</code>.
	 * @param classes
	 * @param objects
	 */
	protected void addContainedClasses(Set classes, Collection objects) {
		if (objects != null) {
			for (Iterator i = objects.iterator(); i.hasNext(); ) {
				classes.add(ClassUtils.getClass(i.next()));
			}
		}
	}

	/**
	 * {@inheritDoc}
	 */
	protected Class[] getSourceClassesImpl() throws Exception {
		if (ObjectUtils.isEmpty(mapping)) {
			throw new IllegalStateException("The mapping property of this converter must be set");
		}
		return getClasses(mapping.keySet(), mapping.values());
	}

	/**
	 * {@inheritDoc}
	 */
	protected Class[] getDestinationClassesImpl() throws Exception {
		if (ObjectUtils.isEmpty(mapping)) {
			throw new IllegalStateException("The mapping property of this converter must be set");
		}
		return getClasses(mapping.values(), mapping.keySet());
	}

	/**
	 * {@inheritDoc}
	 */
	protected boolean isWrappingRuntimeExceptions() {
	    return true;
    }

	/**
	 * Learn whether this ArbitraryObjectMappingConverter is bidirectional.
	 * @return boolean
	 */
	public boolean isBidirectional() {
		return bidirectional;
	}

	/**
	 * Set whether this ArbitraryObjectMappingConverter is bidirectional.
	 * @param bidirectional
	 */
	public void setBidirectional(boolean bidirectional) {
		setInitialized(false);
		this.bidirectional = bidirectional;
	}

	/**
	 * Get the object mapping.
	 * @return Map
	 */
	public Map getMapping() {
		return mapping;
	}

	/**
	 * Set the mapping.
	 * 
	 * @param mapping the mapping
	 * @throws IllegalArgumentException
	 *             if <code>bidirectional</code> is <code>true</code> and
	 *             there is some value which is mapped to more than one key. In
	 *             this case, it is impossible to construct a bidirectional map
	 *             because there is no way to determine to which key the value
	 *             is mapped.
	 */
	public void setMapping(Map mapping) {
		setInitialized(false);
		if (isBidirectional() && !(mapping instanceof BidirectionalMap)) {
			this.mapping = new BidirectionalMap(mapping);
		}
		else {
			this.mapping = mapping;			
		}
	}

	/**
	 * @return Map
	 * @deprecated
	 */
	public Map getVisitedSourceToDestinationMap() {
		return getMapping();
	}

	/**
	 * @param visitedSourceToDestinationMap
	 * @deprecated
	 */
	public void setVisitedSourceToDestinationMap(Map visitedSourceToDestinationMap) {
		throw new UnsupportedOperationException();
	}

}
