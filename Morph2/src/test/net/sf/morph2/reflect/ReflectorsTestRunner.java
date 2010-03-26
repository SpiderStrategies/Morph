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
package net.sf.morph2.reflect;

import junit.framework.Test;
import junit.framework.TestSuite;
import junit.textui.TestRunner;
import net.sf.morph2.reflect.reflectors.ArrayReflectorTestCase;
import net.sf.morph2.reflect.reflectors.CollectionReflectorTestCase;
import net.sf.morph2.reflect.reflectors.DelegatingIndexedContainerReflectorTestRunner;
import net.sf.morph2.reflect.reflectors.DynaBeanReflectorTestCase;
import net.sf.morph2.reflect.reflectors.EnumerationReflectorTestCase;
import net.sf.morph2.reflect.reflectors.ExtractEntriesMapReflectorTestCase;
import net.sf.morph2.reflect.reflectors.ExtractKeysMapReflectorTestCase;
import net.sf.morph2.reflect.reflectors.ExtractValuesMapReflectorTestCase;
import net.sf.morph2.reflect.reflectors.HttpSessionAttributeReflectorTestCase;
import net.sf.morph2.reflect.reflectors.IteratorReflectorTestCase;
import net.sf.morph2.reflect.reflectors.ListReflectorTestCase;
import net.sf.morph2.reflect.reflectors.ObjectReflectorTestCase;
import net.sf.morph2.reflect.reflectors.PageContextAttributeReflectorTestCase;
import net.sf.morph2.reflect.reflectors.ServletContextAttributeReflectorTestCase;
import net.sf.morph2.reflect.reflectors.ServletContextInitParameterReflectorTestCase;
import net.sf.morph2.reflect.reflectors.ServletRequestAttributeReflectorTestCase;
import net.sf.morph2.reflect.reflectors.ServletRequestParameterReflectorTestCase;
import net.sf.morph2.reflect.reflectors.ServletRequestReflectorTestCase;
import net.sf.morph2.reflect.reflectors.SetReflectorTestCase;
import net.sf.morph2.reflect.reflectors.SimpleInstantiatingReflectorTestCase;
import net.sf.morph2.reflect.reflectors.SortedSetReflectorTestCase;
import net.sf.morph2.reflect.reflectors.StringTokenizerReflectorTestCase;
import net.sf.morph2.reflect.reflectors.VelocityContextReflectorTestCase;

/**
 * @author Matt Sgarlata
 * @since Nov 20, 2004
 */
public class ReflectorsTestRunner extends TestRunner {
	
	public static Test suite() {
		TestSuite suite = new TestSuite();

		suite.addTestSuite(ArrayReflectorTestCase.class);
		suite.addTestSuite(SimpleInstantiatingReflectorTestCase.class);
		suite.addTestSuite(CollectionReflectorTestCase.class);
		suite.addTest(DelegatingIndexedContainerReflectorTestRunner.suite());
		suite.addTestSuite(DynaBeanReflectorTestCase.class);
		suite.addTestSuite(HttpSessionAttributeReflectorTestCase.class);
		suite.addTestSuite(StringTokenizerReflectorTestCase.class);
		suite.addTestSuite(EnumerationReflectorTestCase.class);
		suite.addTestSuite(ExtractEntriesMapReflectorTestCase.class);
		suite.addTestSuite(ExtractKeysMapReflectorTestCase.class);
		suite.addTestSuite(ExtractValuesMapReflectorTestCase.class);
		suite.addTestSuite(IteratorReflectorTestCase.class);
		suite.addTestSuite(ListReflectorTestCase.class);
		suite.addTestSuite(ObjectReflectorTestCase.class);
		suite.addTestSuite(PageContextAttributeReflectorTestCase.class);
		suite.addTestSuite(ServletContextAttributeReflectorTestCase.class);
		suite.addTestSuite(ServletContextInitParameterReflectorTestCase.class);
		suite.addTestSuite(ServletRequestAttributeReflectorTestCase.class);
		suite.addTestSuite(ServletRequestParameterReflectorTestCase.class);
		suite.addTestSuite(ServletRequestReflectorTestCase.class);
		suite.addTestSuite(SetReflectorTestCase.class);
		suite.addTestSuite(SortedSetReflectorTestCase.class);
		suite.addTestSuite(VelocityContextReflectorTestCase.class);
		
		return suite;
	}
	
}
