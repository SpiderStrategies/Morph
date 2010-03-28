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
package net.sf.morph2.transform.converters;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import net.sf.morph2.transform.Transformer;
import net.sf.morph2.util.ClassUtils;

/**
 *
 */
public class CloningConverterTestCase extends BaseConverterTestCase {
	/**
	 * {@inheritDoc}
	 * @see net.sf.morph2.transform.converters.BaseConverterTestCase#createDestinationClasses()
	 */
	public List createDestinationClasses() throws Exception {
		return Collections.singletonList(Cloneable.class);
	}

	/**
	 * {@inheritDoc}
	 * @see net.sf.morph2.transform.converters.BaseConverterTestCase#createInvalidDestinationClasses()
	 */
	public List createInvalidDestinationClasses() throws Exception {
		return Arrays.asList(ClassUtils.getPrimitiveTypes());
	}

	/**
	 * {@inheritDoc}
	 * @see net.sf.morph2.transform.converters.BaseConverterTestCase#createInvalidSources()
	 */
	public List createInvalidSources() throws Exception {
		return Arrays.asList(new Object[] { "foo", new Integer(1) });
	}

	/**
	 * {@inheritDoc}
	 * @see net.sf.morph2.transform.transformers.BaseTransformerTestCase#createTransformer()
	 */
	protected Transformer createTransformer() {
		return new CloningConverter();
	}

	/**
	 * {@inheritDoc}
	 * @see net.sf.morph2.transform.converters.BaseConverterTestCase#createValidPairs()
	 */
	public List createValidPairs() throws Exception {
		ArrayList l = new ArrayList(Arrays.asList(new String[] { "foo", "bar", "baz" }));
		return Collections.singletonList(new ConvertedSourcePair(l, l));
	}

	/**
	 * {@inheritDoc}
	 * @see net.sf.morph2.transform.converters.BaseConverterTestCase#createInvalidPairs()
	 */
	public List createInvalidPairs() throws Exception {
		ArrayList l = new ArrayList();
		l.add(new ConvertedSourcePair("foo", "foo"));
		l.add(new ConvertedSourcePair(null, null));
		l.add(new ConvertedSourcePair(new Object(), new Object()));
		l.add(new ConvertedSourcePair(this, this));
		l.add(new ConvertedSourcePair(new Integer(100), new Integer(100)));
		return l;
	}

	public void testNotSame() throws Exception {
		HashMap m = new HashMap();
		m.put("foo", "bar");
		m.put("oscar", "grouch");
		HashMap m2 = (HashMap) getConverter().convert(HashMap.class, m);
		assertEquals(m, m2);
		assertNotSame(m, m2);
	}
}
