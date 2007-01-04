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
import net.sf.morph.transform.converters.number.BigDecimalConverterTestCase;
import net.sf.morph.transform.converters.number.BigIntegerConverterTestCase;
import net.sf.morph.transform.converters.number.ByteConverterTestCase;
import net.sf.morph.transform.converters.number.DoubleConverterTestCase;
import net.sf.morph.transform.converters.number.FloatConverterTestCase;
import net.sf.morph.transform.converters.number.IntegerConverterTestCase;
import net.sf.morph.transform.converters.number.LongConverterTestCase;
import net.sf.morph.transform.converters.number.ShortConverterTestCase;

/**
 * Performs lots of tests on the NumberConverter
 * 
 * @author Matt Sgarlata
 * @since Jan 4, 2005
 */
public class NumberConvertersTestRunner extends TestRunner {
	public static Test suite() {
		TestSuite suite = new TestSuite();

		suite.addTestSuite(BigDecimalConverterTestCase.class);
		suite.addTestSuite(BigIntegerConverterTestCase.class);
		suite.addTestSuite(ByteConverterTestCase.class);
		suite.addTestSuite(DoubleConverterTestCase.class);		
		suite.addTestSuite(FloatConverterTestCase.class);
		suite.addTestSuite(IntegerConverterTestCase.class);
		suite.addTestSuite(LongConverterTestCase.class);
		suite.addTestSuite(ShortConverterTestCase.class);
		
		return suite;
	}
}
