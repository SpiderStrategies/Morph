/*
 * Copyright 2004-2005 the original author or authors.
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
 * License for the specific Defaults.getLanguage() governing permissions and limitations under
 * the License.
 */
package net.sf.morph;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.Locale;

import net.sf.morph.lang.InvalidExpressionException;
import net.sf.morph.lang.LanguageException;
import net.sf.morph.lang.languages.SimpleLanguage;
import net.sf.morph.reflect.BeanReflector;
import net.sf.morph.reflect.ContainerReflector;
import net.sf.morph.reflect.DecoratedReflector;
import net.sf.morph.reflect.GrowableContainerReflector;
import net.sf.morph.reflect.IndexedContainerReflector;
import net.sf.morph.reflect.InstantiatingReflector;
import net.sf.morph.reflect.MutableIndexedContainerReflector;
import net.sf.morph.reflect.ReflectionException;
import net.sf.morph.reflect.Reflector;
import net.sf.morph.reflect.SizableReflector;
import net.sf.morph.transform.DecoratedConverter;
import net.sf.morph.transform.DecoratedCopier;
import net.sf.morph.transform.TransformationException;
import net.sf.morph.wrap.Bean;
import net.sf.morph.wrap.Container;
import net.sf.morph.wrap.GrowableContainer;
import net.sf.morph.wrap.IndexedContainer;
import net.sf.morph.wrap.MutableIndexedContainer;
import net.sf.morph.wrap.WrapperException;

/**
 * <p>
 * A convenient static API for basic use of the Morph framework. This class does
 * <em>not</em> provide any means for customizing/changing its behavior so
 * that components can be guaranteed that calls to this static class will behave
 * consistently regardless of the environment they are executed in. If you need
 * customized behavior, use a
 * {@link net.sf.morph.transform.transformers.SimpleDelegatingTransformer} or
 * other customized Morph objects. This class is just a
 * static facade for accessing those classes directly. They are particularly
 * easy to configure using an Inversion of Control framework such as <a
 * href="http://www.springframework.org">Spring</a>.
 * 
 * @author Matt Sgarlata
 * @since Nov 28, 2004
 * @see net.sf.morph.transform.copiers.DelegatingCopier
 * @see net.sf.morph.transform.converters.DelegatingConverter
 * @see net.sf.morph.lang.languages.SimpleLanguage
 */
public abstract class Morph {
	
	private static final SizableReflector DEFAULT_SIZABLE_REFLECTOR = Defaults.createSizableReflector();
	private static final MutableIndexedContainerReflector DEFAULT_MUTABLE_INDEXED_CONTAINER_REFLECTOR = Defaults.createMutableIndexedContainerReflector();
	private static final InstantiatingReflector DEFAULT_INSTANTIATING_REFLECTOR = Defaults.createInstantiatingReflector();
	private static final IndexedContainerReflector DEFAULT_INDEXED_CONTAINER_REFLECTOR = Defaults.createIndexedContainerReflector();
	private static final GrowableContainerReflector DEFAULT_GROWABLE_CONTAINER_REFLECTOR = Defaults.createGrowableContainerReflector();
	private static final ContainerReflector DEFAULT_CONTAINER_REFLECTOR = Defaults.createContainerReflector();
	private static final BeanReflector DEFAULT_BEAN_REFLECTOR = Defaults.createBeanReflector();
	private static final DecoratedReflector DEFAULT_REFLECTOR = Defaults.createReflector();
	private static final DecoratedCopier DEFAULT_COPIER = Defaults.createCopier();
	private static final SimpleLanguage DEFAULT_LANGUAGE = Defaults.createLanguage();
	private static final DecoratedConverter DEFAULT_PRETTY_TEXT_CONVERTER = Defaults.createPrettyTextConverter();
	private static final DecoratedConverter DEFAULT_CONVERTER = Defaults.createConverter();
	
	/**
	 * This class cannot be instantiated.
	 */
	private Morph() { }
	
	/**
	 * Converts the given <code>source</code> into an object of class
	 * <code>destinationClass</code>. The returned object may be a reference
	 * to <code>source</code> itself. This isn't an issue for immutable
	 * classes (String, Long, etc) but is an issue for Collection and Array
	 * types.
	 * 
	 * @param destinationClass
	 *            the destination class to convert
	 * @param source
	 *            the source object to convert
	 * @return the result of the conversion
	 * @throws TransformationException
	 *             if <code>destinationClass</code> is <code>null</code>,
	 *             an error occurred while performing the conversion
	 */
	public static Object convert(Class destinationClass, Object source)
		throws TransformationException {
		return DEFAULT_CONVERTER.convert(destinationClass, source);
	}
	
	/**
	 * Converts the given <code>source</code> into an object of class
	 * <code>destinationClass</code>. The returned object may be a reference
	 * to <code>source</code> itself. This isn't an issue for immutable
	 * classes (String, Long, etc) but is an issue for Collection and Array
	 * types.
	 * 
	 * @param destinationClass
	 *            the destination class to convert
	 * @param source
	 *            the source object to convert
	 * @param locale
	 *            the locale in which the conversion should take place, or
	 *            <code>null</code> if the locale is not applicable
	 * @return the result of the conversion
	 * @throws TransformationException
	 *             if <code>destinationClass</code> is <code>null</code>,
	 *             an error occurred while performing the conversion
	 */
	public static Object convert(Class destinationClass, Object source, Locale locale)
		throws TransformationException {
		return DEFAULT_CONVERTER.convert(destinationClass, source, locale);
	}
	
	/**
	 * Converts the given <code>source</code> into a String that displays
	 * the information contained in the object in a format good for debugging.
	 * This is a great implementation of the <code>toString</code> method for
	 * an object.  FIXME actually, calling this method from the toString method
	 * of an object is likely to cause a StackOverflowException
	 * 
	 * @param source
	 *            the source object to convert
	 * @return the result of the conversion
	 * @throws TransformationException
	 *             if <code>destinationClass</code> is <code>null</code>,
	 *             an error occurred while performing the conversion
	 */
	public static String convertToPrettyString(Object source)
		throws TransformationException {
		return (String) DEFAULT_PRETTY_TEXT_CONVERTER.convert(String.class, source);
	}
	
	/**
	 * Converts the given <code>source</code> into a <code>BigDecimal</code>.
	 * 
	 * @param source
	 *            the source object to convert
	 * @return the result of the conversion
	 * @throws TransformationException
	 *             if <code>destinationClass</code> is <code>null</code>,
	 *             an error occurred while performing the conversion
	 */
	public static BigDecimal convertToBigDecimal(Object source) throws TransformationException {
		return (BigDecimal) convert(BigDecimal.class, source);
	}
	
	/**
	 * Converts the given <code>source</code> into a <code>BigDecimal</code>.
	 * 
	 * @param source
	 *            the source object to convert
	 * @param locale
	 *            the locale in which the conversion is to be performed
	 * @return the result of the conversion
	 * @throws TransformationException
	 *             if <code>destinationClass</code> is <code>null</code>,
	 *             an error occurred while performing the conversion
	 */
	public static BigDecimal convertToBigDecimal(Object source, Locale locale) throws TransformationException {
		return (BigDecimal) convert(BigDecimal.class, source, locale);
	}
	
	/**
	 * Converts the given <code>source</code> into a <code>BigInteger</code>.
	 * 
	 * @param source
	 *            the source object to convert
	 * @return the result of the conversion
	 * @throws TransformationException
	 *             if <code>destinationClass</code> is <code>null</code>,
	 *             an error occurred while performing the conversion
	 */
	public static BigInteger convertToBigInteger(Object source) throws TransformationException {
		return (BigInteger) convert(BigInteger.class, source);
	}
	
