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
package net.sf.morph.transform.converters;

import java.util.Locale;

import net.sf.morph.transform.Converter;
import net.sf.morph.transform.DecoratedConverter;
import net.sf.morph.transform.TransformationException;
import net.sf.morph.transform.transformers.BaseTransformer;

/**
 * Converts <code>null</code> to <code>null</code>.
 * 
 * @author Matt Sgarlata
 * @since Dec 31, 2004
 */
public class NullConverter extends BaseTransformer implements Converter, DecoratedConverter {

	private static final Class[] SOURCE_TYPES = { null };
	
	private static final Class[] DESTINATION_TYPES = { null, Object.class };

	protected Object convertImpl(Class destinationClass, Object source,
		Locale locale) throws Exception {
		
		if (source == null) {
			return null;
		}
		
		throw new TransformationException(destinationClass, source);

	}

	protected boolean isPerformingLogging() {
		// this transformation is trivial; don't clutter the log with it
		return false;
	}

	protected Class[] getSourceClassesImpl() throws Exception {
		return SOURCE_TYPES;
	}

	protected Class[] getDestinationClassesImpl() throws Exception {
		return DESTINATION_TYPES;
	}

}
