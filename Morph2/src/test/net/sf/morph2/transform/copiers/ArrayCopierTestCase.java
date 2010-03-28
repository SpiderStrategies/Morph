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
package net.sf.morph2.transform.copiers;

import java.lang.reflect.Array;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.sf.composite.util.ArrayIterator;
import net.sf.morph2.transform.Transformer;
import net.sf.morph2.util.ContainerUtils;
import net.sf.morph2.util.IteratorEnumeration;
import net.sf.morph2.util.TestObjects;


/**
 * @author Matt Sgarlata
 * @since Nov 7, 2004
 */
public class ArrayCopierTestCase extends BaseCopierTestCase {

	public ArrayCopierTestCase() {
		super();
	}
	public ArrayCopierTestCase(String arg0) {
		super(arg0);
	}
	
	public List createInvalidDestinationClasses() throws Exception {
		List list = new ArrayList();
		list.add(Object.class);
		list.add(Map.class);
		list.add(Number.class);
		list.add(String.class);
		list.add(Array.class);
		list.add(this.getClass());
		list.add(Collection.class);
		list.add(HashMap.class);
		list.add(List.class);
		list.add(ArrayList.class);
		list.add(Hashtable.class);
		return list;
	}

	public List createInvalidPairs() throws Exception {
		List list = new ArrayList();
		list.add(new ConvertedSourcePair((new TestObjects()).singleElementObjectArray, (new TestObjects()).emptyObjectArray));
		
		list.add(new ConvertedSourcePair((new TestObjects()).multiElementObjectArray, (new TestObjects()).singleElementObjectArray));
		list.add(new ConvertedSourcePair((new TestObjects()).singleElementObjectArray, (new TestObjects()).multiElementObjectArray));
			
		list.add(new ConvertedSourcePair((new TestObjects()).singleElementLongArray, (new TestObjects()).emptyLongArray));
		list.add(new ConvertedSourcePair((new TestObjects()).multiElementLongArray, (new TestObjects()).singleElementLongArray));
		list.add(new ConvertedSourcePair((new TestObjects()).singleElementLongArray, (new TestObjects()).multiElementLongArray));
		return list;
	}
	
	public void testRuntimeArrayType() {
		Object converted = getConverter().convert(Integer[].class, (new TestObjects()).oneTwoThreeNumberArray);
		assertEquals(Integer[].class, converted.getClass());
		
		converted = getConverter().convert(Integer[].class, (new TestObjects()).oneTwoThreeObjectArray);
		assertEquals(Integer[].class, converted.getClass());
		
		List list = new ArrayList();
		list.add(new Integer(1));
		list.add(new Integer(2));
		list.add(new BigDecimal(3));
		
		converted = getConverter().convert(Integer[].class, list);
		assertEquals(Integer[].class, converted.getClass());
	}

	// there aren't really any invalid sources 
	public List createInvalidSources() throws Exception {
		return null;
	}

