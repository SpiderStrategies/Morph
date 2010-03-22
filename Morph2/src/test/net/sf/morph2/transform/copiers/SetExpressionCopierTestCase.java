package net.sf.morph2.transform.copiers;

import java.util.Collections;
import java.util.List;

import net.sf.morph2.transform.Transformer;

/**
 *
 */
public class SetExpressionCopierTestCase extends BaseCopierTestCase {
	public static class Wrapper {
		private Object payload;

		public Wrapper() {
		}

		public Wrapper(Object payload) {
			setPayload(payload);
		}

		public Object getPayload() {
			return payload;
		}

		public void setPayload(Object payload) {
			this.payload = payload;
		}
	}

	/**
	 * {@inheritDoc}
	 * @see net.sf.morph2.transform.converters.BaseConverterTestCase#createDestinationClasses()
	 */
	public List createDestinationClasses() throws Exception {
		return null;
	}

	/**
	 * {@inheritDoc}
	 * @see net.sf.morph2.transform.converters.BaseConverterTestCase#createInvalidDestinationClasses()
	 */
	public List createInvalidDestinationClasses() throws Exception {
		return null;
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
		return new SetExpressionCopier("payload");
	}

	/**
	 * {@inheritDoc}
	 * @see net.sf.morph2.transform.converters.BaseConverterTestCase#createValidPairs()
	 */
	public List createValidPairs() throws Exception {
		return Collections
				.singletonList(new ConvertedSourcePair(new Wrapper(this), this));
	}

	/**
	 * {@inheritDoc}
	 * @see net.sf.morph2.transform.converters.BaseConverterTestCase#createInvalidPairs()
	 */
	public List createInvalidPairs() throws Exception {
		return Collections
				.singletonList(new ConvertedSourcePair(new Wrapper(), this));
	}
}
