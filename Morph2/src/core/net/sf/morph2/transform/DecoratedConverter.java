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
package net.sf.morph2.transform;

import java.util.Locale;

/**
 * <p>
 * An extension of the Converter interface that defines a few more convenient
 * methods. All methods specified in this interface can be easily implemented
 * using just the methods in the Converter interface. Thus, if you are defining
 * your own converter you should implement only the Converter interface. If you
 * extend from {@link net.sf.morph2.transform.converters.BaseConverter}, your
 * converter will implement this inteface automatically. If you don't want to
 * extend from <code>BaseConverter</code>, you can still easily expose this
 * interface by using the
 * {@link net.sf.morph2.transform.converters.ConverterDecorator}.
 * </p>
 * 
 * <p>
 * <em>You should not directly implement this interface, because additional
 * methods may be introduced in later versions of Morph.  Instead, implement the
 * Converter interface and use the ConverterDecorator to expose this
 * interface.</em>
 * </p>
 * 
 * @author Matt Sgarlata
 * @since Dec 2, 2004
 */
public interface DecoratedConverter extends Converter, DecoratedTransformer {

	/**
	 * Converts the given <code>source</code> into an object of class
	 * <code>destinationClass</code>. The returned object may be a reference
	 * to <code>source</code> itself. This isn't an issue for immutable
	 * classes (String, Long, etc) but is an issue for Collection and Array
	 * types. Equivalent to calling
	 * <code>convert(destinationClass, source, Locale.getDefault())</code>.
	 * 
	 * @param destinationClass
	 *            the destination class to test
	 * @param source
	 *            the source object to test
	 * @return the result of the conversion
	 * @throws TransformationException
	 *             if <code>destinationClass</code> is <code>null</code>,
	 *             an error occurred while performing the conversion
	 */
	public Object convert(Class destinationClass, Object source)
		throws TransformationException;

	/**
	 * Tests if object1 and object2 are equivalent with respect to this
	 * converter. Specifically, this method returns <code>true</code> if
	 * either object1 can be converted to object2 or object2 can be converted to
	 * object1.
	 * 
	 * @param object1
	 *            the first object to test for equality
	 * @param object2
	 *            the second object to test for equality
	 * @param locale
	 *            the locale in which conversions take place
	 * @throws TransformationException
	 *             if an error occurred while performing the conversions
	 */
	public boolean equals(Object object1, Object object2, Locale locale)
		throws TransformationException;

	/**
	 * Tests if object1 and object2 are equivalent with respect to this
	 * converter. Specifically, this method returns <code>true</code> if
	 * either object1 can be converted to object2 or object2 can be converted to
	 * object1. Equivalent to calling
	 * <code>equals(object, object2, Locale.getDefault)</code>.
	 * 
	 * @param object1
	 *            the first object to test for equality
	 * @param object2
	 *            the second object to test for equality
	 * @throws TransformationException
	 *             if an error occurred while performing the conversions
	 */
	public boolean equals(Object object1, Object object2)
		throws TransformationException;

}
