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
package net.sf.morph.context.support;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
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
		return ((DelegatingContext) context).getDelegate();
	}
	
	public int size(Context context) {
		checkContextNotNull(context);
		return context.getPropertyNames().length;
	}

	public void clear(Context context) {
		if (context instanceof DelegatingContext &&
			getDelegate(context) instanceof Map) {
			((Map) getDelegate(context)).clear();
		}
		else {
			throw new UnsupportedOperationException();
		}
	}

	public boolean isEmpty(Context context) {
		checkContextNotNull(context);
		return ObjectUtils.isEmpty(context.getPropertyNames());
	}

	public boolean containsKey(Context context, Object key) {
		checkContextNotNull(context);
		return ContainerUtils.contains(context.getPropertyNames(), key);
	}

	public boolean containsValue(Context context, Object value) {
		checkContextNotNull(context);
		String[] propertyNames = context.getPropertyNames();
		if (ObjectUtils.isEmpty(propertyNames)) {
			return false;
		}
		else {
			for (int i=0; i<propertyNames.length; i++) {
				if (ObjectUtils.equals(context.get(propertyNames[i]), value)) {
					return true;
				}
			}
			return false;
		}
	}

	public Collection values(Context context) {
		checkContextNotNull(context);
		List values = new ArrayList();
		String[] propertyNames = context.getPropertyNames();
		for (int i=0; i<propertyNames.length; i++) {
			values.add(context.get(propertyNames[i]));
		}
		return values;
	}

	public void putAll(Context context, Map t) {
		checkContextNotNull(context);
		if (t != null && !t.isEmpty()) {
			Iterator iterator = t.keySet().iterator();
			while (iterator.hasNext()) {
				Object key = iterator.next();
				put(context, key, t.get(key));
			}
		}		
	}

	public Set entrySet(Context context) {
		checkContextNotNull(context);
		String[] propertyNames = context.getPropertyNames();
		if (propertyNames == null) {
			return new HashSet();
		}
		else {
			Set set = new HashSet();
			for (int i=0; i<propertyNames.length; i++) {
				String propertyName = propertyNames[i];
				MapEntry entry = new MapEntry(propertyName,
					context.get(propertyName), false); 
				set.add(entry);
			}
			return set;
		}
	}

	public Set keySet(Context context) {
		checkContextNotNull(context);
		return new HashSet(Arrays.asList(context.getPropertyNames()));
	}

	public Object get(Context context, Object key) {
		checkContextNotNull(context);
		return context.get((String) key);
	}

	public Object remove(Context context, Object key) {
		checkContextNotNull(context);
		if (context instanceof DelegatingContext &&
			getDelegate(context) instanceof Map) {
			return ((Map) getDelegate(context)).remove(key);
		}
		else {
			throw new UnsupportedOperationException();
		}
	}

	public Object put(Context context, Object key, Object value) {
		checkContextNotNull(context);
		if (!(key instanceof String)) {
			throw new IllegalArgumentException("Only string keys can be used");
		}
		Object originalValue = context.get((String) key);
		context.set((String) key, value);
		return originalValue;
	}
	
	protected void checkContextNotNull(Context context)
		throws IllegalArgumentException {
		if (context == null) {
			throw new IllegalArgumentException("context cannot be null");
		}
	}

}
