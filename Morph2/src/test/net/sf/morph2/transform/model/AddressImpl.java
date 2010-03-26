/**
 * 
 */
package net.sf.morph2.transform.model;


public class AddressImpl implements Address {
	private String text;
	private Person person;
	public Person getPerson() {
		return person;
	}
	public void setPerson(Person person) {
		this.person = person;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	public String toString() {
		return text + " (address for " + person + ")";
	}
}