	/**
	 * Converts the given <code>source</code> into a <code>BigInteger</code>.
	 * 
	 * @param source
	 *            the source object to convert
	 * @param locale
	 *            the locale in which the conversion is to be performed
	 * @return the result of the conversion
	 * @throws TransformationException
	 *             if <code>destinationClass</code> is <code>null</code>,
	 *             an error occurred while performing the conversion
	 */
	public static BigInteger convertToBigInteger(Object source, Locale locale) throws TransformationException {
		return (BigInteger) convert(BigInteger.class, source, locale);
	}
	
	/**
	 * Converts the given <code>source</code> into a <code>boolean</code>.
	 * 
	 * @param source
	 *            the source object to convert
	 * @return the result of the conversion
	 * @throws TransformationException
	 *             if <code>destinationClass</code> is <code>null</code>,
	 *             an error occurred while performing the conversion
	 */
	public static boolean convertToBoolean(Object source) throws TransformationException {
		return ((Boolean) convert(boolean.class, source)).booleanValue();
	}
	
//	/**
//	 * Converts the given <code>source</code> into a <code>boolean</code>.
//	 * 
//	 * @param source
//	 *            the source object to convert
//	 * @param locale
//	 *            the locale in which the conversion is to be performed
//	 * @return the result of the conversion
//	 * @throws TransformationException
//	 *             if <code>destinationClass</code> is <code>null</code>,
//	 *             an error occurred while performing the conversion
//	 */
//	public static boolean convertToBoolean(Object source, Locale locale) throws TransformationException {
//		return ((Boolean) convert(boolean.class, source, locale)).booleanValue();
//	}
	
	/**
	 * Converts the given <code>source</code> into a <code>Boolean</code>.
	 * 
	 * @param source
	 *            the source object to convert
	 * @return the result of the conversion
	 * @throws TransformationException
	 *             if <code>destinationClass</code> is <code>null</code>,
	 *             an error occurred while performing the conversion
	 */
	public static Boolean convertToBooleanObject(Object source) throws TransformationException {
		return (Boolean) convert(Boolean.class, source);
	}
	
//	/**
//	 * Converts the given <code>source</code> into a <code>Boolean</code>.
//	 * 
//	 * @param source
//	 *            the source object to convert
//	 * @param locale
//	 *            the locale in which the conversion is to be performed
//	 * @return the result of the conversion
//	 * @throws TransformationException
//	 *             if <code>destinationClass</code> is <code>null</code>,
//	 *             an error occurred while performing the conversion
//	 */
//	public static Boolean convertToBooleanObject(Object source, Locale locale) throws TransformationException {
//		return (Boolean) convert(Boolean.class, source, locale);
//	}
	
	/**
	 * Converts the given <code>source</code> into a <code>byte</code>.
	 * 
	 * @param source
	 *            the source object to convert
	 * @return the result of the conversion
	 * @throws TransformationException
	 *             if <code>destinationClass</code> is <code>null</code>,
	 *             an error occurred while performing the conversion
	 */
	public static byte convertToByte(Object source) throws TransformationException {
		return ((Byte) convert(byte.class, source)).byteValue();
	}
	
	/**
	 * Converts the given <code>source</code> into a <code>byte</code>.
	 * 
	 * @param source
	 *            the source object to convert
	 * @param locale
	 *            the locale in which the conversion is to be performed
	 * @return the result of the conversion
	 * @throws TransformationException
	 *             if <code>destinationClass</code> is <code>null</code>,
	 *             an error occurred while performing the conversion
	 */
	public static byte convertToByte(Object source, Locale locale) throws TransformationException {
		return ((Byte) convert(byte.class, source, locale)).byteValue();
	}
	
	/**
	 * Converts the given <code>source</code> into a <code>Byte</code>.
	 * 
	 * @param source
	 *            the source object to convert
	 * @return the result of the conversion
	 * @throws TransformationException
	 *             if <code>destinationClass</code> is <code>null</code>,
	 *             an error occurred while performing the conversion
	 */
	public static Byte convertToByteObject(Object source) throws TransformationException {
		return (Byte) convert(Byte.class, source);
	}
	
	/**
	 * Converts the given <code>source</code> into a <code>Byte</code>.
	 * 
	 * @param source
	 *            the source object to convert
	 * @param locale
	 *            the locale in which the conversion is to be performed
	 * @return the result of the conversion
	 * @throws TransformationException
	 *             if <code>destinationClass</code> is <code>null</code>,
	 *             an error occurred while performing the conversion
	 */
	public static Byte convertToByteObject(Object source, Locale locale) throws TransformationException {
		return (Byte) convert(Byte.class, source, locale);
	}
	
	/**
	 * Converts the given <code>source</code> into a <code>Calendar</code>.
	 * 
	 * @param source
	 *            the source object to convert
	 * @return the result of the conversion
	 * @throws TransformationException
	 *             if <code>destinationClass</code> is <code>null</code>,
	 *             an error occurred while performing the conversion
	 */
	public static Calendar convertToCalendar(Object source) throws TransformationException {
		return (Calendar) convert(Calendar.class, source);
	}
	
	/**
	 * Converts the given <code>source</code> into a <code>Calendar</code>.
	 * 
	 * @param source
	 *            the source object to convert
	 * @param locale
	 *            the locale in which the conversion is to be performed
	 * @return the result of the conversion
	 * @throws TransformationException
	 *             if <code>destinationClass</code> is <code>null</code>,
	 *             an error occurred while performing the conversion
	 */
	public static Calendar convertToCalendar(Object source, Locale locale) throws TransformationException {
		return (Calendar) convert(Calendar.class, source, locale);
	}
	
	/**
	 * Converts the given <code>source</code> into a <code>Date</code>.
	 * 
	 * @param source
	 *            the source object to convert
	 * @return the result of the conversion
	 * @throws TransformationException
	 *             if <code>destinationClass</code> is <code>null</code>,
	 *             an error occurred while performing the conversion
	 */
	public static Date convertToDate(Object source) throws TransformationException {
		return (Date) convert(Date.class, source);
	}
	
	/**
	 * Converts the given <code>source</code> into a <code>Date</code>.
	 * 
	 * @param source
	 *            the source object to convert
	 * @param locale
	 *            the locale in which the conversion is to be performed
	 * @return the result of the conversion
	 * @throws TransformationException
	 *             if <code>destinationClass</code> is <code>null</code>,
	 *             an error occurred while performing the conversion
	 */
	public static Date convertToDate(Object source, Locale locale) throws TransformationException {
		return (Date) convert(Date.class, source, locale);
	}
	
	/**
	 * Converts the given <code>source</code> into a <code>double</code>.
	 * 
	 * @param source
	 *            the source object to convert
	 * @return the result of the conversion
	 * @throws TransformationException
	 *             if <code>destinationClass</code> is <code>null</code>,
	 *             an error occurred while performing the conversion
	 */
	public static double convertToDouble(Object source) throws TransformationException {
		return ((Double) convert(double.class, source)).doubleValue();
	}
	
	/**
	 * Converts the given <code>source</code> into a <code>double</code>.
	 * 
	 * @param source
	 *            the source object to convert
	 * @param locale
	 *            the locale in which the conversion is to be performed
	 * @return the result of the conversion
	 * @throws TransformationException
	 *             if <code>destinationClass</code> is <code>null</code>,
	 *             an error occurred while performing the conversion
	 */
	public static double convertToDouble(Object source, Locale locale) throws TransformationException {
		return ((Double) convert(double.class, source, locale)).doubleValue();
	}
	
