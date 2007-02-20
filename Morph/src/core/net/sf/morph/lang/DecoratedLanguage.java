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
package net.sf.morph.lang;

import java.util.Locale;

import net.sf.morph.transform.TransformationException;

/**
 * <p>
 * Extends the capabilities of a Language by adding the capabilities of a
 * Converter.
 * </p>
 * 
 * <p>
 * <em>You should not directly implement this interface, because additional
 * methods may be introduced in later versions of Morph.  Instead, implement the
 * Language interface and use the LanguageDecorator to expose this interface.</em>
 * </p>
 * 
 * 
 * @author Matt Sgarlata
 * @since Nov 27, 2004
 */
public interface DecoratedLanguage extends Language {
	
	/**
	 * Retrieve the information indicated by <code>expression</code> from
	 * <code>target</code> as the type indicated by
	 * <code>destinationClass</code>.
	 * 
	 * @param target
	 *            the object from which information will be retrieved
	 * @param expression
	 *            an expression specifying which information to retrieve
	 * @param destinationClass
	 *            indicates the type that should be returned by this method
	 * @return the information indicated by <code>expression</code> from
	 *         <code>target</code>
	 * @throws TransformationException
	 *             if an error occurs while converting the requested information
	 *             to the type indicated by <code>destinationClass</code>
	 * @throws LanguageException
	 *             if <code>target</code> is <code>null</code> or <br>
	 *             an error occurrs while evaluating an otherwise valid
	 *             expression
	 * @throws InvalidExpressionException
	 *             if <code>expression</code> is empty or not a valid
	 *             expression
	 */
	public Object get(Object target, String expression, Class destinationClass)
		throws LanguageException, TransformationException;

	/**
	 * Retrieve the information indicated by <code>expression</code> from
	 * <code>target</code> as the type indicated by
	 * <code>destinationClass</code>.
	 * 
	 * @param target
	 *            the object from which information will be retrieved
	 * @param expression
	 *            an expression specifying which information to retrieve
	 * @param destinationClass
	 *            indicates the type that should be returned by this method
	 * @param locale
	 *            indicates the locale in which the conversion to type
	 *            <code>destinationClass</code> should be performed, if
	 *            applicable
	 * @return the information indicated by <code>expression</code> from
	 *         <code>target</code>
	 * @throws TransformationException
	 *             if an error occurs while converting the requested information
	 *             to the type indicated by <code>destinationClass</code>
	 * @throws LanguageException
	 *             if <code>target</code> is <code>null</code> or <br>
	 *             an error occurrs while evaluating an otherwise valid
	 *             expression
	 * @throws InvalidExpressionException
	 *             if <code>expression</code> is empty or not a valid
	 *             expression
	 */
	public Object get(Object target, String expression, Class destinationClass,
		Locale locale) throws LanguageException, TransformationException;

	/**
	 * Retrieve the information indicated by <code>expression</code> from
	 * <code>target</code> as the type indicated by
	 * <code>destinationClass</code>.
	 * 
	 * @param target
	 *            the object from which information will be retrieved
	 * @param expression
	 *            an expression specifying which information to retrieve
	 * @param locale
	 *            indicates the locale in which the conversion to type
	 *            <code>destinationClass</code> should be performed, if
	 *            applicable
	 * @param destinationClass
	 *            indicates the type that should be returned by this method
	 * @return the information indicated by <code>expression</code> from
	 *         <code>target</code>
	 * @throws TransformationException
	 *             if an error occurs while converting the requested information
	 *             to the type indicated by <code>destinationClass</code>
	 * @throws LanguageException
	 *             if <code>target</code> is <code>null</code> or <br>
	 *             an error occurrs while evaluating an otherwise valid
	 *             expression
	 * @throws InvalidExpressionException
	 *             if <code>expression</code> is empty or not a valid
	 *             expression
	 * @deprecated why would we need both signatures (Object, String, Locale, Class) and (Object, String, Class, Locale)?
	 */
	public Object get(Object target, String expression, Locale locale,
		Class destinationClass) throws LanguageException, TransformationException;

	/**
	 * Sets the information indicated by <code>expression</code> on
	 * <code>target</code>.<code>value</code> will be automatically
	 * converted to a type appropriate for the given <code>expression</code>.
	 * 
	 * @param target
	 *            the object that will be modified
	 * @param expression
	 *            an expression specifying which information will be modified
	 * @param value
	 *            the information to be changed
	 * @throws TransformationException
	 *             if an error occurs while converting <code>value</code> to
	 *             the appropriate type
	 * @throws LanguageException
	 *             if <code>target</code> is <code>null</code> or <br>
	 *             an error occurrs while evaluating an otherwise valid
	 *             expression
	 * @throws InvalidExpressionException
	 *             if <code>expression</code> is empty or not a valid
	 *             expression
	 */
	public void set(Object target, String expression, Object value)
		throws LanguageException, TransformationException;

	/**
	 * Sets the information indicated by <code>expression</code> on
	 * <code>target</code>.<code>value</code> will be automatically
	 * converted to a type appropriate for the given <code>expression</code>.
	 * 
	 * @param target
	 *            the object that will be modified
	 * @param expression
	 *            an expression specifying which information will be modified
	 * @param value
	 *            the information to be changed
	 * @param locale
	 *            indicates the locale in which the conversion to type
	 *            <code>destinationClass</code> should be performed, if
	 *            applicable
	 * @throws TransformationException
	 *             if an error occurs while converting <code>value</code> to
	 *             the appropriate type
	 * @throws LanguageException
	 *             if <code>target</code> is <code>null</code> or <br>
	 *             an error occurrs while evaluating an otherwise valid
	 *             expression
	 * @throws InvalidExpressionException
	 *             if <code>expression</code> is empty or not a valid
	 *             expression
	 */
	public void set(Object target, String expression, Object value,
		Locale locale) throws LanguageException, TransformationException;

}