/*
 * Copyright 2007 the original author or authors.
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
package net.sf.morph.integration.velocity;

import net.sf.morph.Defaults;
import net.sf.morph.reflect.BeanReflector;
import net.sf.morph.util.Assert;

import org.apache.velocity.context.AbstractContext;
import org.apache.velocity.context.Context;

/**
 * Single parent bean Velocity context implemented with a Reflector.
 * @author Matt Benson
 * @since Morph 1.0.2
 */
public class ReflectorVelocityContext extends AbstractContext {
	private Object bean;
	private BeanReflector reflector;

	/**
	 * Create a new ReflectorVelocityContext which must subsequently have its <code>bean</code> property set.
	 */
	public ReflectorVelocityContext() {
		super();
	}

	/**
	 * Create a new ReflectorVelocityContext.
	 * @param bean
	 */
	public ReflectorVelocityContext(Object bean) {
		setBean(bean);
	}

	/**
	 * Create a new ReflectorVelocityContext.
	 * @param bean
	 * @param inner
	 */
	public ReflectorVelocityContext(Object bean, Context inner) {
		super(inner);
		setBean(bean);
	}

	/**
	 * Create a new ReflectorVelocityContext.
	 * @param bean
	 * @param reflector
	 */
	public ReflectorVelocityContext(Object bean, BeanReflector reflector) {
		setBean(bean);
		setReflector(reflector);
	}

	/**
	 * Create a new ReflectorVelocityContext.
	 * @param bean
	 * @param reflector
	 * @param inner
	 */
	public ReflectorVelocityContext(Object bean, BeanReflector reflector, Context inner) {
		super(inner);
		setBean(bean);
		setReflector(reflector);
	}

	/**
	 * {@inheritDoc}
	 */
	public boolean internalContainsKey(Object arg0) {
		return arg0 instanceof String
				&& getReflector().isReadable(getBean(), (String) arg0);
	}

	/**
	 * {@inheritDoc}
	 */
	public Object internalGet(String arg0) {
		return internalContainsKey(arg0) ? getReflector().get(getBean(), arg0) : null;
	}

	/**
	 * {@inheritDoc}
	 */
	public Object[] internalGetKeys() {
		return getReflector().getPropertyNames(getBean());
	}

	/**
	 * {@inheritDoc}
	 */
	public Object internalPut(String arg0, Object arg1) {
		Object result = get(arg0);
		getReflector().set(getBean(), arg0, arg1);
		return result;
	}

	/**
	 * Set to null and take our lumps.
	 */
	public Object internalRemove(Object arg0) {
		return put((String) arg0, null);
	}

	/**
	 * Get the bean.
	 * @return Object
	 */
	public Object getBean() {
		return bean;
	}

	/**
	 * Set the bean.
	 * @param bean the Object to set
	 */
	public void setBean(Object bean) {
		Assert.notNull(bean, "bean");
		this.bean = bean;
	}

	/**
	 * Get the reflector.
	 * @return BeanReflector
	 */
	public synchronized BeanReflector getReflector() {
		if (reflector == null) {
			setReflector(Defaults.createBeanReflector());
		}
		return reflector;
	}

	/**
	 * Set the reflector.
	 * @param reflector the BeanReflector to set
	 */
	public synchronized void setReflector(BeanReflector reflector) {
		this.reflector = reflector;
	}

}
