/*
 * Copyright 2004-2005, 2007-2008, 2010 the original author or authors.
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

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import net.sf.composite.SpecializableComposite;
import net.sf.composite.StrictlyTypedComposite;
import net.sf.composite.util.ObjectUtils;
import net.sf.morph.reflect.BeanReflector;
import net.sf.morph.reflect.CompositeReflector;
import net.sf.morph.reflect.ContainerReflector;
import net.sf.morph.reflect.DecoratedReflector;
import net.sf.morph.reflect.GrowableContainerReflector;
import net.sf.morph.reflect.IndexedContainerReflector;
import net.sf.morph.reflect.InstantiatingReflector;
import net.sf.morph.reflect.MutableIndexedContainerReflector;
import net.sf.morph.reflect.ReflectionException;
import net.sf.morph.reflect.Reflector;
import net.sf.morph.reflect.SizableReflector;
import net.sf.morph.util.ClassUtils;
import net.sf.morph.util.ContainerUtils;
import net.sf.morph.util.ReflectorUtils;

/**
 * Reflector that can be used to combine multiple bean reflectors.  By default,
 * a new instance of this reflector will include all reflectors defined in
 * Morph except the MapReflector (the MapBeanReflector is used
 * instead).
 *
 * @author Matt Sgarlata
 * @since Dec 13, 2004
 */
