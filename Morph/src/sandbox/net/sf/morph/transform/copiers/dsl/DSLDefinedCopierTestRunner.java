package net.sf.morph.transform.copiers.dsl;

import junit.framework.Test;
import junit.framework.TestSuite;
import junit.textui.TestRunner;

public class DSLDefinedCopierTestRunner extends TestRunner {
	public static Test suite() {
		TestSuite suite = new TestSuite();
		suite.addTestSuite(BasicDSLDefinedCopierTest.class);
		suite.addTestSuite(LeftwardDSLDefinedCopierTest.class);
		suite.addTestSuite(RightwardDSLDefinedCopierTest.class);
		suite.addTestSuite(LeftwardMappedDSLDefinedCopierTest.class);
		suite.addTestSuite(RightwardMappedDSLDefinedCopierTest.class);
		suite.addTestSuite(DeepDSLDefinedCopierTest.class);
		suite.addTestSuite(ProxyDSLDefinedCopierTest.class);
		suite.addTestSuite(ShortLeftwardDSLDefinedCopierTest.class);
		suite.addTestSuite(ShortRightwardDSLDefinedCopierTest.class);
		suite.addTestSuite(MultiMappedTestCase.class);
		return suite;
	}
}
