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
