/**
 * 
 */
package net.sf.morph2.transform.model;


public class CustomerImpl extends PersonImpl implements Customer {
	String customerNumber;
	public String getCustomerNumber() {
		return customerNumber;
	}
	public void setCustomerNumber(String customerNumber) {
		this.customerNumber = customerNumber;
	}
}
