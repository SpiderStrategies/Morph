package net.sf.morph2.transform.copiers.dsl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.collections.map.MultiValueMap;

import net.sf.morph2.transform.Transformer;
import net.sf.morph2.transform.transformers.SimpleDelegatingTransformer;
import net.sf.morph2.util.TestClass;

/**
 *
 */
public class MultiMappedTestCase extends DSLDefinedCopierTestBase {
	/**
	 * {@inheritDoc}
	 * @see net.sf.morph2.transform.converters.BaseConverterTestCase#createDestinationClasses()
	 */
	public List createDestinationClasses() throws Exception {
		return Arrays.asList(new Class[] { A.class, B.class });
	}

	/**
	 * {@inheritDoc}
	 * @see net.sf.morph2.transform.converters.BaseConverterTestCase#createValidPairs()
	 */
	public List createValidPairs() throws Exception {
		ArrayList l = new ArrayList();
		A a = new A();
		B b = new B();
		a.setFoo("foo");
		a.setBar(600);
		a.setBaz(new Object());
		a.setStringA("string");
		a.setIntA(300);
		a.setObjectA("IGNORED");
		b.setFoo(new char[] { 'f', 'o', 'o' });
		b.setBar("600");
		b.setBaz(a.getBaz());
		b.setStringB("string");
		b.setIntB(300);
		b.setObjectB("string");
		l.add(new ConvertedSourcePair(b, a));
		a = new A();
		b = new B();
		a.setFoo("foo");
		a.setBar(600);
		a.setBaz(new Object());
		a.setStringA("string");
		a.setIntA(300);
		a.setObjectA("string");
		b.setFoo(new char[] { 'f', 'o', 'o' });
		b.setBar("600");
		b.setBaz(a.getBaz());
		b.setStringB("string");
		b.setIntB(300);
		b.setObjectB("IGNORED");
		l.add(new ConvertedSourcePair(a, b));
		a = new A();
		b = new B();
		b.setBar("0");
		l.add(new ConvertedSourcePair(a, b));
		l.add(new ConvertedSourcePair(b, a));
		a = new A();
		b = new B();
		a.setFoo("foo");
		a.setBar(600);
		a.setBaz(new Object());
		a.setStringA("string");
		a.setIntA(300);
		a.setObjectA("string");
		a.setTestClass(TestClass.getEmptyObject());
		b.setFoo(new char[] { 'f', 'o', 'o' });
		b.setBar("600");
		b.setBaz(a.getBaz());
		b.setStringB("string");
		b.setIntB(300);
		b.setObjectB("IGNORED");
		b.setMap(TestClass.getEmptyMap());
		l.add(new ConvertedSourcePair(a, b));
		a = new A();
		b = new B();
		a.setFoo("foo");
		a.setBar(600);
		a.setBaz(new Object());
		a.setStringA("string");
		a.setIntA(300);
		a.setObjectA("IGNORED");
		a.setTestClass(TestClass.getFullObject());
		b.setFoo(new char[] { 'f', 'o', 'o' });
		b.setBar("600");
		b.setBaz(a.getBaz());
		b.setStringB("string");
		b.setIntB(300);
		b.setObjectB("string");
		b.setMap(TestClass.getFullMap());
		l.add(new ConvertedSourcePair(b, a));
		return l;
	}

	protected Transformer createTransformer() {
		DSLDefinedCopier dslDefinedCopier = new DSLDefinedCopier(getClass()
				.getResourceAsStream(getSource()));
		dslDefinedCopier.setPropertyMapClass(MultiValueMap.class);
		SimpleDelegatingTransformer result = new SimpleDelegatingTransformer(
				new Transformer[] { dslDefinedCopier }, true);
		return result;
	}


	/**
	 * {@inheritDoc}
	 * @see net.sf.morph2.transform.copiers.dsl.DSLDefinedCopierTestBase#getSource()
	 */
	protected String getSource() {
		return "multiMappedTest.morph";
	}

	/**
	 * {@inheritDoc}
	 * @see net.sf.morph2.transform.converters.BaseConverterTestCase#createInvalidPairs()
	 */
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
}
