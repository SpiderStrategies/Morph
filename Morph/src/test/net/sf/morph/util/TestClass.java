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
package net.sf.morph.util;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


public class TestClass {
	
	private static final Log log = LogFactory.getLog(TestClass.class);
	
	public static final Number[] NUMBER_ARRAY = new Number[] { new Integer(1), new Long(2) };

	/**
	 * Returns an empty instance of this class
	 */
	public static TestClass getEmptyObject() {
		return new TestClass();
	}
	
	public static Map getEmptyMap() {
		Map map = new HashMap();
		map.put("anObject", null);
		map.put("myInteger", new Integer(0));
		map.put("myMap", null);
		map.put("myLongValue", null);
		map.put("array", null);
		map.put("numberArray", null);
		map.put("bigDecimal", null);
		map.put("string", null);
		return map;
	}
	
	/**
	 * Returns an instance of this class with some fields populated
	 */
	public static TestClass getPartialObject() {
		TestClass partialObject = new TestClass();
		partialObject.setAnObject(new Long(14));
		partialObject.setMyInteger(4);
		partialObject.setMyMap(null);
		partialObject.setMyLongValue(new Long(13));
		return partialObject;
	}
	
	public static Map getPartialMap() {
		Map partialMap = new HashMap();
		partialMap.put("anObject", new Long(14));
		partialMap.put("myInteger", new Integer(4));
		partialMap.put("myMap", null);
		partialMap.put("myLongValue", new Long(13));
		partialMap.put("array", null);
		partialMap.put("numberArray", null);
		partialMap.put("bigDecimal", null);
		partialMap.put("string", null);
		return partialMap;
	}
	
	public static Map getMyMapProperty() {
		Map myMap = new HashMap();
		myMap.put("one", new Integer(1));
		myMap.put("two", new Object[] { new Integer(1), new BigDecimal(2) });
		return myMap;
	}
	
	public static Map getFullMap() {
		Map fullMap = new HashMap();
		fullMap.put("anObject", new Long(14));
		fullMap.put("myInteger", new Integer(4));
		fullMap.put("myMap", getMyMapProperty());
		fullMap.put("myLongValue", new Long(13));
		fullMap.put("array", new Object[] { "hi" });
		fullMap.put("numberArray", NUMBER_ARRAY);
		fullMap.put("funkyArray", NUMBER_ARRAY);
		fullMap.put("bigDecimal", new BigDecimal(3.5));
		fullMap.put("string", "string");
		return fullMap;
	}
	
	/**
	 * Returns an instance of this class with all fields populated
	 */
	public static TestClass getFullObject() {
		TestClass fullObject = new TestClass();
		fullObject.setAnObject(new Long(14));
		fullObject.setMyInteger(4);
		fullObject.setMyMap(getMyMapProperty());
		fullObject.setMyLongValue(new Long(13));
		fullObject.setArray(new Object[] { "hi" });
		fullObject.setBigDecimal(new BigDecimal(3.5));
		fullObject.setNumberArray(NUMBER_ARRAY);
		fullObject.setFunkyArray(NUMBER_ARRAY);
		fullObject.setString("string");
		return fullObject;
	}
	
	private String string;
	private int myInteger;
	private Long myLongValue;
	private Map myMap;
	private Object anObject;
	private Object[] array;
	private Number[] numberArray;
	// public for testing purposes in the ObjectReflectorTestCase
	public Number[] funkyArray;
	private BigDecimal bigDecimal;
	
	public Object getMethodThatIsNotAProperty(Object arg1, Object arg2) {
		return null;
	}
	public void setMethodThatIsNotAProperty2(Object arg1, Object arg2) {
		// do nothing
	}
	
	public void allocateTwoSpacesForFunkyArray() {
		setFunkyArray(new Number[2]);
	}
	
	public Object[] getArray() {
		return array;
	}
	public void setArray(Object[] array) {
		this.array = array;
	}
	private void setFunkyArray(Number[] array) {
		this.funkyArray = array;
	}
	public void setFunkyArray(int index, Number number) {
		funkyArray[index] = number;
	}
	public Number getFunkyArray(int index) {
		return funkyArray[index];
	}
	public BigDecimal getBigDecimal() {
		return bigDecimal;
	}
	public void setBigDecimal(BigDecimal bigDecimal) {
		this.bigDecimal = bigDecimal;
	}
	public Number[] getNumberArray() {
		return numberArray;
	}
	public void setNumberArray(Number[] numberArray) {
		this.numberArray = numberArray;
	}
	public String getString() {
		return string;
	}
	public void setString(String string) {
		this.string = string;
	}
	public Object getAnObject() {
		return anObject;
	}
	public void setAnObject(Object anObject) {
		this.anObject = anObject;
	}
	public int getMyInteger() {
		return myInteger;
	}
	public void setMyInteger(int myInteger) {
		this.myInteger = myInteger;
	}
	public Long getMyLongValue() {
		return myLongValue;
	}
	public void setMyLongValue(Long myLongValue) {
		this.myLongValue = myLongValue;
	}
	public Map getMyMap() {
		return myMap;
	}
	public void setMyMap(Map myMap) {
		this.myMap = myMap;
	}
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}
}