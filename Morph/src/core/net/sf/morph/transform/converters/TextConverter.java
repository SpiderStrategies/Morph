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
 * Converts the basic text types, {@link java.lang.String},
 * {@link java.lang.StringBuffer} and {@link java.lang.Character}, from one
 * time type to another. Empty Strings and StringBuffers with lengths of zero
 * are converted to <code>null</code> Characters, and non-empty Strings and
 * StringBuffers are converted to Characters by returning the first character in
 * the String or StringBuffer.
 * 
 * @author Matt Sgarlata
 * @since Jan 2, 2005
 */
public class TextConverter extends BaseTransformer implements Converter, DecoratedConverter {
	
	private static final Class[] SOURCE_AND_DESTINATION_TYPES = {
		StringBuffer.class, String.class, Character.class, char.class, null
	};

	protected Object convertImpl(Class destinationClass, Object source,
		Locale locale) throws Exception {

		String string = source.toString();
		
		if (destinationClass.equals(String.class)) {
			return string;
		}
		else if (destinationClass.equals(StringBuffer.class)) {
			return new StringBuffer(string);
		}
		else if (destinationClass.equals(Character.class)) {
			if (string.length() == 0) {
				return null;
			}
			else {
				return new Character(string.charAt(0));	
			}			
		}

		throw new TransformationException(destinationClass, source);
	}

	protected Class[] getSourceClassesImpl() throws Exception {
		return SOURCE_AND_DESTINATION_TYPES;
	}

	protected Class[] getDestinationClassesImpl() throws Exception {
		return SOURCE_AND_DESTINATION_TYPES;
	}

}
