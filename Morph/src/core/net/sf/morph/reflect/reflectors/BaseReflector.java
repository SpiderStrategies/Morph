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
package net.sf.morph.reflect.reflectors;

import java.lang.reflect.Proxy;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import net.sf.composite.util.ObjectUtils;
import net.sf.morph.reflect.BeanReflector;
import net.sf.morph.reflect.ContainerReflector;
import net.sf.morph.reflect.DecoratedReflector;
import net.sf.morph.reflect.GrowableContainerReflector;
import net.sf.morph.reflect.IndexedContainerReflector;
import net.sf.morph.reflect.MutableIndexedContainerReflector;
import net.sf.morph.reflect.ReflectionException;
import net.sf.morph.reflect.Reflector;
import net.sf.morph.reflect.SizableReflector;
import net.sf.morph.util.ClassUtils;
import net.sf.morph.util.ContainerUtils;
import net.sf.morph.util.StringUtils;
import net.sf.morph.wrap.Wrapper;
import net.sf.morph.wrap.support.DefaultWrapperInvocationHandler;
import net.sf.morph.wrap.support.WrapperInvocationHandler;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * <p>
 * Convenient base class for Reflectors. Validates arguments and takes care of
 * logging and exception handling. Also, automatically implements some methods
 * for certain types of reflectors. Most notably, the BeanReflector interface
 * is automatically implemented for reflectors that implement
 * MutableIndexedContainerReflector.  See method JavaDoc for more information.
 * </p>
 *
 * @author Matt Sgarlata
 * @since Nov 14, 2004
 */
public abstract class BaseReflector implements Reflector, DecoratedReflector {

	protected transient Log log = LogFactory.getLog(getClass());

// fields

	private boolean initialized;
	private transient Class[] reflectableClasses;
	private boolean cachingIsReflectableCalls = true;
	private transient Map reflectableCallCache;

	/**
	 * Create a new BaseReflector.
	 */
	public BaseReflector() {
		setInitialized(false);
	}

//	 initialization

	/**
	 * Implementation of {@link #initialize()}.
	 * @throws Exception
	 */
	protected void initializeImpl() throws Exception {
	}

	/**
	 * Initialize this Reflector.
	 * @throws ReflectionException
	 */
	protected final void initialize() throws ReflectionException {
		if (!initialized) {
			if (isPerformingLogging() && log.isInfoEnabled()) {
				log.info("Initializing reflector " + ObjectUtils.getObjectDescription(this));
			}

			try {
				initializeImpl();
				reflectableClasses = getReflectableClassesImpl();
				reflectableCallCache = Collections.synchronizedMap(new HashMap());
				setInitialized(true);
			}
			catch (ReflectionException e) {
				throw e;
			}
			catch (Exception e) {
				if (e instanceof RuntimeException && !isWrappingRuntimeExceptions()) {
					throw (RuntimeException) e;
				}
				throw new ReflectionException("Could not initialize " + ObjectUtils.getObjectDescription(this), e);
			}
		}
	}

// basic reflector, decoratedreflector

	/**
	 * {@inheritDoc}
	 * @see net.sf.morph.reflect.Reflector#getReflectableClasses()
	 */
	public final Class[] getReflectableClasses() {
		initialize();
		return reflectableClasses;
	}

	/**
	 * Implementation of {@link Reflector#getReflectableClasses()}.
	 */
	protected abstract Class[] getReflectableClassesImpl() throws Exception;

	/**
	 * {@inheritDoc}
	 * @see net.sf.morph.reflect.Reflector#getWrapper(java.lang.Object)
	 */
	public final Wrapper getWrapper(Object object) {
		if (log.isTraceEnabled()) {
			log.trace("Creating wrapper for " + ObjectUtils.getObjectDescription(object));
		}

		if (object == null) {
			return null;
		}
		checkIsReflectable(object);

		try {
			return getWrapperImpl(object);
		}
		catch (ReflectionException e) {
			throw e;
		}
		catch (Exception e) {
			if (e instanceof RuntimeException && !isWrappingRuntimeExceptions()) {
				throw (RuntimeException) e;
			}
			throw new ReflectionException("Unable to create wrapper for "
				+ ObjectUtils.getObjectDescription(object), e);
		}
	}

	/**
	 * Implementation of {@link Reflector#getWrapper(Object)}.
	 */
	protected Wrapper getWrapperImpl(Object object) throws Exception {
		WrapperInvocationHandler invocationHandler = createWrapperInvocationHandler(object);

		return (Wrapper) Proxy.newProxyInstance(
			object.getClass().getClassLoader(),
			invocationHandler.getInterfaces(object),
			invocationHandler);
	}

	/**
	 * Create a WrapperInvocationHandler for the specified Object.
	 * @param object
	 * @return WrapperInvocationHandler
	 */
	protected WrapperInvocationHandler createWrapperInvocationHandler(Object object) {
		return new DefaultWrapperInvocationHandler(object, this);
	}

	/**
	 * {@inheritDoc}
	 * @see net.sf.morph.reflect.DecoratedReflector#isReflectable(java.lang.Class)
	 */
	public final boolean isReflectable(Class reflectedType) throws ReflectionException {
		if (isPerformingLogging() && log.isTraceEnabled()) {
			log.trace("Testing reflectability of " + ObjectUtils.getObjectDescription(reflectedType));
		}
		return isReflectableInternal(reflectedType);
	}

