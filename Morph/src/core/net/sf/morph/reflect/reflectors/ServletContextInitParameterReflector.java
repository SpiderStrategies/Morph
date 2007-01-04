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

	private static final Class[] REFLECTABLE_TYPES = new Class[] {
		ServletContext.class
	};
	
	protected ServletContext getServletContext(Object bean) {
		return (ServletContext) bean;
	}

	protected String[] getPropertyNamesImpl(Object bean) throws Exception {
		Enumeration initParameterNames =
			getServletContext(bean).getInitParameterNames();
		return enumerationToStringArray(initParameterNames);
	}

	protected Object getImpl(Object bean, String propertyName) throws Exception {
		return getServletContext(bean).getInitParameter(propertyName);
	}

	protected void setImpl(Object bean, String propertyName, Object value)
		throws Exception {
		throw new UnsupportedOperationException();
	}
	
	protected boolean isWriteableImpl(Object bean, String propertyName)
		throws Exception {
		return false;
	}

	protected Class[] getReflectableClassesImpl() throws Exception {
		return REFLECTABLE_TYPES;
	}

}