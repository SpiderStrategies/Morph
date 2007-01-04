/**
 * 
 */
package net.sf.morph.transform.model;


public class AddressTo implements Address {
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