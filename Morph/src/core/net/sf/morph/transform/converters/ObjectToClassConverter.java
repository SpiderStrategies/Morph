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

import net.sf.morph.transform.DecoratedConverter;
import net.sf.morph.transform.transformers.BaseTransformer;

/**
 * Converts an object into that object's class.
 * 
 * @author Matt Sgarlata
 * @since Feb 10, 2006
 */
public class ObjectToClassConverter extends BaseTransformer implements
		DecoratedConverter {
	
	protected Object convertImpl(Class destinationClass, Object source, Locale locale) throws Exception {
		if (source == null) {
			return null;
		}
		else {
			return source.getClass();
		}
	}

	protected Class[] getSourceClassesImpl() throws Exception {
		return new Class[] { Object.class, float.class, double.class,
				byte.class, short.class, int.class, long.class };
	}

	protected Class[] getDestinationClassesImpl() throws Exception {
		return new Class[] { Class.class } ;
	}

}
