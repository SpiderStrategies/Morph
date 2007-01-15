package net.sf.morph.transform.model;

public class LineItemImpl implements LineItem {
	private int quantity;
	private String itemId;

	public String getItemId() {
		return itemId;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setItemId(String itemId) {
		this.itemId = itemId;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

}
