/*
 * Copyright 2007 the original author or authors.
 * 
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
package net.sf.morph2.transform.copiers.dsl;

import java.util.List;

import net.sf.composite.util.ObjectUtils;
import net.sf.morph2.transform.Copier;
import net.sf.morph2.transform.copiers.CumulativeCopier;
import net.sf.morph2.transform.transformers.BaseCompositeTransformer;
import net.sf.morph2.transform.transformers.SimpleDelegatingTransformer;

class CompositeCopierUtils {
	public interface CompositeFactory {
		abstract BaseCompositeTransformer get();
	}

	public static final CompositeFactory CUMULATIVE = new CompositeFactory() {
		public BaseCompositeTransformer get() {
			return new CumulativeCopier();
		}
	};

	public static final CompositeFactory DELEGATE = new CompositeFactory() {
		public BaseCompositeTransformer get() {
			return new SimpleDelegatingTransformer();
		};
	};

	public static Copier composite(List copiers, CompositeFactory factory) {
		if (ObjectUtils.isEmpty(copiers)) {
			return null;
		}
		if (copiers.size() == 1) {
			return (Copier) copiers.get(0);
		}
		BaseCompositeTransformer result = factory.get();
		result.setComponents(copiers.toArray(new Copier[copiers.size()]));
		return (Copier) result;
	}

}
