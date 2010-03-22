package net.sf.morph2.transform.converters;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import net.sf.morph2.transform.Transformer;

/**
 *
 */
public class ConstantConverterTestCase extends BaseConverterTestCase {
	/**
	 * {@inheritDoc}
	 * @see net.sf.morph2.transform.converters.BaseConverterTestCase#createDestinationClasses()
	 */
	public List createDestinationClasses() throws Exception {
		return Collections.singletonList(String.class);
	}

	/**
	 * {@inheritDoc}
	 * @see net.sf.morph2.transform.converters.BaseConverterTestCase#createInvalidDestinationClasses()
	 */
	public List createInvalidDestinationClasses() throws Exception {
		return Arrays.asList(new Class[] { Object.class, int.class, List.class });
	}

	/**
	 * {@inheritDoc}
	 * @see net.sf.morph2.transform.converters.BaseConverterTestCase#createInvalidSources()
	 */
	public List createInvalidSources() throws Exception {
		return null;
	}

	/**
	 * {@inheritDoc}
	 * @see net.sf.morph2.transform.transformers.BaseTransformerTestCase#createTransformer()
	 */
	protected Transformer createTransformer() {
		return new ConstantConverter("foo");
	}

	/**
	 * {@inheritDoc}
	 * @see net.sf.morph2.transform.converters.BaseConverterTestCase#createValidPairs()
	 */
	public List createValidPairs() throws Exception {
		ArrayList l = new ArrayList();
		l.add(new ConvertedSourcePair("foo", "bar"));
		l.add(new ConvertedSourcePair("foo", null));
		l.add(new ConvertedSourcePair("foo", new Object()));
		l.add(new ConvertedSourcePair("foo", this));
		l.add(new ConvertedSourcePair("foo", new Integer(100)));
		return l;
	}

	/**
	 * {@inheritDoc}
	 * @see net.sf.morph2.transform.converters.BaseConverterTestCase#createInvalidPairs()
	 */
	public List createInvalidPairs() throws Exception {
		ArrayList l = new ArrayList();
		l.add(new ConvertedSourcePair("fool", "bar"));
		l.add(new ConvertedSourcePair("fool", null));
		l.add(new ConvertedSourcePair("fool", new Object()));
		l.add(new ConvertedSourcePair("fool", this));
		l.add(new ConvertedSourcePair("fool", new Integer(100)));
		return l;
	}
}
