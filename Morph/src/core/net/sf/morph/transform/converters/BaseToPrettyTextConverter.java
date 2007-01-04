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
import net.sf.morph.transform.transformers.BaseReflectorTransformer;

/**
 * Base class for converts that convert objects to a pretty programmer-friendly
 * representation using information retrieved using a reflector.
 * 
 * @author Matt Sgarlata
 * @since Feb 15, 2005
 */
public abstract class BaseToPrettyTextConverter extends BaseReflectorTransformer implements Converter, DecoratedConverter {

	private String prefix;
	private String suffix;
	private String separator;
	private Converter textConverter;
	private Converter toTextConverter;
	private boolean showNullValues = false;
	
	protected Class[] getDestinationClassesImpl() throws Exception {
		return getTextConverter().getDestinationClasses();
	}
	
	// don't do any logging, because it will cause an infinite loop.  you can't
	// log an object's string representation as one of the steps of constructing
	// that representation
	protected boolean isPerformingLogging() {
		return false;
	}
	
	public String getSeparator() {
		return separator;
	}
	public void setSeparator(String separator) {
		this.separator = separator;
	}
	public String getPrefix() {
		return prefix;
	}
	public void setPrefix(String prefix) {
		this.prefix = prefix;
	}
	public String getSuffix() {
		return suffix;
	}
	public void setSuffix(String suffix) {
		this.suffix = suffix;
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
	public Converter getToTextConverter() {
		if (toTextConverter == null) {
			setToTextConverter(Defaults.createToTextConverter());
		}
		return toTextConverter;
	}
	public void setToTextConverter(Converter objectToTextConverter) {
		this.toTextConverter = objectToTextConverter;
	}
	
	public boolean isShowNullValues() {
		return showNullValues;
	}
	public void setShowNullValues(boolean showNullValues) {
		this.showNullValues = showNullValues;
	}
}
