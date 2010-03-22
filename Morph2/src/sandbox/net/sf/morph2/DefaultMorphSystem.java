/*
 * Copyright 2007-2008 the original author or authors.
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
package net.sf.morph2;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;

import net.sf.morph2.lang.LanguageException;
import net.sf.morph2.reflect.ReflectionException;
import net.sf.morph2.transform.TransformationException;
import net.sf.morph2.wrap.Bean;
import net.sf.morph2.wrap.Container;
import net.sf.morph2.wrap.GrowableContainer;
import net.sf.morph2.wrap.IndexedContainer;
import net.sf.morph2.wrap.MutableIndexedContainer;
import net.sf.morph2.wrap.Wrapper;
import net.sf.morph2.wrap.WrapperException;

/**
 * 
 * @author Matt Sgarlata
 * @since Morph 1.1 (Oct 31, 2007)
 */
public class DefaultMorphSystem implements MorphSystem {
	
	private Map components;
	
	public DefaultMorphSystem() {
		
	}

	public Wrapper getWrapper(Object object) {
	    // TODO Auto-generated method stub
	    return null;
    }

	public Object newInstance(Class clazz, Object parameters) throws ReflectionException {
	    // TODO Auto-generated method stub
	    return null;
    }

	public boolean add(Object container, Object value)
	        throws ReflectionException {
		// TODO Auto-generated method stub
		return false;
	}

	public Object convert(Class destinationClass, Object source)
	        throws TransformationException {
		// TODO Auto-generated method stub
		return null;
	}

	public Object convert(Class destinationClass, Object source, Locale locale)
	        throws TransformationException {
		// TODO Auto-generated method stub
		return null;
	}

	public BigDecimal convertToBigDecimal(Object source)
	        throws TransformationException {
		// TODO Auto-generated method stub
		return null;
	}

	public BigDecimal convertToBigDecimal(Object source, Locale locale)
	        throws TransformationException {
		// TODO Auto-generated method stub
		return null;
	}

	public BigInteger convertToBigInteger(Object source)
	        throws TransformationException {
		// TODO Auto-generated method stub
		return null;
	}

	public BigInteger convertToBigInteger(Object source, Locale locale)
	        throws TransformationException {
		// TODO Auto-generated method stub
		return null;
	}

	public boolean convertToBoolean(Object source)
	        throws TransformationException {
		// TODO Auto-generated method stub
		return false;
	}

	public Boolean convertToBooleanObject(Object source)
	        throws TransformationException {
		// TODO Auto-generated method stub
		return null;
	}

	public byte convertToByte(Object source) throws TransformationException {
		// TODO Auto-generated method stub
		return 0;
	}

	public byte convertToByte(Object source, Locale locale)
	        throws TransformationException {
		// TODO Auto-generated method stub
		return 0;
	}

	public Byte convertToByteObject(Object source)
	        throws TransformationException {
		// TODO Auto-generated method stub
		return null;
	}

	public Byte convertToByteObject(Object source, Locale locale)
	        throws TransformationException {
		// TODO Auto-generated method stub
		return null;
	}

	public Calendar convertToCalendar(Object source)
	        throws TransformationException {
		// TODO Auto-generated method stub
		return null;
	}

	public Calendar convertToCalendar(Object source, Locale locale)
	        throws TransformationException {
		// TODO Auto-generated method stub
		return null;
	}

	public Date convertToDate(Object source) throws TransformationException {
		// TODO Auto-generated method stub
		return null;
	}

	public Date convertToDate(Object source, Locale locale)
	        throws TransformationException {
		// TODO Auto-generated method stub
		return null;
	}

	public double convertToDouble(Object source) throws TransformationException {
		// TODO Auto-generated method stub
		return 0;
	}

	public double convertToDouble(Object source, Locale locale)
	        throws TransformationException {
		// TODO Auto-generated method stub
		return 0;
	}

	public Double convertToDoubleObject(Object source)
	        throws TransformationException {
		// TODO Auto-generated method stub
		return null;
	}

