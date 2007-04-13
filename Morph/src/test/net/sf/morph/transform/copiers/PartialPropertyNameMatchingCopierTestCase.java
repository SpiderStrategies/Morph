/*
 * Copyright 2007 the original author or authors.
 *
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
package net.sf.morph.transform.copiers;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.morph.reflect.reflectors.ObjectReflector;
import net.sf.morph.transform.Copier;
import net.sf.morph.transform.TransformationException;
import net.sf.morph.transform.Transformer;
import net.sf.morph.transform.converters.BaseConverterTestCase;
import net.sf.morph.util.TestClass;
import net.sf.morph.util.TestUtils;

/**
 * @author Matt Sgarlata
 * @author Matt Benson
 */
public class PartialPropertyNameMatchingCopierTestCase extends BaseConverterTestCase {
	private static final String[] IGNORE_PROPERTIES = new String[] { "myMap", "anObject", "array", "numberArray" };

	private Copier copier;
	private ObjectReflector reflector = new ObjectReflector();

	public PartialPropertyNameMatchingCopierTestCase(String name) {
		super(name);
	}

	protected void setUp() throws Exception {
		super.setUp();
		copier = (Copier) transformer;
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
		doCopyTest1(ignoreProperties(TestClass.getEmptyObject()), ignoreKeys(TestClass.getEmptyMap()));
		doCopyTest1(ignoreProperties(TestClass.getPartialObject()), ignoreKeys(TestClass.getPartialMap()));
		doCopyTest1(ignoreProperties(TestClass.getFullObject()), ignoreKeys(TestClass.getFullMap()));

		doCopyTest2(ignoreProperties(TestClass.getEmptyObject()), ignoreKeys(TestClass.getEmptyMap()));
		doCopyTest2(ignoreProperties(TestClass.getPartialObject()), ignoreKeys(TestClass.getPartialMap()));
		doCopyTest2(ignoreProperties(TestClass.getFullObject()), ignoreKeys(TestClass.getFullMap()));

		doCopyTest3(ignoreProperties(TestClass.getEmptyObject()), ignoreKeys(TestClass.getEmptyMap()));
		doCopyTest3(ignoreProperties(TestClass.getPartialObject()), ignoreKeys(TestClass.getPartialMap()));
		try {
			doCopyTest3(ignoreProperties(TestClass.getFullObject()), ignoreKeys(TestClass.getFullMap()));
			fail("should fail because funkyArray is null");
		} catch (Exception e) { }

		doCopyTest4(ignoreProperties(TestClass.getEmptyObject()), ignoreKeys(TestClass.getEmptyMap()));
		doCopyTest4(ignoreProperties(TestClass.getPartialObject()), ignoreKeys(TestClass.getPartialMap()));
		try {
			doCopyTest4(ignoreProperties(TestClass.getFullObject()), ignoreKeys(TestClass.getFullMap()));
			fail("should fail because funkyArray is null");
		} catch (Exception e) { }
	}

	public List createInvalidDestinationClasses() throws Exception {
		return null;
	}

	public List createInvalidPairs() throws Exception {
		return null;
	}

	public List createInvalidSources() throws Exception {
		return null;
	}

	public List createValidPairs() throws Exception {
		return null;
	}

	public List createDestinationClasses() throws Exception {
		return Collections.singletonList(TestClass.class);
	}

	protected Transformer createTransformer() {
		PropertyNameMatchingCopier result = new PropertyNameMatchingCopier();
		result.setPropertiesToIgnore(IGNORE_PROPERTIES);
		return result;
	}

	private Map ignoreKeys(Map map) {
		for (int i = 0; i < IGNORE_PROPERTIES.length; i++) {
			map.remove(IGNORE_PROPERTIES[i]);
		}
		return map;
	}

	private Object ignoreProperties(Object o) {
		for (int i = 0; i < IGNORE_PROPERTIES.length; i++) {
			reflector.set(o, IGNORE_PROPERTIES[i], null);
		}
		return o;
	}
}
