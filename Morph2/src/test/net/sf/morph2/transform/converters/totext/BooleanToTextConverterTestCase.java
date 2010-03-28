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
package net.sf.morph2.transform.converters.totext;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;

import net.sf.morph2.transform.Transformer;
import net.sf.morph2.transform.converters.BooleanToTextConverter;

/**
 * TODO tests here should be preserved, but moved to other test cases
 * 
 * @author Matt Sgarlata
 * @since Dec 29, 2004
 */
public class BooleanToTextConverterTestCase extends BaseToTextConverterTestCase {

	public BooleanToTextConverterTestCase() {
		super();
	}
	public BooleanToTextConverterTestCase(String arg0) {
		super(arg0);
	}
	
	public List createInvalidSources() throws Exception {
		List list = new ArrayList();
		list.add(new HashMap());
		list.add(new Hashtable());
		list.add(this);
		return list;
	}

	public List createValidPairs() throws Exception {
		List list = new ArrayList();
		addBooleanConversions(list);
		return list;
	}

	public List createDestinationClasses() throws Exception {
		List list = new ArrayList();
		list.add(Character.class);
		list.add(Character.TYPE);
		list.add(char.class);
		return list;
	}

	protected Transformer createTransformer() {
		return new BooleanToTextConverter();
	}

}