	public Double convertToDoubleObject(Object source, Locale locale)
	        throws TransformationException {
		// TODO Auto-generated method stub
		return null;
	}

	public float convertToFloat(Object source) throws TransformationException {
		// TODO Auto-generated method stub
		return 0;
	}

	public float convertToFloat(Object source, Locale locale)
	        throws TransformationException {
		// TODO Auto-generated method stub
		return 0;
	}

	public Float convertToFloatObject(Object source)
	        throws TransformationException {
		// TODO Auto-generated method stub
		return null;
	}

	public Float convertToFloatObject(Object source, Locale locale)
	        throws TransformationException {
		// TODO Auto-generated method stub
		return null;
	}

	public int convertToInt(Object source) throws TransformationException {
		// TODO Auto-generated method stub
		return 0;
	}

	public int convertToInt(Object source, Locale locale)
	        throws TransformationException {
		// TODO Auto-generated method stub
		return 0;
	}

	public Integer convertToIntegerObject(Object source)
	        throws TransformationException {
		// TODO Auto-generated method stub
		return null;
	}

	public Integer convertToIntegerObject(Object source, Locale locale)
	        throws TransformationException {
		// TODO Auto-generated method stub
		return null;
	}

	public long convertToLong(Object source) throws TransformationException {
		// TODO Auto-generated method stub
		return 0;
	}

	public long convertToLong(Object source, Locale locale)
	        throws TransformationException {
		// TODO Auto-generated method stub
		return 0;
	}

	public Long convertToLongObject(Object source)
	        throws TransformationException {
		// TODO Auto-generated method stub
		return null;
	}

	public Long convertToLongObject(Object source, Locale locale)
	        throws TransformationException {
		// TODO Auto-generated method stub
		return null;
	}

	public String convertToPrettyString(Object source)
	        throws TransformationException {
		// TODO Auto-generated method stub
		return null;
	}

	public String convertToString(Object source) throws TransformationException {
		// TODO Auto-generated method stub
		return null;
	}

	public String convertToString(Object source, Locale locale)
	        throws TransformationException {
		// TODO Auto-generated method stub
		return null;
	}

	public void copy(Object destination, Object source)
	        throws TransformationException {
		// TODO Auto-generated method stub

	}

	public void copy(Object destination, Object source, Locale locale)
	        throws TransformationException {
		// TODO Auto-generated method stub

	}

	public Object get(Object target, String expression)
	        throws LanguageException {
		// TODO Auto-generated method stub
		return null;
	}

	public Object get(Object target, String expression, Class destinationClass)
	        throws LanguageException, TransformationException {
		// TODO Auto-generated method stub
		return null;
	}

	public Object get(Object target, String expression, Class destinationClass,
	        Locale locale) throws LanguageException, TransformationException {
		// TODO Auto-generated method stub
		return null;
	}

	public Object get(Object target, String expression, Locale locale,
	        Class destinationClass) throws LanguageException,
	        TransformationException {
		// TODO Auto-generated method stub
		return null;
	}

	public Object get(Object container, int index) throws ReflectionException {
		// TODO Auto-generated method stub
		return null;
	}

	public Bean getBean(Object object) throws WrapperException {
		// TODO Auto-generated method stub
		return null;
	}

	public BigDecimal getBigDecimal(Object target, String expression) {
		// TODO Auto-generated method stub
		return null;
	}

	public BigInteger getBigInteger(Object target, String expression) {
		// TODO Auto-generated method stub
		return null;
	}

	public boolean getBoolean(Object target, String expression) {
		// TODO Auto-generated method stub
		return false;
	}

	public Boolean getBooleanObject(Object target, String expression) {
		// TODO Auto-generated method stub
		return null;
	}

	public byte getByte(Object target, String expression) {
		// TODO Auto-generated method stub
		return 0;
	}

	public Byte getByteObject(Object target, String expression) {
		// TODO Auto-generated method stub
		return null;
	}

	public Calendar getCalendar(Object target, String expression) {
		// TODO Auto-generated method stub
		return null;
	}

