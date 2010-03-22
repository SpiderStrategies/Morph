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
package net.sf.morph2.wrap;

/**
 * A wrapper around another object that allows the data in the object to be
 * manipulated.  Wrappers provide a consistent API for different types.  For
 * example, instead of overloading a method so that it can accept both a
 * Collection and an Object array, a single method signature utilizing the
 * {@link Container} interface may be specified.
 * 
 * @author Matt Sgarlata
 * @since Jan 16, 2005
 */
public interface Wrapper {
	/**
	 * Get the wrapped object.
	 * @return Object
	 */
	public Object getWrappedObject();
	
}
