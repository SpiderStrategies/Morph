package net.sf.morph2.transform.copiers.dsl;

import java.util.Map;

import org.apache.commons.lang.builder.ToStringBuilder;

public class B {
	private char[] foo;
	private String bar;
	private Object baz;
	private String stringB;
	private int intB;
	private Object objectB;
	private Map map;

	/**
	 * Get the bar of this B.
	 * @return the bar
	 */
	public String getBar() {
		return bar;
	}

	/**
	 * Set the bar of this B.
	 * @param bar the bar to set
	 */
	public void setBar(String bar) {
		this.bar = bar;
	}

	/**
	 * Get the baz of this B.
	 * @return the baz
	 */
	public Object getBaz() {
		return baz;
	}

	/**
	 * Set the baz of this B.
	 * @param baz the baz to set
	 */
	public void setBaz(Object baz) {
		this.baz = baz;
	}

	/**
	 * Get the foo of this B.
	 * @return the foo
	 */
	public char[] getFoo() {
		return foo;
	}

	/**
	 * Set the foo of this B.
	 * @param foo the foo to set
	 */
	public void setFoo(char[] foo) {
		this.foo = foo;
	}

	/**
	 * Get the intB of this B.
	 * @return the intB
	 */
	public int getIntB() {
		return intB;
	}

	/**
	 * Set the intB of this B.
	 * @param intB the intB to set
	 */
	public void setIntB(int intB) {
		this.intB = intB;
	}

	/**
	 * Get the objectB of this B.
	 * @return the objectB
	 */
	public Object getObjectB() {
		return objectB;
	}

	/**
	 * Set the objectB of this B.
	 * @param objectB the objectB to set
	 */
	public void setObjectB(Object objectB) {
		this.objectB = objectB;
	}

	/**
	 * Get the stringB of this B.
	 * @return the stringB
	 */
	public String getStringB() {
		return stringB;
	}

	/**
	 * Set the stringB of this B.
	 * @param stringB the stringB to set
	 */
	public void setStringB(String stringB) {
		this.stringB = stringB;
	}

	/**
	 * Get the map of this B.
	 * @return the map
	 */
	public synchronized Map getMap() {
		return map;
	}

	/**
	 * Set the map of this B.
	 * @param map the map to set
	 */
	public synchronized void setMap(Map map) {
		this.map = map;
	}

	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}

}