	/**
	 * Implementation of {@link DecoratedReflector#isReflectable(Class)}.
	 */
	protected boolean isReflectableImpl(Class reflectedType) throws Exception {
		return ClassUtils.inheritanceContains(getReflectableClasses(), reflectedType);
	}

	private boolean isReflectableInternal(Class reflectedType) {
		initialize();

		if (reflectedType == null) {
			throw new ReflectionException(
				"Cannot determine if a null reflectedType is reflectable; please supply a reflectedType to the "
					+ getClass().getName() + ".isReflectable method");
		}

		// try to pull the isReflectable information from the cache
		if (isCachingIsReflectableCalls()) {
			Boolean isReflectable = (Boolean) getReflectableCallCache().get(reflectedType);
			if (isReflectable != null) {
				return isReflectable.booleanValue();
			}
		}

		try {
			boolean isReflectable = isReflectableImpl(reflectedType);
			if (isCachingIsReflectableCalls()) {
				getReflectableCallCache().put(reflectedType, new Boolean(isReflectable));
			}
			return isReflectable;
		}
		catch (ReflectionException e) {
			throw e;
		}
		catch (Exception e) {
			if (e instanceof RuntimeException && !isWrappingRuntimeExceptions()) {
				throw (RuntimeException) e;
			}
			throw new ReflectionException("Unable to determine if class '"
				+ reflectedType.getClass().getName() + "' is reflectable", e);
		}
	}

	/**
	 * {@link net.sf.morph.reflect.DecoratedReflector#isReflectable(Class)}
	 * @param reflectedType
	 * @param reflectorType
	 * @return
	 * @throws ReflectionException
	 */
	public final boolean isReflectable(Class reflectedType, Class reflectorType) throws ReflectionException {
		if (isPerformingLogging() && log.isTraceEnabled()) {
			log.trace("Testing if "
				+ ObjectUtils.getObjectDescription(reflectedType)
				+ " can be reflected with a "
				+ ObjectUtils.getObjectDescription(reflectorType));
		}

		if (reflectedType == null) {
			throw new ReflectionException(
				"Cannot determine if a null reflectedType is reflectable; please supply a reflectedType to the "
					+ getClass().getName() + ".isReflectable method");
		}
		if (reflectorType != null &&
			!Reflector.class.isAssignableFrom(reflectorType)) {
			throw new ReflectionException("The reflectorType you specified, "
				+ ObjectUtils.getObjectDescription(reflectorType)
				+ ", is invalid.  It must be a child of "
				+ ObjectUtils.getObjectDescription(Reflector.class));
		}

		try {
			return reflectorType == null
					? isReflectableImpl(reflectedType) : isReflectableImpl(reflectedType, reflectorType);
		}
		catch (ReflectionException e) {
			throw e;
		}
		catch (Exception e) {
			if (e instanceof RuntimeException && !isWrappingRuntimeExceptions()) {
				throw (RuntimeException) e;
			}
			throw new ReflectionException("Unable to determine if reflectedType '"
				+ reflectedType.getClass().getName() + "' is reflectable", e);
		}
	}

	/**
	 * Implementation of {@link BaseReflector#isReflectable(Class, Class)}.
	 */
	protected boolean isReflectableImpl(Class reflectedType, Class reflectorType) throws Exception {
		throw new UnsupportedOperationException();
	}

	/**
	 * Throws an exception if a the given object is not reflectable by this
	 * reflector. Called before executing each method in this class to ensure
	 * the reflector is being used properly.
	 *
	 * @param object
	 *            the object to test
	 * @throws ReflectionException
	 *             if the given object is not reflectable by this reflector
	 */
	protected void checkIsReflectable(Object object) throws ReflectionException {
		if (!isReflectableInternal(object.getClass())) {
			throw new ReflectionException("Cannot reflect object "
				+ ObjectUtils.getObjectDescription(object) + " using reflector "
				+ ObjectUtils.getObjectDescription(this));
		}
	}

// instantiating reflector

	/**
	 * {@link net.sf.morph.reflect.InstantiatingReflector#newInstance(Class, Object)}
	 * @param clazz
	 * @param parameters
	 */
	public final Object newInstance(Class clazz, Object parameters) {
		if (clazz == null) {
			throw new ReflectionException(
				"You must specify the class for which a new instance is to be created");
		}
		if (!isReflectableInternal(clazz)) {
			throw new ReflectionException(
				ObjectUtils.getObjectDescription(clazz)
					+ " is not reflectable by reflector "
					+ ObjectUtils.getObjectDescription(this));
		}
		if (isPerformingLogging() && log.isTraceEnabled()) {
			log.trace("Creating new instance of '" + ObjectUtils.getObjectDescription(clazz) + "(parameters " + ObjectUtils.getObjectDescription(parameters) + ")");
		}

		try {
			Object result = newInstanceImpl(clazz, parameters);
			if (!clazz.isInstance(result)) {
				throw new ReflectionException(ObjectUtils.getObjectDescription(result) + " is not an instance of " + clazz);
			}
			return result;
		}
		catch (ReflectionException e) {
			throw e;
		}
		catch (Exception e) {
			if (e instanceof RuntimeException && !isWrappingRuntimeExceptions()) {
				throw (RuntimeException) e;
			}
			throw new ReflectionException("Unable to create new instance of "
				+ ObjectUtils.getObjectDescription(clazz) + "(parameters " + ObjectUtils.getObjectDescription(parameters) + ")", e);
		}
	}

