/*
 * Copyright 2008 the original author or authors.
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
package net.sf.morph.transform.copiers;

import java.util.Map;

import net.sf.morph.reflect.reflectors.MapReflector;

/**
 * Copies one Map to another.  A PropertyNameMatchingCopier can do this as well,
 * but only for maps whose keys are Strings!
 *
 * @author mbenson
 * @since Morph 1.1
 */
public class MapCopier extends ContainerCopier {
	private static final Class[] SOURCE_AND_DESTINATION_TYPES = { Map.class };

	/**
	 * {@inheritDoc}
	 */
	protected void initializeImpl() throws Exception {
		super.initializeImpl();
		setReflector(new MapReflector(MapReflector.EXTRACT_ENTRIES));
	}

	/**
	 * {@inheritDoc}
	 */
	protected Class[] getSourceClassesImpl() throws Exception {
		return SOURCE_AND_DESTINATION_TYPES;
	}

	/**
	 * {@inheritDoc}
	 */
	protected Class[] getDestinationClassesImpl() throws Exception {
		return SOURCE_AND_DESTINATION_TYPES;
	}

}
