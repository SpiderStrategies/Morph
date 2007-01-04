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

import net.sf.composite.util.ObjectUtils;
import net.sf.morph.transform.Converter;
import net.sf.morph.transform.DecoratedConverter;
import net.sf.morph.transform.TransformationException;
import net.sf.morph.transform.transformers.BaseCompositeTransformer;

/**
 * Runs one or more converters in a chain.
 * 
 * @author Matt Sgarlata
 * @since Nov 24, 2004
 */
public class ChainedConverter extends BaseCompositeTransformer implements Converter, DecoratedConverter {
	
	private Class[] compiledDestinations;
	
	protected void initializeImpl() throws TransformationException {
		Converter[] chain = getChain();

		Class[] destClasses = new Class[chain.length - 1];
		
		// loop through each converter in the chain, skipping the last one
		for (int i=0; i<chain.length - 1; i++) {

			Class[] sources = chain[i+1].getSourceClasses();
			Class[] destinations = chain[i].getDestinationClasses(); 
			
			for (int j=0; j<sources.length && destClasses[i] == null; j++) {
				for (int k=0; k<destinations.length && destClasses[i] == null; k++) {
					if (sources[j].isAssignableFrom(destinations[k])) {
						destClasses[i] = destinations[k]; 
						break;
					}
				}
			}
			
			logIntermediateDestination(chain,
				ObjectUtils.getObjectDescription(destClasses[i]), i + 1);

			if (destClasses[i] == null) {
				throw new TransformationException(
					"Conversion path could not be determined; Could not find a destination from converter "
						+ ObjectUtils.getObjectDescription(chain[i])
						+ " that could be a source for converter "
						+ ObjectUtils.getObjectDescription(chain[i + 1]));
			}
		}
		compiledDestinations = destClasses;
		
		logIntermediateDestination(chain,
			"the final destination class as provided to the convert method",
			chain.length);
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
		
		Object destination;
		for (int i=0; i<chain.length-1; i++) {
			destination = chain[i].convert(compiledDestinations[i], source, locale);
			logConversion(i + 1, chain, source, destination);
			source = destination;
		}
		destination = chain[chain.length-1].convert(destinationClass, source, locale);
		logConversion(chain.length, chain, source, destination);
		return destination;
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

	public Converter[] getChain() {
		return (Converter[]) getComponents();
	}
	public void setChain(Converter[] chain) throws TransformationException {
		setComponents(chain);
	}
}
