/*
 * Copyright 2004-2005, 2008, 2010 the original author or authors.
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

import java.util.Arrays;
import java.util.Locale;
import java.util.Set;

import net.sf.morph2.transform.Converter;
import net.sf.morph2.util.ClassUtils;
import net.sf.morph2.util.ContainerUtils;
import net.sf.morph2.util.MutableInteger;
import net.sf.morph2.util.TransformerUtils;

/**.
 * <p>
 * Creates a String representation of an object that is useful for debugging.
 * This class is threadsafe and will not enter an infinite loop, even if
 * displaying the information in a cyclic graph of objects.
 * </p>
 * 
 * @author Matt Sgarlata
 * @since Feb 15, 2005
 */
public class ObjectToPrettyTextConverter extends BaseToPrettyTextConverter {
	/** Default types using <code>toString()</code> */
	public static final Class[] DEFAULT_TYPES_USING_TO_STRING = new Class[] {
			String.class, Long.class, Integer.class, Short.class, Character.class, Byte.class,
			Double.class, Float.class, Boolean.class, Long.TYPE, Integer.TYPE, Short.TYPE,
			Character.TYPE, Byte.TYPE, Double.TYPE, Float.TYPE, Boolean.TYPE, StringBuffer.class };

	/** Default levels */
	public static final int DEFAULT_LEVELS = 1;

	private static ThreadLocal currentLevelThreadLocal = new ThreadLocal();

	private int levels = DEFAULT_LEVELS;
	private Converter containerToPrettyTextConverter;
	private Converter beanToPrettyTextConverter;

	private Set typesUsingToString;

	/**
	 * Create a new ObjectToPrettyTextConverter.
	 */
	public ObjectToPrettyTextConverter() {
		super();
		setTypesUsingToString(DEFAULT_TYPES_USING_TO_STRING);
	}

	/**
	 * {@inheritDoc}
	 */
	protected Object convertImpl(Class destinationClass, Object source, Locale locale)
			throws Exception {
		MutableInteger currentLevel = (MutableInteger) currentLevelThreadLocal.get();
		if (currentLevel == null) {
			currentLevel = new MutableInteger();
			currentLevelThreadLocal.set(currentLevel);
		}
		else {
			currentLevel.value++;
		}

		try {
			// if we aren't down too many levels in the object graph
			if (currentLevel.value < levels) {
				if (source == null) {
					return "null";
				}
				if (getTypesUsingToStringInternal().contains(source.getClass())) {
					return source.toString();
				}
				if (TransformerUtils.isTransformable(getContainerToPrettyTextConverter(),
						destinationClass, ClassUtils.getClass(source))) {
					return getContainerToPrettyTextConverter().convert(destinationClass, source,
							locale);
				}
				if (TransformerUtils.isTransformable(getBeanToPrettyTextConverter(),
						destinationClass, ClassUtils.getClass(source))) {
					return getBeanToPrettyTextConverter().convert(destinationClass, source, locale);
				}
			}
		} catch (Exception e) {
			if (log.isErrorEnabled()) {
				log.error(
						"Error occurred while attempting to create a formatted text representation of source "
								+ source, e);
			}
		} finally {
			if (currentLevel.value == 0) {
				currentLevelThreadLocal.set(null);
			}
			else {
				currentLevel.value--;
			}
		}
		return getToTextConverter().convert(destinationClass, source, locale);
	}

	/**
	 * {@inheritDoc}
	 */
	protected Class[] getSourceClassesImpl() throws Exception {
		Set candidates = ContainerUtils.createOrderedSet();
		candidates.addAll(Arrays.asList(getContainerToPrettyTextConverter().getSourceClasses()));
		candidates.addAll(Arrays.asList(getBeanToPrettyTextConverter().getSourceClasses()));
		candidates.addAll(Arrays.asList(getToTextConverter().getSourceClasses()));
		return (Class[]) candidates.toArray(new Class[candidates.size()]);
	}

	/**
	 * Get the levels
	 * @return int
	 */
	public int getLevels() {
		return levels;
	}

	/**
	 * Set the levels.
	 * @param levels
	 */
	public void setLevels(int levels) {
		this.levels = levels;
	}

	/**
	 * Get the converter used to convert beans to pretty text.
	 * @return Converter
	 */
	public Converter getBeanToPrettyTextConverter() {
		if (beanToPrettyTextConverter == null) {
			BeanToPrettyTextConverter converter = new BeanToPrettyTextConverter();
			converter.setToTextConverter(this);
			setBeanToPrettyTextConverter(converter);
		}
		return beanToPrettyTextConverter;
	}

	/**
	 * Set the converter used to convert beans to pretty text.
	 * @param beanToTextConverter
	 */
	public void setBeanToPrettyTextConverter(Converter beanToTextConverter) {
		this.beanToPrettyTextConverter = beanToTextConverter;
	}

	/**
	 * Get the converter used to convert containers to pretty text.
	 * @return Converter
	 */
	public Converter getContainerToPrettyTextConverter() {
		if (containerToPrettyTextConverter == null) {
			ContainerToPrettyTextConverter converter = new ContainerToPrettyTextConverter();
			converter.setToTextConverter(this);
			setContainerToPrettyTextConverter(converter);
		}
		return containerToPrettyTextConverter;
	}

	/**
	 * Set the converter used to convert containers to pretty text.
	 * @param containerToTextConverter
	 */
	public void setContainerToPrettyTextConverter(Converter containerToTextConverter) {
		this.containerToPrettyTextConverter = containerToTextConverter;
	}

	/**
	 * Get the types using <code>toString()</code>.
	 * @return Set<Class>
	 */
	protected Set getTypesUsingToStringInternal() {
		// make sure the set is initialized
		if (typesUsingToString == null)
			getTypesUsingToString();
		return typesUsingToString;
	}

	/**
	 * Get the types using <code>toString()</code>.
	 * @return Class[]
	 */
	public Class[] getTypesUsingToString() {
		if (typesUsingToString == null) {
			setTypesUsingToString(DEFAULT_TYPES_USING_TO_STRING);
		}
		return (Class[]) typesUsingToString.toArray(new Class[typesUsingToString.size()]);
	}

	/**
	 * Set the types using <code>toString()</code>.
	 * @param typesUsingToString
	 */
	public void setTypesUsingToString(Class[] typesUsingToString) {
		Set s = ContainerUtils.createOrderedSet();
		s.addAll(Arrays.asList(typesUsingToString));
		this.typesUsingToString = s;
	}
}
