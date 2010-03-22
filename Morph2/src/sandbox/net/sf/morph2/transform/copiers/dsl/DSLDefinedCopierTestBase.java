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

import net.sf.morph2.transform.DecoratedCopier;
import net.sf.morph2.transform.Transformer;
import net.sf.morph2.transform.converters.BaseConverterTestCase;
import net.sf.morph2.transform.transformers.SimpleDelegatingTransformer;

public abstract class DSLDefinedCopierTestBase extends BaseConverterTestCase {
	protected DecoratedCopier copier;

	/* (non-Javadoc)
	 * @see net.sf.morph2.transform.converters.BaseConverterTestCase#setUp()
	 */
	protected void setUp() throws Exception {
		super.setUp();
		this.copier = (DecoratedCopier) transformer;
	}

	protected Transformer createTransformer() {
		DSLDefinedCopier dslDefinedCopier = new DSLDefinedCopier(getClass().getResourceAsStream(getSource()));
		SimpleDelegatingTransformer result = new SimpleDelegatingTransformer(new Transformer[] { dslDefinedCopier }, true);
		return result;
	}

	protected abstract String getSource();

	/* (non-Javadoc)
	 * @see net.sf.morph2.transform.converters.BaseConverterTestCase#createInvalidDestinationClasses()
	 */
	public List createInvalidDestinationClasses() throws Exception {
		return null;
	}

	/* (non-Javadoc)
	 * @see net.sf.morph2.transform.converters.BaseConverterTestCase#createInvalidSources()
	 */
	public List createInvalidSources() throws Exception {
		return null;
	}

	/* (non-Javadoc)
	 * @see net.sf.morph2.transform.converters.BaseConverterTestCase#createInvalidSourceClasses()
	 */
	public List createInvalidSourceClasses() throws Exception {
		return super.createInvalidSourceClasses();
	}
}
