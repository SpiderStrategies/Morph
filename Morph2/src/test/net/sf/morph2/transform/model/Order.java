package net.sf.morph2.transform.model;

public interface Order {
	LineItem[] getLineItems();
	void setLineItems(LineItem[] lineItems);
}
