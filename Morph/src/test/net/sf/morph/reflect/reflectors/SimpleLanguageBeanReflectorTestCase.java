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
package net.sf.morph.reflect.reflectors;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import net.sf.composite.util.ObjectPair;
import net.sf.morph.reflect.Reflector;
import net.sf.morph.util.TestClass;
import net.sf.morph.util.TestUtils;

public class SimpleLanguageBeanReflectorTestCase extends ObjectReflectorTestCase {
	protected Reflector createReflector() {
		return new SimpleLanguageBeanReflector();
	}

	public void testComplexGettersAndSetters() {
		TestClass test = new TestClass();
		Map map = Collections.singletonMap("test", test);
		getBeanReflector().set(map, "test.anObject", new Long(13));
		assertEquals(test.getAnObject(), new Long(13));
		assertEquals(getBeanReflector().get(map, "[test].anObject"), new Long(13));

		getBeanReflector().set(map, "['test'].myInteger", new Integer(6));
		assertEquals(new Integer(test.getMyInteger()), new Integer(6));
		assertEquals(getBeanReflector().get(map, "test.myInteger"), new Integer(6));

		Number[] modifiedNumberArray = { new BigDecimal(6.2), new Long(2) };
		test.funkyArray = new Number[2];
		getBeanReflector().set(map, "[\"test\"].funkyArray", modifiedNumberArray);
		TestUtils.assertEquals(modifiedNumberArray, test.funkyArray);
		TestUtils.assertEquals(modifiedNumberArray, getBeanReflector().get(map, "test.funkyArray"));
	}

	public void testComplexIsWriteable() {
		List list = Collections.singletonList(new TestClass());
		assertTrue(getBeanReflector().isWriteable(list, "0.string"));
		assertTrue(getBeanReflector().isWriteable(list, "[0]myInteger"));
		assertTrue(getBeanReflector().isWriteable(list, "[0].myLongValue"));
		assertTrue(getBeanReflector().isWriteable(list, "['0'].myMap"));
		assertTrue(getBeanReflector().isWriteable(list, "(0)anObject"));
		assertTrue(getBeanReflector().isWriteable(list, "(0).array"));
		assertTrue(getBeanReflector().isWriteable(list, "[0].numberArray"));
		assertTrue(getBeanReflector().isWriteable(list, "[0].bigDecimal"));
		assertTrue(getBeanReflector().isWriteable(list, "[0].funkyArray"));
		assertFalse(getBeanReflector().isWriteable(list, "[0].methodThatIsNotAProperty"));
		assertFalse(getBeanReflector().isWriteable(list, "[0].methodThatIsNotAProperty2"));
		assertFalse(getBeanReflector().isWriteable(list, "[0].invalidProperty"));
	}

	public void testComplexIsReadable() {
		TestClass[] array = new TestClass[] { new TestClass() };
		assertTrue(getBeanReflector().isReadable(array, "0.string"));
		assertTrue(getBeanReflector().isReadable(array, "[0]myInteger"));
		assertTrue(getBeanReflector().isReadable(array, "[0].myLongValue"));
		assertTrue(getBeanReflector().isReadable(array, "['0']myMap"));
		assertTrue(getBeanReflector().isReadable(array, "\"0\"anObject"));
		assertTrue(getBeanReflector().isReadable(array, "(0)array"));
		assertTrue(getBeanReflector().isReadable(array, "(0)..numberArray"));
		assertTrue(getBeanReflector().isReadable(array, "0.bigDecimal"));
		assertTrue(getBeanReflector().isReadable(array, "0.funkyArray"));
		assertFalse(getBeanReflector().isReadable(array, "0.methodThatIsNotAProperty"));
		assertFalse(getBeanReflector().isReadable(array, "0.methodThatIsNotAProperty2"));
		assertFalse(getBeanReflector().isReadable(array, "0.invalidProperty"));
	}

	public void testComplexGetType() {
		ObjectPair pair = new ObjectPair(null, new TestClass());
		assertSame(getBeanReflector().getType(pair, "object2]string"), String.class);
		assertSame(getBeanReflector().getType(pair, "object2.myInteger"), int.class);
		assertSame(getBeanReflector().getType(pair, "'object2'myLongValue"), Long.class);
		assertSame(getBeanReflector().getType(pair, "object2.myMap"), Map.class);
		assertSame(getBeanReflector().getType(pair, "object2.anObject"), Object.class);
		assertSame(getBeanReflector().getType(pair, "object2.array"), Object[].class);
		assertSame(getBeanReflector().getType(pair, "object2.numberArray"), Number[].class);
		assertSame(getBeanReflector().getType(pair, "object2.bigDecimal"), BigDecimal.class);
		assertSame(getBeanReflector().getType(pair, "object2.funkyArray"), Number[].class);
	}

}
