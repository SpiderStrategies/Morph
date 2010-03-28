/*
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
package net.sf.morph2.transform.converters;

import net.sf.morph2.Defaults;
import net.sf.morph2.transform.Converter;
import net.sf.morph2.transform.Transformer;
import net.sf.morph2.transform.transformers.SimpleDelegatingTransformer;

/**
 * Converts an object to a textual representation by delegating to
 * {@link net.sf.morph2.transform.converters.ContainerToPrettyTextConverter},
 * {@link net.sf.morph2.transform.converters.BooleanToTextConverter}, 
 * {@link net.sf.morph2.transform.converters.TimeToTextConverter},
 * {@link net.sf.morph2.transform.converters.NumberToTextConverter} and
 * {@link net.sf.morph2.transform.converters.ObjectToTextConverter}.
 * 
 * @author Matt Sgarlata
 * @since Jan 1, 2005
 */
public class DefaultToTextConverter extends SimpleDelegatingTransformer {

	/**
	 * Used to specify the destination classes of this converter, and to
	 * determine if a transformation is possible.
	 * 
	 * @see {@link DefaultToTextConverter#isTransformableImpl(Class, Class)}
	 * @see {@link DefaultToTextConverter#getDestinationClassesImpl()}
	 */
	private Converter textConverter;

	/**
	 * Create a new DefaultToTextConverter.
	 */
	public DefaultToTextConverter() {
	    super();
    }

	/**
	 * Create a new DefaultToTextConverter.
	 * @param components
	 * @param appendDefaultComponents
	 */
	public DefaultToTextConverter(Transformer[] components, boolean appendDefaultComponents) {
	    super(components, appendDefaultComponents);
    }

	/**
	 * Create a new DefaultToTextConverter.
	 * @param components
	 */
	public DefaultToTextConverter(Transformer[] components) {
	    super(components);
    }

	/**
	 * {@inheritDoc}
	 */
	protected Transformer[] createDefaultComponents() {
		return new Transformer[] { new ContainerToPrettyTextConverter(),
		        new BooleanToTextConverter(), new TimeToTextConverter(),
		        new NumberToTextConverter(), new ObjectToTextConverter() };
	}

	/**
	 * Unused
	 * @return
	 * @deprecated since Morph 1.1
	 */
	public Converter getTextConverter() {
		if (textConverter == null) {
			setTextConverter(Defaults.createTextConverter());
		}
		return textConverter;
	}

	/**
	 * Unused
	 * @param textConverter
	 * @deprecated since Morph 1.1
	 */
	public void setTextConverter(Converter textConverter) {
		this.textConverter = textConverter;
	}
}
