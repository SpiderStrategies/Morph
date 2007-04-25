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
package net.sf.morph.transform.transformers;

import java.lang.reflect.Method;
import java.util.Collections;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import net.sf.composite.util.CompositeUtils;
import net.sf.composite.util.ObjectPair;
import net.sf.composite.util.ObjectUtils;
import net.sf.morph.Defaults;
import net.sf.morph.reflect.InstantiatingReflector;
import net.sf.morph.reflect.ReflectionException;
import net.sf.morph.reflect.Reflector;
import net.sf.morph.transform.Converter;
import net.sf.morph.transform.Copier;
import net.sf.morph.transform.DecoratedConverter;
import net.sf.morph.transform.DecoratedTransformer;
import net.sf.morph.transform.ExplicitTransformer;
import net.sf.morph.transform.TransformationException;
import net.sf.morph.transform.Transformer;
import net.sf.morph.util.ClassUtils;
import net.sf.morph.util.TransformerUtils;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * <p>
 * Convenient base class for transformers. This base class offers a number of
 * convenient features, including those listed below.
 * <ul>
 * <li>Automatically performs basic argument checking</li>
 * <li>Automatically does basic logging</li>
 * <li>Wraps exceptions in
 * {@link net.sf.morph.transform.TransformationException}s for you</li>
 * <li>Exposes the convenient
 * {@link net.sf.morph.transform.DecoratedTransformer}interface, while only
 * requiring subclasses to implement the methods in
 * {@link net.sf.morph.transform.Transformer}.</li>
 * <li>Provides protected methods that assist in the implementation of
 * transformers which are in turn composed of other transformers.</li>
 * <li>Optionally caches results of
 * {@link ExplicitTransformer#isTransformable(Class, Class)}
 * for better performance. This feature is turned on by default</li>
 * </ul>
 * </p>
 *
 * @author Matt Sgarlata
 * @since Nov 26, 2004
 */
public abstract class BaseTransformer implements Transformer, DecoratedTransformer {

	protected transient Log log = LogFactory.getLog(getClass());

	private static final String SPRING_LOCALE_CONTEXT_HOLDER_CLASS = "org.springframework.context.i18n.LocaleContextHolder";

	private boolean initialized = false;
	private boolean cachingIsTransformableCalls = true;
	private transient Map transformableCallCache;
	protected Class[] sourceClasses;
	protected Class[] destinationClasses;
	private Transformer nestedTransformer;
	private Reflector reflector;

// isTransformable

	/**
	 * Default implementation for
	 * {@link Transformer#isTransformable(Class, Class)} that assumes that each
	 * source type can be converted into each destination type.
	 *
	 * @param destinationType
	 *            the destination type to test
	 * @param sourceType
	 *            the source type to test
	 * @return whether the destination type is transformable to the source
	 *         type
	 * @throws TransformationException
	 *             if it could not be determined if <code>sourceType</code>
	 *             is transformable into <code>destinationType</code>
	 */
	protected boolean isTransformableImpl(Class destinationType,
		Class sourceType) throws Exception {
		return TransformerUtils.isImplicitlyTransformable(this, destinationType,
				sourceType);
	}

	public final boolean isTransformable(Class destinationType,
		Class sourceType) throws TransformationException {
		initialize();

		// Note: null source and destination classes are allowed!

		// first, try to pull the source and destination from the cache
		ObjectPair pair = null;
		if (isCachingIsTransformableCalls()) {
			pair = new ObjectPair(destinationType, sourceType);
			if (getTransformableCallCache().containsKey(pair)) {
				Boolean isTransformable = (Boolean)
					getTransformableCallCache().get(pair);
				return isTransformable.booleanValue();
			}
		}

		try {
			boolean isTransformable = isTransformableImpl(destinationType, sourceType);
			if (isCachingIsTransformableCalls()) {
				getTransformableCallCache().put(pair, Boolean.valueOf(isTransformable));
			}
			return isTransformable;
		}
		catch (TransformationException e) {
			throw e;
		}
		catch (RuntimeException e) {
			if (isWrappingRuntimeExceptions()) {
				throw wrapIsTransformableException(destinationType, sourceType, e);
			}
			else {
				throw e;
			}
		}
		catch (StackOverflowError e) {
			throw new TransformationException(
				"Stack overflow detected.  This usually occurs when a transformer implements "
					+ ObjectUtils.getObjectDescription(ExplicitTransformer.class)
					+ " but does not override the isTransformableImpl method",
				e);
		}
		catch (Exception e) {
			throw wrapIsTransformableException(destinationType, sourceType, e);
		}
	}

	protected TransformationException wrapIsTransformableException(Class destinationType, Class sourceType, Exception e) {
	    return new TransformationException("Could not determine if "
	    		+ sourceType + " is convertible to "
	    		+ destinationType, e);
    }

// source and destination classes

	protected abstract Class[] getSourceClassesImpl() throws Exception;

	protected abstract Class[] getDestinationClassesImpl() throws Exception;

	public final Class[] getSourceClasses() throws TransformationException {
		initialize();
		return sourceClasses;
	}

	public final Class[] getDestinationClasses() throws TransformationException {
		initialize();
		return destinationClasses;
	}

	/**
	 * Configures the <code>sourceClasses</code> property of this transformer.
	 * Note that this method should be called before the transformer is used.
	 * Otherwise, if another thread is in the middle of transforming an object
	 * graph and this method is called, the behavior of the transformer can
	 * change partway through the transformation.
	 *
	 * @param sourceClasses the new <code>sourceClasses</code> for this transformer
	 */
	protected synchronized void setSourceClasses(Class[] sourceClasses) {
		setInitialized(false);
		this.sourceClasses = sourceClasses;
	}

	/**
	 * Configures the <code>destinationClasses</code> property of this
	 * transformer. Note that this method should be called before the
	 * transformer is used. Otherwise, if another thread is in the middle of
	 * transforming an object graph and this method is called, the behavior of
	 * the transformer can change partway through the transformation.
	 *
	 * @param destinationClasses
	 *            the new <code>destinationClasses</code> for this transformer
	 */
	protected synchronized void setDestinationClasses(Class[] destinationClasses) {
		setInitialized(false);
		this.destinationClasses = destinationClasses;
	}

// initialize

	/**
	 * Gives subclasses a chance to perform any computations needed to
	 * initialize the transformer
	 */
	protected void initializeImpl() throws Exception {

	}

	protected synchronized final void initialize() throws TransformationException {
		if (!initialized) {
			if (log.isInfoEnabled()) {
				log.info("Initializing transformer " + ObjectUtils.getObjectDescription(this));
			}

			try {
				initializeImpl();

				if (sourceClasses == null) {
					sourceClasses = getSourceClassesImpl();
				}
				if (destinationClasses == null) {
					destinationClasses = getDestinationClassesImpl();
				}
				if (ObjectUtils.isEmpty(sourceClasses)) {
					throw new TransformationException(
							"This transformer, "
									+ ObjectUtils.getObjectDescription(this)
									+ ", is invalid because it does specify any sourceClasses");
				}
				if (ObjectUtils.isEmpty(destinationClasses)) {
					throw new TransformationException(
							"This transformer, "
									+ ObjectUtils.getObjectDescription(this)
									+ ", is invalid because it does specify any destinationClasses");
				}

				transformableCallCache = Collections.synchronizedMap(new HashMap());

				if (nestedTransformer == null) {
					nestedTransformer = Defaults.createTransformer();
				}

				initialized = true;
			}
			catch (TransformationException e) {
				throw e;
			}
			catch (Exception e) {
				if (e instanceof RuntimeException && !isWrappingRuntimeExceptions()) {
					throw (RuntimeException) e;
				}
				throw new TransformationException("Could not initialize transformer "
						+ ObjectUtils.getObjectDescription(this), e);
			}
		}
	}

	/**
	 * Retrieves the current Locale if none is specified in the method arguments
	 * for a converter or copier.  Attempts to load the Locale using Spring's
	 * {@link org.springframework.context.i18n.LocaleContextHolder}, if Spring
	 * is on the classpath.  Otherwise, returns the default Locale by calling
	 * {@link Locale#getDefault()}.
	 *
	 * @return the current Locale
	 */
	protected Locale getLocale() {
		Locale locale = null;
		if (ClassUtils.isClassPresent(SPRING_LOCALE_CONTEXT_HOLDER_CLASS)) {
			try {
				Class contextHolderClass = Class.forName(SPRING_LOCALE_CONTEXT_HOLDER_CLASS);
				Method getLocaleMethod =
					contextHolderClass.getMethod("getLocale", (Class[]) null);
				locale = (Locale) getLocaleMethod.invoke(null, (Object[]) null);
			}
			catch (Exception e) {
				log.warn("Unable to retrieve locale from Spring", e);
			}
		}

		if (locale == null) {
			locale = Locale.getDefault();
		}

		return locale;
	}

// convert

	/**
	 * {@link DecoratedConverter#convert(Class, Object)}
	 * @param destinationClass
	 * @param source
	 * @return
	 * @throws TransformationException
	 */
	public final Object convert(Class destinationClass, Object source)
			throws TransformationException {
		return convert(destinationClass, source, null);
	}

	/**
	 * @{link {@link Converter#convert(Class, Object, Locale)}
	 * @param destinationClass
	 * @param source
	 * @param locale
	 * @return
	 */
	public final Object convert(Class destinationClass, Object source,
		Locale locale) {
		initialize();

		if (isPerformingLogging() && log.isTraceEnabled()) {
			log.trace("Converting " + ObjectUtils.getObjectDescription(source)
				+ " to destination type "
				+ ObjectUtils.getObjectDescription(destinationClass)
				+ " in locale " + locale);
		}

		if (locale == null) {
			locale = getLocale();
		}

		if (source == null && isAutomaticallyHandlingNulls()) {
			if (destinationClass != null && destinationClass.isPrimitive()) {
				throw new TransformationException(destinationClass, source);
			}
			return null;
		}

		try {
			return convertImpl(destinationClass, source, locale);
		}
		catch (TransformationException e) {
			throw e;
		}
		catch (Exception e) {
			if (e instanceof RuntimeException && !isWrappingRuntimeExceptions()) {
				throw (RuntimeException) e;
			}
			if (isTransformable(destinationClass, ClassUtils.getClass(source))) {
				throw new TransformationException(destinationClass, source, e);
			}
			throw new TransformationException(
					getClass().getName() + " cannot convert "
						+ ObjectUtils.getObjectDescription(source)
						+ " to an instance of "
						+ ObjectUtils.getObjectDescription(destinationClass), e);
		}
	}

	/**
	 * The implementation of the <code>convert</code> method, which may omit
	 * the invalid argument checks already performed by this base class. By
	 * default, this method creates a new instance of the destinationClass and
	 * copies information from the source to the destination. This
	 * implementation should be fine as-is for Copiers, but Converters will need
	 * to implement this method since they will not be implementing the copy
	 * method.
	 *
	 * @param locale
	 *            the locale in which the conversion should take place. for
	 *            converters that are not locale-aware, the local argument can
	 *            simply be ignored
	 */
	protected Object convertImpl(Class destinationClass, Object source,
		Locale locale) throws Exception {
		Object reuseableSource = createReusableSource(destinationClass, source);
		Object newInstance = createNewInstance(destinationClass, reuseableSource);
		copyImpl(newInstance, reuseableSource, locale, Converter.TRANSFORMATION_TYPE_CONVERT);
		return newInstance;
	}

	protected Object createReusableSource(Class destinationClass, Object source) {
		return source;
	}

// equals

	public boolean equals(Object object1, Object object2, Locale locale) {
		if (locale == null) {
			locale = getLocale();
		}
		return object1 == object2
				|| (object1 != null && equalsUnidirectionalTest(object1, object2, locale))
				|| (object2 != null && equalsUnidirectionalTest(object2, object1, locale));
	}

	public final boolean equals(Object object1, Object object2)
		throws TransformationException {
		return equals(object1, object2, null);
	}

	protected boolean equalsUnidirectionalTest(Object cannotBeNull, Object canBeNull, Locale locale) {
		return cannotBeNull.equals(convert(cannotBeNull.getClass(), canBeNull, locale));
	}

// copy

	public final void copy(Object destination, Object source) throws TransformationException {
		copy(destination, source, null);
	}

	public final void copy(Object destination, Object source, Locale locale) throws TransformationException {

		initialize();

		if (isPerformingLogging() && log.isTraceEnabled()) {
			log.trace("Copying information from "
				+ ObjectUtils.getObjectDescription(source) + " to destination "
				+ ObjectUtils.getObjectDescription(destination) + " in locale "
				+ locale);
		}

		if (destination == null) {
			throw new TransformationException("Destination cannot be null");
		}

		if (source == null && isAutomaticallyHandlingNulls()) {
			throw new TransformationException("Cannot handle null copy source");
		}

		if (locale == null) {
			locale = getLocale();
		}

		try {
			copyImpl(destination, source, locale, Copier.TRANSFORMATION_TYPE_COPY);
		}
		catch (TransformationException e) {
			throw e;
		}
		catch (Exception e) {
			if (e instanceof RuntimeException && !isWrappingRuntimeExceptions()) {
				throw (RuntimeException) e;
			}
		    if (isTransformable(destination.getClass(), source.getClass())) {
		    	throw new TransformationException("Error copying source "
		    			+ ObjectUtils.getObjectDescription(source) + " to destination "
		    			+ ObjectUtils.getObjectDescription(destination), e);
		    }
		    throw new TransformationException("The " + getClass().getName()
		    		+ " cannot copy source '" + source + "' (class "
		    		+ source.getClass().getName() + ") to destination '"
		    		+ destination + "' (class " + destination.getClass().getName()
		    		+ ")");
		}
	}

	/**
	 * Implementation of the copy method.  By default, this method throws
	 * UnsupportedOperationException.
	 */
	protected void copyImpl(Object destination, Object source, Locale locale, Integer preferredTransformationType) throws Exception {
		throw new UnsupportedOperationException();
	}

// misc utility methods

	protected Object createNewInstanceImpl(Class destinationClass, Object source) throws Exception {
		if (CompositeUtils.isSpecializable(getReflector(), InstantiatingReflector.class)) {
			try {
				return getInstantiatingReflector().newInstance(destinationClass, source);
			}
			catch (Exception e) {
				// write a warning to the log and fall back to the superclass'
				// behavior
				if (getLog().isWarnEnabled()) {
					getLog().warn(ObjectUtils.getObjectDescription(getReflector())
						+ " is exposable as an InstantiatingReflector, but failed to instantiate "
						+ ObjectUtils.getObjectDescription(destinationClass), e);
				}
			}
		}
		return destinationClass.newInstance();
	}

	public Object createNewInstance(Class destinationClass, Object source) {
		try {
			return createNewInstanceImpl(destinationClass, source);
		}
		catch (ReflectionException e) {
			throw e;
		}
		catch (Exception e) {
			if (e instanceof RuntimeException && !isWrappingRuntimeExceptions()) {
				throw (RuntimeException) e;
			}
			throw new ReflectionException("Unable to instantiate " + ObjectUtils.getObjectDescription(destinationClass), e);
		}
	}

// property getters and setters

	/**
	 * Indicates if calls to the main transformation methods (convert, copy)
	 * will cause a log message to be recorded
	 */
	protected boolean isPerformingLogging() {
		return true;
	}

	/**
	 * Indicates whether <code>null</code> values will automatically be
	 * converted to <code>null</code> by this base class before even calling
	 * the subclass's {@link #convertImpl(Class, Object, Locale)} method.
	 * Subclasses which depend on this behavior (which is all subclasses, by
	 * default) should include <code>null</code> as one of their source and
	 * destination classes so that the actual behavior of the transformer is
	 * consistent with the values that are returned by the
	 * {@link #isTransformable(Class, Class)} method. The conversions will
	 * happen automatically even if the source and destination classes don't
	 * contain <code>null</code>, but for the sake of consistency the
	 * <code>null</code>s should be included.
	 *
	 * @return whether <code>null</code> values will automatically be
	 *         converted to <code>null</code> by this base class before even
	 *         calling the subclass's
	 *         {@link #convertImpl(Class, Object, Locale)} method
	 *
	 * @since Morph 1.0.2
	 */
	protected boolean isAutomaticallyHandlingNulls() {
		return true;
	}
	
	/**
	 * Indicates whether runtime exceptions should be wrapped as
	 * {@link TransformationException}s. By default, this method returns
	 * <code>true</code>.
	 * 
	 * <p>
	 * Simple transformers in Morph that operate on JDK types like Numbers and
	 * Strings will usually set this value to <code>true</code> so that they
	 * throw TransformationExceptions if problems occur. More complex
	 * transformers that operate on graphs of objects are encouraged to set this
	 * value to <code>false</code> so that runtime exceptions are not wrapped.
	 * This way, problems accessing data will be expressed by the native API of
	 * a user's domain objects and avoid the need to catch Morph-specific
	 * exceptions (assuming the use of runtime exceptions in said domain
	 * objects).
	 * 
	 * @return <code>true</code>
	 * @since Morph 1.0.2
	 */
	protected boolean isWrappingRuntimeExceptions() {
		return true;
	}

	protected Transformer getNestedTransformer() {
// can't do this, otherwise GraphTransformers won't be able to detect when they
// have already set the graph transformer
//		if (nestedTransformer == null) {
//			nestedTransformer = (GraphTransformer) Defaults.getTransformer;
//		}
		return nestedTransformer;
	}

	protected void setNestedTransformer(Transformer nestedTransformer) {
		this.nestedTransformer = nestedTransformer;
	}

	protected boolean isInitialized() {
		return initialized;
	}
	protected void setInitialized(boolean initialized) {
		this.initialized = initialized;
	}

	public boolean isCachingIsTransformableCalls() {
		return cachingIsTransformableCalls;
	}
	public void setCachingIsTransformableCalls(
		boolean cachingIsTransformableCalls) {
		this.cachingIsTransformableCalls = cachingIsTransformableCalls;
	}
	protected Map getTransformableCallCache() {
		return transformableCallCache;
	}
	protected void setTransformableCallCache(Map transformableCallCache) {
		this.transformableCallCache = transformableCallCache;
	}

	protected Log getLog() {
		return log;
	}
	protected void setLog(Log log) {
		this.log = log;
	}

	protected InstantiatingReflector getInstantiatingReflector() {
		return (InstantiatingReflector) getReflector(InstantiatingReflector.class);
	}

	protected Reflector getReflector(Class reflectorType) {
		return (Reflector) CompositeUtils.specialize(getReflector(), reflectorType);
	}

	public Reflector getReflector() {
		if (reflector == null) {
			setReflector(Defaults.createReflector());
		}
		return reflector;
	}
	public void setReflector(Reflector reflector) {
		this.reflector = reflector;
	}

	protected Object clone() throws CloneNotSupportedException {
		BaseTransformer result = (BaseTransformer) super.clone();
		result.transformableCallCache = Collections.synchronizedMap(new HashMap());
		return result;
	}
}