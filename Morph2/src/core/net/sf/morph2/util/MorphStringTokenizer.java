/*
 * Copyright 2007, 2010 the original author or authors.
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
package net.sf.morph2.util;

import java.util.StringTokenizer;

/**
 * Same as a regular StringTokenizer, but with a legible {@link #toString()}
 * method.
 * 
 * @author Matt Sgarlata
 * @since Apr 9, 2007
 */
public class MorphStringTokenizer extends StringTokenizer {

	// have to redeclare these since they are private in the superclass
	/** String to tokenize */
	protected String str;
	/** Delimiter(s) */
	protected String delim;

	/**
	 * Create a new MorphStringTokenizer instance.
	 * @param str
	 * @param delim
	 * @param returnDelims
	 */
	public MorphStringTokenizer(String str, String delim, boolean returnDelims) {
		super(str, delim, returnDelims);
		this.str = str;
		this.delim = delim;
	}

	/**
	 * Create a new MorphStringTokenizer instance.
	 * @param str
	 * @param delim
	 */
	public MorphStringTokenizer(String str, String delim) {
		super(str, delim);
		this.str = str;
		this.delim = delim;
	}

	/**
	 * Create a new MorphStringTokenizer instance.
	 * @param str
	 */
	public MorphStringTokenizer(String str) {
		super(str);
		this.str = str;
		// copied from implementation of this method in superclass
		this.delim = " \t\n\r\f";
	}

	/**
	 * {@inheritDoc}
	 */
	public String toString() {
		return "MorphStringTokenizer[str=\"" + str + "\",delims=\"" + delim + "\"]";
	}

}