	/**
	 * This method will be removed in a subsequent release of Morph. Left in-place to flag subclasses that require modification.
	 *
	 * @deprecated Use {@link #newInstanceImpl(Class, Object)} instead. Calls to this method will fail with an {@link UnsupportedOperationException}
	 */
	protected final Object newInstanceImpl(Class clazz) throws Exception {
		throw new UnsupportedOperationException("Deprecated method - use BaseReflector.newInstanceImpl(Class, Object) instead");
	}

	/**
	 * Implementation of
	 * {@link net.sf.morph.reflect.InstantiatingReflector#newInstance(Class, Object)}.
	 * Default implementation returns a new instance of the given class by
	 * calling {@link Class#newInstance())}.
	 */
	protected Object newInstanceImpl(Class clazz, Object parameters) throws Exception {
		if (isPerformingLogging() && log.isTraceEnabled()) {
			log.trace("Creating new instance of "
				+ ObjectUtils.getObjectDescription(clazz));
		}
		return clazz.newInstance();
	}

// bean reflectors

	/**
	 * {@link net.sf.morph.reflect.BeanReflector#getPropertyNames(Object)}
	 * @param bean
	 * @return
	 * @throws ReflectionException
	 */
	public final String[] getPropertyNames(Object bean)
		throws ReflectionException {

		if (bean == null) {
			throw new ReflectionException(
				"Cannot determine the properties of a null bean.  Please supply a bean to the "
					+ getClass().getName() + ".getPropertyNames method");
		}
		checkIsReflectable(bean);

		try {
			String[] propertyNames = getPropertyNamesImpl(bean);
			if (isPerformingLogging() && log.isTraceEnabled()) {
				log.trace("Properties of bean "
					+ ObjectUtils.getObjectDescription(bean) + " are "
					+ StringUtils.englishJoin(propertyNames));
			}
			return propertyNames;
		}
		catch (ReflectionException e) {
			throw e;
		}
		catch (Exception e) {
			if (e instanceof RuntimeException && !isWrappingRuntimeExceptions()) {
				throw (RuntimeException) e;
			}
			throw new ReflectionException(
				"Unable to get property names for bean "
					+ ObjectUtils.getObjectDescription(bean), e);
		}
	}

	/**
	 * Implementation of {@link BeanReflector#getPropertyNames(Object)}.
	 * Implementation automatically provided for
	 * IndexedContainerReflectors.  For other reflectors, throws an
	 * UnsupportedOperationException.
	 */
	protected String[] getPropertyNamesImpl(Object bean) throws Exception {
		if (this instanceof IndexedContainerReflector) {
			// create an array of all the valid indexes for the container
			int size = ((IndexedContainerReflector) this).getSize(bean);
			String[] propertyNames = new String[size];
			for (int i = 0; i < size; i++) {
				propertyNames[i] = Integer.toString(i);
			}
			return propertyNames;
		}
		throw new UnsupportedOperationException();
	}

	/**
	 * {@link BeanReflector#isReadable(Object, String)}
	 * @param bean
	 * @param propertyName
	 * @return boolean
	 * @throws ReflectionException
	 */
	public final boolean isReadable(Object bean, String propertyName)
		throws ReflectionException {

		if (bean == null && ObjectUtils.isEmpty(propertyName)) {
			throw new ReflectionException(
				"Please supply non-null arguments to the "
					+ getClass().getName() + ".isReadable method");
		}
		if (bean == null) {
			throw new ReflectionException("Cannot determine if property '"
				+ propertyName + "' is readable since no bean was specified");
		}
		if (ObjectUtils.isEmpty(propertyName)) {
			throw new ReflectionException(
				"Please supply a property name to test for readability for bean "
					+ ObjectUtils.getObjectDescription(bean));
		}
		checkIsReflectable(bean);

		boolean isReadable;
		try {
			if (this instanceof SizableReflector &&
				propertyName.equals(SizableReflector.IMPLICIT_PROPERTY_SIZE)) {
				isReadable = true;
			}
			else if (this instanceof BeanReflector &&
				(propertyName.equals(BeanReflector.IMPLICIT_PROPERTY_CLASS) ||
				propertyName.equals(BeanReflector.IMPLICIT_PROPERTY_PROPERTY_NAMES) ||
				propertyName.equals(BeanReflector.IMPLICIT_PROPERTY_THIS))) {
				isReadable = true;
			}
			else {
				isReadable = isReadableImpl(bean, propertyName);
			}

			if (isPerformingLogging() && log.isTraceEnabled()) {
				log.trace("Property '" + propertyName + "' is"
					+ (isReadable ? " " : " not ") + "readable in bean "
					+ ObjectUtils.getObjectDescription(bean));
			}
			return isReadable;
		}
		catch (ReflectionException e) {
			throw e;
		}
		catch (Exception e) {
			if (e instanceof RuntimeException && !isWrappingRuntimeExceptions()) {
				throw (RuntimeException) e;
			}
			throw new ReflectionException("Unable determine if property '"
				+ propertyName + "' is readable in bean "
				+ ObjectUtils.getObjectDescription(bean), e);
		}
	}

