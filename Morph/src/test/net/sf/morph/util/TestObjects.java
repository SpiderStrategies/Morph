package net.sf.morph.util;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Vector;

import javax.servlet.ServletContext;
import javax.servlet.ServletRequest;
import javax.servlet.http.HttpSession;
import javax.servlet.jsp.PageContext;

import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.mock.web.MockPageContext;
import org.springframework.mock.web.MockServletContext;

/**
 * @author Matt Sgarlata
 * @since Dec 12, 2004
 */
public class TestObjects {

	public final Object[] emptyObjectArray;
	public final Object[] singleElementObjectArray;
	public final Object[] multiElementObjectArray;
	
	public final Map emptyMap;
	public final Map singleElementMap;
	public final Map multiElementMap;
	
	public final Long[] emptyLongArray;
	public final Long[] singleElementLongArray;
	public final Long[] multiElementLongArray;
	
	public final Map[] emptyMapArray;
	public final Map[] singleElementMapArray;
	public final Map[] multiElementMapArray;
	
	public final List emptyList = new ArrayList();
	public final Vector emptyVector = new Vector();
	
	public final Long one;
	public final BigDecimal two;
	public final Double three;
	
	public final Number[] oneTwoThreeNumberArray;
	public final Object[] oneTwoThreeObjectArray;
	public final Map oneTwoThreeMap;
	public final List oneTwoThreeList;
	public final Vector oneTwoThreeVector;
	public final Set oneTwoThreeSet;
	
	public final long[] emptyPrimitiveArray;
	public final long[] multiElementEmptyPrimitiveArray;
	public final long[] singleElementPrimitiveArray;
	public final long[] multiElementPrimitiveArray;
	public final long[][] multidimensionalPrimitiveArray = { { 13, 12, 2}, {2, 4, 6}, {1, 2, 3} };
	public final Object[][] multidimensionalObjectArray;
	public final Long[][] multidimensionalLongArray = { { new Long(13), new Long(12), new Long(2) }, { new Long(2), new Long(4), new Long(6) }, { new Long(1), new Long(2), new Long(3) } }; 
	
	public final ServletRequest servletRequest;
	public final PageContext pageContext;
	public final HttpSession httpSession;
	public final ServletContext servletContext;
	public final String[] threeStringsArray;
	
	public TestObjects() {
		emptyPrimitiveArray = new long[0];
		multiElementEmptyPrimitiveArray = new long[] { 1, 2, 3 };
		singleElementPrimitiveArray = new long[] { 2 };
		multiElementPrimitiveArray = new long[] { 13, 2, 4 };
//		multidimensionalPrimitiveArray =
//			;
//		multidimensionalPrimitiveArray = new long[3][3];
//		multidimensionalPrimitiveArray[0][0] = 13;
//		multidimensionalPrimitiveArray[0][1] = 12;
//		multidimensionalPrimitiveArray[0][2] = 2;
//		multidimensionalPrimitiveArray[1][0] = 2;
//		multidimensionalPrimitiveArray[1][1] = 4;
//		multidimensionalPrimitiveArray[1][2] = 6;
//		multidimensionalPrimitiveArray[2][0] = 1;
//		multidimensionalPrimitiveArray[2][1] = 2;
//		multidimensionalPrimitiveArray[2][2] = 3;
		multidimensionalObjectArray = new Object[][] {
			{ new Long(13), new Integer(12), new Byte("2") },
			{ new BigDecimal("2"), new BigInteger("4"), new Short("6") },
			{ "1", new Float(2.0), new Double(3.0) }
		};
		
		emptyObjectArray = new Object[0];
		singleElementObjectArray = new Object[1];
		multiElementObjectArray = new Object[3];
		
		emptyMap = ContainerUtils.createOrderedMap();

		singleElementMap = ContainerUtils.createOrderedMap();
		singleElementMap.put("one", null);
		
		multiElementMap = ContainerUtils.createOrderedMap();
		multiElementMap.put("one", null);
		multiElementMap.put("two", null);
		multiElementMap.put("three", null);
		
		emptyLongArray = new Long[0];
		singleElementLongArray = new Long[1];
		multiElementLongArray = new Long[3];
		
		emptyMapArray = new Map[0];
		singleElementMapArray = new Map[1];
		multiElementMapArray = new Map[3];
		
		one = new Long(1);
		two = new BigDecimal(2);
		three = new Double(3);
		
		oneTwoThreeNumberArray = new Number[] { one, two, three };
		oneTwoThreeObjectArray = new Object[] { one, two, three };
		
		oneTwoThreeMap = ContainerUtils.createOrderedMap();
		oneTwoThreeMap.put("one", one);
		oneTwoThreeMap.put("two", two);
		oneTwoThreeMap.put("three", three);
		
		oneTwoThreeList = new ArrayList();
		oneTwoThreeList.add(one);
		oneTwoThreeList.add(two);
		oneTwoThreeList.add(three);
		
		oneTwoThreeVector = new Vector();
		oneTwoThreeVector.add(one);
		oneTwoThreeVector.add(two);
		oneTwoThreeVector.add(three);
		
		oneTwoThreeSet = ContainerUtils.createOrderedSet();
		oneTwoThreeSet.add(one);
		oneTwoThreeSet.add(two);
		oneTwoThreeSet.add(three);

		threeStringsArray = new String[] { "one", "two", "three" };
		MockHttpServletRequest request = new MockHttpServletRequest();
		request.addParameter("param1", "one");
		request.addParameter("three", threeStringsArray);
		request.addParameter("inBothParamsAndAttrs", "paramValue");
		request.setAttribute("attribute1", "one");
		request.setAttribute("attribute2", "two");
		request.setAttribute("three", threeStringsArray);
		request.setAttribute("integer", new Integer(2));
		request.setAttribute("inBothParamsAndAttrs", new Integer(3));
		request.setAttribute("multidimensionalPrimitiveArray", multidimensionalPrimitiveArray);
		servletRequest = request;
		
		MockPageContext context = new MockPageContext();
		context.setAttribute("attribute1", "one");
		context.setAttribute("attribute2", "two");
		context.setAttribute("three", threeStringsArray);
		context.setAttribute("multidimensionalPrimitiveArray", multidimensionalPrimitiveArray);
		pageContext = context;

		MockHttpSession session = new MockHttpSession();
		session.setAttribute("attribute1", "one");
		session.setAttribute("attribute2", "two");
		session.setAttribute("three", threeStringsArray);
		session.setAttribute("multidimensionalPrimitiveArray", multidimensionalPrimitiveArray);
		httpSession = session;
		
		MockServletContext context2 = new MockServletContext();
		context2.setAttribute("attribute1", "one");
		context2.setAttribute("attribute2", "two");
		context2.setAttribute("three", threeStringsArray);
		context2.setAttribute("multidimensionalPrimitiveArray", multidimensionalPrimitiveArray);
		context2.addInitParameter("attribute1", "one");
		context2.addInitParameter("attribute2", "two");
		servletContext = context2;
	}
	
}
