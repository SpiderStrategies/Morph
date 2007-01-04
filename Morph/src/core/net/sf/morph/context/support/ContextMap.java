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

import java.util.Collection;
import java.util.Map;
import java.util.Set;

import net.sf.morph.context.Context;

/**
 * Exposes any context as a Map.
 * 
 * @author Matt Sgarlata
 * @since Nov 22, 2004
 */
public class ContextMap implements Map {

	private ContextMapBridge contextMapBridge = new ContextMapBridge();
	
	private Context context;
	
	public ContextMap(Context context) {
		this.context = context;
	}
	
	public void clear() {
		contextMapBridge.clear(context);
	}
	public boolean containsKey(Object key) {
		return contextMapBridge.containsKey(context, key);
	}
	public boolean containsValue(Object value) {
		return contextMapBridge.containsValue(context, value);
	}
	public Set entrySet() {
		return contextMapBridge.entrySet(context);
	}
	public boolean equals(Object obj) {
		return contextMapBridge.equals(obj);
	}
	public Object get(Object key) {
		return contextMapBridge.get(context, key);
	}
	public int hashCode() {
		return contextMapBridge.hashCode();
	}
	public boolean isEmpty() {
		return contextMapBridge.isEmpty(context);
	}
	public Set keySet() {
		return contextMapBridge.keySet(context);
	}
	public Object put(Object key, Object value) {
		return contextMapBridge.put(context, key, value);
	}
	public void putAll(Map t) {
		contextMapBridge.putAll(context, t);
	}
	public Object remove(Object key) {
		return contextMapBridge.remove(context, key);
	}
	public int size() {
		return contextMapBridge.size(context);
	}
	public String toString() {
		return contextMapBridge.toString();
	}
	public Collection values() {
		return contextMapBridge.values(context);
	}

	public Context getContext() {
		return context;
	}
	public void setContext(Context context) {
		this.context = context;
	}
	public ContextMapBridge getContextMapBridge() {
		return contextMapBridge;
	}
	public void setContextMapBridge(ContextMapBridge contextMapBridge) {
		this.contextMapBridge = contextMapBridge;
	}
	
}
