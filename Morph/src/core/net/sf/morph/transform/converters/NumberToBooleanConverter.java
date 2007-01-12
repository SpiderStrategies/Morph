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

import java.math.BigDecimal;
import java.util.Locale;

import net.sf.morph.transform.Converter;
import net.sf.morph.transform.DecoratedConverter;
import net.sf.morph.transform.transformers.BaseTransformer;

/**
 * Converts numbers to boolean values. Numbers equal to zero are converted to
 * <code>false</code> and non-zero numbers will be converted to
 * <code>true</code>.
 * 
 * @author Matt Sgarlata
 * @since Dec 31, 2004
 */
public class NumberToBooleanConverter extends BaseTransformer implements Converter, DecoratedConverter {

	private static final Class[] DESTINATION_TYPES = { Boolean.class,
		boolean.class };
	
	private static final Class[] SOURCE_TYPES = { Number.class };
	
	protected Object convertImpl(Class destinationClass, Object source,
		Locale locale) throws Exception {
		
		BigDecimal bigDecimal = new BigDecimal(source.toString());
		// the .equals method doesn't work correctly for some reason, so use compareTo
		return bigDecimal.compareTo(new BigDecimal(0)) == 0 ? Boolean.FALSE : Boolean.TRUE;
	}

	protected Class[] getSourceClassesImpl() throws Exception {
		return SOURCE_TYPES;
	}

	protected Class[] getDestinationClassesImpl() throws Exception {
		return DESTINATION_TYPES;
	}

}
