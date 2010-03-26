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
package net.sf.morph2.transform.transformers;

import junit.framework.TestCase;
import net.sf.composite.util.ObjectUtils;
import net.sf.morph2.transform.DecoratedConverter;
import net.sf.morph2.transform.DecoratedCopier;
import net.sf.morph2.transform.Transformer;

/**
 * @author Matt Sgarlata
 * @since Dec 5, 2004
 */
public abstract class BaseTransformerTestCase extends TestCase {
	
	protected Transformer transformer;
	
	public BaseTransformerTestCase() {
		super();
	}
	public BaseTransformerTestCase(String arg0) {
		super(arg0);
	}
	
	protected void setUp() throws Exception {
		super.setUp();
		transformer = createTransformer();
	}
	
	protected abstract Transformer createTransformer();

	public void testGetSourceClasses() {
		assertFalse("sourceClasses should not be empty",
			ObjectUtils.isEmpty(getTransformer().getSourceClasses()));
	}
	
	public void testGetDestinationClasses() {
		assertFalse("destinationClasses should not be empty",
			ObjectUtils.isEmpty(getTransformer().getDestinationClasses()));
	}
	
	public DecoratedConverter getConverter() {
		return (DecoratedConverter) transformer;
	}
	
	public DecoratedCopier getCopier() {
		return (DecoratedCopier) transformer;
	}
	
	/**
	 * @return Returns the transformer.
	 */
	public Transformer getTransformer() {
		return transformer;
	}
	/**
	 * @param transformer The transformer to set.
	 */
	public void setTransformer(Transformer transformer) {
		this.transformer = transformer;
	}
	
}
