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
package net.sf.morph2.context;

import net.sf.morph2.MorphException;

/**
 * An exception related to accessing the information stored in a context.
 * 
 * @author Matt Sgarlata
 * @since Nov 19, 2004
 */
public class ContextException extends MorphException {

	/**
	 * Create a new ContextException instance.
	 */
	public ContextException() {
		super();
	}

	/**
	 * Create a new ContextException instance.
	 * @param message
	 */
	public ContextException(String message) {
		super(message);
	}

	/**
	 * Create a new ContextException instance.
	 * @param message
	 * @param cause
	 */
	public ContextException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * Create a new ContextException instance.
	 * @param cause
	 */
	public ContextException(Throwable cause) {
		super(cause);
	}
}
