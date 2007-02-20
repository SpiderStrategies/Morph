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

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import net.sf.composite.util.ObjectUtils;
import net.sf.morph.transform.Converter;
import net.sf.morph.transform.DecoratedConverter;
import net.sf.morph.transform.TransformationException;
import net.sf.morph.transform.transformers.BaseCompositeTransformer;
import net.sf.morph.util.ClassUtils;
import net.sf.morph.util.TransformerUtils;

/**
 * Runs one or more converters in a chain.
 * 
 * @author Matt Sgarlata
 * @since Nov 24, 2004
 */
public class ChainedConverter extends BaseCompositeTransformer implements Converter, DecoratedConverter {
	
	protected boolean isTransformableImpl(Class destinationType, Class sourceType) throws Exception {
		return getConversionPath(destinationType, sourceType, getChain()) != null;
	}

	protected void logIntermediateDestination(Converter[] chain, String destination, int conversionNumber) {
		if (log.isInfoEnabled()) {
			log.info("Conversion " + conversionNumber + " of " + chain.length
				+ " using "
				+ ObjectUtils.getObjectDescription(chain[conversionNumber - 1])
				+ " will be to " + destination);
		}
	}

	protected Object convertImpl(Class destinationClass, Object source,
		Locale locale) throws Exception {
		Converter[] chain = getChain();
		
		if (log.isTraceEnabled()) {
			log.trace("Using chain to convert "
				+ ObjectUtils.getObjectDescription(source) + " to "
				+ ObjectUtils.getObjectDescription(destinationClass));
		}
		
		Class sourceType = ClassUtils.getClass(source);
		List conversionPath = getConversionPath(destinationClass, sourceType, chain);
		if (conversionPath == null) {
			throw new TransformationException(destinationClass, sourceType, null,
					"Chained conversion path could not be determined");
		}
		Object o = source;
		for (int i = 0; i < conversionPath.size(); i++) {
			o = chain[i].convert((Class) conversionPath.get(i), o, locale);
			logConversion(i + 1, chain, source, o);
		}
		return o;
	}

	protected void logConversion(int conversionNumber, Converter[] chain, Object source, Object destination) {
		if (log.isTraceEnabled()) {
			log.trace("Conversion "
				+ conversionNumber
				+ " of "
				+ chain.length
				+ " was from "
				+ ObjectUtils.getObjectDescription(source)
				+ " to "
				+ ObjectUtils.getObjectDescription(destination)
				+ " and was performed by "
				+ ObjectUtils.getObjectDescription(chain[conversionNumber - 1]));
		}
	}

	protected String getComponentsPropertyName() {
		return "chain";
	}

	protected Class[] getDestinationClassesImpl() throws Exception {
		return getChain()[getChain().length-1].getDestinationClasses();
	}

	protected Class[] getSourceClassesImpl() throws Exception {
		return getChain()[0].getSourceClasses();
	}

	protected List getConversionPath(Class destinationType, Class sourceType, Converter[] chain) {
		return getConversionPath(destinationType, sourceType, chain, 0);
	}

	/**
	 * Get a conversion path by investigating possibilities recursively.
	 * @param destinationType
	 * @param sourceType
	 * @param chain
	 * @param index
	 * @return
	 */
	private List getConversionPath(Class destinationType, Class sourceType, Converter[] chain, int index) {
		Converter c = chain[index];
		if (index + 1 == chain.length) {
			if (TransformerUtils.isTransformable(c, destinationType, sourceType)) {
				ArrayList result = new ArrayList();
				result.add(destinationType);
				return result;
			}
			return null;
		}
		Class[] available = c.getDestinationClasses();
		for (int i = 0; i < available.length; i++) {
			if (TransformerUtils.isTransformable(c, available[i], sourceType)) {
				List tail = getConversionPath(destinationType, available[i], chain, ++index);
				if (tail != null) {
					tail.add(0, available[i]);
				}
				return tail;
			}
		}
		return null;
	}

	public Converter[] getChain() {
		return (Converter[]) getComponents();
	}

	public void setChain(Converter[] chain) throws TransformationException {
		setComponents(chain);
	}
}
