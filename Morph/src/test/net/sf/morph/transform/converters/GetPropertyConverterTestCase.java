/*
 * Copyright 2007 the original author or authors.
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
package net.sf.morph.transform.converters;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import net.sf.morph.reflect.BeanReflector;
import net.sf.morph.transform.Transformer;

/**
 * Tests for the {@link net.sf.morph.transform.converters.GetPropertyConverter}.
 */
public class GetPropertyConverterTestCase extends BaseConverterTestCase {
	public static class HasTraditionalSizeProperty {
		private int size;

		/**
		 * Construct a new GetPropertyConverterTestCase.HasTraditionalSizeProperty.
		 */
		public HasTraditionalSizeProperty(int size) {
			this.size = size;
		}

		public int getSize() {
			return size;
		}
	}

	public List createInvalidDestinationClasses() throws Exception {
		return Collections.EMPTY_LIST;
	}

	public List createInvalidSources() throws Exception {
		return null;
	}

	public List createValidPairs() throws Exception {
		List list = new ArrayList();
		list.add(new ConvertedSourcePair(new Integer(0), new HashMap()));
		String[] foobar = new String[] { "foo", "bar" };
		list.add(new ConvertedSourcePair(new Integer(2), foobar));
		list.add(new ConvertedSourcePair(new Integer(1), Collections
				.singletonList(foobar)));
		list.add(new ConvertedSourcePair(new Integer(100), new HasTraditionalSizeProperty(100)));
		return list;
	}

	public List createDestinationClasses() throws Exception {
		return Collections.singletonList(Object.class);
	}

	protected Transformer createTransformer() {
		return new GetPropertyConverter(BeanReflector.IMPLICIT_PROPERTY_SIZE);
	}

}
