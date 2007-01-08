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

import java.util.Locale;

import net.sf.morph.transform.Converter;
import net.sf.morph.transform.DecoratedConverter;
import net.sf.morph.transform.ExplicitTransformer;
import net.sf.morph.transform.TransformationException;
import net.sf.morph.transform.transformers.BaseTransformer;
import net.sf.morph.util.TransformerUtils;

/**
 * Converts objects that are already instances of the destination class by
 * simply returning the object unchanged.
 * 
 * @author Matt Sgarlata
 * @since Dec 31, 2004
 */
public class IdentityConverter extends BaseTransformer implements Converter, DecoratedConverter, ExplicitTransformer {
	
	public static final Class[] DEFAULT_SOURCE_AND_DESTINATION_TYPES = {
			Object.class, boolean.class, byte.class, char.class, short.class,
			int.class, long.class, float.class, double.class, null };
	
	public IdentityConverter() {
		super();
	}
	
	public IdentityConverter(Class[] sourceAndDestinationClasses) {
		super();
		setSourceClasses(sourceAndDestinationClasses);
		setDestinationClasses(sourceAndDestinationClasses);
	}

	protected boolean isTransformableImpl(Class destinationType,
		Class sourceType) throws Exception {

		if (destinationType == sourceType) {
			return true;
		}
		if (destinationType == null || sourceType == null) {
			return false;
		}
		return TransformerUtils.isImplicitlyTransformable(this, destinationType, sourceType)
				&& destinationType.isAssignableFrom(sourceType);
	}

	protected Object convertImpl(Class destinationClass, Object source,
		Locale locale) throws Exception {
		
		if (destinationClass.isAssignableFrom(source.getClass())) {
			return source;
		}
		throw new TransformationException(destinationClass, source);
	}

	protected boolean isPerformingLogging() {
		// this transformation is trivial; don't clutter the log with it
		return false;
	}
	
	protected Class[] getSourceClassesImpl() throws Exception {
		return DEFAULT_SOURCE_AND_DESTINATION_TYPES;
	}

	protected Class[] getDestinationClassesImpl() throws Exception {
		return DEFAULT_SOURCE_AND_DESTINATION_TYPES;
	}

}
