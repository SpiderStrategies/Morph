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

import java.util.ArrayList;

import net.sf.morph2.transform.NodeCopier;
import net.sf.morph2.transform.Transformer;
import net.sf.morph2.transform.transformers.SimpleDelegatingTransformer;
import net.sf.morph2.util.ProxyUtils;

public class NestedDelegatingCopierTestCase extends DelegatingCopierTestCase {

	protected Transformer createTransformer() {
		SimpleDelegatingTransformer child = new SimpleDelegatingTransformer();
		Transformer[] t = (Transformer[]) child.getComponents();
		ArrayList nodeCopiers = new ArrayList();
		ArrayList others = new ArrayList();
		for (int i = 0; i < t.length; i++) {
			(t[i] instanceof NodeCopier ? nodeCopiers : others).add(t[i]);
		}
		child.setComponents(nodeCopiers.toArray(new Transformer[nodeCopiers.size()]));
		others.add(ProxyUtils.getProxy(child, NodeCopier.class));
		SimpleDelegatingTransformer result = new SimpleDelegatingTransformer();
		result.setComponents(others.toArray(new Transformer[others.size()]));
		return result;
	}
}
