/*
 * Copyright 2004-2005, 2007, 2010 the original author or authors.
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
package net.sf.morph2.context.contexts;

import net.sf.morph2.Defaults;
import net.sf.morph2.context.Context;
import net.sf.morph2.context.ContextException;
import net.sf.morph2.context.DelegatingContext;
import net.sf.morph2.context.HierarchicalContext;
import net.sf.morph2.reflect.BeanReflector;
import net.sf.morph2.util.ClassUtils;

/**
 * A context that delegates property storage to some delegate and uses
 * {@link net.sf.morph2.reflect.BeanReflector}s to manipulate the information in the
 * delegate. By default this class uses the
 * {@link net.sf.morph2.reflect.reflectors.SimpleDelegatingReflector}, so
 * contexts can be created out of any type for which the reflector can expose a
 * BeanReflector.
 *
 * @author Matt Sgarlata
 * @since Nov 21, 2004
 * @see net.sf.morph2.context.support.SimpleReflectorContext
 * @see net.sf.morph2.context.HierarchicalContext
 */
public class ReflectorHierarchicalContext extends BaseHierarchicalContext implements HierarchicalContext, DelegatingContext {
	
	private BeanReflector beanReflector;

	private Object delegate;
	
	/**
	 * Creates a new, empty context.
	 */
	public ReflectorHierarchicalContext() {
		super();
	}
	
	public ReflectorHierarchicalContext(Object delegate) {
		this();
		setDelegate(delegate);
	}
	
	/**
	 * Creates a new, empty context with the specified parent.  Before this
	 * context is used, the 
	 */
	public ReflectorHierarchicalContext(Context parentContext) {
		super(parentContext);
	}
	
	/**
	 * Create a new ReflectorHierarchicalContext instance.
	 * @param delegate
	 * @param parentContext
	 */
	public ReflectorHierarchicalContext(Object delegate, Context parentContext) {
		super(parentContext);
		this.delegate = delegate;
	}
	
	/**
	 * Check the configuration of this {@link ReflectorHierarchicalContext}.
	 */
	protected void checkConfiguration() {
		if (getBeanReflector() == null) {
			throw new ContextException("The " + getClass().getName()
				+ " requires a beanReflector be set using the setReflector method");
		}
		if (getDelegate() == null) {
			throw new ContextException(
				"The "
					+ getClass().getName()
					+ " requires an object that can have its properties delegate.  Use the setReflected method");
		}
		if (!ClassUtils.inheritanceContains(getBeanReflector().getReflectableClasses(), getDelegate().getClass())) {
			throw new ContextException("The beanReflector of class "
				+ getBeanReflector().getClass().getName()
				+ " cannot reflect objects of class "
				+ getDelegate().getClass().getName());
		}
	}

	/**
	 * {@inheritDoc}
	 */
	protected String[] getPropertyNamesHierarchicalImpl() throws Exception {
		checkConfiguration();
		return getBeanReflector().getPropertyNames(getDelegate());
	}

	/**
	 * {@inheritDoc}
	 */
	protected Object getHierarchicalImpl(String propertyName) throws Exception {
		checkConfiguration();
		return getBeanReflector().isReadable(getDelegate(), propertyName)
				? getBeanReflector().get(getDelegate(), propertyName) : null;
	}

	/**
	 * {@inheritDoc}
	 */
	protected void setHierarchicalImpl(String propertyName, Object propertyValue)
		throws Exception {
		checkConfiguration();
		getBeanReflector().set(getDelegate(), propertyName, propertyValue);
	}

	/**
	 * Get the beanReflector of this ReflectorHierarchicalContext.
	 * @return the beanReflector
	 */
	public synchronized BeanReflector getBeanReflector() {
		if (beanReflector == null) {
			beanReflector = Defaults.createBeanReflector();
		}
		return beanReflector;
	}

	/**
	 * Set the beanReflector of this ReflectorHierarchicalContext.
	 * @param beanReflector the BeanReflector to set
	 */
	public void setBeanReflector(BeanReflector beanReflector) {
		this.beanReflector = beanReflector;
	}

	/**
	 * Get the delegate of this ReflectorHierarchicalContext.
	 * @return the delegate
	 */
	public Object getDelegate() {
		return delegate;
	}

	/**
	 * Set the delegate of this ReflectorHierarchicalContext.
	 * @param delegate the Object to set
	 */
	public void setDelegate(Object delegate) {
		this.delegate = delegate;
	}

}
