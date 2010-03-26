/*
 * Copyright 2004-2005 the original author or authors.
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
package net.sf.morph2.transform.converters.totext;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import net.sf.morph2.transform.Transformer;
import net.sf.morph2.transform.converters.ContainerToPrettyTextConverter;
import net.sf.morph2.util.TestObjects;

/**
 * @author Matt Sgarlata
 * @since Jan 9, 2005
 */
public class ContainerToPrettyTextConverterTestCase extends BaseToTextConverterTestCase {

	public List createValidPairs() throws Exception {
		List list = new ArrayList();
		addContainerConversions(list);
		return list;
	}
	
	protected Transformer createTransformer() {
		return new ContainerToPrettyTextConverter();
	}

	public List createInvalidSources() throws Exception {
		return null;
	}
	
	
	
//	public List createValidSources() throws Exception {
//		CollectionReflectorTestCase collectionTestCase = new CollectionReflectorTestCase();
//		ArrayReflectorTestCase arrayTestCase = new ArrayReflectorTestCase();
//		TestObjects to = new TestObjects();
//		List list = new ArrayList();
//		list.addAll(collectionTestCase.createReflectableObjects());
//		list.addAll(arrayTestCase.createReflectableObjects());
//		return list;
//	}

	public List createInvalidSourceClasses() throws Exception {
		List list = new ArrayList();
		list.add(Long.class);
		list.add(Date.class);
		list.add(TestObjects.class);
		list.add(Object.class);
		return list;
	}
	
	public void testNullPrefixAndSuffix() {
		ContainerToPrettyTextConverter converter = new ContainerToPrettyTextConverter();
		converter.setPrefix(null);
		converter.setSuffix(null);
		String converted = (String) converter.convert(String.class, new int[] { 1, 2});
		assertEquals("1,2", converted);
	}
	
}
