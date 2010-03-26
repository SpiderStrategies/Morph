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
package net.sf.morph2.context;

import java.util.Locale;

/**
 * <p>Adds the capabilities of a Language and a Converter to a Context to expose
 * additional functionality.</p>
 * 
 * <p>
 * <em>You should not directly implement this interface, because additional
 * methods may be introduced in later versions of Morph.  Instead, implement the
 * Context interface and use the ContextDecorator to expose this interface.</em>
 * </p>
 * 
 * @author Matt Sgarlata
 * @since Dec 5, 2004
 */
public interface DecoratedContext extends Context {

	/**
	 * Retrieve the property named <code>propertyName</code>. The property
	 * named {@link Context#PROPERTY_NAMES_PROPERTY}has special symantics.
	 * Unless a value for this property has already been explicitly specified
	 * for the context, <code>context.get(PROPERTY_NAMES_PROPERTY)</code>
	 * should return the same result as {@link Context#getPropertyNames()}.
	 * 
	 * @param expression
	 *            an expression in a language that defines the property to be
	 *            retrieved
	 * @return the requested value
	 * @throws ContextException
	 *             if the expression is invalid or an error occurrs while
	 *             attempting to retrieve the requested information
	 */
	public Object get(String expression) throws ContextException;

	/**
	 * Retrieve the information indicated by <code>expression</code> as the
	 * type indicated by <code>destinationClass</code>.
	 * 
	 * @param expression
	 *            an expression specifying which information to retrieve
	 * @param destinationClass
	 *            indicates the type that should be returned by this method
	 * @return the requested value
	 * @throws ContextException
	 *             if the expression is invalid or an error occurrs while
	 *             attempting to retrieve the requested information
	 */
	public Object get(String expression, Class destinationClass)
		throws ContextException;

	/**
	 * Retrieve the information indicated by <code>expression</code> as the
	 * type indicated by <code>destinationClass</code>.
	 * 
	 * @param expression
	 *            an expression specifying which information to retrieve
	 * @param destinationClass
	 *            indicates the type that should be returned by this method
	 * @param locale
	 *            indicates the locale in which the conversion to type
	 *            <code>destinationClass</code> should be performed, if
	 *            applicable
	 * @return the requested value
	 * @throws ContextException
	 *             if the expression is invalid or an error occurrs while
	 *             attempting to retrieve the requested information
	 */
	public Object get(String expression, Class destinationClass, Locale locale)
		throws ContextException;

	/**
	 * Retrieve the information indicated by <code>expression</code> as the
	 * type indicated by <code>destinationClass</code>.
	 * 
	 * @param expression
	 *            an expression specifying which information to retrieve
	 * @param locale
	 *            indicates the locale in which the conversion to type
	 *            <code>destinationClass</code> should be performed, if
	 *            applicable
	 * @param destinationClass
	 *            indicates the type that should be returned by this method
	 * @return the requested value
	 * @throws ContextException
	 *             if the expression is invalid or an error occurred while
	 *             attempting to retrieve the requested information
	 */
	public Object get(String expression, Locale locale, Class destinationClass)
		throws ContextException;

	/**
	 * Sets the information indicated by <code>expression</code> to
	 * <code>value</code>, which will be automatically converted to a type
	 * appropriate for the given <code>expression</code>.
	 * 
	 * @param expression
	 *            an expression specifying which information will be modified
	 * @param value
	 *            the information to be changed
	 * @throws ContextException
	 *             if the given expression is invalid or an error occurred while
	 *             attempting to set the given value
	 */
	public void set(String expression, Object value) throws ContextException;

	/**
	 * Sets the information indicated by <code>expression</code> on
	 * <code>target</code>. <code>value</code> will be automatically
	 * converted to a type appropriate for the given <code>expression</code>.
	 * 
	 * @param expression
	 *            an expression specifying which information will be modified
	 * @param value
	 *            the information to be changed
	 * @param locale
	 *            indicates the locale in which the conversion to type
	 *            <code>destinationClass</code> should be performed, if
	 *            applicable
	 * @throws ContextException
	 *             if the given expression is invalid or an error occurred while
	 *             attempting to set the given value
	 */
	public void set(String expression, Object value, Locale locale)
		throws ContextException;

}