	/**
	 * Converts the given <code>source</code> into a <code>Double</code>.
	 * 
	 * @param source
	 *            the source object to convert
	 * @return the result of the conversion
	 * @throws TransformationException
	 *             if <code>destinationClass</code> is <code>null</code>,
	 *             an error occurred while performing the conversion
	 */
	public static Double convertToDoubleObject(Object source) throws TransformationException {
		return (Double) convert(Double.class, source);
	}
	
	/**
	 * Converts the given <code>source</code> into a <code>Double</code>.
	 * 
	 * @param source
	 *            the source object to convert
	 * @param locale
	 *            the locale in which the conversion is to be performed
	 * @return the result of the conversion
	 * @throws TransformationException
	 *             if <code>destinationClass</code> is <code>null</code>,
	 *             an error occurred while performing the conversion
	 */
	public static Double convertToDoubleObject(Object source, Locale locale) throws TransformationException {
		return (Double) convert(Double.class, source, locale);
	}
	
	/**
	 * Converts the given <code>source</code> into a <code>float</code>.
	 * 
	 * @param source
	 *            the source object to convert
	 * @return the result of the conversion
	 * @throws TransformationException
	 *             if <code>destinationClass</code> is <code>null</code>,
	 *             an error occurred while performing the conversion
	 */
	public static float convertToFloat(Object source) throws TransformationException {
		return ((Float) convert(float.class, source)).floatValue();
	}
	
	/**
	 * Converts the given <code>source</code> into a <code>float</code>.
	 * 
	 * @param source
	 *            the source object to convert
	 * @param locale
	 *            the locale in which the conversion is to be performed
	 * @return the result of the conversion
	 * @throws TransformationException
	 *             if <code>destinationClass</code> is <code>null</code>,
	 *             an error occurred while performing the conversion
	 */
	public static float convertToFloat(Object source, Locale locale) throws TransformationException {
		return ((Float) convert(float.class, source, locale)).floatValue();
	}
	
	/**
	 * Converts the given <code>source</code> into a <code>Float</code>.
	 * 
	 * @param source
	 *            the source object to convert
	 * @return the result of the conversion
	 * @throws TransformationException
	 *             if <code>destinationClass</code> is <code>null</code>,
	 *             an error occurred while performing the conversion
	 */
	public static Float convertToFloatObject(Object source) throws TransformationException {
		return (Float) convert(Float.class, source);
	}
	
	/**
	 * Converts the given <code>source</code> into a <code>Float</code>.
	 * 
	 * @param source
	 *            the source object to convert
	 * @param locale
	 *            the locale in which the conversion is to be performed
	 * @return the result of the conversion
	 * @throws TransformationException
	 *             if <code>destinationClass</code> is <code>null</code>,
	 *             an error occurred while performing the conversion
	 */
	public static Float convertToFloatObject(Object source, Locale locale) throws TransformationException {
		return (Float) convert(Float.class, source, locale);
	}
	
	/**
	 * Converts the given <code>source</code> into a <code>int</code>.
	 * 
	 * @param source
	 *            the source object to convert
	 * @return the result of the conversion
	 * @throws TransformationException
	 *             if <code>destinationClass</code> is <code>null</code>,
	 *             an error occurred while performing the conversion
	 */
	public static int convertToInt(Object source) throws TransformationException {
		return ((Integer) convert(int.class, source)).intValue();
	}
	
	/**
	 * Converts the given <code>source</code> into a <code>int</code>.
	 * 
	 * @param source
	 *            the source object to convert
	 * @param locale
	 *            the locale in which the conversion is to be performed
	 * @return the result of the conversion
	 * @throws TransformationException
	 *             if <code>destinationClass</code> is <code>null</code>,
	 *             an error occurred while performing the conversion
	 */
	public static int convertToInt(Object source, Locale locale) throws TransformationException {
		return ((Integer) convert(int.class, source, locale)).intValue();
	}
	
	/**
	 * Converts the given <code>source</code> into a <code>Integer</code>.
	 * 
	 * @param source
	 *            the source object to convert
	 * @return the result of the conversion
	 * @throws TransformationException
	 *             if <code>destinationClass</code> is <code>null</code>,
	 *             an error occurred while performing the conversion
	 */
	public static Integer convertToIntegerObject(Object source) throws TransformationException {
		return (Integer) convert(Integer.class, source);
	}
	
	/**
	 * Converts the given <code>source</code> into a <code>Integer</code>.
	 * 
	 * @param source
	 *            the source object to convert
	 * @param locale
	 *            the locale in which the conversion is to be performed
	 * @return the result of the conversion
	 * @throws TransformationException
	 *             if <code>destinationClass</code> is <code>null</code>,
	 *             an error occurred while performing the conversion
	 */
	public static Integer convertToIntegerObject(Object source, Locale locale) throws TransformationException {
		return (Integer) convert(Integer.class, source, locale);
	}
	
	/**
	 * Converts the given <code>source</code> into a <code>long</code>.
	 * 
	 * @param source
	 *            the source object to convert
	 * @return the result of the conversion
	 * @throws TransformationException
	 *             if <code>destinationClass</code> is <code>null</code>,
	 *             an error occurred while performing the conversion
	 */
	public static long convertToLong(Object source) throws TransformationException {
		return ((Long) convert(long.class, source)).longValue();
	}
	
	/**
	 * Converts the given <code>source</code> into a <code>long</code>.
	 * 
	 * @param source
	 *            the source object to convert
	 * @param locale
	 *            the locale in which the conversion is to be performed
	 * @return the result of the conversion
	 * @throws TransformationException
	 *             if <code>destinationClass</code> is <code>null</code>,
	 *             an error occurred while performing the conversion
	 */
	public static long convertToLong(Object source, Locale locale) throws TransformationException {
		return ((Long) convert(long.class, source, locale)).longValue();
	}
	
	/**
	 * Converts the given <code>source</code> into a <code>Long</code>.
	 * 
	 * @param source
	 *            the source object to convert
	 * @return the result of the conversion
	 * @throws TransformationException
	 *             if <code>destinationClass</code> is <code>null</code>,
	 *             an error occurred while performing the conversion
	 */
	public static Long convertToLongObject(Object source) throws TransformationException {
		return (Long) convert(Long.class, source);
	}
	
	/**
	 * Converts the given <code>source</code> into a <code>Long</code>.
	 * 
	 * @param source
	 *            the source object to convert
	 * @param locale
	 *            the locale in which the conversion is to be performed
	 * @return the result of the conversion
	 * @throws TransformationException
	 *             if <code>destinationClass</code> is <code>null</code>,
	 *             an error occurred while performing the conversion
	 */
	public static Long convertToLongObject(Object source, Locale locale) throws TransformationException {
		return (Long) convert(Long.class, source, locale);
	}
	
	/**
	 * Converts the given <code>source</code> into a <code>String</code>.
	 * 
	 * @param source
	 *            the source object to convert
	 * @return the result of the conversion
	 * @throws TransformationException
	 *             if <code>destinationClass</code> is <code>null</code>,
	 *             an error occurred while performing the conversion
	 */
	public static String convertToString(Object source) throws TransformationException {
		return (String) convert(String.class, source);
	}
	
	/**
	 * Converts the given <code>source</code> into a <code>String</code>.
	 * 
	 * @param source
	 *            the source object to convert
	 * @param locale
	 *            the locale in which the conversion should take place
	 * @return the result of the conversion
	 * @throws TransformationException
	 *             if <code>destinationClass</code> is <code>null</code>,
	 *             an error occurred while performing the conversion
	 */
	public static String convertToString(Object source, Locale locale) throws TransformationException {
		return (String) convert(String.class, source, locale);
	}
	
