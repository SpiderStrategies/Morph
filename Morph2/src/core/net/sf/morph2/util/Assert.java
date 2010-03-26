/*
 * Copyright 2004-2005, 2010 the original author or authors.
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

import java.util.Iterator;

import net.sf.composite.util.ObjectUtils;

/**
 * Assertions built using Morph.
 * 
 * @author Matt Sgarlata
 * @since Apr 18, 2005
 */
public abstract class Assert extends net.sf.composite.util.Assert {

	/**
	 * Validator interface.
	 */
	protected interface Validator {
		public boolean isValid(Object object);
	}

	/**
	 * Assert that each element of the given container passes the specified validator.
	 * @param validator
	 * @param container
	 * @param message
	 */
	protected static void contentsAssertion(Validator validator, Object container, String message) {
		if (ObjectUtils.isEmpty(container))
			return;

		Iterator iterator = ContainerUtils.getIterator(container);
		int numInvalid = 0;
		while (iterator.hasNext()) {
			Object object = iterator.next();
			if (!validator.isValid(object)) {
				numInvalid++;
			}
		}
		if (numInvalid > 0) {
			throw new IllegalArgumentException(message + ", but there were " + numInvalid
					+ " objects which did not meet this criterion.");
		}
	}

	/**
	 * Throws an exception if any object in the given container is
	 * <code>null</code>.
	 * 
	 * @param container
	 *            any container for which Morph provides a
	 *            {@link net.sf.morph2.reflect.ContainerReflector}
	 * @throws IllegalArgumentException
	 *             if any object in the container is <code>null</code>
	 */
	public static void contentsNotNull(Object container) {
		Validator callback = new Validator() {
			public boolean isValid(Object object) {
				return object != null;
			}
		};

		contentsAssertion(callback, container, "The objects in the container cannot be null");
	}

	/**
	 * Throws an exception if any object in the given container is empty, as
	 * defined by {@link net.sf.composite.util.ObjectUtils#isEmpty(Object)}.
	 * 
	 * @param container
	 *            any container for which Morph provides a
	 *            {@link net.sf.morph2.reflect.ContainerReflector}
	 * @throws IllegalArgumentException
	 *             if any object in the container is empty, as defined by
	 *             {@link net.sf.composite.util.ObjectUtils#isEmpty(Object)}
	 */
	public static void contentsNotEmpty(Object container) {
		Validator callback = new Validator() {
			public boolean isValid(Object object) {
				return !ObjectUtils.isEmpty(object);
			}
		};

		contentsAssertion(callback, container, "The objects in the container cannot be empty");
	}

}
