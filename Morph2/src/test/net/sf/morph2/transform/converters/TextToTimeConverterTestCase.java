package net.sf.morph2.transform.converters;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Map;

import net.sf.morph2.transform.Transformer;
import net.sf.morph2.util.TestObjects;

public class TextToTimeConverterTestCase extends BaseConverterTestCase {

	public List createInvalidDestinationClasses() throws Exception {
		List list = new ArrayList();
		list.add(Object.class);
		list.add(Number.class);
		list.add(Long.TYPE);
		list.add(Date.class);
		list.add(Map.class);
		list.add(List.class);
		return list;
	}

	public List createInvalidSources() throws Exception {
		List list = new ArrayList();
		list.add(new Long(1));
		list.add(new TestObjects());
		list.add(new ArrayList());
		list.add(new Object[0]);
		return list;
	}

	public List createValidPairs() throws Exception {
		Calendar calendar = new GregorianCalendar();
		calendar.set(Calendar.YEAR, 2005);
		calendar.set(Calendar.MONTH, Calendar.JANUARY);
		calendar.set(Calendar.DAY_OF_MONTH, 30);
		// set to 11 o'clock eastern, compensating for the time zone offset
		calendar.set(Calendar.HOUR_OF_DAY, 11 + 5);
		calendar.set(Calendar.MINUTE, 51);
		calendar.set(Calendar.SECOND, 2);
		calendar.set(Calendar.ZONE_OFFSET, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		String source = "January 30, 2005 11:51:02 AM EST";
		List list = new ArrayList();
		list.add(new ConvertedSourcePair(calendar, source));
		list.add(new ConvertedSourcePair(calendar.getTime(), source));
		list.add(new ConvertedSourcePair(calendar, new StringBuffer(source)));
		list.add(new ConvertedSourcePair(calendar.getTime(), new StringBuffer(source)));
		return list;
	}

	public List createDestinationClasses() throws Exception {
		List list = new ArrayList();
		list.add(Calendar.class);
		list.add(GregorianCalendar.class);
		list.add(Date.class);
		return list;
	}

	protected Transformer createTransformer() {
		return new TextToTimeConverter();
	}

}