	/**
	 * Retrieve the property indicated by <code>expression</code> from
	 * <code>target</code>.
	 * 
	 * @param target
	 *            the object from which information will be retrieved
	 * @param expression
	 *            an expression specifying which information to retrieve
	 * @return the information indicated by <code>expression</code> from
	 *         <code>target</code>
	 * @throws LanguageException
	 *             if <code>target</code> is <code>null</code> or <br>
	 *             an error occurrs while evaluating an otherwise valid
	 *             expression
	 * @throws InvalidExpressionException
	 *             if <code>expression</code> is empty or not a valid
	 *             expression
	 */
	public static Object get(Object target, String expression)
		throws LanguageException {
		return DEFAULT_LANGUAGE.get(target, expression);
	}
	
	/**
	 * Retrieve the information indicated by <code>expression</code> from
	 * <code>target</code> as the type indicated by
	 * <code>destinationClass</code>.
	 * 
	 * @param target
	 *            the object from which information will be retrieved
	 * @param expression
	 *            an expression specifying which information to retrieve
	 * @param destinationClass
	 *            indicates the type that should be returned by this method
	 * @return the information indicated by <code>expression</code> from
	 *         <code>target</code>
	 * @throws TransformationException
	 *             if an error occurs while converting the requested information
	 *             to the type indicated by <code>destinationClass</code>
	 * @throws LanguageException
	 *             if <code>target</code> is <code>null</code> or <br>
	 *             an error occurrs while evaluating an otherwise valid
	 *             expression
	 * @throws InvalidExpressionException
	 *             if <code>expression</code> is empty or not a valid
	 *             expression
	 */
	public static Object get(Object target, String expression, Class destinationClass)
		throws LanguageException, TransformationException {
		return DEFAULT_LANGUAGE.get(target, expression, destinationClass);
	}
	
	/**
	 * Retrieve the information indicated by <code>expression</code> from
	 * <code>target</code> as the type indicated by
	 * <code>destinationClass</code>.
	 * 
	 * @param target
	 *            the object from which information will be retrieved
	 * @param expression
	 *            an expression specifying which information to retrieve
	 * @param destinationClass
	 *            indicates the type that should be returned by this method
	 * @param locale
	 *            indicates the locale in which the conversion to type
	 *            <code>destinationClass</code> should be performed, if
	 *            applicable
	 * @return the information indicated by <code>expression</code> from
	 *         <code>target</code>
	 * @throws TransformationException
	 *             if an error occurs while converting the requested information
	 *             to the type indicated by <code>destinationClass</code>
	 * @throws LanguageException
	 *             if <code>target</code> is <code>null</code> or <br>
	 *             an error occurrs while evaluating an otherwise valid
	 *             expression
	 * @throws InvalidExpressionException
	 *             if <code>expression</code> is empty or not a valid
	 *             expression
	 */
	public static Object get(Object target, String expression, Class destinationClass,
		Locale locale) throws LanguageException, TransformationException {
		return DEFAULT_LANGUAGE.get(target, expression, destinationClass, locale);
	}
	
	/**
	 * Retrieve the information indicated by <code>expression</code> from
	 * <code>target</code> as the type indicated by
	 * <code>destinationClass</code>.
	 * 
	 * @param target
	 *            the object from which information will be retrieved
	 * @param expression
	 *            an expression specifying which information to retrieve
	 * @param locale
	 *            indicates the locale in which the conversion to type
	 *            <code>destinationClass</code> should be performed, if
	 *            applicable
	 * @param destinationClass
	 *            indicates the type that should be returned by this method
	 * @return the information indicated by <code>expression</code> from
	 *         <code>target</code>
	 * @throws TransformationException
	 *             if an error occurs while converting the requested information
	 *             to the type indicated by <code>destinationClass</code>
	 * @throws LanguageException
	 *             if <code>target</code> is <code>null</code> or <br>
	 *             an error occurrs while evaluating an otherwise valid
	 *             expression
	 * @throws InvalidExpressionException
	 *             if <code>expression</code> is empty or not a valid
	 *             expression
	 */
	public static Object get(Object target, String expression, Locale locale,
		Class destinationClass) throws LanguageException, TransformationException {
		return DEFAULT_LANGUAGE.get(target, expression, locale, destinationClass);
	}
	
	/**
	 * Sets the information indicated by <code>expression</code> on
	 * <code>target</code>.  <code>value</code> will be automatically
	 * converted to a type appropriate for the given <code>expression</code>.
	 * 
	 * @param target
	 *            the object that will be modified
	 * @param expression
	 *            an expression specifying which information will be modified
	 * @param value
	 *            the information to be changed
	 * @throws TransformationException
	 *             if an error occurs while converting <code>value</code> to
	 *             the appropriate type
	 * @throws LanguageException
	 *             if <code>target</code> is <code>null</code> or <br>
	 *             an error occurrs while evaluating an otherwise valid
	 *             expression
	 * @throws InvalidExpressionException
	 *             if <code>expression</code> is empty or not a valid
	 *             expression
	 */
	public static void set(Object target, String expression, Object value)
		throws LanguageException, TransformationException {
		DEFAULT_LANGUAGE.set(target, expression, value);
	}
	
	/**
	 * Sets the information indicated by <code>expression</code> on
	 * <code>target</code>.  <code>value</code> will be automatically
	 * converted to a type appropriate for the given <code>expression</code>.
	 * 
	 * @param target
	 *            the object that will be modified
	 * @param expression
	 *            an expression specifying which information will be modified
	 * @param value
	 *            the information to be changed
	 * @param locale
	 *            indicates the locale in which the conversion to type
	 *            <code>destinationClass</code> should be performed, if
	 *            applicable
	 * @throws TransformationException
	 *             if an error occurs while converting <code>value</code> to
	 *             the appropriate type
	 * @throws LanguageException
	 *             if <code>target</code> is <code>null</code> or <br>
	 *             an error occurrs while evaluating an otherwise valid
	 *             expression
	 * @throws InvalidExpressionException
	 *             if <code>expression</code> is empty or not a valid
	 *             expression
	 */
	public static void set(Object target, String expression, Object value,
		Locale locale) throws LanguageException, TransformationException {
		DEFAULT_LANGUAGE.set(target, expression, value, locale);
	}
	
	/**
	 * <p>
	 * Copies information from the given source to the given destination.
	 * </p>
	 * 
	 * @param destination
	 *            the object to which information is written
	 * @param source
	 *            the object from which information is read
	 * @throws TransformationException
	 *             if <code>source</code> or <code>destination</code> are
	 *             null
	 */
	
	public static void copy(Object destination, Object source) throws TransformationException {
		DEFAULT_COPIER.copy(destination, source);
	}

	/**
	 * <p>
	 * Copies information from the given source to the given destination.
	 * </p>
	 * 
	 * @param destination
	 *            the object to which information is written
	 * @param source
	 *            the object from which information is read
	 * @param locale
	 *            the locale of the current user, which may be null if the
	 *            locale is unknown or not applicable
	 * @throws TransformationException
	 *             if <code>source</code> or <code>destination</code> are
	 *             null
	 */
	public static void copy(Object destination, Object source, Locale locale)
		throws TransformationException {
		DEFAULT_COPIER.copy(destination, source, locale);
	}

	/**
	 * Returns the given object wrapped as a Bean.
	 * 
	 * @param object
	 *            the object to be wrapped
	 * @return the wrapped object
	 * @throws WrapperException
	 *             if the wrapper could not be retrieved
	 */
	public static Bean getBean(Object object) throws WrapperException {
		return (Bean) DEFAULT_REFLECTOR.getWrapper(object);
	}
	
	/**
	 * Returns the given object wrapped as a Container.
	 * 
	 * @param object
	 *            the object to be wrapped
	 * @return the wrapped object
	 * @throws WrapperException
	 *             if the wrapper could not be retrieved
	 */
	public static Container getContainer(Object object) throws WrapperException {
		return (Container) DEFAULT_REFLECTOR.getWrapper(object);
	}
	
