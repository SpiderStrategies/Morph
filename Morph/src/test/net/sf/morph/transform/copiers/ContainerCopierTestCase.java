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
package net.sf.morph.transform.copiers;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import javax.servlet.http.HttpSession;

import net.sf.morph.transform.Transformer;
import net.sf.morph.util.TestObjects;

/**
 * @author Matt Sgarlata
 * @since Dec 24, 2004
 */
public class ContainerCopierTestCase extends BaseCopierTestCase {

	public List createInvalidDestinationClasses() throws Exception {
		TestObjects to = new TestObjects();
		List list = new ArrayList();
		list.add(HttpSession.class);
		list.add(Object.class);
		list.add(getClass());
		list.add(Integer.class);
		list.add(ResultSet.class);
		return list;
	}

	public List createInvalidSources() throws Exception {
		return null;
	}

	public List createValidPairs() throws Exception {
		TestObjects to = new TestObjects();
		List list = new ArrayList();
		list.add(new ConvertedSourcePair(to.emptyList, to.emptyMap));
		list.add(new ConvertedSourcePair(to.emptyVector, to.emptyMap));
		list.add(new ConvertedSourcePair(to.emptyVector, to.emptyObjectArray));
		list.add(new ConvertedSourcePair(to.emptyList, to.emptyPrimitiveArray));
		list.add(new ConvertedSourcePair(to.emptyPrimitiveArray, to.emptyList));
		list.add(new ConvertedSourcePair(to.multidimensionalLongArray, to.multidimensionalObjectArray));
		list.add(new ConvertedSourcePair(to.multidimensionalLongArray, to.multidimensionalPrimitiveArray));
		list.add(new ConvertedSourcePair(to.multidimensionalPrimitiveArray, to.multidimensionalLongArray));
		
		list.add(new ConvertedSourcePair(to.oneTwoThreeList.iterator(), to.oneTwoThreeList));
		list.add(new ConvertedSourcePair(to.oneTwoThreeList, to.oneTwoThreeMap));
		list.add(new ConvertedSourcePair(to.oneTwoThreeList, to.oneTwoThreeList));
		list.add(new ConvertedSourcePair(to.oneTwoThreeObjectArray, to.oneTwoThreeList));
		list.add(new ConvertedSourcePair(to.oneTwoThreeNumberArray, to.oneTwoThreeList));
		list.add(new ConvertedSourcePair(to.oneTwoThreeSet, to.oneTwoThreeList));
		list.add(new ConvertedSourcePair(to.oneTwoThreeVector, to.oneTwoThreeList));
		
		list.add(new ConvertedSourcePair(to.emptyList.iterator(), to.emptyVector.elements()));
		list.add(new ConvertedSourcePair(to.emptyVector.elements(), to.emptyList.iterator()));
		list.add(new ConvertedSourcePair(to.oneTwoThreeList.iterator(), to.oneTwoThreeVector.elements()));
		list.add(new ConvertedSourcePair(to.oneTwoThreeVector.elements(), to.oneTwoThreeList.iterator()));		
		
		return list;
	}

	public List createDestinationClasses() throws Exception {
		List list = new ArrayList();
		list.add(Set.class);
		list.add(TreeSet.class);
		list.add(SortedSet.class);
		list.add(Object[].class);
		list.add(long[].class);
		list.add(List.class);
		list.add(ArrayList.class);
		list.add(LinkedList.class);
		list.add(Iterator.class);
		list.add(Enumeration.class);
		list.add(Collection.class);
		list.add(Map.class);
		return list;
	}

	protected Transformer createTransformer() {
		return new ContainerCopier();
	}

}