package net.sf.morph.transform.converters;

import java.text.DateFormat;
import java.util.Date;
import java.util.Locale;

import net.sf.composite.util.ObjectUtils;
import net.sf.morph.Defaults;
import net.sf.morph.transform.Converter;
import net.sf.morph.transform.DecoratedConverter;
import net.sf.morph.transform.transformers.BaseTransformer;

public class TextToTimeConverter extends BaseTransformer implements Converter, DecoratedConverter {
	
	private static final DateFormat DEFAULT_DATE_FORMAT = DateFormat.getDateTimeInstance(DateFormat.LONG, DateFormat.LONG);
	
	private DateFormat dateFormat;
	private Converter timeConverter;
	private Converter textConverter;

	protected Object convertImpl(Class destinationClass, Object source,
			Locale locale) throws Exception {
		
		String text = (String) getTextConverter().convert(String.class, source, locale);
		
		if (ObjectUtils.isEmpty(text)) {
			return null;
		}
		else {
			Date date = getDateFormat().parse(text);			
			return getTimeConverter().convert(destinationClass, date, locale);
		}
	}

	protected Class[] getSourceClassesImpl() throws Exception {
		return getTextConverter().getDestinationClasses();
	}

	protected Class[] getDestinationClassesImpl() throws Exception {
		return getTimeConverter().getSourceClasses();
	}

	public DateFormat getDateFormat() {
		if (dateFormat == null) {
			setDateFormat(DEFAULT_DATE_FORMAT);
		}
		return dateFormat;
	}
	public void setDateFormat(DateFormat dateFormat) {
		this.dateFormat = dateFormat;
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
	public Converter getTimeConverter() {
		if (timeConverter == null) {
			setTimeConverter(Defaults.createTimeConverter());
		}
		return timeConverter;
	}
	public void setTimeConverter(Converter timeConverter) {
		this.timeConverter = timeConverter;
	}

}
