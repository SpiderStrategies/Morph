/*
 * Copyright 2004-2005, 2008 the original author or authors.
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

import java.util.StringTokenizer;

import net.sf.morph2.util.MorphStringTokenizer;
import net.sf.morph2.util.StringUtils;

/**
 * An expression parser for use with the simple language.
 *
 * @author Matt Sgarlata
 * @since Nov 28, 2004
 */
public class SimpleExpressionParser extends BaseExpressionParser {

	private static final String DELIMITERS = "[]()\"'.";

	/**
	 * {@inheritDoc}
	 */
	public String[] parseImpl(String expression) throws Exception {
		StringTokenizer tokenizer = new MorphStringTokenizer(
			StringUtils.removeWhitespace(expression), DELIMITERS);
		// Don't use a converter to do this conversion because that makes
		// testing of morph difficult and it isn't as fast as doing the
		// conversion directly. Using a converter would make testing more
		// difficult because the parser breaks if the containercopier
		// breaks, which happens a lot for whatever reason, and a lot of things
		// depend on the parser). The speed issue is very important here,
		// because the simple expression parser is used very heavily by other
		// parts of the framework (e.g. contexts)
		String[] tokens = new String[tokenizer.countTokens()];
		for (int i = 0; i < tokens.length; i++) {
			tokens[i] = tokenizer.nextToken();
		}
		return tokens;
	}

}