	/**
	 * Returns the given object wrapped as a GrowableContainer.
	 * 
	 * @param object
	 *            the object to be wrapped
	 * @return the wrapped object
	 * @throws WrapperException
	 *             if the wrapper could not be retrieved
	 */
	public static GrowableContainer getGrowableContainer(Object object) throws WrapperException {
		return (GrowableContainer) DEFAULT_REFLECTOR.getWrapper(object);
	}
	
	/**
	 * Returns the given object wrapped as an IndexedContainer.
	 * 
	 * @param object
	 *            the object to be wrapped
	 * @return the wrapped object
	 * @throws WrapperException
	 *             if the wrapper could not be retrieved
	 */
	public static IndexedContainer getIndexedContainer(Object object) throws WrapperException {
		return (IndexedContainer) DEFAULT_REFLECTOR.getWrapper(object);
	}
	
	/**
	 * Returns the given object wrapped as a MutableIndexedContainer.
	 * 
	 * @param object
	 *            the object to be wrapped
	 * @return the wrapped object
	 * @throws WrapperException
	 *             if the wrapper could not be retrieved
	 */
	public static MutableIndexedContainer getMutableIndexedContainer(Object object) throws WrapperException {
		return (MutableIndexedContainer) DEFAULT_REFLECTOR.getWrapper(object);
	}

	/**
	 * Retrieve the information indicated by <code>expression</code> from
	 * <code>target</code> as a <code>BigDecimal</code>.
	 * 
	 * @param target
	 *            the object from which information will be retrieved
	 * @param expression
	 *            an expression specifying which information to retrieve
	 * @return the information indicated by <code>expression</code> from
	 *         <code>target</code> as a <code>BigDecimal</code>
	 * @throws TransformationException
	 *             if an error occurs while converting the requested information
	 *             to a <code>BigDecimal</code>
	 * @throws LanguageException
	 *             if <code>target</code> is <code>null</code> or <br>
	 *             an error occurrs while evaluating an otherwise valid
	 *             expression
	 * @throws InvalidExpressionException
	 *             if <code>expression</code> is empty or not a valid
	 *             expression
	 */
    public static BigDecimal getBigDecimal(Object target, String expression) {
    	return (BigDecimal) Morph.get(target, expression, BigDecimal.class);
    }

	/**
	 * Retrieve the information indicated by <code>expression</code> from
	 * <code>target</code> as a <code>BigInteger</code>.
	 * 
	 * @param target
	 *            the object from which information will be retrieved
	 * @param expression
	 *            an expression specifying which information to retrieve
	 * @return the information indicated by <code>expression</code> from
	 *         <code>target</code> as a <code>BigInteger</code>
	 * @throws TransformationException
	 *             if an error occurs while converting the requested information
	 *             to a <code>BigInteger</code>
	 * @throws LanguageException
	 *             if <code>target</code> is <code>null</code> or <br>
	 *             an error occurrs while evaluating an otherwise valid
	 *             expression
	 * @throws InvalidExpressionException
	 *             if <code>expression</code> is empty or not a valid
	 *             expression
	 */
    public static BigInteger getBigInteger(Object target, String expression) {
    	return (BigInteger) Morph.get(target, expression, BigInteger.class);
    }

	/**
	 * Retrieve the information indicated by <code>expression</code> from
	 * <code>target</code> as a <code>boolean</code>.
	 * 
	 * @param target
	 *            the object from which information will be retrieved
	 * @param expression
	 *            an expression specifying which information to retrieve
	 * @return the information indicated by <code>expression</code> from
	 *         <code>target</code> as a <code>boolean</code>
	 * @throws TransformationException
	 *             if an error occurs while converting the requested information
	 *             to a <code>boolean</code>
	 * @throws LanguageException
	 *             if <code>target</code> is <code>null</code> or <br>
	 *             an error occurrs while evaluating an otherwise valid
	 *             expression
	 * @throws InvalidExpressionException
	 *             if <code>expression</code> is empty or not a valid
	 *             expression
	 */
    public static boolean getBoolean(Object target, String expression) {
    	return ((Boolean) Morph.get(target, expression, boolean.class)).booleanValue();
    }

	/**
	 * Retrieve the information indicated by <code>expression</code> from
	 * <code>target</code> as a <code>Boolean</code> object.
	 * 
	 * @param target
	 *            the object from which information will be retrieved
	 * @param expression
	 *            an expression specifying which information to retrieve
	 * @return the information indicated by <code>expression</code> from
	 *         <code>target</code> as a <code>Boolean</code> object
	 * @throws TransformationException
	 *             if an error occurs while converting the requested information
	 *             to a <code>Boolean</code> object
	 * @throws LanguageException
	 *             if <code>target</code> is <code>null</code> or <br>
	 *             an error occurrs while evaluating an otherwise valid
	 *             expression
	 * @throws InvalidExpressionException
	 *             if <code>expression</code> is empty or not a valid
	 *             expression
	 */
    public static Boolean getBooleanObject(Object target, String expression) {
    	return (Boolean) Morph.get(target, expression, Boolean.class);
    }

	/**
	 * Sets the information indicated by <code>expression</code> on
	 * <code>target</code>.  <code>value</code> will be automatically
	 * converted to a type appropriate for the given <code>expression</code>.
	 * 
	 * @param target
	 *            the object that will be modified
	 * @param expression
	 *            an expression specifying which information will be modified
	 * @param value
	 *            the information to be changed
	 * @throws TransformationException
	 *             if an error occurs while converting <code>value</code> to
	 *             the appropriate type
	 * @throws LanguageException
	 *             if <code>target</code> is <code>null</code> or <br>
	 *             an error occurrs while evaluating an otherwise valid
	 *             expression
	 * @throws InvalidExpressionException
	 *             if <code>expression</code> is empty or not a valid
	 *             expression
	 */
    public static void set(Object target, String expression, boolean value) {
    	set(target, expression, Boolean.valueOf(value));
    }    

	/**
	 * Retrieve the information indicated by <code>expression</code> from
	 * <code>target</code> as a <code>byte</code>.
	 * 
	 * @param target
	 *            the object from which information will be retrieved
	 * @param expression
	 *            an expression specifying which information to retrieve
	 * @return the information indicated by <code>expression</code> from
	 *         <code>target</code> as a <code>byte</code>
	 * @throws TransformationException
	 *             if an error occurs while converting the requested information
	 *             to a <code>byte</code>
	 * @throws LanguageException
	 *             if <code>target</code> is <code>null</code> or <br>
	 *             an error occurrs while evaluating an otherwise valid
	 *             expression
	 * @throws InvalidExpressionException
	 *             if <code>expression</code> is empty or not a valid
	 *             expression
	 */
    public static byte getByte(Object target, String expression) {
    	return ((Byte) Morph.get(target, expression, byte.class)).byteValue();
    }

	/**
	 * Retrieve the information indicated by <code>expression</code> from
	 * <code>target</code> as a <code>Byte</code> object.
	 * 
	 * @param target
	 *            the object from which information will be retrieved
	 * @param expression
	 *            an expression specifying which information to retrieve
	 * @return the information indicated by <code>expression</code> from
	 *         <code>target</code> as a <code>Byte</code> object
	 * @throws TransformationException
	 *             if an error occurs while converting the requested information
	 *             to a <code>Byte</code> object
	 * @throws LanguageException
	 *             if <code>target</code> is <code>null</code> or <br>
	 *             an error occurrs while evaluating an otherwise valid
	 *             expression
	 * @throws InvalidExpressionException
	 *             if <code>expression</code> is empty or not a valid
	 *             expression
	 */
	public static Byte getByteObject(Object target, String expression) {
    	return (Byte) Morph.get(target, expression, Byte.class);
    }

