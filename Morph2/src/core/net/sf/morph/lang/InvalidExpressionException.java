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
package net.sf.morph.lang;

/**
 * Indicates an invalid expression was detected.
 * 
 * @author Matt Sgarlata
 * @since Nov 28, 2004
 */
public class InvalidExpressionException extends LanguageException {

	/**
	 * Create a new InvalidExpressionException instance.
	 */
	public InvalidExpressionException() {
		super();
	}

	/**
	 * Create a new InvalidExpressionException instance.
	 * @param message
	 */
	public InvalidExpressionException(String message) {
		super(message);
	}

	/**
	 * Create a new InvalidExpressionException instance.
	 * @param message
	 * @param cause
	 */
	public InvalidExpressionException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * Create a new InvalidExpressionException instance.
	 * @param cause
	 */
	public InvalidExpressionException(Throwable cause) {
		super(cause);
	}
}
