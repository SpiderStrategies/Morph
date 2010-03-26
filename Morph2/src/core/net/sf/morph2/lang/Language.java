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
package net.sf.morph2.lang;

/**
 * <p>
 * Defines a syntax for expressions that can be used to access and change
 * properties (bean or indexed) inside a target object. This is similar to the
 * expression languages found in the JSTL EL, the Spring framework, Struts, and
 * Velocity. Below are some example expressions.
 * </p>
 * 
 * <p>
 * JSTL EL example: <br>
 * <code>
 * myBean.myMap.myProperty
 * </code>
 * </p>
 * 
 * <p>
 * Spring example: <br>
 * <code>
 * myBean.myMap["myProperty"]
 * </code>
 * </p>
 * 
 * @author Matt Sgarlata
 * @since Nov 14, 2004
 */
public interface Language {

	/**
	 * Specifies the least restrictive type that may be assigned to property
	 * indicated by the given <code>expression</code>. If any type can be
	 * assigned to the property, this method returns <code>Object.class</code>.
	 * 
	 * @param target
	 *            the target being analyzed
	 * @param expression
	 *            indicates the property of <code>target</code> that is to be
	 *            analyzed
	 * @return the least restrictive type that may be assigned to the property
	 *         identified by <code>expression</code>
	 * @throws LanguageException
	 *             if <code>target</code> is <code>null</code> or <br>
	 *             an error occurrs while evaluating an otherwise valid
	 *             expression
	 * @throws InvalidExpressionException
	 *             if <code>expression</code> not a valid expression
	 */
	public Class getType(Object target, String expression)
		throws LanguageException;

	/**
	 * Indicates whether the given expression refers to a direct property of a
	 * target object.
	 * 
	 * @param expression
	 *            the expression to evaluate
	 * @return <code>true</code>, if the expression references a direct
	 *         property of the target object or <br>
	 *         <code>false</code>, otherwise
	 * @throws LanguageException
	 *             an error occurrs while evaluating an otherwise valid
	 *             expression
	 * @throws InvalidExpressionException
	 *             if <code>expression</code> not a valid expression
	 */
	public boolean isProperty(String expression) throws LanguageException;

	/**
	 * Retrieve the property indicated by <code>expression</code> from
	 * <code>target</code>.
	 * 
	 * @param target
	 *            the object from which information will be retrieved
	 * @param expression
	 *            an expression specifying which information to retrieve
	 * @return the information indicated by <code>expression</code> from
	 *         <code>target</code>
	 * @throws LanguageException
	 *             if <code>target</code> is <code>null</code> or <br>
	 *             an error occurrs while evaluating an otherwise valid
	 *             expression
	 * @throws InvalidExpressionException
	 *             if <code>expression</code> not a valid expression
	 */
	public Object get(Object target, String expression)
		throws LanguageException;

	/**
	 * Sets the property indicated by <code>expression</code> on
	 * <code>target</code>.
	 * 
	 * @param target
	 *            the object that will be modified
	 * @param expression
	 *            an expression specifying which information will be modified
	 * @param value
	 *            the information to be changed
	 * @throws LanguageException
	 *             if <code>target</code> is <code>null</code> or <br>
	 *             an error occurrs while evaluating an otherwise valid
	 *             expression
	 * @throws InvalidExpressionException
	 *             if <code>expression</code> not a valid expression
	 */
	public void set(Object target, String expression, Object value)
		throws LanguageException;

}
