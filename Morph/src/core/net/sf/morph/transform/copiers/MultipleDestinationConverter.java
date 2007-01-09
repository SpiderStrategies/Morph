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
package net.sf.morph.transform.copiers;

//import java.util.ArrayList;
//import java.util.List;
import java.util.Locale;

import net.sf.morph.Defaults;
import net.sf.morph.transform.Converter;
import net.sf.morph.transform.DecoratedConverter;
import net.sf.morph.transform.Transformer;
import net.sf.morph.transform.transformers.BaseCompositeTransformer;

/**
 * A converter which takes a single object and converts it into multiple
 * objects.
 * 
 * @author Matt Sgarlata
 * @since Apr 18, 2005
 */
public class MultipleDestinationConverter extends BaseCompositeTransformer implements DecoratedConverter {
	
	private Converter containerConverter;
	private Class[] destinationClassesForEachDestination;

	protected Object convertImpl(Class destinationClass, Object source,
		Locale locale) throws Exception {

//		Object[] components = getComponents();
//		Class[] destTypes = getDestinationClassesForEachDestination();
//		List destinationObjects = new ArrayList(components.length);
//		for (int i=0; i<components.length; i++) {
//			Converter converter = (Converter) components[i];
//			Class destType = destTypes[i] == null ? Object.class : destTypes[i];
//			Object converted = converter.convert(destType, source, locale);
//			destinationObjects.add(converted);
//		}
		//huh? what did we just do?
		return getContainerConverter().convert(destinationClass, source, locale);
	}

	public Class getComponentType() {
		return Transformer.class;
	}	
	
	protected Class[] getSourceClassesImpl() throws Exception {
		// FIXME should return those classes which can be accepted by all
		// transformers
		
		return new Class[] { Object.class };
	}

	protected Class[] getDestinationClassesImpl() throws Exception {
		return getContainerConverter().getDestinationClasses();
	}

	public Converter getContainerConverter() {
		if (containerConverter == null) {
			containerConverter = Defaults.createContainerCopier();
		}
		return containerConverter;
	}
	public void setContainerConverter(Converter containerTransformer) {
		this.containerConverter = containerTransformer;
	}
	public Class[] getDestinationClassesForEachDestination() {
		return destinationClassesForEachDestination;
	}
	public void setDestinationClassesForEachDestination(Class[] destinations) {
		this.destinationClassesForEachDestination = destinations;
	}
}
