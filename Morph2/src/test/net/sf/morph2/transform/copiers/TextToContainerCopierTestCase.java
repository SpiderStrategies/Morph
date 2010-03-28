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
package net.sf.morph2.transform.copiers;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.TreeMap;

import net.sf.morph2.transform.Transformer;

/**
 * @author Matt Sgarlata
 * @since Apr 9, 2007
 */
public class TextToContainerCopierTestCase extends BaseCopierTestCase {

	protected Transformer createTransformer() {
		return new TextToContainerCopier();
	}
	
	protected TextToContainerCopier getTextToContainerCopier() {
		return (TextToContainerCopier) getTransformer();
	}
	
	public void testRemoveIgnoredCharacters() {
		TextToContainerCopier copier = getTextToContainerCopier();
		
		// test edge cases
		assertNull(copier.removeIgnoredCharacters(null, null));
		assertEquals("", copier.removeIgnoredCharacters("", ""));
		assertEquals("", copier.removeIgnoredCharacters("", null));
		assertEquals("hi", copier.removeIgnoredCharacters("hi", null));
		assertEquals("hi", copier.removeIgnoredCharacters("hi", ""));

		// now really test the functionality
		assertEquals("testing 123",
			copier.removeIgnoredCharacters("test(ing) 123",
			TextToContainerCopier.DEFAULT_IGNORED_CHARACTERS));
		assertEquals("h", copier.removeIgnoredCharacters("hi", "i"));
	}

	public List createDestinationClasses() throws Exception {
	    List list = new ArrayList();
	    list.add(Iterator.class);
	    list.add(Enumeration.class);
	    list.add(char[].class);
	    list.add(Integer[].class);
	    list.add(Collection.class);
	    list.add(TreeMap.class);
	    return list;
    }

	public List createInvalidDestinationClasses() throws Exception {
	    List list = new ArrayList();
	    list.add(Integer.class);
	    list.add(Object.class);
	    list.add(null);
	    return list;
    }

	public List createInvalidSources() throws Exception {
		// this converter can actually handle just about any input, it will
		// just put the String representation of whatever the input is into a
		// collection
	    return null;
    }

	public List createValidPairs() throws Exception {
	    List list = new ArrayList();	    
	    list.add(new ConvertedSourcePair(new int[] { 1, 2 }, "1,2"));
	    list.add(new ConvertedSourcePair(new int[] { 1, 2 }, new StringBuffer("1, 2")));
	    list.add(new ConvertedSourcePair(null, null));
	    list.add(new ConvertedSourcePair(new String[] {"1", "2"}, "1,2"));
	    return list;
    }
	
	public void testDelimiterConfig() {
	    getTextToContainerCopier().setDelimiters("| ");
	    double[] results =
	    	(double[]) getConverter().convert(double[].class, "1.7|1,234.56");
	    assertEquals(1.7, results[0], 0);
	    assertEquals(1234.56, results[1], 0);
	    getTextToContainerCopier().setDelimiters(TextToContainerCopier.DEFAULT_DELIMITERS);
	}
	
	public void testCopy() {
		List destination = new ArrayList();
		destination.add("1");
		destination.add("2");
		String source = "three,four";
		getCopier().copy(destination, source);
		assertEquals(4, destination.size());
		assertEquals("1", destination.get(0));
		assertEquals("2", destination.get(1));
		// with a list these items get copied onto the end
		assertEquals("three", destination.get(2));
		assertEquals("four", destination.get(3));
		
		String[] destination2 = new String[] { "replace", "here" };
		getCopier().copy(destination2, source);
		// with an array, the original array elements get replaced
		assertEquals("three", destination2[0]);
		assertEquals("four", destination2[1]);
	}

}
