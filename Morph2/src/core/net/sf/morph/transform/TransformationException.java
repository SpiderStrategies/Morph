/*
 * Copyright 2004-2005, 2008 the original author or authors.
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
package net.sf.morph.transform;

import net.sf.composite.util.ObjectUtils;
import net.sf.morph.MorphException;

/**
 * Transformation Exception
 * 
 * @author Matt Sgarlata
 * @since Nov 26, 2004
 */
public class TransformationException extends MorphException {

	/**
	 * Create a new TransformationException.
	 */
	public TransformationException() {
		super();
	}

	/**
	 * Create a new TransformationException.
	 * @param message
	 */
	public TransformationException(String message) {
		super(message);
	}

	/**
	 * Create a new TransformationException.
	 * @param message
	 * @param cause
	 */
	public TransformationException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * Create a new TransformationException.
	 * @param cause
	 */
	public TransformationException(Throwable cause) {
		super(cause);
	}

	/**
	 * Create a new TransformationException.
	 * @param destinationClass
	 * @param source
	 */
	public TransformationException(Class destinationClass, Object source) {
		this(destinationClass, source, null, null);
	}

	/**
	 * Create a new TransformationException.
	 * @param destinationClass
	 * @param source
	 * @param t
	 */
	public TransformationException(Class destinationClass, Object source, Throwable t) {
		this(destinationClass, source, t, null);
	}

	/**
	 * Create a new TransformationException.
	 * @param destinationClass
	 * @param source
	 * @param t
	 * @param message
	 */
	public TransformationException(Class destinationClass, Object source, Throwable t,
			String message) {
		super("Unable to convert " + ObjectUtils.getObjectDescription(source) + " to "
				+ destinationClass + (ObjectUtils.isEmpty(message) ? "" : " due to " + message), t);
	}

	/**
	 * Create a new TransformationException.
	 * @param destination
	 * @param source
	 * @param t
	 */
	public TransformationException(Object destination, Object source, Throwable t) {
		super("Unable to copy from " + ObjectUtils.getObjectDescription(source) + " to "
				+ ObjectUtils.getObjectDescription(destination), t);
	}

	/**
	 * Create a new TransformationException.
	 * @param destination
	 * @param source
	 */
	public TransformationException(Object destination, Object source) {
		this(destination, source, null);
	}

}