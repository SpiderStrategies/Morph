package net.sf.morph.reflect.reflectors;

import java.util.Enumeration;

import javax.servlet.ServletContext;

/**
 * Exposes the init-parameters of a ServletContext.
 * 
 * @author Matt Sgarlata
 * @since Dec 21, 2004
 */
public class ServletContextInitParameterReflector extends BaseServletReflector {

	private static final Class[] REFLECTABLE_TYPES = new Class[] { ServletContext.class };

	/**
	 * Get the ServletContext associated with the specified Object (default implementation: cast <code>bean</code>).
	 * @param bean
	 * @return {@link ServletContext}
	 */
	protected ServletContext getServletContext(Object bean) {
		return (ServletContext) bean;
	}

	/**
	 * {@inheritDoc}
	 */
	protected String[] getPropertyNamesImpl(Object bean) throws Exception {
		Enumeration initParameterNames = getServletContext(bean).getInitParameterNames();
		return enumerationToStringArray(initParameterNames);
	}

	/**
	 * {@inheritDoc}
	 */
	protected Object getImpl(Object bean, String propertyName) throws Exception {
		return getServletContext(bean).getInitParameter(propertyName);
	}

	/**
	 * {@inheritDoc}
	 */
	protected void setImpl(Object bean, String propertyName, Object value) throws Exception {
		throw new UnsupportedOperationException();
	}

	/**
	 * {@inheritDoc}
	 */
	protected boolean isWriteableImpl(Object bean, String propertyName) throws Exception {
		return false;
	}

	/**
	 * {@inheritDoc}
	 */
	protected Class[] getReflectableClassesImpl() throws Exception {
		return REFLECTABLE_TYPES;
	}

}