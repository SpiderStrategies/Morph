/**
 * 
 */
package net.sf.morph.transform.model;

import java.util.List;

public interface Person {
	public String getName();
	public void setName(String name);
	public List getChildren();
	public void setChildren(List children);
	public Address getHomeAddress();
	public void setHomeAddress(Address homeAddress);
}