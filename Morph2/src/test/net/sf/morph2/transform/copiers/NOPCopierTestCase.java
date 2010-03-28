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
package net.sf.morph2.transform.copiers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import net.sf.morph2.transform.Transformer;

/**
 *
 */
public class NOPCopierTestCase extends BaseCopierTestCase {

	/**
	 * {@inheritDoc}
	 * @see net.sf.morph2.transform.converters.BaseConverterTestCase#createDestinationClasses()
	 */
	public List createDestinationClasses() throws Exception {
		return Arrays.asList(new Class[] { Object.class, String.class, StringBuffer.class, List.class });
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
		return new NOPCopier();
	}

	/**
	 * {@inheritDoc}
	 * @see net.sf.morph2.transform.converters.BaseConverterTestCase#createValidPairs()
	 */
	public List createValidPairs() throws Exception {
		ArrayList l = new ArrayList();
		l.add(new ConvertedSourcePair(null, null));
		l.add(new ConvertedSourcePair("", this));
		l.add(new ConvertedSourcePair("", "foo"));
		l.add(new ConvertedSourcePair("", new Integer(100)));
		l.add(new ConvertedSourcePair(new Object(), this));
		l.add(new ConvertedSourcePair(new Object(), "foo"));
		l.add(new ConvertedSourcePair(new Object(), new Integer(100)));
		l.add(new ConvertedSourcePair(new ArrayList(), this));
		l.add(new ConvertedSourcePair(new HashSet(), "foo"));
		l.add(new ConvertedSourcePair(new HashMap(), new Integer(100)));
		return l;
	}

	/**
	 * {@inheritDoc}
	 * @see net.sf.morph2.transform.converters.BaseConverterTestCase#createInvalidPairs()
	 */
	public List createInvalidPairs() throws Exception {
		ArrayList l = new ArrayList();
		l.add(new ConvertedSourcePair("", null));
		l.add(new ConvertedSourcePair(toString(), this));
		l.add(new ConvertedSourcePair("foo", "foo"));
		l.add(new ConvertedSourcePair("100", new Integer(100)));
		l.add(new ConvertedSourcePair(addThis(new ArrayList()), this));
		l.add(new ConvertedSourcePair(addThis(new HashSet()), "foo"));
		return l;
	}

	private Collection addThis(Collection c) {
		c.add(this);
		return c;
	}
}
