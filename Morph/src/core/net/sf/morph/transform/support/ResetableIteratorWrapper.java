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
package net.sf.morph.transform.support;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

import net.sf.composite.util.ObjectUtils;

/**
 * Reads the contents of an Iterator and saves them so that the Iterator can be
 * iterated over multiple times.
 * 
 * @author Matt Sgarlata
 * @since Dec 5, 2004
 */
public class ResetableIteratorWrapper implements Iterator {

	private boolean frozen;
	private int index;
	private List list;
	
	public ResetableIteratorWrapper() {
		this.frozen = false;
		this.index = 0;
	}
	
	public ResetableIteratorWrapper(Iterator iterator) {
		this();
		setIterator(iterator);
	}

	public boolean hasNext() {
		if (!frozen) {
			throw new IllegalStateException("You must set the iterator to wrap before calling this method");
		}
		if (ObjectUtils.isEmpty(list)) {
			return false;
		}
		return index < list.size();
	}

	public Object next() {
		if (!frozen) {
			throw new IllegalStateException("You must set the iterator to wrap before calling this method");
		}
		if (list == null) {
			throw new NoSuchElementException("The supplied iterator was null");
		}
		if (index == list.size()) {
			throw new NoSuchElementException("There are no more elements to iterate over");
		}
		return list.get(index++);		
	}

	public void remove() {
		throw new UnsupportedOperationException();
	}
	
	public void reset() {
		this.index = 0;
	}
	
	/**
	 * Returns a fresh copy of the wrapped iterator that is ready for another
	 * iteration.
	 * 
	 * @return <code>null</code>, if the supplied 
	 */
	public Iterator getIterator() {
		if (list == null) {
			return null;
		}
		else {
			return list.iterator();
		}
	}
	
	/**
	 * Sets the delegate iterator for this wrapper.
	 * @throws IllegalStateException if the iterator has already been set
	 */
	public void setIterator(Iterator iterator) {
		if (frozen) {
			throw new IllegalStateException("You can only set the delegate iterator once");
		}
		this.frozen = true;
		if (iterator != null) {
			list = new ArrayList();
			while (iterator.hasNext()) {
				list.add(iterator.next());
			}
		}
	}

}
