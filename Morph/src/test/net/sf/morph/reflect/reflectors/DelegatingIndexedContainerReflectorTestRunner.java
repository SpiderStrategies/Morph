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

import java.util.List;

import junit.framework.Test;
import junit.framework.TestSuite;
import junit.textui.TestRunner;
import net.sf.morph.reflect.Reflector;

/**
 * @author Matt Sgarlata
 * @since Dec 14, 2004
 */
public class DelegatingIndexedContainerReflectorTestRunner extends TestRunner {

	public static Test suite() {
		TestSuite suite = new TestSuite();

		suite.addTestSuite(DicrArrayIndexedContainerReflectorTestCase.class);
		suite.addTestSuite(DicrSortedSetIndexedContainerReflectorTestCase.class);
		suite.addTestSuite(DicrListIndexedContainerReflectorTestCase.class);
		
		return suite;
	}
	
	public static class DicrArrayIndexedContainerReflectorTestCase extends ArrayReflectorTestCase {
		
		public DicrArrayIndexedContainerReflectorTestCase() {
			super();
		
		}
		protected Reflector createReflector() {
			return new SimpleDelegatingReflector();
		}
		protected List createNonReflectableObjects() {
			return null;
		}
	}

	public static class DicrSortedSetIndexedContainerReflectorTestCase extends SortedSetReflectorTestCase {
		
		public DicrSortedSetIndexedContainerReflectorTestCase() {
			super();
		}
		
//		protected List createNonReflectableObjects() {
//			List list = super.createNonReflectableObjects();
////			System.out.println("BEFORE: " + ObjectUtils.getObjectDescription(list));
//			for (int i=0; i<list.size(); i++) {
//				if (list.get(i).getClass().isArray() || list.get(i) instanceof List) {
//					list.remove(i--);
//				}
//			}
////			System.out.println("AFTER: " + ObjectUtils.getObjectDescription(list));
//			return list;
//		}
		
		protected Reflector createReflector() {
			return new SimpleDelegatingReflector();
		}
		
		protected List createNonReflectableObjects() {
			return null;
		}		
		public void testSetForMutableIndexedReflectors() {
			// no items should be settable for a SortedSet
		}
	}

	
	public static class DicrListIndexedContainerReflectorTestCase extends ListReflectorTestCase {
		
		public DicrListIndexedContainerReflectorTestCase() {
			super();
		}
		public DicrListIndexedContainerReflectorTestCase(String arg0) {
			super(arg0);
		}
		
//		protected List createNonReflectableObjects() {
//			List list = super.createNonReflectableObjects();
////			System.out.println("BEFORE: " + ObjectUtils.getObjectDescription(list));
//			for (int i=0; i<list.size(); i++) {
//				if (list.get(i).getClass().isArray() || list.get(i) instanceof SortedSet) {
//					list.remove(i--);
//				}
//			}
////			System.out.println("AFTER: " + ObjectUtils.getObjectDescription(list));
//			return list;
//		}

		protected Reflector createReflector() {
			return new SimpleDelegatingReflector();
		}
		protected List createNonReflectableObjects() {
			return null;
		}

	}

}
