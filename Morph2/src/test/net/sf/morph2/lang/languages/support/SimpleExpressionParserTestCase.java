package net.sf.morph2.lang.languages.support;

import junit.framework.TestCase;
import net.sf.composite.util.ObjectUtils;
import net.sf.morph2.lang.support.SimpleExpressionParser;
import net.sf.morph2.util.TestUtils;

/**
 * @author Matt Sgarlata
 * @since Dec 5, 2004
 */
public class SimpleExpressionParserTestCase extends TestCase {

	public SimpleExpressionParserTestCase() {
		super();
	}
	public SimpleExpressionParserTestCase(String arg0) {
		super(arg0);
	}
	
	protected SimpleExpressionParser parser;
	
	protected void setUp() throws Exception {
		parser = new SimpleExpressionParser();
	}
	
	public void testEmptyExpressions() {
		assertTrue(ObjectUtils.isEmpty(parser.parse(null)));
		assertTrue(ObjectUtils.isEmpty(parser.parse("")));
		assertTrue(ObjectUtils.isEmpty(parser.parse("  ")));
		assertTrue(ObjectUtils.isEmpty(parser.parse("(")));
		assertTrue(ObjectUtils.isEmpty(parser.parse(" ( ")));
		assertTrue(ObjectUtils.isEmpty(parser.parse("()''\"\"[]")));
	}
	
	public void testSingleTokenExpressions() {
		String[] parsed = new String[] { "one" };
		
		TestUtils.assertEquals(parsed, parser.parse("one"));
		TestUtils.assertEquals(parsed, parser.parse(" one "));
		TestUtils.assertEquals(parsed, parser.parse(" one . [ ] ''\""));
	}

	public void testMultiTokenExpressions() {
		String[] parsed = new String[] { "one", "two", "three" };
		
		TestUtils.assertEquals(parsed, parser.parse("one.two.three"));
		TestUtils.assertEquals(parsed, parser.parse("one(two).three"));
		TestUtils.assertEquals(parsed, parser.parse(" one [' two '] three"));
	}

}
