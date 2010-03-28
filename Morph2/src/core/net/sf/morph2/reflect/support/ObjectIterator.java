/*
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
package net.sf.morph2.reflect.support;

import java.util.Iterator;
import java.util.NoSuchElementException;

import net.sf.composite.util.ObjectUtils;

/**
 * Exposes an object as an iterator by treating the object as a collection with
 * one element. That means <code>hasNext</code> will return <code>true</code>
 * before <code>next</code> has been called, and will return
 * <code>false</code> after it has been called.
 * 
 * @author Matt Sgarlata
 * @since Jan 8, 2005
 */
public class ObjectIterator implements Iterator {
	private static final String NSEE = "There is only one element in a "
			+ ObjectUtils.getObjectDescription(ObjectIterator.class);

	private Object object;
	private boolean hasNext = true;

	/**
	 * Create a new ObjectIterator instance.
	 * @param object
	 */
	public ObjectIterator(Object object) {
		this.object = object;
	}

	/**
	 * {@inheritDoc}
	 */
	public void remove() {
		throw new UnsupportedOperationException();
	}

	/**
	 * {@inheritDoc}
	 */
	public boolean hasNext() {
		return hasNext;
	}

	/**
	 * {@inheritDoc}
	 */
	public Object next() {
		if (!hasNext) {
			throw new NoSuchElementException(NSEE);
		}
		hasNext = false;
		return object;
	}

}
