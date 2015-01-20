/*
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

import net.sf.morph2.transform.Converter;
import net.sf.morph2.transform.Transformer;
import net.sf.morph2.transform.transformers.SimpleDelegatingTransformer;
import net.sf.morph2.util.ClassUtils;

/**
 * Converts any object to a Boolean by delegating to
 * {@link net.sf.morph2.transform.converters.TextToBooleanConverter},
 * {@link net.sf.morph2.transform.converters.NumberToBooleanConverter} and
 * {@link net.sf.morph2.transform.converters.ObjectToBooleanConverter}.
 * 
 * @author Matt Sgarlata
 * @since Dec 29, 2004
 * @see net.sf.morph2.transform.transformers.SimpleDelegatingTransformer
 * @see DefaultToBooleanConverter#COMPONENTS
 * @see net.sf.morph2.transform.converters.TextToBooleanConverter 
 * @see net.sf.morph2.transform.converters.NumberToBooleanConverter 
 * @see net.sf.morph2.transform.converters.ObjectToBooleanConverter 
 */
public class DefaultToBooleanConverter extends SimpleDelegatingTransformer {
	
	private static final Class[] DESTINATION_TYPES = { Boolean.class, boolean.class };

	/**
	 * {@inheritDoc}
	 */
	protected Transformer[] createDefaultComponents() {
		return new Converter[] {
				new IdentityConverter(), new PrimitiveWrapperConverter(),
				new TextToBooleanConverter(), new NumberToBooleanConverter(),
				new ObjectToBooleanConverter() };
	}

	/**
	 * {@inheritDoc}
	 */
	protected Class[] getDestinationClassesImpl() throws Exception {
		return DESTINATION_TYPES;
	}

	/**
	 * {@inheritDoc}
	 */
	protected boolean isTransformableImpl(Class destinationType,
		Class sourceType) throws Exception {
		return ClassUtils.inheritanceContains(getDestinationClasses(), destinationType)
				&& super.isTransformableImpl(destinationType, sourceType);
	}

}
