/*
 * Copyright 2004-2005 the original author or authors.
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

import java.util.Enumeration;
import java.util.Iterator;

/**
 * Exposes an Enumeration as an Iterator.
 * 
 * @author Matt Sgarlata
 * @since Dec 5, 2004
 */
public class EnumerationIterator implements Iterator {
	
	private Enumeration enumeration;
	
	public EnumerationIterator(Enumeration enumeration) {
		this.enumeration = enumeration;
	}
	
	public boolean hasNext() {
		return enumeration.hasMoreElements();
	}

	public Object next() {
		return enumeration.nextElement();	
	}
	
	public void remove() {
		throw new UnsupportedOperationException();
	}

}
