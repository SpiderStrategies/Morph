/*
 * Copyright 2004-2005, 2007-2008 the original author or authors.
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

import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.Locale;
import java.util.Set;

import net.sf.morph2.transform.DecoratedConverter;
import net.sf.morph2.transform.ExplicitTransformer;
import net.sf.morph2.transform.ImpreciseTransformer;
import net.sf.morph2.transform.TransformationException;
import net.sf.morph2.transform.transformers.BaseTransformer;
import net.sf.morph2.util.ClassUtils;
import net.sf.morph2.util.ContainerUtils;
import net.sf.morph2.util.TransformerUtils;

/**
 * Converts text types ({@link java.lang.String},
 * {@link java.lang.StringBuffer} and {@link java.lang.Character},
 * <code>char[]</code> and <code>byte[]</code> from one type to another.
 * Empty Strings, StringBuffers with lengths of zero and empty character and
 * byte arrays are converted to <code>null</code> Characters, and non-empty
 * Strings and StringBuffers are converted to Characters by returning the first
 * character in the String or StringBuffer. CharSequence is handled in this way:
 * an explicit request for CharSequence yields a String. Other CharSequence
 * implementations are handled if they have a public constructor that accepts
 * a String argument.
 *
 * @author Matt Sgarlata
 * @since Jan 2, 2005
 */
public class TextConverter extends BaseTransformer implements DecoratedConverter,
		ExplicitTransformer, ImpreciseTransformer {

	private static final Class CHAR_SEQUENCE = ClassUtils.isJdk14OrHigherPresent() ? ClassUtils
			.convertToClass("java.lang.CharSequence")
			: null;

	private static final HashMap CONSTRUCTOR_CACHE = new HashMap();
	private static final Class[] SOURCE_AND_DESTINATION_TYPES;

	static {
		Set s = ContainerUtils.createOrderedSet();
		
		s.add(StringBuffer.class);
		s.add(String.class);
		s.add(CHAR_SEQUENCE);
		s.add(byte[].class);
		s.add(char[].class);
		s.add(Character.class);
		s.add(char.class);
		s.add(null);
		SOURCE_AND_DESTINATION_TYPES = (Class[]) s.toArray(new Class[s.size()]);
	}

	private boolean allowStringAsChar = true;
	private boolean emptyNull;

	/**
	 * {@inheritDoc}
	 */
	protected Object convertImpl(Class destinationClass, Object source, Locale locale)
			throws Exception {
		Class sourceClass = ClassUtils.getClass(source);
		if (destinationClass == null
				&& ClassUtils.inheritanceContains(getSourceClasses(), sourceClass)) {
			return null;
		}
		if (isChar(destinationClass) && isChar(sourceClass)) {
			return source;
		}
		//if source == null, not automatically handling nulls, therefore null -> "":
		String string = source == null ? "" : source instanceof byte[] ? new String(
				(byte[]) source) : source instanceof char[] ? new String((char[]) source)
				: source.toString();

		if (isChar(destinationClass)) {
			if (!isAllowStringAsChar()) {
				throw new TransformationException(destinationClass, source);
			}
			if ("".equals(string)) {
				if (destinationClass == char.class) {
					throw new TransformationException(destinationClass, source);
				}
				return null;
			}
			return new Character(string.charAt(0));
		}
		if (destinationClass == String.class
				|| (destinationClass == CHAR_SEQUENCE && CHAR_SEQUENCE != null)) {
			return string;
		}
		if (destinationClass == byte[].class) {
			return string.getBytes();
		}
		if (destinationClass == char[].class) {
			return string.toCharArray();
		}
		if (ClassUtils.inheritanceContains(getDestinationClasses(), destinationClass)
				&& canCreate(destinationClass)) {
			try {
				return getConstructor(destinationClass).newInstance(new Object[] { string });
			} catch (Exception e) {
				throw new TransformationException(destinationClass, source, e);
			}
		}
		throw new TransformationException(destinationClass, source);
	}

	/**
	 * {@inheritDoc}
	 */
	protected boolean isTransformableImpl(Class destinationType, Class sourceType)
			throws Exception {
		if (!TransformerUtils.isImplicitlyTransformable(this, destinationType, sourceType)) {
			return false;
		}
		if (destinationType == null) {
			return true;
		}
		if (sourceType == null) {
			return !destinationType.isPrimitive();
		}
		if (isChar(destinationType)) {
			return isChar(sourceType) || isAllowStringAsChar();
		}
		if (isCharSequence(destinationType)) {
			return canCreate(destinationType);
		}
		return ClassUtils.inheritanceContains(getDestinationClasses(), destinationType);
	}

	/**
	 * {@inheritDoc}
	 */
	protected boolean isImpreciseTransformationImpl(Class destinationClass, Class sourceClass) {
		if (super.isImpreciseTransformationImpl(destinationClass, sourceClass)) {
			return true;
		}
		return isChar(destinationClass) && !isChar(sourceClass);
	}

	/**
	 * {@inheritDoc}
	 */
	protected boolean isAutomaticallyHandlingNulls() {
		return !isEmptyNull();
	}

	/**
	 * {@inheritDoc}
	 */
	protected Class[] getSourceClassesImpl() throws Exception {
		return SOURCE_AND_DESTINATION_TYPES;
	}

	/**
	 * {@inheritDoc}
	 */
	protected Class[] getDestinationClassesImpl() throws Exception {
		return SOURCE_AND_DESTINATION_TYPES;
	}

	/**
	 * Learn whether <code>null</code> values return as empty strings.
	 * @return boolean
	 * @since 1.1
	 */
	public boolean isEmptyNull() {
		return emptyNull;
	}

	/**
	 * Set whether <code>null</code> values return as empty strings.
	 * @param emptyNull boolean
	 * @since 1.1
	 */
	public void setEmptyNull(boolean emptyNull) {
		this.emptyNull = emptyNull;
	}

	/**
	 * Learn whether string-to-char type conversions are allowed.
	 * @return boolean
	 * @since 1.1
	 */
	public boolean isAllowStringAsChar() {
		return allowStringAsChar;
	}

	/**
	 * Set whether string-to-char type conversions are allowed. This might be
	 * undesirable for e.g. chained transformations or any operation where
	 * a loss of "precision" might be detrimental. Default <code>true</code>.
	 * @param allowStringAsChar the boolean to set
	 * @since 1.1
	 */
	public void setAllowStringAsChar(boolean allowStringAsChar) {
		this.allowStringAsChar = allowStringAsChar;
	}

	private static boolean isChar(Class c) {
		return c == char.class || c == Character.class;
	}

	private static boolean isCharSequence(Class c) {
		return CHAR_SEQUENCE != null && CHAR_SEQUENCE.isAssignableFrom(c);
	}

	private static synchronized boolean canCreate(Class c) {
		if (CONSTRUCTOR_CACHE.containsKey(c)) {
			return true;
		}
		Constructor[] cs = c.getConstructors();
		for (int i = 0; i < cs.length; i++) {
			Class[] p = cs[i].getParameterTypes();
			if (p.length == 1 && p[0].isAssignableFrom(String.class)) {
				CONSTRUCTOR_CACHE.put(c, cs[i]);
				return true;
			}
		}
		return false;
	}

	private static Constructor getConstructor(Class c) {
		return (Constructor) CONSTRUCTOR_CACHE.get(c);
	}

}
