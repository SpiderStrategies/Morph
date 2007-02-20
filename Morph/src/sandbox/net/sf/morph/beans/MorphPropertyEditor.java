package net.sf.morph.beans;

import java.beans.PropertyEditorSupport;

import net.sf.morph.Morph;
import net.sf.morph.MorphException;
import net.sf.morph.transform.Converter;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * A property editor that delegates all conversion requests to be handled
 * automatically by Morph. By default, conversions will be done using Morph's
 * default behavior (so the behavior will be consistent with the {@link Morph}
 * convenience class).
 * 
 * <p>
 * For custom behavior, the optional <code>toTextConverter</code> and
 * <code>toDestinationTypeConverter</code> properties may be set in order to
 * override Morph's default behavior.
 * 
 * @author Matt Sgarlata
 * @since Jul 3, 2006
 */
public class MorphPropertyEditor extends PropertyEditorSupport {

	private static final Log log = LogFactory.getLog(MorphPropertyEditor.class);

	private Class destinationType;
	private Converter toTextConverter;
	private Converter toDestinationTypeConverter;

	public MorphPropertyEditor() {
		super();
	}

	public MorphPropertyEditor(Class destinationType) {
		super();
		setDestinationType(destinationType);
	}

	public MorphPropertyEditor(Class destinationType,
	        Converter toTextConverter, Converter toDestinationTypeConverter) {
		super();
		setDestinationType(destinationType);
		setToTextConverter(toTextConverter);
		setToDestinationTypeConverter(toDestinationTypeConverter);
	}

	protected void checkState() throws IllegalStateException {
		if (getDestinationType() == null) {
			throw new IllegalStateException("The destinationType cannot be null");
		}
	}

	public String getAsText() {
		checkState();
		if (getToTextConverter() == null) {
			return Morph.convertToString(getValue());
		}
		return (String) getToTextConverter().convert(String.class, getValue(), null);
	}

	public void setAsText(String text) throws IllegalArgumentException {
		checkState();
		try {
			Object value;
			if (getToDestinationTypeConverter() == null) {
				value = Morph.convert(getDestinationType(), text);
			}
			else {
				value = getToDestinationTypeConverter().convert(
					getDestinationType(), text, null);
			}
			setValue(value);
		}
		catch (MorphException e) {
			if (log.isErrorEnabled()) {
				log.error(e.getMessage(), e);
			}
			// rethrow exception as IllegalArgumentException, as specified in
			// the API
			throw new IllegalArgumentException(e.getMessage());
		}
	}

	public Class getDestinationType() {
		return destinationType;
	}

	public void setDestinationType(Class destinationType) {
		this.destinationType = destinationType;
	}

	public Converter getToDestinationTypeConverter() {
		return toDestinationTypeConverter;
	}

	public void setToDestinationTypeConverter(
	        Converter toDestinationTypeConverter) {
		this.toDestinationTypeConverter = toDestinationTypeConverter;
	}

	public Converter getToTextConverter() {
		return toTextConverter;
	}

	public void setToTextConverter(Converter toTextConverter) {
		this.toTextConverter = toTextConverter;
	}

}
