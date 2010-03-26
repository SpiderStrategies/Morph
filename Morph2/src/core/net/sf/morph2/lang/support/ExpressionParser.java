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
package net.sf.morph2.lang.support;

import net.sf.morph2.lang.InvalidExpressionException;

/**
 * A basic parser that breaks an expression into an array of tokens.
 * 
 * @author Matt Sgarlata
 * @since Nov 28, 2004
 */
public interface ExpressionParser {

	/**
	 * Parses an expression and returns the tokens found in the expression.
	 * 
	 * @param expression
	 *            the expression
	 * @return the tokens found in the expression
	 * @throws InvalidExpressionException
	 *             if the supplied expression is invalid
	 */
	public String[] parse(String expression) throws InvalidExpressionException;
}