	/**
	 * Implementation of {@link BeanReflector#isReadable(Object, String)}.
	 * Default implementation assumes that all properties of the bean specified
	 * by {@link BeanReflector#getPropertyNames(Object)} are readable.
	 */
	protected boolean isReadableImpl(Object bean, String propertyName)
		throws Exception {
		return this instanceof IndexedContainerReflector ? isValidIndex(bean, propertyName)
				: ContainerUtils.contains(getPropertyNames(bean), propertyName);
	}

	/**
	 * {@link BeanReflector#isWriteable(Object, String)}
	 * @param bean
	 * @param propertyName
	 * @return boolean
	 */
	public final boolean isWriteable(Object bean, String propertyName) {

		if (bean == null && ObjectUtils.isEmpty(propertyName)) {
			throw new ReflectionException(
				"Please supply non-null arguments to the "
					+ getClass().getName() + ".isWriteable method");
		}
		if (bean == null) {
			throw new ReflectionException("Cannot determine if property '"
				+ propertyName + "' is writeable since no bean was specified");
		}
		if (ObjectUtils.isEmpty(propertyName)) {
			throw new ReflectionException(
				"Please supply a property name to test for writeability for bean "
					+ ObjectUtils.getObjectDescription(bean));
		}
		checkIsReflectable(bean);

		try {
			Boolean isWriteable = null;
			Exception exception = null;

			try {
				isWriteable = new Boolean(isWriteableImpl(bean, propertyName));
			}
			catch (Exception e) {
				exception = e;
			}

			if (isWriteable == null) {
				if (this instanceof SizableReflector
						&& propertyName.equals(SizableReflector.IMPLICIT_PROPERTY_SIZE)) {
					return false;
				}
				if (this instanceof BeanReflector
						&& (BeanReflector.IMPLICIT_PROPERTY_CLASS.equals(propertyName)
								|| BeanReflector.IMPLICIT_PROPERTY_PROPERTY_NAMES
										.equals(propertyName) || BeanReflector.IMPLICIT_PROPERTY_THIS
								.equals(propertyName))) {
					return false;
				}
			}
			if (exception == null) {
				if (isPerformingLogging() && log.isTraceEnabled()) {
					log.trace("Property '" + propertyName + "' is"
						+ (isWriteable.booleanValue() ? " " : " not ")
						+ "writeable in bean "
						+ ObjectUtils.getObjectDescription(bean));
				}
				return isWriteable.booleanValue();
			}
			throw exception;
		}
		catch (ReflectionException e) {
			throw e;
		}
		catch (Exception e) {
			if (e instanceof RuntimeException && !isWrappingRuntimeExceptions()) {
				throw (RuntimeException) e;
			}
			throw new ReflectionException("Unable determine if property '"
				+ propertyName + "' is writeable in bean "
				+ ObjectUtils.getObjectDescription(bean), e);
		}
	}

	/**
	 * Implementation of {@link BeanReflector#isWriteable(Object, String)}.
	 * Default implementation assumes that all readable properties are also
	 * writeable. One exception to this is when this reflector is an
	 * IndexedContainerReflector but not a MutableIndexedContainerReflector, in
	 * which case no properties are considered writeable.
	 */
	protected boolean isWriteableImpl(Object bean, String propertyName)
		throws Exception {
		return (!(this instanceof IndexedContainerReflector)
				|| this instanceof MutableIndexedContainerReflector)
				&& isReadableImpl(bean, propertyName);
	}

	/**
	 * Learn whether <code>propertyName</code> denotes a valid numeric property index for <code>bean</code>.
	 * @param bean
	 * @param propertyName
	 * @return boolean
	 * @throws ReflectionException
	 */
	protected boolean isValidIndex(Object bean, String propertyName) throws ReflectionException {
		try {
			int index = Integer.parseInt(propertyName);
			return index >= 0 && index < getSize(bean);
		}
		catch (NumberFormatException e) {
			return false;
		}
	}

	/**
	 * {@link BeanReflector#set(Object, String, Object)}
	 * @param bean
	 * @param propertyName
	 * @param propertyValue
	 * @throws ReflectionException
	 */
	public final void set(Object bean, String propertyName, Object propertyValue)
		throws ReflectionException {
		if (isPerformingLogging() && log.isTraceEnabled()) {
			log.trace("Setting property '" + propertyName + "' of bean "
				+ ObjectUtils.getObjectDescription(bean) + " to "
				+ ObjectUtils.getObjectDescription(propertyValue));
		}

		if (bean == null && ObjectUtils.isEmpty(propertyName)) {
			throw new ReflectionException(
				"Please supply non-null arguments to the "
					+ getClass().getName() + ".set method");
		}
		if (bean == null) {
			throw new ReflectionException("Cannot retrieve property '"
				+ propertyName + "' since no bean was specified");
		}
		if (ObjectUtils.isEmpty(propertyName)) {
			throw new ReflectionException(
				"Please supply a property name to retrieve from bean "
					+ ObjectUtils.getObjectDescription(bean));
		}
		checkIsReflectable(bean);

		try { //don't bother setting if already same or immutable and equal
			Object currentValue = get(bean, propertyName);
			if (propertyValue == currentValue
					|| (ClassUtils.isImmutable(getType(bean, propertyName)) && ObjectUtils
							.equals(propertyValue, currentValue))) {
				//ignore "this"; else if the property doesn't already exist, this is probably a MapReflector and we want to add anyway
				if (BeanReflector.IMPLICIT_PROPERTY_THIS.equals(propertyName)
						|| ContainerUtils.contains(getPropertyNames(bean), propertyName)) {
					return;
				}
			}
		} catch (ReflectionException e) {
			//simply ignore, maybe we can set but not get:
			if (isPerformingLogging() && log.isTraceEnabled()) {
				log.trace("Ignoring exception encountered getting property " + propertyName
						+ " for object " + ObjectUtils.getObjectDescription(bean));
			}
		}

		if (!isWriteable(bean, propertyName)) {
			throw new ReflectionException("The property '" + propertyName
				+ "' is not writeable in bean "
				+ ObjectUtils.getObjectDescription(bean));
		}

		try {
			setImpl(bean, propertyName, propertyValue);
		}
		catch (ReflectionException e) {
			throw e;
		}
		catch (Exception e) {
			if (e instanceof RuntimeException && !isWrappingRuntimeExceptions()) {
				throw (RuntimeException) e;
			}
			throw new ReflectionException("Unable to set property '"
				+ propertyName + "' of bean "
				+ ObjectUtils.getObjectDescription(bean) + " to "
				+ propertyValue, e);
		}
	}

