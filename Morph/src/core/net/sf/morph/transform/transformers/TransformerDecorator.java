/*
 * Copyright 2004-2005 the original author or authors.
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
package net.sf.morph.transform.transformers;

import net.sf.morph.transform.DecoratedTransformer;
import net.sf.morph.transform.Transformer;

/**
 * Decorates any Transformer so that it implements DecoratedTransformer.  Example usage:
 * 
 * <pre>
 * Transformer myTransformer = new MyTransformer();
 * DecoratedTransformer decoratedTransformer = new DecoratedTransformer(myTransformer);
 * 
 * // now use decoratedTransformer instead of myTransformer
 * if (decoratedTransformer.isTransformable(destinationType, sourceType)) {
 *     ...
 * }
 * </pre>
 * 
 * @author Matt Sgarlata
 * @since Dec 5, 2004
 */
public class TransformerDecorator extends BaseTransformer implements DecoratedTransformer {
	
	private Transformer transformer;
	
	public TransformerDecorator() { }
		
	public TransformerDecorator(Transformer transformer) {
		this();
		this.transformer = transformer;
	}

	public Class[] getSourceClassesImpl() {
		return transformer.getSourceClasses();
	}

	public Class[] getDestinationClassesImpl() {
		return transformer.getDestinationClasses();
	}

	protected boolean isWrappingRuntimeExceptions() {
	    // the whole point of this converter is for decorating user defined
		// transformers, so we don't want to eat their exceptions ;)
	    return false;
    }

	/**
	 * @return Returns the transformer.
	 */
	public Transformer getTransformer() {
		return transformer;
	}

	/**
	 * @param transformer
	 *            The transformer to set.
	 */
	public void setTransformer(Transformer transformer) {
		this.transformer = transformer;
	}
}