package net.sf.morph2.transform.copiers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.morph2.Defaults;
import net.sf.morph2.transform.Copier;
import net.sf.morph2.transform.Transformer;
import net.sf.morph2.transform.converters.NumberConverterTestCase;
import net.sf.morph2.transform.converters.NumberToTimeConverterTestCase;
import net.sf.morph2.transform.converters.ObjectToClassConverterTestCase;
import net.sf.morph2.transform.converters.TextConverterTestCase;
import net.sf.morph2.transform.converters.TextToNumberConverterTestCase;
import net.sf.morph2.transform.converters.TextToTimeConverterTestCase;
import net.sf.morph2.transform.converters.TimeConverterTestCase;
import net.sf.morph2.transform.converters.TimeToNumberConverterTestCase;
import net.sf.morph2.transform.converters.toboolean.DefaultToBooleanConverterTestCase;
import net.sf.morph2.transform.converters.totext.DefaultToTextConverterTestCase;
import net.sf.morph2.util.TestClass;
import net.sf.morph2.util.TestObjects;
import net.sf.morph2.util.TestUtils;

/**
 * @author Matt Sgarlata
 * @since Feb 22, 2005
 */
public class DelegatingCopierTestCase extends BaseCopierTestCase {
	private Copier copier;

	protected void setUp() throws Exception {
		super.setUp();
		copier = (Copier) transformer;
	}

	protected Transformer createTransformer() {
		return Defaults.createCopier();
	}

	public List createInvalidDestinationClasses() throws Exception {
		return null;
	}

	public List createInvalidSources() throws Exception {
		List list = new ArrayList();
		list.add(null);
		return list;
	}

	public List createValidPairs() throws Exception {
		ArrayList result = new ArrayList();
		result.addAll(new ArrayCopierTestCase().createValidPairs());
		result.addAll(new DefaultToBooleanConverterTestCase().createValidPairs());
		result.addAll(new ObjectToClassConverterTestCase().createValidPairs());
		result.addAll(new TextConverterTestCase().createValidPairs());
		result.addAll(new DefaultToTextConverterTestCase().createValidPairs());
		result.addAll(new TextToNumberConverterTestCase().createValidPairs());
		result.addAll(new TextToTimeConverterTestCase().createValidPairs());
		result.addAll(new NumberToTimeConverterTestCase().createValidPairs());
		result.addAll(new TimeToNumberConverterTestCase().createValidPairs());
		result.addAll(new NumberConverterTestCase().createValidPairs());
		result.addAll(new TimeConverterTestCase().createValidPairs());

		//a bunch of container stuff, minus interfaces and enumerations:
		TestObjects to = new TestObjects();
		result.add(new ConvertedSourcePair(to.emptyList, to.emptyMap));
		result.add(new ConvertedSourcePair(to.emptyVector, to.emptyMap));
		result.add(new ConvertedSourcePair(to.emptyVector, to.emptyObjectArray));
		result.add(new ConvertedSourcePair(to.emptyList, to.emptyPrimitiveArray));
		result.add(new ConvertedSourcePair(to.emptyPrimitiveArray, to.emptyList));
		result.add(new ConvertedSourcePair(to.multidimensionalLongArray, to.multidimensionalObjectArray));
		result.add(new ConvertedSourcePair(to.multidimensionalLongArray, to.multidimensionalPrimitiveArray));
		result.add(new ConvertedSourcePair(to.multidimensionalPrimitiveArray, to.multidimensionalLongArray));

		result.add(new ConvertedSourcePair(to.oneTwoThreeList, to.oneTwoThreeMap));
		result.add(new ConvertedSourcePair(to.oneTwoThreeList, to.oneTwoThreeList));
		result.add(new ConvertedSourcePair(to.oneTwoThreeObjectArray, to.oneTwoThreeList));
		result.add(new ConvertedSourcePair(to.oneTwoThreeNumberArray, to.oneTwoThreeList));
		result.add(new ConvertedSourcePair(to.oneTwoThreeSet, to.oneTwoThreeList));
		result.add(new ConvertedSourcePair(to.oneTwoThreeVector, to.oneTwoThreeList));

		return result;
	}

	public List createDestinationClasses() throws Exception {
		return null;
	}

	private void doCopyTest1(Object object, Map map) {
		Map generatedMap = new HashMap();
		copier.copy(generatedMap, object, null);
		generatedMap.remove("class");
		TestUtils.assertEquals(generatedMap, map);
	}
	
	private void doCopyTest2(Object object, Map map) {
		Map generatedMap = new HashMap();
		copier.copy(generatedMap, map, null);
		TestUtils.assertEquals(generatedMap, map);	
	}
	
	private void doCopyTest3(Object object, Map map) {
		Object generatedObject = new TestClass();
		copier.copy(generatedObject, object, null);
		TestUtils.assertEquals(generatedObject, object);
	}
	
	private void doCopyTest4(Object object, Map map) {
		Object generatedObject = new TestClass();
		copier.copy(generatedObject, map, null);
		TestUtils.assertEquals(generatedObject, object);
	}
	
	public void testCopy() {
		doCopyTest1(TestClass.getEmptyObject(), TestClass.getEmptyMap());
		doCopyTest1(TestClass.getPartialObject(), TestClass.getPartialMap());
		doCopyTest1(TestClass.getFullObject(), TestClass.getFullMap());

		doCopyTest2(TestClass.getEmptyObject(), TestClass.getEmptyMap());
		doCopyTest2(TestClass.getPartialObject(), TestClass.getPartialMap());
		doCopyTest2(TestClass.getFullObject(), TestClass.getFullMap());

		doCopyTest3(TestClass.getEmptyObject(), TestClass.getEmptyMap());
		doCopyTest3(TestClass.getPartialObject(), TestClass.getPartialMap());
		try {
			doCopyTest3(TestClass.getFullObject(), TestClass.getFullMap());
			fail("should fail because funkyArray is null");
		} catch (Exception e) { }

		doCopyTest4(TestClass.getEmptyObject(), TestClass.getEmptyMap());
		doCopyTest4(TestClass.getPartialObject(), TestClass.getPartialMap());
		try {
			doCopyTest4(TestClass.getFullObject(), TestClass.getFullMap());
			fail("should fail because funkyArray is null");
		} catch (Exception e) { }
	}

	public void testCopyArrayToMap() {
		String[] s = new String[] { "foo", "bar", "baz" };
		Map m = new HashMap();
		for (int i = 0; i < s.length; i++) {
			m.put(Integer.toString(i), s[i]);
		}
		doCopyTest1(s, m);
	}

}
