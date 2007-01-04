package net.sf.morph.transform.converters;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Map;

import net.sf.morph.transform.Transformer;

/**
 * @author Matt Sgarlata
 * @since Feb 3, 2005
 */
public class NumberToTimeConverterTestCase extends BaseConverterTestCase {

	public List createInvalidDestinationClasses() throws Exception {
		List list = new ArrayList();
		list.add(List.class);
		list.add(Object.class);
		list.add(Map.class);
		list.add(Object[].class);
		list.add(Number.class);
		list.add(int.class);
		list.add(char.class);
		return list;
	}

	public List createInvalidSources() throws Exception {
		List list = new ArrayList();
		list.add(new Date());
		list.add(new GregorianCalendar());
		return list;
	}

	public List createValidPairs() throws Exception {
		Calendar now = new GregorianCalendar();
		String nowString = "" + now.getTime().getTime();
		
		Date date = new Date(2005, 0, 1);
		Calendar newYearsDay2005 = new GregorianCalendar();
		newYearsDay2005.setTimeInMillis(date.getTime());
		
		List list = new ArrayList();
		list.add(new ConvertedSourcePair(now, new BigInteger(nowString)));
		list.add(new ConvertedSourcePair(now, new Long(nowString)));
		list.add(new ConvertedSourcePair(now, new BigDecimal(nowString)));
		list.add(new ConvertedSourcePair(now, new Double(nowString)));
		list.add(new ConvertedSourcePair(now.getTime(), new BigInteger(nowString)));
		list.add(new ConvertedSourcePair(now.getTime(), new Long(nowString)));
		list.add(new ConvertedSourcePair(now.getTime(), new BigDecimal(nowString)));
		list.add(new ConvertedSourcePair(now.getTime(), new Double(nowString)));
		list.add(new ConvertedSourcePair(newYearsDay2005, new Long(date.getTime())));
		return list;
	}

	public List createDestinationClasses() throws Exception {
		List list = new ArrayList();
		list.add(Date.class);
		list.add(java.sql.Date.class);
		list.add(Timestamp.class);
		list.add(Calendar.class);
		list.add(GregorianCalendar.class);
		return list;
	}

	protected Transformer createTransformer() {
		return new NumberToTimeConverter();
	}

}