public class SimpleDelegatingReflector extends BaseCompositeReflector implements
		DecoratedReflector, StrictlyTypedComposite, SpecializableComposite, BeanReflector,
		ContainerReflector, GrowableContainerReflector, IndexedContainerReflector,
		InstantiatingReflector, MutableIndexedContainerReflector, CompositeReflector, Cloneable {

	/**
	 * Construct a new SimpleDelegatingReflector.
	 */
	public SimpleDelegatingReflector() {
		this(null, true);
	}

	/**
	 * Construct a new SimpleDelegatingReflector.
	 * @param components
	 */
	public SimpleDelegatingReflector(Reflector[] components) {
		this(components, false);
	}

	/**
	 * Construct a new SimpleDelegatingReflector.
	 * @param components
	 * @param appendDefaultComponents if true, the components returned from createDefaultComponents() will be added.
	 */
	public SimpleDelegatingReflector(Reflector[] components, boolean appendDefaultComponents) {
		if (appendDefaultComponents) {
			Reflector[] defaultComponents = createDefaultComponents();
			if (ObjectUtils.isEmpty(components)) {
				components = defaultComponents;
			}
			else {
				//use getComponentType() in case a subclass tightens from Reflector;
				Reflector[] newComponents = (Reflector[]) ClassUtils.createArray(
						getComponentType(), components.length + defaultComponents.length);
				System.arraycopy(components, 0, newComponents, 0, components.length);
				System.arraycopy(defaultComponents, 0, newComponents, components.length,
						defaultComponents.length);
				components = newComponents;
			}
		}
		setComponents(components);
	}

	/**
	 * Create the default Reflector components of this {@link SimpleDelegatingReflector}.
	 * @return Reflector
	 */
	protected Reflector[] createDefaultComponents() {
		List componentList = new LinkedList();

		// container reflectors
		componentList.add(new ListReflector());
		componentList.add(new SortedSetReflector());
		componentList.add(new SetReflector());
		componentList.add(new StringTokenizerReflector());
		componentList.add(new ResetableIteratorWrapperReflector());
		componentList.add(new EnumerationReflector());
		componentList.add(new IteratorReflector());
		// have to do this to avoid an infinite loop 
		componentList.add(new ArrayReflector(this));
		componentList.add(new CollectionReflector());

		// result set reflector acts as a container and as a bean reflector
		componentList.add(new ResultSetReflector());

		// the context reflector must be preferred over the map reflector,
		// because all contexts are maps
		componentList.add(new ContextReflector());

		// a map reflector can act both as a container reflector and as a
		// bean reflector
		componentList.add(new MapReflector());

		// bean reflectors
		if (ClassUtils.isServletApiPresent()) {
			componentList.add(new ServletRequestReflector());
			componentList.add(new HttpSessionAttributeReflector());
			componentList.add(new ServletContextAttributeReflector());
		}
		if (ClassUtils.isJspApiPresent()) {
			componentList.add(new PageContextAttributeReflector());
		}
		if (ClassUtils.isBeanUtilsPresent()) {
			componentList.add(new DynaBeanReflector());
		}
		componentList.add(new SimpleInstantiatingReflector());
		componentList.add(new ObjectReflector());

		return (Reflector[]) componentList.toArray(new Reflector[componentList.size()]);
	}

	// internal state initialization/validation

	/**
	 * {@inheritDoc}
	 */
	protected void initializeImpl() throws Exception {
		super.initializeImpl();
		getComponentValidator().validate(this);
	}

	/**
	 * {@inheritDoc}
	 */
	protected Class[] getReflectableClassesImpl() {
		Set set = ContainerUtils.createOrderedSet();
		Object[] reflectors = getComponents();
		for (int i = 0; i < reflectors.length; i++) {
			Class[] reflectableClasses = ((Reflector) reflectors[i]).getReflectableClasses();
			for (int j = 0; j < reflectableClasses.length; j++) {
				set.add(reflectableClasses[j]);
			}
		}
		return (Class[]) set.toArray(new Class[set.size()]);
	}

	// bean reflectors

	/**
	 * {@inheritDoc}
	 */
	protected Object getImpl(Object bean, String propertyName) throws Exception {
		return getBeanReflector(bean).get(bean, propertyName);
	}

	/**
	 * {@inheritDoc}
	 */
	protected String[] getPropertyNamesImpl(Object bean) throws Exception {
		return getBeanReflector(bean).getPropertyNames(bean);
	}

	/**
	 * {@inheritDoc}
	 */
	protected Class getTypeImpl(Object bean, String propertyName) throws Exception {
		return getBeanReflector(bean).getType(bean, propertyName);
	}

	/**
	 * {@inheritDoc}
	 */
	protected boolean isReadableImpl(Object bean, String propertyName) throws Exception {
		return getBeanReflector(bean).isReadable(bean, propertyName);
	}

	/**
	 * {@inheritDoc}
	 */
	protected boolean isWriteableImpl(Object bean, String propertyName) throws Exception {
		return getBeanReflector(bean).isWriteable(bean, propertyName);
	}

	/**
	 * {@inheritDoc}
	 */
	protected void setImpl(Object bean, String propertyName, Object value) throws Exception {
		getBeanReflector(bean).set(bean, propertyName, value);
	}

	// container reflectors

	/**
	 * {@inheritDoc}
	 */
	protected Iterator getIteratorImpl(Object container) throws Exception {
		return getContainerReflector(container).getIterator(container);
	}

	/**
	 * {@inheritDoc}
	 */
	protected Class getContainedTypeImpl(Class clazz) throws Exception {
		return getContainerReflectorForClass(clazz).getContainedType(clazz);
	}

	// sizable reflectors

	/**
	 * {@inheritDoc}
	 */
	protected int getSizeImpl(Object container) throws Exception {
		return getSizableReflector(container).getSize(container);
	}

	// growable reflectors

	/**
	 * {@inheritDoc}
	 */
	protected boolean addImpl(Object container, Object value) throws Exception {
		return getGrowableContainerReflector(container).add(container, value);
	}

	// indexed reflectors

	/**
	 * {@inheritDoc}
	 */
	protected Object getImpl(Object container, int index) throws Exception {
		return getIndexedContainerReflector(container).get(container, index);
	}

	// mutable indexed reflectors

	/**
	 * {@inheritDoc}
	 */
	protected Object setImpl(Object container, int index, Object propertyValue) throws Exception {
		return getMutableIndexedContainerReflector(container).set(container, index, propertyValue);
	}

	/**
	 * {@inheritDoc}
	 */
	protected Object newInstanceImpl(Class clazz, Object parameters) throws Exception {
		InstantiatingReflector reflector = getInstantiatingReflectorForClass(clazz);
		return reflector.newInstance(clazz, parameters);
	}

	/**
	 * {@inheritDoc}
	 */
	protected boolean isReflectableImpl(Class reflectedType, Class reflectorType)
			throws ReflectionException {
		return safeGetReflector(reflectorType, reflectedType) != null;
	}

	/**
	 * Get the contained reflector of the specified type, for the specified type.
	 * @param reflectorType
	 * @param reflectedType
	 * @return Reflector
	 */
	protected Reflector getReflector(Class reflectorType, Class reflectedType) {
		Reflector result = safeGetReflector(reflectorType, reflectedType);
		if (result == null) {
			throw new ReflectionException("Could not find a "
					+ ClassUtils.getUnqualifiedClassName(reflectorType) + " that can reflect "
					+ ObjectUtils.getObjectDescription(reflectedType));
		}
		if (log.isTraceEnabled()) {
			log.trace("Using " + result.toString() + " to reflect "
					+ ObjectUtils.getObjectDescription(reflectedType));
		}
		return result;
	}

	//TODO cache result
	private Reflector safeGetReflector(Class reflectorType, Class reflectedType) {
		for (int i = 0; i < getComponents().length; i++) {
			Reflector component = (Reflector) getComponents()[i];
			if (ReflectorUtils.isReflectable(component, reflectedType, reflectorType)) {
				return component;
			}
		}
		return null;
	}

	/**
	 * Get a BeanReflector for the specified object.
	 * @param bean
	 * @return {@link BeanReflector}
	 */
	protected BeanReflector getBeanReflector(Object bean) {
		return (BeanReflector) getReflector(BeanReflector.class, bean.getClass());
	}

	/**
	 * Get a ContainerReflector for the specified object.
	 * @param bean
	 * @return ContainerReflector
	 */
	protected ContainerReflector getContainerReflector(Object bean) {
		return (ContainerReflector) getReflector(ContainerReflector.class, bean.getClass());
	}

	/**
	 * Get a ContainerReflector for the specified type.
	 * @param reflectedClass
	 * @return ContainerReflector
	 */
	protected ContainerReflector getContainerReflectorForClass(Class reflectedClass) {
		return (ContainerReflector) getReflector(ContainerReflector.class, reflectedClass);
	}

	/**
	 * Get a {@link GrowableContainerReflector} for the specified object.
	 * @param bean
	 * @return {@link GrowableContainerReflector}
	 */
	protected GrowableContainerReflector getGrowableContainerReflector(Object bean) {
		return (GrowableContainerReflector) getReflector(GrowableContainerReflector.class, bean
				.getClass());
	}

	/**
	 * Get a {@link SizableReflector} for the specified Object.
	 * @param bean
	 * @return {@link SizableReflector}
	 */
	protected SizableReflector getSizableReflector(Object bean) {
		return (SizableReflector) getReflector(SizableReflector.class, bean.getClass());
	}

	/**
	 * Get an {@link IndexedContainerReflector} for the specified object.
	 * @param bean
	 * @return {@link IndexedContainerReflector}
	 */
	protected IndexedContainerReflector getIndexedContainerReflector(Object bean) {
		return (IndexedContainerReflector) getReflector(IndexedContainerReflector.class, bean
				.getClass());
	}

	/**
	 * Get a {@link MutableIndexedContainerReflector} for the specified object.
	 * @param bean
	 * @return {@link MutableIndexedContainerReflector}
	 */
	protected MutableIndexedContainerReflector getMutableIndexedContainerReflector(Object bean) {
		return (MutableIndexedContainerReflector) getReflector(
				MutableIndexedContainerReflector.class, bean.getClass());
	}

	/**
	 * Get an {@link InstantiatingReflector} for the specified type.
	 * @param clazz
	 * @return {@link InstantiatingReflector}
	 */
	protected InstantiatingReflector getInstantiatingReflectorForClass(Class clazz) {
		return (InstantiatingReflector) getReflector(InstantiatingReflector.class, clazz);
	}

	/**
	 * {@inheritDoc}
	 */
	// workaround for problem w/constructor using JDK 1.4.2_06 on WinXP SP2
	public Object[] getComponents() {
		if (super.getComponents() == null) {
			setComponents(createDefaultComponents());
		}
		return super.getComponents();
	}

}