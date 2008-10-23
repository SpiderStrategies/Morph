/*
 * Copyright 2008 the original author or authors.
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
package net.sf.morph.transform;

/**
 * Defines access to a <code>nestedTransformer</code> property for
 * {@link Transformer}s that can/should operate in a graph.  This interface
 * has been extracted from {@link NodeCopier} because a {@link Converter}
 * (as opposed to a {@link Copier}) may need to perform nested operations.
 * 
 * @author Matt Sgarlata
 * @author Matt Benson
 * @since Morph 1.1.2
 */
public interface NestingAwareTransformer extends Transformer {

	/**
	 * Retrieves the transformer used to perform nested transformations.
	 * 
	 * @return the transformer used to perform nested transformations
	 */
	public Transformer getNestedTransformer();

	/**
	 * Sets the transformer used to perform nested transformations.
	 * 
	 * @param nestedTransformer
	 *            the transformer used to perform nested transformations
	 */
	public void setNestedTransformer(Transformer nestedTransformer);

}