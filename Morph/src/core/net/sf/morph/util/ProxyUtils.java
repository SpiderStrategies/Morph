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
package net.sf.morph.util;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Proxy utility methods.
 * @since Morph 1.0.2
 *
 * @author Matt Benson
 */
public abstract class ProxyUtils {

	/**
	 * Get a Proxy adding the specified interface to the specified object.
	 * @param object
	 * @param clazz
	 * @return Proxy
	 */
	public static Proxy getProxy(Object object, Class clazz) {
		return getProxy(object, clazz, new AnyAccessDelegatingInvocationHandler(object));
	}

	/**
	 * Get a Proxy adding the specified interface to the specified object.
	 * @param object
	 * @param clazz
	 * @param ih
	 * @return Proxy
	 */
	public static Proxy getProxy(Object object, Class clazz, InvocationHandler ih) {
		return getProxy(object, new Class[] { clazz }, ih);
	}

	/**
	 * Get a Proxy adding the specified interfaces to the specified object.
	 * @param object
	 * @param addInterfaces
	 * @param ih
	 * @return Proxy
	 */
	public static Proxy getProxy(Object object, Class[] addInterfaces,
			InvocationHandler ih) {
		for (int i = 0; addInterfaces != null && i < addInterfaces.length; i++) {
			if (!addInterfaces[i].isInterface()) {
				throw new IllegalArgumentException(addInterfaces[i]
						+ " is not an interface");
			}
		}
		ArrayList allInterfaces = new ArrayList(Arrays.asList(ClassUtils
				.getInterfaces(object.getClass())));
		for (int i = 0; i < addInterfaces.length; i++) {
			if (!allInterfaces.contains(addInterfaces[i])) {
				allInterfaces.add(addInterfaces[i]);
			}
		}
		return (Proxy) Proxy.newProxyInstance(object.getClass().getClassLoader(),
				(Class[]) allInterfaces.toArray(new Class[allInterfaces.size()]), ih);
	}
}
