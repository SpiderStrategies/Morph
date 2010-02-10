package net.sf.morph.transform.model;

import net.sf.composite.util.ObjectUtils;

public class AssembledOrder {
	private Person person;
	private String text;
	private LineItem[] lineItems;

	public Person getPerson() {
		return person;
	}

	public String getText() {
		return text;
	}

	public void setPerson(Person person) {
		this.person = person;
	}

	public void setText(String text) {
		this.text = text;
	}

	public LineItem[] getLineItems() {
		return lineItems;
	}

	public void setLineItems(LineItem[] lineItems) {
		this.lineItems = lineItems;
	}

	public boolean equals(Object o) {
		if (o == this) {
			return true;
		}
		if (!(o instanceof AssembledOrder)) {
			return false;
		}
		AssembledOrder oao = (AssembledOrder) o;
		return ObjectUtils.equals(oao.person, person)
				&& ObjectUtils.equals(oao.text, text)
				&& ObjectUtils.equals(oao.lineItems, lineItems);
	}
}