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
package net.sf.morph2.integration.jdom;


import java.util.Locale;

import org.jdom.input.DOMBuilder;

import net.sf.morph2.transform.Converter;
import net.sf.morph2.transform.DecoratedConverter;
import net.sf.morph2.transform.transformers.BaseTransformer;

/**
 * Converts DOM {@link org.w3c.dom.Document} objects into JDom
 * {@link org.jdom.Document} objects.
 * 
 * @author Matt Sgarlata
 * @since Jan 17, 2008
 */
public class DomToJDomConverter extends BaseTransformer implements Converter, DecoratedConverter {
	
	public static final Class[] SOURCE_CLASSES = new Class[] { org.w3c.dom.Document.class };
	public static final Class[] DESTINATION_CLASSES = new Class[] { org.jdom.Document.class };
	
	protected Object convertImpl(Class destinationClass, Object source, Locale locale) throws Exception {
		org.w3c.dom.Document domDoc = (org.w3c.dom.Document) source;
		DOMBuilder builder = new DOMBuilder();
        org.jdom.Document jdomDoc = builder.build(domDoc);
        return jdomDoc;
    }

	protected Class[] getDestinationClassesImpl() throws Exception {
		return DESTINATION_CLASSES;
	}

	protected Class[] getSourceClassesImpl() throws Exception {
		return SOURCE_CLASSES;
	}

}
