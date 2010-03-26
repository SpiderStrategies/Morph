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
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.Vector;

import net.sf.morph2.reflect.Reflector;

/**
 * @author Matt Sgarlata
 * @since Nov 21, 2004
 */
public class CollectionReflectorTestCase extends
	BaseReflectorTestCase {

	public CollectionReflectorTestCase() {
		super();
	}
	public CollectionReflectorTestCase(String arg0) {
		super(arg0);
	}

	protected Reflector createReflector() {
		return new CollectionReflector();
	}

	protected List createNonReflectableObjects() {
		List list = new ArrayList();
		list.add(new Integer(2));
		list.add(new Long(4));
		list.add("something");
		list.add(new Object[] { "something" });
		list.add(new int[] { 1 });
		return list;
	}

	public List createReflectableObjects() {
		SortedSet sortedSet1 = new TreeSet();
		SortedSet sortedSet2 = new TreeSet();
		sortedSet2.add("one");
		sortedSet2.add("two");

		List arrayList1 = new ArrayList();
		List arrayList2 = new ArrayList();
		arrayList2.add("one");
		arrayList2.add("two");
		
		List linkedList1 = new LinkedList();
		List linkedList2 = new LinkedList();
		linkedList2.add(new Integer(1));
		linkedList2.add(new Integer(2));
		
		List vector1 = new Vector();
		List vector2 = new Vector();
		vector1.add(new Long(1));
		vector2.add(new Long(2));
		
		Set set1 = new HashSet();
		Set set2 = new HashSet();
		set2.add("one");
		set2.add("two");

		List reflectable = new ArrayList();
		reflectable.add(sortedSet1);
		reflectable.add(sortedSet2);
		reflectable.add(arrayList1);
		reflectable.add(arrayList2);
		reflectable.add(linkedList1);
		reflectable.add(linkedList2);
		reflectable.add(vector1);
		reflectable.add(vector2);
		reflectable.add(set1);
		reflectable.add(set2);

		return reflectable;
	}
	public void testNewInstance() {
		// no exception should be thrown
		getInstantiatingReflector().newInstance(Collection.class, null);
		getInstantiatingReflector().newInstance(ArrayList.class, null);
	}
	

}
