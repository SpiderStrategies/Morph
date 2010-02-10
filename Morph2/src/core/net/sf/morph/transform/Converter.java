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
package net.sf.morph.transform;

import java.util.Locale;

/**
 * <p>
 * An object that can convert one type of object into another type of object.
 * Usually, the sourceClasses and destinationClasses attributes determine the
 * set of sources and destinations which the converter can convert.  However,
 * if any of the sourceClasses is not convertible to a destinationClass, then
 * a converter may implement the {@link net.sf.morph.transform.ExplicitTransformer}
 * to explicitly define which sources are convertible to which destinations.
 * </p>
 *  
 * <p>
 * Note that the Converter interface can be easily implemented by a Copier (see
 * {@link net.sf.morph.transform.converters.CopierConverter}), so it is
 * recommended that Converters only be implemented for basic data types
 * (numbers, primitives) or immutable data types (e.g. Strings, but note Dates).
 * </p>
 * 
 * @author Matt Sgarlata
 * @since October 24, 2004
 * @see net.sf.morph.transform.ExplicitTransformer
 */
public interface Converter extends Transformer {
	
	/**
	 * Converts the given <code>source</code> into an object of class
	 * <code>destinationClass</code>. The returned object may be a reference
	 * to <code>source</code> itself. This isn't an issue for immutable
	 * classes (String, Long, etc) but is an issue for Collection and Array
	 * types.
	 * 
	 * @param destinationClass
	 *            the destination class to test
	 * @param source
	 *            the source object to test
	 * @param locale
	 *            the locale in which the conversion should take place, or
	 *            <code>null</code> if the locale is not applicable
	 * @return the result of the conversion
	 * @throws TransformationException
	 *             if <code>destinationClass</code> is <code>null</code>,
	 *             an error occurred while performing the conversion
	 */
	public Object convert(Class destinationClass, Object source, Locale locale)
		throws TransformationException;
}