/*
 * Copyright 2004-2005, 2007 the original author or authors.
 * 
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
package net.sf.morph.transform.converters;

import java.util.Locale;

import net.sf.morph.transform.Converter;
import net.sf.morph.transform.DecoratedConverter;
import net.sf.morph.transform.TransformationException;
import net.sf.morph.transform.transformers.BaseTransformer;

import org.apache.commons.beanutils.ConvertUtilsBean;

/**
 * A converter which delegates to Commons BeanUtils.
 * 
 * @author Matt Sgarlata
 * @since October 25, 2004
 */
public class BeanUtilsConverter extends BaseTransformer implements Converter, DecoratedConverter {

	public static final Class[] ALL_OBJECTS = new Class[] { Object.class };
	
	private ConvertUtilsBean convertUtilsBean;
	
	private org.apache.commons.beanutils.Converter getConverter(Class destinationClass) {
		return getConvertUtilsBean().lookup(destinationClass);
	}
	
	protected boolean isTransformableImpl(Class destinationType, Class sourceType) {
		return getConverter(destinationType) != null;
	}

	protected Object convertImpl(Class destinationClass, Object source, Locale locale)
			throws TransformationException {
		org.apache.commons.beanutils.Converter c =
			getConvertUtilsBean().lookup(destinationClass);
		return c.convert(destinationClass, source);
	}

	/**
	 * Returns the ConvertUtilsBean set with the setConvertUtilsBean method, or
	 * the default ConvertUtilsBean instance that is used by the static methods
	 * in ConvertUtils.
	 * @return the ConvertUtilsBean set with the setConvertUtilsBean method, or
	 * the default ConvertUtilsBean instance that is used by the static methods
	 * in ConvertUtils.
	 */
	public ConvertUtilsBean getConvertUtilsBean() {
		if (convertUtilsBean == null) {
			setConvertUtilsBean(MyConvertUtilsBeanHack.getInstance());
		}
		return convertUtilsBean;
	}

	/**
	 * Set the ConvertUtilsBean to use.
	 * @param convertUtilsBean
	 */
	public void setConvertUtilsBean(ConvertUtilsBean convertUtilsBean) {
		this.convertUtilsBean = convertUtilsBean;
	}

	/**
	 * A hack that allows us to directly access the default ConvertUtilsBean
	 * instance that is used by the static methods in ConvertUtils
	 */
	private static class MyConvertUtilsBeanHack extends ConvertUtilsBean {
		public static ConvertUtilsBean getInstance() {
			return ConvertUtilsBean.getInstance();
		}
	}

	protected boolean isNaivelyConvertible(Class destinationClass, Class sourceClass) {
		return true;
	}

	protected boolean isWrappingRuntimeExceptions() {
		// let runtime ConvertUtils exceptions be thrown if there are any (but
		// I'm pretty sure they're all checked so will have to be wrapped)
	    return false;
    }

	public Class[] getSourceClassesImpl() {
		return ALL_OBJECTS;
	}

	public Class[] getDestinationClassesImpl() {
		return ALL_OBJECTS;
	}

}