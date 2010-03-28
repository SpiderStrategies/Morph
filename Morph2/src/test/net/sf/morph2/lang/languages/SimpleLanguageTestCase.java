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
package net.sf.morph2.lang.languages;

import java.math.BigDecimal;

import net.sf.morph2.lang.Language;
import net.sf.morph2.util.TestClass;
import net.sf.morph2.util.TestUtils;

/**
 * TODO need to test set methods
 * 
 * @author Matt Sgarlata
 * @since Dec 4, 2004
 */
public class SimpleLanguageTestCase extends BaseLanguageTestCase {
	
	protected Language createLanguage() {
		return new LanguageDecorator();
	}
	
	public void doTestSimpleGetsWith(Object obj) {
		TestUtils.assertEquals(language.get(obj, "anObject"), new Long(14));
		TestUtils.assertEquals(language.get(obj, "myInteger"), new Integer(4));
		TestUtils.assertEquals(language.get(obj, "myMap"), TestClass.getMyMapProperty());
		TestUtils.assertEquals(language.get(obj, "myLongValue"), new Long(13));
		TestUtils.assertEquals(language.get(obj, "array"), new Object[] { "hi" });
		TestUtils.assertEquals(language.get(obj, "bigDecimal"), new BigDecimal(3.5));
		TestUtils.assertEquals(language.get(obj, "numberArray"), new Number[] { new Integer(1), new Long(2) });
		TestUtils.assertEquals(language.get(obj, "string"), "string");
	}
	
	public void testSimpleGets() {
		doTestSimpleGetsWith(TestClass.getFullObject());
		doTestSimpleGetsWith(TestClass.getFullMap());
	}
	
	public void doTestComplexGetsWith(Object obj) {
		TestUtils.assertEquals(language.get(obj, "myMap.one"), new Integer(1));
		TestUtils.assertEquals(language.get(obj, "myMap.two"), new Object[] { new Integer(1), new BigDecimal(2) });
		TestUtils.assertEquals(language.get(obj, "myMap.two.0"), new Integer(1));
		TestUtils.assertEquals(language.get(obj, "myMap.two.1"), new BigDecimal(2));
		TestUtils.assertEquals(language.get(obj, "myMap('two')[0]"), new Integer(1));
		TestUtils.assertEquals(language.get(obj, "myMap.two[1]"), new BigDecimal(2));		
		TestUtils.assertEquals(language.get(obj, "numberArray[0]"), new Integer(1));
		TestUtils.assertEquals(language.get(obj, "numberArray [ 1 ] "), new Long(2));
		TestUtils.assertEquals(language.get(obj, "numberArray(1)"), new Long(2));
		TestUtils.assertEquals(language.get(obj, "numberArray.1"), new Long(2));
	}
	
	public void testComplexGets() {
		doTestComplexGetsWith(TestClass.getFullObject());
		doTestComplexGetsWith(TestClass.getFullMap());
	}
	
}
