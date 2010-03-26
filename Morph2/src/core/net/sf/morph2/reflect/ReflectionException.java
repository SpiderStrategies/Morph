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
package net.sf.morph2.reflect;

import net.sf.morph2.MorphException;

/**
 * Indicates an error occurred accessing or manipulating information while using
 * a Reflector.
 * 
 * @author Matt Sgarlata
 * @since Nov 4, 2004
 */
public class ReflectionException extends MorphException {

	/**
	 * Create a new ReflectionException instance.
	 */
	public ReflectionException() {
		super();
	}

	/**
	 * Create a new ReflectionException instance.
	 * @param arg0
	 */
	public ReflectionException(String arg0) {
		super(arg0);
	}

	/**
	 * Create a new ReflectionException instance.
	 * @param arg0
	 * @param arg1
	 */
	public ReflectionException(String arg0, Throwable arg1) {
		super(arg0, arg1);
	}

	/**
	 * Create a new ReflectionException instance.
	 * @param arg0
	 */
	public ReflectionException(Throwable arg0) {
		super(arg0);
	}
}
