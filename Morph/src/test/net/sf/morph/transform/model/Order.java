package net.sf.morph.transform.model;

public interface Order {
	LineItem[] getLineItems();
	void setLineItems(LineItem[] lineItems);
}
