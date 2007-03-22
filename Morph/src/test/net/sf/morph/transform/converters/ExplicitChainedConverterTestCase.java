package net.sf.morph.transform.converters;

import java.util.Locale;

import net.sf.morph.transform.Converter;
import net.sf.morph.transform.converters.ChainedConverter;
import net.sf.morph.transform.transformers.BaseTransformer;
import net.sf.morph.util.*;

import junit.framework.TestCase;

public class ExplicitChainedConverterTestCase extends TestCase {
	public static class A {
	}

	public static class B {
	}

	public static class C {
	}

	public static class D {
	}

	public static class E {
	}

	private class TestConverter extends BaseTransformer implements Converter {
		private TypeMap typeMap;

		TestConverter(TypeMap typeMap) {
			this.typeMap = typeMap;
		}

		protected boolean isTransformableImpl(Class dest, Class source) {
			return TransformerUtils.getMappedDestinationType(typeMap, source) == dest;
		}

		protected Class[] getSourceClassesImpl() {
			return (Class[]) typeMap.keySet().toArray(new Class[typeMap.size()]);
		}

		protected Class[] getDestinationClassesImpl() {
			return (Class[]) typeMap.values().toArray(new Class[typeMap.size()]);
		}

		protected Object convertImpl(Class dest, Object source, Locale loc) throws Exception {
			if (!isTransformable(dest, source.getClass())) {
				throw new Exception("can't transform " + source + " to " + dest);
			}
			return ClassUtils.newInstance(dest);
		}
	}

	private ChainedConverter cc = new ChainedConverter();

	{
		cc.setComponents(new Converter[] {

		new TestConverter(new TypeMap() {
			{
				put(A.class, B.class);
				put(B.class, C.class);
			}
		}),

		new TestConverter(new TypeMap() {
			{
				put(B.class, C.class);
				put(D.class, E.class);
			}
		}) });
	}

	public void testAToCIsTransformable() throws Exception {
		assertTrue("A should be transformable to C", cc.isTransformable(C.class, A.class));
	}

	public void testAToC() throws Exception {
		assertTrue(cc.isTransformable(C.class, A.class));
		assertTrue(cc.convert(C.class, new A()) instanceof C);
	}

	public void testAToEIsTransformable() throws Exception {
		assertFalse("A should not be transformable to E", cc.isTransformable(E.class, A.class));
	}

	public void testAToE() throws Exception {
		try {
			cc.convert(E.class, new A());
			fail("should fail");
		} catch (Exception e) {
		}
	}
}
