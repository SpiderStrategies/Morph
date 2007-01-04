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
package net.sf.morph.examples.person;

import java.util.Locale;

import net.sf.morph.transform.Converter;
import net.sf.morph.transform.DecoratedConverter;
import net.sf.morph.transform.transformers.BaseTransformer;


/**
 * @author Matt Sgarlata
 * @since Feb 12, 2005
 */
public class AddressDAOArrayToStringConverter extends BaseTransformer implements Converter, DecoratedConverter {

	protected Object convertImpl(Class destinationClass, Object source,
		Locale locale) throws Exception {
		
		// the BaseConverter will make sure the source is of the correct type
		// for us, so we can just do a cast here with no error checking
		AddressDAO[] addresses = (AddressDAO[]) source;
		// we can also assume the source is not null, because we didn't
		// explicitly state that null was a valid source class
		AddressDAO address = addresses[0];
		// now we convert the first address to a String
		return address.toString();
		
	}

	protected Class[] getSourceClassesImpl() throws Exception {
		// if we wanted this converter to also handle converting null values
		// to Strings, we could write this line as:
		//
		//      return new Class[] { AddressDAO[].class, null };
		return new Class[] { AddressDAO[].class };
	}

	protected Class[] getDestinationClassesImpl() throws Exception {
		return new Class[] { String.class };
	}

}
