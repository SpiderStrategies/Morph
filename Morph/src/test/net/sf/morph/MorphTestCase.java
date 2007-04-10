package net.sf.morph;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;

import org.springframework.mock.web.MockHttpServletRequest;

import junit.framework.TestCase;
import net.sf.morph.reflect.ReflectionException;
import net.sf.morph.transform.TransformationException;
import net.sf.morph.util.TestUtils;

/**
 * @author Matt Sgarlata
 * @since Mar 2, 2005
 */
public class MorphTestCase extends TestCase {
	
	protected static interface ITestInterface { }
	protected static class TestImplementation implements ITestInterface { }

	public void testGetLongObject() {
		Map map = new HashMap();
		map.put("longObject", "2");
		TestUtils.assertEquals(new Long(2), Morph.getLongObject(map, "longObject"));		
	}
	
	public void testRuntimeArrayTypeOfConversion() {
		List list = new ArrayList();
		list.add(new Integer(1));
		list.add(new Integer(2));
		list.add(new BigDecimal(3));
		
		Object converted = Morph.convert(Integer[].class, list);
		assertTrue(converted instanceof Integer[]);
		
		list = new ArrayList();
		list.add(new HashMap());
		list.add(new HashMap());
		Map[] result = new Map[] { new HashMap(), new HashMap() } ;
		
		converted = Morph.convert(Map[].class, list);
		assertSame(Map[].class, converted.getClass());
		TestUtils.assertEquals(result, converted);
		
		list = new ArrayList();
		list.add(new Integer(2));
		list.add(new Integer(3));
		converted = Morph.convert(Set.class, list);
		assertSame(HashSet.class, converted.getClass());
		TestUtils.assertEquals(converted, new HashSet(list));
		
		list = new ArrayList();
		list.add(new TestImplementation());
		list.add(new TestImplementation());
		converted = Morph.convert(ITestInterface[].class, list);
		assertEquals(ITestInterface[].class, converted.getClass());
		TestUtils.assertEquals(converted, new ITestInterface[] { new TestImplementation(), new TestImplementation()});
		
		list = new ArrayList();
		converted = Morph.convert(ITestInterface[].class, list);
		assertEquals(ITestInterface[].class, converted.getClass());
		TestUtils.assertEquals(converted, new ITestInterface[] { });

		list = new ArrayList();
		converted = Morph.convert(String[].class, list);
		assertEquals(String[].class, converted.getClass());
		TestUtils.assertEquals(converted, new String[] { });
	}
	
	public void testNumberAndCalendarConversion() {
		Date date = new Date(2005, 0, 1);
		Calendar newYearsDay2005 = new GregorianCalendar();
		newYearsDay2005.setTimeInMillis(date.getTime());
		TestUtils.assertEquals(newYearsDay2005, Morph.convertToCalendar(new Long(date.getTime())));		
		TestUtils.assertEquals(new Long(date.getTime()), Morph.convertToLongObject(date));		
	}

	public void testStringToDoubleConversion() {
		assertEquals(2.0d, Morph.convertToDouble("2"), 0.001d);
		assertEquals(2.0d, Morph.convertToDouble("2.0"), 0.001d);
		assertEquals(2.1d, Morph.convertToDouble("2.10000"), 0.001d);
		assertEquals(2.5d, Morph.convertToDouble("2.5"), 0.001d);
		assertEquals(2.9d, Morph.convertToDouble("2.9"), 0.001d);
	}
	

	public void testLocaleSpecificConversions() throws Exception {
		double precision = 0.000001d;
		
		// text in English
		
		assertEquals(4444.44d, Morph.convertToDouble("$4,444.44", Locale.US), precision);
		assertEquals(4444.44d, Morph.convertToDouble("  $ 4,444.44", Locale.US), precision);
		assertEquals(.35, Morph.convertToFloat("35%", Locale.US), precision);
		assertEquals(.0035, Morph.convertToFloat(".35%", Locale.US), precision);
		assertEquals(3.5, Morph.convertToFloat("350%", Locale.US), precision);
		assertEquals(3500000.1234d, Morph.convertToDouble("3,500,000.1234", Locale.US), precision);
		
		assertEquals("3500000", Morph.convertToString(new Double(3500000), Locale.US));
		
		// test in Dutch
		
		Locale dutch = new Locale("nl");
		
		assertEquals(4444.44d, Morph.convertToDouble("\u20ac4.444,44", dutch), precision);
		assertEquals(4444.44d, Morph.convertToDouble("\u20ac 4.444,44", dutch), precision);
		assertEquals(.35, Morph.convertToFloat("35%", dutch), precision);
		assertEquals(.0035, Morph.convertToFloat(",35%", dutch), precision);
		assertEquals(3.5, Morph.convertToFloat("350%", dutch), precision);
		assertEquals(3500000.1234d, Morph.convertToDouble("3.500.000,1234", dutch), precision);
		assertEquals("3500000", Morph.convertToString(new Double(3500000), dutch));
	}
	
