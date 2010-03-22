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
package net.sf.morph2.util;
import java.io.PrintStream;
import java.io.PrintWriter;

/**
 * A nestable checked exception. Some documentation was copied directly from the
 * documentation for the <a
 * href="http://java.sun.com/j2se/1.4.1/docs/api/java/lang/Throwable.html">Throwable
 * </a> class in the Java SE 1.4 API.
 * 
 * @author Matthew Sgarlata
 * @since November 6, 2004
 */
public abstract class NestableCheckedException extends Exception {
	private static final String NESTING_MESSAGE = "Nested Exception: ";

	private Throwable cause;

	/**
	 * Constructs a new exception with null as its detail message. The cause is
	 * not initialized, and may subsequently be initialized by a call to
	 * {@link net.sf.morph2.util.NestableCheckedException#initCause(Throwable)}.
	 */
	public NestableCheckedException() {
		super();
	}

	/**
	 * Constructs a new exception with the specified detail message. The cause
	 * is not initialized, and may subsequently be initialized by a call to
	 * {@link NestableCheckedException#initCause(Throwable)}.
	 * 
	 * @param message
	 *            the detail message. The detail message is saved for later
	 *            retrieval by the {@link NestableCheckedException#getCause()}
	 *            method.
	 */
	public NestableCheckedException(String message) {
		super(message);
	}

	/**
	 * Constructs a new exception with the specified detail message and cause.
	 * <br>
	 * <br>
	 * Note that the detail message associated with cause is not automatically
	 * incorporated in this exception's detail message.
	 * 
	 * @param message
	 *            the detail message (which is saved for later retrieval by the
	 *            {@link java.lang.Throwable#getMessage()}method).
	 * @param cause
	 *            the cause (which is saved for later retrieval by the
	 *            {@link NestableCheckedException#getCause()}method). (A
	 *            <code>null</code> value is permitted, and indicates that the
	 *            cause is nonexistent or unknown.)
	 */
	public NestableCheckedException(String message, Throwable cause) {
		super(message);
		this.cause = cause;
	}

	/**
	 * Constructs a new exception with the specified cause and a detail message
	 * of <code>(cause==null ? null : cause.toString())</code> (which
	 * typically contains the class and detail message of cause). This
	 * constructor is useful for exceptions that are little more than wrappers
	 * for other throwables (for example, <a
	 * href="http://java.sun.com/j2se/1.4.1/docs/api/java/security/PrivilegedActionException.html">PrivilegedActionException
	 * </a>).
	 * 
	 * @param cause
	 *            the cause (which is saved for later retrieval by the
	 *            {@link NestableCheckedException#getCause()}method). (A
	 *            <code>null</code> value is permitted, and indicates that the
	 *            cause is nonexistent or unknown.)
	 */
	public NestableCheckedException(Throwable cause) {
		super();
		this.cause = cause;
	}

	/**
	 * Initializes the cause of this throwable to the specified value. (The
	 * cause is the throwable that caused this throwable to get thrown.) <br>
	 * <br>
	 * This method can be called at most once. It is generally called from
	 * within the constructor, or immediately after creating the throwable. If
	 * this throwable was created with Throwable(Throwable) or
	 * Throwable(String,Throwable), this method cannot be called even once.
	 * 
	 * @param cause
	 *            the cause (which is saved for later retrieval by the
	 *            {@link NestableCheckedException#getCause()}method). (A
	 *            <code>null</code> value is permitted, and indicates that the
	 *            cause is nonexistent or unknown.)
	 */
	public void setCause(Throwable cause) {
		this.cause = cause;
	}

	public Throwable getCause() {
		return cause;
	}
	public void getMessage(StringBuffer sb) {
		sb.append(super.getMessage());
		sb.append("\n");
		if (cause != null) {
			sb.append(NESTING_MESSAGE);
			if (cause instanceof NestableCheckedException) {
				NestableCheckedException chainedCause = (NestableCheckedException) cause;
				chainedCause.getMessage(sb);
			} else {
				sb.append(cause.getMessage());
			}
		}
	}

	public void printStackTrace() {
		super.printStackTrace();

		Throwable t = getCause();
		if (t!=null) {
			System.err.println(NESTING_MESSAGE);
			t.printStackTrace();
		}
	}
	
	/**
	 * @see java.lang.Throwable#printStackTrace(java.io.PrintStream)
	 */
	public void printStackTrace(PrintStream out) {
		super.printStackTrace(out);

		Throwable t = getCause();
		if (t!=null) {
			out.println(NESTING_MESSAGE);
			t.printStackTrace(out);
		}
	}

	/**
	 * @see java.lang.Throwable#printStackTrace(java.io.PrintWriter)
	 */
	public void printStackTrace(PrintWriter out) {
		super.printStackTrace(out);

		Throwable t = getCause();
		if (t!=null) {
			out.println(NESTING_MESSAGE);
			t.printStackTrace(out);
		}
	}	


}
