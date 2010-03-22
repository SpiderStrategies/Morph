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
package net.sf.morph2.transform.support;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

/**
 * Reads the contents of an Iterator and saves them so that the Iterator can be
 * iterated over multiple times.
 * 
 * @author Matt Sgarlata
 * @since Dec 5, 2004
 */
public class ResetableIteratorWrapper implements Iterator {
	private static final String NULL_ITERATOR = "The supplied iterator was null";
	private static final String NEVER_SET = "You must set the iterator to wrap before calling this method";
	private static final String ALREADY_SET = "You can only set the delegate iterator once";
	private static final String NO_MORE = "There are no more elements to iterate over";

	private boolean frozen;
	private int index;
	private List list;

	/**
	 * Create a new ResetableIteratorWrapper instance.
	 */
	public ResetableIteratorWrapper() {
		this.frozen = false;
		this.index = 0;
	}

	/**
	 * Create a new ResetableIteratorWrapper instance.
	 * @param iterator
	 */
	public ResetableIteratorWrapper(Iterator iterator) {
		this();
		setIterator(iterator);
	}

	/**
	 * {@inheritDoc}
	 */
	public synchronized boolean hasNext() {
		if (!frozen) {
			throw new IllegalStateException(NEVER_SET);
		}
		return list != null && index < list.size();
	}

	/**
	 * {@inheritDoc}
	 */
	public synchronized Object next() {
		if (hasNext()) {
			return list.get(index++);
		}
		throw new NoSuchElementException(NO_MORE);
	}

	/**
	 * {@inheritDoc}
	 */
	public void remove() {
		throw new UnsupportedOperationException();
	}

	/**
	 * Reset this {@link ResetableIteratorWrapper}.
	 */
	public synchronized void reset() {
		this.index = 0;
	}

	/**
	 * Returns a fresh copy of the wrapped iterator that is ready for another
	 * iteration.
	 * 
	 * @return <code>null</code> if the delegate iterator was never set
	 */
	public synchronized Iterator getIterator() {
		return list == null ? null : list.iterator();
	}

	/**
	 * Sets the delegate iterator for this wrapper.
	 * @param iterator the Iterator to set
	 * @throws IllegalStateException if the iterator has already been set
	 * @throws IllegalArgumentException if the iterator is null
	 */
	public synchronized void setIterator(Iterator iterator) {
		if (frozen) {
			throw new IllegalStateException(ALREADY_SET);
		}
		if (iterator == null) {
			throw new IllegalArgumentException(NULL_ITERATOR);
		}
		this.frozen = true;
		list = new ArrayList();
		while (iterator.hasNext()) {
			list.add(iterator.next());
		}
	}

	/**
	 * Get the size of the underlying Iterator.
	 * @return int
	 */
	public int size() {
		if (!frozen) {
			throw new IllegalStateException(NEVER_SET);
		}
		return list.size();
	}

	/**
	 * {@inheritDoc}
	 */
	public String toString() {
		return "ResetableIteratorWrapper" + list;
	}

}
