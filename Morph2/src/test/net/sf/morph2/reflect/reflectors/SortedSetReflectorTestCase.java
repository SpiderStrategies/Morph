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
package net.sf.morph2.reflect.reflectors;

import java.util.ArrayList;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

import net.sf.morph2.reflect.Reflector;

/**
 * @author Matt Sgarlata
 * @since Nov 21, 2004
 */
public class SortedSetReflectorTestCase extends CollectionReflectorTestCase {

	public List createReflectableObjects() {
		SortedSet sortedSet1 = new TreeSet();
		SortedSet sortedSet2 = new TreeSet();
		sortedSet2.add("one");
		sortedSet2.add("two");

		List reflectable = new ArrayList();
		reflectable.add(sortedSet1);
		reflectable.add(sortedSet2);
		
		return reflectable;
	}

	protected Reflector createReflector() {
		return new SortedSetReflector();
	}
	
	public void testNewInstance() {
		// no exception should be thrown
		getInstantiatingReflector().newInstance(SortedSet.class, null);
		getInstantiatingReflector().newInstance(TreeSet.class, null);
	}
	
}
