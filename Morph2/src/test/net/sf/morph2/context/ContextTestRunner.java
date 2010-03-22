package net.sf.morph2.context;

import junit.framework.Test;
import junit.framework.TestSuite;
import junit.textui.TestRunner;
import net.sf.morph2.context.contexts.HttpServletContextTestCase;

/**
 * @author Matt Sgarlata
 * @since Nov 29, 2004
 */
public class ContextTestRunner extends TestRunner {
	public static Test suite() {
		TestSuite suite = new TestSuite();

		suite.addTestSuite(HttpServletContextTestCase.class);
		//suite.addTestSuite(ChainContextCreatorTestCase.class);
		
		return suite;
	}
}