	public Class getContainedType(Class clazz) throws ReflectionException {
		// TODO Auto-generated method stub
		return null;
	}

	public Container getContainer(Object object) throws WrapperException {
		// TODO Auto-generated method stub
		return null;
	}

	public Date getDate(Object target, String expression) {
		// TODO Auto-generated method stub
		return null;
	}

	public double getDouble(Object target, String expression) {
		// TODO Auto-generated method stub
		return 0;
	}

	public Double getDoubleObject(Object target, String expression) {
		// TODO Auto-generated method stub
		return null;
	}

	public float getFloat(Object target, String expression) {
		// TODO Auto-generated method stub
		return 0;
	}

	public Float getFloatObject(Object target, String expression) {
		// TODO Auto-generated method stub
		return null;
	}

	public GrowableContainer getGrowableContainer(Object object)
	        throws WrapperException {
		// TODO Auto-generated method stub
		return null;
	}

	public IndexedContainer getIndexedContainer(Object object)
	        throws WrapperException {
		// TODO Auto-generated method stub
		return null;
	}

	public int getInt(Object target, String expression) {
		// TODO Auto-generated method stub
		return 0;
	}

	public Integer getIntegerObject(Object target, String expression) {
		// TODO Auto-generated method stub
		return null;
	}

	public Iterator getIterator(Object container) throws ReflectionException {
		// TODO Auto-generated method stub
		return null;
	}

	public long getLong(Object target, String expression) {
		// TODO Auto-generated method stub
		return 0;
	}

	public Long getLongObject(Object target, String expression) {
		// TODO Auto-generated method stub
		return null;
	}

	public MutableIndexedContainer getMutableIndexedContainer(Object object)
	        throws WrapperException {
		// TODO Auto-generated method stub
		return null;
	}

	public String[] getPropertyNames(Object bean) throws ReflectionException {
		// TODO Auto-generated method stub
		return null;
	}

	public int getSize(Object object) throws ReflectionException {
		// TODO Auto-generated method stub
		return 0;
	}

	public String getString(Object target, String expression) {
		// TODO Auto-generated method stub
		return null;
	}

	public String getString(Object target, String expression, Locale locale) {
		// TODO Auto-generated method stub
		return null;
	}

	public Class getType(Object bean, String propertyName)
	        throws ReflectionException {
		// TODO Auto-generated method stub
		return null;
	}

	public Class getType(Class beanType, String propertyName)
	        throws ReflectionException {
		// TODO Auto-generated method stub
		return null;
	}

	public boolean isReadable(Object bean, String propertyName)
	        throws ReflectionException {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean isWriteable(Object bean, String propertyName)
	        throws ReflectionException {
		// TODO Auto-generated method stub
		return false;
	}

	public Object newInstance(Class clazz) throws ReflectionException {
		// TODO Auto-generated method stub
		return null;
	}

	public void set(Object target, String expression, Object value)
	        throws LanguageException, TransformationException {
		// TODO Auto-generated method stub

	}

	public void set(Object target, String expression, Object value,
	        Locale locale) throws LanguageException, TransformationException {
		// TODO Auto-generated method stub

	}

	public void set(Object target, String expression, boolean value) {
		// TODO Auto-generated method stub

	}

	public void set(Object target, String expression, byte value) {
		// TODO Auto-generated method stub

	}

	public void set(Object target, String expression, double value) {
		// TODO Auto-generated method stub

	}

	public void set(Object target, String expression, float value) {
		// TODO Auto-generated method stub

	}

	public void set(Object target, String expression, int value) {
		// TODO Auto-generated method stub

	}

	public void set(Object target, String expression, long value) {
		// TODO Auto-generated method stub

	}

	public Object set(Object container, int index, Object propertyValue)
	        throws ReflectionException {
		// TODO Auto-generated method stub
		return null;
	}

	protected Map getComponents() {
    	return components;
    }

	protected void setComponents(Map components) {
    	this.components = components;
    }
	
	public void setComponent(String name, Object component) {
		getComponents().put(name, component);
	}

}
