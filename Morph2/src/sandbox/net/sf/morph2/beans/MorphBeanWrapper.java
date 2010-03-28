/*
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package net.sf.morph2.beans;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyDescriptor;
import java.beans.PropertyEditor;

import net.sf.composite.util.ObjectUtils;
import net.sf.morph2.reflect.BeanReflector;
import net.sf.morph2.reflect.ReflectionException;
import net.sf.morph2.transform.TransformationException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.AbstractPropertyAccessor;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeansException;
import org.springframework.beans.FatalBeanException;
import org.springframework.beans.MethodInvocationException;
import org.springframework.beans.NotWritablePropertyException;
import org.springframework.beans.TypeMismatchException;

/**
 * Provides an implementation of Spring's
 * {@link org.springframework.beans.BeanWrapper} interface but leverages Morph's
 * underlying reflector infrastructure.  For example, the properties of a Map
 * or those of an HttpRequest could be exposed by this wrapper, whereas
 * the {@link org.springframework.beans.BeanWrapperImpl} is limited to using
 * reflection to expose the properties of objects.
 * 
 * @author Matt Sgarlata
 * @since Dec 13, 2004
 */
public class MorphBeanWrapper extends AbstractPropertyAccessor implements BeanWrapper {

	private static final Log log = LogFactory.getLog(MorphBeanWrapper.class);

	private Object bean;
	private BeanReflector beanReflector;

	/**
	 * Construct a new MorphBeanWrapper.
	 */
	public MorphBeanWrapper() {
		super();
	}

	/**
	 * Construct a new MorphBeanWrapper.
	 * @param bean the {@link Object} to wrap.
	 */
	public MorphBeanWrapper(Object bean) {
		this();
		setWrappedInstance(bean);
	}

	public void setWrappedInstance(Object bean) {
		this.bean = bean;
	}

	public Object getWrappedInstance() {
		return bean;
	}

	public Class getWrappedClass() {
		if (bean == null) {
			throw new NullPointerException(
					"The wrapped instance has not been set, so the wrapped class cannot be determined");
		}
		return bean.getClass();
	}

	/**
	 * Create a PropertyChangeEvent for use with Spring beans exceptions.
	 * @param propertyName
	 * @param newValue
	 * @return
	 */
	protected PropertyChangeEvent getPropertyChangeEvent(String propertyName,
			Object newValue) {
		Object oldValue;
		try {
			oldValue = beanReflector.get(bean, propertyName);
		} catch (ReflectionException e) {
			oldValue = null;
		}
		return new PropertyChangeEvent(bean, propertyName, oldValue, newValue);
	}

	public PropertyEditor findCustomEditor(Class requiredType, String propertyPath) {
		throw new UnsupportedOperationException();
	}

	public PropertyDescriptor getPropertyDescriptor(String propertyName)
			throws BeansException {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException();
	}

	public PropertyDescriptor[] getPropertyDescriptors() throws BeansException {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException();
	}

	public void setExtractOldValueForEditor(boolean extractOldValueForEditor) {
		//ignored
	}

	public boolean isExtractOldValueForEditor() {
		// TODO Auto-generated method stub
		return false;
	}

	public Class getPropertyType(String propertyName) throws BeansException {
		try {
			return beanReflector.getType(bean, propertyName);
		} catch (ReflectionException e) {
			throw new FatalBeanException("Unable to retrieve type for property '"
					+ propertyName + "' of bean "
					+ ObjectUtils.getObjectDescription(bean));
		}
	}

	public boolean isReadableProperty(String propertyName) throws BeansException {
		try {
			return beanReflector.isReadable(bean, propertyName);
		} catch (ReflectionException e) {
			throw new FatalBeanException("Unable to determine if property '"
					+ propertyName + "' is readable in bean "
					+ ObjectUtils.getObjectDescription(bean), e);
		}
	}

	public boolean isWritableProperty(String propertyName) throws BeansException {
		try {
			return beanReflector.isWriteable(bean, propertyName);
		} catch (ReflectionException e) {
			//TODO why do we throw this here?
			throw new NotWritablePropertyException(bean.getClass(), propertyName);
			//			throw new FatalBeanException("Unable to determine if property '" + propertyName + "' is writeable in bean " + ObjectUtils.getObjectDescription(bean), e);
		}
	}

	public void registerCustomEditor(Class requiredType, PropertyEditor propertyEditor) {
		throw new UnsupportedOperationException();
	}

	public void registerCustomEditor(Class requiredType, String propertyPath,
			PropertyEditor propertyEditor) {
		throw new UnsupportedOperationException();
	}

	public Object getPropertyValue(String propertyName) throws BeansException {
		try {
			return beanReflector.get(bean, propertyName);
		} catch (ReflectionException e) {
			throw new FatalBeanException("Unable to retrieve property '" + propertyName
					+ "' from bean " + ObjectUtils.getObjectDescription(bean), e);
		}
	}

	public void setPropertyValue(String propertyName, Object value) throws BeansException {
		try {
			beanReflector.set(bean, propertyName, value);
		} catch (ReflectionException e) {
			// TODO test that this actually results in TypeMismatchExceptions
			// being thrown as desired
			if (e.getCause() instanceof TransformationException) {
				Class requiredType = null;
				try {
					requiredType = getPropertyType(propertyName);
				} catch (BeansException logged) {
					if (log.isWarnEnabled()) {
						log.warn("Type for property '" + propertyName
								+ "' could not be determined", logged);
					}
				}
				throw new TypeMismatchException(getPropertyChangeEvent(propertyName,
						value), requiredType, e);
			}
			throw new MethodInvocationException(getPropertyChangeEvent(propertyName,
					value), e);
		}
	}

}
