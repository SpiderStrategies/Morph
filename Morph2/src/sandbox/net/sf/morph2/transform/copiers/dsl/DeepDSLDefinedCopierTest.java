/*
 * Copyright 2007 the original author or authors.
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
package net.sf.morph2.transform.copiers.dsl;

import java.util.Arrays;
import java.util.List;

import net.sf.morph2.util.TestClass;
import net.sf.morph2.util.TestUtils;

public class DeepDSLDefinedCopierTest extends DSLDefinedCopierTestBase {
	protected String getSource() {
		return "deepTest.morph";
	}

	public List createInvalidPairs() throws Exception {
		A a = new A();
		a.setFoo("foo");
		a.setBar(600);
		a.setBaz(new Integer(0));
		a.setStringA("string");
		a.setIntA(300);
		a.setObjectA("object");
		B b = new B();
		b.setFoo(new char[] { 'f', 'o', 'o' });
		b.setBar("600");
		b.setBaz(new Integer(1));
		b.setStringB("string");
		b.setIntB(300);
		b.setObjectB("object");
		return Arrays.asList(new Object[] { new ConvertedSourcePair(b, a), new ConvertedSourcePair(a, b) });
	}

	public List createValidPairs() throws Exception {
		return null;
	}

	public List createDestinationClasses() throws Exception {
		return Arrays.asList(new Class[] { A.class, B.class});
	}

	public void testCopy() {
		A a = new A();
		B b = new B();
		a.setFoo("foo");
		a.setBar(600);
		a.setBaz(new Object());
		a.setStringA("string");
		a.setIntA(300);
		a.setObjectA("object");
		a.setTestClass(TestClass.getPartialObject());
		b.setFoo(new char[] { 'f', 'o', 'o' });
		b.setBar("600");
		b.setBaz(a.getBaz());
		b.setStringB("string");
		b.setIntB(300);
		b.setObjectB("object");
		b.setMap(TestClass.getPartialMap());
		A dest = new A();
		dest.setTestClass(new TestClass());
		copier.copy(dest, b);
		TestUtils.assertEquals(a, dest);
		b.setMap(TestClass.getFullMap());
		copier.copy(dest, b);
		TestUtils.assertEquals(a, dest);
	}
}
