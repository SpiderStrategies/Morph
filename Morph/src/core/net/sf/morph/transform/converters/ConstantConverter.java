package net.sf.morph.transform.converters;

import java.util.Locale;

import net.sf.morph.transform.DecoratedConverter;
import net.sf.morph.transform.transformers.BaseTransformer;
import net.sf.morph.util.ClassUtils;

/**
 * Converter that always returns a certain destination object.
 *
 * @author mbenson
 * @since Morph 1.0.2
 */
public class ConstantConverter extends BaseTransformer implements DecoratedConverter {
	private Object result;

	/**
	 * Create a new ConstantConverter.
	 */
	public ConstantConverter(Object result) {
		this.result = result;
	}

	/**
	 * {@inheritDoc}
	 * @see net.sf.morph.transform.transformers.BaseTransformer#getDestinationClassesImpl()
	 */
	protected Class[] getDestinationClassesImpl() throws Exception {
		return new Class[] { ClassUtils.getClass(result) };
	}

	/**
	 * {@inheritDoc}
	 * @see net.sf.morph.transform.transformers.BaseTransformer#getSourceClassesImpl()
	 */
	protected Class[] getSourceClassesImpl() throws Exception {
		return ClassUtils.getAllClasses();
	}

	/**
	 * {@inheritDoc}
	 * @see net.sf.morph.transform.transformers.BaseTransformer#convertImpl(java.lang.Class, java.lang.Object, java.util.Locale)
	 */
	protected Object convertImpl(Class destinationClass, Object source, Locale locale)
			throws Exception {
		return result;
	}

	/**
	 * {@inheritDoc}
	 * @see net.sf.morph.transform.transformers.BaseTransformer#isAutomaticallyHandlingNulls()
	 */
	protected boolean isAutomaticallyHandlingNulls() {
		return false;
	}
}
