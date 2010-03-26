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

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import net.sf.morph2.reflect.Reflector;
import net.sf.morph2.util.TestObjects;

/**
 * @author Matt Sgarlata
 * @since Dec 12, 2004
 */
public class ArrayReflectorTestCase extends BaseReflectorTestCase {

	protected Reflector createReflector() {
		return new ArrayReflector();
	}

	public List createReflectableObjects() {
		List list = new ArrayList();
		list.add((new TestObjects()).emptyObjectArray);
		list.add((new TestObjects()).singleElementObjectArray);
		list.add((new TestObjects()).multiElementObjectArray);
		list.add((new TestObjects()).emptyLongArray);
		list.add((new TestObjects()).singleElementLongArray);
		list.add((new TestObjects()).oneTwoThreeNumberArray);
		
		list.add((new TestObjects()).emptyPrimitiveArray);
		list.add((new TestObjects()).multiElementEmptyPrimitiveArray);
		list.add((new TestObjects()).singleElementPrimitiveArray);
		list.add((new TestObjects()).multiElementPrimitiveArray);
		list.add((new TestObjects()).multidimensionalPrimitiveArray);
		
		return list;
	}

	protected List createNonReflectableObjects() {
		List list = new ArrayList();
		list.add(new Object());
		list.add(new BigDecimal(2));
		list.add(new Integer(1));
		list.add("hi");
		list.add(this);
		return list;
	}

}