	/**
	 * Sets the information indicated by <code>expression</code> on
	 * <code>target</code>.  <code>value</code> will be automatically
	 * converted to a type appropriate for the given <code>expression</code>.
	 * 
	 * @param target
	 *            the object that will be modified
	 * @param expression
	 *            an expression specifying which information will be modified
	 * @param value
	 *            the information to be changed
	 * @throws TransformationException
	 *             if an error occurs while converting <code>value</code> to
	 *             the appropriate type
	 * @throws LanguageException
	 *             if <code>target</code> is <code>null</code> or <br>
	 *             an error occurrs while evaluating an otherwise valid
	 *             expression
	 * @throws InvalidExpressionException
	 *             if <code>expression</code> is empty or not a valid
	 *             expression
	 */
    public static void set(Object target, String expression, byte value) {
    	set(target, expression, new Byte(value));
    }

	/**
	 * Retrieve the information indicated by <code>expression</code> from
	 * <code>target</code> as a <code>double</code>.
	 * 
	 * @param target
	 *            the object from which information will be retrieved
	 * @param expression
	 *            an expression specifying which information to retrieve
	 * @return the information indicated by <code>expression</code> from
	 *         <code>target</code> as a <code>double</code>
	 * @throws TransformationException
	 *             if an error occurs while converting the requested information
	 *             to a <code>double</code>
	 * @throws LanguageException
	 *             if <code>target</code> is <code>null</code> or <br>
	 *             an error occurrs while evaluating an otherwise valid
	 *             expression
	 * @throws InvalidExpressionException
	 *             if <code>expression</code> is empty or not a valid
	 *             expression
	 */
    public static double getDouble(Object target, String expression) {
    	return ((Double) Morph.get(target, expression, double.class)).doubleValue();
    }

	/**
	 * Retrieve the information indicated by <code>expression</code> from
	 * <code>target</code> as a <code>Double</code> object.
	 * 
	 * @param target
	 *            the object from which information will be retrieved
	 * @param expression
	 *            an expression specifying which information to retrieve
	 * @return the information indicated by <code>expression</code> from
	 *         <code>target</code> as a <code>Double</code> object
	 * @throws TransformationException
	 *             if an error occurs while converting the requested information
	 *             to a <code>Double</code> object
	 * @throws LanguageException
	 *             if <code>target</code> is <code>null</code> or <br>
	 *             an error occurrs while evaluating an otherwise valid
	 *             expression
	 * @throws InvalidExpressionException
	 *             if <code>expression</code> is empty or not a valid
	 *             expression
	 */
    public static Double getDoubleObject(Object target, String expression) {
    	return (Double) Morph.get(target, expression, Double.class);
    }

	/**
	 * Sets the information indicated by <code>expression</code> on
	 * <code>target</code>.  <code>value</code> will be automatically
	 * converted to a type appropriate for the given <code>expression</code>.
	 * 
	 * @param target
	 *            the object that will be modified
	 * @param expression
	 *            an expression specifying which information will be modified
	 * @param value
	 *            the information to be changed
	 * @throws TransformationException
	 *             if an error occurs while converting <code>value</code> to
	 *             the appropriate type
	 * @throws LanguageException
	 *             if <code>target</code> is <code>null</code> or <br>
	 *             an error occurrs while evaluating an otherwise valid
	 *             expression
	 * @throws InvalidExpressionException
	 *             if <code>expression</code> is empty or not a valid
	 *             expression
	 */
    public static void set(Object target, String expression, double value) {
    	set(target, expression, new Double(value));
    }

	/**
	 * Retrieve the information indicated by <code>expression</code> from
	 * <code>target</code> as a <code>float</code>.
	 * 
	 * @param target
	 *            the object from which information will be retrieved
	 * @param expression
	 *            an expression specifying which information to retrieve
	 * @return the information indicated by <code>expression</code> from
	 *         <code>target</code> as a <code>float</code>
	 * @throws TransformationException
	 *             if an error occurs while converting the requested information
	 *             to a <code>float</code>
	 * @throws LanguageException
	 *             if <code>target</code> is <code>null</code> or <br>
	 *             an error occurrs while evaluating an otherwise valid
	 *             expression
	 * @throws InvalidExpressionException
	 *             if <code>expression</code> is empty or not a valid
	 *             expression
	 */
    public static float getFloat(Object target, String expression) {
    	return ((Float) Morph.get(target, expression, float.class)).floatValue();
    }

	/**
	 * Retrieve the information indicated by <code>expression</code> from
	 * <code>target</code> as a <code>Float</code> object.
	 * 
	 * @param target
	 *            the object from which information will be retrieved
	 * @param expression
	 *            an expression specifying which information to retrieve
	 * @return the information indicated by <code>expression</code> from
	 *         <code>target</code> as a <code>Float</code> object
	 * @throws TransformationException
	 *             if an error occurs while converting the requested information
	 *             to a <code>Float</code> object
	 * @throws LanguageException
	 *             if <code>target</code> is <code>null</code> or <br>
	 *             an error occurrs while evaluating an otherwise valid
	 *             expression
	 * @throws InvalidExpressionException
	 *             if <code>expression</code> is empty or not a valid
	 *             expression
	 */
    public static Float getFloatObject(Object target, String expression) {
    	return (Float) Morph.get(target, expression, Float.class);
    }

	/**
	 * Sets the information indicated by <code>expression</code> on
	 * <code>target</code>.  <code>value</code> will be automatically
	 * converted to a type appropriate for the given <code>expression</code>.
	 * 
	 * @param target
	 *            the object that will be modified
	 * @param expression
	 *            an expression specifying which information will be modified
	 * @param value
	 *            the information to be changed
	 * @throws TransformationException
	 *             if an error occurs while converting <code>value</code> to
	 *             the appropriate type
	 * @throws LanguageException
	 *             if <code>target</code> is <code>null</code> or <br>
	 *             an error occurrs while evaluating an otherwise valid
	 *             expression
	 * @throws InvalidExpressionException
	 *             if <code>expression</code> is empty or not a valid
	 *             expression
	 */
    public static void set(Object target, String expression, float value) {
    	set(target, expression, new Float(value));
    }
    
	/**
	 * Retrieve the information indicated by <code>expression</code> from
	 * <code>target</code> as a <code>int</code>.
	 * 
	 * @param target
	 *            the object from which information will be retrieved
	 * @param expression
	 *            an expression specifying which information to retrieve
	 * @return the information indicated by <code>expression</code> from
	 *         <code>target</code> as a <code>int</code>
	 * @throws TransformationException
	 *             if an error occurs while converting the requested information
	 *             to a <code>int</code>
	 * @throws LanguageException
	 *             if <code>target</code> is <code>null</code> or <br>
	 *             an error occurrs while evaluating an otherwise valid
	 *             expression
	 * @throws InvalidExpressionException
	 *             if <code>expression</code> is empty or not a valid
	 *             expression
	 */
    public static int getInt(Object target, String expression) {
    	return ((Integer) Morph.get(target, expression, int.class)).intValue();
    }

	/**
	 * Retrieve the information indicated by <code>expression</code> from
	 * <code>target</code> as a <code>Integer</code> object.
	 * 
	 * @param target
	 *            the object from which information will be retrieved
	 * @param expression
	 *            an expression specifying which information to retrieve
	 * @return the information indicated by <code>expression</code> from
	 *         <code>target</code> as a <code>Integer</code> object
	 * @throws TransformationException
	 *             if an error occurs while converting the requested information
	 *             to a <code>Integer</code> object
	 * @throws LanguageException
	 *             if <code>target</code> is <code>null</code> or <br>
	 *             an error occurrs while evaluating an otherwise valid
	 *             expression
	 * @throws InvalidExpressionException
	 *             if <code>expression</code> is empty or not a valid
	 *             expression
	 */
    public static Integer getIntegerObject(Object target, String expression) {
    	return (Integer) Morph.get(target, expression, Integer.class);
    }

