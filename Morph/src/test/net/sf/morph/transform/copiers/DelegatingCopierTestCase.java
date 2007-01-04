package net.sf.morph.transform.copiers;

import java.util.ArrayList;
import java.util.List;

import net.sf.morph.Defaults;
import net.sf.morph.transform.Transformer;

/**
 * @author Matt Sgarlata
 * @since Feb 22, 2005
 */
public class DelegatingCopierTestCase extends BaseCopierTestCase {

	protected Transformer createTransformer() {
		return Defaults.createCopier();
	}

	public List createInvalidDestinationClasses() throws Exception {
		return null;
	}

	public List createInvalidSources() throws Exception {
		List list = new ArrayList();
		list.add(null);
		return list;
	}

	public List createValidPairs() throws Exception {
		return null;
	}

	public List createDestinationClasses() throws Exception {
		return null;
	}

}