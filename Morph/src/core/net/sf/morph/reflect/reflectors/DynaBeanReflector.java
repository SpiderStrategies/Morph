package net.sf.morph.reflect.reflectors;

import org.apache.commons.beanutils.DynaBean;
import org.apache.commons.beanutils.DynaProperty;

/**
 * @author Matt Sgarlata
 * @since Nov 20, 2004
 */
public class DynaBeanReflector extends BaseBeanReflector {
	
	public static final String IMPLICIT_PROPERTY_DYNA_CLASS = "dynaClass";
	
	private static final Class[] REFLECTABLE_TYPES =
		new Class[] { DynaBean.class };

	public DynaBeanReflector() {
		super();
	}
	private DynaBean getDynaBean(Object bean) {
		return (DynaBean) bean;
	}

	protected String[] getPropertyNamesImpl(Object bean) throws Exception {
		// note that we are not removing the "class" implicit property because
		// there is no way to tell if this was an explicitly defined property
		// of the DynaBean or if it was just included because of reflection
		DynaProperty[] properties = getDynaBean(bean).getDynaClass().getDynaProperties();
		String[] propertyNames = new String[properties.length]; 
		for (int i = 0; i < properties.length; i++) {
			propertyNames[i] = properties[i].getName();
		}
		return propertyNames;
	}

	protected Class getTypeImpl(Object bean, String propertyName) throws Exception {
		try {
			return getDynaBean(bean).getDynaClass().getDynaProperty(propertyName).getType();
		}
		// this is inelegant, but the DynaBean API isn't very expressive
		// so there's not much we can do
		catch (Exception e) {
			return Object.class;
		}		
	}

	protected boolean isReadableImpl(Object bean, String propertyName)
		throws Exception {
		// any property of the bean, including implicit properties, are assumed
		// to be readable, because there is no API in BeanUtils to determine
		// otherwise
		return
			IMPLICIT_PROPERTY_DYNA_CLASS.equals(propertyName) ||
			super.isReadableImpl(bean, propertyName);
	}

	protected Object getImpl(Object bean, String propertyName) throws Exception {
		DynaBean dyna = getDynaBean(bean);
		return IMPLICIT_PROPERTY_DYNA_CLASS.equals(propertyName) ? dyna.getDynaClass() : dyna.get(propertyName);
	}

	protected void setImpl(Object bean, String propertyName, Object value)
		throws Exception {
		getDynaBean(bean).set(propertyName, value);
	}

	public Class[] getReflectableClassesImpl() {
		return REFLECTABLE_TYPES;
	}

}