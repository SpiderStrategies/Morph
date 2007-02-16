package net.sf.morph.transform.copiers;

import java.util.ArrayList;

import net.sf.morph.transform.NodeCopier;
import net.sf.morph.transform.Transformer;
import net.sf.morph.transform.transformers.SimpleDelegatingTransformer;
import net.sf.morph.util.ProxyUtils;

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
		others.add((Transformer) ProxyUtils.getProxy(child, NodeCopier.class));
		SimpleDelegatingTransformer result = new SimpleDelegatingTransformer();
		result.setComponents(others.toArray(new Transformer[others.size()]));
		return result;
	}
}
