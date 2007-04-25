package net.sf.morph.transform.copiers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import net.sf.morph.transform.Transformer;

/**
 *
 */
public class NOPCopierTestCase extends BaseCopierTestCase {

	/**
	 * {@inheritDoc}
	 * @see net.sf.morph.transform.converters.BaseConverterTestCase#createDestinationClasses()
	 */
	public List createDestinationClasses() throws Exception {
		return Arrays.asList(new Class[] { Object.class, String.class, StringBuffer.class, List.class });
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
		return new NOPCopier();
	}

	/**
	 * {@inheritDoc}
	 * @see net.sf.morph.transform.converters.BaseConverterTestCase#createValidPairs()
	 */
	public List createValidPairs() throws Exception {
		ArrayList l = new ArrayList();
		l.add(new ConvertedSourcePair(null, null));
		l.add(new ConvertedSourcePair("", this));
		l.add(new ConvertedSourcePair("", "foo"));
		l.add(new ConvertedSourcePair("", Integer.valueOf(100)));
		l.add(new ConvertedSourcePair(new Object(), this));
		l.add(new ConvertedSourcePair(new Object(), "foo"));
		l.add(new ConvertedSourcePair(new Object(), Integer.valueOf(100)));
		l.add(new ConvertedSourcePair(new ArrayList(), this));
		l.add(new ConvertedSourcePair(new HashSet(), "foo"));
		l.add(new ConvertedSourcePair(new HashMap(), Integer.valueOf(100)));
		return l;
	}

	/**
	 * {@inheritDoc}
	 * @see net.sf.morph.transform.converters.BaseConverterTestCase#createInvalidPairs()
	 */
	public List createInvalidPairs() throws Exception {
		ArrayList l = new ArrayList();
		l.add(new ConvertedSourcePair("", null));
		l.add(new ConvertedSourcePair(toString(), this));
		l.add(new ConvertedSourcePair("foo", "foo"));
		l.add(new ConvertedSourcePair("100", Integer.valueOf(100)));
		l.add(new ConvertedSourcePair(addThis(new ArrayList()), this));
		l.add(new ConvertedSourcePair(addThis(new HashSet()), "foo"));
		return l;
	}

	private Collection addThis(Collection c) {
		c.add(this);
		return c;
	}
}
