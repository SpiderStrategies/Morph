package net.sf.morph.transform.copiers;

import java.util.Locale;

import net.sf.morph.lang.DecoratedLanguage;
import net.sf.morph.lang.languages.SimpleLanguage;
import net.sf.morph.reflect.BeanReflector;
import net.sf.morph.transform.DecoratedConverter;
import net.sf.morph.transform.DecoratedCopier;
import net.sf.morph.transform.transformers.BaseTransformer;
import net.sf.morph.util.Assert;
import net.sf.morph.util.ClassUtils;

/**
 * Uses a DecoratedLanguage to set the property denoted by an expression
 * on the destination object.
 *
 * @author mbenson
 * @since Morph 1.0.2
 */
public class SetExpressionCopier extends BaseTransformer implements DecoratedCopier,
		DecoratedConverter {
	private String expression;
	private DecoratedLanguage language;

	/**
	 * Create a new SetExpressionCopier.
	 */
	public SetExpressionCopier() {
		super();
	}

	/**
	 * Create a new SetExpressionCopier.
	 */
	public SetExpressionCopier(String expression) {
		this();
		setExpression(expression);
	}

	/**
	 * {@inheritDoc}
	 * @see net.sf.morph.transform.transformers.BaseTransformer#initializeImpl()
	 */
	protected void initializeImpl() throws Exception {
		super.initializeImpl();
		Assert.notEmpty(getExpression(), "expression");
	}

	/**
	 * {@inheritDoc}
	 * @see net.sf.morph.transform.transformers.BaseTransformer#copyImpl(java.lang.Object, java.lang.Object, java.util.Locale, java.lang.Integer)
	 */
	protected void copyImpl(Object destination, Object source, Locale locale,
			Integer preferredTransformationType) throws Exception {
		getLanguage().set(destination, expression, source, locale);
	}

	/**
	 * {@inheritDoc}
	 * @see net.sf.morph.transform.transformers.BaseTransformer#setDestinationClasses(java.lang.Class[])
	 */
	public synchronized void setDestinationClasses(Class[] destinationClasses) {
		super.setDestinationClasses(destinationClasses);
	}

	/**
	 * {@inheritDoc}
	 * @see net.sf.morph.transform.transformers.BaseTransformer#getDestinationClassesImpl()
	 */
	protected Class[] getDestinationClassesImpl() throws Exception {
		return new Class[] { Object.class };
	}

	/**
	 * {@inheritDoc}
	 * @see net.sf.morph.transform.transformers.BaseTransformer#setSourceClasses(java.lang.Class[])
	 */
	public synchronized void setSourceClasses(Class[] sourceClasses) {
		super.setSourceClasses(sourceClasses);
	}

	/**
	 * {@inheritDoc}
	 * @see net.sf.morph.transform.transformers.BaseTransformer#getSourceClassesImpl()
	 */
	protected Class[] getSourceClassesImpl() throws Exception {
		return ClassUtils.getAllClasses();
	}

	/**
	 * Get the String expression.
	 * @return String
	 */
	public String getExpression() {
		return expression;
	}

	/**
	 * Set the String expression.
	 * @param expression String
	 */
	public void setExpression(String expression) {
		this.expression = expression;
	}

	/**
	 * Get the DecoratedLanguage language.
	 * @return DecoratedLanguage
	 */
	public synchronized DecoratedLanguage getLanguage() {
		if (language == null) {
			language = new SimpleLanguage();
			((SimpleLanguage) language)
					.setReflector((BeanReflector) getReflector(BeanReflector.class));
		}
		return language;
	}

	/**
	 * Set the DecoratedLanguage language.
	 * @param language DecoratedLanguage
	 */
	public synchronized void setLanguage(DecoratedLanguage language) {
		this.language = language;
	}

	/* (non-Javadoc)
	 * @see net.sf.morph.transform.transformers.BaseTransformer#isAutomaticallyHandlingNulls()
	 */
	protected boolean isAutomaticallyHandlingNulls() {
		return false;
	}
}
