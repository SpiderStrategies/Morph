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
package net.sf.morph.reflect.reflectors;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

import net.sf.morph.util.TestClass;

/**
 * TODO need to test the map's container behavior (currently I think only the
 * bean behavior is tested)
 * 
 * @author Matt Sgarlata
 * @since Nov 20, 2004
 */
public abstract class BaseMapReflectorTestCase extends BaseReflectorTestCase {

	protected List createImplicitPropertyNames() {
		List list = super.createImplicitPropertyNames();
		list.add(MapReflector.IMPLICIT_PROPERTY_KEYS);
		list.add(MapReflector.IMPLICIT_PROPERTY_VALUES);
		list.add(MapReflector.IMPLICIT_PROPERTY_ENTRIES);
		return list;
	}
	
	public void testGettersAndSetters() {
		Map test = new HashMap();
		
		getBeanReflector().set(test, "anObject", new Long(13));
		assertEquals(test.get("anObject"), new Long(13));
		assertEquals(getBeanReflector().get(test, "anObject"), new Long(13));
		
		getBeanReflector().set(test, "myInteger", new Integer(6));
		assertEquals(test.get("myInteger"), new Integer(6));
		assertEquals(getBeanReflector().get(test, "myInteger"), new Integer(6));
		
		// should be able to define a new property on-the-fly
		getBeanReflector().set(test, "newProperty", new Integer(6));
		assertEquals(test.get("myInteger"), new Integer(6));
		assertEquals(getBeanReflector().get(test, "myInteger"), new Integer(6));
	}

	protected List createNonReflectableObjects() {
		List beans = new ArrayList();
		beans.add(new ArrayList());
		beans.add(new HashSet());
		beans.add(new Object[0]);
		beans.add(new BigDecimal(3));
		beans.add(new Object());
		return beans;
	}

	protected List createReflectableObjects() {
		List beans = new ArrayList();
		beans.add(TestClass.getEmptyMap());
		beans.add(TestClass.getFullMap());
		beans.add(TestClass.getPartialMap());
		beans.add(new HashMap());
		beans.add(new TreeMap());
		return beans;
	}

	public void testNewInstance() {
		getInstantiatingReflector().newInstance(Map.class);
		getInstantiatingReflector().newInstance(SortedMap.class);
		getInstantiatingReflector().newInstance(HashMap.class);
	}
}