	/**
	 * Sets the information indicated by <code>expression</code> on
	 * <code>target</code>.  <code>value</code> will be automatically
	 * converted to a type appropriate for the given <code>expression</code>.
	 * 
	 * @param target
	 *            the object that will be modified
	 * @param expression
	 *            an expression specifying which information will be modified
	 * @param value
	 *            the information to be changed
	 * @throws TransformationException
	 *             if an error occurs while converting <code>value</code> to
	 *             the appropriate type
	 * @throws LanguageException
	 *             if <code>target</code> is <code>null</code> or <br>
	 *             an error occurrs while evaluating an otherwise valid
	 *             expression
	 * @throws InvalidExpressionException
	 *             if <code>expression</code> is empty or not a valid
	 *             expression
	 */
    public static void set(Object target, String expression, int value) {
    	set(target, expression, new Integer(value));
    }

	/**
	 * Retrieve the information indicated by <code>expression</code> from
	 * <code>target</code> as a <code>long</code>.
	 * 
	 * @param target
	 *            the object from which information will be retrieved
	 * @param expression
	 *            an expression specifying which information to retrieve
	 * @return the information indicated by <code>expression</code> from
	 *         <code>target</code> as a <code>long</code>
	 * @throws TransformationException
	 *             if an error occurs while converting the requested information
	 *             to a <code>long</code>
	 * @throws LanguageException
	 *             if <code>target</code> is <code>null</code> or <br>
	 *             an error occurrs while evaluating an otherwise valid
	 *             expression
	 * @throws InvalidExpressionException
	 *             if <code>expression</code> is empty or not a valid
	 *             expression
	 */
    public static long getLong(Object target, String expression) {
    	return ((Long) Morph.get(target, expression, long.class)).longValue();
    }

	/**
	 * Retrieve the information indicated by <code>expression</code> from
	 * <code>target</code> as a <code>Long</code> object.
	 * 
	 * @param target
	 *            the object from which information will be retrieved
	 * @param expression
	 *            an expression specifying which information to retrieve
	 * @return the information indicated by <code>expression</code> from
	 *         <code>target</code> as a <code>Long</code> object
	 * @throws TransformationException
	 *             if an error occurs while converting the requested information
	 *             to a <code>Long</code> object
	 * @throws LanguageException
	 *             if <code>target</code> is <code>null</code> or <br>
	 *             an error occurrs while evaluating an otherwise valid
	 *             expression
	 * @throws InvalidExpressionException
	 *             if <code>expression</code> is empty or not a valid
	 *             expression
	 */
    public static Long getLongObject(Object target, String expression) {
    	return (Long) Morph.get(target, expression, Long.class);
    }

	/**
	 * Sets the information indicated by <code>expression</code> on
	 * <code>target</code>.  <code>value</code> will be automatically
	 * converted to a type appropriate for the given <code>expression</code>.
	 * 
	 * @param target
	 *            the object that will be modified
	 * @param expression
	 *            an expression specifying which information will be modified
	 * @param value
	 *            the information to be changed
	 * @throws TransformationException
	 *             if an error occurs while converting <code>value</code> to
	 *             the appropriate type
	 * @throws LanguageException
	 *             if <code>target</code> is <code>null</code> or <br>
	 *             an error occurrs while evaluating an otherwise valid
	 *             expression
	 * @throws InvalidExpressionException
	 *             if <code>expression</code> is empty or not a valid
	 *             expression
	 */
    public static void set(Object target, String expression, long value) {
    	set(target, expression, new Long(value));
    }
    
	/**
	 * Retrieve the information indicated by <code>expression</code> from
	 * <code>target</code> as a <code>Date</code>.
	 * 
	 * @param target
	 *            the object from which information will be retrieved
	 * @param expression
	 *            an expression specifying which information to retrieve
	 * @return the information indicated by <code>expression</code> from
	 *         <code>target</code> as a <code>Date</code>
	 * @throws TransformationException
	 *             if an error occurs while converting the requested information
	 *             to a <code>Date</code>
	 * @throws LanguageException
	 *             if <code>target</code> is <code>null</code> or <br>
	 *             an error occurrs while evaluating an otherwise valid
	 *             expression
	 * @throws InvalidExpressionException
	 *             if <code>expression</code> is empty or not a valid
	 *             expression
	 */
    public static Date getDate(Object target, String expression) {
    	return (Date) Morph.get(target, expression, Date.class);
    }

	/**
	 * Retrieve the information indicated by <code>expression</code> from
	 * <code>target</code> as a <code>String</code>.
	 * 
	 * @param target
	 *            the object from which information will be retrieved
	 * @param expression
	 *            an expression specifying which information to retrieve
	 * @return the information indicated by <code>expression</code> from
	 *         <code>target</code> as a <code>String</code>
	 * @throws TransformationException
	 *             if an error occurs while converting the requested information
	 *             to a <code>String</code>
	 * @throws LanguageException
	 *             if <code>target</code> is <code>null</code> or <br>
	 *             an error occurrs while evaluating an otherwise valid
	 *             expression
	 * @throws InvalidExpressionException
	 *             if <code>expression</code> is empty or not a valid
	 *             expression
	 */
    public static String getString(Object target, String expression) {
    	return (String) Morph.get(target, expression, String.class);
    }

	/**
	 * Retrieve the information indicated by <code>expression</code> from
	 * <code>target</code> as a <code>String</code>.
	 * 
	 * @param target
	 *            the object from which information will be retrieved
	 * @param expression
	 *            an expression specifying which information to retrieve
	 * @param locale
	 *            the locale in which the conversion should take place, or
	 *            <code>null</code> if the locale is not applicable
	 * @return the information indicated by <code>expression</code> from
	 *         <code>target</code> as a <code>String</code>
	 * @throws TransformationException
	 *             if an error occurs while converting the requested information
	 *             to a <code>String</code>
	 * @throws LanguageException
	 *             if <code>target</code> is <code>null</code> or <br>
	 *             an error occurrs while evaluating an otherwise valid
	 *             expression
	 * @throws InvalidExpressionException
	 *             if <code>expression</code> is empty or not a valid
	 *             expression
	 */
	public static String getString(Object target, String expression, Locale locale) {
    	return (String) Morph.get(target, expression, String.class, locale);
    }

	/**
	 * Retrieve the information indicated by <code>expression</code> from
	 * <code>target</code> as a <code>Calendar</code>.
	 * 
	 * @param target
	 *            the object from which information will be retrieved
	 * @param expression
	 *            an expression specifying which information to retrieve
	 * @return the information indicated by <code>expression</code> from
	 *         <code>target</code> as a <code>Calendar</code>
	 * @throws TransformationException
	 *             if an error occurs while converting the requested information
	 *             to a <code>Calendar</code>
	 * @throws LanguageException
	 *             if <code>target</code> is <code>null</code> or <br>
	 *             an error occurrs while evaluating an otherwise valid
	 *             expression
	 * @throws InvalidExpressionException
	 *             if <code>expression</code> is empty or not a valid
	 *             expression
	 */
    public static Calendar getCalendar(Object target, String expression) {
    	return (Calendar) Morph.get(target, expression, Calendar.class);
    }
    
