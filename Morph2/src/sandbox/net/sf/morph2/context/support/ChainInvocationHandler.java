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
package net.sf.morph2.context.support;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.Map;

import net.sf.composite.util.DelegatingInvocationHandler;
import net.sf.morph2.context.contexts.MapContext;
import net.sf.morph2.context.contexts.ReflectorHierarchicalContext;

/**
 * Invocation handler used by the
 * {@link net.sf.morph2.context.support.ChainContextCreator}.
 * 
 * @author Matt Sgarlata
 * @since Dec 29, 2004
 */
class ChainInvocationHandler extends DelegatingInvocationHandler implements InvocationHandler {

	private ReflectorHierarchicalContext reflectorHierarchicalContext;
	
	public ChainInvocationHandler(Object delegate) {
		super(delegate);
		setReflectorHierarchicalContext(new ReflectorHierarchicalContext(delegate));
		getReflectorHierarchicalContext().setParentContext(new MapContext());
	}
	
	protected Object getDelegate(Object proxy, Method method, Object[] args) {
		if (method.getDeclaringClass().isAssignableFrom(Map.class) ||
			method.getDeclaringClass().isAssignableFrom(ReflectorHierarchicalContext.class)) {
			return getReflectorHierarchicalContext();
		}
		return super.getDelegate(proxy, method, args);
	}

	protected ReflectorHierarchicalContext getReflectorHierarchicalContext() {
		return reflectorHierarchicalContext;
	}
	protected void setReflectorHierarchicalContext(
		ReflectorHierarchicalContext reflectorHierarchicalContext) {
		this.reflectorHierarchicalContext = reflectorHierarchicalContext;
	}
	
}
