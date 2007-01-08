package net.sf.morph.beans;

import java.beans.PropertyEditorSupport;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import net.sf.morph.Morph;
import net.sf.morph.MorphException;

/**
 * A property editor that delegates all conversion requests to be handled
 * automatically by Morph.
 * 
 * @author Matt Sgarlata
 * @since Jul 3, 2006
 */
public class MorphPropertyEditor extends PropertyEditorSupport {

	private static final Log log = LogFactory.getLog(MorphPropertyEditor.class);

	private Class destinationType;
	
	public MorphPropertyEditor() {
		super();
	}

	public MorphPropertyEditor(Class destinationType) {
		super();
		setDestinationType(destinationType);
	}
	
	protected void checkState() throws IllegalStateException {
		if (getDestinationType() == null) {
			throw new IllegalStateException("The destinationType cannot be null");
		}
	}

	public String getAsText() {
		checkState();
		return Morph.convertToString(getValue(), null);
	}

	public void setAsText(String text) throws IllegalArgumentException {
		checkState();
		try {
			Object value = Morph.convert(getDestinationType(), text, null);
			setValue(value);
		}
		catch (MorphException e) {
			if (log.isErrorEnabled()) {
				log.error(e.getMessage(), e);
			}
			throw new IllegalArgumentException(e.getMessage());
		}
	}

	public Class getDestinationType() {
		return destinationType;
	}

	public void setDestinationType(Class destinationType) {
		this.destinationType = destinationType;
	}
	
}
