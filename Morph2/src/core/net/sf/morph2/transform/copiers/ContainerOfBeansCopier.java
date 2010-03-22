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
package net.sf.morph2.transform.copiers;

import java.util.Locale;

import net.sf.morph2.reflect.IndexedContainerReflector;
import net.sf.morph2.transform.Converter;
import net.sf.morph2.transform.Copier;
import net.sf.morph2.transform.Transformer;
import net.sf.morph2.util.ReflectorUtils;

/**
 * FIXME this class isn't done
 * 
 * @author Matt Sgarlata
 * @since Dec 20, 2004
 * @deprecated it's not clear what the intent of this copier was
 */
public class ContainerOfBeansCopier extends ContainerCopier {

//	private Transformer beanTransformer;
	private Class destinationBeanClass;
	
	protected void put(int index, Object destination, Object value, Class valueClass, Locale locale, Integer preferredTransformationType) {
		Object transformed = null;
		// try to do the transformation using a copy operation
		if (getBeanTransformer() instanceof Copier &&
			ReflectorUtils.isReflectable(getReflector(),
				destination.getClass(), IndexedContainerReflector.class)) {

			if (((IndexedContainerReflector) getReflector()).getSize(destination) > index) {
				transformed = ((IndexedContainerReflector) getReflector()).get(destination, index);
				if (transformed != null &&
					getDestinationBeanClass().isAssignableFrom(transformed.getClass())) {
					((Copier) getBeanTransformer()).copy(transformed, value, locale);				
				}
			}			
		}
		// if a copy isn't possible, do a conversion
		if (transformed == null) {
			transformed = ((Converter) getBeanTransformer()).convert(getDestinationBeanClass(), value, locale);
		}
		super.put(index, destination, transformed, valueClass, locale, preferredTransformationType);
	}
	
	protected Transformer getBeanTransformer() {
//		return (Transformer) CompositeUtils.specialize(getNestedTransformer(), Transformer.class);
		return getNestedTransformer();
	}
	
//	public Transformer getBeanTransformer() {
//		if (beanTransformer == null) {
//			setBeanTransformer(Defaults.getConverter);
//		}
//		return beanTransformer;
//	}
//	public void setBeanTransformer(Transformer beanTransformer) throws TransformationException {
//		if (!(beanTransformer instanceof Converter || beanTransformer instanceof Copier)) {
//			throw new TransformationException(
//				"The beanTransformer must implement either "
//					+ ObjectUtils.getObjectDescription(Converter.class) + " or "
//					+ ObjectUtils.getObjectDescription(Copier.class));
//		}
//		this.beanTransformer = beanTransformer;
//	}
	
	public Class getDestinationBeanClass() {
		return destinationBeanClass;
	}
	public void setDestinationBeanClass(Class destinationBeanClass) {
		this.destinationBeanClass = destinationBeanClass;
	}
}
