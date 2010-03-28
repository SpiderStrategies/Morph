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
package net.sf.morph2.reflect.reflectors;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import net.sf.composite.util.ArrayIterator;
import net.sf.morph2.reflect.Reflector;
import net.sf.morph2.util.TestObjects;

/**
 * @author Matt Sgarlata
 * @since Dec 21, 2004
 */
public class IteratorReflectorTestCase extends BaseReflectorTestCase {

	protected Reflector createReflector() {
		return new IteratorReflector();
	}

	protected List createReflectableObjects() {
		List list = new ArrayList();
		list.add((new TestObjects()).oneTwoThreeList.iterator());
		list.add((new TestObjects()).oneTwoThreeSet.iterator());
		list.add(new ArrayIterator((new TestObjects()).multidimensionalPrimitiveArray));
		list.add(new ArrayIterator((new TestObjects()).oneTwoThreeObjectArray));
		list.add((new TestObjects()).oneTwoThreeMap.keySet().iterator());
		list.add((new TestObjects()).emptyList.iterator());
		return list;
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

}
