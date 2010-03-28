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
package net.sf.morph2.transform.converters.toboolean;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import net.sf.morph2.transform.Transformer;
import net.sf.morph2.transform.converters.ObjectToBooleanConverter;


/**
 * Creates a boolean converter with object conversions turned on and
 * number conversions turned off.
 * 
 * @author Matt Sgarlata
 * @since Dec 30, 2004
 */
public class ObjectToBooleanConverterTestCase extends BaseToBooleanConverterTestCase {

	public List createInvalidSources() throws Exception {
		return null;
	}

	protected Transformer createTransformer() {
		return new ObjectToBooleanConverter();			
	}
	
	public List createValidPairs() throws Exception {
		List list = new ArrayList();
		
		addObjectConversions(list);

		list.add(new ConvertedSourcePair(Boolean.TRUE, "something"));
		list.add(new ConvertedSourcePair(Boolean.FALSE, ""));
		list.add(new ConvertedSourcePair(Boolean.FALSE, new StringBuffer("")));		
		
		// make sure all non-null numbers are converted to false
		list.add(new ConvertedSourcePair(Boolean.TRUE, new Integer("0")));
		list.add(new ConvertedSourcePair(Boolean.TRUE, new Short("0")));
		list.add(new ConvertedSourcePair(Boolean.TRUE, new Byte("0")));
		list.add(new ConvertedSourcePair(Boolean.TRUE, new Float("0.0")));
		list.add(new ConvertedSourcePair(Boolean.TRUE, new Double("0.0")));
		list.add(new ConvertedSourcePair(Boolean.TRUE, new BigDecimal("0.0")));
		list.add(new ConvertedSourcePair(Boolean.TRUE, new BigInteger("0")));
		list.add(new ConvertedSourcePair(Boolean.TRUE, new Integer("1")));
		list.add(new ConvertedSourcePair(Boolean.TRUE, new Double("0.000001")));
		list.add(new ConvertedSourcePair(Boolean.TRUE, new BigDecimal("0.000001")));
		list.add(new ConvertedSourcePair(Boolean.TRUE, new BigInteger("-1")));
		list.add(new ConvertedSourcePair(Boolean.TRUE, new Long("-13214124")));
		
		return list;
	}
}
