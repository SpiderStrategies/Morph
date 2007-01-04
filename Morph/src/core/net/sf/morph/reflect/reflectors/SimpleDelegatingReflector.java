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
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package net.sf.morph.reflect.reflectors;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import net.sf.composite.Defaults;
import net.sf.composite.SpecializableComposite;
import net.sf.composite.StrictlyTypedComposite;
import net.sf.composite.specialize.SpecializationException;
import net.sf.composite.specialize.Specializer;
import net.sf.composite.specialize.specializers.CachingSpecializerProxy;
import net.sf.composite.specialize.specializers.CloningSpecializer;
import net.sf.composite.util.CompositeUtils;
import net.sf.composite.util.ObjectUtils;
import net.sf.composite.validate.ComponentValidator;
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

/**
 * Reflector that can be used to combine multiple bean reflectors.  By default,
 * a new instance of this reflector will include all reflectors defined in
 * Morph except the MapReflector (the MapBeanReflector is used
 * instead).
 * 
 * @author Matt Sgarlata
 * @since Dec 13, 2004
 */
public class SimpleDelegatingReflector extends BaseReflector implements
		DecoratedReflector, StrictlyTypedComposite, SpecializableComposite,
		BeanReflector, ContainerReflector, GrowableContainerReflector,
		IndexedContainerReflector, InstantiatingReflector,
		MutableIndexedContainerReflector, CompositeReflector, Cloneable {
	
	private Object[] components;
	private Specializer specializer;
	private ComponentValidator componentValidator;
	private boolean failFast;
	
	protected Reflector[] createDefaultComponents() {
		List componentList = new LinkedList();
		
		// container reflectors
		componentList.add(new ListReflector());
		componentList.add(new SortedSetReflector());
		componentList.add(new SetReflector());
		componentList.add(new EnumerationReflector());
		componentList.add(new IteratorReflector());
		componentList.add(new ArrayReflector());
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
			componentList.add(new PageContextAttributeReflector());
//				componentList.add(new ServletRequestParameterReflector());
			componentList.add(new ServletRequestAttributeReflector());
			componentList.add(new HttpSessionAttributeReflector());
			componentList.add(new ServletContextAttributeReflector());
		}		
		if (ClassUtils.isBeanUtilsPresent()) {
			componentList.add(new DynaBeanReflector());
		}
		componentList.add(new SimpleInstantiatingReflector());
		componentList.add(new ObjectReflector());
		
		return (Reflector[]) componentList.toArray(new Reflector[componentList.size()]);
	}
	
	public SimpleDelegatingReflector() {
		super();
	}
	
// internal state initialization/validation
	
	protected void initializeImpl() throws Exception {
		super.initializeImpl();
		
		getComponentValidator().validate(this);
	}
	
	public Class[] getReflectableClassesImpl() {
		Set set = new HashSet();
		for (int i=0; i<components.length; i++) {
			Class[] reflectableClasses = ((Reflector) components[i]).getReflectableClasses(); 
			set.addAll(Arrays.asList(reflectableClasses));
		}
		return (Class[]) set.toArray(new Class[set.size()]); 
	}
	
//	public Class getDelegateClass() {
//		return Reflector.class;
//	}
	
// bean reflectors
	
	protected Object getImpl(Object bean, String propertyName) throws Exception {
		return getBeanReflector(bean).get(bean, propertyName);
	}
	protected String[] getPropertyNamesImpl(Object bean) throws Exception {
		return getBeanReflector(bean).getPropertyNames(bean);
	}
	protected Class getTypeImpl(Object bean, String propertyName)
		throws Exception {
		return getBeanReflector(bean).getType(bean, propertyName);
	}
	protected boolean isReadableImpl(Object bean, String propertyName)
		throws Exception {
		return getBeanReflector(bean).isReadable(bean, propertyName);
	}
	protected boolean isWriteableImpl(Object bean, String propertyName)
		throws Exception {
		return getBeanReflector(bean).isWriteable(bean, propertyName);
	}
	
	protected void setImpl(Object bean, String propertyName, Object value)
		throws Exception {
		getBeanReflector(bean).set(bean, propertyName, value);
	}
	
// container reflectors
	
	protected Iterator getIteratorImpl(Object container) throws Exception {
		return getContainerReflector(container).getIterator(container);
	}

	protected Class getContainedTypeImpl(Class clazz) throws Exception {
		return getContainerReflectorForClass(clazz).getContainedType(clazz);
	}
	
// sizable reflectors
	
	protected int getSizeImpl(Object container) throws Exception {
		return getSizableReflector(container).getSize(container);
	}	
	
// growable reflectors	
	
	protected boolean addImpl(Object container, Object value) throws Exception {
		return getGrowableContainerReflector(container).add(container, value);
	}
	
