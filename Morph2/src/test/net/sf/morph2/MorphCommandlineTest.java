package net.sf.morph2;

import java.util.Calendar;
import java.util.GregorianCalendar;

import net.sf.morph2.transform.Converter;
import net.sf.morph2.transform.converters.TextToTimeConverter;
import net.sf.morph2.util.TestUtils;

/**
 * @author Matt Sgarlata
 * @since Dec 23, 2004
 */
public class MorphCommandlineTest {

	public static void main(String[] args) {
		String source = "January 30, 2005 11:51:02 AM EST";
		Converter converter = new TextToTimeConverter();
		Calendar converted = (Calendar) converter.convert(Calendar.class, source, null);
		System.out.println(converted.getTime());
		System.out.println(converted.getTime().getTime());
		
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
		System.out.println(calendar.getTime());
		System.out.println(calendar.getTime().getTime());
		
		System.out.println(calendar.equals(converted));
		System.out.println(calendar.getTime().equals(converted.getTime()));
		System.out.println(TestUtils.equals(calendar, converted));
		System.out.println(calendar.getTime().compareTo(converted.getTime()));
	}
}
