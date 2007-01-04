package net.sf.morph.wrap;

import net.sf.morph.MorphException;

/**
 * @author Matt Sgarlata
 * @since Jan 31, 2005
 */
public class WrapperException extends MorphException {

	public WrapperException() {
		super();
	}
	public WrapperException(String message) {
		super(message);
	}
	public WrapperException(String message, Throwable cause) {
		super(message, cause);
	}
	public WrapperException(Throwable cause) {
		super(cause);
	}
}
