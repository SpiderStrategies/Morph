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
import net.sf.morph2.transform.DecoratedConverter;
import net.sf.morph2.transform.ImpreciseTransformer;
import net.sf.morph2.transform.transformers.BaseReflectorTransformer;
import net.sf.morph2.util.TransformerUtils;

/**
 * Base class for converts that convert objects to a pretty programmer-friendly
 * representation using information retrieved using a reflector.
 * 
 * @author Matt Sgarlata
 * @since Feb 15, 2005
 */
public abstract class BaseToPrettyTextConverter extends BaseReflectorTransformer
		implements DecoratedConverter, ImpreciseTransformer {

	private String prefix;
	private String suffix;
	private String separator;
	private Converter textConverter;
	private Converter toTextConverter;
	private boolean showNullValues = false;

	/**
	 * {@inheritDoc}
	 */
	protected Class[] getDestinationClassesImpl() throws Exception {
		return getTextConverter().getDestinationClasses();
	}

	/**
	 * {@inheritDoc}
	 */
	// don't do any logging, because it will cause an infinite loop.  you can't
	// log an object's string representation as one of the steps of constructing
	// that representation
	protected boolean isPerformingLogging() {
		return false;
	}

	/**
	 * {@inheritDoc}
	 */
	protected boolean isWrappingRuntimeExceptions() {
	    return true;
    }

	/**
	 * Get the separator.
	 * @return String
	 */
	public String getSeparator() {
		return separator;
	}

	/**
	 * Set the separator.
	 * @param separator
	 */
	public void setSeparator(String separator) {
		this.separator = separator;
	}

	/**
	 * Get the prefix.
	 * @return String
	 */
	public String getPrefix() {
		return prefix;
	}

	/**
	 * Set the prefix.
	 * @param prefix
	 */
	public void setPrefix(String prefix) {
		this.prefix = prefix;
	}

	/**
	 * Get the suffix.
	 * @return String
	 */
	public String getSuffix() {
		return suffix;
	}

	/**
	 * Set the suffix.
	 * @param suffix
	 */
	public void setSuffix(String suffix) {
		this.suffix = suffix;
	}

	/**
	 * Get the text converter used by this BaseToPrettyTextConverter.
	 * @return Converter
	 */
	public Converter getTextConverter() {
		if (textConverter == null) {
			setTextConverter(Defaults.createTextConverter());
		}
		return textConverter;
	}

	/**
	 * Set the text converter used by this BaseToPrettyTextConverter.
	 * @param textConverter
	 */
	public void setTextConverter(Converter textConverter) {
		this.textConverter = textConverter;
	}

	/**
	 * Get the "to text" converter used by this BaseToPrettyTextConverter.
	 * @return Converter
	 */
	public Converter getToTextConverter() {
		if (toTextConverter == null) {
			setToTextConverter(Defaults.createToTextConverter());
		}
		return toTextConverter;
	}

	/**
	 * Set the "to text" converter used by this BaseToPrettyTextConverter.
	 * @param objectToTextConverter
	 */
	public void setToTextConverter(Converter objectToTextConverter) {
		this.toTextConverter = objectToTextConverter;
	}

	/**
	 * Learn whether this BaseToPrettyTextConverter is configured to show null values.
	 * @return boolean
	 */
	public boolean isShowNullValues() {
		return showNullValues;
	}

	/**
	 * Set whether this BaseToPrettyTextConverter should show null values. Default <code>false</code>.
	 * @param showNullValues
	 */
	public void setShowNullValues(boolean showNullValues) {
		this.showNullValues = showNullValues;
	}

	/**
	 * {@inheritDoc}
	 */
	protected boolean isImpreciseTransformationImpl(Class destinationClass, Class sourceClass) {
		return TransformerUtils.isImpreciseTransformation(getTextConverter(), destinationClass, sourceClass);
	}

	/**
	 * Get the intermediate class passed to the text converter.
	 * @return
	 */
	protected Class getIntermediateClass() {
		return StringBuffer.class;
	}
}