// indexed reflectors
	
	protected Object getImpl(Object container, int index) throws Exception {
//		checkDelegates();
		return getIndexedContainerReflector(container).get(container, index);
	}

// mutable indexed reflectors
	
	protected Object setImpl(Object container, int index, Object propertyValue)
		throws Exception {
//		checkDelegates();
		return getMutableIndexedContainerReflector(container).set(container, index, propertyValue);
	}

	protected Object newInstanceImpl(Class clazz) throws Exception {
		InstantiatingReflector reflector = getInstantiatingReflectorForClass(clazz);
		return reflector.newInstance(clazz);
	}
	
	public boolean isReflectableImpl(Class reflectedType,
			Class reflectorType) throws ReflectionException {
		for (int i=0; i<getComponents().length; i++) {				
			Reflector component = (Reflector) getComponents()[i];
			if (reflectorType.isAssignableFrom(component.getClass()) &&
				ClassUtils.inheritanceContains(component.getReflectableClasses(), reflectedType)) {
				return true;
			}
		}
		
		return false;
	}
	
	protected Reflector getReflector(Class reflectorType, Class reflectedType) {
		for (int i=0; i<getComponents().length; i++) {				
			Reflector component = (Reflector) getComponents()[i];
			if (reflectorType.isAssignableFrom(component.getClass()) &&
					ClassUtils.inheritanceContains(component.getReflectableClasses(), reflectedType)) {
				if (log.isTraceEnabled()) {
					log.trace("Using "
						+ component.getClass().getName()
						+ " to reflect "
						+ ObjectUtils.getObjectDescription(reflectedType));
				}
				
				return component;
			}
		}
		
		throw new ReflectionException("Could not find a "
				+ ClassUtils.getUnqualifiedClassName(reflectorType)
				+ " that can reflect "
				+ ObjectUtils.getObjectDescription(reflectedType));
	}
	
	protected BeanReflector getBeanReflector(Object bean) {
		return (BeanReflector) getReflector(BeanReflector.class, bean.getClass());
	}
	
	protected ContainerReflector getContainerReflector(Object bean) {
		return (ContainerReflector) getReflector(ContainerReflector.class, bean.getClass());
	}
	
	protected ContainerReflector getContainerReflectorForClass(Class reflectedClass) {
		return (ContainerReflector) getReflector(ContainerReflector.class, reflectedClass);
	}
	
	protected GrowableContainerReflector getGrowableContainerReflector(Object bean) {
		return (GrowableContainerReflector) getReflector(GrowableContainerReflector.class, bean.getClass());
	}
	
	protected SizableReflector getSizableReflector(Object bean) {
		return (SizableReflector) getReflector(SizableReflector.class, bean.getClass());
	}
	
	protected IndexedContainerReflector getIndexedContainerReflector(Object bean) {
		return (IndexedContainerReflector) getReflector(IndexedContainerReflector.class, bean.getClass());
	}
	
	protected MutableIndexedContainerReflector getMutableIndexedContainerReflector(Object bean) {
		return (MutableIndexedContainerReflector) getReflector(MutableIndexedContainerReflector.class, bean.getClass());
	}
	
	protected InstantiatingReflector getInstantiatingReflectorForClass(Class clazz) {
		return (InstantiatingReflector) getReflector(InstantiatingReflector.class, clazz);
	}
	
	
	
	public boolean isSpecializable(Class type) throws SpecializationException {
		initialize();
		return getSpecializer().isSpecializable(this, type);
	}
	
	public Object specialize(Class type) {
		initialize();
		return getSpecializer().specialize(this, type);
	}
	
	public Class getComponentType() {
		return Reflector.class;
	}
	
	protected boolean isPerformingLogging() {
		// let the delegate do the logging
		return false;
	}
	
	public Object clone() throws CloneNotSupportedException {
		return super.clone();
	}

	public String toString() {
		return CompositeUtils.toString(this);
	}

	// workaround for problem w/constructor using JDK 1.4.2_06 on WinXP SP2
	public Object[] getComponents() {
		if (components == null) {
			setComponents(createDefaultComponents());
		}
		return components;
	}
	public void setComponents(Object[] components) {
		setInitialized(false);
		this.components = components;
	}

	public boolean isFailFast() {
		return failFast;
	}
	public void setFailFast(boolean failFast) {
		this.failFast = failFast;
	}

	public Specializer getSpecializer() {
		if (specializer == null) {
			specializer = new CachingSpecializerProxy(new CloningSpecializer()); 
		}
		return specializer;
	}
	public void setSpecializer(Specializer specializer) {
		this.specializer = specializer;
	}

	public ComponentValidator getComponentValidator() {
		if (componentValidator == null) {
			componentValidator = Defaults.createComponentValidator();
		}
		return componentValidator;
	}
	public void setComponentValidator(ComponentValidator validator) {
		this.componentValidator = validator;
	}
}