/*
 * Copyright 2009 the original author or authors.
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

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Set;

import org.apache.commons.lang.ArrayUtils;

import net.sf.morph.Defaults;
import net.sf.morph.transform.DecoratedConverter;
import net.sf.morph.transform.ExplicitTransformer;
import net.sf.morph.transform.TransformationException;
import net.sf.morph.transform.transformers.BaseTransformer;
import net.sf.morph.util.ClassUtils;
import net.sf.morph.util.ContainerUtils;

/**
 * Converts Java5 enums to/from text.
 */
public class TextToEnumConverter extends BaseTransformer implements DecoratedConverter,
		ExplicitTransformer {
	private static final Class ENUM_TYPE;
	private static final Method NAME_METHOD;
	private static final Method VALUE_OF_METHOD;

	static {
		Class enumType = null;
		Method nameMethod = null;
		Method valueOfMethod = null;
		try {
			enumType = Class.forName("java.lang.Enum");
			nameMethod = enumType.getMethod("name", null);
			valueOfMethod = enumType
					.getMethod("valueOf", new Class[] { Class.class, String.class });
		} catch (Exception e) {
		}
		ENUM_TYPE = enumType;
		NAME_METHOD = nameMethod;
		VALUE_OF_METHOD = valueOfMethod;
	}

	private TextConverter textConverter;
	private boolean useToString;
	private boolean emptyStringIsNull = true;

	/**
	 * {@inheritDoc}
	 */
	protected Object convertImpl(Class destinationClass, Object source, java.util.Locale locale)
			throws Exception {
		if (ENUM_TYPE == null) {
			throw new IllegalStateException("Requires Java 5 or greater");
		}
		Throwable cause = null;
		if (ENUM_TYPE.isAssignableFrom(destinationClass)) {
			String s = (String) getTextConverter().convert(String.class, source, locale);
			if ("".equals(s) && isEmptyStringIsNull()) {
				return null;
			}
			try {
				return VALUE_OF_METHOD.invoke(null, new Object[] { destinationClass, s });
			} catch (Throwable t) {
				cause = t;
			}
		}
		if (ENUM_TYPE.isAssignableFrom(ClassUtils.getClass(source))) {
			try {
				String s = isUseToString() ? source.toString() : (String) NAME_METHOD.invoke(source, null);
				return getTextConverter().convert(destinationClass, s, locale);
			} catch (Throwable t) {
				cause = t;
			}
		}
		if (cause != null) {
			throw new TransformationException(destinationClass, source, cause);
		}
		throw new TransformationException(destinationClass, source);
	}

	/**
	 * {@inheritDoc}
	 */
	protected boolean isTransformableImpl(Class destinationType, Class sourceType) throws Exception {
		if (ENUM_TYPE == null) {
			return false;
		}
		if (ENUM_TYPE.isAssignableFrom(destinationType)) {
			return getTextConverter().isTransformable(String.class, sourceType);
		}
		if (ENUM_TYPE.isAssignableFrom(sourceType)) {
			return getTextConverter().isTransformable(destinationType, String.class);
		}
		return false;
	}

	/**
	 * {@inheritDoc}
	 */
	protected Class[] getDestinationClassesImpl() throws Exception {
		if (ENUM_TYPE == null) {
			return ArrayUtils.EMPTY_CLASS_ARRAY;
		}
		Set result = ContainerUtils.createOrderedSet();
		result.add(ENUM_TYPE);
		result.addAll(Arrays.asList(getTextConverter().getDestinationClasses()));
		result.add(null);
		return (Class[]) result.toArray(new Class[result.size()]);
	}

	/**
	 * {@inheritDoc}
	 */
	protected Class[] getSourceClassesImpl() throws Exception {
		if (ENUM_TYPE == null) {
			return ArrayUtils.EMPTY_CLASS_ARRAY;
		}
		Set result = ContainerUtils.createOrderedSet();
		result.add(ENUM_TYPE);
		result.addAll(Arrays.asList(getTextConverter().getSourceClasses()));
		result.add(null);
		return (Class[]) result.toArray(new Class[result.size()]);
	}

	/**
	 * Get the textConverter used by this TextToEnumConverter.
	 * @return the textConverter
	 */
	public synchronized TextConverter getTextConverter() {
		if (textConverter == null) {
			setTextConverter(Defaults.createTextConverter());
		}
		return textConverter;
	}

	/**
	 * Set the textConverter used by this TextToEnumConverter.
	 * @param textConverter the TextConverter to set
	 */
	public synchronized void setTextConverter(TextConverter textConverter) {
		this.textConverter = textConverter;
	}

	/**
	 * Get the useToString of this TextToEnumConverter.  If <code>true</code>,
	 * enumerations will be converted to text using {@link Enum#toString()};
	 * otherwise {@link Enum#name()} will be used.  Default <code>false</code>.
	 * @return the useToString
	 */
	public boolean isUseToString() {
		return useToString;
	}

	/**
	 * Set the useToString of this TextToEnumConverter.  If <code>true</code>,
	 * enumerations will be converted to text using {@link Enum#toString()};
	 * otherwise {@link Enum#name()} will be used.  Default <code>false</code>.
	 * @param useToString the boolean to set
	 */
	public void setUseToString(boolean useToString) {
		this.useToString = useToString;
	}

	/**
	 * Get the emptyStringIsNull of this TextToEnumConverter.  If <code>true</code>,
	 * empty source text will be returned as a <code>null</code> enum instance.
	 * Default <code>true</code>.
	 * @return the emptyStringIsNull
	 */
	public boolean isEmptyStringIsNull() {
		return emptyStringIsNull;
	}

	/**
	 * Set the emptyStringIsNull of this TextToEnumConverter.  If <code>true</code>,
	 * empty source text will be returned as a <code>null</code> enum instance.
	 * Default <code>true</code>.
	 * @param emptyStringIsNull the boolean to set
	 */
	public void setEmptyStringIsNull(boolean emptyStringIsNull) {
		this.emptyStringIsNull = emptyStringIsNull;
	}

}
