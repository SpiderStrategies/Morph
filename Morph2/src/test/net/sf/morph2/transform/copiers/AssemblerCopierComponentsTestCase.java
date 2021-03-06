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
package net.sf.morph2.transform.copiers;

import java.util.Collections;

import net.sf.morph2.transform.Transformer;

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
