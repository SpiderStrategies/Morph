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
package net.sf.morph2.transform.copiers.dsl;

import org.apache.commons.lang.builder.ToStringBuilder;

import net.sf.morph2.util.TestClass;

public class A {
	private String foo;
	private int bar;
	private Object baz;
	private String stringA;
	private int intA;
	private Object objectA;
	private TestClass testClass;

	/**
	 * Get the bar of this A.
	 * @return the bar
	 */
	public int getBar() {
		return bar;
	}

	/**
	 * Set the bar of this A.
	 * @param bar the bar to set
	 */
	public void setBar(int bar) {
		this.bar = bar;
	}

	/**
	 * Get the baz of this A.
	 * @return the baz
	 */
	public Object getBaz() {
		return baz;
	}

	/**
	 * Set the baz of this A.
	 * @param baz the baz to set
	 */
	public void setBaz(Object baz) {
		this.baz = baz;
	}

	/**
	 * Get the foo of this A.
	 * @return the foo
	 */
	public String getFoo() {
		return foo;
	}

	/**
	 * Set the foo of this A.
	 * @param foo the foo to set
	 */
	public void setFoo(String foo) {
		this.foo = foo;
	}

	/**
	 * Get the intA of this A.
	 * @return the intA
	 */
	public int getIntA() {
		return intA;
	}

	/**
	 * Set the intA of this A.
	 * @param intA the intA to set
	 */
	public void setIntA(int intA) {
		this.intA = intA;
	}

	/**
	 * Get the objectA of this A.
	 * @return the objectA
	 */
	public Object getObjectA() {
		return objectA;
	}

	/**
	 * Set the objectA of this A.
	 * @param objectA the objectA to set
	 */
	public void setObjectA(Object objectA) {
		this.objectA = objectA;
	}

	/**
	 * Get the stringA of this A.
	 * @return the stringA
	 */
	public String getStringA() {
		return stringA;
	}

	/**
	 * Set the stringA of this A.
	 * @param stringA the stringA to set
	 */
	public void setStringA(String stringA) {
		this.stringA = stringA;
	}

	/**
	 * Get the testClass of this A.
	 * @return the testClass
	 */
	public synchronized TestClass getTestClass() {
		return testClass;
	}

	/**
	 * Set the testClass of this A.
	 * @param testClass the testClass to set
	 */
	public synchronized void setTestClass(TestClass testClass) {
		this.testClass = testClass;
	}

	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}

}
