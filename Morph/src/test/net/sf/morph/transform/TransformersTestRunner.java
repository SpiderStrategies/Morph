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
import net.sf.morph.transform.converters.ChainedConverterTestCase;
import net.sf.morph.transform.converters.ExplicitChainedConverterTestCase;
import net.sf.morph.transform.converters.IdentityConverterTestCase;
import net.sf.morph.transform.converters.NumberConverterTestCase;
import net.sf.morph.transform.converters.NumberToTimeConverterTestCase;
import net.sf.morph.transform.converters.ObjectToClassConverterTestCase;
import net.sf.morph.transform.converters.PrimitiveWrapperConverterTestCase;
import net.sf.morph.transform.converters.TextConverterTestCase;
import net.sf.morph.transform.converters.TextToNumberConverterTestCase;
import net.sf.morph.transform.converters.TextToTimeConverterTestCase;
import net.sf.morph.transform.converters.TimeConverterTestCase;
import net.sf.morph.transform.converters.TimeToNumberConverterTestCase;
import net.sf.morph.transform.copiers.ArrayCopierTestCase;
import net.sf.morph.transform.copiers.AssemblerCopierComponentsTestCase;
import net.sf.morph.transform.copiers.AssemblerCopierTestCase;
import net.sf.morph.transform.copiers.ContainerCopierTestCase;
import net.sf.morph.transform.copiers.CumulativeCopierTestCase;
import net.sf.morph.transform.copiers.DelegatingCopierTestCase;
import net.sf.morph.transform.copiers.PartialPropertyNameMatchingCopierTestCase;
import net.sf.morph.transform.copiers.PropertyNameMatchingCopierTestCase;
import net.sf.morph.transform.transformers.SimpleDelegatingTransformerTestCase;
import net.sf.morph.transform.transformers.TypeChangingGraphTransformerTestCase;

/**
 * Run tests for all converters
 *
 * @author Matt Sgarlata
 * @since Nov 7, 2004
 */
public class TransformersTestRunner extends TestRunner {
	public static Test suite() {
		TestSuite suite = new TestSuite();

		// COPIERS

		suite.addTestSuite(ContainerCopierTestCase.class);
		suite.addTestSuite(ArrayCopierTestCase.class);
		suite.addTestSuite(PropertyNameMatchingCopierTestCase.class);
		suite.addTestSuite(PartialPropertyNameMatchingCopierTestCase.class);
		suite.addTestSuite(AssemblerCopierTestCase.class);
		suite.addTestSuite(AssemblerCopierComponentsTestCase.class);
		suite.addTestSuite(CumulativeCopierTestCase.class);

		// CONVERTERS

		suite.addTest(NumberConvertersTestRunner.suite());
		suite.addTest(ToBooleanConvertersTestRunner.suite());
		suite.addTest(ToTextConvertersTestRunner.suite());

		suite.addTestSuite(ObjectToClassConverterTestCase.class);
		suite.addTestSuite(NumberConverterTestCase.class);
		suite.addTestSuite(TextToTimeConverterTestCase.class);
		suite.addTestSuite(TimeConverterTestCase.class);
		suite.addTestSuite(IdentityConverterTestCase.class);
		suite.addTestSuite(NumberToTimeConverterTestCase.class);
		suite.addTestSuite(TextConverterTestCase.class);
		suite.addTestSuite(TextToNumberConverterTestCase.class);
		suite.addTestSuite(TimeToNumberConverterTestCase.class);
		suite.addTestSuite(PrimitiveWrapperConverterTestCase.class);

		// COMPOSITE CONVERTERS

		suite.addTestSuite(ChainedConverterTestCase.class);
		suite.addTestSuite(DelegatingCopierTestCase.class);
		suite.addTestSuite(ExplicitChainedConverterTestCase.class);

		// MISC TESTS

		suite.addTestSuite(SourceToDifferentDestinationsTestCase.class);
		suite.addTestSuite(CyclicTransformationTestCase.class);

		// TRANSFORMERS

		suite.addTestSuite(SimpleDelegatingTransformerTestCase.class);
		suite.addTestSuite(TypeChangingGraphTransformerTestCase.class);

		return suite;
	}
}