	public List createValidPairs() throws Exception {
		List pairs = new ArrayList();
		
		pairs.add(new ConvertedSourcePair((new TestObjects()).multidimensionalPrimitiveArray, (new TestObjects()).multidimensionalObjectArray));
		//this isn't a valid test, because there's no way for primitives to be magically converted to random different data types like BigDecimal, Double, etc.
		//pairs.add(new ConvertedSourcePair((new TestObjects()).multidimensionalObjectArray, (new TestObjects()).multidimensionalPrimitiveArray));
		pairs.add(new ConvertedSourcePair((new TestObjects()).multidimensionalPrimitiveArray, (new TestObjects()).multidimensionalPrimitiveArray));
		pairs.add(new ConvertedSourcePair((new TestObjects()).multidimensionalObjectArray, (new TestObjects()).multidimensionalObjectArray));

		pairs.add(new ConvertedSourcePair((new TestObjects()).emptyObjectArray, (new TestObjects()).emptyObjectArray));
		pairs.add(new ConvertedSourcePair((new TestObjects()).singleElementObjectArray, (new TestObjects()).singleElementObjectArray));
		pairs.add(new ConvertedSourcePair((new TestObjects()).multiElementObjectArray, (new TestObjects()).multiElementObjectArray));
			
		pairs.add(new ConvertedSourcePair((new TestObjects()).emptyLongArray, (new TestObjects()).emptyLongArray));
		pairs.add(new ConvertedSourcePair((new TestObjects()).singleElementLongArray, (new TestObjects()).singleElementLongArray));
		pairs.add(new ConvertedSourcePair((new TestObjects()).multiElementLongArray, (new TestObjects()).multiElementLongArray));
			
		pairs.add(new ConvertedSourcePair((new TestObjects()).oneTwoThreeNumberArray, (new TestObjects()).oneTwoThreeNumberArray));
		pairs.add(new ConvertedSourcePair((new TestObjects()).oneTwoThreeObjectArray, (new TestObjects()).oneTwoThreeObjectArray));
		pairs.add(new ConvertedSourcePair((new TestObjects()).oneTwoThreeObjectArray, (new TestObjects()).oneTwoThreeList));
		pairs.add(new ConvertedSourcePair((new TestObjects()).oneTwoThreeObjectArray, new ArrayIterator((new TestObjects()).oneTwoThreeObjectArray)));
		pairs.add(new ConvertedSourcePair((new TestObjects()).oneTwoThreeObjectArray, new IteratorEnumeration(new ArrayIterator((new TestObjects()).oneTwoThreeObjectArray))));
		pairs.addAll(getMapConvertedSourcePairs());
		
		Date now = new Date();
		pairs.add(new ConvertedSourcePair(new Object[] { now }, now));
		pairs.add(new ConvertedSourcePair(new Object[] { new Long(4) }, new Long(4)));
		pairs.add(new ConvertedSourcePair(new Object[] { "test" }, "test"));

		return pairs;
	}
	
	public void doTestSet(Set set) {
		Object converted = getConverter().convert(Object[].class,
			(new TestObjects()).oneTwoThreeSet, null);
		assertNotNull(converted);
		assertTrue(converted instanceof Object[]);
		Object[] array = (Object[]) converted;
		assertEquals(array.length, 3);
		assertTrue(ContainerUtils.contains(array, (new TestObjects()).one));
		assertTrue(ContainerUtils.contains(array, (new TestObjects()).two));
		assertTrue(ContainerUtils.contains(array, (new TestObjects()).three));
	}

	public void testSet() {
		doTestSet((new TestObjects()).oneTwoThreeSet);
	}
	
	public List createDestinationClasses() throws Exception {
		List list = new ArrayList();
		list.add(Object[].class);
		list.add(long[].class);
		list.add(long[][].class);
		list.add(int[].class);
		list.add(char[].class);
		list.add(boolean[].class);
		list.add(byte[].class);
		list.add(float[].class);
		list.add(double[].class);
		list.add(short[].class);
		list.add(boolean[].class);
		return list;
	}

	public Transformer createTransformer() {
		return new ArrayCopier();
	}
	
	protected List getMapConvertedSourcePairs() {
		List list = new ArrayList();
//		list.add(new ConvertedSourcePair(oneTwoThreeObjectArray, oneTwoThreeMap));
		list.add(new ConvertedSourcePair((new TestObjects()).emptyObjectArray, (new TestObjects()).emptyMap));
		list.add(new ConvertedSourcePair((new TestObjects()).singleElementObjectArray, (new TestObjects()).singleElementMap));
		list.add(new ConvertedSourcePair((new TestObjects()).multiElementObjectArray, (new TestObjects()).multiElementMap));
		return list;
	}
	
	public void testMapConversion() {
		doTestSet(new HashSet((new TestObjects()).oneTwoThreeMap.values()));
	}
}
