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
package net.sf.morph2;

import junit.framework.Test;
import junit.framework.TestSuite;
import junit.textui.TestRunner;
import net.sf.morph2.context.ContextTestRunner;
import net.sf.morph2.examples.ExamplesTestRunner;
import net.sf.morph2.integration.IntegrationTestRunner;
import net.sf.morph2.lang.LanguageTestRunner;
import net.sf.morph2.reflect.ReflectorsTestRunner;
import net.sf.morph2.transform.TransformersTestRunner;
import net.sf.morph2.wrap.BeanTestCase;

/**
 * Run all tests in the framework.  Runs tests with the least dependency on
 * other parts of the Morph framework first, and then tests that are highly
 * dependent on other parts of the framework.  That way, the first error that
 * pops up will hopefully be the most direct cause of the error.  Fixing it may
 * very well fix several other failed tests.
 * 
 * @author Matt Sgarlata
 * @since Nov 8, 2004
 */
public class MorphTestRunner extends TestRunner {
	public static Test suite() {
		TestSuite suite = new TestSuite();

		suite.addTest(ReflectorsTestRunner.suite());
		suite.addTest(TransformersTestRunner.suite());
		suite.addTest(LanguageTestRunner.suite());
		suite.addTest(ContextTestRunner.suite());
		suite.addTest(ExamplesTestRunner.suite());
		suite.addTest(IntegrationTestRunner.suite());

		suite.addTestSuite(BeanTestCase.class);
		suite.addTestSuite(MorphTestCase.class);
		
		return suite;
	}
	
	public static void main(String args[]) {
		run(MorphTestRunner.suite());
	}
}
