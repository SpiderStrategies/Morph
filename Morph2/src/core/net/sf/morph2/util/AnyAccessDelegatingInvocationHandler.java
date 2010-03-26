/*
 * Copyright 2007, 2010 the original author or authors.
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
package net.sf.morph2.util;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;

import net.sf.composite.util.DelegatingInvocationHandler;
import net.sf.composite.util.ObjectPair;

/**
 * DelegatingInvocationHandler that uses reflection to invoke non-public methods
 * that match invocation signatures.
 */
public class AnyAccessDelegatingInvocationHandler extends DelegatingInvocationHandler {
	private static class MethodDescription {
		private final String name;
		private final Class[] parameterTypes;
		private int hashCode;
		private MethodDescription(Method m) {
			this.name = m.getName();
			this.parameterTypes = m.getParameterTypes();
			hashCode = name.hashCode();
			for (int i = 0; i < parameterTypes.length; i++) {
				hashCode += parameterTypes[i].hashCode() * i;
			}
		}

		public boolean equals(Object obj) {
			if (this == obj) {
				return true;
			}
			if (!(obj instanceof MethodDescription)) {
				return false;
			}
			return ((MethodDescription) obj).hashCode == this.hashCode;
		}

		public int hashCode() {
			return hashCode;
		}
	}

	private static final Map METHOD_MAP = new HashMap();

	/**
	 * Construct a new AnyAccessDelegatingInvocationHandler.
	 * @param delegate
	 */
	public AnyAccessDelegatingInvocationHandler(Object delegate) {
		super(delegate);
	}

	/**
	 * {@inheritDoc}
	 */
	protected Method getDelegateMethod(Object delegate, Method method, Object[] args) throws Exception {
		try {
			return super.getDelegateMethod(delegate, method, args);
		} catch (Exception e) {
			MethodDescription methodDescription = new MethodDescription(method);
			ObjectPair key = new ObjectPair(delegate.getClass(), methodDescription);
			Method result;
			synchronized (METHOD_MAP) {
				//deal with possibility of no matching methods:
				if (METHOD_MAP.containsKey(key)) {
					result = (Method) METHOD_MAP.get(key);
				}
				result = getNonPublicMethod(delegate, method);
				METHOD_MAP.put(key, result);
			}
			if (result == null) {
				throw new NoSuchMethodException(method.toString());
			}
			if (!result.isAccessible()) {
				result.setAccessible(true);
			}
			return result;
		}
	}

	private Method getNonPublicMethod(Object delegate, Method method) {
		for (Class current = delegate.getClass(); current != null; current = current.getSuperclass()) {
			Method[] m = current.getDeclaredMethods();
			for (int i = 0; i < m.length; i++) {
				if (!Modifier.isPublic(m[i].getModifiers())
						&& m[i].getName().equals(method.getName())
						&& equals(m[i].getParameterTypes(), method.getParameterTypes())) {
					return m[i];
				}
			}
		}
		return null;
	}

	private static boolean equals(Class[] c1, Class[] c2) {
		if (c1.length != c2.length) {
			return false;
		}
		for (int i = 0; i < c1.length; i++) {
			if (c1[i] != c2[i]) {
				return false;
			}
		}
		return true;
	}
}
