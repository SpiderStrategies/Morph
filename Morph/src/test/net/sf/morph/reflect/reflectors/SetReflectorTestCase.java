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

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import net.sf.morph.reflect.Reflector;

/**
 * @author Matt Sgarlata
 * @since Nov 21, 2004
 */
public class SetReflectorTestCase extends CollectionReflectorTestCase {

	protected Reflector createReflector() {
		return new SetReflector();
	}

	public List createReflectableObjects() {
		Set set1 = new HashSet();
		Set set2 = new HashSet();
		set2.add("one");
		set2.add("two");

		List reflectable = new ArrayList();
		reflectable.add(set1);
		reflectable.add(set2);
		return reflectable;
	}

	public void testNewInstance() {
		// no exception should be thrown
		getInstantiatingReflector().newInstance(Set.class);
		getInstantiatingReflector().newInstance(HashSet.class);
	}
	
}
