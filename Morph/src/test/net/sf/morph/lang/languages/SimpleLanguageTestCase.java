package net.sf.morph.lang.languages;

import java.math.BigDecimal;

import net.sf.morph.lang.Language;
import net.sf.morph.util.TestClass;
import net.sf.morph.util.TestUtils;

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