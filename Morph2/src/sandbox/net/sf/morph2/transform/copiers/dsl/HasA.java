package net.sf.morph2.transform.copiers.dsl;

import org.apache.commons.lang.builder.ToStringBuilder;

/**
 *
 */
public class HasA {
	private A a;

	public HasA() {
	}

	public HasA(A a) {
		setA(a);
	}

	/**
	 * Get the a of this HasA.
	 * @return the a
	 */
	public synchronized A getA() {
		return a;
	}

	/**
	 * Set the a of this HasA.
	 * @param a the a to set
	 */
	public synchronized void setA(A a) {
		this.a = a;
	}

	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}
}
