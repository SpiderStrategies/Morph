package net.sf.morph.transform.copiers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import net.sf.morph.transform.Transformer;

/**
 * @author mbenson
 *
 */
public class ConditionalCopierTestCase extends BaseCopierTestCase {
	/* (non-Javadoc)
	 * @see net.sf.morph.transform.transformers.BaseTransformerTestCase#createTransformer()
	 */
	protected Transformer createTransformer() {
		ConditionalCopier result = new ConditionalCopier();
		SetExpressionCopier thenCopier = new SetExpressionCopier();
		thenCopier.setExpression("foo");
		SetExpressionCopier elseCopier = new SetExpressionCopier();
		elseCopier.setExpression("bar");
		result.setThenTransformer(thenCopier);
		result.setElseTransformer(elseCopier);
		return result;
	}

	/* (non-Javadoc)
	 * @see net.sf.morph.transform.converters.BaseConverterTestCase#createInvalidDestinationClasses()
	 */
	public List createInvalidDestinationClasses() throws Exception {
		return null;
	}

	/* (non-Javadoc)
	 * @see net.sf.morph.transform.converters.BaseConverterTestCase#createDestinationClasses()
	 */
	public List createDestinationClasses() throws Exception {
		return null;
	}

	/* (non-Javadoc)
	 * @see net.sf.morph.transform.converters.BaseConverterTestCase#createInvalidSources()
	 */
	public List createInvalidSources() throws Exception {
		return null;
	}

	/* (non-Javadoc)
	 * @see net.sf.morph.transform.converters.BaseConverterTestCase#createValidPairs()
	 */
	public List createValidPairs() throws Exception {
		ArrayList result = new ArrayList();
		result.add(new ConvertedSourcePair(singletonMap("foo", "true"), "true"));
		result.add(new ConvertedSourcePair(singletonMap("bar", "false"), "false"));
		Integer z = new Integer(0);
		result.add(new ConvertedSourcePair(singletonMap("bar", z), z));
		Integer uno = new Integer(1);
		result.add(new ConvertedSourcePair(singletonMap("foo", uno), uno));
		result.add(new ConvertedSourcePair(singletonMap("foo", Boolean.TRUE), Boolean.TRUE));
		result.add(new ConvertedSourcePair(singletonMap("bar", Boolean.FALSE), Boolean.FALSE));
		Object o = new Object();
		result.add(new ConvertedSourcePair(singletonMap("foo", o), o));
		result.add(new ConvertedSourcePair(singletonMap("bar", null), null));
		return result;
	}

	private HashMap singletonMap(Object key, Object value) {
		HashMap result = new HashMap();
		result.put(key, value);
		return result;
	}
}
