/*
 * Copyright 2004-2005, 2007-2008 the original author or authors.
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
package net.sf.morph2.transform.converters;

import net.sf.morph2.util.ClassUtils;

/**
 * An identity converter that only converts immutable types.
 * 
 * @author Matt Sgarlata
 * @since Dec 1, 2005
 */
public class ImmutableTypesOnlyIdentityConverter extends IdentityConverter {

	private static final Class[] IMMUTABLE_TYPES = ClassUtils.getImmutableTypes();

	/**
	 * {@inheritDoc}
	 */
	protected Class[] getSourceClassesImpl() throws Exception {
		return IMMUTABLE_TYPES;
	}

	/**
	 * {@inheritDoc}
	 */
	protected Class[] getDestinationClassesImpl() throws Exception {
		return IMMUTABLE_TYPES;
	}

}
