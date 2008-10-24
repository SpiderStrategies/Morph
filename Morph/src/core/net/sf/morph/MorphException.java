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
package net.sf.morph;

import net.sf.morph.util.NestableRuntimeException;

/**
 * An exception in the Morph framework.
 * 
 * @author Matt Sgarlata
 * @since Nov 16, 2004
 */
public class MorphException extends NestableRuntimeException {

	/**
	 * Create a new MorphException.
	 */
	public MorphException() {
		super();
	}

	/**
	 * Create a new MorphException.
	 * @param message
	 */
	public MorphException(String message) {
		super(message);
	}

	/**
	 * Create a new MorphException.
	 * @param message
	 * @param cause
	 */
	public MorphException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * Create a new MorphException.
	 * @param cause
	 */
	public MorphException(Throwable cause) {
		super(cause);
	}
}
