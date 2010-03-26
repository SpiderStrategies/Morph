package net.sf.morph2.transform.converters;

import java.util.ArrayList;
import java.util.List;

import net.sf.morph2.transform.Transformer;

public class PrimitiveWrapperConverterTestCase extends BaseConverterTestCase {

	public List createInvalidDestinationClasses() throws Exception {
		List list = new ArrayList();
		list.add(List.class);
		list.add(Object.class);
		list.add(Class.class);
		list.add(null);
		return list;
	}

	public List createInvalidSources() throws Exception {
		List list = new ArrayList();
		list.add(List.class);
		list.add(Object.class);
		list.add(Class.class);
		list.add(null);
		return list;
	}

	public List createValidPairs() throws Exception {
		return null;
	}
	
	public void testConversions() {

		assertEquals(Boolean.TRUE, getConverter().convert(Boolean.class, Boolean.TRUE));
		assertEquals(Boolean.TRUE, getConverter().convert(boolean.class, Boolean.TRUE));
		
		assertEquals(new Byte("1"), getConverter().convert(Byte.class, new Byte("1")));
		assertEquals(new Byte("1"), getConverter().convert(byte.class, new Byte("1")));
		
		assertEquals(new Character('1'), getConverter().convert(Character.class, new Character('1')));
		assertEquals(new Character('1'), getConverter().convert(char.class, new Character('1')));
		
		assertEquals(new Short("1"), getConverter().convert(Short.class, new Short("1")));
		assertEquals(new Short("1"), getConverter().convert(short.class, new Short("1")));
		
		assertEquals(new Integer("1"), getConverter().convert(Integer.class, new Integer("1")));
		assertEquals(new Integer("1"), getConverter().convert(int.class, new Integer("1")));
		
		assertEquals(new Long("1"), getConverter().convert(Long.class, new Long("1")));
		assertEquals(new Long("1"), getConverter().convert(long.class, new Long("1")));
		
		assertEquals(new Float("1"), getConverter().convert(Float.class, new Float("1")));
		assertEquals(new Float("1"), getConverter().convert(float.class, new Float("1")));
		
		assertEquals(new Double("1"), getConverter().convert(Double.class, new Double("1")));
		assertEquals(new Double("1"), getConverter().convert(double.class, new Double("1")));

	}

	public List createDestinationClasses() throws Exception {
		return null;
	}

	protected Transformer createTransformer() {
		return new PrimitiveWrapperConverter();
	}

}
