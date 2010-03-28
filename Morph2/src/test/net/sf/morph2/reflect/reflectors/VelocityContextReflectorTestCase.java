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

import java.util.ArrayList;
import java.util.List;

import net.sf.morph2.reflect.Reflector;
import net.sf.morph2.util.TestClass;
import net.sf.morph2.util.TestObjects;

import org.apache.velocity.VelocityContext;

/**
 * @author Matt Sgarlata
 * @since Dec 29, 2004
 */
public class VelocityContextReflectorTestCase extends BaseReflectorTestCase {

	protected Reflector createReflector() {
		return new VelocityContextReflector();
	}

	protected List createReflectableObjects() {
		TestObjects testObjects = new TestObjects();
		
		List list = new ArrayList();
		list.add(new VelocityContext());
		list.add(new VelocityContext(testObjects.emptyMap));
		list.add(new VelocityContext(testObjects.singleElementMap));
		list.add(new VelocityContext(testObjects.multiElementMap));
		list.add(new VelocityContext(TestClass.getEmptyMap()));
		list.add(new VelocityContext(TestClass.getFullMap()));
		list.add(new VelocityContext(TestClass.getPartialMap()));
		return list;
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

}
