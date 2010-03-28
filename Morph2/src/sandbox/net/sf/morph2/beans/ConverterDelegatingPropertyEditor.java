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
package net.sf.morph2.beans;

import java.beans.PropertyEditor;
import java.beans.PropertyEditorSupport;
import java.util.Locale;

import net.sf.morph2.MorphException;
import net.sf.morph2.transform.Converter;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * A PropertyEditor backed by underlying Morph
 * {@link net.sf.morph2.transform.Converter}s.
 * 
 * @author Matt Sgarlata
 * @since Dec 13, 2004
 */
public class ConverterDelegatingPropertyEditor extends PropertyEditorSupport implements PropertyEditor {
	
	private static final Log log = LogFactory.getLog(ConverterDelegatingPropertyEditor.class);

	private Converter toTextConverter;
	private Converter toDestinationTypeConverter;
	private Class destinationType;

	protected void checkState() throws IllegalStateException {
		if (getToTextConverter() == null) {
			throw new IllegalStateException("The toTextConverter cannot be null");
		}
		if (getToDestinationTypeConverter() == null) {
			throw new IllegalStateException("The toDestinationTypeConverter cannot be null");
		}
		if (getDestinationType() == null) {
			throw new IllegalStateException("The destinationType cannot be null");
		}
	}
	
	public String getAsText() {
		checkState();
		return (String) getToTextConverter().convert(String.class, getValue(),
			Locale.getDefault());
	}

	public void setAsText(String text) throws IllegalArgumentException {
		checkState();
		try {
			Object value = getToDestinationTypeConverter().convert(
				getDestinationType(), text, Locale.getDefault());
			setValue(value);
		}
		catch (MorphException e) {
			if (log.isErrorEnabled()) {
				log.error(e);
			}
			throw new IllegalArgumentException(e.getMessage());
		}
	}

	public Converter getToTextConverter() {
		return toTextConverter;
	}

	public void setToTextConverter(Converter converter) {
		this.toTextConverter = converter;
	}

	public Converter getToDestinationTypeConverter() {
		return toDestinationTypeConverter;
	}

	public void setToDestinationTypeConverter(Converter toDestinationTypeConverter) {
		this.toDestinationTypeConverter = toDestinationTypeConverter;
	}

	public Class getDestinationType() {
		return destinationType;
	}

	public void setDestinationType(Class destinationType) {
		this.destinationType = destinationType;
	}
	
}
