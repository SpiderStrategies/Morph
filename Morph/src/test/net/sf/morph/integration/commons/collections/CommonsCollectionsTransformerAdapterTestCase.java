package net.sf.morph.integration.commons;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.collections.TransformerUtils;

import net.sf.morph.transform.Transformer;
import net.sf.morph.transform.converters.BaseConverterTestCase;

/**
 *
 */
public class CommonsCollectionsTransformerAdapterTestCase extends BaseConverterTestCase {
	/**
	 * {@inheritDoc}
	 * @see net.sf.morph.transform.converters.BaseConverterTestCase#createDestinationClasses()
	 */
	public List createDestinationClasses() throws Exception {
		return null;
	}

	/**
	 * {@inheritDoc}
	 * @see net.sf.morph.transform.converters.BaseConverterTestCase#createInvalidDestinationClasses()
	 */
	public List createInvalidDestinationClasses() throws Exception {
		return null;
	}

	/**
	 * {@inheritDoc}
	 * @see net.sf.morph.transform.converters.BaseConverterTestCase#createInvalidSources()
	 */
	public List createInvalidSources() throws Exception {
		return null;
	}

	/**
	 * {@inheritDoc}
	 * @see net.sf.morph.transform.transformers.BaseTransformerTestCase#createTransformer()
	 */
	protected Transformer createTransformer() {
		HashMap testMap = new HashMap();
		testMap.put("foo", "FOO");
		testMap.put("bar", "BAR");
		testMap.put("baz", "BAZ");
		return new CommonsCollectionsTransformerAdapter(TransformerUtils.mapTransformer(testMap));
	}

	/**
	 * {@inheritDoc}
	 * @see net.sf.morph.transform.converters.BaseConverterTestCase#createValidPairs()
	 */
	public List createValidPairs() throws Exception {
		ArrayList l = new ArrayList();
		l.add(new ConvertedSourcePair("FOO", "foo"));
		l.add(new ConvertedSourcePair("BAR", "bar"));
		l.add(new ConvertedSourcePair("BAZ", "baz"));
		l.add(new ConvertedSourcePair(null, "spazz"));
		l.add(new ConvertedSourcePair(null, Integer.valueOf(6)));
		l.add(new ConvertedSourcePair(null, new Object()));
		return l;
	}

	/**
	 * {@inheritDoc}
	 * @see net.sf.morph.transform.converters.BaseConverterTestCase#createInvalidPairs()
	 */
	public List createInvalidPairs() throws Exception {
		ArrayList l = new ArrayList();
		l.add(new ConvertedSourcePair("foo", "foo"));
		l.add(new ConvertedSourcePair("bar", "bar"));
		l.add(new ConvertedSourcePair("baz", "baz"));
		l.add(new ConvertedSourcePair("spazz", "spazz"));
		l.add(new ConvertedSourcePair(Integer.valueOf(6), Integer.valueOf(6)));
		Object o = new Object();
		l.add(new ConvertedSourcePair(o, o));
		return l;
	}
}
