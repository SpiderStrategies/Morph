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

import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;

import net.sf.morph2.transform.Transformer;
import net.sf.morph2.transform.converters.BaseConverterTestCase;
import net.sf.morph2.util.MorphStringTokenizer;

/**
 * 
 * @author Matt Sgarlata
 * @since Dec 8, 2005
 */
public class SimpleDelegatingTransformerTestCase extends BaseConverterTestCase {

	protected Transformer createTransformer() {
		return new SimpleDelegatingTransformer();
	}

	public List createDestinationClasses() throws Exception {
		List destinationClasses = new ArrayList();
		destinationClasses.add(Object.class);
		destinationClasses.add(Collection.class);
		destinationClasses.add(Iterator.class);
		destinationClasses.add(Enumeration.class);
		destinationClasses.add(String.class);
		destinationClasses.add(Number.class);
		destinationClasses.add(boolean.class);
		destinationClasses.add(char.class);
		destinationClasses.add(byte.class);
		destinationClasses.add(short.class);
		destinationClasses.add(int.class);
		destinationClasses.add(long.class);
		destinationClasses.add(double.class);
		destinationClasses.add(float.class);
		destinationClasses.add(Object[].class);
		destinationClasses.add(boolean[].class);
		destinationClasses.add(char[].class);
		destinationClasses.add(byte[].class);
		destinationClasses.add(short[].class);
		destinationClasses.add(int[].class);
		destinationClasses.add(long[].class);
		destinationClasses.add(double[].class);
		destinationClasses.add(float[].class);
		return destinationClasses;
	}

	public List createInvalidDestinationClasses() throws Exception {
		return null;
	}

	public List createInvalidSources() throws Exception {
		return null;
	}

	public List createValidPairs() throws Exception {
		List pairs = new ArrayList();
		
		Integer[] intArray = new Integer[] { new Integer(123), new Integer(456) };
		Enumeration e = new MorphStringTokenizer("123,456", ",");
		pairs.add(new ConvertedSourcePair(intArray, e));
		
		return pairs;
	}
	
	

}
