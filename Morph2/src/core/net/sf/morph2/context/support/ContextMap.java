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
package net.sf.morph2.context.support;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

import net.sf.morph2.context.Context;

/**
 * Exposes any context as a Map.
 * 
 * @author Matt Sgarlata
 * @since Nov 22, 2004
 */
public class ContextMap implements Map {

	private ContextMapBridge contextMapBridge = new ContextMapBridge();

	private Context context;

	/**
	 * Create a new ContextMap instance.
	 * @param context
	 */
	public ContextMap(Context context) {
		this.context = context;
	}

	/**
	 * {@inheritDoc}
	 */
	public void clear() {
		contextMapBridge.clear(context);
	}

	/**
	 * {@inheritDoc}
	 */
	public boolean containsKey(Object key) {
		return contextMapBridge.containsKey(context, key);
	}

	/**
	 * {@inheritDoc}
	 */
	public boolean containsValue(Object value) {
		return contextMapBridge.containsValue(context, value);
	}

	/**
	 * {@inheritDoc}
	 */
	public Set entrySet() {
		return contextMapBridge.entrySet(context);
	}

	/**
	 * {@inheritDoc}
	 */
	public boolean equals(Object obj) {
		return contextMapBridge.equals(obj);
	}

	/**
	 * {@inheritDoc}
	 */
	public Object get(Object key) {
		return contextMapBridge.get(context, key);
	}

	/**
	 * {@inheritDoc}
	 */
	public int hashCode() {
		return contextMapBridge.hashCode();
	}

	/**
	 * {@inheritDoc}
	 */
	public boolean isEmpty() {
		return contextMapBridge.isEmpty(context);
	}

	/**
	 * {@inheritDoc}
	 */
	public Set keySet() {
		return contextMapBridge.keySet(context);
	}

	/**
	 * {@inheritDoc}
	 */
	public Object put(Object key, Object value) {
		return contextMapBridge.put(context, key, value);
	}

	/**
	 * {@inheritDoc}
	 */
	public void putAll(Map t) {
		contextMapBridge.putAll(context, t);
	}

	/**
	 * {@inheritDoc}
	 */
	public Object remove(Object key) {
		return contextMapBridge.remove(context, key);
	}

	/**
	 * {@inheritDoc}
	 */
	public int size() {
		return contextMapBridge.size(context);
	}

	/**
	 * {@inheritDoc}
	 */
	public String toString() {
		return contextMapBridge.toString();
	}

	/**
	 * {@inheritDoc}
	 */
	public Collection values() {
		return contextMapBridge.values(context);
	}

	/**
	 * Get the contextMapBridge of this ContextMap.
	 * @return the contextMapBridge
	 */
	public ContextMapBridge getContextMapBridge() {
		return contextMapBridge;
	}

	/**
	 * Set the contextMapBridge of this ContextMap.
	 * @param contextMapBridge the ContextMapBridge to set
	 */
	public void setContextMapBridge(ContextMapBridge contextMapBridge) {
		this.contextMapBridge = contextMapBridge;
	}

	/**
	 * Get the context of this ContextMap.
	 * @return the context
	 */
	public Context getContext() {
		return context;
	}

	/**
	 * Set the context of this ContextMap.
	 * @param context the Context to set
	 */
	public void setContext(Context context) {
		this.context = context;
	}


}
