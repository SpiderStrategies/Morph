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
package net.sf.morph.transform.converters;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import net.sf.morph.transform.Transformer;

/**
 * Tests for the {@link net.sf.morph.transform.converters.ObjectToClassConverter}.
 * 
 * @author Matt Sgarlata
 * @since Feb 10, 2006
 */
public class ObjectToClassConverterTestCase extends BaseConverterTestCase {

	public List createInvalidDestinationClasses() throws Exception {
		List list = new ArrayList();
		list.add(Object.class);
		list.add(Object[].class);
		list.add(int.class);
		return list;
	}

	public List createInvalidSources() throws Exception {
		return null;
	}

	public List createValidPairs() throws Exception {
		List list = new ArrayList();
		list.add(new ConvertedSourcePair(HashMap.class, new HashMap()));
		list.add(new ConvertedSourcePair(null, null));
		list.add(new ConvertedSourcePair(ObjectToClassConverterTestCase.class, this));
		list.add(new ConvertedSourcePair(String.class, ""));
		list.add(new ConvertedSourcePair(Float.class, new Float(3.5)));
		return list;
	}

	public List createDestinationClasses() throws Exception {
		return Collections.singletonList(Class.class);
	}

	protected Transformer createTransformer() {
		return new ObjectToClassConverter();
	}

}