	/**
	 * Implementation of {@link BeanReflector#set(Object, String, Object)}.
	 * Implementation automatically provided for
	 * MutableIndexedContainerReflectors.  For other reflectors, throws an
	 * UnsupportedOperationException.
	 */
	protected void setImpl(Object bean, String propertyName,
		Object value) throws Exception {
		if (!(this instanceof MutableIndexedContainerReflector)) {
			throw new UnsupportedOperationException();
		}
		((MutableIndexedContainerReflector) this).set(bean, Integer.parseInt(propertyName), value);
	}

	/**
	 * {@link BeanReflector#get(Object, String)}
	 * @param bean
	 * @param propertyName
	 * @return
	 * @throws ReflectionException
	 */
	public final Object get(Object bean, String propertyName)
		throws ReflectionException {
		if (bean == null && ObjectUtils.isEmpty(propertyName)) {
			throw new ReflectionException(
				"Please supply non-null arguments to the "
					+ getClass().getName() + ".get method");
		}
		if (bean == null) {
			throw new ReflectionException("Cannot retrieve property '"
				+ propertyName + "' from a null object");
		}
		if (ObjectUtils.isEmpty(propertyName)) {
			throw new ReflectionException(
				"Please supply a property name to retrieve from bean "
					+ ObjectUtils.getObjectDescription(bean));
		}
		checkIsReflectable(bean);

		try {
			Object value = null;
			Exception exception = null;
			// try to retrieve the property
			try {
				if (!isReadable(bean, propertyName)) {
					throw new ReflectionException("The property '"
						+ propertyName + "' is not readable in bean "
						+ ObjectUtils.getObjectDescription(bean)
						+ " using reflector "
						+ ObjectUtils.getObjectDescription(this));
				}
				value = getImpl(bean, propertyName);
			}
			// if an error occurrs, remember it for later
			catch (Exception e) {
				exception = e;
			}

			// if we couldn't get the property and it's a implicit property,
			// return the value of the implicit property
			if (value == null &&
				this instanceof BeanReflector &&
				propertyName.equals(BeanReflector.IMPLICIT_PROPERTY_CLASS)) {
				return bean.getClass();
			}
			if (value == null &&
				this instanceof BeanReflector &&
				propertyName.equals(BeanReflector.IMPLICIT_PROPERTY_PROPERTY_NAMES)) {
				return getPropertyNames(bean);
			}
			if (value == null &&
				this instanceof BeanReflector &&
				propertyName.equals(BeanReflector.IMPLICIT_PROPERTY_THIS)) {
				return bean;
			}
			if (value == null &&
				this instanceof SizableReflector &&
				propertyName.equals(SizableReflector.IMPLICIT_PROPERTY_SIZE)) {
				return new Integer(getSize(bean));
			}
			// if the returned value was null and the property wasn't a implicit
			// property, rethrow any exception that was thrown, or if no
			// exception was thrown, return the value (which will be null)
			if (exception == null) {
				if (isPerformingLogging() && log.isTraceEnabled()) {
					log.trace("Property '" + propertyName + "' has value "
						+ ObjectUtils.getObjectDescription(value)
						+ " in bean "
						+ ObjectUtils.getObjectDescription(bean));
				}
				return value;
			}
			throw exception;
		}
		catch (ReflectionException e) {
			throw e;
		}
		catch (Exception e) {
			if (e instanceof RuntimeException && !isWrappingRuntimeExceptions()) {
				throw (RuntimeException) e;
			}
			throw new ReflectionException("Unable to retrieve property '"
				+ propertyName + "' from bean "
				+ ObjectUtils.getObjectDescription(bean), e);
		}
	}

