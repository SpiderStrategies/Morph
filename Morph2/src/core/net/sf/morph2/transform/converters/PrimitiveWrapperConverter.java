/*
 * Copyright 2006, 2008 the original author or authors.
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

import java.util.Locale;

import net.sf.morph2.transform.DecoratedConverter;
import net.sf.morph2.transform.ExplicitTransformer;
import net.sf.morph2.transform.transformers.BaseTransformer;
import net.sf.morph2.util.ClassUtils;

/**
 * Converts primitive objects to their Object wrappers and vice-versa. Cannot
 * convert a primitive to a primitive or a wrapper to a wrapper (for those
 * conversions, use the
 * {@link net.sf.morph2.transform.converters.IdentityConverter}.
 * 
 * @author Matt Sgarlata
 * @since Jun 14, 2006
 */
public class PrimitiveWrapperConverter extends BaseTransformer implements
		DecoratedConverter, ExplicitTransformer {

	private static final Class[] SOURCE_DEST_CLASSES;
	static {
		Class[] primitiveTypes = ClassUtils.getPrimitiveTypes();
		Class[] wrapperTypes = ClassUtils.getWrapperTypes();
		SOURCE_DEST_CLASSES = new Class[primitiveTypes.length + wrapperTypes.length];
		System.arraycopy(primitiveTypes, 0, SOURCE_DEST_CLASSES, 0, primitiveTypes.length);
		System.arraycopy(wrapperTypes, 0, SOURCE_DEST_CLASSES, primitiveTypes.length, wrapperTypes.length);
	}

	/**
	 * {@inheritDoc}
	 */
	protected Class[] getSourceClassesImpl() throws Exception {
		return SOURCE_DEST_CLASSES;
	}

	/**
	 * {@inheritDoc}
	 */
	protected Class[] getDestinationClassesImpl() throws Exception {
		return SOURCE_DEST_CLASSES;
	}

	/**
	 * {@inheritDoc}
	 */
	protected boolean isTransformableImpl(Class destinationType, Class sourceType) throws Exception {
		return destinationType != null && sourceType != null
				&& (sourceType == ClassUtils.getPrimitiveWrapper(destinationType) ||
						destinationType == ClassUtils.getPrimitiveWrapper(sourceType));
	}

	/**
	 * {@inheritDoc}
	 */
	protected Object convertImpl(Class destinationClass, Object source, Locale locale) throws Exception {
		return source;
	}

	/**
	 * {@inheritDoc}
	 */
	protected boolean isWrappingRuntimeExceptions() {
	    return true;
    }

}
