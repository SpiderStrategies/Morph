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

import java.lang.reflect.Proxy;

import net.sf.morph2.context.HierarchicalContext;

import org.apache.commons.chain.Context;


/**
 * Creates {@link org.apache.commons.chain.Context} instances out of any Object.
 * 
 * @author Matt Sgarlata
 * @since Dec 29, 2004
 */
public class ChainContextCreator {

	/**
	 * Creates a {@link org.apache.commons.chain.Context} instance out of any
	 * Object.  The context instance is backed by the given object and by a
	 * Map, so that if properties are not accessible via the object, they will
	 * be via the Map.
	 * 
	 * @param object the object to be exposed as a context
	 * @returns a {@link org.apache.commons.chain.Context} instance backed by
	 * the given object 
	 * @throws IllegalArgumentException if object is <code>null</code>
	 */
	public Context createContext(Object object) throws IllegalArgumentException {
		if (object == null) {
			throw new IllegalArgumentException("An object from which a context is to be created must be specified");
		}
		
	    Context context = (Context) Proxy.newProxyInstance(
			Context.class.getClassLoader(), new Class[] { Context.class,
				HierarchicalContext.class },
			new ChainInvocationHandler(object));
		return context;
	}
	
}
