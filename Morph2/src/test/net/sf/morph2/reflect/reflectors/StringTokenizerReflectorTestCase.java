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
package net.sf.morph2.reflect.reflectors;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;
import java.util.Vector;

import net.sf.morph2.Morph;
import net.sf.morph2.reflect.ReflectionException;
import net.sf.morph2.reflect.Reflector;
import net.sf.morph2.util.MorphStringTokenizer;

/**
 * 
 * @author Matt Sgarlata
 * @since Apr 10, 2007
 */
public class StringTokenizerReflectorTestCase extends BaseReflectorTestCase {

	protected List createNonReflectableObjects() {
		List list = new ArrayList();
		list.add(new Vector().elements());
		list.add(new ArrayList().iterator());
		list.add(new ArrayList());
		list.add("");
		return list;
	}

	protected List createReflectableObjects() {
		List list = new ArrayList();
		list.add(new StringTokenizer("test"));
		list.add(new MorphStringTokenizer("test"));
		return list;
	}

	protected Reflector createReflector() {
		return new StringTokenizerReflector();
	}
	
	public void testGetSize2() {
		try {
			getSizableReflector().getSize(null);
			fail("Exception should be thrown when retrieving the size of a null object");
		}
		catch (ReflectionException e) {
			// this is the expected behavior
		}
		assertEquals(0, Morph.getSize(new StringTokenizer("")));
		assertEquals(1, Morph.getSize(new StringTokenizer("1")));
		assertEquals(2, Morph.getSize(new StringTokenizer("two words")));
	}

}
