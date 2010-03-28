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
package net.sf.morph2.reflect.reflectors;

import java.util.StringTokenizer;

import net.sf.morph2.reflect.SizableReflector;

/**
 * Reflector for {@link StringTokenizer}s.
 * 
 * @author Matt Sgarlata
 * @since Apr 9, 2007
 */
public class StringTokenizerReflector extends EnumerationReflector implements SizableReflector {

	private static final Class[] REFLECTABLE_TYPES = new Class[] { StringTokenizer.class };

	/**
	 * {@inheritDoc}
	 */
	protected int getSizeImpl(Object container) throws Exception {
		StringTokenizer tokenizer = (StringTokenizer) container;
		return tokenizer.countTokens();
	}

	/**
	 * {@inheritDoc}
	 */
	public Class[] getReflectableClassesImpl() {
		return REFLECTABLE_TYPES;
	}

}
