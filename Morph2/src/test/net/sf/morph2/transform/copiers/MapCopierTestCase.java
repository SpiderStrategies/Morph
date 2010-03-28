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
import java.util.Collections;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import net.sf.morph2.transform.Transformer;
import net.sf.morph2.transform.converters.TextConverter;
import net.sf.morph2.util.TestUtils;

public class MapCopierTestCase extends BaseCopierTestCase {
	protected Transformer createTransformer() {
		return new MapCopier();
	}

	public List createInvalidSources() throws Exception {
		return null;
	}

	public List createInvalidDestinationClasses() throws Exception {
		return Arrays.asList(new Class[] {
				Object.class, Integer.class, int.class, List.class, null });
	}

	public List createValidPairs() throws Exception {
		HashMap m = new HashMap();
		m.put("foo", "fooVal");
		m.put(new Integer(0), "zeroVal");
		m.put(new Object(), "objectVal");
		m.put(Boolean.TRUE, "trueVal");
		m.put(Boolean.FALSE, "falseVal");
		HashMap clone = (HashMap) m.clone();
		ArrayList result = new ArrayList();
		result.add(new ConvertedSourcePair(clone, m));
		result.add(new ConvertedSourcePair(new HashMap(), new HashMap()));
		return result;
	}

	public List createDestinationClasses() throws Exception {
		return Collections.singletonList(Map.class);
	}

	public void testCopyDifferentMapTypes() throws Exception {
		Hashtable source = new Hashtable();
		source.put("foo", "bar");
		source.put("baz", new StringBuffer("blah"));
		HashMap dest = (HashMap) getConverter().convert(HashMap.class, source);
		HashMap sourceHashMap = new HashMap(source);
		TestUtils.assertEquals(dest, sourceHashMap);
	}

	public void testDeepCopy() throws Exception {
		HashMap source = new HashMap();
		source.put("foo", "bar");
		source.put("baz", new StringBuffer("blah"));
		MapCopier converter = new MapCopier();
		converter.setNestedTransformer(new TextConverter());
		HashMap dest = (HashMap) converter.convert(HashMap.class, source);
		assertSame(source.get("foo"), dest.get("foo"));
		assertNotSame(source.get("baz"), dest.get("baz"));
	}

}
