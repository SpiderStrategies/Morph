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
package net.sf.morph.transform.converters.toboolean;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;

import net.sf.morph.transform.Transformer;
import net.sf.morph.transform.converters.NumberToBooleanConverter;


/**
 * Creates a boolean converter with number conversions turned on and object
 * conversions turned off.
 * 
 * @author Matt Sgarlata
 * @since Dec 30, 2004
 */
public class NumberToBooleanConverterTestCase extends BaseToBooleanConverterTestCase {

	public List createInvalidSources() throws Exception {
		List list = new ArrayList();
		list.add(new HashMap());
		list.add(new Hashtable());
		list.add(this);
		list.add(new StringBuffer("13z"));
		return list;  
	}

	protected Transformer createTransformer() {
		return new NumberToBooleanConverter();
	}
	
	public List createValidPairs() throws Exception {
		List list = new ArrayList();
		addNumberConversions(list);
		return list;
	}

}