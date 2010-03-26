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
package net.sf.morph2.context.contexts;

import java.util.HashMap;
import java.util.Map;

import net.sf.morph2.context.Context;

/**
 * A simple Context backed by a Map.
 * 
 * @author Matt Sgarlata
 * @since Nov 21, 2004
 */
public class MapContext extends ReflectorHierarchicalContext {

	/**
	 * Creates a new, empty context.
	 */
	public MapContext() {
		super();
		super.setDelegate(new HashMap());
	}

	/**
	 * Creates a new context with the given context as parent.
	 * 
	 * @param parentContext
	 *            the parent context
	 */
	public MapContext(Context parentContext) {
		super(parentContext);
		super.setDelegate(new HashMap());
	}

	/**
	 * Creates a new context with the values taken from the supplied Map. Only
	 * the map entries that have String keys will be exposed as properties.
	 * 
	 * @param map
	 *            the map used to populate this context with initial values
	 */
	public MapContext(Map map) {
		super();
		super.setDelegate(map);
	}

	/**
	 * Creates a new context with the given parent context and initial values
	 * taken from the supplied Map. Only the map entries that have String keys
	 * will be exposed as properties.
	 * 
	 * @param parentContext
	 *            the parent context
	 * @param map
	 *            the map used to populate this context with initial values
	 */
	public MapContext(Context parentContext, Map map) {
		super(parentContext);
		super.setDelegate(map);
	}

}