	/**
	 * Implementation of {@link BeanReflector#get(Object, String)}.
	 * Implementation automatically provided for
	 * IndexedContainerReflectors.  For other reflectors, throws an
	 * UnsupportedOperationException.
	 */
	protected Object getImpl(Object bean, String propertyName)
		throws Exception {
		if (!(this instanceof IndexedContainerReflector)) {
			throw new UnsupportedOperationException();
		}
		return BeanReflector.IMPLICIT_PROPERTY_CLASS.equals(propertyName)
				|| BeanReflector.IMPLICIT_PROPERTY_SIZE.equals(propertyName)
				|| BeanReflector.IMPLICIT_PROPERTY_THIS.equals(propertyName) ? null
				: get(bean, Integer.parseInt(propertyName));
	}

	/**
	 * {@link BeanReflector#getType(Object, String)}
	 * @param bean
	 * @param propertyName
	 * @return
	 * @throws ReflectionException
	 */
	public final Class getType(Object bean, String propertyName)
		throws ReflectionException {

		if (bean == null && ObjectUtils.isEmpty(propertyName)) {
			throw new ReflectionException(
				"Please supply non-null arguments to the "
					+ getClass().getName() + ".getType method");
		}
		if (bean == null) {
			throw new ReflectionException("Cannot determine type of property '"
				+ propertyName + "' since no bean was specified");
		}
		if (ObjectUtils.isEmpty(propertyName)) {
			throw new ReflectionException(
				"Please supply a property name of bean "
					+ ObjectUtils.getObjectDescription(bean)
					+ " for which you would like to know the type");
		}
		checkIsReflectable(bean);

		boolean hasPropertyDefined = ContainerUtils.contains(
			getPropertyNames(bean), propertyName);
		if (isStrictlyTyped() &&
			!hasPropertyDefined &&
			!SizableReflector.IMPLICIT_PROPERTY_SIZE.equals(propertyName) &&
			!BeanReflector.IMPLICIT_PROPERTY_CLASS.equals(propertyName) &&
			!BeanReflector.IMPLICIT_PROPERTY_THIS.equals(propertyName) &&
			!BeanReflector.IMPLICIT_PROPERTY_PROPERTY_NAMES.equals(propertyName)) {
			throw new ReflectionException("Cannot determine type of property '"
				+ propertyName + "' because it is not a property of "
				+ ObjectUtils.getObjectDescription(bean));
		}

		// take care of implicit properties, if applicable
		Class type = null;
		if (!hasPropertyDefined) {
			if (propertyName.equals(BeanReflector.IMPLICIT_PROPERTY_CLASS)) {
				type = Class.class;
			}
			else if (propertyName.equals(BeanReflector.IMPLICIT_PROPERTY_PROPERTY_NAMES)) {
				type = String[].class;
			}
			else if (propertyName.equals(BeanReflector.IMPLICIT_PROPERTY_THIS)) {
				type = ClassUtils.getClass(bean);
			}
			else if (propertyName.equals(SizableReflector.IMPLICIT_PROPERTY_SIZE)) {
				type = Integer.TYPE;
			}
		}

		// if we aren't retrieving an implicit property, let the subclass handle
		// the request
		if (type == null) {
			try {
				type = getTypeImpl(bean, propertyName);
			}
			catch (ReflectionException e) {
				throw e;
			}
			catch (Exception e) {
				if (e instanceof RuntimeException && !isWrappingRuntimeExceptions()) {
					throw (RuntimeException) e;
				}
				throw new ReflectionException(
					"Unable to determine type of property '" + propertyName
						+ "' for bean " + ObjectUtils.getObjectDescription(bean),
						e);
			}
		}

		if (isPerformingLogging() && log.isTraceEnabled()) {
			log.trace("Property '" + propertyName + "' has type "
				+ ObjectUtils.getObjectDescription(type) + " in bean "
				+ ObjectUtils.getObjectDescription(bean));
		}

		return type;
	}

	/**
	 * Implementation of {@link BeanReflector#getType(Object, String)}.
	 * Default implementation provided. For IndexedContainerReflectors,
	 * returns the type by calling
	 * {@link net.sf.morph.reflect.ContainerReflector#getContainedType(Class)}.
	 * For other reflectors, checks the type of the property by calling
	 * {@link BaseReflector#get(Object, String)}.
	 */
	protected Class getTypeImpl(Object bean, String propertyName)
		throws Exception {

		if (this instanceof IndexedContainerReflector) {
			if (isValidIndex(bean, propertyName)) {
				return ((IndexedContainerReflector) this).getContainedType(
						bean.getClass());
			}
			throw new ReflectionException("'" + propertyName
					+ "' is not a valid index in the container "
					+ ObjectUtils.getObjectDescription(bean));
		}
		return ClassUtils.getClass(getImpl(bean, propertyName));
	}

// container reflectors

	/**
	 * {@link ContainerReflector#getContainedType(Class)}
	 * @param clazz
	 * @return
	 * @throws ReflectionException
	 */
	public final Class getContainedType(Class clazz) throws ReflectionException {

		if (clazz == null) {
			throw new ReflectionException(
				"Can't determine the type of a null object");
		}

		try {
			Class type = getContainedTypeImpl(clazz);
			if (isPerformingLogging() && log.isTraceEnabled()) {
				log.trace("Contained type is "
					+ ObjectUtils.getObjectDescription(type)
					+ " for instances of "
					+ ObjectUtils.getObjectDescription(clazz));
			}
			return type;
		}
		catch (ReflectionException e) {
			throw e;
		}
		catch (Exception e) {
			if (e instanceof RuntimeException && !isWrappingRuntimeExceptions()) {
				throw (RuntimeException) e;
			}
			throw new ReflectionException(
				"Could not determine the type of objects contained in the container of "
					+ ObjectUtils.getObjectDescription(clazz));
		}

	}

