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

import net.sf.morph.Defaults;
import net.sf.morph.transform.Converter;
import net.sf.morph.transform.DecoratedConverter;
import net.sf.morph.transform.Transformer;
import net.sf.morph.transform.transformers.SimpleDelegatingTransformer;
import net.sf.morph.util.ContainerUtils;

/**
 * Converts an object to a textual representation by delegating to
 * {@link net.sf.morph.transform.converters.ContainerToPrettyTextConverter},
 * {@link net.sf.morph.transform.converters.TimeToTextConverter},
 * {@link net.sf.morph.transform.converters.NumberToTextConverter} and
 * {@link net.sf.morph.transform.converters.ObjectToTextConverter}.
 * 
 * @author Matt Sgarlata
 * @since Jan 1, 2005
 */
public class DefaultToTextConverter extends SimpleDelegatingTransformer implements Converter, DecoratedConverter {
	
	private static final Class[] SOURCE_TYPES = { Object.class };

	/**
	 * Used to specify the destination classes of this converter, and to
	 * determine if a transformation is possible.
	 * 
	 * @see {@link DefaultToTextConverter#isTransformableImpl(Class, Class)}
	 * @see {@link DefaultToTextConverter#getDestinationClassesImpl()}
	 */
	private Converter textConverter;
	
	/**
	 * Returns <code>true</code> if the destination class is one of the
	 * destination classes allowed by the
	 * {@link DefaultToTextConverter#textConverter}.
	 */
	protected boolean isTransformableImpl(Class destinationType,
		Class sourceType) throws Exception {
		return ContainerUtils.contains(
			getTextConverter().getDestinationClasses(), destinationType);
	}
	
	/**
	 * Returns an array containing the Object class, since all sources can be
	 * converted by this converter.
	 */
	protected Class[] getSourceClassesImpl() throws Exception {
		return SOURCE_TYPES;
	}
	
	/**
	 * Returns the destination classes allowed by the
	 * {@link DefaultToTextConverter#textConverter}.
	 */
	protected Class[] getDestinationClassesImpl() throws Exception {
		return getTextConverter().getDestinationClasses();
	}

	public Object[] getComponents() {
		if (components == null) {
			setComponents(new Transformer[] {
				new BooleanToTextConverter(),
				new TimeToTextConverter(),
				new NumberToTextConverter(),
				new ObjectToTextConverter()
			});
		}
		return super.getComponents();
	}
	
	public Converter getTextConverter() {
		if (textConverter == null) {
			setTextConverter(Defaults.createTextConverter());
		}
		return textConverter;
	}
	public void setTextConverter(Converter textConverter) {
		this.textConverter = textConverter;
	}
}