package net.sf.morph.util;

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
	protected String str;
	protected String delim;

	public MorphStringTokenizer(String str, String delim, boolean returnDelims) {
	    super(str, delim, returnDelims);
	    this.str = str;
	    this.delim = delim;
	}

	public MorphStringTokenizer(String str, String delim) {
	    super(str, delim);
	    this.str = str;
	    this.delim = delim;
    }

	public MorphStringTokenizer(String str) {
	    super(str);
	    this.str = str;
	    // copied from implementation of this method in superclass
	    this.delim = " \t\n\r\f";
    }

	public String toString() {
		return "MorphStringTokenizer[str=\"" + str + "\",delims=\"" + delim + "\"]";
	}
	
}
