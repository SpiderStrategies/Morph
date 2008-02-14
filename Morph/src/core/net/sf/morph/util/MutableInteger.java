/*
 * Copyright 2007-2008 the original author or authors.
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

/**
 * Holder for a mutable int. Good for ThreadLocal stacks, etc.
 * 
 * @author Matt Benson
 * @since Morph 1.1
 */
public class MutableInteger {
	public int value;

	/**
	 * Construct a new Int with the default value (0).
	 */
	public MutableInteger() {
	}

	/**
	 * Construct a new Int with the specified value.
	 * 
	 * @param value
	 */
	public MutableInteger(int value) {
		this.value = value;
	}
}
