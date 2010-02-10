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
package net.sf.morph;

import java.text.NumberFormat;
import java.util.Locale;

/**
 * An informal test outside of JUnit that compares the speed of Morph to doing
 * primitive conversions manually.
 * 
 * @author Matt Sgarlata
 * @since Feb 2, 2006
 */
public class SpeedComparison {
	
	private static final int ITERATIONS = 10000; // 10,000

	public static void main(String args[]) {
		Double d = new Double(123456.789d);
		Locale locale = Locale.getDefault();
		NumberFormat format = NumberFormat.getNumberInstance(locale);
		
		// do conversion once outside the speed test because we don't care about
		// startup speed really
		Morph.convertToString(d, locale);
		format.format(d);
		
		long start = System.currentTimeMillis();
		for (int i=0; i<ITERATIONS; i++	) {
			Morph.convertToString(d, locale);
		}
		long stop = System.currentTimeMillis();
		System.out.println("Double -> String, Morph  = " + (stop - start) + " ms");
		
		start = System.currentTimeMillis();
		for (int i=0; i<ITERATIONS; i++) {
			format.format(d);
		}
		stop = System.currentTimeMillis();
		System.out.println("Double -> String, Manual = " + (stop - start) + " ms");
	}
	
}