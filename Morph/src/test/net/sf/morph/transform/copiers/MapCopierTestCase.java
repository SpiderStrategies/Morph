package net.sf.morph.transform.copiers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.morph.transform.Transformer;

public class MapCopierTestCase extends BaseCopierTestCase {
	protected Transformer createTransformer() {
		return new MapCopier();
	}

	public List createInvalidSources() throws Exception {
		return null;
	}

	public List createInvalidDestinationClasses() throws Exception {
		return Arrays.asList(new Class[] {
				Object.class, Integer.class, int.class, List.class, null });
	}

	public List createValidPairs() throws Exception {
		HashMap m = new HashMap();
		m.put("foo", "fooVal");
		m.put(new Integer(0), "zeroVal");
		m.put(new Object(), "objectVal");
		m.put(Boolean.TRUE, "trueVal");
		m.put(Boolean.FALSE, "falseVal");
		HashMap clone = (HashMap) m.clone();
		ArrayList result = new ArrayList();
		result.add(new ConvertedSourcePair(clone, m));
		result.add(new ConvertedSourcePair(new HashMap(), new HashMap()));
		return result;
	}

	public List createDestinationClasses() throws Exception {
		return Collections.singletonList(Map.class);
	}
}
