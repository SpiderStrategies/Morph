/*
 * Copyright 2004-2005, 2007 the original author or authors.
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
package net.sf.morph.transform.copiers;

import net.sf.morph.transform.converters.BaseConverterTestCase;

/**
 * @author Matt Sgarlata
 * @since Dec 11, 2004
 */
public abstract class BaseCopierTestCase extends BaseConverterTestCase {

	public BaseCopierTestCase() {
		super();
	}
	public BaseCopierTestCase(String arg0) {
		super(arg0);
	}
	public void testCopyArguments() {
		/* WE ARE NOW ALLOWING NULL AS COPY SOURCE */
	}

}
