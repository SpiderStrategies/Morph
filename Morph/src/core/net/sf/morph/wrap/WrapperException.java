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
package net.sf.morph.wrap;

import net.sf.morph.MorphException;

/**
 * Exception thrown by a Wrapper.
 * @author Matt Sgarlata
 * @since Jan 31, 2005
 */
public class WrapperException extends MorphException {
	/**
	 * Create a new WrapperException instance.
	 */
	public WrapperException() {
		super();
	}

	/**
	 * Create a new WrapperException instance.
	 * @param message
	 */
	public WrapperException(String message) {
		super(message);
	}

	/**
	 * Create a new WrapperException instance.
	 * @param message
	 * @param cause
	 */
	public WrapperException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * Create a new WrapperException instance.
	 * @param cause
	 */
	public WrapperException(Throwable cause) {
		super(cause);
	}
}