	/**
	 * Implementation of {@link net.sf.morph.reflect.ContainerReflector#getContainedType(Class)}.
	 */
	protected Class getContainedTypeImpl(Class clazz) throws Exception {
		throw new UnsupportedOperationException();
	}

	/**
	 * {@link ContainerReflector#getIterator(Object)}
	 * @param container
	 * @return Iterator
	 * @throws ReflectionException
	 */
	public final Iterator getIterator(Object container)
		throws ReflectionException {
		if (isPerformingLogging() && log.isTraceEnabled()) {
			log.trace("Retrieving iterator for "
				+ ObjectUtils.getObjectDescription(container));
		}
		if (container == null) {
			throw new ReflectionException(
				"Cannot iterate through the contents of null container");
		}
		checkIsReflectable(container);

		try {
			return getIteratorImpl(container);
		}
		catch (ReflectionException e) {
			throw e;
		}
		catch (Exception e) {
			if (e instanceof RuntimeException && !isWrappingRuntimeExceptions()) {
				throw (RuntimeException) e;
			}
			throw new ReflectionException("Could not retrieve iterator for "
				+ ObjectUtils.getObjectDescription(container), e);
		}
	}

	/**
	 * Implementation of {@link net.sf.morph.reflect.ContainerReflector#getIterator(Object)}.
	 */
	protected Iterator getIteratorImpl(Object container)
		throws Exception {
		throw new UnsupportedOperationException();
	}

	/**
	 * Validate <code>index</code> into <code>container</code>.
	 * @param container
	 * @param index
	 * @throws ReflectionException
	 */
	protected void checkIndex(Object container, int index)
		throws ReflectionException {
		if (index < 0) {
			throw new ReflectionException("Must specify a non-negative index");
		}
		if (index > getSize(container) - 1) {
			throw new ReflectionException("Cannot access element " + index
				+ " because the container object has only "
				+ getSize(container) + " elements");
		}
	}

// sizable reflectors

	/**
	 * {@link SizableReflector#getSize(Object)}
	 * @param container
	 * @return
	 * @throws ReflectionException
	 */
	public final int getSize(Object container) throws ReflectionException {
		if (isPerformingLogging() && log.isTraceEnabled()) {
			log.trace("Retrieving size of "
				+ ObjectUtils.getObjectDescription(container));
		}

		if (container == null) {
			throw new ReflectionException(
				"Cannot determine the size of a null object");
		}
		checkIsReflectable(container);

		try {
			return getSizeImpl(container);
		}
		catch (ReflectionException e) {
			throw e;
		}
		catch (Exception e) {
			if (e instanceof RuntimeException && !isWrappingRuntimeExceptions()) {
				throw (RuntimeException) e;
			}
			throw new ReflectionException("Could not determine the size of "
				+ ObjectUtils.getObjectDescription(container) + " object", e);
		}
	}

	/**
	 * Implementation of {@link SizableReflector#getSize(Object)}.
	 */
	protected int getSizeImpl(Object container) throws Exception {
		if (this instanceof BeanReflector) {
			return getPropertyNamesImpl(container).length;
		}
		throw new UnsupportedOperationException();
	}

// indexed container reflectors

	/**
	 * {@link IndexedContainerReflector#get(Object, int)}
	 * @param container
	 * @param index
	 * @return
	 * @throws ReflectionException
	 */
	public final Object get(Object container, int index)
		throws ReflectionException {

		if (container == null) {
			throw new ReflectionException(
				"Can't retrieve values from a null object");
		}
		checkIndex(container, index);
		checkIsReflectable(container);

		try {
			Object value = getImpl(container, index);

			if (isPerformingLogging() && log.isTraceEnabled()) {
				log.trace("Item at index " + index + " has value "
					+ ObjectUtils.getObjectDescription(value) + " in container "
					+ ObjectUtils.getObjectDescription(container));
			}

			return value;
		}
		catch (ReflectionException e) {
			throw e;
		}
		catch (Exception e) {
			if (e instanceof RuntimeException && !isWrappingRuntimeExceptions()) {
				throw (RuntimeException) e;
			}
			throw new ReflectionException("Could not retrieve element " + index
				+ " from " + ObjectUtils.getObjectDescription(container), e);
		}
	}

	/**
	 * Implementation of {@link IndexedContainerReflector#get(Object, int)}.
	 */
	protected Object getImpl(Object container, int index) throws Exception {
		throw new UnsupportedOperationException();
	}

// mutable indexed container reflectors

