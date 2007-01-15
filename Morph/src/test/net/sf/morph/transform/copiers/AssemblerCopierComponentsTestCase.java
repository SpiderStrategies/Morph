package net.sf.morph.transform.copiers;

import java.util.Collections;

import net.sf.morph.transform.Transformer;

/**
 * Test the AssemblerCopier with component copiers assigned.
 */
public class AssemblerCopierComponentsTestCase extends AssemblerCopierTestCase {
	public Transformer getTransformer() {
		AssemblerCopier result = new AssemblerCopier();
		Object[] components = new Object[2];
		components[0] = new PropertyNameMatchingCopier();
		PropertyNameMappingCopier mapper = new PropertyNameMappingCopier();
		mapper.setMapping(Collections.singletonMap("lineItems", "lineItems"));
		components[1] = mapper;
		result.setComponents(components);
		return result;
	}
}
