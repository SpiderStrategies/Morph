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
import java.util.Date;
import java.util.List;

import net.sf.morph2.transform.Transformer;
import net.sf.morph2.util.TestUtils;

/**
 * @author Matt Sgarlata
 * @since Jan 2, 2005
 */
public class TextConverterTestCase extends BaseConverterTestCase {

	public List createInvalidDestinationClasses() throws Exception {
		List list = new ArrayList();
		list.add(Number.class);
		list.add(List.class);
		list.add(Date.class);
		list.add(null);
		return list;
	}

	public List createInvalidSources() throws Exception {
		return null;
	}

	public List createValidPairs() throws Exception {
		List list = new ArrayList();
		list.add(new ConvertedSourcePair("", new StringBuffer("")));
		list.add(new ConvertedSourcePair(new StringBuffer(""), ""));
		list.add(new ConvertedSourcePair("hello", new StringBuffer("hello")));
		list.add(new ConvertedSourcePair(new StringBuffer("hello"), "hello"));
		list.add(new ConvertedSourcePair(new Character('0'), "0"));
		list.add(new ConvertedSourcePair("0", new Character('0')));
		list.add(new ConvertedSourcePair(new Character('0'), new StringBuffer("0")));
		list.add(new ConvertedSourcePair(new StringBuffer("0"), new Character('0')));
		list.add(new ConvertedSourcePair(new byte[] {'f', 'o', 'o'}, "foo"));
		list.add(new ConvertedSourcePair(new char[] {'f', 'o', 'o'}, "foo"));
		return list;
	}

	public List createDestinationClasses() throws Exception {
		List list = new ArrayList();
		list.add(String.class);
		list.add(StringBuffer.class);
		return list;
	}

	protected Transformer createTransformer() {
		return new TextConverter(); 
	}
	
	public void testCharacterConversions() throws Exception {
		TestUtils.assertEquals(
			new Character('h'),
			getConverter().convert(Character.class, "hello"));
		TestUtils.assertEquals(
			new Character('h'),
			getConverter().convert(Character.class, new StringBuffer("hello")));
		TestUtils.assertEquals(
			null,
			getConverter().convert(Character.class, ""));
		TestUtils.assertEquals(
			null,
			getConverter().convert(Character.class, new StringBuffer("")));
		
		TestUtils.assertEquals(
			"h",
			getConverter().convert(String.class, new Character('h')));
		// an empty string should be converted to a null character
		assertEquals(null, getConverter().convert(Character.class, "", null));
	}
}