    /**
	 * Gets the names of the properties which are currently defined for the
	 * given bean. Note that some beans (e.g. - Maps) allow the creation of new
	 * properties, which means isWriteable may return true for property names
	 * that are not included in the return value of this method.
	 * 
	 * @param bean
	 *            the bean for which we would like a list of properties
	 * @return the names of the properties which are currently defined for the
	 *         given bean. Note that some beans (e.g. - Maps) allow the creation
	 *         of new properties, which means isWriteable may return true for
	 *         property names that are not included in the return value of this
	 *         method.
	 * @throws ReflectionException
	 *             if bean is <code>null</code>
	 */
	public static String[] getPropertyNames(Object bean) throws ReflectionException {
		return DEFAULT_BEAN_REFLECTOR.getPropertyNames(bean);
	}

	/**
	 * Specifies the least restrictive type that may be assigned to the given
	 * property. In the case of a weakly typed bean, the correct value to return
	 * is simply <code>Object.class</code>, which indicates that any type can
	 * be assigned to the given property.
	 * 
	 * @param bean
	 *            the bean
	 * @param propertyName
	 *            the name of the property
	 * @return the least restrictive type that may be assigned to the given
	 *         property. In the case of a weakly typed bean, the correct value
	 *         to return is simply <code>Object.class</code>, which indicates
	 *         that any type can be assigned to the given property
	 * @throws ReflectionException
	 *             if <code>bean</code> or <code>propertyName</code> are
	 *             <code>null</code> or <br>
	 *             if the type could not be retrieved for some reason
	 */
	public static Class getType(Object bean, String propertyName)
		throws ReflectionException {
		return DEFAULT_BEAN_REFLECTOR.getType(bean, propertyName);
	}

	/**
	 * Specifies whether the given property is readable. A reflector can always
	 * determine if a property is readable by attempting to read the property
	 * value, so this method can be counted on to truly indicate whether or not
	 * the given property is readable.
	 * 
	 * @param bean
	 *            the bean
	 * @param propertyName
	 *            the name of the property
	 * @return <code>true</code> if the property is readable, or <br>
	 *         <code>false</code>, otherwise
	 * @throws ReflectionException
	 *             if <code>bean</code> or <code>propertyName</code> are
	 *             <code>null</code> or <br>
	 *             if the readability of the property cannot be determined
	 */
	public static boolean isReadable(Object bean, String propertyName)
		throws ReflectionException {
		return DEFAULT_BEAN_REFLECTOR.isReadable(bean, propertyName);
	}

	/**
	 * Specifies whether the given property is writeable. If the reflector
	 * cannot determine whether the given property is writeable, it may simply
	 * return <code>true</code>. This method only guarantees that if
	 * <code>isWriteable</code> returns false, the method is not writeable.
	 * The method may or may not be writeable if this method returns
	 * <code>true</code>.
	 * 
	 * @param bean
	 *            the bean
	 * @param propertyName
	 *            the name of the property
	 * @return <code>false</code> if the property is not writeable or <br>
	 *         <code>true</code> if the property is writeable or if this
	 *         reflector cannot determine for sure whether or not the property
	 *         is writeable
	 * @throws ReflectionException
	 *             if <code>bean</code> or <code>propertyName</code> are
	 *             <code>null</code> or <br>
	 *             if the writeability of the property cannot be determined
	 */
	public static boolean isWriteable(Object bean, String propertyName)
		throws ReflectionException {
		return DEFAULT_BEAN_REFLECTOR.isWriteable(bean, propertyName);
	}

	/**
	 * Returns the type of the elements that are contained in objects of the
	 * given class. For example, if <code>indexedClass</code> represents an
	 * array of <code>int</code>s,<code>Integer.TYPE</code> should be
	 * returned. This method should only be called if
	 * {@link Reflector#isReflectable(Class)}returns <code>true</code>.
	 * 
	 * @param clazz
	 *            the container's type
	 * @return the type of the elements that are container by the given object
	 * @throws ReflectionException
	 *             if <code>container</code> is null or <br>
	 *             the type of the elements that are container could not be
	 *             determined
	 */
	public Class getContainedType(Class clazz) throws ReflectionException {
		return DEFAULT_CONTAINER_REFLECTOR.getContainedType(clazz);
	}

	/**
	 * Exposes an iterator over the contents of the container. Note that in many
	 * cases, an Iterator may only be used once and is then considered invalid.
	 * If you need to loop through the contents of the iterator multiple times,
	 * you will have to copy the contents of the iterator to some other
	 * structure, such as a java.util.List.
	 * 
	 * @param container
	 *            the container to iterate over
	 * @return an Iterator over the elements in the container
	 * @throws ReflectionException
	 *             if <code>container</code> is <code>null</code> or <br>
	 *             the Iterator could not be created for some reason
	 */
	public Iterator getIterator(Object container) throws ReflectionException {
		return DEFAULT_CONTAINER_REFLECTOR.getIterator(container);
	}
	
	/**
	 * Adds a new <code>value</code> to the end of a <code>container</code>.
	 * 
	 * @param container
	 *            the container to which the value is to be added
	 * @param value
	 *            the value to be added
	 * @return <code>true</code> if the container changed as a result of the
	 *         call or <br>
	 *         <code>false</code>, otherwise
	 * @throws ReflectionException
	 *             if an error occurrs
	 */
	public boolean add(Object container, Object value) throws ReflectionException {
		return DEFAULT_GROWABLE_CONTAINER_REFLECTOR.add(container, value);
	}
	
	/**
	 * Gets the element at the specified index. Valid indexes range between 0
	 * and one less than the container object's size, inclusive.
	 * 
	 * @param container
	 *            the container object
	 * @param index
	 *            a number indiciating which element should be retrieved
	 * @return the object at the specified index
	 * @throws ReflectionException
	 *             if <code>container</code> is null or <br>
	 *             <code>index</code> is not a valid index for the given
	 *             container object or <br>
	 *             the object at the specified index could not be retrieved for
	 *             some reason
	 */
	public Object get(Object container, int index) throws ReflectionException {
		return DEFAULT_INDEXED_CONTAINER_REFLECTOR.get(container, index);
	}

	/**
	 * Creates a new instance of the given type.
	 * 
	 * @param clazz
	 *            the type for which we would like a new instance to be created
	 * @throws ReflectionException
	 *             if an error occurrs
	 */
	public Object newInstance(Class clazz) throws ReflectionException {
		return DEFAULT_INSTANTIATING_REFLECTOR.newInstance(clazz);
	}
	
	/**
	 * Sets the element at the specified index. Valid indexes range between 0
	 * and one less than the container object's size, inclusive.
	 * 
	 * @param container
	 *            the container object
	 * @param index
	 *            a number indiciating which element should be set
	 * @param propertyValue
	 *            the value to be set
	 * @return the element previously at the specified position
	 * @throws ReflectionException
	 *             if <code>container</code> is null or <br>
	 *             <code>index</code> is not a valid index for the given
	 *             container object or <br>
	 *             the object at the specified index could not be set for some
	 *             reason
	 */
	public Object set(Object container, int index, Object propertyValue)
		throws ReflectionException {
		return DEFAULT_MUTABLE_INDEXED_CONTAINER_REFLECTOR.set(container, index, propertyValue);
	}
	
	/**
	 * Returns the number of elements contained in a given object.  If the
	 * object is a bean, the number of properties is returned.  If the object
	 * is a container, the number of elements in the container is returned. 
	 * 
	 * @param object
	 *            the object
	 * @return the number of elements contained in the given object
	 * @throws ReflectionException
	 *             if <code>object</code> is <code>null</code> or the
	 *             number of elements in the object could not be determined
	 */
	public int getSize(Object object) throws ReflectionException {
		return DEFAULT_SIZABLE_REFLECTOR.getSize(object);
	}
	
}