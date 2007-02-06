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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
public class CumulativeCopierTestCase extends BaseConverterTestCase {
	private static final String[] PROPERTIES = new String[] { "myMap", "anObject", "array", "numberArray" };

	private Copier copier;

	public CumulativeCopierTestCase(String name) {
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
		} catch (TransformationException e) {
			assertTrue(e.getCause().getMessage().contains("funkyArray"));
		}

		doCopyTest4(TestClass.getEmptyObject(), TestClass.getEmptyMap());
		doCopyTest4(TestClass.getPartialObject(), TestClass.getPartialMap());
		try {
			doCopyTest4(TestClass.getFullObject(), TestClass.getFullMap());
			fail("should fail because funkyArray is null");
		} catch (TransformationException e) {
			assertTrue(e.getCause().getMessage().contains("funkyArray"));
		}
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
		List list = new ArrayList();
		list.add(TestClass.class);
		return list;
	}

	protected Transformer createTransformer() {
		PropertyNameMatchingCopier[] components = new PropertyNameMatchingCopier[2];
		components[0] = new PropertyNameMatchingCopier();
		components[0].setPropertiesToCopy(PROPERTIES);
		components[1] = new PropertyNameMatchingCopier();
		components[1].setPropertiesToIgnore(PROPERTIES);
		CumulativeCopier result = new CumulativeCopier();
		result.setComponents(components);
		return result;
	}
}
