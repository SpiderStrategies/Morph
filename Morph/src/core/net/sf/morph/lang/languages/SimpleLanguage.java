/*
 * Copyright 2004-2005, 2007 the original author or authors.
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
package net.sf.morph.lang.languages;

import net.sf.composite.util.ObjectUtils;
import net.sf.morph.Defaults;
import net.sf.morph.lang.support.ExpressionParser;
import net.sf.morph.lang.support.SimpleExpressionParser;
import net.sf.morph.reflect.BeanReflector;

/**
 * <p>
 * A simple language that is similar to the languages used by the JSTL EL, the
 * Spring framework, Jakarta Commons BeanUtils, and Struts. Any "simple"
 * expression in any of these languages should be recognized by this language.
 * An example of a simple expression is <code>myBean.myProperty</code>. An
 * example of a "non-simple" expression is
 * </p>
 * 
 * <p>
 * <code>
 * myBean.myProperty + myOtherBean.myOtherProperty 
 * </code>
 * </p>
 * 
 * <p>
 * To be more precise, this language treats numbers as indexes into array-like
 * objects and treats other alphanumeric identifiers as property names of
 * bean-like objects. The following characters are all considered delimiters
 * with no particularly special meaning: <code>[]().&quot;'</code>. Thus, all
 * of the expressions in the table below have the same value in this language.
 * </p>
 * 
 * <table border="1">
 * <tr>
 * <th>Language</th>
 * <th>Expression</th>
 * </tr>
 * <tr>
 * <td><a
 * href="http://bellsouthpwp.net/b/i/billsigg/jstl-quick-reference.pdf">JSTL EL
 * </a>, Morph's simple language</td>
 * <td>myBean.myMap["mapProperty"].myArray[0]</td>
 * </tr>
 * <tr>
 * <td><a
 * href="http://www.springframework.org/docs/reference/validation.html#beans-beans-conventions">Spring
 * </a>, Morph's simple language</td>
 * <td>myBean.myMap[mapProperty].myArray[0]</td>
 * </tr>
 * <tr>
 * <td><a
 * href="http://jakarta.apache.org/commons/beanutils/commons-beanutils-1.7.0/docs/api/org/apache/commons/beanutils/package-summary.html#standard.nested">Jakarta
 * Commons BeanUtils </a>, <a
 * href="http://struts.apache.org/faqs/indexedprops.html">Struts </a>, Morph's
 * simple language</td>
 * <td>myBean.myMap(mapProperty).myArray[0]</tr>
 * <tr>
 * <td>Morph's simple language</td>
 * <td>myBean.myMap.mapProperty.myArray[0]</td>
 * </tr>
 * <tr>
 * <td>Morph's simple language</td>
 * <td>myBean.myMap.mapProperty.myArray.0</td>
 * </tr>
 * <tr>
 * <td>Morph's simple language</td>
 * <td>
 * myBean&quot;myMap&quot;'''['.']'('.')mapProperty&quot;&quot;&quot;&quot;myArray[]0....
 * </td>
 * </tr>
 * </table>
 * 
 * 
 * <p>
 * A simple language that will work for most use cases, but has the limitation
 * that bean property names cannot be numbers. Numbers like 123 are always
 * considered indicies into an indexed object. For example, <code>123</code>
 * is an invalid bean property name in this language, but <code>12Monkies</code>
 * and <code>fridayThe13th</code> are valid property names.
 * </p>
 * 
 * @author Matt Sgarlata
 * @since Nov 28, 2004
 */
public class SimpleLanguage extends BaseLanguage {
	
	private static final ExpressionParser DEFAULT_EXPRESSION_PARSER = new SimpleExpressionParser();

	private ExpressionParser expressionParser = DEFAULT_EXPRESSION_PARSER;
	private BeanReflector reflector;
	
	protected boolean isPropertyImpl(String expression) throws Exception {
		return expressionParser.parse(expression).length == 1;
	}

	protected Class getTypeImpl(Object target, String expression) throws Exception {
		if (ObjectUtils.isEmpty(expression)) {
			return target.getClass();
		}		
		
		Object value = target;
		
		String[] tokens = expressionParser.parse(expression);
		for (int i=0; i<tokens.length - 1; i++) {
			String token = tokens[i];
			value = getReflector().get(value, token);
		}
		
		String token = tokens[tokens.length - 1];
		return getReflector().getType(value, token);
	}

	protected Object getImpl(Object target, String expression) throws Exception {
		if (ObjectUtils.isEmpty(expression)) {
			return target;
		}

		Object value = target;
		
		String[] tokens = expressionParser.parse(expression);
		for (int i=0; i<tokens.length; i++) {
			String token = tokens[i];
			if (value == null) {
				return null;
			}
			value = getReflector().get(value, token);
		}
		
		return value;
	}

	protected void setImpl(Object target, String expression, Object value) throws Exception {
		if (ObjectUtils.isEmpty(expression)) {
			target = value;
		}
		
		Object currentTarget = target;
		
		String[] tokens = expressionParser.parse(expression);
		for (int i=0; i<tokens.length; i++) {
			String token = tokens[i];
			if (i == tokens.length - 1) {
				getReflector().set(currentTarget, token, value);	
			}
			else {
				currentTarget = getReflector().get(currentTarget, token);
			}			
		}

	}

	/**
	 * @return Returns the expressionParser.
	 */
	public ExpressionParser getExpressionParser() {
		return expressionParser;
	}
	/**
	 * @param expressionParser The expressionParser to set.
	 */
	public void setExpressionParser(ExpressionParser expressionParser) {
		this.expressionParser = expressionParser;
	}

	public BeanReflector getReflector() {
		if (reflector == null) {
			setReflector(Defaults.createBeanReflector());
		}
		return reflector;
	}
	public void setReflector(BeanReflector reflector) {
		this.reflector = reflector;
	}
}