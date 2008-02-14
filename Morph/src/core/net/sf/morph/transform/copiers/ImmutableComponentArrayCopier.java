/*
 * Copyright 2007-2008 the original author or authors.
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

import java.lang.reflect.Array;
import java.util.Locale;
import java.util.Set;

import net.sf.morph.transform.DecoratedConverter;
import net.sf.morph.transform.DecoratedCopier;
import net.sf.morph.transform.ExplicitTransformer;
import net.sf.morph.transform.transformers.BaseTransformer;
import net.sf.morph.util.ClassUtils;
import net.sf.morph.util.ContainerUtils;
import net.sf.morph.util.TransformerUtils;

/**
 * Copies arrays of matching immutable component types using System.arraycopy.
 * @since Morph 1.1
 * @author mbenson
 */
public class ImmutableComponentArrayCopier extends BaseTransformer implements
		DecoratedConverter, DecoratedCopier, ExplicitTransformer {
	private static final Class[] SOURCE_AND_DEST_TYPES;

	static {
		Class[] componentTypes = ClassUtils.getImmutableTypes();
		Set arrayTypes = ContainerUtils.createOrderedSet();
		for (int i = 0; i < componentTypes.length; i++) {
			if (componentTypes[i] != null) {
				arrayTypes.add(ClassUtils.getArrayClass(componentTypes[i]));
			}
		}
		SOURCE_AND_DEST_TYPES = (Class[]) arrayTypes
				.toArray(new Class[arrayTypes.size()]);
	}

	/**
	 * {@inheritDoc}
	 * @see net.sf.morph.transform.transformers.BaseTransformer#getDestinationClassesImpl()
	 */
	protected Class[] getDestinationClassesImpl() throws Exception {
		return SOURCE_AND_DEST_TYPES;
	}

	/**
	 * {@inheritDoc}
	 * @see net.sf.morph.transform.transformers.BaseTransformer#getSourceClassesImpl()
	 */
	protected Class[] getSourceClassesImpl() throws Exception {
		return SOURCE_AND_DEST_TYPES;
	}

	/**
	 * {@inheritDoc}
	 * @see net.sf.morph.transform.transformers.BaseTransformer#isTransformableImpl(java.lang.Class, java.lang.Class)
	 */
	protected boolean isTransformableImpl(Class destinationType, Class sourceType)
			throws Exception {
		return TransformerUtils.isImplicitlyTransformable(this, destinationType,
				sourceType)
				&& sourceType.getComponentType() == destinationType.getComponentType();
	}

	/**
	 * {@inheritDoc}
	 * @see net.sf.morph.transform.transformers.BaseTransformer#convertImpl(java.lang.Class, java.lang.Object, java.util.Locale)
	 */
	protected Object convertImpl(Class destinationClass, Object source, Locale locale) throws Exception {
		Object array = ClassUtils.createArray(destinationClass.getComponentType(), Array.getLength(source));
		copyImpl(array, source, locale, TRANSFORMATION_TYPE_CONVERT);
		return array;
	}

	/**
	 * {@inheritDoc}
	 * @see net.sf.morph.transform.transformers.BaseTransformer#copyImpl(java.lang.Object, java.lang.Object, java.util.Locale, java.lang.Integer)
	 */
	protected void copyImpl(Object destination, Object source, Locale locale,
			Integer preferredTransformationType) throws Exception {
		System.arraycopy(source, 0, destination, 0, Array.getLength(source));
	}

	/**
	 * {@inheritDoc}
	 * @see net.sf.morph.transform.transformers.BaseTransformer#isPerformingLogging()
	 */
	protected boolean isPerformingLogging() {
		return false;
	}
}
