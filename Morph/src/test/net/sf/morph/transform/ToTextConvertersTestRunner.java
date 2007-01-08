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
package net.sf.morph.transform;

import junit.framework.Test;
import junit.framework.TestSuite;
import junit.textui.TestRunner;
import net.sf.morph.transform.converters.totext.BooleanToTextConverterTestCase;
import net.sf.morph.transform.converters.totext.ContainerToPrettyTextConverterTestCase;
import net.sf.morph.transform.converters.totext.DefaultToTextConverterTestCase;
import net.sf.morph.transform.converters.totext.NumberToTextConverterTestCase;
import net.sf.morph.transform.converters.totext.ObjectToPrettyTextConverterTestCase;
import net.sf.morph.transform.converters.totext.ObjectToTextConverterTestCase;
import net.sf.morph.transform.converters.totext.TimeToTextConverterTestCase;

/**
 * @author Matt Sgarlata
 * @since Dec 30, 2004
 */
public class ToTextConvertersTestRunner extends TestRunner {
	
	public static Test suite() {
		TestSuite suite = new TestSuite();

		suite.addTestSuite(BooleanToTextConverterTestCase.class);
		suite.addTestSuite(ObjectToTextConverterTestCase.class);
		suite.addTestSuite(NumberToTextConverterTestCase.class);
		suite.addTestSuite(TimeToTextConverterTestCase.class);
		suite.addTestSuite(ContainerToPrettyTextConverterTestCase.class);
		suite.addTestSuite(DefaultToTextConverterTestCase.class);
		suite.addTestSuite(ObjectToPrettyTextConverterTestCase.class);

		return suite;
	}
	
}