	/**
	 * {@link MutableIndexedContainerReflector#set(Object, int, Object)}
	 * @param container
	 * @param index
	 * @param propertyValue
	 * @return
	 * @throws ReflectionException
	 */
	public final Object set(Object container, int index, Object propertyValue)
		throws ReflectionException {

		if (isPerformingLogging() && log.isTraceEnabled()) {
			log.trace("Setting item at index " + index + " for object "
				+ ObjectUtils.getObjectDescription(container) + " to value "
				+ ObjectUtils.getObjectDescription(propertyValue));
		}

		if (container == null) {
			throw new ReflectionException("Can't set values of a null object");
		}
		checkIndex(container, index);
		checkIsReflectable(container);

		try { //don't bother setting if already same or immutable and equal
			Object currentValue = get(container, index);
			if (propertyValue == currentValue
					|| (ClassUtils.isImmutable(getContainedType(container.getClass())) && ObjectUtils
							.equals(propertyValue, currentValue))) {
				return currentValue;
			}
		} catch (ReflectionException e) {
			//simply ignore, maybe we can set but not get:
			if (isPerformingLogging() && log.isTraceEnabled()) {
				log.trace("Ignoring exception encountered getting item at index " + index
						+ " for object " + ObjectUtils.getObjectDescription(container));
			}
		}

		try {
			return setImpl(container, index, propertyValue);
		}
		catch (ReflectionException e) {
			throw e;
		}
		catch (Exception e) {
			if (e instanceof RuntimeException && !isWrappingRuntimeExceptions()) {
				throw (RuntimeException) e;
			}
			throw new ReflectionException(
				"Could not set element " + index + " of "
					+ ObjectUtils.getObjectDescription(container) + " to value "
					+ ObjectUtils.getObjectDescription(propertyValue), e);
		}
	}

	/**
	 * Implementation of {@link MutableIndexedContainerReflector#set(Object, int, Object)}.
	 */
	protected Object setImpl(Object container, int index, Object propertyValue)
		throws Exception {
		throw new UnsupportedOperationException();
	}

// growable container reflectors

	/**
	 * {@link GrowableContainerReflector#add(Object, Object)}
	 * @param container
	 * @param value
	 * @return
	 * @throws ReflectionException
	 */
	public final boolean add(Object container, Object value)
		throws ReflectionException {

		if (isPerformingLogging() && log.isTraceEnabled()) {
			log.trace("Adding item " + ObjectUtils.getObjectDescription(value)
				+ " to container " + ObjectUtils.getObjectDescription(container));
		}

		if (container == null) {
			throw new ReflectionException("Can't add values of a null object");
		}
		checkIsReflectable(container);

		try {
			return addImpl(container, value);
		}
		catch (ReflectionException e) {
			throw e;
		}
		catch (Exception e) {
			if (e instanceof RuntimeException && !isWrappingRuntimeExceptions()) {
				throw (RuntimeException) e;
			}
			throw new ReflectionException("Could not add item "
				+ ObjectUtils.getObjectDescription(value) + " to container "
				+ ObjectUtils.getObjectDescription(container), e);
		}
	}

	/**
	 * Implementation of {@link net.sf.morph.wrap.GrowableContainer#add(Object)}.
	 */
	protected boolean addImpl(Object container, Object value) throws Exception {
		throw new UnsupportedOperationException();
	}

// getters and setters

	/**
	 * Indicates whether this reflector is writing log messages
	 */
	protected boolean isPerformingLogging() {
		return true;
	}

	/**
	 * Indicates whether this reflector is strictly typed.  If a reflector is
	 * strictly typed, the {@link #getType(Object, String)} method will throw
	 * an exception if the requested property name is not a valid property
	 * of the object.  Default implementation returns <code>false</code>.
	 * @return <code>false</code>.
	 */
	public boolean isStrictlyTyped() {
		return false;
	}

	/**
	 * Learn whether this Reflector is initialized.
	 * @return boolean
	 */
	protected boolean isInitialized() {
		return initialized;
	}

	/**
	 * Set the initialization status of this Reflector.
	 * @param initialized
	 */
	protected void setInitialized(boolean initialized) {
		this.initialized = initialized;
	}

	/**
	 * Learn whether this Reflector is caching {@link #isReflectable(Class)} calls.
	 * Default <code>true</code>.
	 * @return boolean
	 */
	public boolean isCachingIsReflectableCalls() {
		return cachingIsReflectableCalls;
	}

	/**
	 * Set whether this Reflector is caching {@link #isReflectable(Class)} calls.
	 * @param cachingIsReflectableCalls
	 */
	public void setCachingIsReflectableCalls(boolean cachingIsReflectableCalls) {
		this.cachingIsReflectableCalls = cachingIsReflectableCalls;
	}

	/**
	 * Get the {@link #isReflectable(Class)} call cache.
	 * @return Map
	 */
	protected Map getReflectableCallCache() {
		return reflectableCallCache;
	}

	/**
	 * Set the {@link #isReflectable(Class)} call cache.
	 * @return Map
	 */
	protected void setReflectableCallCache(Map reflectableCallCache) {
		this.reflectableCallCache = reflectableCallCache;
	}

	/**
	 * Indicates whether runtime exceptions should be wrapped as
	 * {@link ReflectionException}s. By default, this method returns
	 * <code>true</code>.
	 * 
	 * <p>
	 * Simple Reflectors in Morph will usually set this value to <code>true</code>
	 * so that they throw ReflectionExceptions if problems occur.  User-written
	 * Reflectors are encouraged to return <code>false</code> so that runtime
	 * exceptions are not wrapped.  This way, problems accessing data will be
	 * expressed by the native API of a user's domain objects and avoid the need to
	 * catch Morph-specific exceptions (assuming the use of runtime exceptions in said
	 * domain objects).
	 * 
	 * @return <code>true</code>
	 * @since Morph 1.0.2
	 */
	protected boolean isWrappingRuntimeExceptions() {
		return true;
	}

}