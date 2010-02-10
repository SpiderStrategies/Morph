package net.sf.morph.transform.converters.totext;

import java.util.ArrayList;
import java.util.List;

import net.sf.morph.transform.Transformer;
import net.sf.morph.transform.converters.ObjectToPrettyTextConverter;

public class ObjectToPrettyTextConverterTestCase extends BaseToTextConverterTestCase {

	public List createValidPairs() throws Exception {
		List list = new ArrayList();
		addContainerConversions(list);
		return list;
	}

	public List createInvalidSources() throws Exception {
		return null;
	}

	protected Transformer createTransformer() {
		return new ObjectToPrettyTextConverter();
	}

}
