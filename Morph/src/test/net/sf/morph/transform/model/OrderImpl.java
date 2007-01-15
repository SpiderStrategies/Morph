package net.sf.morph.transform.model;

public class OrderImpl implements Order {
	private LineItem[] lineItems;

	public LineItem[] getLineItems() {
		return lineItems;
	}

	public void setLineItems(LineItem[] lineItems) {
		this.lineItems = lineItems;
	}

}
