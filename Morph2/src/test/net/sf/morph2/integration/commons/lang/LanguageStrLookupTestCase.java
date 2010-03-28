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
package net.sf.morph2.integration.commons.lang;

import java.util.HashMap;

import org.apache.commons.lang.text.StrSubstitutor;

import junit.framework.TestCase;

public class LanguageStrLookupTestCase extends TestCase {
	public void testMe() {
		HashMap map = new HashMap();
		map.put("string", "\"string\"");
		map.put("one", new Integer(1));
		map.put("array", new String[] { "foo", "bar", "baz" });
		StrSubstitutor ss = new StrSubstitutor(new LanguageStrLookup(map));
		assertEquals("\"string\"", ss.replace("${string}"));
		assertEquals("1", ss.replace("${one}"));
		assertEquals("foo", ss.replace("${array[0]}"));
		assertEquals("bar", ss.replace("${array[1]}"));
		assertEquals("baz", ss.replace("${array[2]}"));
	}
}
