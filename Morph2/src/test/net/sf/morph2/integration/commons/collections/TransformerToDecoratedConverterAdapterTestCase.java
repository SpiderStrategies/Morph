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
package net.sf.morph2.integration.commons.collections;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.collections.TransformerUtils;

import net.sf.morph2.transform.Transformer;
import net.sf.morph2.transform.converters.BaseConverterTestCase;

/**
 *
 */
public class TransformerToDecoratedConverterAdapterTestCase extends BaseConverterTestCase {
	/**
	 * {@inheritDoc}
	 * @see net.sf.morph2.transform.converters.BaseConverterTestCase#createDestinationClasses()
	 */
	public List createDestinationClasses() throws Exception {
		return null;
	}

	/**
	 * {@inheritDoc}
	 * @see net.sf.morph2.transform.converters.BaseConverterTestCase#createInvalidDestinationClasses()
	 */
	public List createInvalidDestinationClasses() throws Exception {
		return null;
	}

	/**
	 * {@inheritDoc}
	 * @see net.sf.morph2.transform.converters.BaseConverterTestCase#createInvalidSources()
	 */
	public List createInvalidSources() throws Exception {
		return null;
	}

	/**
	 * {@inheritDoc}
	 * @see net.sf.morph2.transform.transformers.BaseTransformerTestCase#createTransformer()
	 */
	protected Transformer createTransformer() {
		HashMap testMap = new HashMap();
		testMap.put("foo", "FOO");
		testMap.put("bar", "BAR");
		testMap.put("baz", "BAZ");
		return new TransformerToDecoratedConverterAdapter(TransformerUtils.mapTransformer(testMap));
	}

	/**
	 * {@inheritDoc}
	 * @see net.sf.morph2.transform.converters.BaseConverterTestCase#createValidPairs()
	 */
	public List createValidPairs() throws Exception {
		ArrayList l = new ArrayList();
		l.add(new ConvertedSourcePair("FOO", "foo"));
		l.add(new ConvertedSourcePair("BAR", "bar"));
		l.add(new ConvertedSourcePair("BAZ", "baz"));
		l.add(new ConvertedSourcePair(null, "spazz"));
		l.add(new ConvertedSourcePair(null, new Integer(6)));
		l.add(new ConvertedSourcePair(null, new Object()));
		return l;
	}

	/**
	 * {@inheritDoc}
	 * @see net.sf.morph2.transform.converters.BaseConverterTestCase#createInvalidPairs()
	 */
	public List createInvalidPairs() throws Exception {
		ArrayList l = new ArrayList();
		l.add(new ConvertedSourcePair("foo", "foo"));
		l.add(new ConvertedSourcePair("bar", "bar"));
		l.add(new ConvertedSourcePair("baz", "baz"));
		l.add(new ConvertedSourcePair("spazz", "spazz"));
		l.add(new ConvertedSourcePair(new Integer(6), new Integer(6)));
		Object o = new Object();
		l.add(new ConvertedSourcePair(o, o));
		return l;
	}
}