	public static final class DestinationWithoutGetter {
		Object testProperty;
//		public void setTestProperty(String object) {
//			testProperty = object;
//		}
		public void setTestProperty(Object object) {
			testProperty = object;
		}
	}
	
	public void testCopyToDestWithoutGetter() throws Exception {
		Map source = new HashMap();
		source.put("testProperty", new Integer(123));
		DestinationWithoutGetter dest = new DestinationWithoutGetter();
		Morph.copy(dest, source);
		assertTrue(dest.testProperty.equals(new Integer(123)));		
	}
	
	public void testGetBooleanObject() throws Exception {
		Map map = new HashMap();
		
		map.put("true", Boolean.TRUE);
		map.put("false", Boolean.FALSE);

		map.put("trueStr", "true");
		map.put("falseStr", "false");
		
		map.put("emptyStr", "");		
		map.put("null", null);

		assertEquals(Boolean.TRUE, Morph.getBooleanObject(map, "true"));
		assertEquals(Boolean.FALSE, Morph.getBooleanObject(map, "false"));

		assertEquals(Boolean.TRUE, Morph.getBooleanObject(map, "trueStr"));
		assertEquals(Boolean.FALSE, Morph.getBooleanObject(map, "falseStr"));

		assertEquals(null, Morph.getBooleanObject(map, "emptyStr"));
		assertEquals(null, Morph.getBooleanObject(map, "null"));
	}
	
	public void testConvertToBoolean() throws Exception {
		assertTrue(Morph.convertToBoolean(Boolean.TRUE));
		assertFalse(Morph.convertToBoolean(Boolean.FALSE));
		try {
			Morph.convertToBoolean(null);
			fail("Should not have been able to convert null to a boolean");
		}
		catch (TransformationException e) { }
	}
	
	public void testConvertToBooleanObject() throws Exception {
		assertEquals(Boolean.TRUE, Morph.convertToBooleanObject(Boolean.TRUE));
		assertEquals(Boolean.FALSE, Morph.convertToBooleanObject(Boolean.FALSE));
		assertNull(Morph.convertToBooleanObject(null));
	}
	
	public static class CopiableObject {
		private Boolean booleanProperty;

		public Boolean getBooleanProperty() {
			return booleanProperty;
		}

		public void setBooleanProperty(Boolean booleanProperty) {
			this.booleanProperty = booleanProperty;
		}
	}
	
	public void testCopy() {
		CopiableObject source = new CopiableObject();
		source.setBooleanProperty(Boolean.TRUE);
		CopiableObject destination = new CopiableObject();
		
		Morph.copy(destination, source);
		
		assertEquals(destination.getBooleanProperty(), Boolean.TRUE);
	}
	
	public void testGetSize() {
		try {
			Morph.getSize(null);
			fail("Exception should be thrown when retrieving the size of a null object");
		}
		catch (ReflectionException e) {
			// this is the expected behavior
		}
		assertEquals(0, Morph.getSize(new StringTokenizer("")));
		assertEquals(1, Morph.getSize(new StringTokenizer("1")));
		assertEquals(2, Morph.getSize(new StringTokenizer("two words")));
	}
	
	// has to be public or Morph barphs
	public static final class DomainObject {
		Integer[] array;
		
		public Integer[] getArray() {
        	return array;
        }

		public void setArray(Integer[] array) {
        	this.array = array;
        }
	}
	
	public void testWebLikeDataBinding() {
		Map map = new HashMap();
		map.put("array", "1,2");
		DomainObject object = new DomainObject();
		Morph.copy(object, map);
		
		assertEquals(new Integer(1), object.getArray()[0]);
		assertEquals(new Integer(2), object.getArray()[1]);
	}
	
}