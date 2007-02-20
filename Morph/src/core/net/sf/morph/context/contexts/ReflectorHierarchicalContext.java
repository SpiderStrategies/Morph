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
package net.sf.morph.context.contexts;

import net.sf.morph.Defaults;
import net.sf.morph.context.Context;
import net.sf.morph.context.ContextException;
import net.sf.morph.context.DelegatingContext;
import net.sf.morph.context.HierarchicalContext;
import net.sf.morph.reflect.BeanReflector;
import net.sf.morph.util.ClassUtils;

/**
 * A context that delegates property storage to some delegate and uses
 * {@link net.sf.morph.reflect.BeanReflector}s to manipulate the information in the
 * delegate. By default this class uses the
 * {@link net.sf.morph.reflect.reflectors.SimpleDelegatingReflector}, so
 * contexts can be created out of any type for which the reflector can expose a
 * BeanReflector.
 *
 * @author Matt Sgarlata
 * @since Nov 21, 2004
 * @see net.sf.morph.context.support.SimpleReflectorContext
 * @see net.sf.morph.context.HierarchicalContext
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
	
	public ReflectorHierarchicalContext(Object delegate, Context parentContext) {
		super(parentContext);
		this.delegate = delegate;
	}
	
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

	protected String[] getPropertyNamesHierarchicalImpl() throws Exception {
		checkConfiguration();
		return getBeanReflector().getPropertyNames(getDelegate());
	}

	protected Object getHierarchicalImpl(String propertyName) throws Exception {
		checkConfiguration();
		return getBeanReflector().isReadable(getDelegate(), propertyName)
				? getBeanReflector().get(getDelegate(), propertyName) : null;
	}

	protected void setHierarchicalImpl(String propertyName, Object propertyValue)
		throws Exception {
		checkConfiguration();
		getBeanReflector().set(getDelegate(), propertyName, propertyValue);
	}

	public BeanReflector getBeanReflector() {
		if (beanReflector == null) {
			beanReflector = Defaults.createBeanReflector();
		}
		return beanReflector;
	}

	public void setBeanReflector(BeanReflector beanReflector) {
		this.beanReflector = beanReflector;
	}

	public Object getDelegate() {
		return delegate;
	}

	public void setDelegate(Object reflected) {
		this.delegate = reflected;
	}

// sgarlatam 12/11/2004: turns out this is more annoying than helpful	
//	public String toString() {
//		return "[delegate=" + ObjectUtils.getObjectDescription(delegate) + ",parent=" + ObjectUtils.getObjectDescription(getParentContext()) + "]";
//	}
}
