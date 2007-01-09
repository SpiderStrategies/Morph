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

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.Map.Entry;

import net.sf.composite.util.ObjectUtils;
import net.sf.morph.reflect.BeanReflector;
import net.sf.morph.reflect.GrowableContainerReflector;
import net.sf.morph.reflect.InstantiatingReflector;
import net.sf.morph.reflect.ReflectionException;
import net.sf.morph.reflect.SizableReflector;
import net.sf.morph.transform.copiers.ContainerCopier;
import net.sf.morph.util.ContainerUtils;
import net.sf.morph.util.StringUtils;

/**
 * Reflector for Maps that allows a map to be treated both as a container and as
 * a bean.
 * 
 * @author Matt Sgarlata
 * @since Nov 27, 2004
 */
public class MapReflector extends BaseReflector implements InstantiatingReflector, SizableReflector, GrowableContainerReflector,
	BeanReflector {
	
	public static final String IMPLICIT_PROPERTY_ENTRIES = "entries";
	public static final String IMPLICIT_PROPERTY_KEYS = "keys";
	public static final String IMPLICIT_PROPERTY_VALUES = "values";
	
	/**
	 * Indicates that the <code>values</code> of the source map should be
	 * copied to the destination container object.
	 * 
	 * @see Map#values()
	 */
	public static final String EXTRACT_VALUES = "EXTRACT_VALUES";

	/**
	 * Indicates that the <code>entrySet</code> of the source map should be
	 * copied to the destination container object. For example, if the
	 * destination container object is a List, then each Map.Entry in the source
	 * map will be copied to the destination List
	 * 
	 * @see Map#entrySet()
	 */
	public static final String EXTRACT_ENTRIES = "EXTRACT_ENTRIES";

	/**
	 * Indicates that the <code>keySet</code> of the source map should be
	 * copied to the destination container object. For example, if the
	 * destination container object is a List, then each key in the source map
	 * will be copied to the destination List.
	 * 
	 * @see Map#keySet()
	 */
	public static final String EXTRACT_KEYS = "EXTRACT_KEYS";

	/**
	 * The default treatment for Maps (which is to extract the values in the
	 * Map).
	 * 
	 * @see ContainerCopier#EXTRACT_VALUES
	 */
	public static final String DEFAULT_MAP_TREATMENT = EXTRACT_VALUES;
	
	private static final Class[] REFLECTABLE_TYPES = new Class[] {
		Map.class
	};
	
	/**
	 * All of the allowed map treatments.
	 */
	protected static String[] MAP_TREATMENTS = new String[] {
		EXTRACT_VALUES, EXTRACT_ENTRIES, EXTRACT_KEYS
	};
	
	/**
	 * The map treatment this copier is using.
	 */
	private String mapTreatment;
	
	public MapReflector() {
		setMapTreatment(DEFAULT_MAP_TREATMENT);
	}
	
	protected Map getMap(Object container) {
		return (Map) container;
	}
	
// container 	
	
	protected int getSizeImpl(Object container) throws Exception {
		return getMap(container).size();
	}

	protected Class getContainedTypeImpl(Class clazz) throws Exception {
		// TODO JDK 1.5 support
		return Object.class;
	}

	public Class[] getReflectableClassesImpl() {
		return REFLECTABLE_TYPES;
	}

	protected Object newInstanceImpl(Class interfaceClass) throws Exception {
		if (interfaceClass == Map.class) {
			return new HashMap();
		}
		if (interfaceClass == SortedMap.class) {
			return new TreeMap();
		}
		return super.newInstanceImpl(interfaceClass);
	}
	
	protected boolean addImpl(Object container, Object value) throws Exception {
		if (isExtractEntries()) {
			if (!(value instanceof Map.Entry)) {
				throw new IllegalArgumentException(ObjectUtils.getObjectDescription(value) + " cannot be added to the Map because it is not of type java.util.Map.Entry");
			}
			Entry entry = (Map.Entry) value;
			Object returnVal = getMap(container).put(entry.getKey(), entry.getValue());
			return ObjectUtils.equals(value, returnVal);
		}
		if (isExtractKeys()) {
			Object returnVal = getMap(container).put(value, null);
			return ObjectUtils.equals(value, returnVal);
		}
		if (isExtractValues()) {
			if (log.isWarnEnabled()) {
				log.warn("The " + ObjectUtils.getObjectDescription(this) + " is set to " + getMapTreatment() + " so " + ObjectUtils.getObjectDescription(value) + " will be added to the Map with a null key");
			}
			Object returnVal = getMap(container).put(null, value);
			return ObjectUtils.equals(value, returnVal);
		}
		throw new ReflectionException("Unknown map treatment '" + getMapTreatment() + "'");
	}
	
	protected Iterator getIteratorImpl(Object container) throws Exception {
		if (isExtractEntries()) {
			return getMap(container).entrySet().iterator();
		}
		if (isExtractKeys()) {
			return getMap(container).keySet().iterator();
		}
		if (isExtractValues()) {
			return getMap(container).values().iterator();
		}
		// this shouldn't ever happen
		throw new ReflectionException("Invalid mapTreatment: " + getMapTreatment());
	}
	
// bean
	
	protected String[] getPropertyNamesImpl(Object bean) throws Exception {
//		 the getPropertyNames method used to return implicit properties, but it
//		 doesn't anymore			
//		Set keys = new HashSet(getMap(bean).keySet());
//		keys.add(IMPLICIT_PROPERTY_KEYS);
//		keys.add(IMPLICIT_PROPERTY_VALUES);
//		keys.add(IMPLICIT_PROPERTY_ENTRIES);
//		return (String[]) keys.toArray(new String[keys.size()]);
		Set keys = new HashSet(getMap(bean).keySet());
		return (String[]) keys.toArray(new String[keys.size()]);
	}
	protected Class getTypeImpl(Object bean, String propertyName) throws Exception {
		return Object.class;
	}
	protected boolean isReadableImpl(Object bean, String propertyName)
		throws Exception {
		return true;
//		return
//			getMap(bean).containsKey(propertyName) ||
//			IMPLICIT_PROPERTY_KEYS.equals(propertyName) ||
//			IMPLICIT_PROPERTY_ENTRIES.equals(propertyName) ||
//			IMPLICIT_PROPERTY_VALUES.equals(propertyName);
	}
	protected boolean isWriteableImpl(Object bean, String propertyName)
		throws Exception {
		return true;
	}
	protected Object getImpl(Object bean, String propertyName) throws Exception {
		Object value = getMap(bean).get(propertyName);
		if (propertyName.equals(IMPLICIT_PROPERTY_VALUES) && value == null) {
			return getMap(bean).values();
		}
		if (propertyName.equals(IMPLICIT_PROPERTY_KEYS) && value == null) {
			return getMap(bean).keySet();
		}
		if (propertyName.equals(IMPLICIT_PROPERTY_ENTRIES) && value == null) {
			return getMap(bean).entrySet();
		}
		return value;
	}
	protected void setImpl(Object bean, String propertyName, Object value)
		throws Exception {
		getMap(bean).put(propertyName, value);
	}

	public String getMapTreatment() {
		return mapTreatment;
	}
	/**
	 * Sets how maps are treated by this reflector
	 * @param mapTreatment how maps are treated by this reflector
	 * @throws ReflectionException if an invalid map treatment is specified
	 */
	public void setMapTreatment(String mapTreatment) throws
		ReflectionException {
		if (!ContainerUtils.contains(MAP_TREATMENTS, mapTreatment)) {
			throw new ReflectionException("Invalid value for the mapTreatment attribute.  Valid values are: " + StringUtils.englishJoin(MAP_TREATMENTS));
		}
		this.mapTreatment = mapTreatment;
	}
	
	public boolean isExtractEntries() {
		return EXTRACT_KEYS.equals(getMapTreatment());
	}
	
	public boolean isExtractKeys() {
		return EXTRACT_ENTRIES.equals(getMapTreatment());
	}
	
	public boolean isExtractValues() {
		return EXTRACT_VALUES.equals(getMapTreatment());
	}

}