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
package net.sf.morph.context.support;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.sf.composite.util.ObjectUtils;
import net.sf.morph.context.Context;
import net.sf.morph.context.DelegatingContext;
import net.sf.morph.util.ContainerUtils;

/**
 * A bridge between the Context and Map APIs; this class can be used to
 * implement the methods in the Map interface. 
 * 
 * @author Matt Sgarlata
 * @since Nov 22, 2004
 */
public class ContextMapBridge {

	protected Object getDelegate(Context context) {
		return context instanceof DelegatingContext
				? ((DelegatingContext) context).getDelegate() : null;
	}

	public int size(Context context) {
		return getPropertyNames(context).length;
	}

	public void clear(Context context) {
		Object delegate = getDelegate(context);
		if (!(delegate instanceof Map)) {
			throw new UnsupportedOperationException();
		}
		((Map) delegate).clear();
	}

	public boolean isEmpty(Context context) {
		return ObjectUtils.isEmpty(getPropertyNames(context));
	}

	public boolean containsKey(Context context, Object key) {
		return ContainerUtils.contains(getPropertyNames(context), key);
	}

	public boolean containsValue(Context context, Object value) {
		String[] propertyNames = getPropertyNames(context);
		if (ObjectUtils.isEmpty(propertyNames)) {
			return false;
		}
		for (int i=0; i<propertyNames.length; i++) {
			if (ObjectUtils.equals(context.get(propertyNames[i]), value)) {
				return true;
			}
		}
		return false;
	}

	public Collection values(Context context) {
		String[] propertyNames = getPropertyNames(context);
		if (ObjectUtils.isEmpty(propertyNames)) {
			return Collections.EMPTY_LIST;
		}
		List values = new ArrayList(propertyNames.length);
		for (int i=0; i<propertyNames.length; i++) {
			values.add(context.get(propertyNames[i]));
		}
		return values;
	}

	public void putAll(Context context, Map t) {
		checkContextNotNull(context);
		if (t == null || t.isEmpty()) {
			return;
		}
		for (Iterator it = t.entrySet().iterator(); it.hasNext();) {
			Map.Entry e = (Map.Entry) it.next();
			put(context, e.getKey(), e.getValue());
		}
	}

	public Set entrySet(Context context) {
		String[] propertyNames = getPropertyNames(context);
		if (ObjectUtils.isEmpty(propertyNames)) {
			return Collections.EMPTY_SET;
		}
		Set set = ContainerUtils.createOrderedSet();
		for (int i=0; i<propertyNames.length; i++) {
			set.add(new MapEntry(propertyNames[i],
					context.get(propertyNames[i]), false));
		}
		return set;
	}

	public Set keySet(Context context) {
		String[] propertyNames = getPropertyNames(context);
		if (ObjectUtils.isEmpty(propertyNames)) {
			return Collections.EMPTY_SET;
		}
		else {
			Set s = ContainerUtils.createOrderedSet();
			s.addAll(Arrays.asList(propertyNames));
			return s;
		}
	}

	public Object get(Context context, Object key) {
		return checkContextNotNull(context).get((String) key);
	}

	public Object remove(Context context, Object key) {
		Object delegate = getDelegate(context);
		if (delegate instanceof Map) {
			return ((Map) delegate).remove(key);
		}
		throw new UnsupportedOperationException();
	}

	public Object put(Context context, Object key, Object value) {
		if (!(key instanceof String)) {
			throw new IllegalArgumentException("Only string keys can be used");
		}
		Object originalValue = checkContextNotNull(context).get((String) key);
		context.set((String) key, value);
		return originalValue;
	}

	protected Context checkContextNotNull(Context context)
		throws IllegalArgumentException {
		if (context == null) {
			throw new IllegalArgumentException("context cannot be null");
		}
		return context;
	}

	/**
	 * Convenience method
	 * @param context non-null Context
	 * @return propertyNames
	 * @since Morph 1.1
	 */
	protected String[] getPropertyNames(Context context) {
		return checkContextNotNull(context).getPropertyNames();
	}
}
