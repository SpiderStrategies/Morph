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
package net.sf.morph.lang.support;

import java.util.List;

import net.sf.composite.util.ObjectUtils;
import net.sf.morph.lang.InvalidExpressionException;

/**
 * A base class for expression parsers.
 * 
 * @author Matt Sgarlata
 * @since Nov 28, 2004
 */
public abstract class BaseExpressionParser implements ExpressionParser {

	public abstract List parseImpl(String expression) throws Exception;
	
	public String[] parse(String expression) throws InvalidExpressionException {
		if (ObjectUtils.isEmpty(expression)) {
			return new String[] { };
		}
		
		try {
			List list = parseImpl(expression);
			return (String[]) list.toArray(new String[list.size()]);
		}
		catch (InvalidExpressionException e) {
			throw e;
		}
		catch (Exception e) {
			throw new InvalidExpressionException("The expression '" + expression + "' is invalid", e);
		}
	}

}
